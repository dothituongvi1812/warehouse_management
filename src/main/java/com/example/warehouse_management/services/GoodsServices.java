package com.example.warehouse_management.services;

import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.payload.request.GoodsRequest;
import com.example.warehouse_management.payload.response.GoodsResponse;

import java.util.List;

public interface GoodsServices {
    public GoodsResponse addGoods(GoodsRequest goodsRequest);
    public List<GoodsResponse> getAll();
    public GoodsResponse getByCode(String code);
    public List<GoodsResponse> searchByCodeOrName(String keyword);
    public Goods findGoodByCode(String code);
}
