package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.ColumnLocationRequest;
import com.example.warehouse_management.payload.response.ColumnLocationResponse;
import com.example.warehouse_management.services.ColumnLocationServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/column-location")
public class ColumnLocationOfShelfController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ColumnLocationServices columnLocationServices;
    @PostMapping("/add")
    public ResponseEntity<List<ColumnLocationResponse>> addColumns(@RequestBody ColumnLocationRequest columnLocationRequest){
        logger.info("/add");
        return new ResponseEntity(columnLocationServices.addColumns(columnLocationRequest),HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<ColumnLocationResponse>>getAll(){
        logger.info("/get-all");
        return new ResponseEntity(columnLocationServices.getAll(), HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<ColumnLocationResponse>getByCode(@PathVariable String code){
        logger.info("/get-by/"+code);
        return new ResponseEntity(columnLocationServices.getByCode(code),HttpStatus.OK);
    }
}
