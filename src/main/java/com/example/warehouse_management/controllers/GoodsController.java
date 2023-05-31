package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.goods.GoodsAddRequest;
import com.example.warehouse_management.payload.request.goods.UpdateGoodsRequest;
import com.example.warehouse_management.payload.response.GoodsResponse;
import com.example.warehouse_management.payload.response.GoodsStaticsResponse;
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
    public ResponseEntity<Page<GoodsResponse>> searchByCodeOrName(@PathVariable String keyword,@RequestParam Integer page, @RequestParam Integer size){
        logger.info("/search/"+keyword);
        return new ResponseEntity(goodsServices.searchByCodeOrName(keyword,page,size),HttpStatus.OK);
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
    }@GetMapping("/count-quantity-by/{warehouseCode}")
    public ResponseEntity<Map<String,Integer>> countCurrentQuantityOfGoodsByWarehouseCode(@PathVariable String warehouseCode){
        logger.info("/count-quantity-by/{warehouseCode}");
        return new ResponseEntity<>(goodsServices.countCurrentQuantityOfGoodsByWarehouseCode(warehouseCode),HttpStatus.OK);
    }

    @PostMapping("/update/{goodsCode}")
    public ResponseEntity<GoodsResponse> updateGoods(@PathVariable String goodsCode,@RequestBody UpdateGoodsRequest updateGoodsRequest){
        return new ResponseEntity<>(goodsServices.updateGoods(goodsCode,updateGoodsRequest),HttpStatus.OK);
    }
    @GetMapping("/report-imported-quantity-by/{date}")
    public ResponseEntity<Map<String,Integer>> reportImportedQuantityGoodsByDate(@PathVariable String date){
        return new ResponseEntity<>(goodsServices.reportImportedQuantityGoodsByDate(date),HttpStatus.OK);
    }
    @GetMapping("/report-exported-quantity-by/{date}")
    public ResponseEntity<Map<String,Integer>> reportExportedQuantityGoodsByDate(@PathVariable String date){
        return new ResponseEntity<>(goodsServices.reportExportedQuantityGoodsByDate(date),HttpStatus.OK);
    }
    @GetMapping("/report-total-quantity-imported-by-period")
    public ResponseEntity<Map<String,Integer>> reportTotalQuantityImportedByPeriod(@RequestParam(required = false) String fromDate,@RequestParam(required = false) String toDate){
        return new ResponseEntity<>(goodsServices.reportSumQuantityImportedByPeriod(fromDate,toDate),HttpStatus.OK);
    }
    @GetMapping("/report-total-quantity-exported-by-period")
    public ResponseEntity<Map<String,Integer>> reportTotalQuantityExportedByPeriod(@RequestParam(required = false) String fromDate,@RequestParam(required = false) String toDate){
        return new ResponseEntity<>(goodsServices.reportSumQuantityExportedByPeriod(fromDate,toDate),HttpStatus.OK);
    }
    @GetMapping("/statistic-of-the-top5-imported-products-by/{month}")
    public ResponseEntity<List<GoodsStaticsResponse>>statisticOfTheTop5ImportedProducts(@PathVariable int month){
        return new ResponseEntity<>(goodsServices.statisticOfTheTop5ImportedProducts(month),HttpStatus.OK);
    }
    @GetMapping("/statistic-of-the-top5-exported-products-by/{month}")
    public ResponseEntity<List<GoodsStaticsResponse>> statisticOfTheTop5ExportedProducts(@PathVariable int month){
        return new ResponseEntity<>(goodsServices.statisticOfTheTop5ExportedProducts(month),HttpStatus.OK);
    }
    @GetMapping("/statistic-of-the-top1-imported-products-by/{month}")
    public ResponseEntity<List<GoodsStaticsResponse>>statisticOfTheTop1ImportedProducts(@PathVariable int month){
        return new ResponseEntity<>(goodsServices.statisticOfTheTop1ImportedProducts(month),HttpStatus.OK);
    }
    @GetMapping("/statistic-of-the-top1-exported-products-by/{month}")
    public ResponseEntity<List<GoodsStaticsResponse>> statisticOfTheTop1ExportedProducts(@PathVariable int month){
        return new ResponseEntity<>(goodsServices.statisticOfTheTop1ExportedProducts(month),HttpStatus.OK);
    }
    @GetMapping("/statistic-of-the-total-imported_exported-products-by-current-month")
    public ResponseEntity<Map<String,Integer>> statisticOfTheTotalImportedAndExportedProductsByCurrentMonth(){
        return new ResponseEntity<>(goodsServices.statisticOfTheTotalImportedAndExportedProductsByCurrentMonth(),HttpStatus.OK);
    }
    @GetMapping("/get-all-in-warehouse-by/{warehouseCode}")
    public ResponseEntity<List<GoodsResponse>> getAllGoodsInWarehouse(@PathVariable String warehouseCode){
        return new ResponseEntity<>(goodsServices.getAllGoodsInWarehouse(warehouseCode),HttpStatus.OK);
    }

    @GetMapping("/statistic-of-the-top1-exported-products")
    public ResponseEntity<List<GoodsStaticsResponse>> statisticOfTheTop1ExportedProducts(){
        return new ResponseEntity<>(goodsServices.statisticOfTheTop1ExportedProducts(),HttpStatus.OK);
    }
    @GetMapping("/statistic-of-the-top1-imported-products")
    public ResponseEntity<List<GoodsStaticsResponse>> statisticOfTheTop1ImportedProducts(){
        return new ResponseEntity<>(goodsServices.statisticOfTheTop1ImportedProducts(),HttpStatus.OK);
    }
}
