package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.warehouse.ShelveStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelveStorageRepository extends CrudRepository<ShelveStorage,Long> {
    ShelveStorage findByCode(String code);
    List<ShelveStorage> findAll();
}
