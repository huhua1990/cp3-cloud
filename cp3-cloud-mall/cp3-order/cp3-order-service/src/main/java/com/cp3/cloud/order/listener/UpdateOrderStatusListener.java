package com.cp3.cloud.order.listener;

import com.cp3.cloud.comments.pojo.Review;
import com.cp3.cloud.order.mapper.OrderStatusMapper;
import com.cp3.cloud.order.pojo.OrderStatus;
import com.cp3.cloud.order.service.OrderService;
import com.cp3.cloud.order.service.OrderStatusService;
import com.cp3.cloud.order.vo.CommentsParameter;
import com.cp3.cloud.order.vo.OrderStatusMessage;
import com.cp3.cloud.common.utils.JsonUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: 98050
 * @Time: 2018-12-10 23:12
 * @Feature: 自动修改订单状态：自动确认收货，自动评价
 */
@Component
public class UpdateOrderStatusListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderStatusService orderStatusService;


    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "cp3.order.delay.ttl.queue",durable = "true"), //队列持久化
            exchange = @Exchange(
                    value = "cp3.amq.direct",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"cp3_ttl_orderStatus"}
    ))
    public void listenOrderDelayMessage(byte[] str){
        OrderStatusMessage orderStatusMessage = JsonUtils.parse(new String(str), OrderStatusMessage.class);
        if (orderStatusMessage == null){
            return;
        }
        int type = orderStatusMessage.getType();

        if (type == 1){
            //自动确认收货，时间为7天

            //1.查询当前订单状态
            int status = orderService.queryOrderStatusById(orderStatusMessage.getOrderId()).getStatus();
            int nowStatus = 4;
            if (status + 1 == nowStatus){
                //2.修改订单状态
                updateOrderStatusDelay(orderStatusMessage.getOrderId(), nowStatus);

            }
        }else {
            //自动好评，时间为5天
            //1.查询当前订单状态
            int status = orderService.queryOrderStatusById(orderStatusMessage.getOrderId()).getStatus();
            int nowStatus = 6;
            if (status + 2 != nowStatus){
                return;
            }
            //2.修改订单状态
            updateOrderStatusDelay(orderStatusMessage.getOrderId(), nowStatus);
            //3.发送评论消息
            CommentsParameter commentsParameter = constructMessage(orderStatusMessage);

            this.orderStatusService.sendComments(commentsParameter);
        }
    }

    private CommentsParameter constructMessage(OrderStatusMessage orderStatusMessage) {
        Long spuId = orderStatusMessage.getSpuId();
        String content = "默认好评";
        Long userId = orderStatusMessage.getUserId();
        String nickname = orderStatusMessage.getUsername();
        List<String> images = new ArrayList<>();
        boolean iscomment = true;
        String parentId = 0 + "";
        boolean isparent = true;
        int commentType = 1;
        Review review = new Review(orderStatusMessage.getOrderId()+"",spuId+"", content, userId+"", nickname, images, iscomment, parentId,isparent,commentType);
        CommentsParameter commentsParameter = new CommentsParameter();
        commentsParameter.setOrderId(orderStatusMessage.getOrderId());
        commentsParameter.setReview(review);
        return commentsParameter;
    }


    private void updateOrderStatusDelay(Long orderId, int nowStatus) {

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(nowStatus);
        if (nowStatus == 4){
            orderStatus.setEndTime(new Date());
        }
        if (nowStatus == 6){
            orderStatus.setCommentTime(new Date());
        }

        this.orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }


}
