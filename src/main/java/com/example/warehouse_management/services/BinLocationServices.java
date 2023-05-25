package com.example.warehouse_management.services;

import com.example.warehouse_management.models.warehouse.BinPosition;
import com.example.warehouse_management.payload.request.bin.BinLocationMoveToRequest;
import com.example.warehouse_management.payload.request.bin.BinLocationRequest;
import com.example.warehouse_management.payload.request.goods.GoodsCheckedRequest;
import com.example.warehouse_management.payload.request.goods.GoodsCreatedReceiptVoucherRequest;
import com.example.warehouse_management.payload.request.bin.StatusRequest;
import com.example.warehouse_management.payload.response.BinPositionResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface BinLocationServices {
    public List<BinPositionResponse> addRowLocations(BinLocationRequest request);
    public Page<BinPositionResponse> getPage(Integer page, Integer size);
    public BinPositionResponse getByCode(String code);
    public BinPosition findRowLocationByCode(String code);
    public BinPositionResponse mapperRowLocation(BinPosition binPosition);
    public List<BinPositionResponse> filterStatusByCodeWarehouse(String codeWarehouse, StatusRequest statusRequest);
    public List<BinPosition> findAllRowLocationByGoodsCode(String goodCode);
    public Page<BinPositionResponse> getPageRowLocationByWarehouseCode(String goodCode, Integer page, Integer size);
    public Integer getSumCurrentCapacityByGoodsName(String goodsName);
    public List<BinPosition> findAllByGoodsNameEnoughToExport(String goodsName, int quantity);
    public Map<String,Integer> reportStockPosition(String codeWarehouse);
    public List<BinPositionResponse> getAll();
    public List<BinPositionResponse> getAllRowLocationByWarehouseCode(String codeWarehouse);
    List<BinPositionResponse> getAllUsablePositionForGoods(String warehouseCode, GoodsCreatedReceiptVoucherRequest request);
    String moveBin(String fromBinLocationCode, BinLocationMoveToRequest binLocationMoveToRequest);
    Page<BinPositionResponse> search(String keyword, String codeWarehouse,Integer page, Integer size);
    List<BinPositionResponse> filterByColumnLocationCode(String columnCode, String codeWarehouse);
    boolean checkRemainingVolumeForGoodsByBinCode(String binCode,GoodsCheckedRequest goodsCheckedRequest);
    BinPosition findOnePosition(int quanity, String goodsCode);
    List<BinPositionResponse> findAllByGoodsCode(String goodsCode);
    List<BinPosition> findAllByStatusEmptyOrAvailable(String warehouseCode);
}
