package com.example.warehouse_management.models.selling;

import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.purchase.PurchaseDetail;
import com.example.warehouse_management.models.type.EStatusSaleReceipt;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Table(name = "sale_receipts")
@Entity
public class SaleReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partnerId")
    private Partner partner;
    @OneToMany(mappedBy = "saleReceipt")
    private Set<SaleDetail> saleDetails;

    @OneToMany(mappedBy ="saleReceipt")
    private Set<InventoryDeliveryVoucher> inventoryDeliveryVouchers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    private Date createdDate;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isCanceled;
    private EStatusSaleReceipt status;
}
