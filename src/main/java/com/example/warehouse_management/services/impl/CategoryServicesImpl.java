package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Category;
import com.example.warehouse_management.payload.request.category.CategoryRequest;
import com.example.warehouse_management.payload.response.CategoryResponse;
import com.example.warehouse_management.repository.CategoryRepository;
import com.example.warehouse_management.services.CategoryServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
        Category category  = categoryRepository.findByName(request.getName());
        if(ObjectUtils.isEmpty(category)){
            Category category1= new Category();
            String code = generateCategoryCode();
            category1.setCode(code);
            category1.setName(request.getName());
            category1.setDescription(request.getDescription());
            CategoryResponse response =mapperCategoryResponse(categoryRepository.save(category1));
            return response;
        }
        else{
            throw new ErrorException("Đã tồn tại loại sản phẩm "+ request.getName());
        }
    }

    @Override
    public List<CategoryResponse> findAll() {
        List<CategoryResponse> categoryResponses =categoryRepository.findAll().stream()
                .map(e->mapperCategoryResponse(e))
                .collect(Collectors.toList());
        return categoryResponses;
    }

    @Override
    public Category findCategoryByCode(String code) {
        Category category = categoryRepository.findByCode(code);
        if(category==null)
            throw new NotFoundGlobalException("Không tìm thấy loại hàng hoá "+code);
        return category;
    }

    @Override
    public CategoryResponse getByCode(String code) {
        return mapperCategoryResponse(findCategoryByCode(code));
    }

    private String generateCategoryCode(){
        Random rnd = new Random();
        String code = String.format("L"+String.format("%04d",rnd.nextInt(999999)));
        return code;
    }
    private CategoryResponse mapperCategoryResponse(Category category){
        CategoryResponse categoryResponse= modelMapper.map(category,CategoryResponse.class);
        return categoryResponse;
    }
}
