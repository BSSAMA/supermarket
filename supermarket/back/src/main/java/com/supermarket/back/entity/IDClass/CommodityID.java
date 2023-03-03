package com.supermarket.back.entity.IDClass;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommodityID implements Serializable {
    private String cid,batchNumber;
}
