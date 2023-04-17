package com.example.warehouse_management.models.purchase;

import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.type.EStatusPurchaseReceipt;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "purchase_receipts")
@Getter
@Setter
public class PurchaseReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partnerId")
    private Partner partner;
    @OneToMany(mappedBy = "purchaseReceipt")
    private Set<PurchaseDetail>purchaseDetails;

    @OneToMany(mappedBy ="purchaseReceipt")
    private Set<InventoryReceiptVoucher> inventoryReceiptVouchers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    private Date createdDate;
    @Enumerated(EnumType.STRING)
    private EStatusPurchaseReceipt status;



}
