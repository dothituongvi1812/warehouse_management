package com.example.warehouse_management.payload.request.partner;

import com.example.warehouse_management.models.partner.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class PartnerRequest {
    @NotBlank(message = "Tên đối tác không thể trống")
    private String name;
    private Address address;
    @NotBlank(message = "Số điện thoại không thể trống")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b",message = "Số điện thoại không hợp lệ")
    private String phone;
}
