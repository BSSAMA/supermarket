package com.supermarket.back.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "sys_role")
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rid;
    private String role;
    private String name;
}
