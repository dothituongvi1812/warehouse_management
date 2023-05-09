package com.example.warehouse_management.services;


import com.example.warehouse_management.payload.request.column.ColumnLocationRequest;
import com.example.warehouse_management.payload.response.ColumnPositionResponse;

import java.util.List;


public interface ColumnLocationServices {
    public List<ColumnPositionResponse> addColumns(ColumnLocationRequest columnLocationRequest);
    public List<ColumnPositionResponse> getAll();
    public ColumnPositionResponse getByCode(String code);
}
