package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.models.goods.GoodsDelivery;
import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;
import com.example.warehouse_management.repository.InventoryDeliveryVoucherRepository;
import com.example.warehouse_management.services.GoodsServices;
import com.example.warehouse_management.services.InventoryDeliveryVoucherServices;
import com.example.warehouse_management.services.RowLocationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class InventoryDeliveryVoucherServiceImpl implements InventoryDeliveryVoucherServices {
    @Autowired
    InventoryDeliveryVoucherRepository deliveryVoucherRepository;
    @Autowired
    GoodsServices goodsServices;
    @Autowired
    RowLocationServices rowLocationServices;
    @Override
    public List<InventoryDeliveryVoucher> createInventoryDeliveryVoucher(DeliveryVoucherRequest deliveryVoucherRequest) {
        List<GoodsDelivery> goodsDeliveryList = deliveryVoucherRequest.getGoodsRequests().stream()
                .map(item->new GoodsDelivery(goodsServices.findGoodByCode(item.getGoodCode()), item.getQuantity())).collect(Collectors.toList());
        for (GoodsDelivery goodDelivery:goodsDeliveryList) {
            List<RowLocation> rowLocationList = rowLocationServices.findAllRowLocationByGoodsCode(goodDelivery.getGoods().getCode());
            for (RowLocation rl:rowLocationList) {
                if(rl.getCurrentCapacity()>=goodDelivery.getQuantity()){
                    System.out.println("Tao phieu xuat");
                }
            }
        }
        return null;
    }
}
