package com.example.warehouse_management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDeliveryVoucherResponse {

    private String code;
    private Set<DeliveryVoucherDetailResponse> details;
    private String createdBy;
    private PartnerResponse partner;
    private Date createDate;
    private String reason;
}
