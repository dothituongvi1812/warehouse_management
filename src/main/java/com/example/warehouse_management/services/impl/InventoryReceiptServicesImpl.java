package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotEnoughSpaceException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.DeliveryVoucherDetail;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.models.voucher.ReceiptVoucherDetail;
import com.example.warehouse_management.models.voucher.RowLocationGoodsTemp;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.request.GoodsRequest;
import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;
import com.example.warehouse_management.payload.response.*;
import com.example.warehouse_management.repository.InventoryReceiptVoucherRepository;
import com.example.warehouse_management.repository.ReceiptVoucherDetailRepository;
import com.example.warehouse_management.repository.RowLocationRepository;
import com.example.warehouse_management.repository.UserRepository;
import com.example.warehouse_management.services.*;
import com.example.warehouse_management.services.domain.UtillServies;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
    RowLocationServices rowLocationServices;
    @Autowired
    GoodsServices goodsServices;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryServices categoryServices;
    @Autowired
    RowLocationRepository rowLocationRepository;
    @Autowired
    ReceiptVoucherDetailRepository receiptVoucherDetailRepository;
    @Autowired
    GoodsServicesImpl goodsServicesImpl;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public InventoryReceiptVoucherResponse createReceiptVoucher(ReceiptVoucherRequest receiptVoucherRequest) {
        // validate good_request
        RowLocation rowLocation = rowLocationRepository.findTopByOrderByIdDesc();
        double volumeRowLocation = rowLocation.getVolume();
        receiptVoucherRequest.getGoodsRequests().stream()
                .filter(goodsRequest -> compareSizeShelfAndGoods(
                        createArraySize(rowLocation.getWidth(), rowLocation.getHeight(), rowLocation.getLength()),
                        createArraySize(goodsRequest.getWidth(), goodsRequest.getHeight(), goodsRequest.getLength()),
                        goodsRequest.getName()))
                .collect(Collectors.toList());

        //process
        List<RowLocationGoodsTemp> rowLocationGoodsTempList = new ArrayList<>();
        List<String> rowLocationCodeSelected = new ArrayList<>();
        rowLocationCodeSelected.add("");
        for (GoodsRequest goodsRequest : receiptVoucherRequest.getGoodsRequests()) {
            List<RowLocation> rowLocationList = rowLocationRepository.findByGoodsName(goodsRequest.getName(), rowLocationCodeSelected);
            double volumeGoods = goodsRequest.getVolume() * goodsRequest.getQuantity();
            if (CollectionUtils.isEmpty(rowLocationList)) {
                process(goodsRequest,volumeGoods,volumeRowLocation,rowLocationGoodsTempList,rowLocationCodeSelected);
            }
            //trường hợp sp muốn nhập có trên kệ rồi
            else {
                int quantityRequest = goodsRequest.getQuantity();
                for (RowLocation rl : rowLocationList) {
                    if (rl.getRemainingVolume() >= volumeGoods) {
                        RowLocationGoodsTemp rowLocationGoodsTemp = new RowLocationGoodsTemp(rl, goodsRequest);
                        rowLocationGoodsTempList.add(rowLocationGoodsTemp);
                        rowLocationCodeSelected.add(rl.getCode());
                    } else {
                        int quantityRefill = rl.getMaxCapacity() - rl.getCurrentCapacity();
                        RowLocationGoodsTemp rowLocationGoodsTemp = new RowLocationGoodsTemp(rl,
                                new GoodsRequest(goodsRequest.getName(),
                                        goodsRequest.getHeight(), goodsRequest.getWidth(), goodsRequest.getLength(),
                                        goodsRequest.getUnit(), quantityRefill, goodsRequest.getCategoryCode(), goodsRequest.getVolume()));
                        rowLocationGoodsTempList.add(rowLocationGoodsTemp);
                        rowLocationCodeSelected.add(rl.getCode());
                        quantityRequest = quantityRequest - quantityRefill;
                    }
                }
                if (quantityRequest > 0) {
                    System.out.println("Tìm vị trí khác để set vào");
                    double volume1SP = goodsRequest.getVolume();
                    int maxCapacity = (int) (volumeRowLocation / volume1SP);
                    if (quantityRequest <= maxCapacity) {
                        RowLocation rowLocation1 = rowLocationRepository.findTopOneByStatusTrong(rowLocationCodeSelected);
                        RowLocationGoodsTemp rowLocationGoodsTemp = new RowLocationGoodsTemp(rowLocation1,
                                new GoodsRequest(goodsRequest.getName(),
                                        goodsRequest.getHeight(), goodsRequest.getWidth(), goodsRequest.getLength(),
                                        goodsRequest.getUnit(), quantityRequest, goodsRequest.getCategoryCode(), goodsRequest.getVolume()));
                        rowLocationGoodsTempList.add(rowLocationGoodsTemp);
                        rowLocationCodeSelected.add(rowLocation1.getCode());
                    } else {
                        double volumeGoods1= quantityRequest*volume1SP;
                        GoodsRequest goodsRequest1 = new GoodsRequest(goodsRequest.getName(),
                                goodsRequest.getHeight(), goodsRequest.getWidth(), goodsRequest.getLength(),
                                goodsRequest.getUnit(), quantityRequest, goodsRequest.getCategoryCode(), goodsRequest.getVolume());
                        process(goodsRequest1,volumeGoods1,volumeRowLocation,rowLocationGoodsTempList,rowLocationCodeSelected);
                    }

                }

            }
        }

        Partner partner = partnerServices.addPartner(receiptVoucherRequest.getPartnerRequest());
        User user = userRepository.findUserByEmail(receiptVoucherRequest.getEmail());
        InventoryReceiptVoucher saveInventoryReceiptVoucher = createObjectReceiptVoucher(partner, user);

        Set<ReceiptVoucherDetail> receiptVoucherDetailSet = new HashSet<>();
        for (RowLocationGoodsTemp item : rowLocationGoodsTempList) {
            ReceiptVoucherDetail receiptVoucherDetail = createObjectReceiptVoucherDetail(saveInventoryReceiptVoucher, item);
            receiptVoucherDetailSet.add(receiptVoucherDetail);
        }
        saveInventoryReceiptVoucher.setReceiptVoucherDetails(receiptVoucherDetailSet);
        receiptVoucherRepository.save(saveInventoryReceiptVoucher);
        return mapperInventoryReceiptVoucher(saveInventoryReceiptVoucher);
    }

    @Override
    public List<RowLocationResponse> putTheGoodsOnShelf(String receiptVoucherCode) {
        List<RowLocation> rowLocationList = new ArrayList<>();
        InventoryReceiptVoucher inventoryReceiptVoucher = receiptVoucherRepository.findByCode(receiptVoucherCode);
        if (ObjectUtils.isEmpty(inventoryReceiptVoucher))
            throw new NotFoundGlobalException("Không tìm thấy phiếu nhập " + receiptVoucherCode);
        for (ReceiptVoucherDetail detail : inventoryReceiptVoucher.getReceiptVoucherDetails()) {
            Goods goods = detail.getGoods();
            RowLocation rowLocation = detail.getRowLocation();
            int quantity = detail.getQuantity();
            int maxCapacity = (int) (rowLocation.getVolume() / goods.getVolume());
            int currentCapacity = rowLocation.getCurrentCapacity() + quantity;
            double remainingVolume = rowLocation.getRemainingVolume() - (goods.getVolume() * quantity);
            rowLocation.setGoods(goods);
            rowLocation.setMaxCapacity(maxCapacity);
            rowLocation.setCurrentCapacity(currentCapacity);
            rowLocation.setRemainingVolume(remainingVolume);
            if (remainingVolume <= 0)
                rowLocation.setStatus(EStatusStorage.DADAY);
            else {
                rowLocation.setStatus(EStatusStorage.CONCHO);
            }
            RowLocation saveRowLocation = rowLocationRepository.save(rowLocation);
            rowLocationList.add(saveRowLocation);
        }
        List<RowLocationResponse> rowLocationResponses = rowLocationList.stream().map(item ->
                rowLocationServices.mapperRowLocation(item)
        ).collect(Collectors.toList());
        return rowLocationResponses;
    }

    @Override
    public List<InventoryReceiptVoucherResponse> getAllSortedByDate() {
        List<InventoryReceiptVoucherResponse> responseList = getAll().stream()
                .map(item->mapperInventoryReceiptVoucher(item)).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<InventoryReceiptVoucher> getAll() {
        return receiptVoucherRepository.findAllBySortedCreateDate();
    }

    public InventoryReceiptVoucherResponse mapperInventoryReceiptVoucher(InventoryReceiptVoucher inventoryReceiptVoucher) {

        InventoryReceiptVoucherResponse response = modelMapper.map(inventoryReceiptVoucher, InventoryReceiptVoucherResponse.class);
        response.setCreatedBy(inventoryReceiptVoucher.getCreatedBy().getFullName());
        response.setPartner(modelMapper.map(inventoryReceiptVoucher.getPartner(), PartnerResponse.class));
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
            throw new NotEnoughSpaceException(goodsName + "không thể chứa");
    }

    private InventoryReceiptVoucher createObjectReceiptVoucher(Partner partner, User user) {
        InventoryReceiptVoucher inventoryReceiptVoucher = new InventoryReceiptVoucher();
        inventoryReceiptVoucher.setPartner(partner);
        inventoryReceiptVoucher.setCreatedBy(user);
        inventoryReceiptVoucher.setCreateDate(new Date());
        inventoryReceiptVoucher.setCode(generateReceiptVoucher());
        InventoryReceiptVoucher saveInventoryReceiptVoucher = receiptVoucherRepository.save(inventoryReceiptVoucher);
        return saveInventoryReceiptVoucher;
    }

    private ReceiptVoucherDetail createObjectReceiptVoucherDetail(InventoryReceiptVoucher saveInventoryReceiptVoucher, RowLocationGoodsTemp rowLocationGoodsTemp) {
        ReceiptVoucherDetail receiptVoucherDetail = new ReceiptVoucherDetail();
        receiptVoucherDetail.setInventoryReceiptVoucher(saveInventoryReceiptVoucher);
        receiptVoucherDetail.setGoods(goodsServices.createGoods(rowLocationGoodsTemp.getGoodsRequest()));
        receiptVoucherDetail.setQuantity(rowLocationGoodsTemp.getGoodsRequest().getQuantity());
        receiptVoucherDetail.setRowLocation(rowLocationGoodsTemp.getRowLocation());
        receiptVoucherDetailRepository.save(receiptVoucherDetail);

        return receiptVoucherDetail;
    }
    private ReceiptVoucherDetailResponse mapperReceiptVoucherDetailResponse(ReceiptVoucherDetail receiptVoucherDetail){
        ReceiptVoucherDetailResponse detailResponse = new ReceiptVoucherDetailResponse(goodsServicesImpl.mapperGoodResponse(
                receiptVoucherDetail.getGoods()), UtillServies.mapperLocationInWarehouse(receiptVoucherDetail.getRowLocation()),receiptVoucherDetail.getQuantity());
        return detailResponse;
    }

    private void process(GoodsRequest goodsRequest, double volumeGoods, double volumeRowLocation, List<RowLocationGoodsTemp> rowLocationGoodsTempList, List<String> rowLocationCodeSelected) {
        double numberRowLocation = volumeGoods / volumeRowLocation;
        if (numberRowLocation <= 1) {
            RowLocation rowLocationSelected = rowLocationRepository.findTopOneByStatusTrongAndRemainingVolumeGreaterThanEqual(volumeGoods, rowLocationCodeSelected);
            RowLocationGoodsTemp rowLocationGoodsTemp = new RowLocationGoodsTemp(rowLocationSelected, goodsRequest);
            rowLocationGoodsTempList.add(rowLocationGoodsTemp);
            //selected
            rowLocationCodeSelected.add(rowLocationSelected.getCode());
        }
            // Trường hợp cần nhiều vị trí
        else {
            if (volumeGoods % volumeRowLocation > 0) {
                numberRowLocation = (int) numberRowLocation + 1;
            }
            List<RowLocation> rowLocationSelectedList = rowLocationRepository.findTopNByStatusAndRemainingVolume(goodsRequest.getVolume(), (int) numberRowLocation, rowLocationCodeSelected);
                int maxGoodsShelfContain = (int) (volumeRowLocation / goodsRequest.getVolume());
                int quantity = goodsRequest.getQuantity();
                for (RowLocation rl : rowLocationSelectedList) {
                    RowLocationGoodsTemp rowLocationGoodsTemp = new RowLocationGoodsTemp();
                    rowLocationGoodsTemp.setRowLocation(rl);
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


