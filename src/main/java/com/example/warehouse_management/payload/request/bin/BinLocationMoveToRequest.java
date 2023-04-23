package com.example.warehouse_management.payload.request.bin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BinLocationMoveToRequest {
    private String toBinLocationCode;
    private int quantity;
}
