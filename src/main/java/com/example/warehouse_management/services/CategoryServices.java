package com.example.warehouse_management.services;

import com.example.warehouse_management.models.goods.Category;
import com.example.warehouse_management.payload.request.category.CategoryRequest;
import com.example.warehouse_management.payload.response.CategoryResponse;

import java.util.List;


public interface CategoryServices {
    public CategoryResponse addCategory(CategoryRequest request);
    public List<CategoryResponse> findAll();
    public Category findCategoryByCode(String code);
    public CategoryResponse getByCode(String code);
}
