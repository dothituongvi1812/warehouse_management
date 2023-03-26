package com.example.warehouse_management.services;

import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.payload.request.LoginRequest;
import com.example.warehouse_management.payload.request.RegisterUserRequest;
import com.example.warehouse_management.payload.request.ResetPasswordRequest;
import com.example.warehouse_management.payload.request.UserUpdateRequest;
import com.example.warehouse_management.payload.response.JwtResponse;
import com.example.warehouse_management.payload.response.UserResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserServices {
    JwtResponse authenticationUser(@RequestBody LoginRequest login);
    UserResponse registerUser(RegisterUserRequest registerRequest);
    public boolean resetPassword(ResetPasswordRequest request);
    public boolean forgetPassword(String email);
    public UserResponse updateUser(String code, UserUpdateRequest userUpdateRequest);
    public List<UserResponse> getAll();
    public UserResponse findUserByCode(String code);
    public User findByCode(String code);
}
