package com.example.warehouse_management.services;


import com.example.warehouse_management.models.warehouse.ShelfStorage;
import com.example.warehouse_management.payload.request.shelf.ShelveStorageRequest;
import com.example.warehouse_management.payload.response.ShelveStorageResponse;

import java.util.List;


public interface ShelveStorageServices {
    public ShelveStorageResponse addShelfStorage(ShelveStorageRequest request);
    public List<ShelveStorageResponse> findAll();
    public ShelfStorage findShelveStorageByCode(String code);
    public ShelveStorageResponse getByCode(String code);
}
