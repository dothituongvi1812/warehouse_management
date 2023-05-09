package com.example.warehouse_management.services.impl;


import com.example.warehouse_management.exception.ErrorException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.goods.Goods;
import com.example.warehouse_management.models.type.EStatusStorage;
import com.example.warehouse_management.models.warehouse.BinPosition;
import com.example.warehouse_management.models.warehouse.ColumnPosition;
import com.example.warehouse_management.models.warehouse.ShelfStorage;
import com.example.warehouse_management.payload.request.bin.BinLocationMoveToRequest;
import com.example.warehouse_management.payload.request.bin.BinLocationRequest;
import com.example.warehouse_management.payload.request.goods.GoodsCheckedRequest;
import com.example.warehouse_management.payload.request.goods.GoodsCreatedReceiptVoucherRequest;
import com.example.warehouse_management.payload.request.bin.StatusRequest;
import com.example.warehouse_management.payload.response.BinPositionResponse;
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
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<BinPositionResponse> addRowLocations(BinLocationRequest request) {
        List<BinPositionResponse> binLocationResponse = new ArrayList<>();
        ColumnPosition columnPosition = columnLocationRepository.findByCode(request.getColumnLocationCode());
        if (columnPosition == null) {
            throw new NotFoundGlobalException("Không tìm thấy vị trí cột " + request.getColumnLocationCode());
        }
        ShelfStorage shelfStorage = columnPosition.getShelfStorage();
        int numberOfFloor = shelfStorage.getNumberOfFloors();
        int numberOfRow = binLocationRepository.findAll().size();
        String code = "BP000";
        for (int i = 0; i < numberOfFloor; i++) {
            BinPosition bin = new BinPosition();
            double height = shelfStorage.getHeight() / 3;
            double volume = height * shelfStorage.getWidth() * columnPosition.getLength();
            bin.setHeight(height);
            bin.setWidth(shelfStorage.getWidth());
            bin.setLength(columnPosition.getLength());
            bin.setVolume(volume);
            bin.setRemainingVolume(volume);
            bin.setStatus(EStatusStorage.EMPTY);
            bin.setName(generateRowLocationName(i + 1));
            bin.setCode(code + (numberOfRow + i + 1));
            bin.setColumnPosition(columnPosition);
            BinPosition saveBin = binLocationRepository.save(bin);
            BinPositionResponse binPositionResponseMapper = mapperRowLocationResponse(saveBin);
            binLocationResponse.add(binPositionResponseMapper);
        }

        return binLocationResponse;
    }

    @Override
    public Page<BinPositionResponse> getPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BinPosition> rowLocations = binLocationRepository.findAll(pageable);
        Page<BinPositionResponse> pages = new PageImpl<BinPositionResponse>(rowLocations.getContent()
                .stream().map(this::mapperRowLocationResponse).collect(Collectors.toList()), pageable,
                rowLocations.getTotalElements());

        return pages;
    }

    @Override
    public BinPositionResponse getByCode(String code) {
        return mapperRowLocationResponse(findRowLocationByCode(code));
    }

    @Override
    public BinPosition findRowLocationByCode(String code) {
        BinPosition bin = binLocationRepository.findByCode(code);
        if (bin == null)
            throw new NotFoundGlobalException("Không tìm thấy vị trí " + code);

        return bin;
    }

    @Override
    public BinPositionResponse mapperRowLocation(BinPosition bin) {
        return mapperRowLocationResponse(bin);
    }

    @Override
    public List<BinPositionResponse> filterStatusByCodeWarehouse(String codeWarehouse, StatusRequest statusRequest) {
        String request = statusRequest.getStatus();
        String status = "";
        switch (request) {
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
            default:
                status = EStatusStorage.FULL.name();
                ;
        }
        List<BinPositionResponse> responseList = binLocationRepository.filterStatusByWarehouseCode(codeWarehouse, status)
                .stream().map(item -> mapperRowLocationResponse(item)).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<BinPosition> findAllRowLocationByGoodsCode(String goodCode) {
        return binLocationRepository.findByGoodsCode(goodCode);
    }

    @Override
    public Page<BinPositionResponse> getPageRowLocationByWarehouseCode(String warehouseCode, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BinPosition> rowLocations = binLocationRepository.getPageRowLocationByWarehouseCode(warehouseCode, pageable);
        Page<BinPositionResponse> pages = new PageImpl<>(rowLocations.getContent().stream().map(this::mapperRowLocationResponse).collect(Collectors.toList()),
                pageable, rowLocations.getTotalElements());
        return pages;
    }

    @Override
    public Integer getSumCurrentCapacityByGoodsName(String goodsName) {
        return binLocationRepository.getSumCurrentCapacityByGoodsName(goodsName);
    }

    @Override
    public List<BinPosition> findAllByGoodsNameEnoughToExport(String goodsName, int quantity) {
        return binLocationRepository.findByGoodsNameEnoughToExport(goodsName, quantity);
    }

    @Override
    public Map<String, Integer> reportStockPosition(String codeWarehouse) {
        Map<String, Integer> map = new HashMap<>();
        List<Object[]> list = binLocationRepository.reportStockPosition(codeWarehouse);
        for (Object[] ob : list) {
            String key = (String) ob[0];
            Integer value = ((BigInteger) ob[1]).intValue();
            map.put(key, value);
        }
        return map;
    }

    @Override
    public List<BinPositionResponse> getAll() {
        List<BinPositionResponse> responseList = binLocationRepository.findAll().stream().
                map(rowLocation -> mapperRowLocationResponse(rowLocation))
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<BinPositionResponse> getAllRowLocationByWarehouseCode(String warehouseCode) {
        List<BinPositionResponse> binLocationRespons = binLocationRepository.getAllRowLocationByWarehouseCode(warehouseCode)
                .stream().map(item -> mapperRowLocationResponse(item)).collect(Collectors.toList());
        return binLocationRespons;
    }

    @Override
    public List<BinPositionResponse> getAllUsablePositionForGoods(String warehouseCode, GoodsCreatedReceiptVoucherRequest request) {
        List<BinPosition> binPositionCheck = binLocationRepository.getAllBinStatusEmptyAndAvailable();
        if (CollectionUtils.isEmpty(binPositionCheck))
            throw new ErrorException("Kho đã đầy");
        List<Long> usingBinLocation = binLocationRepository.getAllUsingBinLocation();
        if (CollectionUtils.isEmpty(usingBinLocation))
            usingBinLocation.add(0L);
        Goods goods = goodsServices.findGoodByCode(request.getCodeGoods());
        double volume = goods.getVolume() * request.getQuantity();
        List<BinPosition> binPositions = binLocationRepository.findByGoodsCode(goods.getCode());
        List<BinPosition> usablePositonList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(binPositions)) {
            usablePositonList = binLocationRepository.getAllUsablePositionForGoodsExisted(warehouseCode, volume, goods.getCode(), usingBinLocation).stream()
                    .sorted(Comparator.comparing(BinPosition::getCode)).collect(Collectors.toList());
        } else {
            usablePositonList = binLocationRepository.getAllUsablePositionForGoodsNotExisted(warehouseCode, usingBinLocation).stream()
                    .sorted(Comparator.comparing(BinPosition::getCode)).collect(Collectors.toList());
        }
        List<BinPositionResponse> responseList = usablePositonList.stream().map(this::mapperRowLocationResponse)
                .sorted(Comparator.comparing(BinPositionResponse::getNameShelf)
                        .thenComparing(BinPositionResponse::getNameColumn))
                .limit(10)
                .collect(Collectors.toList());

        return responseList;
    }

    @Override
    public String moveBin(String fromBinLocationCode, BinLocationMoveToRequest binLocationMoveToRequest) {
        BinPosition fromBinPosition = findRowLocationByCode(fromBinLocationCode);
        BinPosition toBinPosition = findRowLocationByCode(binLocationMoveToRequest.getToBinLocationCode());
        if (!ObjectUtils.isEmpty(toBinPosition.getGoods())) {
            if (fromBinPosition.getGoods().equals(toBinPosition.getGoods())) {
                throw new ErrorException("Vị trí có mã " + fromBinPosition.getCode() + " đang chứa sản phẩm" + fromBinPosition.getGoods().getCode());
            }
        } else {
            toBinPosition.setGoods(fromBinPosition.getGoods());
        }
        int quantity = binLocationMoveToRequest.getQuantity();
        double volumeGoods = quantity * toBinPosition.getGoods().getVolume();
        toBinPosition.setCurrentCapacity(toBinPosition.getCurrentCapacity() + quantity);
        toBinPosition.setRemainingVolume(toBinPosition.getRemainingVolume() - volumeGoods);
        fromBinPosition.setCurrentCapacity(fromBinPosition.getCurrentCapacity() - quantity);
        fromBinPosition.setRemainingVolume(fromBinPosition.getRemainingVolume() + volumeGoods);
        List<BinPosition> binPositionList = Arrays.asList(toBinPosition, fromBinPosition);
        binLocationRepository.saveAll(binPositionList);
        return "Dời thành công từ vị trí" + fromBinLocationCode + " sang vị trí " + toBinPosition.getCode();
    }

    @Override
    public List<BinPositionResponse> search(String keyword, String codeWarehouse) {
        List<BinPositionResponse> binPositionResponseList = binLocationRepository
                .search(keyword, codeWarehouse).stream().map(this::mapperRowLocationResponse)
                .sorted(Comparator.comparing(BinPositionResponse::getCodeBin))
                .collect(Collectors.toList());

        return binPositionResponseList;
    }

    @Override
    public List<BinPositionResponse> filterByColumnLocationCode(String columnCode, String codeWarehouse) {
        List<BinPositionResponse> binPositionResponseList = binLocationRepository.filterByColumnCode(columnCode, codeWarehouse)
                .stream().map(this::mapperRowLocationResponse)
                .sorted(Comparator.comparing(BinPositionResponse::getCodeColumn))
                .collect(Collectors.toList());
        return binPositionResponseList;
    }

    @Override
    public boolean checkRemainingVolumeForGoodsByBinCode(String binCode,GoodsCheckedRequest goodsCheckedRequest) {
        BinPosition binPosition = findRowLocationByCode(binCode);
        if (binPosition.getStatus().equals(EStatusStorage.FULL))
            throw new ErrorException(String.format("Vị trí có mã %s đã đầy",binCode));
        Goods goods = goodsServices.findGoodByCode(goodsCheckedRequest.getGoodsCode());
        if (!binPosition.getGoods().equals(goods))
            throw new ErrorException(String.format("Vị trí có mã %s đang chứa hàng hoá có tên: %s ",binCode,binPosition.getGoods().getName()));
        double volumeGoods = goods.getVolume() * goodsCheckedRequest.getQuantity();
        if (binPosition.getRemainingVolume() < volumeGoods)
            throw new ErrorException(String.format("Vị trí có mã %s không chứa đủ. Thể tích còn lại của vị trí là: %0.2f ",binCode,binPosition.getRemainingVolume()));
        return true;
    }

    private String generateRowLocationName(int numberRow) {
        String name = "";
        switch (numberRow) {
            case 1:
                name = "Tầng 1";
                break;
            case 2:
                name = "Tầng 2";
                break;
            case 3:
                name = "Tầng 3";
                break;

        }
        return name;

    }

    private BinPositionResponse mapperRowLocationResponse(BinPosition binPosition) {
        String status = null;
        switch (binPosition.getStatus()) {
            case FULL:
                status = "Đã đầy";
                break;
            case EMPTY:
                status = "Trống";
                break;
            case AVAILABLE:
                status = "Còn chỗ";
                break;
        }
        BinPositionResponse binPositionResponseMapper = modelMapper.map(binPosition, BinPositionResponse.class);
        binPositionResponseMapper.setStatus(status);
        binPositionResponseMapper.setCodeBin(binPosition.getCode());
        binPositionResponseMapper.setNameBin(binPosition.getName());
        binPositionResponseMapper.setCodeColumn(binPosition.getColumnPosition().getCode());
        binPositionResponseMapper.setNameColumn(binPosition.getColumnPosition().getName());
        binPositionResponseMapper.setCodeShelf(binPosition.getColumnPosition().getShelfStorage().getCode());
        binPositionResponseMapper.setNameShelf(binPosition.getColumnPosition().getShelfStorage().getName());
        binPositionResponseMapper.setNameWarehouse(binPosition.getColumnPosition().getShelfStorage().getWarehouse().getName());
        binPositionResponseMapper.setCodeWarehouse(binPosition.getColumnPosition().getShelfStorage().getWarehouse().getCode());
        if (ObjectUtils.isEmpty(binPosition.getGoods()))
            binPositionResponseMapper.setGoods(null);
        else
            binPositionResponseMapper.setGoods(goodsServices.mapperGoods(binPosition.getGoods()));
        return binPositionResponseMapper;
    }
}
