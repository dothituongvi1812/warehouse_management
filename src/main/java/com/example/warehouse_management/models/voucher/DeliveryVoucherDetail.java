package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.goods.Goods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_voucher_details")
public class DeliveryVoucherDetail {
    @EmbeddedId
    private DeliveryVoucherDetailPK deliveryVoucherDetailPK =new DeliveryVoucherDetailPK();
    @ManyToOne
    @MapsId("goodsId")
    @JoinColumn(name = "goods_id")
    private Goods goods;
    @ManyToOne
    @MapsId("deliveryVoucherId")
    @JoinColumn(name = "delivery_voucher_id")
    private InventoryDeliveryVoucher inventoryDeliveryVoucher;
    private int quantity;
}
