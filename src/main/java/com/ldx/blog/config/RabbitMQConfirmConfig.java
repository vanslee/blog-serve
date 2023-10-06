package com.ldx.blog.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Uaena
 * @date 2023/7/31 22:02
 */
@Configuration
public class RabbitMQConfirmConfig {

    public static final String CONFIRM_EXCHANGE_NAME = "BLOG.email.confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "BLOG.email.confirm.queue";

    public static final String BACKUP_EXCHANGE_NAME = "BLOG.email.backup.exchange";
    public static final String BACKUP_QUEUE_NAME = "BLOG.email.backup.queue";
    public static final String WARNING_QUEUE_NAME = "BLOG.email.warning.queue";
    public static final String CONFIRM_ROUTE_KEY_NAME = "EMAIL";

    //声明确定队列
    @Bean("confirmQueue")
    public Queue queue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //声明业务交换机
    @Bean("confirmExchange")
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true).withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();
    }

    //绑定确定队列和业务交换机
    @Bean
    public Binding bindingQueueToExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                          @Qualifier("confirmExchange")DirectExchange confirmExchange){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTE_KEY_NAME);
    }

    //声明备份 Exchange
    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //声明备份队列
    @Bean("backQueue")
    public Queue backQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }
    //声明报警队列
    @Bean("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //声明备份队列和 备份交换机之间的绑定关系
    @Bean
    public Binding backupBinding(@Qualifier("backQueue") Queue queue,
                                 @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(queue).to(backupExchange);
    }

    //声明报警队列和 备份交换机之间的绑定关系
    @Bean
    public Binding warningBinding(@Qualifier("warningQueue") Queue queue,
                                  @Qualifier("backupExchange") FanoutExchange
                                          backupExchange){
        return BindingBuilder.bind(queue).to(backupExchange);
    }
}
