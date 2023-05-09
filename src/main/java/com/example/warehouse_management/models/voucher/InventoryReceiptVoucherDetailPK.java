package com.example.warehouse_management.models.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReceiptVoucherDetailPK implements Serializable {
    private Long inventoryReceiptVoucherId;
    private Long binPositionId;
    private Long goodsId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryReceiptVoucherDetailPK that = (InventoryReceiptVoucherDetailPK) o;
        return Objects.equals(inventoryReceiptVoucherId, that.inventoryReceiptVoucherId) && Objects.equals(binPositionId, that.binPositionId) && Objects.equals(goodsId, that.goodsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryReceiptVoucherId, binPositionId, goodsId);
    }
}
