package com.supermarket.back.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "provider")
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;
    private String name;
    private String address;
    private String headName;
    private String tel;
}
