package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.warehouse.ColumnLocation;
import com.example.warehouse_management.models.warehouse.ShelveStorage;
import com.example.warehouse_management.models.warehouse.Warehouse;
import com.example.warehouse_management.payload.request.ColumnLocationRequest;
import com.example.warehouse_management.payload.request.ShelveStorageRequest;
import com.example.warehouse_management.payload.response.ColumnLocationResponse;
import com.example.warehouse_management.payload.response.ShelveStorageResponse;
import com.example.warehouse_management.repository.ColumnLocationRepository;
import com.example.warehouse_management.repository.ShelveStorageRepository;
import com.example.warehouse_management.repository.WarehouseRepository;
import com.example.warehouse_management.services.ColumnLocationServices;
import com.example.warehouse_management.services.ShelveStorageServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ColumnLocationServicesImpl implements ColumnLocationServices {

    @Autowired
    ShelveStorageRepository shelveStorageRepository;

    @Autowired
    ColumnLocationRepository columnLocationRepository;
    private ModelMapper modelMapper =new ModelMapper();

    @Override
    public List<ColumnLocationResponse> addColumns(ColumnLocationRequest columnLocationRequest) {
        List<ColumnLocationResponse> responses =new ArrayList<>();
        ShelveStorage shelveStorage =shelveStorageRepository.findByCode(columnLocationRequest.getShelfStorageCode());
        if (shelveStorage==null){
            throw new NotFoundGlobalException("Không tìm thấy kệ "+columnLocationRequest.getShelfStorageCode());
        }
        int numberColumn= (int) (shelveStorage.getLength()/columnLocationRequest.getLength());

        String code ="CL000";
        for (int i = 0; i < numberColumn; i++) {
            ColumnLocation columnLocation =new ColumnLocation();
            columnLocation.setStatus(EStatusStorage.TRONG);
            columnLocation.setShelveStorage(shelveStorage);
            columnLocation.setLength(columnLocationRequest.getLength());
            columnLocation.setName(generateColumnLocationName(i+1));
            columnLocation.setCode(code+(i+1));
            ColumnLocation columnLocationSave=columnLocationRepository.save(columnLocation);
            ColumnLocationResponse columnLocationResponse= mapperColumnLocationResponse(columnLocationSave);
            responses.add(columnLocationResponse);
        }
        return responses;
    }

    @Override
    public List<ColumnLocationResponse> findAll() {
            List<ColumnLocationResponse> responseList =columnLocationRepository.findAll().stream()
                    .map(columnLocation ->mapperColumnLocationResponse(columnLocation) )
                    .collect(Collectors.toList());
        return responseList;
    }
    private String generateShelveId(){
        Random rnd = new Random();
        String code = String.format("%04d",rnd.nextInt(999999));
        String shelfName = String.format("SS-"+code);
        return shelfName;
    }
    private String generateColumnLocationName(int numberCol){
        String name ="";
        switch (numberCol){
            case 1: name= "Cột 1";
                break;
            case 2: name= "Cột 2";
                break;
            case 3: name= "Cột 3";
                break;
            case 4: name= "Cột 4";
                break;
            case 5: name= "Cột 5";
                break;
            case 6: name= "Cột 6";
                break;
            case 7: name= "Cột 7";
                break;
            case 8: name= "Cột 8";
                break;
            case 9: name= "Cột 9";
                break;
            case 10: name= "Cột 10";
                break;
        }
        return name;

    }
    private ColumnLocationResponse mapperColumnLocationResponse(ColumnLocation columnLocation){
        ColumnLocationResponse columnLocationResponse =modelMapper.map(columnLocation,ColumnLocationResponse.class);
        columnLocationResponse.setShelfStorageCode(columnLocation.getShelveStorage().getCode());
        columnLocationResponse.setShelfStorageName(columnLocation.getShelveStorage().getName());

        return columnLocationResponse;
    }
}
