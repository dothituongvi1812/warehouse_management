package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.warehouse.ShelfStorage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelveStorageRepository extends CrudRepository<ShelfStorage,Long> {
    ShelfStorage findByCode(String code);
    List<ShelfStorage> findAll();
    @Query(nativeQuery = true,value = "select * from shelf_storages ss \n" +
            "join warehouse w on ss.warehouse_id = w.id \n" +
            "where w.code =:warehouseCode")
    List<ShelfStorage> findAllByWarehouse(String warehouseCode);
}
