package com.ldx.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Uaena
 * @date 2023/7/31 22:10
 */
@Component
@Slf4j
public class RabbitMQTransaction  implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{
    /**
     *  交换机是否收到消息的一个回调方法
     *
     * @param correlationData  消息相关数据
     * @param ack              交换机是否收到消息
     * @param s                失败 的原因，成功的话为null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        String id = correlationData!=null?correlationData.getId():"";
        if (ack){
            log.info("交换机已收到id为:{}",id);
        }else{
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}",id,s);
        }
    }

    /**
     * 只有当消息传递过程中不可达目的地时，将消息返回给生产者
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息：{},被交换机:{}退回,退回原因:{},路由key:{}",returnedMessage.getMessage(),returnedMessage.getExchange(),returnedMessage.getReplyText(),returnedMessage.getRoutingKey());
    }
}
