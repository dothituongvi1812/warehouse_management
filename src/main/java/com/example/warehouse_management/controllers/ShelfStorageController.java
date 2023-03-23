package com.example.warehouse_management.controllers;

import com.example.warehouse_management.models.warehouse.ShelveStorage;
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
    public ResponseEntity<List<ShelveStorage>>findAll(){
        return new ResponseEntity(shelveStorageServices.findAll(), HttpStatus.OK);
    }
}
