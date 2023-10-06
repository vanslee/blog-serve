package com.ldx.blog.controller;

import com.ldx.blog.config.RabbitMQTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import static com.ldx.blog.config.RabbitMQConfirmConfig.CONFIRM_EXCHANGE_NAME;

/**
 * @author Uaena
 * @date 2023/7/31 22:11
 */
@RestController
@RequestMapping("/confirm")
@Slf4j
public class RabbitMQConfirmProducer {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQTransaction rabbitMQTransaction;

    //依赖注入rabbitMQ之后再设置它的回调对象
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(rabbitMQTransaction);
        rabbitTemplate.setReturnsCallback(rabbitMQTransaction);
    }

    //发送消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        //指定消息id为1
        CorrelationData correlationData1 = new CorrelationData("1");
        String routintKey = "key1";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,routintKey,message+routintKey,correlationData1);
        log.info("发送的消息是：{}",message);

        //指定消息id为2
        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME,routintKey+123,message+routintKey,correlationData2);
        log.info("发送的消息是：{}",message);
    }
}
