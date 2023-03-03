package com.supermarket.back.config;

import com.supermarket.back.entity.resp.RestBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler extends JSONAuthentication implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //只需要以Json的格式返回一个提示就行

        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        RestBean<String> result = new RestBean<>(200,"登录成功！",username);

        this.WriteJSON(request, response, result);
    }
}
