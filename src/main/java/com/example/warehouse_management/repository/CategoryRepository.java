package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.goods.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category,Long> {
    Category findByCode(String code);
    List<Category> findAll ();
}
