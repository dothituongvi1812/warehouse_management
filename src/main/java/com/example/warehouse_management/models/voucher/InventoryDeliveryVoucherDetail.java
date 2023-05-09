package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.warehouse.BinPosition;
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
@Table(name = "inventory_delivery_voucher_details")
public class InventoryDeliveryVoucherDetail {
    @EmbeddedId
    private InventoryDeliveryVoucherDetailPK inventoryDeliveryVoucherDetailPK =new InventoryDeliveryVoucherDetailPK();
    @ManyToOne
    @MapsId("goodsId")
    @JoinColumn(name = "goods_id")
    private Goods goods;
    @ManyToOne
    @MapsId("deliveryVoucherId")
    @JoinColumn(name = "delivery_voucher_id")
    private InventoryDeliveryVoucher inventoryDeliveryVoucher;

    @ManyToOne
    @MapsId("binPositionId")
    @JoinColumn(name = "bin_position_id")
    private BinPosition binPosition;
    @Column(name = "quantity", columnDefinition = "INT CHECK (quantity > 0)")
    private int quantity;
}
