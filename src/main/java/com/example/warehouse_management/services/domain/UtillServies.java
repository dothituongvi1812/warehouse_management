package com.example.warehouse_management.services.domain;

import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.models.warehouse.BinPosition;
import com.example.warehouse_management.payload.request.goods.GoodsRequest;
import com.example.warehouse_management.payload.response.LocationInWarehouse;

import java.util.Arrays;

public class UtillServies {

    public static LocationInWarehouse mapperLocationInWarehouse(BinPosition bin) {
        LocationInWarehouse locationInWarehouse = new LocationInWarehouse();
        locationInWarehouse.setNameRow(bin.getName());
        locationInWarehouse.setCodeRow(bin.getCode());
        locationInWarehouse.setNameColumn(bin.getColumnPosition().getName());
        locationInWarehouse.setCodeColumn(bin.getColumnPosition().getCode());
        locationInWarehouse.setNameShelf(bin.getColumnPosition().getShelfStorage().getName());
        locationInWarehouse.setCodeShelf(bin.getColumnPosition().getShelfStorage().getCode());
        locationInWarehouse.setNameWarehouse(bin.getColumnPosition().getShelfStorage().getWarehouse().getName());
        locationInWarehouse.setCodeWarehouse(bin.getColumnPosition().getShelfStorage().getWarehouse().getCode());
        return locationInWarehouse;
    }
    public static boolean validateGoods(GoodsRequest goodsRequest, BinPosition binPosition){

      boolean check= compareSizeShelfAndGoods(
                        createArraySize(binPosition.getWidth(), binPosition.getHeight(), binPosition.getLength()),
                        createArraySize(goodsRequest.getWidth(), goodsRequest.getHeight(), goodsRequest.getLength()),
                        goodsRequest.getName());
      return check;

    }
    private static boolean compareSizeShelfAndGoods(double[] sizeShelf, double[] sizeGoods, String goodsName) {
        if (sizeShelf[0] >= sizeGoods[0] && sizeShelf[1] >= sizeGoods[1] && sizeShelf[2] >= sizeGoods[2])
            return true;
        else
            throw new ErrorException(goodsName + " có kích thước quá lớn,kho không thể chứa");
    }
    private static double[] createArraySize(double width, double height, double length) {
        double[] size = {width, height, length};
        Arrays.sort(size);
        return size;

    }
}
