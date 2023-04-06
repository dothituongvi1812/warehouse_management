package com.example.warehouse_management.models.partner;

import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="partners")
@Getter
@Setter
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String code;
    @Column(columnDefinition = "text",length = 255)
    private String name;
    private String address;
    @Column(unique = true)
    private String phone;
    @OneToMany(mappedBy = "partner")
    private Set<InventoryDeliveryVoucher> inventoryDeliveryVouchers;
    @OneToMany(mappedBy = "partner")
    private Set<InventoryReceiptVoucher> inventoryReceiptVouchers;


}
