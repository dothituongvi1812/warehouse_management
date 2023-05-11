package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.receive.ReceiptVoucherRequest;
import com.example.warehouse_management.payload.response.InventoryDeliveryVoucherResponse;
import com.example.warehouse_management.payload.response.InventoryReceiptVoucherResponse;
import com.example.warehouse_management.payload.response.BinPositionResponse;
import com.example.warehouse_management.services.InventoryReceiptServices;
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
@RequestMapping("api/receipt-voucher")
public class InventoryReceiptVoucherController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    InventoryReceiptServices inventoryReceiptServices;
    @PostMapping("/create/{purchaseReceiptCode}")
    public ResponseEntity<InventoryReceiptVoucherResponse> createReceiptVoucher(@PathVariable String purchaseReceiptCode,@Valid @RequestBody ReceiptVoucherRequest receiptVoucherRequest, Principal principal){
        logger.info("api/receipt-voucher/create"+purchaseReceiptCode);
        receiptVoucherRequest.setEmail(principal.getName());
        return new ResponseEntity(inventoryReceiptServices.createReceiptVoucher(purchaseReceiptCode,receiptVoucherRequest), HttpStatus.OK);
    }

    @GetMapping("/get-receipt-voucher-by/{voucherCode}")
    public ResponseEntity<InventoryReceiptVoucherResponse> getReceiptVoucherByCode(@PathVariable String voucherCode){
       logger.info("api/receipt-voucher/get-receipt-voucher-by/"+voucherCode);
        return new ResponseEntity(inventoryReceiptServices.getReceiptVoucherByCode(voucherCode), HttpStatus.OK);
    }
    @PostMapping("/put-goods-on-shelf/{receiptVoucherCode}")
    public ResponseEntity<List<BinPositionResponse>> putTheGoodsOnShelf(@PathVariable String receiptVoucherCode){
        logger.info("api/receipt-voucher/put-goods-on-shelf/"+receiptVoucherCode);
        return new ResponseEntity(inventoryReceiptServices.putTheGoodsOnShelf(receiptVoucherCode),HttpStatus.OK);
    }
    @GetMapping("/get-page")
    public ResponseEntity<List<InventoryReceiptVoucherResponse>> getAllSortByDate(@RequestParam Integer page, @RequestParam Integer size){
        logger.info("api/receipt-voucher/get-page");
        return new ResponseEntity(inventoryReceiptServices.getPageSortedByDate(page,size),HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<InventoryReceiptVoucherResponse>> getAll(){
        logger.info("api/receipt-voucher/get-all");
        return new ResponseEntity(inventoryReceiptServices.findAll(),HttpStatus.OK);
    }
    @GetMapping("/search-by/{date}")
    public ResponseEntity<List<InventoryReceiptVoucherResponse>> searchByDate(@PathVariable String date){
        return new ResponseEntity<>(inventoryReceiptServices.searchByDate(date),HttpStatus.OK);
    }
    @PostMapping("/cancel/{receiptVoucherCode}")
    public ResponseEntity<Boolean> cancelInventoryReceiptVoucherByCode(@PathVariable String receiptVoucherCode){
        return new ResponseEntity<>(inventoryReceiptServices.cancelInventoryReceiptVoucherByCode(receiptVoucherCode),HttpStatus.OK);
    }

}
