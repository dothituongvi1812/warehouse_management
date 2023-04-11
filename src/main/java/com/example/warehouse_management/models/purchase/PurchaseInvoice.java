package com.example.warehouse_management.models.purchase;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.goods.GoodsReceipt;
import com.example.warehouse_management.models.partner.Partner;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "purchase_invoices")
@Getter
public class PurchaseInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partnerId")
    private Partner partner;
    @OneToMany(mappedBy = "purchaseInvoice")
    private Set<PurchaseDetail>purchaseDetails;


}
