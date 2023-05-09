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
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class InventoryDeliveryVoucherDetailPK implements Serializable {
    private Long goodsId;
    private Long deliveryVoucherId;
    private Long binPositionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryDeliveryVoucherDetailPK that = (InventoryDeliveryVoucherDetailPK) o;
        return Objects.equals(goodsId, that.goodsId) && Objects.equals(deliveryVoucherId, that.deliveryVoucherId) && Objects.equals(binPositionId, that.binPositionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId, deliveryVoucherId, binPositionId);
    }
}
