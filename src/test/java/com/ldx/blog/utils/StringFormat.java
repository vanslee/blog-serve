package com.ldx.blog.utils;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Uaena
 * @date 2023/6/10 0:16
 */
public class StringFormat {
    @Test
    public void format(){
          String QQ_TOKEN_URL = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&fmt=json&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s";
        String oeleJjvsZ6PVN1Qa = String.format(QQ_TOKEN_URL, "102024615", "OeleJjvsZ6PVN1Qa", "https%3A%2F%2Fwww.lidengxiang.top%2Foauth%2Fqq%2Flogin");
        System.out.println(oeleJjvsZ6PVN1Qa);
    }
    @Test
    public void test(){
        String hashpw = BCrypt.hashpw("zhou2001.");
        System.out.println(hashpw);
    }
}
