package com.example.warehouse_management.controllers;

import com.example.warehouse_management.exception.TokenRefreshException;
import com.example.warehouse_management.models.user.RefreshToken;
import com.example.warehouse_management.payload.request.auth.*;
import com.example.warehouse_management.payload.response.JwtResponse;
import com.example.warehouse_management.payload.response.TokenRefreshResponse;
import com.example.warehouse_management.payload.response.UserResponse;
import com.example.warehouse_management.repository.RoleRepository;
import com.example.warehouse_management.repository.UserRepository;
import com.example.warehouse_management.security.jwt.JwtUtils;
import com.example.warehouse_management.security.services.RefreshTokenService;
import com.example.warehouse_management.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class AuthController {
    private Logger logger = LoggerFactory.getLogger(getClass());
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
    @Autowired
    RefreshTokenService refreshTokenService;
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterUserRequest registerRequest) throws MessagingException, UnsupportedEncodingException {
        logger.info("/register");
        return new ResponseEntity(userServices.registerUser(registerRequest),HttpStatus.OK) ;
    }
    @PostMapping("auth/login")
    public ResponseEntity<JwtResponse> authenticationUser(@RequestBody LoginRequest login){
        logger.info("auth/login");
        return new ResponseEntity(userServices.authenticationUser(login),HttpStatus.OK);
    }

    @PostMapping("auth/reset-password")
    public ResponseEntity<String>updatePassword(@RequestBody @Valid ResetPasswordRequest request, Principal principal){
        logger.info("auth/reset-password");
        request.setEmail(principal.getName());
        if(userServices.resetPassword(request))
            return new ResponseEntity<>("Mật khẩu của bạn đã được thay đổi",HttpStatus.ACCEPTED);
        return new ResponseEntity<>("Thất bại",HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/auth/forget")
    public ResponseEntity forgetPassword(@RequestBody ForgetPasswordRequest request){
        logger.info("/auth/forget");
        return new ResponseEntity(userServices.forgetPassword(request.getEmail()),HttpStatus.NO_CONTENT);
    }
    @PostMapping("/auth/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateJwtToken(user);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }


}
