package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.UserUpdateRequest;
import com.example.warehouse_management.payload.response.UserResponse;
import com.example.warehouse_management.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping ("/api/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserServices userServices;
    @PostMapping("/update/{code}")
    public ResponseEntity updateUser(@PathVariable String code, @RequestBody @Valid UserUpdateRequest userUpdateRequest){
        return new ResponseEntity(userServices.updateUser(code,userUpdateRequest), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity getAll(){
        return new ResponseEntity(userServices.getAll(),HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<UserResponse> getUserByCode(@PathVariable String code){
        return new ResponseEntity(userServices.findUserByCode(code),HttpStatus.OK);
    }
}
