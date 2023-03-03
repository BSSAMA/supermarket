package com.supermarket.back.entity;

import com.supermarket.back.entity.IDClass.CommodityID;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Date;

@Data
@Entity
@IdClass(CommodityID.class)
@Table(name = "commodity")
public class Commodity {
    @Id
    private String cid;
    private String name;
    private String brand;
    private Date dateProduction;
    private Integer expirationDate;
    private float buyingPrice;
    private float price;
    private String type;
    private String location;
    private Integer number;
    @Id
    private String batchNumber;
}
