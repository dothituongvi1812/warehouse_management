package com.example.warehouse_management.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "Email không thể trống")
    @Size(max = 50)
    @Email(message = "Địa chỉ email không hợp lệ",flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;

    private String role;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Pattern(regexp ="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"
            ,message = "Mật khẩu có độ dài tối thiểu 8 ký tự, ít nhất một chữ cái viết hoa, một chữ cái viết thường, một số và một ký tự đặc biệt")
    private String password;

    @NotBlank(message = "Tên không thể trống")
    private String fullName;
    private String sex;
}
