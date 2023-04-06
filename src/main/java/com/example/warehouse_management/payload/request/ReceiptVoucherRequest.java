package com.example.warehouse_management.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptVoucherRequest {
    @Valid
    private PartnerRequest partnerRequest;
    @Valid
    private Set<GoodsRequest> goodsRequests;
    private String email;
}
