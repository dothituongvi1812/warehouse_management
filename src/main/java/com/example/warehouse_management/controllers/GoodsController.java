package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.goods.GoodsAddRequest;
import com.example.warehouse_management.payload.request.goods.UpdateGoodsRequest;
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
import java.util.Map;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    GoodsServices goodsServices;
    @PostMapping("/add")
    public ResponseEntity<GoodsResponse> addGoods(@Valid @RequestBody GoodsAddRequest goodsAddRequest){
        logger.info("/add");
        return new ResponseEntity(goodsServices.addGoods(goodsAddRequest), HttpStatus.OK);
    }
    @GetMapping("/get-page")
    public ResponseEntity<Page<GoodsResponse>> getPage(@RequestParam Integer page, @RequestParam Integer size){
        logger.info("/get-page");
        return new ResponseEntity(goodsServices.getPage(page,size),HttpStatus.OK);
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
    @GetMapping("/get-all")
    public ResponseEntity<List<GoodsResponse>> getAll(){
        logger.info("/get-all");
        return new ResponseEntity(goodsServices.getAll(),HttpStatus.OK);
    }
    @GetMapping("/get-current-quantity-by/{goodsCode}")
    public ResponseEntity<Integer> getCurrentQuantityOfGoodsInWarehouse(@PathVariable String goodsCode){
        logger.info("/get-current-quantity-by"+goodsCode);
        return new ResponseEntity<>(goodsServices.getCurrentQuantityOfGoodsInWarehouse(goodsCode),HttpStatus.OK);
    }
    @GetMapping("/count-quantity")
    public ResponseEntity<Map<String,Integer>> countCurrentQuantityOfGoodsInWarehouse(){
        logger.info("/count-quantity");
        return new ResponseEntity<>(goodsServices.countCurrentQuantityOfGoodsInWarehouse(),HttpStatus.OK);
    }
    @PostMapping("/update/{goodsCode}")
    public ResponseEntity<GoodsResponse> updateGoods(@PathVariable String goodsCode,@RequestBody UpdateGoodsRequest updateGoodsRequest){
        return new ResponseEntity<>(goodsServices.updateGoods(goodsCode,updateGoodsRequest),HttpStatus.OK);
    }

}
