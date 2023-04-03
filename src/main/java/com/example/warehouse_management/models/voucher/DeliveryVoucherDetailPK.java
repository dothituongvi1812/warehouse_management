package com.example.warehouse_management.models.voucher;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class DeliveryVoucherDetailPK implements Serializable {
    private Long goodsId;
    private Long deliveryVoucherId;
    private Long rowLocationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryVoucherDetailPK that = (DeliveryVoucherDetailPK) o;
        return Objects.equals(goodsId, that.goodsId) && Objects.equals(deliveryVoucherId, that.deliveryVoucherId) && Objects.equals(rowLocationId, that.rowLocationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId, deliveryVoucherId, rowLocationId);
    }
}
