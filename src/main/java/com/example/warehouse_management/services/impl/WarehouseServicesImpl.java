package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.warehouse.Warehouse;
import com.example.warehouse_management.payload.request.ShelveStorageRequest;
import com.example.warehouse_management.payload.request.WarehouseRequest;
import com.example.warehouse_management.payload.response.WarehouseResponse;
import com.example.warehouse_management.repository.WarehouseRepository;
import com.example.warehouse_management.services.ShelveStorageServices;
import com.example.warehouse_management.services.WarehouseServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class WarehouseServicesImpl implements WarehouseServices {
    private static final Double DISTANCE_BETWEEN_TWO_SHELVES=2.0;
    private static final Double DISTANCE_BETWEEN_SHELVES_AND_WALL=1.0*2;
    private static final Double DISTANCE_CORRIDOR=5.0*2;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    ShelveStorageServices shelveStorageServices;
    private ModelMapper modelMapper =new ModelMapper();
    public WarehouseResponse addWarehouse(WarehouseRequest request){
        //validate
        if(request.getLengthShelf()>request.getLength()-DISTANCE_CORRIDOR){
            throw new RuntimeException(String.format("Chiều dài tối đa của kệ là %f "
                    ,request.getLength()-DISTANCE_CORRIDOR));
        }
        // chiều rộng kệ là số nguyên
        if (!(request.getWidthShelf() == Math.floor(request.getWidthShelf())) && !Double.isInfinite(request.getWidthShelf())) {
            throw new RuntimeException("Chiều rộng kệ là số nguyên");
        }
        if(request.getWidthShelf()> request.getWidth()-DISTANCE_BETWEEN_SHELVES_AND_WALL){
            throw new RuntimeException(String.format("Chiều rộng tối đa của kệ là %f "
                    ,request.getWidth()-DISTANCE_BETWEEN_SHELVES_AND_WALL));
        }
        if(request.getHeightShelf()> request.getHeight()-5){
            throw new RuntimeException(String.format("Chiều cao tối đa của kệ là %f "
                    ,request.getHeight()-5));
        }
        String code = generateWarehouseCode();
        Warehouse warehouse =new Warehouse();
        warehouse.setCode(code);
        warehouse.setName(request.getName());
        warehouse.setLocation(request.getLocation());
        warehouse.setHeight(request.getHeight());
        warehouse.setLength(request.getLength());
        warehouse.setWidth(request.getWidth());
        warehouse.setAcreage(request.getLength()* request.getWidth());
        warehouse.setVolume(request.getLength()* request.getWidth()* request.getHeight());
        warehouse.setStatus(EStatusStorage.TRONG);
        warehouse.setWidthShelve(request.getWidthShelf());
        warehouse.setLengthShelve(request.getLengthShelf());
        warehouse.setHeightShelve(request.getHeightShelf());
        warehouse.setNumberOfShelve(getNumberOfShelve(request.getWidth(), request.getWidthShelf()));
        Warehouse saveResponse=warehouseRepository.save(warehouse);
        WarehouseResponse response=modelMapper.map(saveResponse, WarehouseResponse.class);
        // xử lý kệ
        for (int i = 0; i < warehouse.getNumberOfShelve(); i++) {
            String nameShelve= "Kệ "+ (i+1);
            String codeShelve ="SS00"+(i+1);
                    ShelveStorageRequest shelveStorageRequest=
                            new ShelveStorageRequest(nameShelve,codeShelve, warehouse.getWidthShelve(),
                                    warehouse.getLengthShelve(),warehouse.getHeightShelve(), request.getNumberOfFloor(), warehouse.getCode());
            shelveStorageServices.addShelfStorage(shelveStorageRequest);
        }
        return response;
    }

    private int getNumberOfShelve(double widthWarehouse, double withShelve){
        int number= (int) ((widthWarehouse-DISTANCE_BETWEEN_SHELVES_AND_WALL-DISTANCE_BETWEEN_TWO_SHELVES)
                /withShelve);
        return number;
    }
    @Override
    public List<WarehouseResponse> findAll() {
        List<WarehouseResponse> responseList = warehouseRepository.findAll().stream().
                map(item -> modelMapper.map(item,WarehouseResponse.class) )
                .collect(Collectors.toList());
       return responseList;
    }

    public String generateWarehouseCode(){
        Random rnd = new Random();
        String code = String.format("%06d",rnd.nextInt(999999));
        String warehouseCode = String.format("WID-"+code);
        return warehouseCode;
    }

}
