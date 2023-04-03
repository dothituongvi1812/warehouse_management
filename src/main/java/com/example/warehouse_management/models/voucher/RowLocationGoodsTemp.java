package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.warehouse.RowLocation;
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
    private RowLocation rowLocation;
    private GoodsRequest goodsRequest;
}
