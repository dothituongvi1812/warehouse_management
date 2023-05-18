package com.example.warehouse_management.services;

import com.example.warehouse_management.models.selling.SaleReceipt;
import com.example.warehouse_management.payload.request.sale.SaleReceiptRequest;
import com.example.warehouse_management.payload.response.PurchaseReceiptResponse;
import com.example.warehouse_management.payload.response.SaleReceiptResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface SaleReceiptServices {
    public SaleReceiptResponse createSaleReceipt(SaleReceiptRequest saleReceiptRequest);
    public List<SaleReceiptResponse> getAll();
    public SaleReceipt findSaleReceiptByCode(String saleReceiptCode);
    public SaleReceiptResponse getSaleReceiptByCode(String saleReceiptCode);
    public Page<SaleReceiptResponse> searchByDateOrCodeOrCreatedBy(String date, String code, String createdBy, Integer page, Integer size);

}
