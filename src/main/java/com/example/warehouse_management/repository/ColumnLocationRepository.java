package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.warehouse.ColumnLocation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnLocationRepository extends CrudRepository<ColumnLocation,Long> {
    ColumnLocation findByCode(String code);
    List<ColumnLocation> findAll();
}
