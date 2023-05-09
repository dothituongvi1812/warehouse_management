package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.warehouse.BinPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinLocationRepository extends CrudRepository<BinPosition,Long> {
    Page<BinPosition> findAll(Pageable pageable);
    List<BinPosition> findAll();
    BinPosition findByCode(String code);
    BinPosition findTopByOrderByIdDesc();
    @Query(nativeQuery = true,value = "select * from bin_positions bl \n" +
            "order by bl.volume asc\n" +
            "limit 1")
    BinPosition findBinLocationMinVolume();
    @Query(nativeQuery = true,
            value = "select * from bin_positions rl \n" +
                    "where rl.remaining_volume >=:volumeGoods and rl.status ='EMPTY' and rl.code not in :codeRowLocations\n" +
                    "order by rl.id asc limit 1")
    BinPosition findTopOneByStatusEmptyAndRemainingVolumeGreaterThanEqual(double volumeGoods, List<String> codeRowLocations );

    @Query(nativeQuery = true,
            value = "select rl.* from bin_positions rl \n" +
                    "join column_positions cl on rl.column_position_id = cl.id \n" +
                    "join shelf_storages ss on ss.id = cl.shelf_storage_id \n" +
                    "join warehouse w on w.id =ss.warehouse_id \n" +
                    "where rl.status =:status and w.code =:code")
    List<BinPosition> filterStatusByWarehouseCode(String code, String status);

    @Query(nativeQuery = true, value = "select rl.* from bin_positions rl \n" +
            "join goods g ON rl.goods_id = g.id \n" +
            "where g.code =:goodsCode\n" +
            "order by rl.current_capacity desc \n")
    List<BinPosition> findByGoodsCode(String goodsCode);
    @Query(nativeQuery = true,
            value = "select rl.* from bin_positions rl \n" +
                    "join column_positions cl on rl.column_position_id = cl.id \n" +
                    "join shelf_storages ss on ss.id = cl.shelf_storage_id \n" +
                    "join warehouse w on w.id =ss.warehouse_id \n" +
                    "where w.code =:code")
    Page<BinPosition> getPageRowLocationByWarehouseCode(String code, Pageable pageable);

    @Query(nativeQuery = true,value = "select sum(current_capacity)  from bin_positions rl \n" +
            "join goods g ON rl.goods_id = g.id \n" +
            "where g.name =:goodsName\n" +
            "group by goods_id ")
    Integer getSumCurrentCapacityByGoodsName(String goodsName);

    @Query(nativeQuery = true,value = "select * from bin_positions rl  \n" +
            "join goods g ON rl.goods_id = g.id \n" +
            "where g.name =:goodsName and (select sum(current_capacity)  from bin_positions rl \n" +
            "join goods g ON rl.goods_id = g.id \n" +
            "where g.name =:goodsName\n" +
            "group by goods_id ) >=:quantity\n" +
            "order by current_capacity desc ")
    List<BinPosition> findByGoodsNameEnoughToExport(String goodsName, int quantity);

    @Query(nativeQuery = true,value = "select status,count(status) from bin_positions bl \n" +
            "join column_positions cl on bl.column_position_id = cl.id\n" +
            "join shelf_storages ss on ss.id = cl.shelf_storage_id \n" +
            "join warehouse w on w.id = ss.warehouse_id \n" +
            "where w.code  =:codeWarehouse\n" +
            "group by status ")
    List<Object[]> reportStockPosition(String codeWarehouse);
    @Query(nativeQuery = true,
            value = "select rl.* from bin_positions rl \n" +
                    "join column_positions cl on rl.column_position_id = cl.id \n" +
                    "join shelf_storages ss on ss.id = cl.shelf_storage_id \n" +
                    "join warehouse w on w.id =ss.warehouse_id \n" +
                    "where w.code =:code")
    List<BinPosition> getAllRowLocationByWarehouseCode(String code);

    @Query(nativeQuery = true,value = "select bl.* from bin_positions bl\n" +
            "join column_positions cl ON bl.column_position_id = cl.id\n" +
            "join shelf_storages ss on cl.shelf_storage_id = ss.id\n" +
            "join warehouse w on ss.warehouse_id = w.id \n" +
            "where w.code =:warehouseCode and bl.status = 'EMPTY'\n" +
            "and bl.id not in :usingBinLocation")
    List<BinPosition> getAllUsablePositionForGoodsNotExisted(String warehouseCode, List<Long> usingBinLocation);

    @Query(nativeQuery = true,value = "select bl.* from bin_positions bl\n" +
            "join column_positions cl ON bl.column_position_id = cl.id \n" +
            "join shelf_storages ss on cl.shelf_storage_id = ss.id \n" +
            "join warehouse w on ss.warehouse_id = w.id \n" +
            "join goods g ON bl.goods_id = g.id \n" +
            "where w.code =:warehouseCode and bl.remaining_volume >=:volume and g.code=:codeGoods\n" +
            "and bl.id not in :usingBinLocation")
    List<BinPosition> getAllUsablePositionForGoodsExisted(String warehouseCode, double volume, String codeGoods, List<Long> usingBinLocation);

    @Query(nativeQuery = true,value = "select distinct  irvd.bin_position_id  from inventory_receipt_vouchers irv \n" +
            "join inventory_receipt_voucher_details irvd  on irv.id = irvd.inventory_receipt_voucher_id \n" +
            "where status = 'NOT_YET_IMPORTED'")
    List<Long> getAllUsingBinLocation();

    @Query(nativeQuery = true,value = "select * from bin_positions bl \n" +
            "where bl.status = 'EMPTY' or bl.status = 'AVAILABLE'")
    List<BinPosition> getAllBinStatusEmptyAndAvailable();

    @Query(nativeQuery = true,value = "select bl.* from bin_positions bl \n" +
            "join column_positions cl ON bl.column_position_id = cl.id \n" +
            "join shelf_storages ss on cl.shelf_storage_id = ss.id \n" +
            "join warehouse w on ss.warehouse_id = w.id \n" +
            "where w.code =:codeWarehouse \n" +
            "and bl.code like  '%' || :keyword || '%' \n" +
            "or bl.\"name\"  like '%' || :keyword || '%' ")
    List<BinPosition> search(String keyword, String codeWarehouse);

    @Query(nativeQuery = true,value = "select bl.* from bin_positions bl \n" +
            "join column_positions cl ON bl.column_position_id = cl.id \n" +
            "join shelf_storages ss on cl.shelf_storage_id = ss.id \n" +
            "join warehouse w on ss.warehouse_id = w.id \n" +
            "where w.code =:codeWarehouse \n" +
            "and cl.code like  '%' || :columnCode || '%' ")
    List<BinPosition> filterByColumnCode(String columnCode, String codeWarehouse);


}
