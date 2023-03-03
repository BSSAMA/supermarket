package com.supermarket.back.entity.IDClass;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysAccountsRoleID implements Serializable {
    private int accountId,roleId;
}
