package com.example.warehouse_management.models.selling;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@Embeddable
public class SaleDetailPK implements Serializable {
    private Long goodsId;
    private Long saleId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleDetailPK that = (SaleDetailPK) o;
        return Objects.equals(goodsId, that.goodsId) && Objects.equals(saleId, that.saleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId, saleId);
    }
}
