package com.supermarket.back.entity.IDClass;

import lombok.Data;

import java.io.Serializable;

@Data
public class SalesID implements Serializable {
    private String sid,cid,batchNumber;
}
