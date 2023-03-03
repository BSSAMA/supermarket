package com.supermarket.back.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aid;
    private String username;
    private String password;
    private String email;
    private String tel;
    private float integral;
    private String enable;
}
