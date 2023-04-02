package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDeliveryVoucherRepository extends CrudRepository<InventoryDeliveryVoucher,Long> {
}
