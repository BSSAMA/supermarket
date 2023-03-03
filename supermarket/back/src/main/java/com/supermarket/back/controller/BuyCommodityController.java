package com.supermarket.back.controller;

import com.supermarket.back.entity.BuyCommodity;
import com.supermarket.back.entity.resp.RestBean;
import com.supermarket.back.service.Impl.BuyCommodityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buycommodity")
public class BuyCommodityController {

    @Autowired
    private BuyCommodityServiceImpl buyCommodityService;

    @GetMapping("/getbuycommodity/{page}")
    public RestBean<Page<BuyCommodity>> getBuyCommodity(BuyCommodity buyCommodity, @PathVariable int page){
        Pageable pageable = PageRequest.of(page-1,6);
        Page<BuyCommodity> buyCommodity2 = buyCommodityService.getBuyCommodity(buyCommodity.getBid(),buyCommodity.getCid(),buyCommodity.getBatchNumber(),pageable);
        if (buyCommodity2.isEmpty())
            return new RestBean<>(403,"查询失败，未查询到数据！");
        return new RestBean<>(200,"查找成功！",buyCommodity2);
    }

    @PostMapping("/savebuycommodity")
    public RestBean<Void> saveBuyCommodity(@RequestBody BuyCommodity buyCommodity){
        if (buyCommodityService.buyCommodityExist(buyCommodity.getBid(),buyCommodity.getCid(),buyCommodity.getBatchNumber())){
            buyCommodityService.saveBuyCommodity(buyCommodity);
            return new RestBean<>(200,"进货单更新成功！");
        }
        buyCommodityService.saveBuyCommodity(buyCommodity);
        return new RestBean<>(200,"进货单添加成功！");
    }

    @GetMapping("/deletebuycommodity")
    public RestBean<Void> deleteBuyCommodity(BuyCommodity buyCommodity){
        if (buyCommodityService.deleteById(buyCommodity.getBid(),buyCommodity.getCid(),buyCommodity.getBatchNumber()))
            return new RestBean<>(200,"删除成功！");
        return new RestBean<>(403,"删除失败！");
    }

    @PostMapping("/createbuycommodity/{pid}")
    public RestBean<Void> createSales(@RequestBody List<BuyCommodity> buyCommodity, @PathVariable("pid") Integer pid){
        buyCommodityService.createBuyCommodity(buyCommodity,pid);
        return new RestBean<>(200,"结算完成！");
    }
}
