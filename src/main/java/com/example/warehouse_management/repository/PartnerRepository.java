package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.partner.Partner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends CrudRepository<Partner,Long> {
    Partner findTopByOrderByIdDesc();
    List<Partner> findAll();
    Partner findByCode(String code);
    Partner findByName(String name);

}
