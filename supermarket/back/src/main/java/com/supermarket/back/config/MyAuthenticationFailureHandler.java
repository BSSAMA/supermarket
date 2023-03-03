package com.supermarket.back.config;

import com.supermarket.back.entity.resp.RestBean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler extends JSONAuthentication implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        RestBean<Void> result = new RestBean<>(0,"");
       if (e instanceof BadCredentialsException) {
            //密码错误
            result.error(2002,"密码错误！");
        } else if (e instanceof DisabledException) {
            //账号不可用
            result.error(2003,"账号不可用！");
        } else if (e instanceof InternalAuthenticationServiceException) {
            //用户不存在
            result.error(2004,"用户不存在！");
        } else {
            //其他错误
            result.error(2009,"其他错误！");
        }
        this.WriteJSON(request, response, result);
    }
}
