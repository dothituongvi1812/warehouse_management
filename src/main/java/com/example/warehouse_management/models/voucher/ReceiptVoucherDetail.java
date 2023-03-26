package com.example.warehouse_management.models.voucher;

import com.example.warehouse_management.models.goods.Goods;
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
@Table(name = "receipt_voucher_detail")
public class ReceiptVoucherDetail {
    @EmbeddedId
    private ReceiptVoucherDetailPK receiptVoucherDetailPK =new ReceiptVoucherDetailPK();
    @ManyToOne
    @MapsId("goodsId")
    @JoinColumn(name = "goods_id")
    private Goods goods;
    @ManyToOne
    @MapsId("receiptVoucherId")
    @JoinColumn(name = "receipt_voucher_id")
    private InventoryReceiptVoucher inventoryReceiptVoucher;
    private int quantity;
}
