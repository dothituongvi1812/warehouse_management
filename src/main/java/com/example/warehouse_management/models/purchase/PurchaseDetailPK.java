package com.example.warehouse_management.models.purchase;

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
public class PurchaseDetailPK implements Serializable {
    private Long goodsId;
    private Long purchaseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseDetailPK that = (PurchaseDetailPK) o;
        return Objects.equals(goodsId, that.goodsId) && Objects.equals(purchaseId, that.purchaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId, purchaseId);
    }
}
