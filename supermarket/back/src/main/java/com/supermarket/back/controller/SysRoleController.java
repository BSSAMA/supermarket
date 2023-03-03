package com.supermarket.back.controller;

import com.supermarket.back.entity.resp.RestBean;
import com.supermarket.back.service.Impl.SysRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class SysRoleController {
    @Autowired
    private SysRoleServiceImpl sysRoleService;

    @GetMapping("/getrolelist")
    public RestBean< List<String> > getRoleList(){
        return new RestBean<>(200,"请求成功！",sysRoleService.getRoleList());
    }


}
