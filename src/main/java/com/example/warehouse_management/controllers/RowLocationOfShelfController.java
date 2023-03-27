package com.example.warehouse_management.controllers;

import com.example.warehouse_management.models.warehouse.ShelveStorage;
import com.example.warehouse_management.payload.request.RowLocationRequest;
import com.example.warehouse_management.payload.response.RowLocationResponse;
import com.example.warehouse_management.services.RowLocationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/row-location")
public class RowLocationOfShelfController {
    @Autowired
    private RowLocationServices rowLocationServices;
    @PostMapping("/add")
    public ResponseEntity<RowLocationResponse> addRowLocation(@RequestBody RowLocationRequest request){
        return new ResponseEntity(rowLocationServices.addRowLocations(request), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<RowLocationResponse>>getAll(){
        return new ResponseEntity(rowLocationServices.getAll(), HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<RowLocationResponse>getByCode(@PathVariable String code){
        return new ResponseEntity(rowLocationServices.getByCode(code), HttpStatus.OK);
    }

}
