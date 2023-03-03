package com.supermarket.back.entity.IDClass;

import lombok.Data;

import java.io.Serializable;

@Data
public class BuyCommodityID implements Serializable {
    private String bid,cid,batchNumber;
}
