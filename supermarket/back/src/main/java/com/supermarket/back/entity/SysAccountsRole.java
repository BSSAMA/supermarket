package com.supermarket.back.entity;

import com.supermarket.back.entity.IDClass.SysAccountsRoleID;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@Entity
@IdClass(SysAccountsRoleID.class)
@Table(name = "sys_accounts_role")
public class SysAccountsRole {
    @Id
    private int accountId;
    @Id
    private int roleId;
}
