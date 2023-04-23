package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.request.partner.PartnerRequest;
import com.example.warehouse_management.payload.request.partner.UpdatePartnerRequest;
import com.example.warehouse_management.payload.response.PartnerResponse;
import com.example.warehouse_management.services.PartnerServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        logger.info("/create");
        return new ResponseEntity(partnerServices.createPartner(partnerRequest),HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PartnerResponse>> getAll(){
        logger.info("/get-all");
        return new ResponseEntity(partnerServices.getAll(),HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<PartnerResponse> getPartnerByCode(@PathVariable String code){
        logger.info("/get-by"+code);
        return new ResponseEntity(partnerServices.getPartnerByCode(code),HttpStatus.OK);
    }
    @GetMapping("/get-by-phone/{phonePartner}")
    public ResponseEntity<PartnerResponse> getPartnerByPhone(@PathVariable String phonePartner){
        logger.info("/get-by-phone"+phonePartner);
        return new ResponseEntity(partnerServices.getPartnerByPhone(phonePartner),HttpStatus.OK);
    }
    @GetMapping("/get-page")
    public ResponseEntity<Page<PartnerResponse>> getPage(@RequestParam Integer page, @RequestParam Integer size){
        logger.info("/get-page");
        return new ResponseEntity(partnerServices.getPage(page-1, size),HttpStatus.OK);
    }
    @PostMapping("/update/{partnerCode}")
    public ResponseEntity<PartnerResponse> updatePartner(@PathVariable String partnerCode, @RequestBody UpdatePartnerRequest updatePartnerRequest){
        logger.info("/update"+partnerCode);
        return new ResponseEntity(null,HttpStatus.OK);
    }
}
