package com.example.warehouse_management.services;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.payload.request.goods.GoodsAddRequest;
import com.example.warehouse_management.payload.request.goods.GoodsRequest;
import com.example.warehouse_management.payload.request.goods.UpdateGoodsRequest;
import com.example.warehouse_management.payload.response.GoodsResponse;
import com.example.warehouse_management.payload.response.GoodsStaticsResponse;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface GoodsServices {
    public GoodsResponse addGoods(GoodsAddRequest goodsRequest);
    public Goods createGoods(GoodsRequest goodsRequest);
    public Page<GoodsResponse> getPage(Integer page, Integer size);
    public GoodsResponse getByCode(String code);
    public Page<GoodsResponse> searchByCodeOrName(String keyword,Integer page, Integer size);
    public Goods findGoodByCode(String code);
    public Goods findGoodByName(String name);
    public GoodsResponse mapperGoods(Goods goods);
    public List<GoodsResponse> getAllByCategoryCode(String categoryCode);
    public List<GoodsResponse> getAll();
    public Integer getCurrentQuantityOfGoodsInWarehouse(String goodsCode);
    Map<String, Integer> countCurrentQuantityOfGoodsInWarehouse();
    Map<String, Integer> countCurrentQuantityOfGoodsByWarehouseCode(String warehouseCode);
    public GoodsResponse updateGoods(String goodsCode, UpdateGoodsRequest updateGoodsRequest);
    public Map<String,Integer> reportImportedQuantityGoodsByDate(String date);
    public Map<String,Integer> reportExportedQuantityGoodsByDate(String date);
    public Map<String,Integer> reportSumQuantityImportedByPeriod(String fromDate,String toDate);
    public Map<String,Integer> reportSumQuantityExportedByPeriod(String fromDate,String toDate);
    public List<GoodsStaticsResponse> statisticOfTheTop5ImportedProducts(int month);
    public List<GoodsStaticsResponse> statisticOfTheTop5ExportedProducts(int month);
    public Map<String,Integer> statisticOfTheTotalImportedAndExportedProductsByCurrentMonth();
    public List<GoodsStaticsResponse> statisticOfTheTop1ImportedProducts(int month);
    public List<GoodsStaticsResponse> statisticOfTheTop1ExportedProducts(int month);
    public List<GoodsResponse> getAllGoodsInWarehouse(String warehouseCode);
    public List<GoodsStaticsResponse> statisticOfTheTop1ExportedProducts();
    public List<GoodsStaticsResponse> statisticOfTheTop1ImportedProducts();

}
