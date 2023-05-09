package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.partner.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends CrudRepository<Partner,Long> {
    Partner findTopByOrderByIdDesc();
    List<Partner> findAll();
    Partner findByCode(String code);
    Partner findByName(String name);
    Partner findByPhone(String phone);
    Page<Partner> findAll(Pageable pageable);
    @Query(nativeQuery = true,value = "select * from partners p \n" +
            "where code like '%' || :keyword || '%'\n" +
            "or \"name\" like '%' || :keyword || '%'\n" +
            "or phone like '%' || :keyword || '%'")
    List<Partner> search(String keyword);

}
