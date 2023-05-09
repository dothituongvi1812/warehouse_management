package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.warehouse.ColumnPosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnLocationRepository extends CrudRepository<ColumnPosition,Long> {
    ColumnPosition findByCode(String code);
    List<ColumnPosition> findAll();
}
