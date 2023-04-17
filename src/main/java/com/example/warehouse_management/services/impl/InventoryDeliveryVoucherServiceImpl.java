package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.goods.GoodsDelivery;
import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.selling.SaleReceipt;
import com.example.warehouse_management.models.type.EStatusOfVoucher;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.*;
import com.example.warehouse_management.models.warehouse.BinLocation;
import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.request.GoodDeliveryRequest;
import com.example.warehouse_management.payload.response.*;
import com.example.warehouse_management.repository.InventoryDeliveryVoucherRepository;
import com.example.warehouse_management.repository.BinLocationRepository;
import com.example.warehouse_management.repository.UserRepository;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class InventoryDeliveryVoucherServiceImpl implements InventoryDeliveryVoucherServices {
    @Autowired
    InventoryDeliveryVoucherRepository deliveryVoucherRepository;
    @Autowired
    GoodsServices goodsServices;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartnerServices partnerServices;
    @Autowired
    BinLocationServices binLocationServices;
    @Autowired
    BinLocationRepository binLocationRepository;

    @Autowired
    SaleReceiptServices saleReceiptServices;

    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public InventoryDeliveryVoucherResponse createInventoryDeliveryVoucher(String saleReceiptCode,DeliveryVoucherRequest deliveryVoucherRequest) {
        SaleReceipt saleReceipt = saleReceiptServices.findSaleReceiptByCode(saleReceiptCode);
        InventoryDeliveryVoucher inventoryDeliveryVoucher = new InventoryDeliveryVoucher();
        inventoryDeliveryVoucher.setSaleReceipt(saleReceipt);
        User user = userRepository.findUserByEmail(deliveryVoucherRequest.getEmail());
        Set<DeliveryVoucherDetail> deliveryVoucherDetails = new HashSet<>();
        List<RowLocationGoodsDeliveryTemp> rowLocationGoodsDeliveryTempList = new ArrayList<>();
        List<GoodsDelivery> goodsDeliveryList = deliveryVoucherRequest.getGoodsRequests().stream()
                .map(item->new GoodsDelivery(goodsServices.findGoodByCode(item.getGoodCode()), item.getQuantity())).collect(Collectors.toList());
        for (GoodsDelivery goodDelivery:goodsDeliveryList) {
            //tìm những vị trí sao cho số lượng current >=quantity request
            int sumQuantityOfGoods= binLocationServices.getSumCurrentCapacityByGoodsName(goodDelivery.getGoods().getName()).intValue();
            if(goodDelivery.getQuantity()>sumQuantityOfGoods){
                throw new ErrorException("Không đủ số lượng để xuất");
            }
            List<BinLocation> binList = binLocationServices.findAllByGoodsNameEnoughToExport(goodDelivery.getGoods().getName(),goodDelivery.getQuantity());
            for (BinLocation rl: binList) {
                int currentQuantity=rl.getCurrentCapacity();
                if(currentQuantity>=goodDelivery.getQuantity()){
                    System.out.println("Tao phieu xuat");
                    rowLocationGoodsDeliveryTempList.add(new RowLocationGoodsDeliveryTemp(rl,goodDelivery));
                }else{
                    if(sumQuantityOfGoods<rl.getCurrentCapacity()) {
                        GoodsDelivery goodsDelivery = new GoodsDelivery(goodDelivery.getGoods(),sumQuantityOfGoods);
                        rowLocationGoodsDeliveryTempList.add(new RowLocationGoodsDeliveryTemp(rl,goodsDelivery));
                    }else{
                        GoodsDelivery goodsDelivery = new GoodsDelivery(goodDelivery.getGoods(),currentQuantity);
                        rowLocationGoodsDeliveryTempList.add(new RowLocationGoodsDeliveryTemp(rl,goodsDelivery));
                        sumQuantityOfGoods = sumQuantityOfGoods -goodDelivery.getQuantity();
                    }

                }

            }
        }
        inventoryDeliveryVoucher.setCreateDate(new Date());
        inventoryDeliveryVoucher.setCreatedBy(user);
        inventoryDeliveryVoucher.setCode(generateDeliveryVoucher());
        inventoryDeliveryVoucher.setStatus(EStatusOfVoucher.NOT_YET_EXPORTED);
        InventoryDeliveryVoucher deliveryVoucherSave = deliveryVoucherRepository.save(inventoryDeliveryVoucher);
        for (RowLocationGoodsDeliveryTemp item:rowLocationGoodsDeliveryTempList) {
            DeliveryVoucherDetail deliveryVoucherDetail = new DeliveryVoucherDetail();
            deliveryVoucherDetail.setGoodsCode(item.getGoodsDelivery().getGoods().getCode());
            deliveryVoucherDetail.setQuantity(item.getGoodsDelivery().getQuantity());
            deliveryVoucherDetail.setBinLocation(item.getBinLocation());
            deliveryVoucherDetail.setInventoryDeliveryVoucher(deliveryVoucherSave);
            deliveryVoucherDetails.add(deliveryVoucherDetail);
        }
        deliveryVoucherSave.setDeliveryVoucherDetails(deliveryVoucherDetails);
        return mapperInventoryDeliveryVoucher(deliveryVoucherRepository.save(deliveryVoucherSave));
    }

    @Override
    public List<BinLocationResponse> exportGoods(String deliveryVoucherCode) {
        InventoryDeliveryVoucher inventoryDeliveryVoucher = deliveryVoucherRepository.findByCode(deliveryVoucherCode);
        if(inventoryDeliveryVoucher.getStatus().equals(EStatusOfVoucher.EXPORTED))
            throw new ErrorException("Phiếu xuất" + deliveryVoucherCode + "đã được xuất");
        List<BinLocation> binList = new ArrayList<>();
        if (ObjectUtils.isEmpty(inventoryDeliveryVoucher))
            throw new NotFoundGlobalException("Không tìm thấy phiếu xuất "+ deliveryVoucherCode);
        inventoryDeliveryVoucher.setStatus(EStatusOfVoucher.EXPORTED);
        for (DeliveryVoucherDetail detail:inventoryDeliveryVoucher.getDeliveryVoucherDetails()) {
            Goods goods =goodsServices.findGoodByCode(detail.getGoodsCode());
            BinLocation bin = detail.getBinLocation();
            int currentCapacity= bin.getCurrentCapacity();
            double remainingVolume= bin.getRemainingVolume();
            bin.setCurrentCapacity(currentCapacity-detail.getQuantity());
            if(currentCapacity-detail.getQuantity()==0){
                bin.setStatus(EStatusStorage.EMPTY);
                bin.setGoods(null);
                bin.setRemainingVolume(bin.getVolume());
            }
            else{
                bin.setRemainingVolume(remainingVolume+ goods.getVolume()*detail.getQuantity());
            }
            BinLocation bin1 = binLocationRepository.save(bin);
            binList.add(bin1);

        }
        List<BinLocationResponse> binLocationResponses = binList.stream().map(item ->
                binLocationServices.mapperRowLocation(item)).collect(Collectors.toList());
        deliveryVoucherRepository.save(inventoryDeliveryVoucher);
        return binLocationResponses;
    }

    @Override
    public InventoryDeliveryVoucherResponse getDeliveryVoucherByCode(String deliveryVoucherCode) {
        InventoryDeliveryVoucher deliveryVoucher = deliveryVoucherRepository.findByCode(deliveryVoucherCode);
        if(ObjectUtils.isEmpty(deliveryVoucher))
            throw new NotFoundGlobalException("Không tìm thấy phiếu xuất "+deliveryVoucherCode);
        return mapperInventoryDeliveryVoucher(deliveryVoucher);
    }

    @Override
    public Page<InventoryDeliveryVoucherResponse> getPageSortedByDate(Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<InventoryDeliveryVoucher> vouchers= deliveryVoucherRepository.getPage(pageable);
        Page<InventoryDeliveryVoucherResponse> pages = new PageImpl<InventoryDeliveryVoucherResponse>(vouchers.getContent()
                .stream().map(this::mapperInventoryDeliveryVoucher).collect(Collectors.toList()), pageable,
                vouchers.getTotalElements());
        return pages;
    }

    @Override
    public List<InventoryDeliveryVoucherResponse> getAll() {
        List<InventoryDeliveryVoucherResponse> responseList = deliveryVoucherRepository.findAll().stream()
                .map(item->mapperInventoryDeliveryVoucher(item)).collect(Collectors.toList());
        return responseList;
    }

    private String generateDeliveryVoucher() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dateFormat = sdf.format(new Date());
        String deliveryCode = "PX" + dateFormat;
        InventoryDeliveryVoucher inventoryDeliveryVoucher = deliveryVoucherRepository.findTopByOrderByIdDesc();
        if (inventoryDeliveryVoucher == null) {
            return deliveryCode + 1;
        }
        long id = inventoryDeliveryVoucher.getId();
        return deliveryCode + (id + 1);
    }
    private InventoryDeliveryVoucherResponse mapperInventoryDeliveryVoucher(InventoryDeliveryVoucher deliveryVoucher){
        String status ="";
        switch (deliveryVoucher.getStatus()){
            case EXPORTED:
                status="Đã xuất";
                break;
            case NOT_YET_EXPORTED:
                status="Chưa xuất";
                break;

        }
        Set<DeliveryVoucherDetailResponse>  detailResponseSet = deliveryVoucher.getDeliveryVoucherDetails().stream().map(item ->mapperDeliveryVoucherDetailResponse(item))
                .collect(Collectors.toSet());
        InventoryDeliveryVoucherResponse inventoryDeliveryVoucherResponse = new InventoryDeliveryVoucherResponse();
        inventoryDeliveryVoucherResponse = modelMapper.map(deliveryVoucher,InventoryDeliveryVoucherResponse.class);
//        inventoryDeliveryVoucherResponse.setPartner(modelMapper.map(deliveryVoucher.getPartner(), PartnerResponse.class));
        inventoryDeliveryVoucherResponse.setCreatedBy(deliveryVoucher.getCreatedBy().getFullName());
        inventoryDeliveryVoucherResponse.setDetails(detailResponseSet);
        inventoryDeliveryVoucherResponse.setStatus(status);
        return inventoryDeliveryVoucherResponse;
    }
    private DeliveryVoucherDetailResponse mapperDeliveryVoucherDetailResponse(DeliveryVoucherDetail deliveryVoucherDetail){
        DeliveryVoucherDetailResponse detailResponse = new DeliveryVoucherDetailResponse(goodsServices.mapperGoods(
                null), UtillServies.mapperLocationInWarehouse(deliveryVoucherDetail.getBinLocation()),deliveryVoucherDetail.getQuantity());
        return detailResponse;
    }

}
