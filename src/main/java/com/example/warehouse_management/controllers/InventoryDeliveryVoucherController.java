package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.request.GoodDeliveryRequest;
import com.example.warehouse_management.payload.response.RowLocationResponse;
import com.example.warehouse_management.services.InventoryDeliveryVoucherServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/delivery-voucher")
public class InventoryDeliveryVoucherController {
    @Autowired
    InventoryDeliveryVoucherServices inventoryDeliveryVoucherServices;
    @PostMapping("/create")
    public ResponseEntity<?>createDeliveryVoucher(@Valid @RequestBody  DeliveryVoucherRequest deliveryVoucherRequest, Principal principal){
        deliveryVoucherRequest.setEmail(principal.getName());
        return new ResponseEntity(inventoryDeliveryVoucherServices.createInventoryDeliveryVoucher(deliveryVoucherRequest), HttpStatus.OK);
    }
    @PostMapping("/export-goods/{deliveryVoucherCode}")
    public ResponseEntity<?> exportGoods(@PathVariable String deliveryVoucherCode){
        return new ResponseEntity(inventoryDeliveryVoucherServices.exportGoods(deliveryVoucherCode),HttpStatus.OK);
    }
    @GetMapping("/get-delivery-by/{deliveryVoucherCode}")
    public ResponseEntity<?> getDeliveryVoucherByCode(@PathVariable String deliveryVoucherCode){
        return new ResponseEntity(inventoryDeliveryVoucherServices.getDeliveryVoucherByCode(deliveryVoucherCode),HttpStatus.OK);
    }
}
