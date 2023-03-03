package com.supermarket.back.controller;

import com.supermarket.back.entity.Account;
import com.supermarket.back.entity.resp.RestBean;
import com.supermarket.back.service.Impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;

    @GetMapping("/islogin")
    public RestBean<Void> isLogin(){
        return new RestBean<>(200,"用户已登录！");
    }

    @GetMapping("/getaccount/{page}")
    public RestBean<Page<Account>> getAccount(Account account, @PathVariable("page") int page){
        Pageable pageable = PageRequest.of(page-1,6);
        Page<Account> accounts = accountService.getAccount(account.getUsername(),account.getEmail(),account.getEnable(),pageable);
        if (accounts.isEmpty())
            return new RestBean<>(403,"查询失败，未查询到数据！");
        return new RestBean<>(200,"查找成功！",accounts);
    }

    @PostMapping("/saveaccount")
    public RestBean<Void> saveAccount(@RequestBody Account account){
        accountService.saveAccount(account);
        return new RestBean<>(200,"用户更新成功！");
    }

    @GetMapping("/deleteaccount/{aid}")
    public RestBean<Void> deleteAccount(@PathVariable("aid") Integer aid){
        if (accountService.deleteById(aid))
            return new RestBean<>(200,"删除成功！");
        return new RestBean<>(403,"删除失败！");
    }

    @GetMapping("/telexist/{tel}")
    public RestBean<Void> telExist(@PathVariable("tel") String tel){
        if (accountService.telExist(tel))
            return new RestBean<>(200,"此会员电话存在！");
        return new RestBean<>(403,"此会员电话不存在！");
    }

    @GetMapping("/getrole/{username}")
    public RestBean< List<String> > getRole(@PathVariable("username") String username){
        return new RestBean<>(200,"查找成功！",accountService.getRoleNameByUserName(username));
    }

    @PostMapping("/updaterole/{username}")
    public RestBean<Void> updateRole(@PathVariable("username") String username,@RequestBody List<String> roles){
        accountService.updateRole(username,roles);
        return new RestBean<>(200,"更新成功！");
    }

    @PostMapping("/updatePassword/{username}/{oldpassword}/{newpassword}")
    public RestBean<Void> updatePassword(@PathVariable("username") String username,@PathVariable("oldpassword") String oldPassword,@PathVariable("newpassword") String newPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Account account = accountService.findAccountByUsername(username);
        if (encoder.matches(oldPassword,account.getPassword())){
            account.setPassword(encoder.encode(newPassword));
            accountService.saveAccount(account);
            return new RestBean<>(200,"修改成功，请重新登录！");
        }
        else
            return new RestBean<>(403,"旧密码错误！");
    }

    @GetMapping("/getmyinfo/{username}")
    public RestBean<Account> getMyInfo(@PathVariable("username") String username){
        Account account = accountService.findAccountByUsername(username);
        account.setPassword(null);
        account.setEnable(null);
        return new RestBean<>(200,"请求成功！",account);
    }

    @PostMapping("/updatemyinfo")
    public RestBean<Void> updateMyInfo(@RequestBody Account account){
        Account oldAccount = accountService.findAccountByUsername(account.getUsername());
        oldAccount.setEmail(account.getEmail());
        oldAccount.setTel(account.getTel());
        accountService.saveAccount(oldAccount);
        return new RestBean<>(200,"修改成功！");
    }


}
