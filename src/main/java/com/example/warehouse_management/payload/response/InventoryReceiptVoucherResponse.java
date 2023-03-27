package com.example.warehouse_management.payload.response;

import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.ReceiptVoucherDetail;
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
public class InventoryReceiptVoucherResponse {

    private String code;
    private Set<ReceiptVoucherDetailResponse> receiptVoucherDetailResponses;
    private User createdBy;
    private Partner partner;
    private Date createDate;
}
