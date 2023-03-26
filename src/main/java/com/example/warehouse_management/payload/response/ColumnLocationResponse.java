package com.example.warehouse_management.payload.response;

import com.example.warehouse_management.models.type.EStatusStorage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnLocationResponse {
    private String  code;
    private String name;
    private double length;
    private String status;
    private String shelfStorageCode;
    private String shelfStorageName;
}
