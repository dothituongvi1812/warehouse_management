package com.example.warehouse_management.services;


import com.example.warehouse_management.payload.request.column.ColumnLocationRequest;
import com.example.warehouse_management.payload.response.ColumnLocationResponse;

import java.util.List;


public interface ColumnLocationServices {
    public List<ColumnLocationResponse> addColumns(ColumnLocationRequest columnLocationRequest);
    public List<ColumnLocationResponse> getAll();
    public ColumnLocationResponse getByCode(String code);
}
