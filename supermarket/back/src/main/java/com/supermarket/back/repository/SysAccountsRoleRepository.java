package com.supermarket.back.repository;

import com.supermarket.back.entity.IDClass.SysAccountsRoleID;
import com.supermarket.back.entity.SysAccountsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysAccountsRoleRepository extends JpaRepository<SysAccountsRole, SysAccountsRoleID> {
    List<SysAccountsRole> getAllByAccountId(Integer id);
}
