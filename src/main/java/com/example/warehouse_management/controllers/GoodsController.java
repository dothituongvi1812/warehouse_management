package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.GoodsAddRequest;
import com.example.warehouse_management.payload.request.GoodsRequest;
import com.example.warehouse_management.payload.response.GoodsResponse;
import com.example.warehouse_management.services.GoodsServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    GoodsServices goodsServices;
    @PostMapping("/add")
    public ResponseEntity<GoodsResponse> addGoods(@RequestBody @Valid GoodsAddRequest goodsRequest){
        logger.info("/add");
        return new ResponseEntity(goodsServices.addGoods(goodsRequest), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<Page<GoodsResponse>> getAll(@RequestParam Integer page, @RequestParam Integer size){
        logger.info("/get-all");
        return new ResponseEntity(goodsServices.getAll(page,size),HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<GoodsResponse> getByCode(@PathVariable String code){
        logger.info("/get-by/"+code);
        return new ResponseEntity(goodsServices.getByCode(code),HttpStatus.OK);
    }
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<GoodsResponse>> searchByCodeOrName(@PathVariable String keyword){
        logger.info("/search/"+keyword);
        return new ResponseEntity(goodsServices.searchByCodeOrName(keyword),HttpStatus.OK);
    }
    @GetMapping("/get-all-by/{categoryCode}")
    public ResponseEntity<List<GoodsResponse>> getAllByCategoryCode(@PathVariable String categoryCode ){
        logger.info("/get-all-by/"+categoryCode);
        return new ResponseEntity(goodsServices.getAllByCategoryCode(categoryCode),HttpStatus.OK);
    }
}
