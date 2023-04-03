package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.NotEnoughSpaceException;
import com.example.warehouse_management.models.goods.GoodsDelivery;
import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.*;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.response.DeliveryVoucherDetailResponse;
import com.example.warehouse_management.payload.response.InventoryDeliveryVoucherResponse;
import com.example.warehouse_management.payload.response.LocationInWarehouse;
import com.example.warehouse_management.payload.response.PartnerResponse;
import com.example.warehouse_management.repository.InventoryDeliveryVoucherRepository;
import com.example.warehouse_management.repository.PartnerRepository;
import com.example.warehouse_management.repository.UserRepository;
import com.example.warehouse_management.services.GoodsServices;
import com.example.warehouse_management.services.InventoryDeliveryVoucherServices;
import com.example.warehouse_management.services.RowLocationServices;
import com.example.warehouse_management.services.domain.UtillServies;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class InventoryDeliveryVoucherServiceImpl implements InventoryDeliveryVoucherServices {
    @Autowired
    InventoryDeliveryVoucherRepository deliveryVoucherRepository;
    @Autowired
    GoodsServices goodsServices;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    RowLocationServices rowLocationServices;
    @Autowired
    GoodsServicesImpl goodsServicesImpl;
    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public InventoryDeliveryVoucherResponse createInventoryDeliveryVoucher(DeliveryVoucherRequest deliveryVoucherRequest) {
        InventoryDeliveryVoucher inventoryDeliveryVoucher = new InventoryDeliveryVoucher();
        User user = userRepository.findUserByEmail(deliveryVoucherRequest.getEmail());
        Partner partner = partnerRepository.findByName(deliveryVoucherRequest.getPartnerName());
        Set<DeliveryVoucherDetail> deliveryVoucherDetails = new HashSet<>();
        List<RowLocationGoodsDeliveryTemp> rowLocationGoodsDeliveryTempList = new ArrayList<>();
        List<GoodsDelivery> goodsDeliveryList = deliveryVoucherRequest.getGoodsRequests().stream()
                .map(item->new GoodsDelivery(goodsServices.findGoodByCode(item.getGoodCode()), item.getQuantity())).collect(Collectors.toList());
        for (GoodsDelivery goodDelivery:goodsDeliveryList) {
            //tìm những vị trí sao cho số lượng current >=quantity request
            int sumQuantityOfGoods=rowLocationServices.getSumCurrentCapacityByGoodsName(goodDelivery.getGoods().getName());
            if(goodDelivery.getQuantity()>sumQuantityOfGoods){
                throw new NotEnoughSpaceException("Không đủ số lượng để xuất");
            }
            List<RowLocation> rowLocationList = rowLocationServices.findAllByGoodsNameEnoughToExport(goodDelivery.getGoods().getName(),goodDelivery.getQuantity());
            for (RowLocation rl:rowLocationList) {
                int currentQuantity=rl.getCurrentCapacity();
                if(currentQuantity>=goodDelivery.getQuantity()){
                    System.out.println("Tao phieu xuat");
                    rowLocationGoodsDeliveryTempList.add(new RowLocationGoodsDeliveryTemp(rl,goodDelivery));
                }else{
                    if(sumQuantityOfGoods<rl.getCurrentCapacity()) {
                        GoodsDelivery goodsDelivery = new GoodsDelivery(goodDelivery.getGoods(),sumQuantityOfGoods);
                        rowLocationGoodsDeliveryTempList.add(new RowLocationGoodsDeliveryTemp(rl,goodsDelivery));
                    }else{
                        GoodsDelivery goodsDelivery = new GoodsDelivery(goodDelivery.getGoods(),currentQuantity);
                        rowLocationGoodsDeliveryTempList.add(new RowLocationGoodsDeliveryTemp(rl,goodsDelivery));
                        sumQuantityOfGoods = sumQuantityOfGoods -goodDelivery.getQuantity();
                    }

                }

            }
        }
        inventoryDeliveryVoucher.setCreateDate(new Date());
        inventoryDeliveryVoucher.setCreatedBy(user);
        inventoryDeliveryVoucher.setPartner(partner);
        inventoryDeliveryVoucher.setReason(deliveryVoucherRequest.getReason());
        inventoryDeliveryVoucher.setCode(generateDeliveryVoucher());
        InventoryDeliveryVoucher deliveryVoucherSave = deliveryVoucherRepository.save(inventoryDeliveryVoucher);
        for (RowLocationGoodsDeliveryTemp item:rowLocationGoodsDeliveryTempList) {
            DeliveryVoucherDetail deliveryVoucherDetail = new DeliveryVoucherDetail();
            deliveryVoucherDetail.setGoods(item.getGoodsDelivery().getGoods());
            deliveryVoucherDetail.setQuantity(item.getGoodsDelivery().getQuantity());
            deliveryVoucherDetail.setRowLocation(item.getRowLocation());
            deliveryVoucherDetail.setInventoryDeliveryVoucher(deliveryVoucherSave);
            deliveryVoucherDetails.add(deliveryVoucherDetail);
        }
        deliveryVoucherSave.setDeliveryVoucherDetails(deliveryVoucherDetails);
        return mapperInventoryDeliveryVoucher(deliveryVoucherRepository.save(deliveryVoucherSave));
    }
    private String generateDeliveryVoucher() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dateFormat = sdf.format(new Date());
        String deliveryCode = "PX" + dateFormat;
        InventoryDeliveryVoucher inventoryDeliveryVoucher = deliveryVoucherRepository.findTopByOrderByIdDesc();
        if (inventoryDeliveryVoucher == null) {
            return deliveryCode + 1;
        }
        long id = inventoryDeliveryVoucher.getId();
        return deliveryCode + (id + 1);
    }
    private InventoryDeliveryVoucherResponse mapperInventoryDeliveryVoucher(InventoryDeliveryVoucher deliveryVoucher){
        Set<DeliveryVoucherDetailResponse>  detailResponseSet = deliveryVoucher.getDeliveryVoucherDetails().stream().map(item ->mapperDeliveryVoucherDetailResponse(item))
                .collect(Collectors.toSet());
        InventoryDeliveryVoucherResponse inventoryDeliveryVoucherResponse = new InventoryDeliveryVoucherResponse();
        inventoryDeliveryVoucherResponse = modelMapper.map(deliveryVoucher,InventoryDeliveryVoucherResponse.class);
        inventoryDeliveryVoucherResponse.setPartner(modelMapper.map(deliveryVoucher.getPartner(), PartnerResponse.class));
        inventoryDeliveryVoucherResponse.setCreatedBy(deliveryVoucher.getCreatedBy().getFullName());
        inventoryDeliveryVoucherResponse.setDetails(detailResponseSet);
        return inventoryDeliveryVoucherResponse;
    }
    private DeliveryVoucherDetailResponse mapperDeliveryVoucherDetailResponse(DeliveryVoucherDetail deliveryVoucherDetail){
        DeliveryVoucherDetailResponse detailResponse = new DeliveryVoucherDetailResponse(goodsServicesImpl.mapperGoodResponse(
                deliveryVoucherDetail.getGoods()), UtillServies.mapperLocationInWarehouse(deliveryVoucherDetail.getRowLocation()),deliveryVoucherDetail.getQuantity());
        return detailResponse;
    }

}
