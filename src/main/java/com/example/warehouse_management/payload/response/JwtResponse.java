package com.example.warehouse_management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String code;
    private String fullName;
    private String email;
    private List<String> roles;


    public JwtResponse(String token, Long id, String code, String fullName, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }
}
