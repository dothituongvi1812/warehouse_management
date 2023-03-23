package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.warehouse.RowLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RowLocationRepository extends CrudRepository<RowLocation,Long> {
    List<RowLocation> findAll();
    @Query("SELECT rl FROM RowLocation rl WHERE rl.status ='TRONG' OR rl.status='CONTRONG' ")
    List<RowLocation> findByStatusTrongAndConTrong();
}
