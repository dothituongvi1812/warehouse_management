package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.voucher.ReceiptVoucherDetail;
import com.example.warehouse_management.models.voucher.ReceiptVoucherDetailPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptVoucherDetailRepository extends CrudRepository<ReceiptVoucherDetail, ReceiptVoucherDetailPK> {
}
