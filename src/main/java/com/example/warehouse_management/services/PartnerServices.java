package com.example.warehouse_management.services;

import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.payload.request.PartnerRequest;
import com.example.warehouse_management.payload.response.PartnerResponse;

import java.util.List;

public interface PartnerServices {
     Partner addPartner(PartnerRequest partnerRequest);
    List<PartnerResponse> getAll();
    PartnerResponse getPartnerByCode(String code);
    PartnerResponse getPartnerByPhone(String phone);
    Partner findPartnerByPhone(String phone);
}
