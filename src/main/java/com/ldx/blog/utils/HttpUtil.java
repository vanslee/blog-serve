package com.ldx.blog.utils;

import com.alibaba.fastjson.JSON;
import com.ldx.blog.pojo.oath.github.GithubUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * @author ldx
 */
@Slf4j
public class HttpUtil {
    private static final OkHttpClient client = new OkHttpClient();

    private HttpUtil() {
    }

    public static GithubUser getGithubUser(String user_info_url, String accessToken) {
        log.info("========请求路径:{}", user_info_url);
        Request request = new Request.Builder()
                .header("Accept", "application/vnd.github+json")
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(user_info_url)
                .get()
                .build();
        try(Response response = client.newCall(request).execute()){
            return JSON.parseObject(response.body().bytes(),GithubUser.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取GitHub用户信息失败:{}",accessToken);
            return null;
        }
    }

    private static class SingletonHolder {
        private static final OkHttpClient INSTANCE = new OkHttpClient();
    }

    public static OkHttpClient getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public static <T> T getAccessToken(String url, Class<T> type, String method) {
        log.info("========请求路径:{}", url);
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), "");
        Request request = null;
        if (method.equals("post")) {
            request = new Request.Builder()
                    .header("Accept", "application/json")
                    .url(url)
                    .post(body)
                    .build();
        }
        if (method.equals("get")) {
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
        }
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == HttpStatus.OK.value()) {
                return JSON.parseObject(response.body().bytes(), type);
            } else {
                log.error(response.body().bytes().toString());
            }

        } catch (IOException e) {
            log.error("获取AccessToken出现错误:{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}