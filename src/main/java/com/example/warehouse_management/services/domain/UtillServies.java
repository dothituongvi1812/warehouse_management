package com.example.warehouse_management.services.domain;

import com.example.warehouse_management.models.warehouse.BinLocation;
import com.example.warehouse_management.payload.response.LocationInWarehouse;

import java.util.Random;

public class UtillServies {
    private String generateGoodCode(){
        Random rnd = new Random();
        String code = String.format("L-"+String.format("%04d",rnd.nextInt(999999)));
        return code;
    }
    public static LocationInWarehouse mapperLocationInWarehouse(BinLocation bin) {
        LocationInWarehouse locationInWarehouse = new LocationInWarehouse();
        locationInWarehouse.setNameRow(bin.getName());
        locationInWarehouse.setCodeRow(bin.getCode());
        locationInWarehouse.setNameColumn(bin.getColumnLocation().getName());
        locationInWarehouse.setCodeColumn(bin.getColumnLocation().getCode());
        locationInWarehouse.setNameShelf(bin.getColumnLocation().getShelfStorage().getName());
        locationInWarehouse.setCodeShelf(bin.getColumnLocation().getShelfStorage().getCode());
        locationInWarehouse.setNameWarehouse(bin.getColumnLocation().getShelfStorage().getWarehouse().getName());
        locationInWarehouse.setCodeWarehouse(bin.getColumnLocation().getShelfStorage().getWarehouse().getCode());
        return locationInWarehouse;
    }

}
