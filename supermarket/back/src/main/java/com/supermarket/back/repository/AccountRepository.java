package com.supermarket.back.repository;


import com.supermarket.back.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    Page<Account> findAccountByUsernameLikeAndEmailLikeAndEnableLikeOrderByAidAsc(String username, String email, String enable, Pageable pageable);

    Account findByUsername(String username);

    Account findAccountByUsername(String username);

//    nativeQuery = true 原生SQL执行
    @Query(value = "select\n" +
            "        r.role\n" +
            "        from sys_accounts_role ur\n" +
            "        left join `accounts` u on ur.account_id = u.aid\n" +
            "        left join sys_role r on ur.role_id = r.rid\n" +
            "        where\n" +
            "        u.username = ?1",nativeQuery = true)
    List<String> getRoleByUserName(String username);

    @Query(value = "select\n" +
            "        r.name\n" +
            "        from sys_accounts_role ur\n" +
            "        left join `accounts` u on ur.account_id = u.aid\n" +
            "        left join sys_role r on ur.role_id = r.rid\n" +
            "        where\n" +
            "        u.username = ?1",nativeQuery = true)
    List<String> getRoleNameByUserName(String username);


    Account findAccountByTel(String tel);

    @Query(value = "select aid from accounts where username = ?1",nativeQuery = true)
    Integer findIdByUsername(String username);
}
