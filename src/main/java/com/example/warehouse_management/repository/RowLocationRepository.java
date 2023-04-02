package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.warehouse.RowLocation;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface RowLocationRepository extends CrudRepository<RowLocation,Long> {
    List<RowLocation> findAll();
    @Query("SELECT rl FROM RowLocation rl WHERE rl.status ='TRONG' OR rl.status='CONTRONG' ")
    List<RowLocation> findByStatusTrongAndConTrong();
    @Query("SELECT rl from RowLocation rl join Goods g ON rl.goods.id = g.id where g.name LIKE :goodsName and rl.code not in :codeRowLocations\n")
    List<RowLocation> findByGoodsName(String goodsName, List<String> codeRowLocations);
    RowLocation findByCode(String code);
    RowLocation findTopByOrderByIdDesc();
    @Query(nativeQuery = true,
            value = "select * from row_locations rl \n" +
                    "where rl.remaining_volume >=:volumeGoods and rl.status ='TRONG' and rl.code not in :codeRowLocations\n" +
                    "order by rl.id asc limit 1")
    RowLocation findTopOneByStatusTrongAndRemainingVolumeGreaterThanEqual(double volumeGoods, List<String> codeRowLocations );


    @Query(nativeQuery = true,
            value = "select * from row_locations rl \n" +
                    "where rl.remaining_volume >=:volumeGoods and rl.status ='TRONG' and rl.code not in :codeRowLocations\n" +
                    "order by rl.id asc limit :limit")
    public List<RowLocation> findTopNByStatusAndRemainingVolume(double volumeGoods ,int limit, List<String> codeRowLocations);

    @Query(nativeQuery = true,
            value = "select rl from row_locations rl \n" +
                    "join goods g ON rl.goods_id = g.id \n" +
                    "where rl.status ='CONCHO' \n" +
                    "and g.name =:goodsName \n" +
                    "and rl.remaining_volume >=:volumeGoods\n" +
                    "and rl.code not in :codeRowLocations")
    public List<RowLocation> findByStatusAndRemainingVolumeAndGoodsName(String goodsName,double volumeGoods , List<String> codeRowLocations);

    @Query(nativeQuery = true,
            value = "select * from row_locations rl \n" +
                    "where rl.status ='TRONG' and rl.code not in :codeRowLocations\n" +
                    "order by rl.id asc limit 1")
    RowLocation findTopOneByStatusTrong(List<String> codeRowLocations );

    @Query(nativeQuery = true,
            value = "select rl.* from row_locations rl \n" +
                    "join column_locations cl on rl.column_location_id = cl.id \n" +
                    "join shelve_storages ss on ss.id = cl.shelve_storage_id \n" +
                    "join warehouse w on w.id =ss.warehouse_id \n" +
                    "where rl.status =:status and w.code =:code")
    List<RowLocation> filterStatusByWarehouseCode(String code, String status);

    @Query(nativeQuery = true, value = "select rl.* from row_locations rl \n" +
            "join goods g ON rl.goods_id = g.id \n" +
            "where g.code =:goodsCode\n" +
            "order by rl.current_capacity desc \n")
    List<RowLocation> findByGoodsCode(String goodsCode);
    @Query(nativeQuery = true,
            value = "select rl.* from row_locations rl \n" +
                    "join column_locations cl on rl.column_location_id = cl.id \n" +
                    "join shelve_storages ss on ss.id = cl.shelve_storage_id \n" +
                    "join warehouse w on w.id =ss.warehouse_id \n" +
                    "where w.code =:code")
    List<RowLocation> getAllRowLocationByWarehouseCode(String code);


}
