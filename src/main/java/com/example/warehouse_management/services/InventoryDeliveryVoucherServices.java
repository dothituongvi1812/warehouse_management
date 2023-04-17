package com.example.warehouse_management.services;

import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryDeliveryVoucherResponse;
import com.example.warehouse_management.payload.response.BinLocationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryDeliveryVoucherServices {
    InventoryDeliveryVoucherResponse createInventoryDeliveryVoucher(String saleReceiptCode,DeliveryVoucherRequest deliveryVoucherRequest );
    List<BinLocationResponse> exportGoods(String deliveryVoucherCode);
    InventoryDeliveryVoucherResponse getDeliveryVoucherByCode(String deliveryVoucherCode);
    public Page<InventoryDeliveryVoucherResponse> getPageSortedByDate(Integer page, Integer size);
    public List<InventoryDeliveryVoucherResponse> getAll();

}
