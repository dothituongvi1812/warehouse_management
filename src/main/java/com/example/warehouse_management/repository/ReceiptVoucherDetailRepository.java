package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.voucher.InventoryReceiptVoucherDetail;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucherDetailPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptVoucherDetailRepository extends CrudRepository<InventoryReceiptVoucherDetail, InventoryReceiptVoucherDetailPK> {
}
