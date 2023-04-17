package com.example.warehouse_management.payload.response;

import com.example.warehouse_management.models.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String code;
    private String sex;
    private String email;
    private String fullName;
    private Set<Role> roles;
    private boolean enabled;
}
