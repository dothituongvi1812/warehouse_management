package com.example.warehouse_management.payload.request.auth;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {
    @NotBlank(message = "Email không thể trống")
    @Size(max = 50)
    @Email(message = "Địa chỉ email không hợp lệ",flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Pattern(regexp ="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"
            ,message = "Mật khẩu có độ dài tối thiểu 8 ký tự, ít nhất một chữ cái viết hoa, một chữ cái viết thường, một số và một ký tự đặc biệt")
    private String password;

}
