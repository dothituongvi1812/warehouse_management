package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.category.CategoryRequest;
import com.example.warehouse_management.services.CategoryServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/category")
public class CategoryController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    CategoryServices categoryServices;
    @PostMapping("/add")
    public ResponseEntity addCategory(@RequestBody CategoryRequest request){
        logger.info("/add");
        return new ResponseEntity(categoryServices.addCategory(request), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity getAll(){
        logger.info("/get-all");
        return new ResponseEntity(categoryServices.findAll(), HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity getByCode(@PathVariable String code){
        logger.info("/get-by/"+code);
        return new ResponseEntity(categoryServices.getByCode(code), HttpStatus.OK);
    }
}
