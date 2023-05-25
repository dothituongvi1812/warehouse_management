package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.warehouse.BinPosition;
import com.example.warehouse_management.models.warehouse.ColumnPosition;
import com.example.warehouse_management.models.warehouse.ShelfStorage;
import com.example.warehouse_management.models.warehouse.Warehouse;
import com.example.warehouse_management.payload.request.column.ColumnLocationRequest;
import com.example.warehouse_management.payload.request.bin.BinLocationRequest;
import com.example.warehouse_management.payload.request.shelf.ShelveStorageRequest;
import com.example.warehouse_management.payload.request.warehouse.WarehouseRequest;
import com.example.warehouse_management.payload.response.WarehouseResponse;
import com.example.warehouse_management.repository.ColumnLocationRepository;
import com.example.warehouse_management.repository.ShelveStorageRepository;
import com.example.warehouse_management.repository.WarehouseRepository;
import com.example.warehouse_management.services.ColumnLocationServices;
import com.example.warehouse_management.services.BinLocationServices;
import com.example.warehouse_management.services.ShelveStorageServices;
import com.example.warehouse_management.services.WarehouseServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
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
    BinLocationServices binLocationServices;
    private ModelMapper modelMapper =new ModelMapper();
    public WarehouseResponse addWarehouse(WarehouseRequest request){
        Warehouse warehouse1 = warehouseRepository.findByName(request.getName());
        if(!ObjectUtils.isEmpty(warehouse1))
            throw new ErrorException("Tên nhà kho đã tồn tại");
        Warehouse warehouse2 = warehouseRepository.findByLocation(request.getLocation());
        if(!ObjectUtils.isEmpty(warehouse2))
            throw new ErrorException("Vị trí này đã tồn tại");
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
        warehouse.setStatus(EStatusStorage.EMPTY);
        Warehouse saveResponse=warehouseRepository.save(warehouse);
        WarehouseResponse response=mapperWarehouseResponse(saveResponse);
        int numberOfShelve = getNumberOfShelve(request.getWidth(), request.getWidthShelf());
        // xử lý kệ
        int numberShelveInWarehouse = shelveStorageServices.findAll().size();
        createShelveOfWarehouse(request,warehouse,numberShelveInWarehouse,numberOfShelve,request.getNumberOfFloor());
        //xử lý cột
        List<ShelfStorage> shelveInWarehouse=shelveStorageRepository.findAllByWarehouse(warehouse.getCode());
        createColumnLocationOfShelve(request.getLengthOfColumn(), shelveInWarehouse);
        //xử lý vị trí
        List<ColumnPosition> columnPositionInShelves =columnLocationRepository.findAll();
        createRowLocationOfColumn(columnPositionInShelves);
        return response;
    }

    private int getNumberOfShelve(double widthWarehouse, double withShelve){
        int number= (int) ((widthWarehouse-DISTANCE_BETWEEN_SHELVES_AND_WALL-DISTANCE_BETWEEN_TWO_SHELVES)
                /withShelve);
        return number;
    }
    @Override
    public List<WarehouseResponse> findAll() {
        warehouseRepository.findAll().stream().forEach(e->{
            List<BinPosition> binPositionList = binLocationServices.findAllByStatusEmptyOrAvailable(e.getCode());
            if(CollectionUtils.isEmpty(binPositionList)){
                e.setStatus(EStatusStorage.FULL);
                warehouseRepository.save(e);
            }
        });
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
        return mapperWarehouseResponse(warehouse);
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
    private void createColumnLocationOfShelve(double lengthOfColumn,List<ShelfStorage> shelfStorages){
        for (ShelfStorage shelfStorage : shelfStorages) {
            columnLocationServices.addColumns(new ColumnLocationRequest(lengthOfColumn, shelfStorage.getCode()));
        }
    }
    private void createRowLocationOfColumn(List<ColumnPosition> columnPositions){
        for (ColumnPosition columnPosition : columnPositions) {
            binLocationServices.addRowLocations(new BinLocationRequest(columnPosition.getCode()));
        }
    }
    private WarehouseResponse mapperWarehouseResponse(Warehouse warehouse){
        String status = null;
        switch (warehouse.getStatus()) {
            case FULL:
                status = "Đã đầy";
                break;
            case EMPTY:
                status = "Trống";
                break;
            case AVAILABLE:
                status = "Còn chỗ";
                break;
        }
        WarehouseResponse warehouseResponse = modelMapper.map(warehouse,WarehouseResponse.class);
        warehouseResponse.setStatus(status);

        return warehouseResponse;
    }
}
