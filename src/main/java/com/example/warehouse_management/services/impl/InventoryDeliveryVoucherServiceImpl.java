package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.goods.GoodsDelivery;
import com.example.warehouse_management.models.partner.Partner;
import com.example.warehouse_management.models.type.EStatusOfVoucher;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.*;
import com.example.warehouse_management.models.warehouse.RowLocation;
import com.example.warehouse_management.payload.request.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.response.*;
import com.example.warehouse_management.repository.InventoryDeliveryVoucherRepository;
import com.example.warehouse_management.repository.RowLocationRepository;
import com.example.warehouse_management.repository.UserRepository;
import com.example.warehouse_management.services.GoodsServices;
import com.example.warehouse_management.services.InventoryDeliveryVoucherServices;
import com.example.warehouse_management.services.PartnerServices;
import com.example.warehouse_management.services.RowLocationServices;
import com.example.warehouse_management.services.domain.UtillServies;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    private PartnerServices partnerServices;
    @Autowired
    RowLocationServices rowLocationServices;
    @Autowired
    RowLocationRepository rowLocationRepository;
    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public InventoryDeliveryVoucherResponse createInventoryDeliveryVoucher(DeliveryVoucherRequest deliveryVoucherRequest) {
        InventoryDeliveryVoucher inventoryDeliveryVoucher = new InventoryDeliveryVoucher();
        User user = userRepository.findUserByEmail(deliveryVoucherRequest.getEmail());
        Partner partner = partnerServices.findPartnerByPhone(deliveryVoucherRequest.getPartnerPhone());
        Set<DeliveryVoucherDetail> deliveryVoucherDetails = new HashSet<>();
        List<RowLocationGoodsDeliveryTemp> rowLocationGoodsDeliveryTempList = new ArrayList<>();
        List<GoodsDelivery> goodsDeliveryList = deliveryVoucherRequest.getGoodsRequests().stream()
                .map(item->new GoodsDelivery(goodsServices.findGoodByCode(item.getGoodCode()), item.getQuantity())).collect(Collectors.toList());
        for (GoodsDelivery goodDelivery:goodsDeliveryList) {
            //tìm những vị trí sao cho số lượng current >=quantity request
            int sumQuantityOfGoods=rowLocationServices.getSumCurrentCapacityByGoodsName(goodDelivery.getGoods().getName());
            if(goodDelivery.getQuantity()>sumQuantityOfGoods){
                throw new ErrorException("Không đủ số lượng để xuất");
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
        inventoryDeliveryVoucher.setStatus(EStatusOfVoucher.NOT_YET_EXPORTED);
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

    @Override
    public List<RowLocationResponse> exportGoods(String deliveryVoucherCode) {
        InventoryDeliveryVoucher inventoryDeliveryVoucher = deliveryVoucherRepository.findByCode(deliveryVoucherCode);
        if(inventoryDeliveryVoucher.getStatus().equals(EStatusOfVoucher.EXPORTED))
            throw new ErrorException("Phiếu xuất" + deliveryVoucherCode + "đã được xuất");
        List<RowLocation> rowLocationList = new ArrayList<>();
        if (ObjectUtils.isEmpty(inventoryDeliveryVoucher))
            throw new NotFoundGlobalException("Không tìm thấy phiếu xuất "+ deliveryVoucherCode);
        inventoryDeliveryVoucher.setStatus(EStatusOfVoucher.EXPORTED);
        for (DeliveryVoucherDetail detail:inventoryDeliveryVoucher.getDeliveryVoucherDetails()) {
            Goods goods = detail.getGoods();
            RowLocation rowLocation = detail.getRowLocation();
            int currentCapacity=rowLocation.getCurrentCapacity();
            double remainingVolume= rowLocation.getRemainingVolume();
            rowLocation.setCurrentCapacity(currentCapacity-detail.getQuantity());
            if(currentCapacity-detail.getQuantity()==0){
                rowLocation.setStatus(EStatusStorage.EMPTY);
                rowLocation.setGoods(null);
                rowLocation.setRemainingVolume(rowLocation.getVolume());
            }
            else{
                rowLocation.setRemainingVolume(remainingVolume+ goods.getVolume()*detail.getQuantity());
            }
            RowLocation rowLocation1 = rowLocationRepository.save(rowLocation);
            rowLocationList.add(rowLocation1);

        }
        List<RowLocationResponse> rowLocationResponses = rowLocationList.stream().map(item ->
                rowLocationServices.mapperRowLocation(item)).collect(Collectors.toList());
        deliveryVoucherRepository.save(inventoryDeliveryVoucher);
        return rowLocationResponses;
    }

    @Override
    public InventoryDeliveryVoucherResponse getDeliveryVoucherByCode(String deliveryVoucherCode) {
        InventoryDeliveryVoucher deliveryVoucher = deliveryVoucherRepository.findByCode(deliveryVoucherCode);
        if(ObjectUtils.isEmpty(deliveryVoucher))
            throw new NotFoundGlobalException("Không tìm thấy phiếu xuất "+deliveryVoucherCode);
        return mapperInventoryDeliveryVoucher(deliveryVoucher);
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
        String status ="";
        switch (deliveryVoucher.getStatus()){
            case EXPORTED:
                status="Đã xuất";
                break;
            case NOT_YET_EXPORTED:
                status="Chưa xuất";
                break;

        }
        Set<DeliveryVoucherDetailResponse>  detailResponseSet = deliveryVoucher.getDeliveryVoucherDetails().stream().map(item ->mapperDeliveryVoucherDetailResponse(item))
                .collect(Collectors.toSet());
        InventoryDeliveryVoucherResponse inventoryDeliveryVoucherResponse = new InventoryDeliveryVoucherResponse();
        inventoryDeliveryVoucherResponse = modelMapper.map(deliveryVoucher,InventoryDeliveryVoucherResponse.class);
        inventoryDeliveryVoucherResponse.setPartner(modelMapper.map(deliveryVoucher.getPartner(), PartnerResponse.class));
        inventoryDeliveryVoucherResponse.setCreatedBy(deliveryVoucher.getCreatedBy().getFullName());
        inventoryDeliveryVoucherResponse.setDetails(detailResponseSet);
        inventoryDeliveryVoucherResponse.setStatus(status);
        return inventoryDeliveryVoucherResponse;
    }
    private DeliveryVoucherDetailResponse mapperDeliveryVoucherDetailResponse(DeliveryVoucherDetail deliveryVoucherDetail){
        DeliveryVoucherDetailResponse detailResponse = new DeliveryVoucherDetailResponse(goodsServices.mapperGoods(
                deliveryVoucherDetail.getGoods()), UtillServies.mapperLocationInWarehouse(deliveryVoucherDetail.getRowLocation()),deliveryVoucherDetail.getQuantity());
        return detailResponse;
    }

}
