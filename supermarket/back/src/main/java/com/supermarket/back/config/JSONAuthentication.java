package com.supermarket.back.config;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//封装输出JSON格式的类
public abstract class JSONAuthentication {
    protected void WriteJSON(HttpServletRequest request,
                             HttpServletResponse response,
                             Object data) throws IOException, ServletException {
        //这里很重要，否则页面获取不到正常的JSON数据集
        //处理编码方式，防止中文乱码的情况
        response.setContentType("application/json;charset=UTF-8");
//        //以下解决跨域问题
        //动态设置Access-Control-Allow-Origin 前端axios携带cookie时要求不能为空
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("origin"));
        //输出JSON
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(data));
        out.flush();
        out.close();
    }
}
