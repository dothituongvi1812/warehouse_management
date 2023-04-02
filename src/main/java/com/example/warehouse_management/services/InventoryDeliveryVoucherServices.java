package com.example.warehouse_management.services;

import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;

import java.util.List;

public interface InventoryDeliveryVoucherServices {
    List<InventoryDeliveryVoucher> createInventoryDeliveryVoucher(DeliveryVoucherRequest deliveryVoucherRequest );
}
