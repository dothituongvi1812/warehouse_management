package com.example.warehouse_management.payload.request;

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
    private String address;
    @NotBlank(message = "Số điện thoại không thể trống")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b")
    private String phone;
}
