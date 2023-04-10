package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryDeliveryVoucherRepository extends CrudRepository<InventoryDeliveryVoucher,Long> {
    InventoryDeliveryVoucher findTopByOrderByIdDesc();
    InventoryDeliveryVoucher findByCode(String code);
    List<InventoryDeliveryVoucher> findAll();
}
