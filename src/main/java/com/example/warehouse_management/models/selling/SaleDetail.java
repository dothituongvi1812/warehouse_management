package com.example.warehouse_management.models.selling;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.purchase.PurchaseDetailPK;
import com.example.warehouse_management.models.purchase.PurchaseReceipt;
import com.example.warehouse_management.models.type.EStatusOfPurchasingGoods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sale_details")
public class SaleDetail {
    @EmbeddedId
    private SaleDetailPK saleDetailPK =new SaleDetailPK();
    @ManyToOne
    @MapsId("goodsId")
    @JoinColumn(name = "goods_id")
    private Goods goods;
    @ManyToOne
    @MapsId("saleId")
    @JoinColumn(name = "sale_id")
    private SaleReceipt saleReceipt;
    private int quantity;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatusOfPurchasingGoods status;
}
