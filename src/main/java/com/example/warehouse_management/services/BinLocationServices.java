package com.example.warehouse_management.services;

import com.example.warehouse_management.models.warehouse.BinLocation;
import com.example.warehouse_management.payload.request.bin.BinLocationMoveToRequest;
import com.example.warehouse_management.payload.request.bin.BinLocationRequest;
import com.example.warehouse_management.payload.request.goods.GoodsCreatedReceiptVoucherRequest;
import com.example.warehouse_management.payload.request.bin.StatusRequest;
import com.example.warehouse_management.payload.response.BinLocationResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface BinLocationServices {
    public List<BinLocationResponse> addRowLocations(BinLocationRequest request);
    public Page<BinLocationResponse> getPage(Integer page, Integer size);
    public BinLocationResponse getByCode(String code);
    public BinLocation findRowLocationByCode(String code);
    public BinLocationResponse mapperRowLocation(BinLocation binLocation);
    public List<BinLocationResponse> filterStatusByCodeWarehouse(String codeWarehouse, StatusRequest statusRequest);
    public List<BinLocation> findAllRowLocationByGoodsCode(String goodCode);
    public Page<BinLocationResponse> getPageRowLocationByWarehouseCode(String goodCode, Integer page, Integer size);
    public Integer getSumCurrentCapacityByGoodsName(String goodsName);
    public List<BinLocation> findAllByGoodsNameEnoughToExport(String goodsName, int quantity);
    public Map<String,Integer> reportStockPosition(String codeWarehouse);
    public List<BinLocationResponse> getAll();
    public List<BinLocationResponse> getAllRowLocationByWarehouseCode(String codeWarehouse);
    List<BinLocationResponse> getAllUsablePositionForGoods(String warehouseCode, GoodsCreatedReceiptVoucherRequest request);
    String moveBin(String fromBinLocationCode, BinLocationMoveToRequest binLocationMoveToRequest);
}
