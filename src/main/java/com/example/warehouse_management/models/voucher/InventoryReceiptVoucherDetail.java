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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventory_receipt_voucher_details")
public class InventoryReceiptVoucherDetail {
    @EmbeddedId
    private InventoryReceiptVoucherDetailPK inventoryReceiptVoucherDetailPK =new InventoryReceiptVoucherDetailPK();
    @ManyToOne
    @MapsId("goodsId")
    @JoinColumn(name = "goods_id")
    private Goods goods;
    @ManyToOne
    @MapsId("inventoryReceiptVoucherId")
    @JoinColumn(name = "inventory_receipt_voucher_id")
    private InventoryReceiptVoucher inventoryReceiptVoucher;
    @ManyToOne
    @MapsId("binPositionId")
    @JoinColumn(name = "bin_position_id")
    private BinPosition binPosition;
    @Column(name = "quantity", columnDefinition = "INT CHECK (quantity > 0)")
    private int quantity;
}
