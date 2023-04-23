package com.example.warehouse_management.payload.request.user;

import com.example.warehouse_management.models.user.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class UserUpdateRequest {
    private String fullName;
    private String sex;
    private String role;
}
