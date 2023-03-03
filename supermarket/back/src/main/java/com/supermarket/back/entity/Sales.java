package com.supermarket.back.entity;

import com.supermarket.back.entity.IDClass.SalesID;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Data
@IdClass(SalesID.class)
@Table(name = "sales")
public class Sales {
    @Id
    private String sid;
    @Id
    private String cid;
    @Id
    private String batchNumber;
    private int number;
    private float price;
    private float sumPrice;
    private String tel;
}
