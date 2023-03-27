package com.example.warehouse_management.models.goods;

import com.example.warehouse_management.payload.request.GoodsRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceipt {
    private GoodsRequest goodsRequest;
    private int quantity;
}
