package com.example.warehouse_management.services;

import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryReceiptVoucherResponse;
import com.example.warehouse_management.payload.response.BinLocationResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface InventoryReceiptServices {
    public InventoryReceiptVoucherResponse createReceiptVoucher(String purchaseReceiptCode,ReceiptVoucherRequest receiptVoucherRequest);
    public List<BinLocationResponse> putTheGoodsOnShelf(String receiptVoucherCode);
    public Page<InventoryReceiptVoucherResponse> getPageSortedByDate(Integer page, Integer size);
    public List<InventoryReceiptVoucherResponse> findAll();
    public List<InventoryReceiptVoucher> getAll();
    public InventoryReceiptVoucherResponse getReceiptVoucherByCode(String voucherCode);
}
