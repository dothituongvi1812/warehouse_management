package com.example.warehouse_management.models.voucher;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@Embeddable
public class ReceiptVoucherDetailPK implements Serializable {
    private Long receiptVoucherId;
    private Long binLocationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptVoucherDetailPK that = (ReceiptVoucherDetailPK) o;
        return  Objects.equals(receiptVoucherId, that.receiptVoucherId) && Objects.equals(binLocationId, that.binLocationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash( receiptVoucherId, binLocationId);
    }
}
