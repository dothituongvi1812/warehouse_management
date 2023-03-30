package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.goods.Goods;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends CrudRepository<Goods,Long> {
    Goods findTopByOrderByIdDesc();
    List<Goods> findAll();
    Goods findByCode(String code);
    @Query("SELECT g FROM Goods g WHERE g.code LIKE %:keyword% or g.name LIKE %:keyword%")
    List<Goods> findByCodeAndName(@Param("keyword") String keyword);

    Goods findByName(String name);

}
