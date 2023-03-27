package com.example.warehouse_management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationInWarehouse {
    private String nameWarehouse;
    private String codeWarehouse;
    private String codeShelf;
    private String nameShelf;
    private String codeColumn;
    private String nameColumn;
    private String codeRow;
    private String nameRow;
}
