package com.example.warehouse_management.payload.response;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.models.voucher.ReceiptVoucherDetailPK;
import com.example.warehouse_management.models.warehouse.RowLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptVoucherDetailResponse {

    private GoodsResponse goodsResponse;
    private LocationInWarehouse locationInWarehouse;
    private int quantity;
}
