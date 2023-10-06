package com.ldx.blog.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Uaena
 * @date 2023/6/3 21:15
 */
@Slf4j
public class RestTemplateUtils {
    private static RestTemplate restTemplate;
    /**
     * 超时时间，单位为毫秒
     */
    private static final int TIMEOUT = 5000;

    private RestTemplateUtils() {
    }

    ;

    /**
     * 获取单例的 RestTemplate 对象
     *
     * @return 单例的 RestTemplate 对象
     */
    public static synchronized RestTemplate getInstance() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        return restTemplate;
    }

    private static final String EMPTY_STR = "";

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static <T> T get(String url,Class<T> type) {
        RestTemplate instance = getInstance();
        return instance.getForObject(url,type);
    }

    /**
     * post JSON
     *
     * @param url
     * @param headerMap
     * @param bodyMap
     * @return
     */
    public static String postJson(String url, Map<String, String> bodyMap, Map<String, String> headerMap) {

        RestTemplate instance = getInstance();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 ");
//        headers.add("Accept","*/*");

//        if (!CollectionUtils.isEmpty(headerMap)) {
//            headerMap.forEach((k, v) -> headers.set(k, v));
//        }

//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(bodyMap, headers);
        try {
            ResponseEntity<String> responseEntity = instance.postForEntity(url, null, String.class);
            log.info("responseEntity:{}",responseEntity);
            return null;
        }catch (HttpClientErrorException e){
            String responseBodyAsString = e.getResponseBodyAsString();
            log.error("获取Gitee-AccessToken失败:{}",e);
            return null;
        }
//        if (responseEntity.getStatusCode().is2xxSuccessful()){
//            // 解析响应体,获取访问令牌
//            return responseEntity.getBody();
//        }else {
//            log.error("获取Gitee-AccessToken失败:{}",responseEntity.getStatusCode());
//            // 处理错误
//            throw new RuntimeException();
//        }

    }

    /**
     * post Form
     *
     * @param url
     * @param headerMap
     * @param paramMap
     * @return
     */
    public static String postForm(String url, Map<String,String> paramMap) {
        log.info("url:{}",url);
        RestTemplate instance = getInstance();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        if (!CollectionUtils.isEmpty(headerMap)) {
//            headerMap.forEach((k, v) -> headers.set(k, v));
//        }
        String resultStr = instance.postForObject(url, null, String.class);
        return resultStr;
    }
}

