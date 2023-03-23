package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotEnoughSpaceException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.goods.GoodsReceipt;
import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.models.voucher.ReceiptVoucherDetail;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;
import com.example.warehouse_management.repository.InventoryReceiptVoucherRepository;
import com.example.warehouse_management.services.*;
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
    UserServices userServices;
    @Override
    public List<InventoryReceiptVoucher> createReceiptVoucher(ReceiptVoucherRequest receiptVoucherRequest) {
        List<InventoryReceiptVoucher> inventoryReceiptVoucherList =new ArrayList<>();
        Partner partner = partnerServices.addPartner(receiptVoucherRequest.getPartnerRequest());
        User user =userServices.findByCode(receiptVoucherRequest.getEmail());
        Set<ReceiptVoucherDetail> receiptVoucherDetailSet = new HashSet<>();
        List<GoodsReceipt> goodsReceipts= receiptVoucherRequest.getGoodsReceiptRequests()
                .stream().map(item->new GoodsReceipt(goodsServices.findGoodByCode(item.getGoodsCode()), item.getQuantity()))
                .collect(Collectors.toList());
        List<RowLocation> rowLocations = rowLocationServices.findByStatusTrongAndConTrong();
        if(CollectionUtils.isEmpty(rowLocations)){
            throw new NotFoundGlobalException("Không còn vị trí trống");
        }
        for (RowLocation row : rowLocations) {
            if (!CollectionUtils.isEmpty(goodsReceipts)) {
                for (GoodsReceipt goodsReceipt : goodsReceipts) {
                    if (goodsReceipt.getQuantity() != 0) {
                        double volumeGoods = goodsReceipt.getGoods().getVolume() * goodsReceipt.getQuantity();
                        int maxCapacity = (int) (row.getVolume() / goodsReceipt.getGoods().getVolume());
                        Set<RowLocation> rowLocationGoods =new HashSet<>();
                        if (row.getRemainingVolume() >= volumeGoods) {
                            ReceiptVoucherDetail receiptVoucherDetail=new ReceiptVoucherDetail();
                            rowLocationGoods.add(row);
                            goodsReceipt.getGoods().setRowLocations(rowLocationGoods);
                            receiptVoucherDetail.setQuantity(goodsReceipt.getQuantity());
                            receiptVoucherDetailSet.add(receiptVoucherDetail);
                            //Nếu có chứa hàng hoá
                            if (!ObjectUtils.isEmpty(row.getGoods())) {
                                System.out.println("Re-fill");
                                //Kiểm tra có cùng hàng hoá không
                                if (row.getGoods().getCode().equals(goodsReceipt.getGoods().getCode())) {
                                    row.setRemainingVolume(row.getRemainingVolume() - volumeGoods);
                                    goodsReceipt.setQuantity(0);
                                }
                            //Chưa chứa hàng hoá
                            } else {
                                System.out.println("Hàng trống");
                                row.setGoods(goodsReceipt.getGoods());
                                row.setMaxCapacity(maxCapacity);
                                row.setCurrentCapacity(goodsReceipt.getQuantity());
                                double remainingVolume = row.getRemainingVolume() - volumeGoods;
                                System.out.println("remainingVolume" + remainingVolume);
                                row.setRemainingVolume(remainingVolume);
                                goodsReceipt.setQuantity(0);
                            }

                        }
                        else{
                            throw new NotEnoughSpaceException("Không đủ không gian để chứa hàng hoá "+goodsReceipt.getGoods().getName());
                        }
                    }

                }
            }

        }
        InventoryReceiptVoucher inventoryReceiptVoucher =new InventoryReceiptVoucher();
        inventoryReceiptVoucher.setPartner(partner);
        inventoryReceiptVoucher.setCreatedBy(user);
        inventoryReceiptVoucher.setCreateDate(new Date());
        inventoryReceiptVoucher.setCode(generateReceiptVoucher());
        inventoryReceiptVoucher.setReceiptVoucherDetails(receiptVoucherDetailSet);
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

}


