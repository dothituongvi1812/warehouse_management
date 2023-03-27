package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotEnoughSpaceException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.goods.GoodsReceipt;
import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.type.EUnit;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.models.voucher.ReceiptVoucherDetail;
import com.example.warehouse_management.models.voucher.RowLocationGoodsTemp;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.models.warehouse.Warehouse;
import com.example.warehouse_management.payload.request.GoodsRequest;
import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryReceiptVoucherResponse;
import com.example.warehouse_management.payload.response.LocationInWarehouse;
import com.example.warehouse_management.payload.response.ReceiptVoucherDetailResponse;
import com.example.warehouse_management.repository.InventoryReceiptVoucherRepository;
import com.example.warehouse_management.repository.ReceiptVoucherDetailRepository;
import com.example.warehouse_management.repository.RowLocationRepository;
import com.example.warehouse_management.repository.UserRepository;
import com.example.warehouse_management.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

        List<RowLocationGoodsTemp> rowLocationGoodsTempList = new ArrayList<>();
        List<String> goodsNameSelected = new ArrayList<>();
        List<String> rowLocationCodeSelected = new ArrayList<>();


        for ( GoodsRequest goodsRequest : receiptVoucherRequest.getGoodsRequests()) {
            List<RowLocation> rowLocationList = rowLocationRepository.findByGoodsName(goodsRequest.getName());
            double volumeGoods = goodsRequest.getVolume() * goodsRequest.getQuantity();
            if (CollectionUtils.isEmpty(rowLocationList)) {
                double numberRowLocation = volumeGoods / volumeRowLocation;
                if (numberRowLocation <= 1) {
                    RowLocation rowLocationSelected = rowLocationRepository.findTopOneByStatusTrongAndRemainingVolumeGreaterThanEqual(volumeGoods);
                    RowLocationGoodsTemp rowLocationGoodsTemp = new RowLocationGoodsTemp(rowLocationSelected.getCode(), goodsRequest);
                    rowLocationGoodsTempList.add(rowLocationGoodsTemp);
                    //selected
                    goodsNameSelected.add(goodsRequest.getName());
                    rowLocationCodeSelected.add(rowLocationSelected.getCode());
                }
                // Trường hợp cần nhiều vị trí
                else {
                    if (volumeGoods % volumeRowLocation > 0) {
                        numberRowLocation = (int) numberRowLocation + 1;
                    }
                    List<RowLocation> rowLocationSelectedList = rowLocationRepository
                            .findTopNByStatusAndRemainingVolume(goodsRequest.getVolume(), (int) numberRowLocation);
                    int maxGoodsShelfContain = (int) (volumeRowLocation / goodsRequest.getVolume());
                    System.out.println("maxGoodsShelfContain " + maxGoodsShelfContain);
                    for (RowLocation rl : rowLocationSelectedList) {
                        do {
                            System.out.println("Them");
                        } while (goodsRequest.getQuantity() == 0);
                    }
                }

            }
        }

        Partner partner = partnerServices.addPartner(receiptVoucherRequest.getPartnerRequest());
        User user = userRepository.findUserByEmail(receiptVoucherRequest.getEmail());
        InventoryReceiptVoucher inventoryReceiptVoucher = new InventoryReceiptVoucher();
        inventoryReceiptVoucher.setPartner(partner);
        inventoryReceiptVoucher.setCreatedBy(user);
        inventoryReceiptVoucher.setCreateDate(new Date());
        inventoryReceiptVoucher.setCode(generateReceiptVoucher());
        InventoryReceiptVoucher saveInventoryReceiptVoucher = receiptVoucherRepository.save(inventoryReceiptVoucher);
        Set<ReceiptVoucherDetail> receiptVoucherDetailSet = new HashSet<>();
        for (RowLocationGoodsTemp item : rowLocationGoodsTempList) {
            ReceiptVoucherDetail receiptVoucherDetail = new ReceiptVoucherDetail();
            receiptVoucherDetail.setInventoryReceiptVoucher(saveInventoryReceiptVoucher);
            receiptVoucherDetail.setGoods(goodsServices.createGoods(item.getGoodsRequest()));
            receiptVoucherDetail.setQuantity(item.getGoodsRequest().getQuantity());
            receiptVoucherDetail.setRowLocation(rowLocationServices.findRowLocationByCode(item.getRowLocationOfShelfCode()));
            receiptVoucherDetailRepository.save(receiptVoucherDetail);
            receiptVoucherDetailSet.add(receiptVoucherDetail);

        }
        inventoryReceiptVoucher.setReceiptVoucherDetails(receiptVoucherDetailSet);

        return mapperInventoryReceiptVoucher(inventoryReceiptVoucher);
    }

    public InventoryReceiptVoucherResponse mapperInventoryReceiptVoucher(InventoryReceiptVoucher inventoryReceiptVoucher) {

        InventoryReceiptVoucherResponse response = modelMapper.map(inventoryReceiptVoucher, InventoryReceiptVoucherResponse.class);
        Set<ReceiptVoucherDetailResponse> detailResponseSet = inventoryReceiptVoucher
                .getReceiptVoucherDetails().stream().map(item -> new ReceiptVoucherDetailResponse(goodsServicesImpl.mapperGoodResponse(item.getGoods())
                        , mapperLocationInWarehouse(item.getRowLocation())
                        , item.getQuantity()))
                .collect(Collectors.toSet());
        response.setReceiptVoucherDetailResponses(detailResponseSet);
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

    private EUnit parseUnitGood(String unit) {
        EUnit unitGoods = null;
        switch (unit) {
            case "Thùng":
            case "thùng":
                unitGoods = EUnit.THUNG;
        }
        return unitGoods;
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

    private LocationInWarehouse mapperLocationInWarehouse(RowLocation rowLocation) {
        LocationInWarehouse locationInWarehouse = new LocationInWarehouse();
        locationInWarehouse.setNameRow(rowLocation.getName());
        locationInWarehouse.setCodeRow(rowLocation.getCode());
        locationInWarehouse.setNameColumn(rowLocation.getColumnLocation().getName());
        locationInWarehouse.setCodeColumn(rowLocation.getColumnLocation().getCode());
        locationInWarehouse.setNameShelf(rowLocation.getColumnLocation().getShelveStorage().getName());
        locationInWarehouse.setCodeShelf(rowLocation.getColumnLocation().getShelveStorage().getCode());
        locationInWarehouse.setNameWarehouse(rowLocation.getColumnLocation().getShelveStorage().getWarehouse().getName());
        locationInWarehouse.setCodeWarehouse(rowLocation.getColumnLocation().getShelveStorage().getWarehouse().getCode());
        return locationInWarehouse;
    }
}


