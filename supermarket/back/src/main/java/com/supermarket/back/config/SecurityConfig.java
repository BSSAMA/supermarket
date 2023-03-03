package com.supermarket.back.config;

import com.supermarket.back.service.Impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Autowired
    private MyLogoutHandler myLogoutHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private MyInvalidSessionStrategy myInvalidSessionStrategy;

    @Autowired
    private MyExpiredSessionStrategy myExpiredSessionStrategy;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                //配置访问资源所需要的权限
//                .antMatchers("/users","/roles")
//                //.hasAuthority() 只能设置一个
//                //Authority 和 Role 效果一样，可以同时用
//                //设置权限 ROLE开头
//                .hasAnyAuthority("ROLE_user","ROLE_admin")
//                .antMatchers("/menus","/others")
//                //.hasRole() 只能设置一个
//                //设置角色 不需要ROLE开头
//                .hasAnyRole("admin")
                //设置查询商品需要具有管理员或者用户的权限
                .antMatchers("/api/commodity/getcommodity/**","/api/user/islogin").hasAnyAuthority("ROLE_admin","ROLE_user")
                //设置商品的其他操作需要管理员权限
                .antMatchers("/api/commodity/**","/api/provider/**","/api/user/**").hasAnyAuthority("ROLE_admin")
                //设置身份验证类请求任何人都可访问
                .antMatchers("/api/auth/**").permitAll()
//                .antMatchers("/api/user/**")
//                .hasAnyAuthority("ROLE_admin")
                //任何请求都需要验证
                .anyRequest()
                .authenticated()
                .and()
                //设置登录页面
                .formLogin()
                .loginProcessingUrl("/api/auth/login")
                //对上一请求不做验证
                .permitAll()
                //登陆成功，自定义登录成功处理器
                .successHandler(myAuthenticationSuccessHandler)
                //登录失败，自定义登录失败处理器
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                .exceptionHandling()
                //权限不足的处理方案，自定义权限不足处理器
                .accessDeniedHandler(myAccessDeniedHandler)
                //未登录访问资源时提示的异常，自定义未登录处理器
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .and()
                .logout()
                .permitAll()
                //添加注销操作处理器，自定义注销处理器
                .addLogoutHandler(myLogoutHandler)
                .logoutUrl("/api/auth/logout")
                //注销成功处理器，自定义注销成功处理器
                .logoutSuccessHandler(myLogoutSuccessHandler)
                //注销后删除cookie
                .deleteCookies("JSESSIONID")
                .and()
                .sessionManagement()
                //会话创建
                //always：如果当前请求没有对应的session存在，Spring Security创建一个session。
                //ifRequired（默认）： Spring Security在需要使用到session时才创建session
                //never： Spring Security将永远不会主动创建session，但是如果session在当前应用中已经存在，它将使用该session
                //stateless：Spring Security不会创建或使用任何session。适合于接口型的无状态应用（前后端分离无状态应用），这种方式节省内存资源
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                //登录超时处理方案，自定义登录超时处理器
                .invalidSessionStrategy(myInvalidSessionStrategy)
                //最大允许登录数量
                .maximumSessions(1)
                //是否允许另一个账户登录，true：达到最大数后不允许登录，false：达到最大数允许登录，最先登录的被踢下线
                .maxSessionsPreventsLogin(false)
                //被挤下线登录方式
                .expiredSessionStrategy(myExpiredSessionStrategy);



        http.csrf().disable();
        // 开启跨域访问
        http.cors(); //.disable();
//        // iframe 跳转错误处理 Refused to display 'url' in a frame because it set 'X-Frame-Options' to 'deny'
//        http.headers().frameOptions().disable();
//        // 当出现跨域的OPTIONS请求时，发现被拦截，加入下面设置可实现对OPTIONS请求的放行。
//        http.authorizeRequests().
//                requestMatchers(CorsUtils::isPreFlightRequest).
//                permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
