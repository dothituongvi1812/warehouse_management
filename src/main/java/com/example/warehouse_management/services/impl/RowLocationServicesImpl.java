package com.example.warehouse_management.services.impl;


import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.warehouse.ColumnLocation;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.models.warehouse.ShelveStorage;
import com.example.warehouse_management.payload.request.RowLocationRequest;
import com.example.warehouse_management.payload.request.StatusRequest;
import com.example.warehouse_management.payload.response.GoodsResponse;
import com.example.warehouse_management.payload.response.RowLocationResponse;
import com.example.warehouse_management.repository.ColumnLocationRepository;
import com.example.warehouse_management.repository.RowLocationRepository;
import com.example.warehouse_management.repository.ShelveStorageRepository;
import com.example.warehouse_management.services.GoodsServices;
import com.example.warehouse_management.services.RowLocationServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
    @Autowired
    private GoodsServices goodsServices;
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
            rowLocation.setStatus(EStatusStorage.EMPTY);
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
    public List<RowLocationResponse> getAll() {
        List<RowLocationResponse> responseList = rowLocationRepository.findAll().stream().
                map(rowLocation ->mapperRowLocationResponse(rowLocation))
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public RowLocationResponse getByCode(String code) {
        return mapperRowLocationResponse(findRowLocationByCode(code));
    }

    @Override
    public RowLocation findRowLocationByCode(String code) {
        RowLocation rowLocation = rowLocationRepository.findByCode(code);
        if(rowLocation==null)
            throw new NotFoundGlobalException("Không tìm thấy vị trí "+code);

        return rowLocation ;
    }

    @Override
    public RowLocationResponse mapperRowLocation(RowLocation rowLocation) {
        return mapperRowLocationResponse(rowLocation);
    }

    @Override
    public List<RowLocationResponse> filterStatusByCodeWarehouse(String codeWarehouse, StatusRequest statusRequest) {
        String request = statusRequest.getStatus();
        String status="";
        switch (request){
            case "Trống":
            case "trống":
            case "TRỐNG":
                status = EStatusStorage.EMPTY.name();
                break;
            case "Còn chỗ":
            case "CÒN CHỖ":
            case "còn chỗ":
                status = EStatusStorage.AVAILABLE.name();
                break;
            default: status =EStatusStorage.FULL.name(); ;
        }
        List<RowLocationResponse> responseList = rowLocationRepository.filterStatusByWarehouseCode(codeWarehouse,status)
                .stream().map(item->mapperRowLocationResponse(item)).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<RowLocation> findAllRowLocationByGoodsCode(String goodCode) {
        return rowLocationRepository.findByGoodsCode(goodCode);
    }

    @Override
    public List<RowLocationResponse> getAllRowLocationByWarehouseCode(String warehouseCode) {
        List<RowLocationResponse> rowLocationResponses= rowLocationRepository.getAllRowLocationByWarehouseCode(warehouseCode)
                .stream().map(item->mapperRowLocationResponse(item)).collect(Collectors.toList());
        return rowLocationResponses;
    }

    @Override
    public Integer getSumCurrentCapacityByGoodsName(String goodsName) {
    return rowLocationRepository.getSumCurrentCapacityByGoodsName(goodsName);
    }

    @Override
    public List<RowLocation> findAllByGoodsNameEnoughToExport(String goodsName, int quantity) {
        return rowLocationRepository.findByGoodsNameEnoughToExport(goodsName,quantity);
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
            case FULL:
                status ="Đã đầy";
                break;
            case EMPTY:
                status ="Trống";
                break;
            case AVAILABLE:
                status="Còn chỗ";
                break;
        }
        RowLocationResponse rowLocationResponseMapper=modelMapper.map(rowLocation,RowLocationResponse.class);
        rowLocationResponseMapper.setStatus(status);
        rowLocationResponseMapper.setCodeRow(rowLocation.getCode());
        rowLocationResponseMapper.setNameRow(rowLocation.getName());
        rowLocationResponseMapper.setCodeColumn(rowLocation.getColumnLocation().getCode());
        rowLocationResponseMapper.setNameColumn(rowLocation.getColumnLocation().getName());
        rowLocationResponseMapper.setCodeShelf(rowLocation.getColumnLocation().getShelveStorage().getCode());
        rowLocationResponseMapper.setNameShelf(rowLocation.getColumnLocation().getShelveStorage().getName());
        rowLocationResponseMapper.setNameWarehouse(rowLocation.getColumnLocation().getShelveStorage().getWarehouse().getName());
        rowLocationResponseMapper.setCodeWarehouse(rowLocation.getColumnLocation().getShelveStorage().getWarehouse().getCode());
        if(ObjectUtils.isEmpty(rowLocation.getGoods()))
            rowLocationResponseMapper.setGoods(null);
        else
            rowLocationResponseMapper.setGoods(goodsServices.mapperGoods(rowLocation.getGoods()));
        return rowLocationResponseMapper;
    }
}
