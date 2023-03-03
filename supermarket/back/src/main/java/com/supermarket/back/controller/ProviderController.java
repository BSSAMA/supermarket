package com.supermarket.back.controller;

import com.supermarket.back.entity.Provider;
import com.supermarket.back.entity.resp.RestBean;
import com.supermarket.back.service.Impl.ProviderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/provider")
public class ProviderController {
    @Autowired
    private ProviderServiceImpl providerService;

    @GetMapping("/getprovider/{page}")
    public RestBean<Page<Provider>> getProvider(Provider provider, @PathVariable int page){
        Pageable pageable = PageRequest.of(page-1,6);
        Page<Provider> providers = providerService.getProvider(provider.getName(),provider.getAddress(),pageable);
        if (providers.isEmpty())
            return new RestBean<>(403,"查询失败，未查询到数据！");
        return new RestBean<>(200,"查找成功！",providers);
    }

    @PostMapping("/saveprovider")
    public RestBean<Void> saveProvider(@RequestBody Provider provider){
        if (providerService.providerExist(provider.getPid())){
            providerService.saveProvider(provider);
            return new RestBean<>(200,"供应商更新成功！");
        }
        providerService.saveProvider(provider);
        return new RestBean<>(200,"供应商添加成功！");
    }

    @GetMapping("/deleteprovider")
    public RestBean<Void> deleteProvider(Provider provider){
        if (providerService.deleteById(provider.getPid()))
            return new RestBean<>(200,"删除成功！");
        return new RestBean<>(403,"删除失败！");
    }

    @GetMapping("/pidexist/{pid}")
    public RestBean<Void> pidExist(@PathVariable("pid") Integer pid){
        if (providerService.pidExist(pid))
            return new RestBean<>(200,"此供应商编码存在！");
        return new RestBean<>(403,"此供应商编码不存在！");
    }
}
