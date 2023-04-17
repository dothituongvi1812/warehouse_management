package com.example.warehouse_management.controllers;

import com.example.warehouse_management.models.selling.SaleReceipt;
import com.example.warehouse_management.payload.request.PurchaseReceiptRequest;
import com.example.warehouse_management.payload.request.SaleReceiptRequest;
import com.example.warehouse_management.payload.response.PurchaseReceiptResponse;
import com.example.warehouse_management.payload.response.SaleReceiptResponse;
import com.example.warehouse_management.services.SaleReceiptServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/sale-receipt")
public class SaleReceiptController {
    @Autowired
    SaleReceiptServices saleReceiptServices;
    @PostMapping("/create")
    public ResponseEntity<SaleReceiptResponse>createPurchaseReceipt(@Valid @RequestBody SaleReceiptRequest saleReceiptRequest, Principal principal){
        saleReceiptRequest.setEmail(principal.getName());
        return new ResponseEntity<>(saleReceiptServices.createSaleReceipt(saleReceiptRequest), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<SaleReceiptResponse>>getAll(){
        return new ResponseEntity<>(saleReceiptServices.getAll(),HttpStatus.OK);
    }
    @GetMapping("/get-by/{saleReceiptCode}")
    public ResponseEntity<SaleReceiptResponse>getSaleReceiptByCode(String saleReceiptCode){
        return new ResponseEntity<>(saleReceiptServices.getSaleReceiptByCode(saleReceiptCode),HttpStatus.OK);
    }
}
