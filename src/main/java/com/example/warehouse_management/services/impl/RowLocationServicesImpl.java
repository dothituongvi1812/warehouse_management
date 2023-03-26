package com.example.warehouse_management.services.impl;


import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.warehouse.ColumnLocation;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.models.warehouse.ShelveStorage;
import com.example.warehouse_management.payload.request.RowLocationRequest;
import com.example.warehouse_management.payload.response.RowLocationResponse;
import com.example.warehouse_management.repository.ColumnLocationRepository;
import com.example.warehouse_management.repository.RowLocationRepository;
import com.example.warehouse_management.repository.ShelveStorageRepository;
import com.example.warehouse_management.services.RowLocationServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RowLocationServicesImpl implements RowLocationServices {

    @Autowired
    ColumnLocationRepository columnLocationRepository;
    @Autowired
    RowLocationRepository rowLocationRepository;

    private ModelMapper modelMapper=new ModelMapper();
    @Override
    public List<RowLocationResponse> addRowLocations(RowLocationRequest request) {
        List<RowLocationResponse> rowLocationResponses=new ArrayList<>();
        ColumnLocation columnLocation =columnLocationRepository.findByCode(request.getColumnLocationCode());
        if(columnLocation==null){
            throw new NotFoundGlobalException("Không tìm thấy vị trí cột "+request.getColumnLocationCode());
        }
        ShelveStorage shelveStorage =columnLocation.getShelveStorage();
        int numberOfFloor=shelveStorage.getNumberOfFloors();
        int numberOfRow=rowLocationRepository.findAll().size();
        String code ="HL000";
        for (int i = 0; i < numberOfFloor; i++) {
            RowLocation rowLocation =new RowLocation();
            double height=shelveStorage.getHeight()/3;
            double volume = height*shelveStorage.getWidth()*columnLocation.getLength();
            rowLocation.setHeight(height);
            rowLocation.setWidth(shelveStorage.getWidth());
            rowLocation.setLength(columnLocation.getLength());
            rowLocation.setVolume(volume);
            rowLocation.setRemainingVolume(volume);
            rowLocation.setStatus(EStatusStorage.TRONG);
            rowLocation.setName(generateRowLocationName(i+1));
            rowLocation.setCode(code+(numberOfRow+i+1));
            rowLocation.setColumnLocation(columnLocation);
            RowLocation saveRowLocation=rowLocationRepository.save(rowLocation);
            RowLocationResponse rowLocationResponseMapper=mapperRowLocationResponse(saveRowLocation);
            rowLocationResponses.add(rowLocationResponseMapper);
        }

        return rowLocationResponses;
    }

    @Override
    public List<RowLocationResponse> findAll() {
        List<RowLocationResponse> responseList = rowLocationRepository.findAll().stream().
                map(rowLocation ->mapperRowLocationResponse(rowLocation))
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<RowLocation> findByStatusTrongAndConTrong() {
        return rowLocationRepository.findByStatusTrongAndConTrong();
    }

    @Override
    public List<RowLocation> findByGoodsName(List<String> goodsName) {
        return rowLocationRepository.findByGoodsName(goodsName);
    }

    private String generateRowLocationName(int numberRow){
        String name ="";
        switch (numberRow){
            case 1: name= "Tầng 1";
                break;
            case 2: name= "Tầng 2";
                break;
            case 3: name= "Tầng 3";
                break;

        }
        return name;

    }
    private String generateRowCode(){
        Random rnd = new Random();
        String shelfName = String.format("HH-"+String.format("%04d",rnd.nextInt(999999)));
        return shelfName;
    }
    private RowLocationResponse mapperRowLocationResponse(RowLocation rowLocation){
        String status=null;
        switch (rowLocation.getStatus()){
            case DADAY:
                status ="Đã đầy";
                break;
            case TRONG:
                status ="Trống";
                break;
            case CONCHO:
                status="Còn chỗ";
                break;
        }
        RowLocationResponse rowLocationResponseMapper=modelMapper.map(rowLocation,RowLocationResponse.class);
        rowLocationResponseMapper.setStatus(status);
        rowLocationResponseMapper.setColumnLocationCode(rowLocation.getColumnLocation().getCode());
        rowLocationResponseMapper.setColumnLocationName(rowLocation.getColumnLocation().getName());

        return rowLocationResponseMapper;
    }
}
