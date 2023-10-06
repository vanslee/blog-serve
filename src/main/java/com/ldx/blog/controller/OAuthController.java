package com.ldx.blog.controller;

import com.ldx.blog.pojo.oath.GiteeAccessToken;
import com.ldx.blog.pojo.oath.gitee.GiteeUser;
import com.ldx.blog.pojo.oath.github.GithubAccessToken;
import com.ldx.blog.pojo.oath.github.GithubUser;
import com.ldx.blog.pojo.oath.qq.QQAccessToken;
import com.ldx.blog.pojo.oath.qq.QQOpenid;
import com.ldx.blog.pojo.oath.qq.QQUserInfo;
import com.ldx.blog.result.Result;
import com.ldx.blog.service.impl.user.UserServiceImpl;
import com.ldx.blog.utils.HttpUtil;
import com.ldx.blog.utils.IPUtil;
import com.ldx.blog.utils.RestTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Uaena
 * @date 2023/6/3 19:19
 */
@Profile("!test")
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@RestController
@RequestMapping("/oauth")
@Slf4j
public class OAuthController {
    @Value("${redirect.error-page}")
    private String ERROR_PAGE;
    @Resource
    private UserServiceImpl userService;
    @Value("${oauth.gitee.client_id}")
    private String GITEE_CLIENT_ID;
    @Value("${oauth.qq.client_id}")
    private String QQ_CLIENT_ID;
    @Value("${oauth.gitee.client_secret}")
    private String GITEE_CLIENT_SECRET;
    @Value("${oauth.qq.app_key}")
    private String QQ_APP_SECRET;
    @Value("${oauth.gitee.redirect_uri}")
    private String GITEE_REDIRECT_URI;
    @Value("${oauth.qq.redirect_uri}")
    private String QQ_REDIRECT_URI;
    @Value("${oauth.github.client_id}")
    private String GITHUB_CLIENT_ID;
    @Value("${oauth.github.client_secret}")
    private String GITHUB_CLIENT_SECRET;
    @Value("${oauth.github.redirect_uri}")
    private String GITHUB_REDIRECT_URI;
    private final String GITEE_TOKEN_URI = "https://gitee.com/oauth/token?grant_type=authorization_code&code=";
    private final String GITHUB_TOKEN_URI = "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s&redirect_uri=%s";
    private final String QQ_TOKEN_URL = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&fmt=json&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s";
    private final String QQ_OPENID_URL = "https://graph.qq.com/oauth2.0/me?access_token=%s&fmt=json";
    private final String QQ_USERINFO_URL = "https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";

    @GetMapping("/qq/login")
    public void qqLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam String code) throws IOException {
        log.debug("获取到的Code信息:{}", code);
        String ip = IPUtil.ip(request);
        String ak_url = String.format(QQ_TOKEN_URL,QQ_CLIENT_ID, QQ_APP_SECRET, code,QQ_REDIRECT_URI);
        QQAccessToken tkVo = HttpUtil.getAccessToken(ak_url, QQAccessToken.class,"get");
        String openid_url = String.format(QQ_OPENID_URL, tkVo.getAccessToken());
        QQOpenid openidVo = HttpUtil.getAccessToken(openid_url, QQOpenid.class,"get");
        String userinfo_url = String.format(QQ_USERINFO_URL, tkVo.getAccessToken(), QQ_CLIENT_ID, openidVo.getOpenid());
        QQUserInfo qqUserVo = HttpUtil.getAccessToken(userinfo_url, QQUserInfo.class,"get");
        qqUserVo.setOpenid(openidVo.getOpenid());
        userService.oauthLogin(qqUserVo, ip, response);


    }

    @GetMapping("/wechat/login")
    public Result<String> wechatLogin() {
        return Result.success("success");
    }
    @GetMapping("/github/login")
    public void githubLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam String code) throws IOException {
        log.debug("获取到的Code信息:{}", code);
        String ip = IPUtil.ip(request);
        // 获取Token
        String access_token = String.format(GITHUB_TOKEN_URI,GITHUB_CLIENT_ID,GITHUB_CLIENT_SECRET,code,GITHUB_REDIRECT_URI);
        GithubAccessToken akVo = HttpUtil.getAccessToken(access_token, GithubAccessToken.class,"post");
        if (Objects.isNull(akVo) ||Objects.isNull(akVo.getAccessToken()) ) {
            log.error("gitee登录获取AccessToken失败");
            response.sendRedirect(ERROR_PAGE);
        }
        log.debug("获取到的AK信息:{}", akVo.getAccessToken());
        String user_info_url = "https://api.github.com/user";
        GithubUser githubUser = HttpUtil.getGithubUser(user_info_url, akVo.getAccessToken());
        if (Objects.isNull(githubUser)){
            response.sendRedirect(ERROR_PAGE);
            return ;
        }
        userService.oauthLogin(githubUser, ip, response);

    }

    @GetMapping("/gitee/login")
    public void giteeLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam String code) throws IOException {
        log.debug("获取到的Code信息:{}", code);
        String ip = IPUtil.ip(request);
        // 获取Token
        String access_token = GITEE_TOKEN_URI + code + "&client_id=" + GITEE_CLIENT_ID + "&redirect_uri=" + GITEE_REDIRECT_URI + "&client_secret=" + GITEE_CLIENT_SECRET;
        GiteeAccessToken accessToken = HttpUtil.getAccessToken(access_token, GiteeAccessToken.class,"post");
        if (Objects.isNull(accessToken)) {
            log.error("gitee登录获取AccessToken失败");
            response.sendRedirect(ERROR_PAGE);
        }
        log.debug("获取到的AK信息:{}", accessToken.getAccessToken());
        String user_info = "https://gitee.com/api/v5/user?access_token=" + accessToken.getAccessToken();
//        String user_info = "https://gitee.com/api/v5/user?access_token=27c0ef87d04a028dbd849f6ceb805b27";
        String email_info = "https://gitee.com/api/v5/emails?access_token=" + accessToken.getAccessToken();
//        String email_info = "https://gitee.com/api/v5/emails?access_token=27c0ef87d04a028dbd849f6ceb805b27";
        GiteeUser giteeUser = null;
        String email = null;
        try {
            giteeUser = RestTemplateUtils.get(user_info, GiteeUser.class);
        } catch (Exception e) {
            log.error("获取Gitee用户信息失败,使用的Code:{}", code);
            response.sendRedirect(ERROR_PAGE);
        }
        try {
            List list = RestTemplateUtils.get(email_info, List.class);
            if (!Objects.isNull(list)) {
                Object o = list.get(0);
                if (!Objects.isNull(o)) {
                    Map<String, String> body = (Map<String, String>) o;
                    email = body.get("email");
                } else {
                    throw new Exception("获取Gitee邮箱失败");
                }
            } else {
                throw new Exception("获取Gitee邮箱失败");
            }
        } catch (Exception e) {
            log.error("获取Gitee邮箱失败,使用的Code:{}", code);
            response.sendRedirect(ERROR_PAGE);
        }
        giteeUser.setEmail(email);
        userService.oauthLogin(giteeUser, ip, response);

    }
//    @GetMapping("/callback")
//    public Result<String> callback(Oa){
//
//    }
}
