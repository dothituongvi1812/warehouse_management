package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.warehouse.RowLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RowLocationRepository extends CrudRepository<RowLocation,Long> {
    List<RowLocation> findAll();
    @Query("SELECT rl FROM RowLocation rl WHERE rl.status ='TRONG' OR rl.status='CONTRONG' ")
    List<RowLocation> findByStatusTrongAndConTrong();
    @Query("SELECT rl from RowLocation rl join Goods g ON rl.goods.id = g.id where g.name LIKE :goodsName")
    List<RowLocation> findByGoodsName(String goodsName);
    RowLocation findByCode(String code);
    RowLocation findTopByOrderByIdDesc();
    @Query(nativeQuery = true,
            value = "select * from row_locations rl \n" +
                    "where rl.remaining_volume >=:volumeGoods and rl.status ='TRONG'\n" +
                    "order by rl.id asc limit 1")
    RowLocation findTopOneByStatusTrongAndRemainingVolumeGreaterThanEqual(double volumeGoods);


    @Query(nativeQuery = true,
            value = "select * from row_locations rl \n" +
                    "where rl.remaining_volume >=:volumeGoods and rl.status ='TRONG'\n" +
                    "order by rl.id asc limit :limit")
    public List<RowLocation> findTopNByStatusAndRemainingVolume(double volumeGoods ,int limit);
}
