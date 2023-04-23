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
public class PurchaseDetailResponse {

    private GoodsResponse goods;
    private int quantityPurchased;
    private int quantityRemaining;

    private EStatusOfPurchasingGoods status;
}
