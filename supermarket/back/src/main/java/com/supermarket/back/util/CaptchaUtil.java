package com.supermarket.back.util;

import cn.hutool.core.codec.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class CaptchaUtil {
    //验证码个数
    private int count=4;
    //验证码宽度，且设置每个字的宽度
    private int width=count*25;
    //验证码高度
    private int height=40;
    //图片验证码key
    private String code="";
    //bufferedImage
    private BufferedImage bufferedImage;
    public CaptchaUtil() {
    }
    public CaptchaUtil(int count, int width, int height) {
        this.count = count;
        this.width = width;
        this.height = height;
    }
    public String getCode() {
        return code;
    }
    public BufferedImage getImage(){
        //图片缓冲区
        BufferedImage image = new BufferedImage(width,height,1);
        //获得笔
        Graphics graphics = image.getGraphics();
        //设置初始画笔为白色
        graphics.setColor(new Color(255,255,254));
        //画满整个图，也就是把图片先变为白色
        graphics.fillRect(0,0,width,height);
        Random rd=new Random();
        //设置字体
        Font font=new Font("宋体",Font.PLAIN,35+rd.nextInt(10));
        graphics.setFont(font);
        char[] chars="0123456789".toCharArray();
        //画验证码
        for (int i = 0; i <count ; i++) {
            String string="";
            string+=chars[rd.nextInt(chars.length)]+"";
            graphics.setColor(new Color(rd.nextInt(254),rd.nextInt(254),rd.nextInt(254)));
            graphics.drawString(string,25*i+rd.nextInt(10),26+rd.nextInt(15));
            code+=string;
        }
        //干扰线
        for (int i = 0; i <count+count/2 ; i++) {
            graphics.setFont(new Font("宋体",Font.PLAIN,10));
            graphics.setColor(new Color(rd.nextInt(255),rd.nextInt(255),rd.nextInt(255)));
            graphics.drawLine(rd.nextInt(width),rd.nextInt(height),rd.nextInt(width),rd.nextInt(height));
        }
        //归还笔
        graphics.dispose();
        //写到流里面需要用到ImageIo
        this.bufferedImage=image;
        return image;
    }
    public static String getBase64(BufferedImage image){
        String base64 = null;
        try {
            //输出流
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            base64 = Base64.encode(stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;

    }
}
