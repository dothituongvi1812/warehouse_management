package com.example.warehouse_management.payload.request.receive;

import com.example.warehouse_management.utils.GoodsAndBinLocationToCreateVoucher;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptVoucherRequest {
    @Valid
    private List<GoodsAndBinLocationToCreateVoucher> goodsToCreateVoucher;
    private String email;
}
