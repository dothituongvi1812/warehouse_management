package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.warehouse.BinPosition;
import com.example.warehouse_management.payload.request.goods.GoodsRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RowLocationGoodsTemp {
    private BinPosition binPosition;
    private GoodsRequest goodsRequest;
}
