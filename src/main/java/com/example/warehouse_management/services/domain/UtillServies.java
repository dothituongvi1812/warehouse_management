package com.example.warehouse_management.services.domain;

import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.response.LocationInWarehouse;

import java.util.Random;

public class UtillServies {
    private String generateGoodCode(){
        Random rnd = new Random();
        String code = String.format("L-"+String.format("%04d",rnd.nextInt(999999)));
        return code;
    }
    public static LocationInWarehouse mapperLocationInWarehouse(RowLocation rowLocation) {
        LocationInWarehouse locationInWarehouse = new LocationInWarehouse();
        locationInWarehouse.setNameRow(rowLocation.getName());
        locationInWarehouse.setCodeRow(rowLocation.getCode());
        locationInWarehouse.setNameColumn(rowLocation.getColumnLocation().getName());
        locationInWarehouse.setCodeColumn(rowLocation.getColumnLocation().getCode());
        locationInWarehouse.setNameShelf(rowLocation.getColumnLocation().getShelveStorage().getName());
        locationInWarehouse.setCodeShelf(rowLocation.getColumnLocation().getShelveStorage().getCode());
        locationInWarehouse.setNameWarehouse(rowLocation.getColumnLocation().getShelveStorage().getWarehouse().getName());
        locationInWarehouse.setCodeWarehouse(rowLocation.getColumnLocation().getShelveStorage().getWarehouse().getCode());
        return locationInWarehouse;
    }

}
