package com.supermarket.back.controller;

import com.supermarket.back.entity.Sales;
import com.supermarket.back.entity.resp.RestBean;
import com.supermarket.back.service.Impl.SalesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    @Autowired
    SalesServiceImpl salesService;

    @GetMapping("/getsales/{page}")
    public RestBean<Page<Sales>> getSales(Sales sales, @PathVariable int page){
        Pageable pageable = PageRequest.of(page-1,6);
        Page<Sales> sales2 = salesService.getSales(sales.getSid(),sales.getCid(),sales.getBatchNumber(),pageable);
        if (sales2.isEmpty())
            return new RestBean<>(403,"查询失败，未查询到数据！");
        return new RestBean<>(200,"查找成功！",sales2);
    }

    @PostMapping("/savesales")
    public RestBean<Void> saveSales(@RequestBody Sales sales){
        if (salesService.salesExist(sales.getSid(),sales.getCid(),sales.getBatchNumber())){
            salesService.saveSales(sales);
            return new RestBean<>(200,"销售单更新成功！");
        }
        salesService.saveSales(sales);
        return new RestBean<>(200,"销售单添加成功！");
    }

    @GetMapping("/deletesales")
    public RestBean<Void> deleteSales(Sales sales){
        if (salesService.deleteById(sales.getSid(),sales.getCid(),sales.getBatchNumber()))
            return new RestBean<>(200,"删除成功！");
        return new RestBean<>(403,"删除失败！");
    }

    @PostMapping("/createsales/{tel}")
    public RestBean<Void> createSales(@RequestBody List<Sales> sales,@PathVariable("tel") String tel){
        if (tel.equals("null"))
            salesService.createSales(sales);
        else
            salesService.createSales(sales,tel);
        return new RestBean<>(200,"结算完成！");
    }
}
