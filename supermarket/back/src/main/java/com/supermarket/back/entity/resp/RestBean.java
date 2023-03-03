package com.supermarket.back.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestBean<T> {
    int code;
    String reason;
    T data;

    public RestBean(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public void ok(String reason){
        this.code = 200;
        this.reason = reason;
    }

    public void error(int code,String reason){
        this.code = code;
        this.reason = reason;
    }


}
