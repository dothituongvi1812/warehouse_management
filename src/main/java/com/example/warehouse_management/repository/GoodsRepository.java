package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.goods.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GoodsRepository extends CrudRepository<Goods,Long> {
    Goods findTopByOrderByIdDesc();
    Page<Goods> findAll(Pageable pageable);
    Goods findByCode(String code);
    @Query("SELECT g FROM Goods g WHERE g.code LIKE %:keyword% or g.name LIKE %:keyword%")
    List<Goods> findByCodeAndName(@Param("keyword") String keyword);
    @Query("SELECT g FROM Goods g WHERE g.name LIKE %:name%")
    Goods findByName(String name);
    @Query(nativeQuery = true,value = "select * from goods g \n" +
            "join category c on g.category_id = c.id \n" +
            "where c.code =:categoryCode")
    List<Goods> findAllByCategory(String categoryCode);
    List<Goods> findAll();

    @Query(nativeQuery = true,value = "select sum(bl.current_capacity) from bin_positions bl \n" +
            "join goods g on bl.goods_id = g.id \n" +
            "where g.code =:goodsCode\n" +
            "group by bl.goods_id ")
    Integer getCurrentQuantityOfGoodsInWarehouse(String goodsCode);
    @Query(nativeQuery = true,value = "select g.\"name\" ,sum(bl.current_capacity) as quantity  from bin_positions bl\n" +
            "join goods g ON bl.goods_id = g.id \n" +
            "group by g.\"name\" ")
    List<Object[]> countCurrentQuantityOfGoodsInWarehouse();
    @Query(nativeQuery = true,value = "select g.\"name\" , sum(bp.current_capacity) from goods g      \n" +
            "join bin_positions bp on g.id = bp.goods_id \n" +
            "join column_positions cp on bp.column_position_id  =cp.id \n" +
            "join shelf_storages ss on cp.shelf_storage_id = ss.id \n" +
            "join warehouse w on ss.warehouse_id = w.id \n" +
            "where w.code =:warehouseCode \n" +
            "group by g.\"name\"")
    List<Object[]> countCurrentQuantityOfGoodsByWarehouseCode(String warehouseCode);

    @Query(nativeQuery = true,value ="select g.name ,sum(irvd.quantity) from goods g\n" +
            "join inventory_receipt_voucher_details irvd on g.id = irvd.goods_id\n" +
            "join inventory_receipt_vouchers irv on irvd.inventory_receipt_voucher_id = irv.id\n" +
            "where irv.status ='IMPORTED' and imported_date BETWEEN :from AND :to\n" +
            "group by g.\"name\"" )
    List<Object[]>reportImportedQuantityGoodsByDate(Timestamp from, Timestamp to);
    @Query(nativeQuery = true,value ="select g.name ,sum(idvd.quantity) from goods g\n" +
            "join inventory_delivery_voucher_details idvd  on g.id = idvd.goods_id \n" +
            "join inventory_delivery_vouchers idv on idvd.delivery_voucher_id = idv.id \n" +
            "where idv.status ='EXPORTED' and idv.exported_date BETWEEN :from AND :to\n" +
            "group by g.\"name\"" )
    List<Object[]>reportExportedQuantityGoodsByDate(Timestamp from, Timestamp to);
    @Query(nativeQuery = true,value = "select g.\"name\",sum(irvd.quantity)  from inventory_receipt_vouchers irv \n" +
            "join inventory_receipt_voucher_details irvd on irv.id = irvd.inventory_receipt_voucher_id \n" +
            "join goods g on g.id = irvd.goods_id \n" +
            "where irv.status = 'IMPORTED' and irv.imported_date between :fromDate  and :toDate\n" +
            "group by g.\"name\" ")
    List<Object[]>reportSumQuantityImportedByPeriod(Timestamp fromDate, Timestamp toDate);
    @Query(nativeQuery = true,value = "select g.\"name\",sum(idvd.quantity)  from inventory_delivery_vouchers idv \n" +
            "join inventory_delivery_voucher_details idvd on idv.id = idvd.delivery_voucher_id \n" +
            "join goods g on g.id = idvd.goods_id \n" +
            "where idv.status = 'EXPORTED' and idv.exported_date  between :fromDate  and :toDate\n" +
            "group by g.\"name\"")
    List<Object[]>reportSumQuantityExportedByPeriod(Timestamp fromDate, Timestamp toDate);
    @Query(nativeQuery = true,value = "select g.\"name\",g.code,c.\"name\" as categoryName ,sum(irvd.quantity) as total  from inventory_receipt_vouchers irv\n" +
            "join inventory_receipt_voucher_details irvd on irv.id = irvd.inventory_receipt_voucher_id \n" +
            "join goods g on g.id = irvd.goods_id\n" +
            "join category c on c.id = g.category_id\n" +
            "where irv.status ='IMPORTED' and EXTRACT(MONTH FROM irv.imported_date) =:month\n" +
            "group by  g.\"name\" ,g.code,c.\"name\"\n" +
            "order by total desc\n" +
            "limit 5")
    List<Object[]>statisticOfTheMostImportedProducts(int month);
    @Query(nativeQuery = true,value = "select g.\"name\",g.code ,c.\"name\" as categoryName ,sum(idvd.quantity) as total  from inventory_delivery_vouchers idv \n" +
            "join inventory_delivery_voucher_details idvd on idv.id = idvd.delivery_voucher_id \n" +
            "join goods g on g.id = idvd.goods_id \n" +
            "join category c on c.id = g.category_id\n" +
            "where idv.status ='EXPORTED' and EXTRACT(MONTH FROM idv.exported_date) =:month\n" +
            "group by g.\"name\" ,g.code,c.\"name\"\n" +
            "order by total desc\n" +
            "limit 5")
    List<Object[]>statisticOfTheMostExportedProducts(int month);

    @Query(nativeQuery = true,value = "select g.\"name\" ,sum(idvd.quantity) as total  from inventory_delivery_vouchers idv \n" +
            "            join inventory_delivery_voucher_details idvd on idv.id = idvd.delivery_voucher_id \n" +
            "            join goods g on g.id = idvd.goods_id \n" +
            "            where idv.status ='EXPORTED' and EXTRACT(MONTH FROM idv.exported_date) =:month\n" +
            "            group by g.\"name\" ")
    List<Object[]>statisticExportedProducts(int month);

    @Query(nativeQuery = true,value = " select g.\"name\" ,sum(irvd.quantity) as total  from inventory_receipt_vouchers irv \n" +
            "            join inventory_receipt_voucher_details irvd on irv.id = irvd.inventory_receipt_voucher_id \n" +
            "            join goods g on g.id = irvd.goods_id\n" +
            "            where irv.status ='IMPORTED' and EXTRACT(MONTH FROM irv.imported_date) =:month\n" +
            "            group by g.\"name\" ")
    List<Object[]>statisticImportedProducts(int month);

    @Query(nativeQuery = true,value = "select g.\"name\",g.code,c.\"name\" as categoryName ,sum(irvd.quantity) as total  from inventory_receipt_vouchers irv\n" +
            "join inventory_receipt_voucher_details irvd on irv.id = irvd.inventory_receipt_voucher_id \n" +
            "join goods g on g.id = irvd.goods_id\n" +
            "join category c on c.id = g.category_id\n" +
            "where irv.status ='IMPORTED' and EXTRACT(MONTH FROM irv.imported_date) =:month\n" +
            "group by  g.\"name\" ,g.code,c.\"name\"\n" +
            "order by total desc\n" +
            "limit 1")
    List<Object[]> statisticOfTheTop1ImportedProducts(int month);
    @Query(nativeQuery = true,value = "select g.\"name\",g.code ,c.\"name\" as categoryName ,sum(idvd.quantity) as total  from inventory_delivery_vouchers idv \n" +
            "join inventory_delivery_voucher_details idvd on idv.id = idvd.delivery_voucher_id \n" +
            "join goods g on g.id = idvd.goods_id \n" +
            "join category c on c.id = g.category_id\n" +
            "where idv.status ='EXPORTED' and EXTRACT(MONTH FROM idv.exported_date) =:month\n" +
            "group by g.\"name\" ,g.code,c.\"name\"\n" +
            "order by total desc\n" +
            "limit 1")
    List<Object[]> statisticOfTheTop1ExportedProducts(int month);

}
