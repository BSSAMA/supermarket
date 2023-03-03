package com.supermarket.back.service.Impl;

import com.supermarket.back.entity.Account;
import com.supermarket.back.entity.SysAccountsRole;
import com.supermarket.back.entity.SysMenu;
import com.supermarket.back.entity.SysMenuRole;
import com.supermarket.back.repository.AccountRepository;
import com.supermarket.back.repository.SysAccountsRoleRepository;
import com.supermarket.back.repository.SysMenuRepository;
import com.supermarket.back.repository.SysMenuRoleRepository;
import com.supermarket.back.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SysAccountsRoleRepository sysAccountsRoleRepository;

    @Autowired
    private SysMenuRoleRepository sysMenuRoleRepository;

    @Autowired
    private SysMenuRepository sysMenuRepository;

    private List<Integer> findRoleId(){
        // 从数据库中获取当前用户
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        Account user = accountRepository.findByUsername(username);
        // 获得当前用户对应的所有角色的 id 列表
        return sysAccountsRoleRepository.getAllByAccountId(user.getAid())
                .stream().map(SysAccountsRole::getRoleId).collect(Collectors.toList());
    }

    private void handleMenus(List<SysMenu> menus) {
        menus.forEach(m -> {
            List<SysMenu> children = sysMenuRepository.getAllByParentIdAndRoleIn(m.getMid(),findRoleId());
            m.setChildren(children);
        });

        menus.removeIf(m -> m.getParentId() != 0);
    }

    @Override
    public List<SysMenu> getMenusByCurrentUser() {
        // 查询出这些角色对应的所有菜单项
        List<Integer> menuIds = sysMenuRoleRepository.findAllByRoleIdIn(findRoleId())
                .stream().map(SysMenuRole::getMenuId).collect(Collectors.toList());
        List<SysMenu> menus = sysMenuRepository.findAllById(menuIds).stream().distinct().collect(Collectors.toList());

//         处理菜单项的结构
        handleMenus(menus);
        return menus;
    }
}
