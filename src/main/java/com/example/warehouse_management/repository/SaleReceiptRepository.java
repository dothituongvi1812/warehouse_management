package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.selling.SaleReceipt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SaleReceiptRepository extends CrudRepository<SaleReceipt,Long> {
    List<SaleReceipt> findAll();
    SaleReceipt findTopByOrderByIdDesc();
    SaleReceipt findByCode(String code);
    @Query(nativeQuery = true,value = "select * from sale_receipts sr \n" +
            "where sr.created_date between :from and :to")
    List<SaleReceipt> searchByCreatedDate(Timestamp from, Timestamp to);
    @Query(nativeQuery = true,value = "select * from sale_receipts sr \n" +
            "where sr.code like '%' || :code || '%'")
    List<SaleReceipt> searchByCode(String code);
    @Query(nativeQuery = true,value = "select sr.* from sale_receipts sr       \n" +
            "join users u on sr.created_by = u.id \n" +
            "where u.full_name like '%' || :createdBy || '%'")
    List<SaleReceipt> searchByCreatedBy(String createdBy);
}
