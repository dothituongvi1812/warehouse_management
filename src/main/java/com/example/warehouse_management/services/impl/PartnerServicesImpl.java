package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.payload.request.partner.PartnerRequest;
import com.example.warehouse_management.payload.request.partner.UpdatePartnerRequest;
import com.example.warehouse_management.payload.response.PartnerResponse;
import com.example.warehouse_management.repository.PartnerRepository;
import com.example.warehouse_management.services.PartnerServices;
import com.example.warehouse_management.services.domain.ObjectsUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Partner partnerSearch=partnerRepository.findByPhone(partnerRequest.getPhone());
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
    public PartnerResponse createPartner(PartnerRequest partnerRequest) {
        Partner partner =new Partner();
        partner.setCode(generatePartnerCode());
        partner.setAddress(partnerRequest.getAddress());
        partner.setName(partnerRequest.getName());
        partner.setPhone(partnerRequest.getPhone());
        Partner savePartner=partnerRepository.save(partner);
        return modelMapper.map(savePartner,PartnerResponse.class);
    }

    @Override
    public List<PartnerResponse> getAll() {
        List<PartnerResponse> responses=partnerRepository.findAll().stream()
                .map(partner -> modelMapper.map(partner,PartnerResponse.class)).collect(Collectors.toList());
        return responses;
    }

    @Override
    public Page<PartnerResponse> getPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Partner> page1 = partnerRepository.findAll(pageable);
        Page<PartnerResponse> responsePage = new PageImpl<>(page1.getContent().stream()
                .map(item->modelMapper.map(item,PartnerResponse.class))
                .collect(Collectors.toList())
                ,pageable,page1.getTotalElements());
        return responsePage;
    }

    @Override
    public PartnerResponse getPartnerByCode(String code) {
        PartnerResponse partnerResponse=modelMapper.map(partnerRepository.findByCode(code),PartnerResponse.class);
        return partnerResponse;
    }

    @Override
    public PartnerResponse getPartnerByPhone(String phone) {
        PartnerResponse partnerResponse=modelMapper.map(partnerRepository.findByPhone(phone),PartnerResponse.class);
        return partnerResponse;
    }

    @Override
    public Partner findPartnerByPhone(String phone) {
        Partner partner = partnerRepository.findByPhone(phone);
        if(partner==null)
            throw new NotFoundGlobalException("Không tìm thấy đối tác có số điện thoại"+ phone);
        return partner;
    }

    @Override
    public Partner findPartnerByCode(String partnerCode) {
        Partner partner = partnerRepository.findByCode(partnerCode);
        if(partner==null)
            throw new NotFoundGlobalException("Không tìm thấy đối tác có mã"+ partnerCode);
        return partner;
    }

    @Override
    public PartnerResponse updatePartner(String partnerCode, UpdatePartnerRequest request) {
        Partner partner = findPartnerByCode(partnerCode);
        if(!ObjectsUtils.equalObject(partner.getAddress(),request.getAddress())){
            partner.setAddress(request.getAddress());
        }
        if(!ObjectsUtils.equal(partner.getName(),request.getName())){
            partner.setName(request.getName());
        }
        if(!ObjectsUtils.equal(partner.getPhone(),request.getPhone())){
            partner.setName(request.getPhone());
        }
        Partner partnerSave = partnerRepository.save(partner);
        return modelMapper.map(partnerSave,PartnerResponse.class);
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
