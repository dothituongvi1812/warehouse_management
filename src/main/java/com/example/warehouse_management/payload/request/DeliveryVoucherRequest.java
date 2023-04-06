package com.example.warehouse_management.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryVoucherRequest {

    @NotBlank(message = "Số điện thoại không thể trống")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b")
    private String partnerPhone;
    @Valid
    private Set<GoodDeliveryRequest> goodsRequests;
    private String email;
    @NotBlank(message = "Lý do không thể trống")
    private String reason;
}
