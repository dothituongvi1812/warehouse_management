package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.purchase.PurchaseReceiptRequest;
import com.example.warehouse_management.payload.response.InventoryDeliveryVoucherResponse;
import com.example.warehouse_management.payload.response.PurchaseReceiptResponse;
import com.example.warehouse_management.services.PurchaseReceiptServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/purchase-receipt")
public class PurchaseReceiptController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    PurchaseReceiptServices purchaseReceiptServices;
    @PostMapping("/create")
    public ResponseEntity<PurchaseReceiptResponse>createPurchaseReceipt(@RequestBody PurchaseReceiptRequest purchaseReceiptRequest, Principal principal){
        logger.info("api/purchase-receipt/create");
        purchaseReceiptRequest.setEmail(principal.getName());
        return new ResponseEntity<>(purchaseReceiptServices.createPurchaseReceipt(purchaseReceiptRequest), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<PurchaseReceiptResponse>>getAll(){
        logger.info("/get-all");
        return new ResponseEntity<>(purchaseReceiptServices.getAll(),HttpStatus.OK);
    }
    @GetMapping("/get-by/{purchaseReceiptCode}")
    public ResponseEntity<PurchaseReceiptResponse>getSaleReceiptByCode(@PathVariable String purchaseReceiptCode){
        logger.info("/get-by"+purchaseReceiptCode);
        return new ResponseEntity<>(purchaseReceiptServices.getPurchaseReceiptByCode(purchaseReceiptCode),HttpStatus.OK);
    }
    @GetMapping("/search-by/{date}")
    public ResponseEntity<List<PurchaseReceiptResponse>> searchByDate(@PathVariable String date){
        return new ResponseEntity<>(purchaseReceiptServices.searchByDate(date),HttpStatus.OK);
    }

}
