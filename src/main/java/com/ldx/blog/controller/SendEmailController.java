package com.ldx.blog.controller;

import com.alibaba.fastjson.JSON;
import com.ldx.blog.components.RedisDao;
import com.ldx.blog.config.RabbitMQTransaction;
import com.ldx.blog.constants.RedisKeys;
import com.ldx.blog.pojo.email.EmailMessage;
import com.ldx.blog.result.Result;
import com.ldx.blog.result.ResultCodeEnum;
import com.ldx.blog.utils.GenerateRandCipherUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.ldx.blog.config.RabbitMQConfirmConfig.CONFIRM_EXCHANGE_NAME;
import static com.ldx.blog.config.RabbitMQConfirmConfig.CONFIRM_ROUTE_KEY_NAME;

/**
 * @author Uaena
 * @date 2023/8/1 22:25
 */
@RestController
@RequestMapping("/email")
public class SendEmailController {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RedisDao redisDao;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(rabbitMQTransaction);
        rabbitTemplate.setReturnsCallback(rabbitMQTransaction);
    }

    @Autowired
    private RabbitMQTransaction rabbitMQTransaction;

    @GetMapping("/sign_in_by_email")
    public Result<Boolean> sendSignInEmail(@RequestParam String email) {
        if (redisDao.hasKey(RedisKeys.SIGN_IN_EMAIL.concat(email))) {
            return Result.fail(ResultCodeEnum.HAS_SEND_EMAIL);
        } else {
            String verificationCode = GenerateRandCipherUtil.generateCode();
            redisDao.setValue(RedisKeys.SIGN_IN_EMAIL.concat(email), verificationCode, TimeUnit.MINUTES.toMillis(3));
            EmailMessage emailMessage = new EmailMessage(email, verificationCode);
            rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,CONFIRM_ROUTE_KEY_NAME, JSON.toJSONString(emailMessage));
        }
        return Result.success(ResultCodeEnum.EMAIL_SEND_SUCCESS,true);
    }
}
