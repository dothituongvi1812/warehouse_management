package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.selling.SaleReceipt;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.type.EStatusOfVoucher;
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
@Table(name = "inventory_delivery_vouchers")
public class InventoryDeliveryVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String code;
    private String reason;

    @OneToMany(mappedBy ="inventoryDeliveryVoucher",cascade = CascadeType.ALL)
    private Set<InventoryDeliveryVoucherDetail> inventoryDeliveryVoucherDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_receipt_id")
    private SaleReceipt saleReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EStatusOfVoucher status;

    private Date createDate;
    private Date exportedDate;
}
