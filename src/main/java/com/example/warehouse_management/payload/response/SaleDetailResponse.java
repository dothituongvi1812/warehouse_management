package com.example.warehouse_management.payload.response;

import com.example.warehouse_management.models.type.EStatusOfPurchasingGoods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailResponse {

    private GoodsResponse goods;
    private int quantitySale;
    private int quantityRemaining;
    private EStatusOfPurchasingGoods status;
}
