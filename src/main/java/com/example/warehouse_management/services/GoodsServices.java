package com.example.warehouse_management.services;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.payload.request.goods.GoodsAddRequest;
import com.example.warehouse_management.payload.request.goods.GoodsRequest;
import com.example.warehouse_management.payload.request.goods.UpdateGoodsRequest;
import com.example.warehouse_management.payload.response.GoodsResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface GoodsServices {
    public GoodsResponse addGoods(GoodsAddRequest goodsRequest);
    public Goods createGoods(GoodsRequest goodsRequest);
    public Page<GoodsResponse> getPage(Integer page, Integer size);
    public GoodsResponse getByCode(String code);
    public List<GoodsResponse> searchByCodeOrName(String keyword);
    public Goods findGoodByCode(String code);
    public Goods findGoodByName(String name);
    public GoodsResponse mapperGoods(Goods goods);
    public List<GoodsResponse> getAllByCategoryCode(String categoryCode);
    public List<GoodsResponse> getAll();
    public Integer getCurrentQuantityOfGoodsInWarehouse(String goodsCode);
    Map<String, Integer> countCurrentQuantityOfGoodsInWarehouse();
    public GoodsResponse updateGoods(String goodsCode, UpdateGoodsRequest updateGoodsRequest);
}
