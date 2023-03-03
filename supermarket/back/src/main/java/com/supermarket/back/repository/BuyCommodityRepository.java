package com.supermarket.back.repository;

import com.supermarket.back.entity.BuyCommodity;
import com.supermarket.back.entity.IDClass.BuyCommodityID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyCommodityRepository extends JpaRepository<BuyCommodity, BuyCommodityID> {
    BuyCommodity findBuyCommodityByBidAndCidAndBatchNumber(String bid,String cid, String batchNumber);
    Page<BuyCommodity> findBuyCommodityByBidLikeAndCidLikeAndBatchNumberLike(String bid, String cid,String batchNumber, Pageable pageable);
}
