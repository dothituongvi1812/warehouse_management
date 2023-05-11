package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.delivery.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryDeliveryVoucherResponse;
import com.example.warehouse_management.services.InventoryDeliveryVoucherServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/delivery-voucher")
public class InventoryDeliveryVoucherController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    InventoryDeliveryVoucherServices inventoryDeliveryVoucherServices;

    @PostMapping("/create/{saleReceiptCode}")
    public ResponseEntity<?> createDeliveryVoucher(@PathVariable String saleReceiptCode,@Valid @RequestBody DeliveryVoucherRequest deliveryVoucherRequest, Principal principal) {
        logger.info("/api/delivery-voucher/create");
        deliveryVoucherRequest.setEmail(principal.getName());
        return new ResponseEntity(inventoryDeliveryVoucherServices.createInventoryDeliveryVoucher(saleReceiptCode,deliveryVoucherRequest), HttpStatus.OK);
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
    @GetMapping("/get-page")
    public ResponseEntity<Page<InventoryDeliveryVoucherResponse>> getPageSortByDate(@RequestParam Integer page, @RequestParam Integer size){
        logger.info("/api/delivery-voucher/get-page");
        return new ResponseEntity(inventoryDeliveryVoucherServices.getPageSortedByDate(page,size),HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<InventoryDeliveryVoucherResponse>> getAll(){
        logger.info("/api/delivery-voucher/get-all");
        return new ResponseEntity(inventoryDeliveryVoucherServices.getAll(),HttpStatus.OK);
    }
    @GetMapping("/search-by/{date}")
    public ResponseEntity<List<InventoryDeliveryVoucherResponse>> searchByDate(@PathVariable String date){
        return new ResponseEntity<>(inventoryDeliveryVoucherServices.searchByDate(date),HttpStatus.OK);
    }

    @PostMapping("/cancel/{receiptVoucherCode}")
    public ResponseEntity<Boolean> cancelInventoryReceiptVoucherByCode(@PathVariable String receiptVoucherCode){
        return new ResponseEntity<>(inventoryDeliveryVoucherServices.cancelInventoryDeliveryVoucherByCode(receiptVoucherCode),HttpStatus.OK);
    }
}

