package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.purchase.PurchaseDetail;
import com.example.warehouse_management.models.purchase.PurchaseDetailPK;
import com.example.warehouse_management.models.purchase.PurchaseReceipt;
import com.example.warehouse_management.models.type.EStatusOfPurchasingGoods;
import com.example.warehouse_management.models.type.EStatusPurchaseReceipt;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.payload.request.goods.GoodsRequest;
import com.example.warehouse_management.payload.request.purchase.PurchaseReceiptRequest;
import com.example.warehouse_management.payload.response.*;
import com.example.warehouse_management.repository.BinLocationRepository;
import com.example.warehouse_management.repository.PurchaseDetailRepository;
import com.example.warehouse_management.repository.PurchaseReceiptRepository;
import com.example.warehouse_management.services.GoodsServices;
import com.example.warehouse_management.services.PartnerServices;
import com.example.warehouse_management.services.PurchaseReceiptServices;
import com.example.warehouse_management.services.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseReceiptServiceImpl implements PurchaseReceiptServices {
    @Autowired
    PurchaseReceiptRepository purchaseReceiptRepository;
    @Autowired
    PartnerServices partnerServices;
    @Autowired
    UserServices userServices;
    @Autowired
    GoodsServices goodsServices;
    @Autowired
    PurchaseDetailRepository purchaseDetailRepository;
    @Autowired
    BinLocationRepository binLocationRepository;
    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public PurchaseReceiptResponse createPurchaseReceipt(PurchaseReceiptRequest purchaseReceiptRequest) {
        Partner partner = partnerServices.addPartner(purchaseReceiptRequest.getPartnerRequest());
        User user = userServices.findUserByEmail(purchaseReceiptRequest.getEmail());
        PurchaseReceipt purchaseReceipt = new PurchaseReceipt();
        purchaseReceipt.setCode(generatePurchaseReceipt());
        purchaseReceipt.setPartner(partner);
        purchaseReceipt.setCreatedBy(user);
        purchaseReceipt.setCreatedDate(new Date());
        purchaseReceipt.setStatus(EStatusPurchaseReceipt.NOT_DONE_YET);
        PurchaseReceipt savePurchase= purchaseReceiptRepository.save(purchaseReceipt);
        Set<PurchaseDetail> purchaseDetails = new HashSet<>();
        for (GoodsRequest goodsRequest: purchaseReceiptRequest.getGoodsRequests()) {
            PurchaseDetail purchaseDetail = new PurchaseDetail();
            purchaseDetail.setPurchaseReceipt(savePurchase);
            Goods goods = goodsServices.createGoods(goodsRequest);
            purchaseDetail.setGoods(goods);
            purchaseDetail.setPurchaseDetailPK(new PurchaseDetailPK(savePurchase.getId(),goods.getId()));
            purchaseDetail.setQuantityPurchased(goodsRequest.getQuantity());
            purchaseDetail.setQuantityRemaining(goodsRequest.getQuantity());
            purchaseDetail.setStatus(EStatusOfPurchasingGoods.NOT_YET_CREATED);
            purchaseDetailRepository.save(purchaseDetail);
            purchaseDetails.add(purchaseDetail);

        }
        savePurchase.setPurchaseDetails(purchaseDetails);
         purchaseReceiptRepository.save(savePurchase);
        return mapperPurchaseReceiptResponse(savePurchase);
    }

    @Override
    public List<PurchaseReceiptResponse> getAll() {
        List<PurchaseReceipt> purchaseReceipts = purchaseReceiptRepository.findAll();
        purchaseReceipts.stream().forEach(purchase->{
            int flag = purchase.getPurchaseDetails().size();
            int temp = (int) purchase.getPurchaseDetails().stream().filter(detail -> detail.getStatus().equals(EStatusOfPurchasingGoods.CREATED)).count();
            if(flag == temp){
                purchase.setStatus(EStatusPurchaseReceipt.DONE);
                purchaseReceiptRepository.save(purchase);
            }
            else{
                purchase.setStatus(EStatusPurchaseReceipt.NOT_DONE_YET);
                purchaseReceiptRepository.save(purchase);
            }
        });
        List<PurchaseReceiptResponse> purchaseReceiptResponses = purchaseReceipts.stream()
                .map(this::mapperPurchaseReceiptResponse).collect(Collectors.toList());

        return purchaseReceiptResponses;
    }

    @Override
    public PurchaseReceipt findPurchaseReceiptByCode(String purchaseReceiptCode) {
        PurchaseReceipt purchaseReceipt = purchaseReceiptRepository.findByCode(purchaseReceiptCode);
        if(ObjectUtils.isEmpty(purchaseReceipt))
            throw new NotFoundGlobalException("Không tìm thấy phiếu mua"+purchaseReceiptCode);
        return purchaseReceipt;
    }

    @Override
    public PurchaseReceiptResponse getPurchaseReceiptByCode(String purchaseReceiptCode) {
        return mapperPurchaseReceiptResponse(findPurchaseReceiptByCode(purchaseReceiptCode));
    }

    private String generatePurchaseReceipt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dateFormat = sdf.format(new Date());
        String receiptCode = "PM" + dateFormat;
        PurchaseReceipt purchaseReceipt = purchaseReceiptRepository.findTopByOrderByIdDesc();
        if (purchaseReceipt == null) {
            return receiptCode + 1;
        }
        long id = purchaseReceipt.getId();
        return receiptCode + (id + 1);
    }
    private PurchaseReceiptResponse mapperPurchaseReceiptResponse(PurchaseReceipt purchaseReceipt){
        PurchaseReceiptResponse response = new PurchaseReceiptResponse();
        response.setCode(purchaseReceipt.getCode());
        response.setCreatedBy(purchaseReceipt.getCreatedBy().getFullName());
        response.setPartner(modelMapper.map(purchaseReceipt.getPartner(), PartnerResponse.class));
        response.setStatus(purchaseReceipt.getStatus());
        Set<PurchaseDetailResponse> purchaseDetailResponseSet = new HashSet<>();
        for (PurchaseDetail detail:purchaseReceipt.getPurchaseDetails()) {
            PurchaseDetailResponse purchaseDetailResponse = new PurchaseDetailResponse();
            purchaseDetailResponse.setGoods(goodsServices.mapperGoods(detail.getGoods()));
            purchaseDetailResponse.setQuantityPurchased(detail.getQuantityPurchased());
            purchaseDetailResponse.setQuantityRemaining(detail.getQuantityRemaining());
            purchaseDetailResponse.setStatus(detail.getStatus());
            purchaseDetailResponseSet.add(purchaseDetailResponse);
        }
        response.setPurchaseDetails(purchaseDetailResponseSet);
        return response;
    }
    private double[] createArraySize(double width, double height, double length) {
        double[] size = {width, height, length};
        Arrays.sort(size);
        return size;

    }

    private boolean compareSizeShelfAndGoods(double[] sizeShelf, double[] sizeGoods, String goodsName) {
        if (sizeShelf[0] >= sizeGoods[0] && sizeShelf[1] >= sizeGoods[1] && sizeShelf[2] >= sizeGoods[2])
            return true;
        else
            throw new ErrorException(goodsName + "có kích thước quá lớn,kho không thể chứa");
    }
}
