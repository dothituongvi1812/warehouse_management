package com.example.warehouse_management.services;

import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.request.RowLocationRequest;
import com.example.warehouse_management.payload.request.StatusRequest;
import com.example.warehouse_management.payload.response.RowLocationResponse;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface RowLocationServices {
    public List<RowLocationResponse> addRowLocations(RowLocationRequest request);
    public Page<RowLocationResponse> getAll(Integer page, Integer size);
    public RowLocationResponse getByCode(String code);
    public RowLocation findRowLocationByCode(String code);
    public RowLocationResponse mapperRowLocation(RowLocation rowLocation);
    public List<RowLocationResponse> filterStatusByCodeWarehouse(String codeWarehouse, StatusRequest statusRequest);
    public List<RowLocation> findAllRowLocationByGoodsCode(String goodCode);
    public Page<RowLocationResponse> getAllRowLocationByWarehouseCode(String goodCode,Integer page,Integer size);
    public Integer getSumCurrentCapacityByGoodsName(String goodsName);
    public List<RowLocation> findAllByGoodsNameEnoughToExport(String goodsName,int quantity);
    public Map<String,Integer> reportStockPosition();
}
