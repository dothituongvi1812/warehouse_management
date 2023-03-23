package com.example.warehouse_management.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class ReceiptVoucherRequest {
    private PartnerRequest partnerRequest;
    private List<GoodsReceiptRequest> goodsReceiptRequests;
    private String email;
}
