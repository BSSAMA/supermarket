package com.supermarket.back.service;

import com.supermarket.back.entity.Commodity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommodityService {
    void saveCommodity(Commodity commodity);
    Page<Commodity> getCommodity(String name, String type, Pageable pageable);

    boolean commodityExist(String cid, String batchNumber);

    List<String> getAllType();

    boolean deleteById(String cid,String batchNumber);

    Commodity findCommodityByCidAndBatchNumber(String cid, String batchNumber);

    Commodity findFirstByCid(String cid);

    List<String> getBatchNumberByCid(String cid);

    boolean cidExist(String cid);
}
