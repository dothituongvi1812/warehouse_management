package com.example.warehouse_management.services.impl;


import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.warehouse.BinLocation;
import com.example.warehouse_management.models.warehouse.ColumnLocation;
import com.example.warehouse_management.models.warehouse.ShelfStorage;
import com.example.warehouse_management.payload.request.bin.BinLocationMoveToRequest;
import com.example.warehouse_management.payload.request.bin.BinLocationRequest;
import com.example.warehouse_management.payload.request.goods.GoodsCreatedReceiptVoucherRequest;
import com.example.warehouse_management.payload.request.bin.StatusRequest;
import com.example.warehouse_management.payload.response.BinLocationResponse;
import com.example.warehouse_management.repository.ColumnLocationRepository;
import com.example.warehouse_management.repository.BinLocationRepository;
import com.example.warehouse_management.repository.ReceiptVoucherDetailRepository;
import com.example.warehouse_management.services.GoodsServices;
import com.example.warehouse_management.services.BinLocationServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BinLocationServicesImpl implements BinLocationServices {

    @Autowired
    ColumnLocationRepository columnLocationRepository;
    @Autowired
    BinLocationRepository binLocationRepository;
    @Autowired
    private GoodsServices goodsServices;
    @Autowired
    private ReceiptVoucherDetailRepository receiptVoucherDetailRepository;
    private ModelMapper modelMapper=new ModelMapper();
    @Override
    public List<BinLocationResponse> addRowLocations(BinLocationRequest request) {
        List<BinLocationResponse> binLocationRespons =new ArrayList<>();
        ColumnLocation columnLocation =columnLocationRepository.findByCode(request.getColumnLocationCode());
        if(columnLocation==null){
            throw new NotFoundGlobalException("Không tìm thấy vị trí cột "+request.getColumnLocationCode());
        }
        ShelfStorage shelfStorage =columnLocation.getShelfStorage();
        int numberOfFloor= shelfStorage.getNumberOfFloors();
        int numberOfRow= binLocationRepository.findAll().size();
        String code ="BL000";
        for (int i = 0; i < numberOfFloor; i++) {
            BinLocation bin =new BinLocation();
            double height= shelfStorage.getHeight()/3;
            double volume = height* shelfStorage.getWidth()*columnLocation.getLength();
            bin.setHeight(height);
            bin.setWidth(shelfStorage.getWidth());
            bin.setLength(columnLocation.getLength());
            bin.setVolume(volume);
            bin.setRemainingVolume(volume);
            bin.setStatus(EStatusStorage.EMPTY);
            bin.setName(generateRowLocationName(i+1));
            bin.setCode(code+(numberOfRow+i+1));
            bin.setColumnLocation(columnLocation);
            BinLocation saveBin = binLocationRepository.save(bin);
            BinLocationResponse binLocationResponseMapper =mapperRowLocationResponse(saveBin);
            binLocationRespons.add(binLocationResponseMapper);
        }

        return binLocationRespons;
    }

    @Override
    public Page<BinLocationResponse> getPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<BinLocation> rowLocations= binLocationRepository.findAll(pageable);
        Page<BinLocationResponse> pages = new PageImpl<BinLocationResponse>(rowLocations.getContent()
                .stream().map(this::mapperRowLocationResponse).collect(Collectors.toList()), pageable,
                rowLocations.getTotalElements());

        return pages;
    }

    @Override
    public BinLocationResponse getByCode(String code) {
        return mapperRowLocationResponse(findRowLocationByCode(code));
    }

    @Override
    public BinLocation findRowLocationByCode(String code) {
        BinLocation bin = binLocationRepository.findByCode(code);
        if(bin ==null)
            throw new NotFoundGlobalException("Không tìm thấy vị trí "+code);

        return bin;
    }

    @Override
    public BinLocationResponse mapperRowLocation(BinLocation bin) {
        return mapperRowLocationResponse(bin);
    }

    @Override
    public List<BinLocationResponse> filterStatusByCodeWarehouse(String codeWarehouse, StatusRequest statusRequest) {
        String request = statusRequest.getStatus();
        String status="";
        switch (request){
            case "Trống":
            case "trống":
            case "TRỐNG":
                status = EStatusStorage.EMPTY.name();
                break;
            case "Còn chỗ":
            case "CÒN CHỖ":
            case "còn chỗ":
                status = EStatusStorage.AVAILABLE.name();
                break;
            default: status =EStatusStorage.FULL.name(); ;
        }
        List<BinLocationResponse> responseList = binLocationRepository.filterStatusByWarehouseCode(codeWarehouse,status)
                .stream().map(item->mapperRowLocationResponse(item)).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<BinLocation> findAllRowLocationByGoodsCode(String goodCode) {
        return binLocationRepository.findByGoodsCode(goodCode);
    }

    @Override
    public Page<BinLocationResponse> getPageRowLocationByWarehouseCode(String warehouseCode, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<BinLocation> rowLocations= binLocationRepository.getPageRowLocationByWarehouseCode(warehouseCode,pageable);
        Page<BinLocationResponse> pages= new PageImpl<>(rowLocations.getContent().stream().map(this::mapperRowLocationResponse).collect(Collectors.toList()),
                pageable,rowLocations.getTotalElements());
        return pages;
    }

    @Override
    public Integer getSumCurrentCapacityByGoodsName(String goodsName) {
    return binLocationRepository.getSumCurrentCapacityByGoodsName(goodsName);
    }

    @Override
    public List<BinLocation> findAllByGoodsNameEnoughToExport(String goodsName, int quantity) {
        return binLocationRepository.findByGoodsNameEnoughToExport(goodsName,quantity);
    }

    @Override
    public Map<String, Integer> reportStockPosition(String codeWarehouse) {
       Map<String,Integer> map = new HashMap<>();
        List<Object[]> list = binLocationRepository.reportStockPosition(codeWarehouse);
        for (Object[] ob : list){
            String key = (String)ob[0];
            Integer value = ((BigInteger) ob[1]).intValue();
            map.put(key,value);
        }
        return map;
    }

    @Override
    public List<BinLocationResponse> getAll() {
        List<BinLocationResponse> responseList = binLocationRepository.findAll().stream().
                map(rowLocation ->mapperRowLocationResponse(rowLocation))
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<BinLocationResponse> getAllRowLocationByWarehouseCode(String warehouseCode) {
        List<BinLocationResponse> binLocationRespons = binLocationRepository.getAllRowLocationByWarehouseCode(warehouseCode)
                .stream().map(item->mapperRowLocationResponse(item)).collect(Collectors.toList());
        return binLocationRespons;
    }

    @Override
    public List<BinLocationResponse> getAllUsablePositionForGoods(String warehouseCode, GoodsCreatedReceiptVoucherRequest request) {
        List<BinLocation> binLocationCheck = binLocationRepository.getAllBinStatusEmptyAndAvailable();
        if(CollectionUtils.isEmpty(binLocationCheck))
            throw new ErrorException("Kho đã đầy");
        List<Long> usingBinLocation = binLocationRepository.getAllUsingBinLocation();
        if(CollectionUtils.isEmpty(usingBinLocation))
            usingBinLocation.add(0L);
        Goods goods = goodsServices.findGoodByCode(request.getCodeGoods());
        double volume = goods.getVolume() * request.getQuantity();
        List<BinLocation> binLocations = binLocationRepository.findByGoodsCode(goods.getCode());
        List<BinLocation> usablePositonList =new ArrayList<>();
        if(!CollectionUtils.isEmpty(binLocations)){
            usablePositonList = binLocationRepository.getAllUsablePositionForGoodsExisted(warehouseCode,volume,goods.getCode(),usingBinLocation).stream()
                    .sorted(Comparator.comparing(BinLocation::getCode)).collect(Collectors.toList());
        }
        else{
            usablePositonList = binLocationRepository.getAllUsablePositionForGoodsNotExisted(warehouseCode,usingBinLocation).stream()
                    .sorted(Comparator.comparing(BinLocation::getCode)).collect(Collectors.toList());
        }
        List<BinLocationResponse> responseList = usablePositonList.stream().map(this::mapperRowLocationResponse)
                .sorted(Comparator.comparing(BinLocationResponse::getCodeShelf))
                .sorted(Comparator.comparing(BinLocationResponse::getCodeColumn))
                .sorted(Comparator.comparing(BinLocationResponse::getCodeBin))
                .collect(Collectors.toList());

        return responseList;
    }

    @Override
    public String moveBin(String fromBinLocationCode, BinLocationMoveToRequest binLocationMoveToRequest) {
        BinLocation fromBinLocation = findRowLocationByCode(fromBinLocationCode);
        BinLocation toBinLocation = findRowLocationByCode(binLocationMoveToRequest.getToBinLocationCode());
        if (!ObjectUtils.isEmpty(toBinLocation.getGoods())){
            if(fromBinLocation.getGoods().equals(toBinLocation.getGoods())){
                throw new ErrorException("Vị trí có mã "+ fromBinLocation.getCode()+ " đang chứa sản phẩm"+ fromBinLocation.getGoods().getCode());
            }
        }
        else {
            toBinLocation.setGoods(fromBinLocation.getGoods());
        }
        int quantity=binLocationMoveToRequest.getQuantity();
        double volumeGoods = quantity* toBinLocation.getGoods().getVolume();
        toBinLocation.setCurrentCapacity(toBinLocation.getCurrentCapacity()+ quantity);
        toBinLocation.setRemainingVolume(toBinLocation.getRemainingVolume()-volumeGoods);
        fromBinLocation.setCurrentCapacity(fromBinLocation.getCurrentCapacity()-quantity);
        fromBinLocation.setRemainingVolume(fromBinLocation.getRemainingVolume()+volumeGoods);
        List<BinLocation> binLocationList = Arrays.asList(toBinLocation,fromBinLocation);
        binLocationRepository.saveAll(binLocationList);
        return "Dời thành công từ vị trí" + fromBinLocationCode +" sang vị trí " + toBinLocation.getCode();
    }

    private String generateRowLocationName(int numberRow){
        String name ="";
        switch (numberRow){
            case 1: name= "Tầng 1";
                break;
            case 2: name= "Tầng 2";
                break;
            case 3: name= "Tầng 3";
                break;

        }
        return name;

    }
    private BinLocationResponse mapperRowLocationResponse(BinLocation binLocation){
        String status=null;
        switch (binLocation.getStatus()){
            case FULL:
                status ="Đã đầy";
                break;
            case EMPTY:
                status ="Trống";
                break;
            case AVAILABLE:
                status="Còn chỗ";
                break;
        }
        BinLocationResponse binLocationResponseMapper =modelMapper.map(binLocation, BinLocationResponse.class);
        binLocationResponseMapper.setStatus(status);
        binLocationResponseMapper.setCodeBin(binLocation.getCode());
        binLocationResponseMapper.setNameBin(binLocation.getName());
        binLocationResponseMapper.setCodeColumn(binLocation.getColumnLocation().getCode());
        binLocationResponseMapper.setNameColumn(binLocation.getColumnLocation().getName());
        binLocationResponseMapper.setCodeShelf(binLocation.getColumnLocation().getShelfStorage().getCode());
        binLocationResponseMapper.setNameShelf(binLocation.getColumnLocation().getShelfStorage().getName());
        binLocationResponseMapper.setNameWarehouse(binLocation.getColumnLocation().getShelfStorage().getWarehouse().getName());
        binLocationResponseMapper.setCodeWarehouse(binLocation.getColumnLocation().getShelfStorage().getWarehouse().getCode());
        if(ObjectUtils.isEmpty(binLocation.getGoods()))
            binLocationResponseMapper.setGoods(null);
        else
            binLocationResponseMapper.setGoods(goodsServices.mapperGoods(binLocation.getGoods()));
        return binLocationResponseMapper;
    }
}
