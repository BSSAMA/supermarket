package com.supermarket.back.service;


import com.supermarket.back.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    void createAccount(Account account);

    void saveAccount(Account account);

    Page<Account> getAccount(String username, String email, String enable, Pageable pageable);

    boolean accountExist(String username);

    boolean deleteById(Integer aid);

    boolean telExist(String tel);

    Account findAccountByTel(String tel);

    List<String> getRoleNameByUserName(String username);
}
