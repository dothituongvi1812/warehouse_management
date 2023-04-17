package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.goods.GoodsDelivery;
import com.example.warehouse_management.models.warehouse.BinLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RowLocationGoodsDeliveryTemp {
    private BinLocation binLocation;
    private GoodsDelivery goodsDelivery;
}
