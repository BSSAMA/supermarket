package com.supermarket.back.entity;


import com.supermarket.back.entity.IDClass.SysMenuRoleID;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@Entity
@IdClass(SysMenuRoleID.class)
@Table(name = "sys_menu_role")
public class SysMenuRole {
    @Id
    private int menuId;
    @Id
    private int roleId;
}
