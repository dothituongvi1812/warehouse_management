package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Category;
import com.example.warehouse_management.payload.request.CategoryRequest;
import com.example.warehouse_management.payload.response.CategoryResponse;
import com.example.warehouse_management.repository.CategoryRepository;
import com.example.warehouse_management.services.CategoryServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CategoryServicesImpl implements CategoryServices {
    private ModelMapper modelMapper=new ModelMapper();
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public CategoryResponse addCategory(CategoryRequest request) {
        Category category= new Category();
        String code = generateCategoryCode();
        category.setCode(code);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        CategoryResponse response =modelMapper.map(categoryRepository.save(category),CategoryResponse.class);

        return response;
    }

    @Override
    public List<CategoryResponse> findAll() {
        List<CategoryResponse> categoryResponses =categoryRepository.findAll().stream()
                .map(e-> new CategoryResponse(e.getCode(),e.getName(),e.getDescription())
                ).collect(Collectors.toList());
        return categoryResponses;
    }

    @Override
    public Category findCategoryByCode(String code) {
        Category category = categoryRepository.findByCode(code);
        if(category==null)
            throw new NotFoundGlobalException("Không tìm thấy loại hàng hoá "+code);
        return category;
    }

    private String generateCategoryCode(){
        Random rnd = new Random();
        String code = String.format("L"+String.format("%04d",rnd.nextInt(999999)));
        return code;
    }
}
