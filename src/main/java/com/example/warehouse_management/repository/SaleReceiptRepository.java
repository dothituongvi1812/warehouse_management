package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.selling.SaleReceipt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleReceiptRepository extends CrudRepository<SaleReceipt,Long> {
    List<SaleReceipt> findAll();
    SaleReceipt findTopByOrderByIdDesc();
    SaleReceipt findByCode(String code);
}
