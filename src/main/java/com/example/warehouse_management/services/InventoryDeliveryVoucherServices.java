package com.example.warehouse_management.services;

import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryDeliveryVoucherResponse;
import com.example.warehouse_management.payload.response.InventoryReceiptVoucherResponse;
import com.example.warehouse_management.payload.response.RowLocationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryDeliveryVoucherServices {
    InventoryDeliveryVoucherResponse createInventoryDeliveryVoucher(DeliveryVoucherRequest deliveryVoucherRequest );
    List<RowLocationResponse> exportGoods(String deliveryVoucherCode);
    InventoryDeliveryVoucherResponse getDeliveryVoucherByCode(String deliveryVoucherCode);
    public Page<InventoryDeliveryVoucherResponse> getAllSortedByDate(Integer page, Integer size);

}
