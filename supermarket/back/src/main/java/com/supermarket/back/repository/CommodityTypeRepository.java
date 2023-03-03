package com.supermarket.back.repository;

import com.supermarket.back.entity.CommodityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityTypeRepository extends JpaRepository<CommodityType,String> {
}
