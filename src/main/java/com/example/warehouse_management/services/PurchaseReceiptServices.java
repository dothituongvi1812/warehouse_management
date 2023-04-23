package com.example.warehouse_management.services;

import com.example.warehouse_management.models.purchase.PurchaseReceipt;
import com.example.warehouse_management.payload.request.purchase.PurchaseReceiptRequest;
import com.example.warehouse_management.payload.response.PurchaseReceiptResponse;

import java.util.List;

public interface PurchaseReceiptServices {
    public PurchaseReceiptResponse createPurchaseReceipt(PurchaseReceiptRequest purchaseReceiptRequest);
    public List<PurchaseReceiptResponse> getAll();
    public PurchaseReceipt findPurchaseReceiptByCode(String purchaseReceiptCode);
    public PurchaseReceiptResponse getPurchaseReceiptByCode(String purchaseReceiptCode);

}
