package com.example.warehouse_management.services;


import com.example.warehouse_management.payload.request.ColumnLocationRequest;
import com.example.warehouse_management.payload.request.ShelveStorageRequest;
import com.example.warehouse_management.payload.response.ColumnLocationResponse;
import com.example.warehouse_management.payload.response.ShelveStorageResponse;

import java.util.List;


public interface ColumnLocationServices {
    public List<ColumnLocationResponse> addColumns(ColumnLocationRequest columnLocationRequest);
    public List<ColumnLocationResponse> getAll();
    public ColumnLocationResponse getByCode(String code);
}
