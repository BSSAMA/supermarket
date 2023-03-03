package com.supermarket.back.service;

import com.supermarket.back.entity.SysMenu;

import java.util.List;

public interface SysMenuService {
    List<SysMenu> getMenusByCurrentUser();
}
