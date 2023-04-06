package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.GoodsRequest;
import com.example.warehouse_management.payload.response.GoodsResponse;
import com.example.warehouse_management.services.GoodsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    @Autowired
    GoodsServices goodsServices;
    @PostMapping("/add")
    public ResponseEntity<GoodsResponse> addGoods(@RequestBody @Valid  GoodsRequest goodsRequest){
        return new ResponseEntity(goodsServices.addGoods(goodsRequest), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<List<GoodsResponse>> getAll(){
        return new ResponseEntity(goodsServices.getAll(),HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<GoodsResponse> getByCode(@PathVariable String code){
        return new ResponseEntity(goodsServices.getByCode(code),HttpStatus.OK);
    }
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<GoodsResponse>> searchByCodeOrName(@PathVariable String keyword){
        return new ResponseEntity(goodsServices.searchByCodeOrName(keyword),HttpStatus.OK);
    }
    @GetMapping("/get-all-by/{categoryCode}")
    public ResponseEntity<List<GoodsResponse>> getAllByCategoryCode(@PathVariable String categoryCode ){
        return new ResponseEntity(goodsServices.getAllByCategoryCode(categoryCode),HttpStatus.OK);
    }
}
