package com.example.warehouse_management.payload.response;


import com.example.warehouse_management.models.type.EStatusPurchaseReceipt;
import com.example.warehouse_management.models.type.EStatusSaleReceipt;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
@Getter
@Setter
public class SaleReceiptResponse {
    private String code;
    private PartnerResponse partner;
    private String createdBy;
    private Set<SaleDetailResponse> saleDetailResponses;
    private Date createdDate;
    private EStatusSaleReceipt status;

}
