package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.RowLocationRequest;
import com.example.warehouse_management.payload.request.StatusRequest;
import com.example.warehouse_management.payload.response.RowLocationResponse;
import com.example.warehouse_management.services.RowLocationServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/row-location")
public class RowLocationOfShelfController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RowLocationServices rowLocationServices;
    @PostMapping("/add")
    public ResponseEntity<RowLocationResponse> addRowLocation(@RequestBody RowLocationRequest request){
        logger.info("/add");
        return new ResponseEntity(rowLocationServices.addRowLocations(request), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<Page<RowLocationResponse>>getAll(@RequestParam Integer page, @RequestParam Integer size){
        logger.info("/get-all");
        return new ResponseEntity(rowLocationServices.getAll(page,size), HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<RowLocationResponse>getByCode(@PathVariable String code){
        logger.info("/get-by"+code);
        return new ResponseEntity(rowLocationServices.getByCode(code), HttpStatus.OK);
    }
    @GetMapping("/filter-status-by/{codeWarehouse}")
    public ResponseEntity<RowLocationResponse>filterByStatus(@PathVariable String codeWarehouse, @RequestBody StatusRequest statusRequest){
        logger.info("/filter-status-by/"+codeWarehouse);
        return new ResponseEntity(rowLocationServices.filterStatusByCodeWarehouse(codeWarehouse,statusRequest),HttpStatus.OK);
    }
    @GetMapping("/get-all-by/{codeWarehouse}")
    public ResponseEntity<Page<RowLocationResponse>>getAllByCodeWarehouse(@PathVariable String codeWarehouse,Integer page,Integer size){
        logger.info("/get-all-by/"+codeWarehouse);
        return new ResponseEntity(rowLocationServices.getAllRowLocationByWarehouseCode(codeWarehouse,page,size),HttpStatus.OK);
    }
    @GetMapping("/report-stock-position")
    public ResponseEntity<?>reportStockPosition(){
        logger.info("/report-stock-position");
        return new ResponseEntity<>(rowLocationServices.reportStockPosition(),HttpStatus.OK);
    }

}
