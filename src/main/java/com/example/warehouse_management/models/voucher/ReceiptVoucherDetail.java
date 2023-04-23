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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "receipt_voucher_details")
public class ReceiptVoucherDetail {
    @EmbeddedId
    private ReceiptVoucherDetailPK receiptVoucherDetailPK =new ReceiptVoucherDetailPK();
    private String goodsCode;
    @ManyToOne
    @MapsId("receiptVoucherId")
    @JoinColumn(name = "receipt_voucher_id")
    private InventoryReceiptVoucher inventoryReceiptVoucher;
    @ManyToOne
    @MapsId("binLocationId")
    @JoinColumn(name = "bin_location_id")
    private BinLocation binLocation;
    //@Column(name = "quantity", columnDefinition = "INT(4) CHECK (quantity > 0)")
    private int quantity;
}
