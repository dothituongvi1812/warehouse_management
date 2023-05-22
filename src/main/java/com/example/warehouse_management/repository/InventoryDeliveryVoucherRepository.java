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
public interface InventoryDeliveryVoucherRepository extends CrudRepository<InventoryDeliveryVoucher,Long> {
    InventoryDeliveryVoucher findTopByOrderByIdDesc();
    InventoryDeliveryVoucher findByCode(String code);
    @Query(nativeQuery = true,value = "select * from inventory_delivery_vouchers idv\n" +
            "order by idv.create_date desc  ")
    Page<InventoryDeliveryVoucher> getPage(Pageable pageable);
    List<InventoryDeliveryVoucher> findAll();

    @Query(nativeQuery = true,value = "select * from inventory_delivery_vouchers idv \n" +
            "where idv .create_date between :from and :to")
    List<InventoryDeliveryVoucher> searchByDate(Timestamp from, Timestamp to);
    @Query(nativeQuery = true,value = "select * from inventory_delivery_vouchers idv  \n" +
            "where idv.code like '%' || :code || '%'")
    List<InventoryDeliveryVoucher> searchByCode(String code);

    @Query(nativeQuery = true,value = "select idv.* from inventory_delivery_vouchers idv   \n" +
            "join users u on idv.created_by = u.id \n" +
            "where u.full_name like '%' || :createdBy || '%'")
    List<InventoryDeliveryVoucher> searchByCreatedBy(String createdBy);

}
