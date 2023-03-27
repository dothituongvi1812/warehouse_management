package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.ColumnLocationRequest;
import com.example.warehouse_management.payload.response.ColumnLocationResponse;
import com.example.warehouse_management.services.ColumnLocationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/column-location")
public class ColumnLocationOfShelfController {
    @Autowired
    private ColumnLocationServices columnLocationServices;
    @PostMapping("/add")
    public ResponseEntity<List<ColumnLocationResponse>> addColumns(@RequestBody ColumnLocationRequest columnLocationRequest){
        return new ResponseEntity(columnLocationServices.addColumns(columnLocationRequest),HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<ColumnLocationResponse>>getAll(){
        return new ResponseEntity(columnLocationServices.getAll(), HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<ColumnLocationResponse>getByCode(@PathVariable String code){
        return new ResponseEntity(columnLocationServices.getByCode(code),HttpStatus.OK);
    }
}
