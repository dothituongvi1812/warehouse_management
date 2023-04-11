package com.example.warehouse_management.models.purchase;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.type.EStatusOfPurchasingGoods;
import com.example.warehouse_management.models.type.EStatusOfVoucher;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import com.example.warehouse_management.models.voucher.ReceiptVoucherDetailPK;
import com.example.warehouse_management.models.warehouse.RowLocation;
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
    @JoinColumn(name = "purchaseId")
    private PurchaseInvoice purchaseInvoice;
    private int quantity;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatusOfPurchasingGoods status;
}
