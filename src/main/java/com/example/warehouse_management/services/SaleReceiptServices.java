package com.example.warehouse_management.services;

import com.example.warehouse_management.models.selling.SaleReceipt;
import com.example.warehouse_management.payload.request.sale.SaleReceiptRequest;
import com.example.warehouse_management.payload.response.SaleReceiptResponse;

import java.util.List;


public interface SaleReceiptServices {
    public SaleReceiptResponse createSaleReceipt(SaleReceiptRequest saleReceiptRequest);
    public List<SaleReceiptResponse> getAll();
    public SaleReceipt findSaleReceiptByCode(String saleReceiptCode);
    public SaleReceiptResponse getSaleReceiptByCode(String saleReceiptCode);
    public String cancelSaleReceipt(String saleReceiptCode );
}
