package com.ldx.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.ldx.blog.config.RabbitMQConfirmConfig.WARNING_QUEUE_NAME;

/**
 * @author Uaena
 * @date 2023/7/31 22:13
 */
@Component
@Slf4j
public class RabbitMQWarningConsumer {


    @RabbitListener(queues = WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由消息：{}", msg);
    }
}
