package com.supermarket.back.repository;

import com.supermarket.back.entity.Commodity;
import com.supermarket.back.entity.IDClass.CommodityID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, CommodityID> {
    Commodity findCommodityByCidAndBatchNumber(String id,String batchNumber);
    Commodity findFirstByCid(String cid);
    Page<Commodity> findCommodityByNameLikeAndTypeLikeOrderByLocation(String name, String type, Pageable pageable);

    @Query(value = "select distinct type from commodity",nativeQuery = true)
    List<String> findType();


    @Query(value = "select distinct batch_number from commodity where cid = ?1",nativeQuery = true)
    List<String> getBatchNumberByCid(String cid);

    @Query(value = "select distinct cid from commodity",nativeQuery = true)
    List<String> findAllCid();
}
