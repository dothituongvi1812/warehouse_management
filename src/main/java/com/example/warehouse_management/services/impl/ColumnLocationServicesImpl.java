package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.warehouse.ColumnLocation;
import com.example.warehouse_management.models.warehouse.ShelfStorage;
import com.example.warehouse_management.payload.request.column.ColumnLocationRequest;
import com.example.warehouse_management.payload.response.ColumnLocationResponse;
import com.example.warehouse_management.repository.ColumnLocationRepository;
import com.example.warehouse_management.repository.ShelveStorageRepository;
import com.example.warehouse_management.services.ColumnLocationServices;
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
        ShelfStorage shelfStorage =shelveStorageRepository.findByCode(columnLocationRequest.getShelfStorageCode());
        if (shelfStorage ==null){
            throw new NotFoundGlobalException("Không tìm thấy kệ "+columnLocationRequest.getShelfStorageCode());
        }
        //validation chiều dài cột
        //nhỏ hơn chiều dài kệ
        // khi chia phải là số nguyên
        double lengthColumn=columnLocationRequest.getLength();
        double lengthShelf= shelfStorage.getLength();
        if(!(lengthColumn <= lengthShelf)){
            throw new ErrorException("Chiều dài của cột phải nhỏ hơn hoặc bằng chiều dài của kệ là: "+lengthShelf);
        }
        if(!(lengthShelf % lengthColumn == 0)){
            throw new ErrorException("Chiều dài của cột không phù hợp. Chiều dài của kệ là: "+lengthShelf);
        }

        int numberColumn= (int) (shelfStorage.getLength()/columnLocationRequest.getLength());
        int numberColumnCurrent=columnLocationRepository.findAll().size();
        String code ="CL000";
        for (int i = 0; i < numberColumn; i++) {
            ColumnLocation columnLocation =new ColumnLocation();
            columnLocation.setShelfStorage(shelfStorage);
            columnLocation.setLength(columnLocationRequest.getLength());
            columnLocation.setName(generateColumnLocationName(i+1));
            columnLocation.setCode(code+(numberColumnCurrent+i+1));
            ColumnLocation columnLocationSave=columnLocationRepository.save(columnLocation);
            ColumnLocationResponse columnLocationResponse= mapperColumnLocationResponse(columnLocationSave);
            responses.add(columnLocationResponse);
        }
        return responses;
    }

    @Override
    public List<ColumnLocationResponse> getAll() {
            List<ColumnLocationResponse> responseList =columnLocationRepository.findAll().stream()
                    .map(columnLocation ->mapperColumnLocationResponse(columnLocation) )
                    .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public ColumnLocationResponse getByCode(String code) {
        ColumnLocation columnLocation=columnLocationRepository.findByCode(code);
        ColumnLocationResponse response=mapperColumnLocationResponse(columnLocation);
        return response;
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
        columnLocationResponse.setShelfStorageCode(columnLocation.getShelfStorage().getCode());
        columnLocationResponse.setShelfStorageName(columnLocation.getShelfStorage().getName());

        return columnLocationResponse;
    }
}
