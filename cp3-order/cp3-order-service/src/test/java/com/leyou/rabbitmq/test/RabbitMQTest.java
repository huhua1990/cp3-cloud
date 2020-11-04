//package com.cp3.cloud.rabbitmq.test;
//
//import com.cp3.cloud.order.LyOrderApplication;
//import com.cp3.cloud.order.service.OrderStatusService;
//import com.cp3.cloud.order.vo.OrderStatusMessage;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @Author: 98050
// * @Time: 2018-12-11 10:54
// * @Feature:
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = LyOrderApplication.class)
//public class RabbitMQTest {
//
//    @Autowired
//    private OrderStatusService orderStatusService;
//
//    @Test
//    public void sendMessage(){
//        OrderStatusMessage orderStatusMessage = new OrderStatusMessage();
//        orderStatusMessage.setOrderId(123L);
//        orderStatusMessage.setType(1);
//        orderStatusMessage.setUserId(111L);
//        orderStatusService.sendMessage(orderStatusMessage);
//    }
//}
