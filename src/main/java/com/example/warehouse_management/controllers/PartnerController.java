package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.PartnerRequest;
import com.example.warehouse_management.payload.response.PartnerResponse;
import com.example.warehouse_management.services.PartnerServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/partner")
public class PartnerController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    PartnerServices partnerServices;
    @PostMapping("/create")
    public ResponseEntity<PartnerResponse> createPartner(@Valid @RequestBody PartnerRequest partnerRequest){
        return new ResponseEntity(partnerServices.createPartner(partnerRequest),HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PartnerResponse>> getAll(){
        return new ResponseEntity(partnerServices.getAll(),HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<PartnerResponse> getPartnerByCode(@PathVariable String code){
        return new ResponseEntity(partnerServices.getPartnerByCode(code),HttpStatus.OK);
    }
    @GetMapping("/get-by/{phonePartner}")
    public ResponseEntity<PartnerResponse> getPartnerByPhone(@PathVariable String code){
        return new ResponseEntity(partnerServices.getPartnerByPhone(code),HttpStatus.OK);
    }
}
