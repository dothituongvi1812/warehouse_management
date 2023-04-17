package com.example.warehouse_management.payload.response;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class SaleReceiptResponse {
    private String code;
    private PartnerResponse partner;
    private String createdBy;
    private Set<SaleDetailResponse> saleDetailResponses;

}
