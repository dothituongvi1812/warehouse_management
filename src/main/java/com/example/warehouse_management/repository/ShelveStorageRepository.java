package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.warehouse.ShelfStorage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelveStorageRepository extends CrudRepository<ShelfStorage,Long> {
    ShelfStorage findByCode(String code);
    List<ShelfStorage> findAll();
}
