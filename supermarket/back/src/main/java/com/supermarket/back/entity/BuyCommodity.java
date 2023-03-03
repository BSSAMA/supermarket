package com.supermarket.back.entity;

import com.supermarket.back.entity.IDClass.BuyCommodityID;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Date;

@Data
@Entity
@IdClass(BuyCommodityID.class)
@Table(name = "buy_commodity")
public class BuyCommodity {
    @Id
    private String bid;
    @Id
    private String cid;
    @Id
    private String batchNumber;
    private Date dateProduction;
    private Integer expirationDate;
    private float buyingPrice;
    private int number;
    private float sumPrice;
    private Integer pid;
}
