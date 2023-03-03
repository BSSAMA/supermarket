package com.supermarket.back.service.Impl;

import com.supermarket.back.entity.Account;
import com.supermarket.back.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) throw new UsernameNotFoundException("用户名或密码错误！");
        //如果存在
        boolean enable;
        enable = account.getEnable().equals("true");
        List<GrantedAuthority> authorities = new ArrayList<>();
        //需要根据用户获取角色 根据登录的用户获取权限/角色集合
        List<String> codeList = accountRepository.getRoleByUserName(username);
        codeList.forEach(code -> {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(code);
            authorities.add(authority);
        });
        return new User(account.getUsername(), account.getPassword(),enable,true,true,true, authorities);
    }

}
