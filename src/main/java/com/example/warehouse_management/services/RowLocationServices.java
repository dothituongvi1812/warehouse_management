package com.example.warehouse_management.services;

import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.request.RowLocationRequest;
import com.example.warehouse_management.payload.response.RowLocationResponse;

import java.util.List;


public interface RowLocationServices {
    public List<RowLocationResponse> addRowLocations(RowLocationRequest request);
    public List<RowLocationResponse> getAll();

    public RowLocationResponse getByCode(String code);
    public RowLocation findRowLocationByCode(String code);

}
