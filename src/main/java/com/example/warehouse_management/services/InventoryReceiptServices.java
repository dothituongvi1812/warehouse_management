package com.example.warehouse_management.services;

import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryReceiptVoucherResponse;
import com.example.warehouse_management.payload.response.RowLocationResponse;

import java.util.List;


public interface InventoryReceiptServices {
    public InventoryReceiptVoucherResponse createReceiptVoucher(ReceiptVoucherRequest receiptVoucherRequest);
    public List<RowLocationResponse> putTheGoodsOnShelf(String receiptVoucherCode);
    public List<InventoryReceiptVoucherResponse> getAllSortedByDate();
    public List<InventoryReceiptVoucher> getAll();
}
