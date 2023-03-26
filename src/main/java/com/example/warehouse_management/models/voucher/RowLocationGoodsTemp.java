package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.goods.GoodsReceipt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RowLocationGoodsTemp {
    private String rowLocationOfShelfCode;
    private GoodsReceipt goodsReceipt;
}
