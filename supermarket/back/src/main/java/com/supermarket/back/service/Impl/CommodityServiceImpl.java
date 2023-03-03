package com.supermarket.back.service.Impl;

import com.supermarket.back.entity.Commodity;
import com.supermarket.back.entity.IDClass.CommodityID;
import com.supermarket.back.repository.CommodityRepository;
import com.supermarket.back.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommodityServiceImpl implements CommodityService {
    @Autowired
    CommodityRepository commodityRepository;

    @Override
    public void saveCommodity(Commodity commodity) {
        commodityRepository.save(commodity);
    }

    @Override
    public Page<Commodity> getCommodity(String name, String type, Pageable pageable) {
        return commodityRepository.findCommodityByNameLikeAndTypeLikeOrderByLocation('%'+name+'%','%'+type+'%',pageable);
    }

    @Override
    public boolean commodityExist(String cid,String batchNumber) {
        Commodity commodity = commodityRepository.findCommodityByCidAndBatchNumber(cid,batchNumber);
        return commodity != null;
    }

    @Override
    public List<String> getAllType() {
        return commodityRepository.findType();
    }

    @Override
    public boolean deleteById(String cid, String batchNumber){
        CommodityID commodityID = new CommodityID();
        commodityID.setCid(cid);
        commodityID.setBatchNumber(batchNumber);
        commodityRepository.deleteById(commodityID);
        return true;
    }


    @Override
    public Commodity findCommodityByCidAndBatchNumber(String cid, String batchNumber){
        return commodityRepository.findCommodityByCidAndBatchNumber(cid,batchNumber);
    }
    @Override
    public Commodity findFirstByCid(String cid) {
        return commodityRepository.findFirstByCid(cid);
    }

    @Override
    public List<String> getBatchNumberByCid(String cid) {
        return commodityRepository.getBatchNumberByCid(cid);
    }

    @Override
    public boolean cidExist(String cid) {
        return commodityRepository.findAllCid().contains(cid);
    }
}
