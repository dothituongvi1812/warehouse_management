package com.example.warehouse_management.payload.request.sale;

import com.example.warehouse_management.payload.request.goods.GoodsToSaleRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

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
