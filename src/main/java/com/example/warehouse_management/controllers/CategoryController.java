package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.CategoryRequest;
import com.example.warehouse_management.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/category")
public class CategoryController {
    @Autowired
    CategoryServices categoryServices;
    @PostMapping("/add")
    public ResponseEntity addCategory(@RequestBody CategoryRequest request){
//        Test change git username
        return new ResponseEntity(categoryServices.addCategory(request), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity getAll(){
        return new ResponseEntity(categoryServices.findAll(), HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity getByCode(@PathVariable String code){
        return new ResponseEntity(categoryServices.getByCode(code), HttpStatus.OK);
    }
}
