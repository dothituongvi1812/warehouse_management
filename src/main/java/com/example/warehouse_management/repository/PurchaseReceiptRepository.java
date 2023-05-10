package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.purchase.PurchaseReceipt;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PurchaseReceiptRepository extends CrudRepository<PurchaseReceipt,Long> {
    PurchaseReceipt findTopByOrderByIdDesc();
    List<PurchaseReceipt> findAll();
    PurchaseReceipt findByCode(String purchaseReceiptCode);
    @Query(nativeQuery = true,value = "select * from purchase_receipts pr \n" +
            "where pr.created_date between :from and :to")
    List<PurchaseReceipt> searchByCreatedDate(Timestamp from, Timestamp to);
}
