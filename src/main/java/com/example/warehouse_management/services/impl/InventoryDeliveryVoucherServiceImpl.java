package com.example.warehouse_management.services.impl;

import com.amazonaws.util.CollectionUtils;
import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.type.EStatusOfSellingGoods;
import com.example.warehouse_management.repository.*;
import com.example.warehouse_management.utils.GoodsDelivery;
import com.example.warehouse_management.models.selling.SaleReceipt;
import com.example.warehouse_management.models.type.EStatusOfVoucher;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.models.voucher.*;
import com.example.warehouse_management.models.warehouse.BinPosition;
import com.example.warehouse_management.payload.request.delivery.DeliveryVoucherRequest;
import com.example.warehouse_management.payload.response.*;
import com.example.warehouse_management.services.*;
import com.example.warehouse_management.services.domain.UtillServies;
import com.example.warehouse_management.utils.RowLocationGoodsDeliveryTemp;
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
public class InventoryDeliveryVoucherServiceImpl implements InventoryDeliveryVoucherServices {
    @Autowired
    InventoryDeliveryVoucherRepository deliveryVoucherRepository;
    @Autowired
    GoodsServices goodsServices;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BinLocationServices binLocationServices;
    @Autowired
    BinLocationRepository binLocationRepository;
    @Autowired
    SaleReceiptServices saleReceiptServices;
    @Autowired
    SaleDetailRepository saleDetailRepository;
    @Autowired
    SaleReceiptRepository saleReceiptRepository;

    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public InventoryDeliveryVoucherResponse createInventoryDeliveryVoucher(String saleReceiptCode,DeliveryVoucherRequest deliveryVoucherRequest) {
        SaleReceipt saleReceipt = saleReceiptServices.findSaleReceiptByCode(saleReceiptCode);
        InventoryDeliveryVoucher inventoryDeliveryVoucher = new InventoryDeliveryVoucher();
        inventoryDeliveryVoucher.setSaleReceipt(saleReceipt);
        User user = userRepository.findUserByEmail(deliveryVoucherRequest.getEmail());
        Set<InventoryDeliveryVoucherDetail> inventoryDeliveryVoucherDetails = new HashSet<>();
        List<RowLocationGoodsDeliveryTemp> rowLocationGoodsDeliveryTempList = new ArrayList<>();
        List<GoodsDelivery> goodsDeliveryList = deliveryVoucherRequest.getGoodsRequests().stream()
                .map(item->new GoodsDelivery(goodsServices.findGoodByCode(item.getGoodCode()), item.getQuantity())).collect(Collectors.toList());
        for (GoodsDelivery goodDelivery:goodsDeliveryList) {
            //tìm 1 vị trí đủ để xuất
            BinPosition binPosition = binLocationServices.findOnePosition(goodDelivery.getQuantity(),goodDelivery.getGoods().getCode());
            if(!ObjectUtils.isEmpty(binPosition)){
                rowLocationGoodsDeliveryTempList.add(new RowLocationGoodsDeliveryTemp(binPosition,goodDelivery));
            }
            else{
                //tìm những vị trí sao cho số lượng current >=quantity request
                int sumQuantityOfGoods= binLocationServices.getSumCurrentCapacityByGoodsName(goodDelivery.getGoods().getName()).intValue();
                if(goodDelivery.getQuantity()>sumQuantityOfGoods){
                    throw new ErrorException("Không đủ số lượng để xuất");
                }
                List<BinPosition> binList = binLocationServices.findAllByGoodsNameEnoughToExport(goodDelivery.getGoods().getName(),goodDelivery.getQuantity());
                for (BinPosition rl: binList) {
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

            saleReceipt.getSaleDetails().stream().forEach(e->{
                if(e.getGoods().equals(goodDelivery.getGoods())){
                    e.setQuantityRemaining(e.getQuantityRemaining()-goodDelivery.getQuantity());
                    if(e.getQuantityRemaining()==0){
                        e.setStatus(EStatusOfSellingGoods.CREATED);
                    }
                    else{
                        e.setStatus(EStatusOfSellingGoods.NOT_YET_CREATED);
                    }
                    saleDetailRepository.save(e);
                }
            });
            saleReceiptRepository.save(saleReceipt);
        }
        inventoryDeliveryVoucher.setCreateDate(new Date());
        inventoryDeliveryVoucher.setCreatedBy(user);
        inventoryDeliveryVoucher.setCode(generateDeliveryVoucher());
        inventoryDeliveryVoucher.setStatus(EStatusOfVoucher.NOT_YET_EXPORTED);
        InventoryDeliveryVoucher deliveryVoucherSave = deliveryVoucherRepository.save(inventoryDeliveryVoucher);
        for (RowLocationGoodsDeliveryTemp item:rowLocationGoodsDeliveryTempList) {
            InventoryDeliveryVoucherDetail inventoryDeliveryVoucherDetail = new InventoryDeliveryVoucherDetail();
            inventoryDeliveryVoucherDetail.setGoods(item.getGoodsDelivery().getGoods());
            inventoryDeliveryVoucherDetail.setQuantity(item.getGoodsDelivery().getQuantity());
            inventoryDeliveryVoucherDetail.setBinPosition(item.getBinPosition());
            inventoryDeliveryVoucherDetail.setInventoryDeliveryVoucherDetailPK(new InventoryDeliveryVoucherDetailPK(item.getGoodsDelivery().getGoods().getId(),deliveryVoucherSave.getId(),item.getBinPosition().getId()));
            inventoryDeliveryVoucherDetail.setInventoryDeliveryVoucher(deliveryVoucherSave);
            inventoryDeliveryVoucherDetails.add(inventoryDeliveryVoucherDetail);
        }
        deliveryVoucherSave.setInventoryDeliveryVoucherDetails(inventoryDeliveryVoucherDetails);
        return mapperInventoryDeliveryVoucher(deliveryVoucherRepository.save(deliveryVoucherSave));
    }

    @Override
    public List<BinPositionResponse> exportGoods(String deliveryVoucherCode) {
        InventoryDeliveryVoucher inventoryDeliveryVoucher = deliveryVoucherRepository.findByCode(deliveryVoucherCode);
        if (ObjectUtils.isEmpty(inventoryDeliveryVoucher))
            throw new NotFoundGlobalException("Không tìm thấy phiếu xuất "+ deliveryVoucherCode);
        if(inventoryDeliveryVoucher.isCanceled()==true)
            throw new ErrorException("Phiếu xuất" + deliveryVoucherCode + "đã bị huỷ");
        if(inventoryDeliveryVoucher.getStatus().equals(EStatusOfVoucher.EXPORTED))
            throw new ErrorException("Phiếu xuất" + deliveryVoucherCode + "đã được xuất");
        List<BinPosition> binList = new ArrayList<>();

        inventoryDeliveryVoucher.setExportedDate(new Date());
        inventoryDeliveryVoucher.setStatus(EStatusOfVoucher.EXPORTED);
        for (InventoryDeliveryVoucherDetail detail:inventoryDeliveryVoucher.getInventoryDeliveryVoucherDetails()) {
            Goods goods =goodsServices.findGoodByCode(detail.getGoods().getCode());
            BinPosition bin = detail.getBinPosition();
            int currentCapacity= bin.getCurrentCapacity();
            double remainingVolume= bin.getRemainingVolume();
            bin.setCurrentCapacity(currentCapacity-detail.getQuantity());
            if(currentCapacity-detail.getQuantity()==0){
                bin.setStatus(EStatusStorage.EMPTY);
                bin.setGoods(null);
                bin.setRemainingVolume(bin.getVolume());
            }
            if(bin.getMaxCapacity()==bin.getCurrentCapacity()){
                bin.setStatus(EStatusStorage.FULL);
            }
            else{
                bin.setRemainingVolume(remainingVolume+ goods.getVolume()*detail.getQuantity());
            }

            BinPosition bin1 = binLocationRepository.save(bin);
            binList.add(bin1);

        }
        List<BinPositionResponse> binPositionRespons = binList.stream().map(item ->
                binLocationServices.mapperRowLocation(item)).collect(Collectors.toList());
        deliveryVoucherRepository.save(inventoryDeliveryVoucher);
        return binPositionRespons;
    }

    @Override
    public InventoryDeliveryVoucherResponse getDeliveryVoucherByCode(String deliveryVoucherCode) {
        InventoryDeliveryVoucher deliveryVoucher = deliveryVoucherRepository.findByCode(deliveryVoucherCode);
        if(ObjectUtils.isEmpty(deliveryVoucher))
            throw new NotFoundGlobalException("Không tìm thấy phiếu xuất "+deliveryVoucherCode);
        return mapperInventoryDeliveryVoucher(deliveryVoucher);
    }

    @Override
    public Page<InventoryDeliveryVoucherResponse> getPageSortedByDate(Integer page,Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<InventoryDeliveryVoucher> vouchers= deliveryVoucherRepository.getPage(pageable);
        Page<InventoryDeliveryVoucherResponse> pages = new PageImpl<InventoryDeliveryVoucherResponse>(vouchers.getContent()
                .stream().map(this::mapperInventoryDeliveryVoucher).collect(Collectors.toList()), pageable,
                vouchers.getTotalElements());
        return pages;
    }

    @Override
    public List<InventoryDeliveryVoucherResponse> getAll() {
        List<InventoryDeliveryVoucherResponse> responseList = deliveryVoucherRepository.findAll().stream()
                .map(item->mapperInventoryDeliveryVoucher(item))
                .sorted(Comparator.comparing(InventoryDeliveryVoucherResponse::getCreateDate))
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public Page<InventoryDeliveryVoucherResponse> searchByDateOrCodeOrCreatedBy(String date,String code, String createdBy,Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<InventoryDeliveryVoucherResponse> pages =null ;
        if(code!=null){
            List<InventoryDeliveryVoucherResponse> responseList = deliveryVoucherRepository.searchByCode(code).stream()
                    .map(item->mapperInventoryDeliveryVoucher(item)).collect(Collectors.toList());
            pages = new PageImpl<>(responseList,pageable,responseList.size());
        }
        else if(createdBy!=null){
            List<InventoryDeliveryVoucherResponse> responseList = deliveryVoucherRepository.searchByCreatedBy(createdBy).stream()
                    .map(item->mapperInventoryDeliveryVoucher(item)).collect(Collectors.toList());
            pages = new PageImpl<>(responseList,pageable,responseList.size());
        }else{
                if(date == null){
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
                List<InventoryDeliveryVoucherResponse> responseList = deliveryVoucherRepository.searchByDate(from,to).stream()
                        .map(item->mapperInventoryDeliveryVoucher(item)).collect(Collectors.toList());
                pages = new PageImpl<>(responseList,pageable,responseList.size());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return pages;
    }

    @Override
    public boolean cancelInventoryDeliveryVoucherByCode(String code) {
        InventoryDeliveryVoucher deliveryVoucher = deliveryVoucherRepository.findByCode(code);
        if(ObjectUtils.isEmpty(deliveryVoucher))
            throw new NotFoundGlobalException("Không tìm thấy phiếu xuất "+deliveryVoucher);
        deliveryVoucher.setCanceled(true);
        deliveryVoucher.setStatus(EStatusOfVoucher.IS_CANCELED);
        deliveryVoucherRepository.save(deliveryVoucher);
        return true;
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
            case IS_CANCELED:
                status="Đã huỷ";
                break;

        }
        Set<DeliveryVoucherDetailResponse>  detailResponseSet = deliveryVoucher.getInventoryDeliveryVoucherDetails().stream().map(item ->mapperDeliveryVoucherDetailResponse(item))
                .collect(Collectors.toSet());
        InventoryDeliveryVoucherResponse inventoryDeliveryVoucherResponse = new InventoryDeliveryVoucherResponse();
        inventoryDeliveryVoucherResponse = modelMapper.map(deliveryVoucher,InventoryDeliveryVoucherResponse.class);
        inventoryDeliveryVoucherResponse.setPartner(modelMapper.map(deliveryVoucher.getSaleReceipt().getPartner(), PartnerResponse.class));
        inventoryDeliveryVoucherResponse.setCreatedBy(deliveryVoucher.getCreatedBy().getFullName());
        inventoryDeliveryVoucherResponse.setDetails(detailResponseSet);
        inventoryDeliveryVoucherResponse.setStatus(status);
        inventoryDeliveryVoucherResponse.setCanceled(deliveryVoucher.isCanceled());
        return inventoryDeliveryVoucherResponse;
    }
    private DeliveryVoucherDetailResponse mapperDeliveryVoucherDetailResponse(InventoryDeliveryVoucherDetail inventoryDeliveryVoucherDetail){
        DeliveryVoucherDetailResponse detailResponse = new DeliveryVoucherDetailResponse(goodsServices.mapperGoods(
                goodsServices.findGoodByCode(inventoryDeliveryVoucherDetail.getGoods().getCode())), UtillServies.mapperLocationInWarehouse(inventoryDeliveryVoucherDetail.getBinPosition()), inventoryDeliveryVoucherDetail.getQuantity());
        return detailResponse;
    }

}
