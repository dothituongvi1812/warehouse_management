package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryReceiptVoucherRepository extends CrudRepository<InventoryReceiptVoucher,Long> {
    InventoryReceiptVoucher findTopByOrderByIdDesc();
}
