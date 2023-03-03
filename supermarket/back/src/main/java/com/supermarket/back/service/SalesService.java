package com.supermarket.back.service;

import com.supermarket.back.entity.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalesService {
    void saveSales(Sales sales);

    Page<Sales> getSales(String sid, String cid, String barchNumber, Pageable pageable);

    boolean salesExist(String sid, String cid, String batchNumber);

    boolean deleteById(String sid, String cid, String batchNumber);

    void createSales(List<Sales> sales, String tel);

    void createSales(List<Sales> sales);
}
