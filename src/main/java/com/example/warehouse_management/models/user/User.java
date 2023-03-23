package com.example.warehouse_management.models.user;

import com.example.warehouse_management.models.voucher.InventoryDeliveryVoucher;
import com.example.warehouse_management.models.voucher.InventoryReceiptVoucher;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar",length = 5,unique = true)
    private String code;
    @Column(columnDefinition = "text",length = 255)
    private String fullName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    private boolean enabled;

    private String sex;
    private Date createDate;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy ="createdBy",cascade = CascadeType.ALL)
    private Set<InventoryDeliveryVoucher> inventoryDeliveryVouchers;

    @OneToMany(mappedBy ="createdBy",cascade = CascadeType.ALL)
    private Set<InventoryReceiptVoucher> inventoryReceiptVouchers;

    public User() {
    }

    public User(String code, String email, String password,String fullName,String sex, boolean enabled) {
        this.code=code;
        this.email = email;
        this.password = password;
        this.fullName=fullName;
        this.sex=sex;
        this.enabled = enabled;
    }

}