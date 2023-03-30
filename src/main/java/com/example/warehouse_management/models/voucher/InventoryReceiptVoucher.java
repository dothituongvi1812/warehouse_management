package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.partner.Partner;
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
    @Column(unique = true)
    private String code;
    @OneToMany(mappedBy ="inventoryReceiptVoucher",cascade = CascadeType.ALL)
    private Set<ReceiptVoucherDetail> receiptVoucherDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partnerId")
    private Partner partner;

    private Date createDate;

}
