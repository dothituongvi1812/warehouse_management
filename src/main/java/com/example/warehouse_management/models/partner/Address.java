package com.example.warehouse_management.models.partner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
    private String province;
    private String ward;
    private String district;
    private String street;
}
