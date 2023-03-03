package com.supermarket.back.service;

import com.supermarket.back.entity.BuyCommodity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BuyCommodityService {
    void saveBuyCommodity(BuyCommodity buyCommodity);

    Page<BuyCommodity> getBuyCommodity(String bid, String cid, String barchNumber, Pageable pageable);

    boolean buyCommodityExist(String bid, String cid, String batchNumber);

    boolean deleteById(String bid, String cid, String batchNumber);

    void createBuyCommodity(List<BuyCommodity> buyCommodity, Integer pid);
}
