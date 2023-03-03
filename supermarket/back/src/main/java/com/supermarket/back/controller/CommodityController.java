package com.supermarket.back.controller;

import com.supermarket.back.entity.BuyCommodity;
import com.supermarket.back.entity.Commodity;
import com.supermarket.back.entity.Sales;
import com.supermarket.back.entity.resp.RestBean;
import com.supermarket.back.service.Impl.CommodityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/commodity")
public class CommodityController {

    @Autowired
    private CommodityServiceImpl commodityService;

    private Date TimeConversion(String date){
        if (date.equals("00000000"))
            return null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        java.util.Date d = null;
        try {
            d =sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(d.getTime());
    }

    private boolean IsAdmin(){
        SecurityContext context = SecurityContextHolder.getContext();
        Collection<? extends GrantedAuthority> authorities = context.getAuthentication().getAuthorities();
        for (GrantedAuthority e : authorities) {
            if (e.toString().equals("ROLE_admin"))
                return true;
        }
        return false;
    }


    @GetMapping("/getcommodity/{page}")
    public RestBean<Object> getCommodity(Commodity commodity, @PathVariable int page){
        Pageable pageable = PageRequest.of(page-1,6);
        Page<Commodity> commodities = commodityService.getCommodity(commodity.getName(),commodity.getType(),pageable);
        if (!IsAdmin())
            commodities.forEach((e) -> {
                e.setBuyingPrice(0.0f);
            });
        if (commodities.isEmpty())
            return new RestBean<>(403,"查询失败，未查询到数据！");
        return new RestBean<>(200,"查找成功！",commodities);
    }

    @PostMapping("/savecommodity")
    public RestBean<Void> saveCommodity(@RequestBody Commodity commodity){
        if (commodityService.commodityExist(commodity.getCid(),commodity.getBatchNumber())){
            commodityService.saveCommodity(commodity);
            return new RestBean<>(200,"商品更新成功！");
        }
        commodity.setNumber(0);
        commodityService.saveCommodity(commodity);
        return new RestBean<>(200,"商品创建成功！");
    }


    @GetMapping("/getcommoditytype")
    public RestBean<List<String>> getCommodityType(){
        return new RestBean<>(200,"查找成功！",commodityService.getAllType());
    }

    @GetMapping("/deletecommodity")
    public RestBean<Void> deleteCommodity(Commodity commodity){
        if (commodityService.deleteById(commodity.getCid(),commodity.getBatchNumber()))
            return new RestBean<>(200,"删除成功！");
        return new RestBean<>(403,"删除失败！");
    }

    @GetMapping("/getcommoditybycidandbatchnumber")
    public RestBean<Sales> getCommodityByCidAndBatchNumber(Commodity commodity){
        Sales sales = new Sales();
        sales.setBatchNumber(commodity.getBatchNumber());
        sales.setNumber(commodity.getNumber());
        sales.setCid(commodity.getCid());
        commodity = commodityService.findCommodityByCidAndBatchNumber(commodity.getCid(),commodity.getBatchNumber());
        sales.setPrice(commodity.getPrice());
        sales.setSumPrice(sales.getPrice()*sales.getNumber());
        return new RestBean<>(200,"查找成功！",sales);
    }

    @GetMapping("/getcommoditybycidandbatchnumber2")
    public RestBean<BuyCommodity> getCommodityByCidAndBatchNumber2(Commodity commodity){
        Commodity commodity2 = commodityService.findFirstByCid(commodity.getCid());
        if (commodity2 == null)
            return new RestBean<>(403,"此商品编号第一次进货！");
        BuyCommodity buyCommodity = new BuyCommodity();
        buyCommodity.setDateProduction(TimeConversion(commodity.getBatchNumber()));
        buyCommodity.setBatchNumber(commodity.getBatchNumber());
        buyCommodity.setNumber(commodity.getNumber());
        buyCommodity.setCid(commodity.getCid());

        buyCommodity.setExpirationDate(commodity2.getExpirationDate());
        buyCommodity.setBuyingPrice(commodity2.getBuyingPrice());
        buyCommodity.setSumPrice(Float.parseFloat(String.format("%.2f",buyCommodity.getBuyingPrice() * buyCommodity.getNumber())));
        return new RestBean<>(200,"查找成功！",buyCommodity);
    }

    @GetMapping("/getbatchnumberbycid/{cid}")
    public RestBean<List<String>> getBatchNumberByCid(@PathVariable("cid") String cid){
        List<String> strings = commodityService.getBatchNumberByCid(cid);
        return new RestBean<>(200,"查找成功！",strings);
    }

    @GetMapping("/cidexist/{cid}")
    public RestBean<Void> cidExist(@PathVariable("cid") String cid){
        if (commodityService.cidExist(cid))
            return new RestBean<>(200,"此商品编码存在！");
        return new RestBean<>(403,"此商品编码不存在！");
    }

    @GetMapping("/getcommoditynumber/{cid}/{batchnumber}")
    public RestBean<Integer> getCommodityNumber(@PathVariable("cid") String cid,@PathVariable("batchnumber") String batchNumber){
        Commodity commodity = commodityService.findCommodityByCidAndBatchNumber(cid,batchNumber);
        return new RestBean<>(200,"查找成功！",commodity.getNumber());
    }
}
