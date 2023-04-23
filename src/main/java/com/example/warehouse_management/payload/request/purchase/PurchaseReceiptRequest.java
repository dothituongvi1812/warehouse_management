package com.example.warehouse_management.payload.request.purchase;

import com.example.warehouse_management.payload.request.partner.PartnerRequest;
import com.example.warehouse_management.payload.request.goods.GoodsRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseReceiptRequest {
    @Valid
    private PartnerRequest partnerRequest;
    @Valid
    private Set<GoodsRequest> goodsRequests;
    private String email;
}
