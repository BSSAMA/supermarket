package com.supermarket.back.service.Impl;

import com.supermarket.back.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl {
    @Autowired
    SysRoleRepository sysRoleRepository;


    public List<String> getRoleList() {
        return sysRoleRepository.getRoleList();
    }
}
