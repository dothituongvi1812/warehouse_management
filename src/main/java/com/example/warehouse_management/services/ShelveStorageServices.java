package com.example.warehouse_management.services;


import com.example.warehouse_management.models.warehouse.ShelveStorage;
import com.example.warehouse_management.payload.request.ShelveStorageRequest;
import com.example.warehouse_management.payload.request.ShelveStorageRequestSave;
import com.example.warehouse_management.payload.response.ShelveStorageResponse;

import java.util.List;


public interface ShelveStorageServices {
    public ShelveStorageResponse addShelfStorage(ShelveStorageRequest request);
    public List<ShelveStorageResponse> findAll();
    public ShelveStorage findShelveStorageByCode(String code);
    public ShelveStorageResponse getByCode(String code);
}
