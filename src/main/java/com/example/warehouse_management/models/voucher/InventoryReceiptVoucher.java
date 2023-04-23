package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.purchase.PurchaseReceipt;
import com.example.warehouse_management.models.type.EStatusOfVoucher;
import com.example.warehouse_management.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="inventory_receipt_vouchers")
public class InventoryReceiptVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String code;
    @OneToMany(mappedBy ="inventoryReceiptVoucher",cascade = CascadeType.ALL)
    private Set<ReceiptVoucherDetail> receiptVoucherDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_receipt_id")
    private PurchaseReceipt purchaseReceipt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatusOfVoucher status;
    @Column(name = "create_date")
    private Date createDate;

}
