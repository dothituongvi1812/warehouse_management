package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryReceiptVoucherRepository extends CrudRepository<InventoryReceiptVoucher,Long> {
    InventoryReceiptVoucher findTopByOrderByIdDesc();
    InventoryReceiptVoucher findByCode(String code);
    @Query(nativeQuery = true,
            value = "select * from inventory_receipt_vouchers irv \n" +
                    "order by create_date desc ")
    List<InventoryReceiptVoucher> findAllBySortedCreateDate();

    InventoryReceiptVoucher findInventoryReceiptVoucherByCode(String voucherCode);
}
