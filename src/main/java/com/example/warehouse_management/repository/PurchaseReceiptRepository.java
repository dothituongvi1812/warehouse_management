package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.purchase.PurchaseReceipt;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseReceiptRepository extends CrudRepository<PurchaseReceipt,Long> {
    PurchaseReceipt findTopByOrderByIdDesc();
    List<PurchaseReceipt> findAll();
    PurchaseReceipt findByCode(String purchaseReceiptCode);
}
