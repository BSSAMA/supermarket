package com.supermarket.back.service.Impl;

import com.supermarket.back.entity.Account;
import com.supermarket.back.entity.IDClass.SysAccountsRoleID;
import com.supermarket.back.entity.SysAccountsRole;
import com.supermarket.back.repository.AccountRepository;
import com.supermarket.back.repository.SysAccountsRoleRepository;
import com.supermarket.back.repository.SysRoleRepository;
import com.supermarket.back.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    AccountRepository accountRepository;

    @Resource
    SysAccountsRoleRepository sysAccountsRoleRepository;

    @Resource
    SysRoleRepository sysRoleRepository;

    @Override
    public void createAccount(Account account) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        account.setPassword(encoder.encode(account.getPassword()));
        account.setIntegral(0);
        account.setEnable("true");
        accountRepository.save(account);
        SysAccountsRole sysAccountsRole = new SysAccountsRole();
        sysAccountsRole.setAccountId(accountRepository.findIdByUsername(account.getUsername()));
        sysAccountsRole.setRoleId(2);
        sysAccountsRoleRepository.save(sysAccountsRole);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Page<Account> getAccount(String username, String email, String enable, Pageable pageable) {
        return accountRepository.findAccountByUsernameLikeAndEmailLikeAndEnableLikeOrderByAidAsc('%'+username+'%','%'+email+'%','%'+enable+'%',pageable);
    }

    @Override
    public boolean accountExist(String username) {
        Account user = accountRepository.findByUsername(username);
        return user != null;
    }

    @Override
    public boolean deleteById(Integer aid) {
        accountRepository.deleteById(aid);
        return true;
    }


    @Override
    public boolean telExist(String tel) {
        Account user = accountRepository.findAccountByTel(tel);
        return user != null;
    }

    @Override
    public Account findAccountByTel(String tel) {
        return accountRepository.findAccountByTel(tel);
    }

    public Account findAccountByUsername(String username){
        return accountRepository.findAccountByUsername(username);
    }


    @Override
    public List<String> getRoleNameByUserName(String username){

        return accountRepository.getRoleNameByUserName(username);
    }

    public void updateRole(String username, List<String> roles) {
        Integer aid = accountRepository.findByUsername(username).getAid();
        List<String> rolesName = accountRepository.getRoleNameByUserName(username);
        rolesName.forEach(e -> {
            if (!roles.contains(e)) {
                Integer rid = sysRoleRepository.findRidByName(e);
                SysAccountsRoleID sysAccountsRoleID = new SysAccountsRoleID();
                sysAccountsRoleID.setAccountId(aid);
                sysAccountsRoleID.setRoleId(rid);
                sysAccountsRoleRepository.deleteById(sysAccountsRoleID);
            }
        });
        roles.forEach(e -> {
            if(!rolesName.contains(e)){
                Integer rid = sysRoleRepository.findRidByName(e);
                SysAccountsRole sysAccountsRole = new SysAccountsRole();
                sysAccountsRole.setAccountId(aid);
                sysAccountsRole.setRoleId(rid);
                sysAccountsRoleRepository.save(sysAccountsRole);
            }
        });
    }
}
