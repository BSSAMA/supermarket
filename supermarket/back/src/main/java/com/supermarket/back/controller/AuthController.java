package com.supermarket.back.controller;

import com.supermarket.back.entity.Account;
import com.supermarket.back.entity.Captcha;
import com.supermarket.back.entity.resp.RestBean;
import com.supermarket.back.service.AccountService;
import com.supermarket.back.service.VerifyService;
import com.supermarket.back.util.CaptchaUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    VerifyService verifyService;
    @Resource
    AccountService accountService;
    @Resource
    StringRedisTemplate template;


    @GetMapping("/verifycode/{email}")
    public RestBean<Void> verifyCode(@PathVariable("email") String mail){
        try{
            verifyService.sendVerifyCode(mail);
            return new RestBean<>(200,"邮件发送成功！");
        }catch (Exception e){
            return new RestBean<>(500,"邮件发送失败！");
        }
    }

    @PostMapping(value = "/register/{verify}")
    public RestBean<Void> register(@RequestBody Account account,@PathVariable("verify") String verify){
        if (verifyService.doVerify(account.getEmail(),verify)){
            accountService.createAccount(account);
            return new RestBean<>(200,"注册成功！");
        }
        else {
            return new RestBean<>(403,"邮箱验证码错误！请重新获取！");
        }
    }


    @GetMapping("/getcaptcha/{imgUUID}")
    public RestBean<Captcha> getCaptcha(@PathVariable("imgUUID") String imgUUID) {

        //画图工具类
        CaptchaUtil imageCode = new CaptchaUtil();
        // 获取验证码对应的 base64  编码
        String base64 = CaptchaUtil.getBase64(imageCode.getImage());

        // 获取对应的 验证码 code
        String code = imageCode.getCode();

        // 封装 获取的 验证码相关的数据 到 验证码对象中，并响应
        Captcha captcha = new Captcha();
        if (imgUUID.equals("null")) {
            imgUUID = String.valueOf(System.currentTimeMillis());
            captcha.setImgUUID(imgUUID);
        }
        captcha.setImg(base64);

        // 将验证码的信息保存到 redis中,并设置 有效时间！
        template.opsForValue().set("captchaUUID:"+imgUUID,code+"",3, TimeUnit.MINUTES);

        // 将封装好的验证码对象响应给前端
        return new RestBean<>(200,"请求成功！",captcha);
    }

    @PostMapping(value = "/checkcaptcha/{imgUUID}/{code}")
    public RestBean<Void> checkVerify(@PathVariable("imgUUID") String imgUUID,@PathVariable("code") String code) {
        String captcha = template.opsForValue().get("captchaUUID:"+imgUUID);
        if (captcha == null)
            return new RestBean<>(403,"验证码过期！");
        else if (!captcha.equals(code))
            return new RestBean<>(403,"验证码错误！");
        else {
            template.delete("captchaUUID:"+imgUUID);
            return new RestBean<>(200,"验证码正确！");
        }
    }

}
