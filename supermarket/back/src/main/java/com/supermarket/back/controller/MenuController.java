package com.supermarket.back.controller;

import com.supermarket.back.entity.SysMenu;
import com.supermarket.back.entity.resp.RestBean;
import com.supermarket.back.service.Impl.SysMenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    @Autowired
    private SysMenuServiceImpl sysMenuService;

    @GetMapping("/getmenu")
    public RestBean<List<SysMenu>> menu() {
        return new RestBean<>(200, "菜单获取成功！", sysMenuService.getMenusByCurrentUser()) ;
    }

}
