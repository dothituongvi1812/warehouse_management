package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.payload.request.PartnerRequest;
import com.example.warehouse_management.payload.response.PartnerResponse;
import com.example.warehouse_management.repository.PartnerRepository;
import com.example.warehouse_management.services.PartnerServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PartnerServicesImpl implements PartnerServices {
    @Autowired
    PartnerRepository partnerRepository;

    private ModelMapper modelMapper=new ModelMapper();
    @Override
    public Partner addPartner(PartnerRequest partnerRequest) {
        Partner partnerSearch=partnerRepository.findByName(partnerRequest.getName());
        if (ObjectUtils.isEmpty(partnerSearch)){
            Partner partner =new Partner();
            partner.setCode(generatePartnerCode());
            partner.setAddress(partnerRequest.getAddress());
            partner.setName(partnerRequest.getName());
            partner.setPhone(partnerRequest.getPhone());
            Partner savePartner=partnerRepository.save(partner);
            return savePartner;
        }
        return partnerSearch;
    }

    @Override
    public List<PartnerResponse> getAll() {
        List<PartnerResponse> responses=partnerRepository.findAll().stream()
                .map(partner -> modelMapper.map(partner,PartnerResponse.class)).collect(Collectors.toList());
        return responses;
    }

    @Override
    public PartnerResponse getPartnerByCode(String code) {
        PartnerResponse partnerResponse=modelMapper.map(partnerRepository.findByCode(code),PartnerResponse.class);
        return partnerResponse;
    }

    private String generatePartnerCode(){
        Random rnd = new Random();
        Partner partner =partnerRepository.findTopByOrderByIdDesc();
        if (partner ==null)
            return "PN0001";

        long id= partner.getId()+1;
        String partnerCode=String.format("PN000%d",id);
        return partnerCode;

    }
}
