package com.ldx.blog.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Uaena
 * @date 2023/5/21 12:00
 */
//@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private static final long MAX_AGE = 24 * 60 * 60;
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                //.allowCredentials(true)
                //.allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> {
                    SaRouter.match("/**")
                            .notMatch("/**/login/**",
                                    "/actuator/**",
                                    "/**/registry",
                                    "/**/logout",
                                    "/oauth/**",
                                    "/article/list",
                                    "/article/details/**",
                                    "/comment/comment_brief",
                                    "/website/**",
                                    "/category/**",
                                    "/tag/**",
                                    "/email/**",
                                    "/upload/**")
                            .check(r-> StpUtil.checkLogin());
                    // 根据路由划分模块，不同模块不同鉴权
//                    SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
//                    SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//                    SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//                    SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//                    SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
//                    SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
                }))
                .addPathPatterns("/**");
    }
}
