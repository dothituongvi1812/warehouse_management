package com.example.warehouse_management.services;

import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryReceiptVoucherResponse;


public interface InventoryReceiptServices {
    public InventoryReceiptVoucherResponse createReceiptVoucher(ReceiptVoucherRequest receiptVoucherRequest);
}
