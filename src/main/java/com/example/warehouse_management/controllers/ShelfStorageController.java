package com.example.warehouse_management.controllers;

import com.example.warehouse_management.payload.response.ShelveStorageResponse;
import com.example.warehouse_management.services.ShelveStorageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/shelve-storage")
public class ShelfStorageController {
    @Autowired
    private ShelveStorageServices shelveStorageServices;

    @GetMapping("/get-all")
    public ResponseEntity<List<ShelveStorageResponse>>getAll(){
        return new ResponseEntity(shelveStorageServices.findAll(), HttpStatus.OK);
    }
    @GetMapping("/get-by/{code}")
    public ResponseEntity<ShelveStorageResponse> getByCode(@PathVariable String code){
        return new ResponseEntity(shelveStorageServices.getByCode(code),HttpStatus.OK);
    }
}
