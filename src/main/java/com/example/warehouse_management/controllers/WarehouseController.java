package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.WarehouseRequest;
import com.example.warehouse_management.payload.response.WarehouseResponse;
import com.example.warehouse_management.services.WarehouseServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/warehouse")
public class WarehouseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WarehouseServices warehouseServices;

    @PostMapping("/add")
    public ResponseEntity<WarehouseResponse> addGeneralWarehouse(@Valid @RequestBody WarehouseRequest request){
        logger.info("/add");
        return new ResponseEntity(warehouseServices.addWarehouse(request), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<WarehouseResponse>> getAll(){
        logger.info("/get-all");
        return new ResponseEntity(warehouseServices.findAll(), HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<WarehouseResponse> getByCode(@PathVariable String code){
        logger.info("get-by/"+code);
        return new ResponseEntity(warehouseServices.findByCode(code), HttpStatus.OK);
    }

}
