package com.example.warehouse_management.services;

import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.payload.request.partner.PartnerRequest;
import com.example.warehouse_management.payload.request.partner.UpdatePartnerRequest;
import com.example.warehouse_management.payload.response.PartnerResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PartnerServices {
    Partner addPartner(PartnerRequest partnerRequest);
    PartnerResponse createPartner(PartnerRequest partnerRequest);
    List<PartnerResponse> getAll();
    Page<PartnerResponse> getPage(Integer page, Integer size);
    PartnerResponse getPartnerByCode(String code);
    PartnerResponse getPartnerByPhone(String phone);
    Partner findPartnerByPhone(String phone);
    Partner findPartnerByCode(String partnerCode);
    PartnerResponse updatePartner(String partnerCode, UpdatePartnerRequest request);
}
