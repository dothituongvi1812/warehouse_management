package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.purchase.PurchaseDetail;
import com.example.warehouse_management.models.purchase.PurchaseDetailPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDetailRepository extends CrudRepository<PurchaseDetail, PurchaseDetailPK> {
    PurchaseDetail findByPurchaseDetailPK(PurchaseDetailPK purchaseDetailPK);
}
