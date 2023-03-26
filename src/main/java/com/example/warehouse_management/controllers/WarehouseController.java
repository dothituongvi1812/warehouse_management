package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.WarehouseRequest;
import com.example.warehouse_management.payload.response.WarehouseResponse;
import com.example.warehouse_management.services.WarehouseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/warehouse")
public class WarehouseController {
    @Autowired
    private WarehouseServices warehouseServices;

    @PostMapping("/add")
    public ResponseEntity<WarehouseResponse> addGeneralWarehouse(@RequestBody WarehouseRequest request){
        return new ResponseEntity(warehouseServices.addWarehouse(request), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<WarehouseResponse>> findAll(){
        return new ResponseEntity(warehouseServices.findAll(), HttpStatus.OK);
    }
}
