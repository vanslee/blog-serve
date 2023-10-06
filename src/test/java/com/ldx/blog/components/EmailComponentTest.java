package com.ldx.blog.components;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Uaena
 * @date 2023/8/1 21:45
 */
@SpringBootTest
class EmailComponentTest {
    @Resource
    private  EmailSenderService emailComponent;
    @Test
    public void sendEmail(){
        emailComponent.sendEmail("2492675305@qq.com","测试邮件", "你好");
    }
}