package com.example.warehouse_management.utils;

import com.example.warehouse_management.utils.GoodsDelivery;
import com.example.warehouse_management.models.warehouse.BinPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RowLocationGoodsDeliveryTemp {
    private BinPosition binPosition;
    private GoodsDelivery goodsDelivery;
}
