package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.goods.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends CrudRepository<Goods,Long> {
    Goods findTopByOrderByIdDesc();
    Page<Goods> findAll(Pageable pageable);
    Goods findByCode(String code);
    @Query("SELECT g FROM Goods g WHERE g.code LIKE %:keyword% or g.name LIKE %:keyword%")
    List<Goods> findByCodeAndName(@Param("keyword") String keyword);
    @Query("SELECT g FROM Goods g WHERE g.name LIKE %:name%")
    Goods findByName(String name);
    @Query(nativeQuery = true,value = "select * from goods g \n" +
            "join category c on g.category_id = c.id \n" +
            "where c.code =:categoryCode")
    List<Goods> findAllByCategory(String categoryCode);

}
