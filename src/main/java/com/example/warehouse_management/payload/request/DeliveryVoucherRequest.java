package com.example.warehouse_management.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryVoucherRequest {
    private String partnerName;
    private List<GoodDeliveryRequest> goodsRequests;
    private String email;
    private String reason;
}
