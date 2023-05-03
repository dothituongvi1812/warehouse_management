package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.purchase.PurchaseDetail;
import com.example.warehouse_management.models.purchase.PurchaseDetailPK;
import com.example.warehouse_management.models.selling.SaleDetail;
import com.example.warehouse_management.models.selling.SaleDetailPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleDetailRepository extends CrudRepository<SaleDetail, SaleDetailPK> {
    SaleDetail findBySaleDetailPK(SaleDetailPK saleDetailPK);
}
