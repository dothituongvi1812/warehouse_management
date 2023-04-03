package com.example.warehouse_management.services;

import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryDeliveryVoucherResponse;

import java.util.List;

public interface InventoryDeliveryVoucherServices {
    InventoryDeliveryVoucherResponse createInventoryDeliveryVoucher(DeliveryVoucherRequest deliveryVoucherRequest );
}
