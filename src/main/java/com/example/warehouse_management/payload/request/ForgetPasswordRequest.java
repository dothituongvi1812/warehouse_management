package com.example.warehouse_management.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ForgetPasswordRequest {
    @NotBlank(message = "Email không thể trống")
    @Size(max = 50)
    @Email(message = "Địa chỉ email không hợp lệ",flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;
}
