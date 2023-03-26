package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.warehouse.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralWarehouseRepository extends JpaRepository<Warehouse,Long> {

}
