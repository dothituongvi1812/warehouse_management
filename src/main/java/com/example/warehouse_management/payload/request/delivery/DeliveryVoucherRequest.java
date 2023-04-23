package com.example.warehouse_management.payload.request.delivery;

import com.example.warehouse_management.payload.request.goods.GoodDeliveryRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryVoucherRequest {

    @Valid
    private List<GoodDeliveryRequest> goodsRequests;
    private String email;
}
