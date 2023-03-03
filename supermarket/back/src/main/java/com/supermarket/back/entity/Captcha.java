package com.supermarket.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码对应实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Captcha {
    // 该验证码对应的UUID
    private String imgUUID;
    // 该验证码对应的 base64 加密格式
    private String img;
    // 该验证码对应的具体的 码
    private String code;
}
