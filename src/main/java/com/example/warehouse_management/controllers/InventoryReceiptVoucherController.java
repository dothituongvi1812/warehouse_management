package com.example.warehouse_management.controllers;

import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.payload.request.ReceiptVoucherRequest;
import com.example.warehouse_management.payload.response.RowLocationResponse;
import com.example.warehouse_management.services.InventoryReceiptServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/receipt-voucher")
public class InventoryReceiptVoucherController {
    @Autowired
    InventoryReceiptServices inventoryReceiptServices;
    @PostMapping("/create")
    public ResponseEntity<List<InventoryReceiptVoucher>> addReceiptVoucher(@RequestBody ReceiptVoucherRequest receiptVoucherRequest, Principal principal){
        receiptVoucherRequest.setEmail(principal.getName());
        return new ResponseEntity(inventoryReceiptServices.createReceiptVoucher(receiptVoucherRequest), HttpStatus.OK);
    }
    @PostMapping("/put-goods-on-shelf/{receiptVoucherCode}")
    public ResponseEntity<List<RowLocationResponse>> putTheGoodsOnShelf(@PathVariable String receiptVoucherCode){
        return new ResponseEntity(inventoryReceiptServices.putTheGoodsOnShelf(receiptVoucherCode),HttpStatus.OK);
    }

}
