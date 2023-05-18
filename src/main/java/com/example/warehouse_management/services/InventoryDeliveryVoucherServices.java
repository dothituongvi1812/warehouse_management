package com.example.warehouse_management.services;

import com.example.warehouse_management.payload.request.delivery.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryDeliveryVoucherResponse;
import com.example.warehouse_management.payload.response.BinPositionResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface InventoryDeliveryVoucherServices {
    InventoryDeliveryVoucherResponse createInventoryDeliveryVoucher(String saleReceiptCode,DeliveryVoucherRequest deliveryVoucherRequest );
    List<BinPositionResponse> exportGoods(String deliveryVoucherCode);
    InventoryDeliveryVoucherResponse getDeliveryVoucherByCode(String deliveryVoucherCode);
    public Page<InventoryDeliveryVoucherResponse> getPageSortedByDate(Integer page, Integer size);
    public List<InventoryDeliveryVoucherResponse> getAll();
    public Page<InventoryDeliveryVoucherResponse> searchByDateOrCodeOrCreatedBy(String date, String code, String createdBy, Integer page, Integer size);
    public boolean cancelInventoryDeliveryVoucherByCode(String code);

}
