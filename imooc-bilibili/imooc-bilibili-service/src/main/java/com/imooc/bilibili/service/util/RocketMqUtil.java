package com.imooc.bilibili.service.util;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 作者：xgp
 * 时间：2024/4/22
 * 描述：
 */
public class RocketMqUtil {

    public static void syncSendMsg(DefaultMQProducer producer, Message msg) throws Exception {
        SendResult result = producer.send(msg);
       System.out.println(result);
    }

    public static void asyncSendMsg(DefaultMQProducer producer, Message msg) throws Exception {
        int messageCount = 2;
        CountDownLatch latch = new CountDownLatch(messageCount);
        for(int i = 0; i < messageCount; i++) {
            String tempString = new String(msg.getBody()) + i ;
            Message message = new Message(msg.getTopic(), msg.getTags(), tempString.getBytes());
            producer.send(message,new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    latch.countDown();
                    System.out.println(sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    latch.countDown();
                    System.out.println("发送消息的时候发生了异常！"+e);
                    e.printStackTrace();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
    }


}
