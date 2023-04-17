package com.example.warehouse_management.payload.response;

import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.purchase.PurchaseDetail;
import com.example.warehouse_management.models.type.EStatusPurchaseReceipt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReceiptResponse {
    private String code;
    private PartnerResponse partner;
    private String createdBy;
    private EStatusPurchaseReceipt status;
    private Set<PurchaseDetailResponse> purchaseDetails;

}
