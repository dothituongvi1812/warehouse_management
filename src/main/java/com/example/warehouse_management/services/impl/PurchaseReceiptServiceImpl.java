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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.text.ParseException;
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
        if(purchaseDetails == null){
            purchaseReceiptRepository.delete(savePurchase);
            throw new ErrorException("loi tao phieu mua");
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

    @Override
    public Page<PurchaseReceiptResponse> searchByDateOrCodeOrCreatedBy(String date, String code, String createdBy, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<PurchaseReceiptResponse> pages = null;
        if(code!=null){
            List<PurchaseReceiptResponse> responseList = purchaseReceiptRepository.searchByCode(code).stream()
                    .map(item->mapperPurchaseReceiptResponse(item)).collect(Collectors.toList());
            pages = new PageImpl<>(responseList,pageable,responseList.size());
        }
        else if(createdBy!=null){
            List<PurchaseReceiptResponse> responseList = purchaseReceiptRepository.searchByCreatedBy(createdBy).stream()
                    .map(item->mapperPurchaseReceiptResponse(item)).collect(Collectors.toList());
            pages = new PageImpl<>(responseList,pageable,responseList.size());
        }
        else {
            if(date ==null){
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date(System.currentTimeMillis());
                date = formatter.format(currentDate);
            }
            String toDate = date+" 23:59:59";
            String fromDate = date +" 00:00:00";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date parsedFromDate = dateFormat.parse(fromDate);
                Date parsedToDate = dateFormat.parse(toDate);
                Timestamp from = new Timestamp(parsedFromDate.getTime());
                Timestamp to = new Timestamp(parsedToDate.getTime());
                List<PurchaseReceiptResponse> responseList = purchaseReceiptRepository.searchByCreatedDate(from,to).stream()
                        .map(item->mapperPurchaseReceiptResponse(item)).collect(Collectors.toList());
                pages = new PageImpl<>(responseList,pageable,responseList.size());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
       return pages;
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
        response.setCreatedDate(purchaseReceipt.getCreatedDate());
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
