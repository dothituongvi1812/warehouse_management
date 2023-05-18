package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Category;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.type.EUnit;
import com.example.warehouse_management.models.warehouse.BinPosition;
import com.example.warehouse_management.payload.request.goods.GoodsAddRequest;
import com.example.warehouse_management.payload.request.goods.GoodsRequest;
import com.example.warehouse_management.payload.request.goods.UpdateGoodsRequest;
import com.example.warehouse_management.payload.response.GoodsResponse;
import com.example.warehouse_management.repository.BinLocationRepository;
import com.example.warehouse_management.repository.CategoryRepository;
import com.example.warehouse_management.repository.GoodsRepository;
import com.example.warehouse_management.services.GoodsServices;
import com.example.warehouse_management.services.domain.ObjectsUtils;
import com.example.warehouse_management.services.domain.UtillServies;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsServicesImpl implements GoodsServices {
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BinLocationRepository binLocationRepository;

    private ModelMapper modelMapper=new ModelMapper();
    @Override
    public GoodsResponse addGoods(GoodsAddRequest goodsAddRequest) {
        GoodsRequest goodsRequest  = modelMapper.map(goodsAddRequest,GoodsRequest.class);
        GoodsResponse response=mapperGoodResponse(createGoods(goodsRequest));
        return response;
    }
    @Override
    public Goods createGoods(GoodsRequest goodsRequest) {
        BinPosition binPosition = binLocationRepository.findBinLocationMinVolume();
        UtillServies.validateGoods(goodsRequest, binPosition);
        Category category=categoryRepository.findByCode(goodsRequest.getCategoryCode());
        if(category==null){
            throw new NotFoundGlobalException("Không tìm thấy loại hàng hoá");
        }
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
            goods.setImage(goodsRequest.getImage());
            goods.setVolume(goodsRequest.getHeight()*goodsRequest.getWidth()*goodsRequest.getLength());
            Goods goodSave=goodsRepository.save(goods);
            return goodSave;
        }
        else{
            return goodSearch;
        }
    }

    @Override
    public Page<GoodsResponse> getPage(Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Goods> goodsPage= goodsRepository.findAll(pageable);
        Page<GoodsResponse> pages = new PageImpl<GoodsResponse>(goodsPage.getContent()
                .stream().map(this::mapperGoodResponse).collect(Collectors.toList()), pageable,
                goodsPage.getTotalElements());

        return pages;
    }

    @Override
    public GoodsResponse getByCode(String code) {
        Goods goods=findGoodByCode(code);
        GoodsResponse response=mapperGoodResponse(goods);
        return response;
    }

    @Override
    public Page<GoodsResponse> searchByCodeOrName(String keyword,Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<GoodsResponse> goodsResponses=goodsRepository.findByCodeAndName(keyword).stream()
                .map(goods->mapperGoodResponse(goods)).collect(Collectors.toList());
        Page<GoodsResponse> pages = new PageImpl<>(goodsResponses, pageable, goodsResponses.size());
        return pages;
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

    @Override
    public List<GoodsResponse> getAll() {
        List<GoodsResponse> goodsResponse = goodsRepository.findAll().stream()
                .map(goods -> mapperGoodResponse(goods))
                .collect(Collectors.toList());

        return goodsResponse;
    }

    @Override
    public Integer getCurrentQuantityOfGoodsInWarehouse(String goodsCode) {
        // kiểm tra hàng hóa đó đã có trên kệ chưa, nếu chưa thì trả về 0 còn có đi query
        if(goodsRepository.getCurrentQuantityOfGoodsInWarehouse(goodsCode) == null)
            return 0;
        else
            return Integer.valueOf(goodsRepository.getCurrentQuantityOfGoodsInWarehouse(goodsCode));
    }

    @Override
    public Map<String, Integer> countCurrentQuantityOfGoodsInWarehouse() {
        List<Object[]> objects = goodsRepository.countCurrentQuantityOfGoodsInWarehouse();
        Map<String,Integer> map = new HashMap<>();
        for (Object[] ob : objects){
            String key = (String)ob[0];
            Integer value = ((BigInteger) ob[1]).intValue();
            map.put(key,value);
        }
        return map;
    }

    @Override
    public GoodsResponse updateGoods(String goodsCode, UpdateGoodsRequest updateGoodsRequest) {
        Goods goods = findGoodByCode(goodsCode);
        if(!ObjectsUtils.equal(goods.getName(),updateGoodsRequest.getName())){
            goods.setName(updateGoodsRequest.getName());
        }
        if(!ObjectsUtils.equalDouble(goods.getHeight(),updateGoodsRequest.getHeight())){
            goods.setHeight(updateGoodsRequest.getHeight());
        }
        if(!ObjectsUtils.equalDouble(goods.getLength(),updateGoodsRequest.getLength())){
            goods.setHeight(updateGoodsRequest.getLength());
        }
        if(!ObjectsUtils.equalDouble(goods.getWidth(),updateGoodsRequest.getWidth())){
            goods.setHeight(updateGoodsRequest.getWidth());
        }
        if(!ObjectsUtils.equal(goods.getCategory().getCode(),updateGoodsRequest.getCategoryCode())){
            goods.setCategory(categoryRepository.findByCode(updateGoodsRequest.getCategoryCode()));
        }
        if(!ObjectsUtils.equal(goods.getImage(),updateGoodsRequest.getImage())){
            goods.setImage(updateGoodsRequest.getImage());
        }
        Goods goodsUpdate = goodsRepository.save(goods);
        return mapperGoods(goodsUpdate);
    }

    @Override
    public Map<String, Integer> reportImportedQuantityGoodsByDate(String date) {
        String toDate = date+" 23:59:59";
        String fromDate = date +" 00:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String,Integer> map = new HashMap<>();
        try {
            Date parsedFromDate = dateFormat.parse(fromDate);
            Date parsedToDate = dateFormat.parse(toDate);
            Timestamp from = new Timestamp(parsedFromDate.getTime());
            Timestamp to = new Timestamp(parsedToDate.getTime());
            List<Object[]> objects = goodsRepository.reportImportedQuantityGoodsByDate(from,to);

            for (Object[] ob : objects){
                String key = (String)ob[0];
                Integer value = ((BigInteger) ob[1]).intValue();
                map.put(key,value);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Override
    public Map<String, Integer> reportExportedQuantityGoodsByDate(String date) {
        String toDate = date+" 23:59:59";
        String fromDate = date +" 00:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String,Integer> map = new HashMap<>();
        try {
            Date parsedFromDate = dateFormat.parse(fromDate);
            Date parsedToDate = dateFormat.parse(toDate);
            Timestamp from = new Timestamp(parsedFromDate.getTime());
            Timestamp to = new Timestamp(parsedToDate.getTime());
            List<Object[]> objects = goodsRepository.reportExportedQuantityGoodsByDate(from,to);

            for (Object[] ob : objects){
                String key = (String)ob[0];
                Integer value = ((BigInteger) ob[1]).intValue();
                map.put(key,value);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Override
    public Map<String, Integer> reportSumQuantityImportedByPeriod(String fromDate, String toDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(fromDate == null || toDate ==null ){
            LocalDateTime date = LocalDateTime.now();
            toDate = date.format(formatter);
            fromDate = date.withDayOfMonth(1).format(formatter);
        }
        else{
            toDate =toDate+" 23:59:59";
            fromDate = fromDate +" 00:00:00";
        }

        Map<String,Integer> map = new HashMap<>();
        try {
            Date parsedFromDate = dateFormat.parse(fromDate);
            Date parsedToDate = dateFormat.parse(toDate);
            Timestamp from = new Timestamp(parsedFromDate.getTime());
            Timestamp to = new Timestamp(parsedToDate.getTime());
            List<Object[]> objects = goodsRepository.reportSumQuantityImportedByPeriod(from,to);
            for (Object[] ob : objects){
                String key = (String)ob[0];
                Integer value = ((BigInteger) ob[1]).intValue();
                map.put(key,value);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Override
    public Map<String, Integer> reportSumQuantityExportedByPeriod(String fromDate, String toDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(fromDate == null || toDate ==null ){
            LocalDateTime date = LocalDateTime.now();
            toDate = date.format(formatter);
            fromDate = date.withDayOfMonth(1).format(formatter);
        }
        else{
            toDate =toDate+" 23:59:59";
            fromDate = fromDate +" 00:00:00";
        }

        Map<String,Integer> map = new HashMap<>();
        try {
            Date parsedFromDate = dateFormat.parse(fromDate);
            Date parsedToDate = dateFormat.parse(toDate);
            Timestamp from = new Timestamp(parsedFromDate.getTime());
            Timestamp to = new Timestamp(parsedToDate.getTime());
            List<Object[]> objects = goodsRepository.reportSumQuantityExportedByPeriod(from,to);
            for (Object[] ob : objects){
                String key = (String)ob[0];
                Integer value = ((BigInteger) ob[1]).intValue();
                map.put(key,value);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Override
    public Map<String, Integer> statisticOfTheMostImportedProducts(int month) {
        Map<String,Integer> map = new HashMap<>();
        List<Object[]> objects = goodsRepository.statisticOfTheMostImportedProducts(month);
        for (Object[] ob : objects){
            String key = (String)ob[0];
            Integer value = ((BigInteger) ob[1]).intValue();
            map.put(key,value);
        }
        return map;
    }

    @Override
    public Map<String, Integer> statisticOfTheMostExportedProducts(int month) {
        Map<String,Integer> map = new HashMap<>();
        List<Object[]> objects = goodsRepository.statisticOfTheMostExportedProducts(month);
        for (Object[] ob : objects){
            String key = (String)ob[0];
            Integer value = ((BigInteger) ob[1]).intValue();
            map.put(key,value);
        }
        return map;
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
