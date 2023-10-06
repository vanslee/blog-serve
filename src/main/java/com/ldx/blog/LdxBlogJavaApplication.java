package com.ldx.blog;

import cn.dev33.satoken.SaManager;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author Uaena
 */
@Slf4j
@EnableCaching
@SpringBootApplication
@MapperScan(basePackages = "com.ldx.blog.mapper")
public class LdxBlogJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(LdxBlogJavaApplication.class, args);
        log.debug("启动成功：Sa-Token配置如下：[{}]",SaManager.getConfig());
    }

}
