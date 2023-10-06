package com.ldx.blog.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Uaena
 * @date 2023/6/4 0:10
 */
@SpringBootTest
@ActiveProfiles("test")
class RestTemplateUtilsTest {
    @Value("${oauth.gitee.client_id}")
    private String GITEE_CLIENT_ID;
    @Value("${oauth.gitee.client_secret}")
    private String GITEE_CLIENT_SECRET;
    @Value("${oauth.gitee.redirect_uri}")
    private String GITEE_REDIRECT_URI;
    private final String GITEE_TOKEN_URI = "https://gitee.com/oauth/token?grant_type=authorization_code&";

    @Test
    void postJson() {
        String code = "204c01869730144dcb73dc39d87d0820c6d55b0703a3402e2f10e2994e182842";
        String url = "https://gitee.com/oauth/token?grant_type=authorization_code&code="+code+"&client_id="+GITEE_CLIENT_ID+"&redirect_uri="+GITEE_REDIRECT_URI+"&client_secret="+GITEE_CLIENT_SECRET;
        String s = RestTemplateUtils.postJson(url, null, null);
        System.out.println(s);
    }
}