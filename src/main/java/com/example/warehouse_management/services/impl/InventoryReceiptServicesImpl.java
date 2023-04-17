package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.goods.GoodsAndBinLocationToCreateVoucher;
import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.purchase.PurchaseDetail;
import com.example.warehouse_management.models.purchase.PurchaseDetailPK;
import com.example.warehouse_management.models.purchase.PurchaseReceipt;
import com.example.warehouse_management.models.type.EStatusOfPurchasingGoods;
import com.example.warehouse_management.models.type.EStatusOfVoucher;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.models.voucher.ReceiptVoucherDetail;
import com.example.warehouse_management.models.voucher.RowLocationGoodsTemp;
import com.example.warehouse_management.models.warehouse.BinLocation;
import com.example.warehouse_management.payload.request.GoodsRequest;
import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;
import com.example.warehouse_management.payload.response.*;
import com.example.warehouse_management.repository.*;
import com.example.warehouse_management.services.*;
import com.example.warehouse_management.services.domain.UtillServies;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryReceiptServicesImpl implements InventoryReceiptServices {
    @Autowired
    InventoryReceiptVoucherRepository receiptVoucherRepository;
    @Autowired
    PartnerServices partnerServices;
    @Autowired
    BinLocationServices binLocationServices;
    @Autowired
    GoodsServices goodsServices;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryServices categoryServices;
    @Autowired
    BinLocationRepository binLocationRepository;
    @Autowired
    ReceiptVoucherDetailRepository receiptVoucherDetailRepository;
    @Autowired
    PurchaseReceiptServices purchaseReceiptServices;
    @Autowired
    PurchaseDetailRepository purchaseDetailRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public InventoryReceiptVoucherResponse createReceiptVoucher(String purchaseReceiptCode,ReceiptVoucherRequest receiptVoucherRequest) {
        List<GoodsAndBinLocationToCreateVoucher>  goodsAndBin = receiptVoucherRequest.getGoodsToCreateVoucher();
        for (int i = 0; i < goodsAndBin.size()-1; i++) {
            String binCode1 =goodsAndBin.get(i).getBinLocationCode();
            String binCode2 = goodsAndBin.get(i+1).getBinLocationCode();
            if(binCode1.equals(binCode2)){
                throw new ErrorException("Vị trí "+binCode2 +" đã được chọn" );
            }

        }

        PurchaseReceipt purchaseReceipt = purchaseReceiptServices.findPurchaseReceiptByCode(purchaseReceiptCode);
        User user = userRepository.findUserByEmail(receiptVoucherRequest.getEmail());
        InventoryReceiptVoucher inventoryReceiptVoucher = new InventoryReceiptVoucher();
        inventoryReceiptVoucher.setCode(generateReceiptVoucher());
        inventoryReceiptVoucher.setCreateDate(new Date());
        inventoryReceiptVoucher.setPurchaseReceipt(purchaseReceipt);
        inventoryReceiptVoucher.setCreatedBy(user);
        inventoryReceiptVoucher.setStatus(EStatusOfVoucher.NOT_YET_IMPORTED);
        InventoryReceiptVoucher savedVoucher= receiptVoucherRepository.save(inventoryReceiptVoucher);
        Set<ReceiptVoucherDetail> receiptVoucherDetailSet = new HashSet<>();
        for(GoodsAndBinLocationToCreateVoucher object: receiptVoucherRequest.getGoodsToCreateVoucher()){
            ReceiptVoucherDetail receiptVoucherDetail = new ReceiptVoucherDetail();
            Goods goods = goodsServices.findGoodByCode(object.getGoodsCode());
            BinLocation binLocation = binLocationServices.findRowLocationByCode(object.getBinLocationCode());
            PurchaseDetailPK purchaseDetailPK = new PurchaseDetailPK();
            purchaseDetailPK.setPurchaseId(purchaseReceipt.getId());
            purchaseDetailPK.setGoodsId(goods.getId());
            PurchaseDetail purchaseDetail = purchaseDetailRepository.findByPurchaseDetailPK(purchaseDetailPK);
            double volume = goods.getVolume() * object.getQuantity();
            if(binLocation.getRemainingVolume()<volume){
               int maxQuantity = (int) (binLocation.getRemainingVolume()/ goods.getVolume());
                receiptVoucherDetail.setQuantity(maxQuantity);
                purchaseDetail.setQuantityActual(maxQuantity);
                purchaseDetail.setStatus(EStatusOfPurchasingGoods.NOT_YET_CREATED);
            }
            else{
                receiptVoucherDetail.setQuantity(object.getQuantity());
                purchaseDetail.setQuantityActual(object.getQuantity());
                purchaseDetail.setStatus(EStatusOfPurchasingGoods.CREATED);
            }
            purchaseDetailRepository.save(purchaseDetail);
            receiptVoucherDetail.setGoodsCode(goods.getCode());
            receiptVoucherDetail.setBinLocation(binLocation);
            receiptVoucherDetail.setInventoryReceiptVoucher(savedVoucher);
            receiptVoucherDetailSet.add(receiptVoucherDetail);
        }
        savedVoucher.setReceiptVoucherDetails(receiptVoucherDetailSet);
        receiptVoucherRepository.save(savedVoucher);

        return mapperInventoryReceiptVoucher(savedVoucher);

    }

    @Override
    public List<BinLocationResponse> putTheGoodsOnShelf(String receiptVoucherCode) {
        List<BinLocation> binList = new ArrayList<>();
        InventoryReceiptVoucher inventoryReceiptVoucher = receiptVoucherRepository.findByCode(receiptVoucherCode);
        if(inventoryReceiptVoucher.getStatus().equals(EStatusOfVoucher.IMPORTED))
            throw new ErrorException("Phiếu nhập "+inventoryReceiptVoucher.getCode() + "đã được nhập hàng lên kệ");
        inventoryReceiptVoucher.setStatus(EStatusOfVoucher.IMPORTED);
        if (ObjectUtils.isEmpty(inventoryReceiptVoucher))
            throw new NotFoundGlobalException("Không tìm thấy phiếu nhập " + receiptVoucherCode);
        for (ReceiptVoucherDetail detail : inventoryReceiptVoucher.getReceiptVoucherDetails()) {
//            Goods goods = detail.getGoods();
            Goods goods = null;
            BinLocation bin = detail.getBinLocation();
            int quantity = detail.getQuantity();
            int maxCapacity = (int) (bin.getVolume() / goods.getVolume());
            int currentCapacity = bin.getCurrentCapacity() + quantity;
            double remainingVolume = bin.getRemainingVolume() - (goods.getVolume() * quantity);
            bin.setGoods(goods);
            bin.setMaxCapacity(maxCapacity);
            bin.setCurrentCapacity(currentCapacity);
            bin.setRemainingVolume(remainingVolume);
            if (remainingVolume <= 0)
                bin.setStatus(EStatusStorage.FULL);
            else {
                bin.setStatus(EStatusStorage.AVAILABLE);
            }
            BinLocation saveBin = binLocationRepository.save(bin);
            binList.add(saveBin);
        }
        List<BinLocationResponse> binLocationRespons = binList.stream().map(item ->
                binLocationServices.mapperRowLocation(item)
        ).collect(Collectors.toList());
        receiptVoucherRepository.save(inventoryReceiptVoucher);
        return binLocationRespons;
    }

    @Override
    public Page<InventoryReceiptVoucherResponse> getPageSortedByDate(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<InventoryReceiptVoucher> vouchers = receiptVoucherRepository.findAllBySortedCreateDate(pageable);
        Page<InventoryReceiptVoucherResponse> pages = new PageImpl<InventoryReceiptVoucherResponse>(vouchers.getContent()
                .stream().map(this::mapperInventoryReceiptVoucher).collect(Collectors.toList()), pageable,
                vouchers.getTotalElements());
        return pages;

    }

    @Override
    public List<InventoryReceiptVoucherResponse> findAll() {
        List<InventoryReceiptVoucherResponse> responseList = getAll().stream()
                .map(item->mapperInventoryReceiptVoucher(item)).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<InventoryReceiptVoucher> getAll() {
        return receiptVoucherRepository.findAll();
    }

    @Override
    public InventoryReceiptVoucherResponse getReceiptVoucherByCode(String voucherCode) {
        InventoryReceiptVoucher voucher = receiptVoucherRepository.findInventoryReceiptVoucherByCode(voucherCode);
        if(ObjectUtils.isEmpty(voucher))
            throw new NotFoundGlobalException("Không tìm thấy phiếu nhập "+voucherCode);
        return mapperInventoryReceiptVoucher(voucher) ;
    }

    public InventoryReceiptVoucherResponse mapperInventoryReceiptVoucher(InventoryReceiptVoucher inventoryReceiptVoucher) {
        String status ="";
        switch (inventoryReceiptVoucher.getStatus()){
            case IMPORTED:
                status="Đã nhập lên kệ";
                break;
            case NOT_YET_IMPORTED:
                status="Chưa nhập lên kệ";
                break;

        }
        InventoryReceiptVoucherResponse response = modelMapper.map(inventoryReceiptVoucher, InventoryReceiptVoucherResponse.class);
        response.setCreatedBy(inventoryReceiptVoucher.getCreatedBy().getFullName());
        response.setStatus(status);
        response.setPartner(modelMapper.map(inventoryReceiptVoucher.getPurchaseReceipt().getPartner(), PartnerResponse.class));
        Set<ReceiptVoucherDetailResponse> detailResponseSet = inventoryReceiptVoucher
                .getReceiptVoucherDetails().stream().map(item -> mapperReceiptVoucherDetailResponse(item))
                .collect(Collectors.toSet());
        response.setReceiptVoucherDetails(detailResponseSet);
        return response;
    }

    private String generateReceiptVoucher() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dateFormate = sdf.format(new Date());
        String receiptCode = "PN" + dateFormate;
        InventoryReceiptVoucher inventoryReceiptVoucher = receiptVoucherRepository.findTopByOrderByIdDesc();
        if (inventoryReceiptVoucher == null) {
            return receiptCode + 1;
        }
        long id = inventoryReceiptVoucher.getId();
        return receiptCode + (id + 1);
    }

    private double[] createArraySize(double width, double height, double length) {
        double[] size = {width, height, length};
        Arrays.sort(size);
        return size;

    }

    private boolean compareSizeShelfAndGoods(double[] sizeShelf, double[] sizeGoods, String goodsName) {
        if (sizeShelf[0] >= sizeGoods[0] && sizeShelf[1] >= sizeGoods[1] && sizeShelf[2] >= sizeGoods[2])
            return true;
        else
            throw new ErrorException(goodsName + "không thể chứa");
    }

    private InventoryReceiptVoucher createObjectReceiptVoucher(Partner partner, User user) {
        InventoryReceiptVoucher inventoryReceiptVoucher = new InventoryReceiptVoucher();
       // inventoryReceiptVoucher.setPartner(partner);
        inventoryReceiptVoucher.setCreatedBy(user);
        inventoryReceiptVoucher.setCreateDate(new Date());
        inventoryReceiptVoucher.setCode(generateReceiptVoucher());
        inventoryReceiptVoucher.setStatus(EStatusOfVoucher.NOT_YET_IMPORTED);
        InventoryReceiptVoucher saveInventoryReceiptVoucher = receiptVoucherRepository.save(inventoryReceiptVoucher);
        return saveInventoryReceiptVoucher;
    }

    private ReceiptVoucherDetail createObjectReceiptVoucherDetail(InventoryReceiptVoucher saveInventoryReceiptVoucher, RowLocationGoodsTemp rowLocationGoodsTemp) {
        ReceiptVoucherDetail receiptVoucherDetail = new ReceiptVoucherDetail();
        receiptVoucherDetail.setInventoryReceiptVoucher(saveInventoryReceiptVoucher);
//        receiptVoucherDetail.setGoods(goodsServices.createGoods(rowLocationGoodsTemp.getGoodsRequest()));
        receiptVoucherDetail.setQuantity(rowLocationGoodsTemp.getGoodsRequest().getQuantity());
        receiptVoucherDetail.setBinLocation(rowLocationGoodsTemp.getBinLocation());
        receiptVoucherDetailRepository.save(receiptVoucherDetail);

        return receiptVoucherDetail;
    }
    private ReceiptVoucherDetailResponse mapperReceiptVoucherDetailResponse(ReceiptVoucherDetail receiptVoucherDetail){

        ReceiptVoucherDetailResponse detailResponse = new ReceiptVoucherDetailResponse(goodsServices.mapperGoods(
                goodsServices.findGoodByCode(receiptVoucherDetail.getGoodsCode())), UtillServies.mapperLocationInWarehouse(receiptVoucherDetail.getBinLocation()),receiptVoucherDetail.getQuantity());
        return detailResponse;
    }

    private void process(GoodsRequest goodsRequest, double volumeGoods, double volumeRowLocation, List<RowLocationGoodsTemp> rowLocationGoodsTempList, List<String> rowLocationCodeSelected) {
        double numberRowLocation = volumeGoods / volumeRowLocation;
        if (numberRowLocation <= 1) {
            BinLocation binSelected = binLocationRepository.findTopOneByStatusEmptyAndRemainingVolumeGreaterThanEqual(volumeGoods, rowLocationCodeSelected);
            RowLocationGoodsTemp rowLocationGoodsTemp = new RowLocationGoodsTemp(binSelected, goodsRequest);
            rowLocationGoodsTempList.add(rowLocationGoodsTemp);
            //selected
            rowLocationCodeSelected.add(binSelected.getCode());
        }
            // Trường hợp cần nhiều vị trí
        else {
            if (volumeGoods % volumeRowLocation > 0) {
                numberRowLocation = (int) numberRowLocation + 1;
            }
            List<BinLocation> binSelectedList = binLocationRepository.findTopNByStatusAndRemainingVolume(goodsRequest.getVolume(), (int) numberRowLocation, rowLocationCodeSelected);
                int maxGoodsShelfContain = (int) (volumeRowLocation / goodsRequest.getVolume());
                int quantity = goodsRequest.getQuantity();
                for (BinLocation rl : binSelectedList) {
                    RowLocationGoodsTemp rowLocationGoodsTemp = new RowLocationGoodsTemp();
                    rowLocationGoodsTemp.setBinLocation(rl);
                    if (quantity <= maxGoodsShelfContain) {
                        goodsRequest.setQuantity(quantity);
                    } else {
                        goodsRequest.setQuantity(maxGoodsShelfContain);
                    }
                    rowLocationGoodsTemp.setGoodsRequest(new GoodsRequest(goodsRequest.getName(),
                            goodsRequest.getHeight(), goodsRequest.getWidth(), goodsRequest.getLength(),
                            goodsRequest.getUnit(), goodsRequest.getQuantity(), goodsRequest.getCategoryCode(), goodsRequest.getVolume()));
                    rowLocationGoodsTempList.add(rowLocationGoodsTemp);
                    rowLocationCodeSelected.add(rl.getCode());
                    quantity = quantity - maxGoodsShelfContain;
                }
            }

    }
}


