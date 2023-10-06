package com.ldx.blog.utils;

import com.ldx.blog.pojo.oath.GiteeAccessToken;
import com.ldx.blog.pojo.oath.gitee.GiteeUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

/**
 * @author Uaena
 * @date 2023/6/4 0:43
 */
@SpringBootTest
@ActiveProfiles("test")
class HttpUtilTest {
    @Value("${oauth.gitee.client_id}")
    private String GITEE_CLIENT_ID;
    @Value("${oauth.gitee.client_secret}")
    private String GITEE_CLIENT_SECRET;
    @Value("${oauth.gitee.redirect_uri}")
    private String GITEE_REDIRECT_URI;
    @Test
    void getAccessToken() {
        String code = "f2dea59820928cfb51f9534f52368e74e91c36d435e098b14dd1ae0ade0fe7e1";
        String url = "https://gitee.com/oauth/token?grant_type=authorization_code&code="+code+"&client_id="+GITEE_CLIENT_ID+"&redirect_uri="+GITEE_REDIRECT_URI+"&client_secret="+GITEE_CLIENT_SECRET;
        GiteeAccessToken accessToken = HttpUtil.getAccessToken(url, GiteeAccessToken.class,"post");
        System.out.println(accessToken);
    }
    @Test
    void getGiteeUserInfo(){
        String user_info = "https://gitee.com/api/v5/user?access_token=a070ee0497e81e544cfb5d37fa6f1f0e";
        String email_info = "https://gitee.com/api/v5/emails?access_token=a070ee0497e81e544cfb5d37fa6f1f0e";
        GiteeUser giteeUser = RestTemplateUtils.get(user_info, GiteeUser.class);
        System.out.println(giteeUser);
        List giteeUser1 = RestTemplateUtils.get(email_info, List.class);
        Map<String,String> o = (Map<String, String>) giteeUser1.get(0);
        String email = o.get("email");
//        giteeUser.setEmail(RestTemplateUtils.get(email_info, GiteeUser.class).getEmail());
        System.out.println(email);

    }
}