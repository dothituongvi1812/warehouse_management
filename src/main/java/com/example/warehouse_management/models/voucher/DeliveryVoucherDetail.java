package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.warehouse.BinLocation;
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
    private String goodsCode;
    @ManyToOne
    @MapsId("deliveryVoucherId")
    @JoinColumn(name = "delivery_voucher_id")
    private InventoryDeliveryVoucher inventoryDeliveryVoucher;

    @ManyToOne
    @MapsId("binLocationId")
    @JoinColumn(name = "bin_location_id")
    private BinLocation binLocation;
    private int quantity;
}
