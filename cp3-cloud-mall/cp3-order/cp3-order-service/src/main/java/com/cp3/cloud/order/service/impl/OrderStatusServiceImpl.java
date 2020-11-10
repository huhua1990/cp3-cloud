package com.cp3.cloud.order.service.impl;

import com.cp3.cloud.order.service.OrderStatusService;
import com.cp3.cloud.order.vo.CommentsParameter;
import com.cp3.cloud.order.vo.OrderStatusMessage;
import com.cp3.cloud.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 98050
 * @Time: 2018-12-10 23:24
 * @Feature:
 */
@Service
public class OrderStatusServiceImpl implements OrderStatusService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusServiceImpl.class);


    /**
     * 发送延时消息到延时队列中
     * @param orderStatusMessage
     */
    @Override
    public void sendMessage(OrderStatusMessage orderStatusMessage) {
        String json = JsonUtils.serialize(orderStatusMessage);
        MessageProperties properties;
        if (orderStatusMessage.getType() == 1){
            // 持久性 non-persistent (1) or persistent (2)
            properties = MessagePropertiesBuilder.newInstance().setExpiration("60000").setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        }else {
            properties = MessagePropertiesBuilder.newInstance().setExpiration("90000").setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        }

        Message message = MessageBuilder.withBody(json.getBytes()).andProperties(properties).build();
        //发送消息
        try {
            this.amqpTemplate.convertAndSend("", "cp3.order.delay.queue", message);
        }catch (Exception e){
            LOGGER.error("延时消息发送异常，订单号为：id：{}，用户id为：{}",orderStatusMessage.getOrderId(),orderStatusMessage.getUserId(),e);
        }
    }

    /**
     * 将评论发送到消息队列中
     * @param commentsParameter
     */
    @Override
    public void sendComments(CommentsParameter commentsParameter) {
        String json = JsonUtils.serialize(commentsParameter);
        try {
            this.amqpTemplate.convertAndSend("cp3.comments.exchange","user.comments", json);
        }catch (Exception e){
            LOGGER.error("评论消息发送异常，订单id：{}",commentsParameter.getOrderId(),e);
        }
    }
}
