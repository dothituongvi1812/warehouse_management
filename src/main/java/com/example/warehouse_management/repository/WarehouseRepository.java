package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.warehouse.Warehouse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends CrudRepository<Warehouse,Long> {
    Warehouse findWarehouseByCode(String code);
    List<Warehouse> findAll();
}
