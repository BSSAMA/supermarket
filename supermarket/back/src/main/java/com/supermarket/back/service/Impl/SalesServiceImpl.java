package com.supermarket.back.service.Impl;

import com.supermarket.back.entity.Account;
import com.supermarket.back.entity.Commodity;
import com.supermarket.back.entity.IDClass.SalesID;
import com.supermarket.back.entity.Sales;
import com.supermarket.back.repository.SalesRepository;
import com.supermarket.back.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {
    @Autowired
    SalesRepository salesRepository;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    CommodityServiceImpl commodityService;

    @Override
    public void saveSales(Sales sales) {
        salesRepository.save(sales);
    }

    @Override
    public Page<Sales> getSales(String sid, String cid, String barchNumber, Pageable pageable) {
        return salesRepository.findSalesBySidLikeAndCidLikeAndBatchNumberLike('%'+sid+'%','%'+cid+'%','%'+barchNumber+'%',pageable);
    }

    @Override
    public boolean salesExist(String sid, String cid, String batchNumber) {
        Sales sales = salesRepository.findCommodityBySidAndCidAndBatchNumber(sid,cid,batchNumber);
        return sales != null;
    }

    @Override
    public boolean deleteById(String sid, String cid, String batchNumber) {
        SalesID salesID = new SalesID();
        salesID.setSid(sid);
        salesID.setCid(cid);
        salesID.setBatchNumber(batchNumber);
        salesRepository.deleteById(salesID);
        return true;
    }

    @Override
    public void createSales(List<Sales> sales, String tel) {
        Account account = accountService.findAccountByTel(tel);
        Date date = new Date();
        SimpleDateFormat dateString = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = dateString.format(date);
        strDate = 'S' + strDate;
        String finalStrDate = strDate;
        sales.forEach(e -> {
            //保存sales
            e.setSid(finalStrDate);
            e.setTel(tel);
            salesRepository.save(e);
            //更新用户积分
            account.setIntegral(account.getIntegral() + e.getSumPrice());
            //更新商品数量
            Commodity commodity = commodityService.findCommodityByCidAndBatchNumber(e.getCid(),e.getBatchNumber());
            if (commodity.getNumber() - e.getNumber() == 0){
                commodityService.deleteById(commodity.getCid(),commodity.getBatchNumber());
            }
            else {
                commodity.setNumber(commodity.getNumber() - e.getNumber());
                commodityService.saveCommodity(commodity);
            }
        });
        accountService.saveAccount(account);
    }

    @Override
    public void createSales(List<Sales> sales) {
        Date date = new Date();
        SimpleDateFormat dateString = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = dateString.format(date);
        strDate = 'S' + strDate;
        String finalStrDate = strDate;
        sales.forEach(e -> {
            //保存sales
            e.setSid(finalStrDate);
            salesRepository.save(e);
            //更新商品数量
            Commodity commodity = commodityService.findCommodityByCidAndBatchNumber(e.getCid(),e.getBatchNumber());
            if (commodity.getNumber() - e.getNumber() == 0)
                commodityService.deleteById(commodity.getCid(),commodity.getBatchNumber());
            else {
                commodity.setNumber(commodity.getNumber() - e.getNumber());
                commodityService.saveCommodity(commodity);
            }
        });
    }
}
