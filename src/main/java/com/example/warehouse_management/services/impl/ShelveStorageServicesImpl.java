package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.warehouse.ShelfStorage;
import com.example.warehouse_management.models.warehouse.Warehouse;
import com.example.warehouse_management.payload.request.shelf.ShelveStorageRequest;
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
            ShelfStorage shelfStorage =new ShelfStorage();
            shelfStorage.setCode(request.getCode());
            shelfStorage.setName(request.getName());
            shelfStorage.setHeight(request.getHeight());
            shelfStorage.setLength(request.getLength());
            shelfStorage.setWidth(request.getWidth());
            shelfStorage.setNumberOfFloors(request.getNumberOfFloor());
            shelfStorage.setWarehouse(warehouse);

            ShelveStorageResponse response = mapperShelveStorageResponse(shelfStorage);
            return response;
        }

    @Override
    public List<ShelveStorageResponse> findAll() {
        List<ShelveStorageResponse> shelveStorageResponseList=shelfStorageRepository.findAll().stream()
                .map(shelve->mapperShelveStorageResponse(shelve)).collect(Collectors.toList());
        return shelveStorageResponseList;
    }

    @Override
    public ShelfStorage findShelveStorageByCode(String code) {
        ShelfStorage shelfStorage =shelfStorageRepository.findByCode(code);
        if(shelfStorage ==null)
            throw new NotFoundGlobalException("Không tìm thấy kệ "+code);
        return shelfStorage;
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
    private ShelveStorageResponse mapperShelveStorageResponse(ShelfStorage shelfStorage){
        ShelveStorageResponse response = modelMapper.map(shelfStorageRepository.save(shelfStorage),ShelveStorageResponse.class);
        response.setWarehouseCode(shelfStorage.getWarehouse().getCode());
        response.setWarehouseName(shelfStorage.getWarehouse().getName());

        return response;
    }

}
