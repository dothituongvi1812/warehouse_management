package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.warehouse.BinLocation;
import com.example.warehouse_management.payload.request.GoodsRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RowLocationGoodsTemp {
    private BinLocation binLocation;
    private GoodsRequest goodsRequest;
}
