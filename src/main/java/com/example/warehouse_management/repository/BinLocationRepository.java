package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.warehouse.BinLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinLocationRepository extends CrudRepository<BinLocation,Long> {
    Page<BinLocation> findAll(Pageable pageable);
    List<BinLocation> findAll();
    @Query("SELECT rl FROM BinLocation rl WHERE rl.status ='EMPTY' OR rl.status='AVAILABLE' ")
    List<BinLocation> findByStatusTrongAndConTrong();
    @Query("SELECT rl from BinLocation rl join Goods g ON rl.goods.id = g.id where g.name LIKE :goodsName and rl.code not in :codeRowLocations\n")
    List<BinLocation> findByGoodsName(String goodsName, List<String> codeRowLocations);
    BinLocation findByCode(String code);
    BinLocation findTopByOrderByIdDesc();
    @Query(nativeQuery = true,value = "select * from bin_locations bl \n" +
            "order by bl.volume asc\n" +
            "limit 1")
    BinLocation findBinLocationMinVolume();
    @Query(nativeQuery = true,
            value = "select * from bin_locations rl \n" +
                    "where rl.remaining_volume >=:volumeGoods and rl.status ='EMPTY' and rl.code not in :codeRowLocations\n" +
                    "order by rl.id asc limit 1")
    BinLocation findTopOneByStatusEmptyAndRemainingVolumeGreaterThanEqual(double volumeGoods, List<String> codeRowLocations );


    @Query(nativeQuery = true,
            value = "select * from bin_locations rl \n" +
                    "where rl.remaining_volume >=:volumeGoods and rl.status ='EMPTY' and rl.code not in :codeRowLocations\n" +
                    "order by rl.id asc limit :limit")
    public List<BinLocation> findTopNByStatusAndRemainingVolume(double volumeGoods , int limit, List<String> codeRowLocations);

    @Query(nativeQuery = true,
            value = "select rl from bin_locations rl \n" +
                    "join goods g ON rl.goods_id = g.id \n" +
                    "where rl.status ='AVAILABLE' \n" +
                    "and g.name =:goodsName \n" +
                    "and rl.remaining_volume >=:volumeGoods\n" +
                    "and rl.code not in :codeRowLocations")
    public List<BinLocation> findByStatusAndRemainingVolumeAndGoodsName(String goodsName, double volumeGoods , List<String> codeRowLocations);

    @Query(nativeQuery = true,
            value = "select * from bin_locations rl \n" +
                    "where rl.status ='EMPTY' and rl.code not in :codeRowLocations\n" +
                    "order by rl.id asc limit 1")
    BinLocation findTopOneByStatusEmpty(List<String> codeRowLocations );

    @Query(nativeQuery = true,
            value = "select rl.* from bin_locations rl \n" +
                    "join column_locations cl on rl.column_location_id = cl.id \n" +
                    "join shelve_storages ss on ss.id = cl.shelf_storage_id \n" +
                    "join warehouse w on w.id =ss.warehouse_id \n" +
                    "where rl.status =:status and w.code =:code")
    List<BinLocation> filterStatusByWarehouseCode(String code, String status);

    @Query(nativeQuery = true, value = "select rl.* from bin_locations rl \n" +
            "join goods g ON rl.goods_id = g.id \n" +
            "where g.code =:goodsCode\n" +
            "order by rl.current_capacity desc \n")
    List<BinLocation> findByGoodsCode(String goodsCode);
    @Query(nativeQuery = true,
            value = "select rl.* from bin_locations rl \n" +
                    "join column_locations cl on rl.column_location_id = cl.id \n" +
                    "join shelve_storages ss on ss.id = cl.shelf_storage_id \n" +
                    "join warehouse w on w.id =ss.warehouse_id \n" +
                    "where w.code =:code")
    Page<BinLocation> getPageRowLocationByWarehouseCode(String code, Pageable pageable);

    @Query(nativeQuery = true,value = "select sum(current_capacity)  from bin_locations rl \n" +
            "join goods g ON rl.goods_id = g.id \n" +
            "where g.name =:goodsName\n" +
            "group by goods_id ")
    Integer getSumCurrentCapacityByGoodsName(String goodsName);

    @Query(nativeQuery = true,value = "select * from bin_locations rl  \n" +
            "join goods g ON rl.goods_id = g.id \n" +
            "where g.name =:goodsName and (select sum(current_capacity)  from bin_locations rl \n" +
            "join goods g ON rl.goods_id = g.id \n" +
            "where g.name =:goodsName\n" +
            "group by goods_id ) >=:quantity\n" +
            "order by current_capacity desc ")
    List<BinLocation> findByGoodsNameEnoughToExport(String goodsName, int quantity);

    @Query(nativeQuery = true,value = "select status,count(status) from bin_locations bl \n" +
            "join column_locations cl on bl.column_location_id = cl.id\n" +
            "join shelve_storages ss on ss.id = cl.shelf_storage_id \n" +
            "join warehouse w on w.id = ss.warehouse_id \n" +
            "where w.code  =:codeWarehouse\n" +
            "group by status ")
    List<Object[]> reportStockPosition(String codeWarehouse);
    @Query(nativeQuery = true,
            value = "select rl.* from bin_locations rl \n" +
                    "join column_locations cl on rl.column_location_id = cl.id \n" +
                    "join shelve_storages ss on ss.id = cl.shelf_storage_id \n" +
                    "join warehouse w on w.id =ss.warehouse_id \n" +
                    "where w.code =:code")
    List<BinLocation> getAllRowLocationByWarehouseCode(String code);

    @Query(nativeQuery = true,value = "select bl.* from bin_locations bl\n" +
            "join column_locations cl ON bl.column_location_id = cl.id\n" +
            "join shelve_storages ss on cl.shelf_storage_id = ss.id\n" +
            "join warehouse w on ss.warehouse_id = w.id \n" +
            "where w.code =:warehouseCode and bl.status = 'EMPTY'\n" +
            "and bl.id not in :usingBinLocation")
    List<BinLocation> getAllUsablePositionForGoodsNotExisted(String warehouseCode,List<Long> usingBinLocation);

    @Query(nativeQuery = true,value = "select bl.* from bin_locations bl \n" +
            "join column_locations cl ON bl.column_location_id = cl.id \n" +
            "join shelve_storages ss on cl.shelf_storage_id = ss.id \n" +
            "join warehouse w on ss.warehouse_id = w.id \n" +
            "join goods g ON bl.goods_id = g.id \n" +
            "where w.code =:warehouseCode and bl.remaining_volume >=:volume and g.code=:codeGoods" +
            "and and bl.id not in :usingBinLocation")
    List<BinLocation> getAllUsablePositionForGoodsExisted(String warehouseCode,double volume,String codeGoods,List<Long> usingBinLocation);

    @Query(nativeQuery = true,value = "select distinct  rvd.bin_location_id  from inventory_receipt_vouchers irv \n" +
            "join receipt_voucher_details rvd  on irv.id = rvd.receipt_voucher_id \n" +
            "where status ='NOT_YET_IMPORTED' ")
    List<Long> getAllUsingBinLocation();

    @Query(nativeQuery = true,value = "select * from bin_locations bl \n" +
            "where bl.status = 'EMPTY' or bl.status = 'AVAILABLE'")
    List<BinLocation> getAllBinStatusEmptyAndAvailable();


}
