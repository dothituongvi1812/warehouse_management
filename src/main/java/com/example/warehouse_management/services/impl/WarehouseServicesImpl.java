package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.warehouse.ColumnLocation;
import com.example.warehouse_management.models.warehouse.ShelveStorage;
import com.example.warehouse_management.models.warehouse.Warehouse;
import com.example.warehouse_management.payload.request.ColumnLocationRequest;
import com.example.warehouse_management.payload.request.RowLocationRequest;
import com.example.warehouse_management.payload.request.ShelveStorageRequest;
import com.example.warehouse_management.payload.request.WarehouseRequest;
import com.example.warehouse_management.payload.response.WarehouseResponse;
import com.example.warehouse_management.repository.ColumnLocationRepository;
import com.example.warehouse_management.repository.ShelveStorageRepository;
import com.example.warehouse_management.repository.WarehouseRepository;
import com.example.warehouse_management.services.ColumnLocationServices;
import com.example.warehouse_management.services.RowLocationServices;
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
    @Autowired
    ShelveStorageRepository shelveStorageRepository;
    @Autowired
    ColumnLocationServices columnLocationServices;
    @Autowired
    ColumnLocationRepository columnLocationRepository;
    @Autowired
    RowLocationServices rowLocationServices;
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
        if(request.getWidthShelf()> request.getWidth()-DISTANCE_BETWEEN_SHELVES_AND_WALL-DISTANCE_BETWEEN_TWO_SHELVES){
            throw new RuntimeException(String.format("Chiều rộng tối đa của kệ là %f "
                    ,request.getWidth()-DISTANCE_BETWEEN_SHELVES_AND_WALL-DISTANCE_BETWEEN_TWO_SHELVES));
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
        Warehouse saveResponse=warehouseRepository.save(warehouse);
        WarehouseResponse response=modelMapper.map(saveResponse, WarehouseResponse.class);
        int numberOfShelve = getNumberOfShelve(request.getWidth(), request.getWidthShelf());
        // xử lý kệ
        int numberShelveInWarehouse = shelveStorageServices.findAll().size();
        createShelveOfWarehouse(request,warehouse,numberShelveInWarehouse,numberOfShelve,request.getNumberOfFloor());
        //xử lý cột
        List<ShelveStorage> shelveInWarehouse=shelveStorageRepository.findAll();
        createColumnLocationOfShelve(request.getLengthOfColumn(), shelveInWarehouse);
        //xử lý vị trí
        List<ColumnLocation> columnLocationInShelves=columnLocationRepository.findAll();
        createRowLocationOfColumn(columnLocationInShelves);
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

    @Override
    public WarehouseResponse findByCode(String code) {
        Warehouse warehouse =warehouseRepository.findWarehouseByCode(code);
        if(warehouse==null)
            throw new NotFoundGlobalException("Không tìm thấy kho "+code);
        return modelMapper.map(warehouse, WarehouseResponse.class);
    }

    public String generateWarehouseCode(){
        Warehouse warehouse =warehouseRepository.findTopByOrderByIdDesc();
        if(warehouse==null){
            return "W0001";
        }
        long id=warehouse.getId();
        String code = String.format("W000%d",id+1);
        return code;
    }
    private void createShelveOfWarehouse(WarehouseRequest warehouseRequest,Warehouse warehouse,int numberShelveInWarehouse,int numberShelveToAdd,int numberOfFloor){
        for (int i = 0; i < numberShelveToAdd; i++) {
            String nameShelve= "Kệ "+ (i+1);
            String codeShelve ="SS00"+(numberShelveInWarehouse+i+1);
            ShelveStorageRequest shelveStorageRequest=
                    new ShelveStorageRequest(nameShelve,codeShelve, warehouseRequest.getWidthShelf(),
                            warehouseRequest.getLengthShelf(),warehouseRequest.getHeightShelf(), numberOfFloor, warehouse.getCode());
            shelveStorageServices.addShelfStorage(shelveStorageRequest);
        }
    }
    private void createColumnLocationOfShelve(double lengthOfColumn,List<ShelveStorage> shelveStorages){
        for (ShelveStorage shelveStorage:shelveStorages) {
            columnLocationServices.addColumns(new ColumnLocationRequest(lengthOfColumn,shelveStorage.getCode()));
        }
    }
    private void createRowLocationOfColumn(List<ColumnLocation> columnLocations){
        for (ColumnLocation columnLocation:columnLocations) {
            rowLocationServices.addRowLocations(new RowLocationRequest(columnLocation.getCode()));
        }
    }
}
