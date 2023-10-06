package com.ldx.blog.config;

import com.alibaba.fastjson.JSON;
import com.ldx.blog.components.EmailSenderService;
import com.ldx.blog.pojo.email.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.ldx.blog.config.RabbitMQConfirmConfig.CONFIRM_QUEUE_NAME;

/**
 * @author Uaena
 * @date 2023/7/31 22:13
 */
@Component
@Slf4j
public class RabbitMQConfirmCustomer {
    @Resource
    private EmailSenderService emailSenderService;

    @RabbitListener(queues = CONFIRM_QUEUE_NAME)
    public void receiveMsg(Message message){
        EmailMessage emailMessage = JSON.parseObject(new String(message.getBody()), EmailMessage.class);
        emailSenderService.sendEmail(emailMessage.getReceiverEmail(),"李图报博客验证码:", emailMessage.getVerificationCode());
        log.debug("发送给用户:{}的验证码:{}成功.时间:{}",emailMessage.getReceiverEmail(),emailMessage.getVerificationCode(), System.currentTimeMillis());
    }
}
