package com.example.warehouse_management.payload.request;

import com.example.warehouse_management.models.goods.GoodsAndBinLocationToCreateVoucher;
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
public class ReceiptVoucherRequest {
    @Valid
    private List<GoodsAndBinLocationToCreateVoucher> goodsToCreateVoucher;
    private String email;
}
