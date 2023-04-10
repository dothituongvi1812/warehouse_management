package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Category;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.type.EUnit;
import com.example.warehouse_management.payload.request.GoodsAddRequest;
import com.example.warehouse_management.payload.request.GoodsRequest;
import com.example.warehouse_management.payload.response.GoodsResponse;
import com.example.warehouse_management.repository.CategoryRepository;
import com.example.warehouse_management.repository.GoodsRepository;
import com.example.warehouse_management.services.GoodsServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServicesImpl implements GoodsServices {
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    CategoryRepository categoryRepository;

    private ModelMapper modelMapper=new ModelMapper();
    @Override
    public GoodsResponse addGoods(GoodsAddRequest goodsAddRequest) {
        GoodsRequest goodsRequest  = modelMapper.map(goodsAddRequest,GoodsRequest.class);
        GoodsResponse response=mapperGoodResponse(createGoods(goodsRequest));
        return response;
    }
    @Override
    public Goods createGoods(GoodsRequest goodsRequest) {
        Category category=categoryRepository.findByCode(goodsRequest.getCategoryCode());
        if(category==null){
            throw new NotFoundGlobalException("Không tìm thấy loại hàng hoá");
        }
        System.out.println("--------------category.getGoods()" +category.getGoods());
        Goods goodSearch = goodsRepository.findByName(goodsRequest.getName());
        if(ObjectUtils.isEmpty(goodSearch)){
            Goods goods =new Goods();
            goods.setCode(generateGoodCode());
            goods.setCategory(category);
            goods.setName(goodsRequest.getName());
            goods.setHeight(goodsRequest.getHeight());
            goods.setWidth(goodsRequest.getWidth());
            goods.setLength(goodsRequest.getLength());
            goods.setUnit(EUnit.THUNG);
            goods.setVolume(goodsRequest.getHeight()*goodsRequest.getWidth()*goodsRequest.getLength());
            Goods goodSave=goodsRepository.save(goods);
            return goodSave;
        }
       return goodSearch;
    }

    @Override
    public List<GoodsResponse> getAll() {
        List<GoodsResponse> goodsResponse = goodsRepository.findAll().stream()
                .map(goods -> mapperGoodResponse(goods))
                .collect(Collectors.toList());

        return goodsResponse;
    }

    @Override
    public GoodsResponse getByCode(String code) {
        Goods goods=findGoodByCode(code);
        GoodsResponse response=mapperGoodResponse(goods);
        return response;
    }

    @Override
    public List<GoodsResponse> searchByCodeOrName(String keyword) {
        List<GoodsResponse> goodsResponses=goodsRepository.findByCodeAndName(keyword).stream()
                .map(goods->mapperGoodResponse(goods)).collect(Collectors.toList());
        return goodsResponses;
    }
    @Override
    public Goods findGoodByCode(String code) {
        Goods goods=goodsRepository.findByCode(code);
        if(goods==null){
            throw new NotFoundGlobalException("Không tìm thấy hàng hoá có mã "+code);
        }
        return goods;
    }

    @Override
    public Goods findGoodByName(String name) {
        Goods goods =goodsRepository.findByName(name);
        if(goods==null){
            throw new NotFoundGlobalException("Không tìm thấy hàng hoá tên "+name);
        }
        return goods;
    }

    @Override
    public GoodsResponse mapperGoods(Goods goods) {
        return mapperGoodResponse(goods);
    }

    @Override
    public List<GoodsResponse> getAllByCategoryCode(String categoryCode) {
        List<GoodsResponse> responseList = goodsRepository.findAllByCategory(categoryCode).stream()
                .map(item->mapperGoodResponse(item)).collect(Collectors.toList());
        return responseList;
    }

    public GoodsResponse mapperGoodResponse(Goods goods){
        String unit="";
        if(goods.getUnit().equals(EUnit.THUNG)){
            unit="Thùng";
        }
        GoodsResponse response=modelMapper.map(goods,GoodsResponse.class);
        response.setUnit(unit);
        response.setCategoryCode(goods.getCategory().getCode());
        response.setCategoryName(goods.getCategory().getName());
        return response;
    }

    private String generateGoodCode(){
        Goods goods =goodsRepository.findTopByOrderByIdDesc();
        if(goods==null){
            return "HH00001";
        }
        long id=goods.getId();
        String code = String.format("HH0000%d",id+1);
        return code;
    }
}
