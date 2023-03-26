package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotEnoughSpaceException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.goods.GoodsReceipt;
import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.type.EUnit;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.models.voucher.RowLocationGoodsTemp;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.request.GoodsRequest;
import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;
import com.example.warehouse_management.repository.InventoryReceiptVoucherRepository;
import com.example.warehouse_management.services.*;
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
    UserServices userServices;
    @Autowired
    CategoryServices categoryServices;
    @Override
    public List<InventoryReceiptVoucher> createReceiptVoucher(ReceiptVoucherRequest receiptVoucherRequest) {
        // validate good_request
        List<InventoryReceiptVoucher> inventoryReceiptVoucherList =new ArrayList<>();
        Partner partner = partnerServices.addPartner(receiptVoucherRequest.getPartnerRequest());
        User user =userServices.findByCode(receiptVoucherRequest.getEmail());
        List<GoodsReceipt> goodsReceiptList = initListGoods(receiptVoucherRequest.getGoodsRequests());
        List<String> goodsNames=goodsReceiptList.stream().map(goodsReceipt -> goodsReceipt.getGoods().getName()).collect(Collectors.toList());
        List<RowLocation> rowLocationListStatus= rowLocationServices.findByStatusTrongAndConTrong();

        goodsReceiptList.stream()
                .filter (goodsReceipt -> rowLocationListStatus.stream()
                        .anyMatch(rowLocation ->compareSizeShelfAndGoods(
                                createArraySize(rowLocation.getWidth(), rowLocation.getHeight(), rowLocation.getLength()),
                                createArraySize(goodsReceipt.getGoods().getWidth(),goodsReceipt.getGoods().getHeight(),goodsReceipt.getGoods().getLength()),
                                goodsReceipt.getGoods().getName())))
                .collect(Collectors.toList());
        List<RowLocationGoodsTemp> rowLocationGoodsTempList=new ArrayList<>();
        List<String> rowLocationUsed=new ArrayList<>();
        //Kiểm tra trên kệ có sản phẩm chưa.
       List<RowLocation> rowLocations=rowLocationServices.findByGoodsName(goodsNames);
        if(CollectionUtils.isEmpty(rowLocations)){
            for (RowLocation rowLocation:rowLocationListStatus) {
                for (GoodsReceipt goodsReceipt:goodsReceiptList){
                    double volumeGoods=goodsReceipt.getGoods().getVolume()*goodsReceipt.getQuantity();
                    if(rowLocation.getVolume()>=volumeGoods){
                        rowLocationUsed.add(rowLocation.getCode());
                        RowLocationGoodsTemp rowLocationGoodsTemp =new RowLocationGoodsTemp();
                        rowLocationGoodsTemp.setRowLocationOfShelfCode(rowLocation.getCode());
                        rowLocationGoodsTemp.setGoodsReceipt(goodsReceipt);
                        rowLocationGoodsTempList.add(rowLocationGoodsTemp);
                    }
                }
            }
        }

        InventoryReceiptVoucher inventoryReceiptVoucher =new InventoryReceiptVoucher();
        inventoryReceiptVoucher.setPartner(partner);
        inventoryReceiptVoucher.setCreatedBy(user);
        inventoryReceiptVoucher.setCreateDate(new Date());
        inventoryReceiptVoucher.setCode(generateReceiptVoucher());
        return null;
    }

    private String generateReceiptVoucher(){
//        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//        String code = year.substring(Math.max(year.length() - 2, 0));
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dateFormate = sdf.format(new Date());
        String receiptCode="PN"+dateFormate;
        InventoryReceiptVoucher inventoryReceiptVoucher =receiptVoucherRepository.findTopByOrderByIdDesc();
        if(inventoryReceiptVoucher==null){
            return receiptCode+1;
        }
        long id=inventoryReceiptVoucher.getId();
        return receiptCode+(id+1);
    }
    private EUnit parseUnitGood(String unit){
        EUnit unitGoods = null;
        switch (unit){
            case "Thùng":
            case "thùng": unitGoods=EUnit.THUNG;
        }
        return unitGoods;
    }
    private List<GoodsReceipt> initListGoods(List<GoodsRequest> goodsRequestList){
        List<GoodsReceipt> goodsReceiptList = goodsRequestList.stream()
                .map(goodsRequest ->new GoodsReceipt(
                        new Goods(goodsRequest.getName(),parseUnitGood(goodsRequest.getUnit()),
                                goodsRequest.getLength(), goodsRequest.getWidth(), goodsRequest.getHeight(),
                                goodsRequest.getHeight()* goodsRequest.getWidth()* goodsRequest.getLength(),
                                categoryServices.findCategoryByCode(goodsRequest.getCategoryCode()))
                        , goodsRequest.getQuantity()))
                .collect(Collectors.toList());
        return goodsReceiptList;
    }
    private boolean compareVolumeGoodsandRowLocation(double volumeGoods,int quantity, double volumeRowLocation){
        if(volumeRowLocation>=volumeGoods*quantity)
            return true;
        return false;
    }

    private double[] createArraySize(double width, double height, double length){
        double[] size ={width,height,length};
        Arrays.sort(size);
        return size;

    }
    private boolean compareSizeShelfAndGoods(double[] sizeShelf,double[] sizeGoods,String goodsName){

        if(sizeShelf[0]>=sizeGoods[0]&&sizeShelf[1]>=sizeGoods[1]&&sizeShelf[2]>=sizeGoods[2])
            return true;
        else
            throw new NotEnoughSpaceException(goodsName+ "không thể chứa");
    }

}


