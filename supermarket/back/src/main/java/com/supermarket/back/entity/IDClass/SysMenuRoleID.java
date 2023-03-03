package com.supermarket.back.entity.IDClass;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysMenuRoleID implements Serializable {
    private int menuId,roleId;
}
