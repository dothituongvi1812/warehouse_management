package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.ForgetPasswordRequest;
import com.example.warehouse_management.payload.request.LoginRequest;
import com.example.warehouse_management.payload.request.RegisterUserRequest;
import com.example.warehouse_management.payload.request.ResetPasswordRequest;
import com.example.warehouse_management.payload.response.JwtResponse;
import com.example.warehouse_management.payload.response.UserResponse;
import com.example.warehouse_management.repository.RoleRepository;
import com.example.warehouse_management.repository.UserRepository;
import com.example.warehouse_management.security.jwt.JwtUtils;
import com.example.warehouse_management.services.UserServices;
import com.example.warehouse_management.services.impl.UserServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    UserServices userServices;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @PostMapping("register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterUserRequest registerRequest) throws MessagingException, UnsupportedEncodingException {
        return new ResponseEntity(userServices.registerUser(registerRequest),HttpStatus.OK) ;
    }
    @PostMapping("auth/login")
    public ResponseEntity<JwtResponse> authenticationUser(@RequestBody LoginRequest login){
        return new ResponseEntity(userServices.authenticationUser(login),HttpStatus.OK);
    }

    @PostMapping("auth/reset-password")
    public ResponseEntity<String>updatePassword(@RequestBody @Valid ResetPasswordRequest request, Principal principal){
        request.setEmail(principal.getName());
        if(userServices.resetPassword(request))
            return new ResponseEntity<>("Mật khẩu của bạn đã được thay đổi",HttpStatus.ACCEPTED);
        return new ResponseEntity<>("Thất bại",HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/auth/forget")
    public ResponseEntity forgetPassword(@RequestBody ForgetPasswordRequest request){
        return new ResponseEntity(userServices.forgetPassword(request.getEmail()),HttpStatus.NO_CONTENT);
    }



}
