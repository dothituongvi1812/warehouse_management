package com.example.warehouse_management.services;

import com.example.warehouse_management.payload.request.warehouse.WarehouseRequest;
import com.example.warehouse_management.payload.response.WarehouseResponse;

import java.util.List;


public interface WarehouseServices {
    public WarehouseResponse addWarehouse(WarehouseRequest request);
    public List<WarehouseResponse> findAll();
    public WarehouseResponse findByCode(String code);
}
