package com.supermarket.back.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "commodity_type")
public class CommodityType {
    @Id
    private String name;
}
