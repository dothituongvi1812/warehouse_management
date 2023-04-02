package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.request.GoodDeliveryRequest;
import com.example.warehouse_management.services.InventoryDeliveryVoucherServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/delivery-voucher")
public class InventoryDeliveryVoucherController {
    @Autowired
    InventoryDeliveryVoucherServices inventoryDeliveryVoucherServices;
    @PostMapping("/create")
    public ResponseEntity<?>createDeliveryVoucher(@RequestBody DeliveryVoucherRequest deliveryVoucherRequest, Principal principal){
        deliveryVoucherRequest.setEmail(principal.getName());
        return new ResponseEntity(inventoryDeliveryVoucherServices.createInventoryDeliveryVoucher(deliveryVoucherRequest), HttpStatus.OK);
    }
}
