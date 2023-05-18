package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface InventoryReceiptVoucherRepository extends CrudRepository<InventoryReceiptVoucher,Long> {
    InventoryReceiptVoucher findTopByOrderByIdDesc();
    InventoryReceiptVoucher findByCode(String code);
    @Query(nativeQuery = true,
            value = "select * from inventory_receipt_vouchers irv \n" +
                    "order by create_date desc ")
    Page<InventoryReceiptVoucher> findAllBySortedCreateDate(Pageable pageable);
    List<InventoryReceiptVoucher>  findAll();
    InventoryReceiptVoucher findInventoryReceiptVoucherByCode(String voucherCode);
    @Query(nativeQuery = true,value = "select * from inventory_receipt_vouchers irv\n" +
            "where irv.create_date between :from and :to")
    List<InventoryReceiptVoucher> searchByDate(Timestamp from, Timestamp to);

    @Query(nativeQuery = true,value = "select * from inventory_receipt_vouchers irv \n" +
            "where irv.code like '%' || :code || '%'")
    List<InventoryReceiptVoucher> searchByCode(String code);

    @Query(nativeQuery = true,value = "select irv.* from inventory_receipt_vouchers irv  \n" +
            "join users u on irv.created_by = u.id \n" +
            "where u.full_name like '%' || :createdBy || '%'")
    List<InventoryReceiptVoucher> searchByCreatedBy(String createdBy);



}
