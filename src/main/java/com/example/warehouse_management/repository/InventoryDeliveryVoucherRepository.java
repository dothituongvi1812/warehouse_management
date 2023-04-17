package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryDeliveryVoucherRepository extends CrudRepository<InventoryDeliveryVoucher,Long> {
    InventoryDeliveryVoucher findTopByOrderByIdDesc();
    InventoryDeliveryVoucher findByCode(String code);
    @Query(nativeQuery = true,value = "select * from inventory_delivery_vouchers idv ")
    Page<InventoryDeliveryVoucher> getPage(Pageable pageable);
    List<InventoryDeliveryVoucher> findAll();
}
