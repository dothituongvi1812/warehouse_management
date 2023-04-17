package com.example.warehouse_management.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SaleReceiptRequest {
    @NotBlank(message = "Mã đối tác không thể trống")
    private String partnerCode;
    @Valid
    private List<GoodsToSaleRequest> goodsToSaleRequests;
    private String email;
}
