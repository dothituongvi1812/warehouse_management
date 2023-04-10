package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryReceiptVoucherResponse;
import com.example.warehouse_management.services.InventoryDeliveryVoucherServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/delivery-voucher")
public class InventoryDeliveryVoucherController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    InventoryDeliveryVoucherServices inventoryDeliveryVoucherServices;

    @PostMapping("/create")
    public ResponseEntity<?> createDeliveryVoucher(@Valid @RequestBody DeliveryVoucherRequest deliveryVoucherRequest, Principal principal) {
        logger.info("/api/delivery-voucher/create");
        deliveryVoucherRequest.setEmail(principal.getName());
        return new ResponseEntity(inventoryDeliveryVoucherServices.createInventoryDeliveryVoucher(deliveryVoucherRequest), HttpStatus.OK);
    }

    @PostMapping("/export-goods/{deliveryVoucherCode}")
    public ResponseEntity<?> exportGoods(@PathVariable String deliveryVoucherCode) {
        logger.info("/api/delivery-voucher/export-goods/" + deliveryVoucherCode);
        return new ResponseEntity(inventoryDeliveryVoucherServices.exportGoods(deliveryVoucherCode), HttpStatus.OK);
    }

    @GetMapping("/get-delivery-by/{deliveryVoucherCode}")
    public ResponseEntity<?> getDeliveryVoucherByCode(@PathVariable String deliveryVoucherCode) {
        logger.info("/api/delivery-voucher/get-delivery-by/" + deliveryVoucherCode);
        return new ResponseEntity(inventoryDeliveryVoucherServices.getDeliveryVoucherByCode(deliveryVoucherCode), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<InventoryReceiptVoucherResponse>> getAllSortByDate(){
        logger.info("/api/delivery-voucher/get-all");
        return new ResponseEntity(inventoryDeliveryVoucherServices.getAllSortedByDate(),HttpStatus.OK);
    }
}

