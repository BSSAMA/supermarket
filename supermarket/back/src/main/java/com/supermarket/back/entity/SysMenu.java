package com.supermarket.back.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "sys_menu")
public class SysMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mid;
    private String path;
    private String name;
    private String nameZh;
    private String iconCls;
    private String component;
    private Integer parentId;

    @Transient
    List<SysMenu> children;
}
