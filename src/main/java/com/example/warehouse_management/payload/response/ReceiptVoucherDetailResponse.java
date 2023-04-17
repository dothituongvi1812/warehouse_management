package com.example.warehouse_management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptVoucherDetailResponse {

    private GoodsResponse goods;
    private LocationInWarehouse locationInWarehouse;
    private int quantity;
}
