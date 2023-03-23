package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.response.PartnerResponse;
import com.example.warehouse_management.services.PartnerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partner")
public class PartnerController {
    @Autowired
    PartnerServices partnerServices;

    @GetMapping("/get-all")
    public ResponseEntity<List<PartnerResponse>> getAll(){
        return new ResponseEntity(partnerServices.getAll(),HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<PartnerResponse> getPartnerByCode(@PathVariable String code){
        return new ResponseEntity(partnerServices.getPartnerByCode(code),HttpStatus.OK);
    }
}
