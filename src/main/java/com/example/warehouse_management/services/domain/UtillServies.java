package com.example.warehouse_management.services.domain;

import java.util.Random;

public class UtillServies {
    private String generateGoodCode(){
        Random rnd = new Random();
        String code = String.format("L-"+String.format("%04d",rnd.nextInt(999999)));
        return code;
    }
}
