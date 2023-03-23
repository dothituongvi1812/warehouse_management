package com.example.warehouse_management.services;

import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;

import java.util.List;

public interface InventoryReceiptServices {
    public List<InventoryReceiptVoucher> createReceiptVoucher(ReceiptVoucherRequest receiptVoucherRequest);
}
