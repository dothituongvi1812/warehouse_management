package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.warehouse.ShelveStorage;
import com.example.warehouse_management.models.warehouse.Warehouse;
import com.example.warehouse_management.payload.request.ShelveStorageRequest;
import com.example.warehouse_management.payload.response.ShelveStorageResponse;
import com.example.warehouse_management.repository.ShelveStorageRepository;
import com.example.warehouse_management.repository.WarehouseRepository;
import com.example.warehouse_management.services.ShelveStorageServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ShelveStorageServicesImpl implements ShelveStorageServices {

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    ShelveStorageRepository shelfStorageRepository;

    private ModelMapper modelMapper =new ModelMapper();

    @Override
    public ShelveStorageResponse addShelfStorage(ShelveStorageRequest request) {
            Warehouse warehouse = warehouseRepository.findWarehouseByCode(request.getWarehouseCode());
            if(warehouse==null){
                throw new NotFoundGlobalException("Không tìm thấy kho "+ request.getWarehouseCode());
            }
            ShelveStorage shelveStorage =new ShelveStorage();
            shelveStorage.setCode(request.getCode());
            shelveStorage.setName(request.getName());
            shelveStorage.setHeight(request.getHeight());
            shelveStorage.setLength(request.getLength());
            shelveStorage.setWidth(request.getWidth());
            shelveStorage.setNumberOfFloors(request.getNumberOfFloor());
            shelveStorage.setWarehouse(warehouse);

            ShelveStorageResponse response = mapperShelveStorageResponse(shelveStorage);
            return response;
        }

    @Override
    public List<ShelveStorageResponse> findAll() {
        List<ShelveStorageResponse> shelveStorageResponseList=shelfStorageRepository.findAll().stream()
                .map(shelve->mapperShelveStorageResponse(shelve)).collect(Collectors.toList());
        return shelveStorageResponseList;
    }

    @Override
    public ShelveStorage findShelveStorageByCode(String code) {
        ShelveStorage shelveStorage=shelfStorageRepository.findByCode(code);
        if(shelveStorage==null)
            throw new NotFoundGlobalException("Không tìm thấy kệ "+code);
        return shelveStorage;
    }

    @Override
    public ShelveStorageResponse getByCode(String code) {
        return mapperShelveStorageResponse(findShelveStorageByCode(code));
    }

    private String generateShelveId(){
        Random rnd = new Random();
        String code = String.format("%04d",rnd.nextInt(999999));
        String shelfName = String.format("SS-"+code);
        return shelfName;
    }
    private ShelveStorageResponse mapperShelveStorageResponse(ShelveStorage shelveStorage){
        ShelveStorageResponse response = modelMapper.map(shelfStorageRepository.save(shelveStorage),ShelveStorageResponse.class);
        response.setWarehouseCode(shelveStorage.getWarehouse().getCode());
        response.setWarehouseName(shelveStorage.getWarehouse().getName());

        return response;
    }

}
