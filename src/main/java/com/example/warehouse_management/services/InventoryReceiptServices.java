package com.example.warehouse_management.services;

import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryReceiptVoucherResponse;
import com.example.warehouse_management.payload.response.RowLocationResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface InventoryReceiptServices {
    public InventoryReceiptVoucherResponse createReceiptVoucher(ReceiptVoucherRequest receiptVoucherRequest);
    public List<RowLocationResponse> putTheGoodsOnShelf(String receiptVoucherCode);
    public Page<InventoryReceiptVoucherResponse> getAllSortedByDate(Integer page, Integer size);
    public List<InventoryReceiptVoucher> getAll();
    public InventoryReceiptVoucherResponse getReceiptVoucherByCode(String voucherCode);
}
