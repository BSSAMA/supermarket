package com.supermarket.back.repository;

import com.supermarket.back.entity.IDClass.SalesID;
import com.supermarket.back.entity.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends JpaRepository<Sales, SalesID> {
    Page<Sales> findSalesBySidLikeAndCidLikeAndBatchNumberLike(String sid, String cid, String batchNumber, Pageable pageable);

    Sales findCommodityBySidAndCidAndBatchNumber(String sid, String cid, String batchNumber);
}
