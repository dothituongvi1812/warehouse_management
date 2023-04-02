package com.example.warehouse_management.models.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDelivery {
    private Goods goods;
    private int quantity;
}
