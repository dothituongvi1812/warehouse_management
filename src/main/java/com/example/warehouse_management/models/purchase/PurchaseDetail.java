package com.example.warehouse_management.models.purchase;

import com.example.warehouse_management.models.goods.Goods;
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
@Table(name = "purchase_details")
public class PurchaseDetail {
    @EmbeddedId
    private PurchaseDetailPK purchaseDetailPK =new PurchaseDetailPK();
    @ManyToOne
    @MapsId("goodsId")
    @JoinColumn(name = "goods_id")
    private Goods goods;
    @ManyToOne
    @MapsId("purchaseId")
    @JoinColumn(name = "purchase_id")
    private PurchaseReceipt purchaseReceipt;
//    @Column(name = "quantity_purchased", columnDefinition = "INT(4) CHECK (quantity_purchased > 0)")
    private int quantityPurchased;
//    @Column(name = "quantity_remaining", columnDefinition = "INT(4) CHECK (quantity_remaining > 0)")
    private int quantityRemaining;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatusOfPurchasingGoods status;
}
