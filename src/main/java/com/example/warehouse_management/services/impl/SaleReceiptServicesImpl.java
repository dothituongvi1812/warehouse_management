package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.purchase.PurchaseReceipt;
import com.example.warehouse_management.models.selling.SaleDetail;
import com.example.warehouse_management.models.selling.SaleReceipt;
import com.example.warehouse_management.models.type.EStatusOfPurchasingGoods;
import com.example.warehouse_management.models.warehouse.BinLocation;
import com.example.warehouse_management.payload.request.GoodDeliveryRequest;
import com.example.warehouse_management.payload.request.GoodsToSaleRequest;
import com.example.warehouse_management.payload.request.SaleReceiptRequest;
import com.example.warehouse_management.payload.response.*;
import com.example.warehouse_management.repository.SaleReceiptRepository;
import com.example.warehouse_management.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaleReceiptServicesImpl implements SaleReceiptServices {
    @Autowired
    SaleReceiptRepository saleReceiptRepository;
    @Autowired
    UserServices userServices;
    @Autowired
    PartnerServices partnerServices;
    @Autowired
    GoodsServices goodsServices;
    @Autowired
    BinLocationServices binLocationServices;
    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public SaleReceiptResponse createSaleReceipt(SaleReceiptRequest saleReceiptRequest) {
        for (GoodsToSaleRequest request:saleReceiptRequest.getGoodsToSaleRequests()) {
            List<BinLocation> binList = binLocationServices.findAllRowLocationByGoodsCode(request.getGoodsCode());
            if(CollectionUtils.isEmpty(binList))
                throw new ErrorException("Hàng hoá có mã "+ request.getGoodsCode() + " chưa nhập vào kho");
        }

        SaleReceipt saleReceipt = new SaleReceipt();
        saleReceipt.setCreatedBy(userServices.findUserByEmail(saleReceiptRequest.getEmail()));
        saleReceipt.setPartner(partnerServices.findPartnerByCode(saleReceiptRequest.getPartnerCode()));
        saleReceipt.setCode(generateSaleReceipt());
        saleReceipt.setCreatedDate(new Date());
        Set<SaleDetail> saleDetails = new HashSet<>();
        for (GoodsToSaleRequest goodsRequest: saleReceiptRequest.getGoodsToSaleRequests()) {
            int currentQuantity= goodsServices.getCurrentQuantityOfGoodsInWarehouse(goodsRequest.getGoodsCode());
            if(goodsRequest.getQuantity()>currentQuantity)
                throw new ErrorException("Hiện tại số lượng của sản phẩm "+ goodsRequest.getGoodsCode()+"trong kho không đủ");
            SaleDetail saleDetail = new SaleDetail();
            saleDetail.setGoods(goodsServices.findGoodByCode(goodsRequest.getGoodsCode()));
            saleDetail.setQuantity(goodsRequest.getQuantity());
            saleDetail.setStatus(EStatusOfPurchasingGoods.NOT_YET_CREATED);
            saleDetails.add(saleDetail);

        }
        saleReceipt.setSaleDetails(saleDetails);
        SaleReceipt saveSaleReceipt= saleReceiptRepository.save(saleReceipt);
        return mapperPurchaseReceiptResponse(saveSaleReceipt);
    }

    @Override
    public List<SaleReceiptResponse> getAll() {
        List<SaleReceiptResponse> saleReceiptResponses = saleReceiptRepository.findAll().stream()
                .map(this::mapperPurchaseReceiptResponse).collect(Collectors.toList());
        return saleReceiptResponses;
    }

    @Override
    public SaleReceipt findSaleReceiptByCode(String saleReceiptCode) {
        SaleReceipt saleReceipt = saleReceiptRepository.findByCode(saleReceiptCode);
        if(ObjectUtils.isEmpty(saleReceipt))
            throw new NotFoundGlobalException("Không tìm thấy phiếu mua "+saleReceiptCode);
        return saleReceipt;
    }

    @Override
    public SaleReceiptResponse getSaleReceiptByCode(String saleReceiptCode) {
        return mapperPurchaseReceiptResponse(findSaleReceiptByCode(saleReceiptCode));
    }

    private SaleReceiptResponse mapperPurchaseReceiptResponse(SaleReceipt saleReceipt){
        SaleReceiptResponse response = new SaleReceiptResponse();
        response.setCode(saleReceipt.getCode());
        response.setCreatedBy(saleReceipt.getCreatedBy().getFullName());
        response.setPartner(modelMapper.map(saleReceipt.getPartner(), PartnerResponse.class));
        Set<SaleDetailResponse> saleDetailSet = new HashSet<>();
        for (SaleDetail detail:saleReceipt.getSaleDetails()) {
            SaleDetailResponse saleDetailResponse = new SaleDetailResponse();
            saleDetailResponse.setGoods(goodsServices.mapperGoods(detail.getGoods()));
            saleDetailResponse.setQuantity(detail.getQuantity());
            saleDetailResponse.setStatus(detail.getStatus());
            saleDetailSet.add(saleDetailResponse);
        }
        response.setSaleDetailResponses(saleDetailSet);
        return response;
    }
    private String generateSaleReceipt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dateFormat = sdf.format(new Date());
        String receiptCode = "PB" + dateFormat;
        SaleReceipt saleReceipt = saleReceiptRepository.findTopByOrderByIdDesc();
        if (saleReceipt == null) {
            return receiptCode + 1;
        }
        long id = saleReceipt.getId();
        return receiptCode + (id + 1);
    }
}
