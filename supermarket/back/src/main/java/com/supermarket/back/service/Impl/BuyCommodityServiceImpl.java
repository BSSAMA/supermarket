package com.supermarket.back.service.Impl;

import com.supermarket.back.entity.*;
import com.supermarket.back.entity.IDClass.BuyCommodityID;
import com.supermarket.back.entity.IDClass.CommodityID;
import com.supermarket.back.repository.BuyCommodityRepository;
import com.supermarket.back.service.BuyCommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BuyCommodityServiceImpl implements BuyCommodityService {
    @Autowired
    BuyCommodityRepository buyCommodityRepository;

    @Autowired
    CommodityServiceImpl commodityService;

    @Autowired
    ProviderServiceImpl providerService;

    @Override
    public void saveBuyCommodity(BuyCommodity buyCommodity) {
        buyCommodityRepository.save(buyCommodity);
    }

    @Override
    public Page<BuyCommodity> getBuyCommodity(String bid, String cid, String batchNumber, Pageable pageable) {
        return buyCommodityRepository.findBuyCommodityByBidLikeAndCidLikeAndBatchNumberLike('%'+bid+'%','%'+cid+'%','%'+batchNumber+'%',pageable);
    }

    @Override
    public boolean buyCommodityExist(String bid, String cid, String batchNumber) {
        BuyCommodity buyCommodity = buyCommodityRepository.findBuyCommodityByBidAndCidAndBatchNumber(bid,cid,batchNumber);
        return buyCommodity != null;
    }

    @Override
    public boolean deleteById(String bid, String cid, String batchNumber) {
        BuyCommodityID buyCommodityID = new BuyCommodityID();
        buyCommodityID.setBid(bid);
        buyCommodityID.setCid(cid);
        buyCommodityID.setBatchNumber(batchNumber);
        buyCommodityRepository.deleteById(buyCommodityID);
        return true;
    }

    @Override
    public void createBuyCommodity(List<BuyCommodity> buyCommodity, Integer pid) {
        Date date = new Date();
        SimpleDateFormat dateString = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = dateString.format(date);
        strDate = 'B' + strDate;
        String finalStrDate = strDate;
        buyCommodity.forEach(e -> {
            //保存sales
            e.setBid(finalStrDate);
            e.setPid(pid);
            buyCommodityRepository.save(e);
            //更新商品数量
            if(commodityService.commodityExist(e.getCid(),e.getBatchNumber())){
                Commodity commodity = commodityService.findCommodityByCidAndBatchNumber(e.getCid(),e.getBatchNumber());
                commodity.setNumber(commodity.getNumber() + e.getNumber());
                commodityService.saveCommodity(commodity);
            }
            else {
                Commodity commodity = commodityService.findFirstByCid(e.getCid());
                Commodity commodity1 = new Commodity();
                commodity1.setCid(commodity.getCid());
                commodity1.setName(commodity.getName());
                commodity1.setBrand(commodity.getBrand());
                commodity1.setExpirationDate(commodity.getExpirationDate());
                commodity1.setBuyingPrice(commodity.getBuyingPrice());
                commodity1.setPrice(commodity.getPrice());
                commodity1.setType(commodity.getType());
                commodity1.setLocation(commodity.getLocation());
                commodity1.setBatchNumber(e.getBatchNumber());
                commodity1.setNumber(e.getNumber());
                commodity1.setDateProduction(e.getDateProduction());
                commodityService.saveCommodity(commodity1);
            }
        });
    }
}
