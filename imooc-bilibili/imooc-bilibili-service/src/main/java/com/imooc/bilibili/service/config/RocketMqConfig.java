package com.imooc.bilibili.service.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibili.domain.UserFollowing;
import com.imooc.bilibili.domain.UserMoment;
import com.imooc.bilibili.domain.constant.UserMomentsConstant;
import com.imooc.bilibili.service.UserFollowingService;
import com.imooc.bilibili.service.websocket.TestWebSocketService;
import com.imooc.bilibili.service.websocket.WebSocketService;
import io.netty.util.internal.StringUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xgp
 * 时间：2024/4/22
 * 描述：
 */
@Configuration
public class RocketMqConfig {

    @Value("${rocketmq.name.server.address}")
    private String nameServerAddr;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserFollowingService userFollowingService;


    //生产者
    @Bean("momentsProducer")
    public DefaultMQProducer momentsProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_MOMENTS);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        return producer;
    }

    //消费者
    @Bean("momentsConsumer")
    public DefaultMQPushConsumer momentsConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_MOMENTS);
        consumer.setNamesrvAddr(nameServerAddr);
        consumer.subscribe(UserMomentsConstant.TOPIC_MOMENTS,"*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt msg = msgs.get(0);
                if(msg == null){
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

                String bodyStr = new String(msg.getBody());

                //反序列化对象
                UserMoment userMoment = JSONObject.toJavaObject(JSONObject.parseObject(bodyStr), UserMoment.class);

                Long userId = userMoment.getUserId();

                List<UserFollowing> userFollingList = userFollowingService.getUserFans(userId);
                for(UserFollowing fans : userFollingList){
                    String key = "subscribed-" + fans.getUserId();
                    String subscribeListStr = redisTemplate.opsForValue().get(key);
                    List<UserMoment> subscribeList;
                    if(StringUtil.isNullOrEmpty(subscribeListStr)){
                        subscribeList = new ArrayList<>();
                    }else{
                        subscribeList = JSONArray.parseArray(subscribeListStr, UserMoment.class);
                    }

                    subscribeList.add(userMoment);
                    redisTemplate.opsForValue().set(key, JSONObject.toJSONString(subscribeList));
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        return consumer;

    }

    //生产者
    @Bean("danmuProducer")
    public DefaultMQProducer danmuProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_DANMUS);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        return producer;
    }

    //消费者
    @Bean("danmuConsumer")
    public DefaultMQPushConsumer danmuConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_DANMUS);
        consumer.setNamesrvAddr(nameServerAddr);
        consumer.subscribe(UserMomentsConstant.TOPIC_DANMUS,"*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt msg = msgs.get(0);
                byte[] msgByte = msg.getBody();
                String bodyStr = new String(msgByte);
                JSONObject jsonObject = JSONObject.parseObject(bodyStr);

                String sessionId = jsonObject.getString("sessionId");
                String message = jsonObject.getString("message");

                WebSocketService webSocketService = WebSocketService.WEBSOCKET_MAP.get(sessionId);
                if(webSocketService.getSession().isOpen()){
                    try{
                        System.out.println(sessionId+":"+"消费一条消息");
                        //同一个会话的多个弹幕被多个线程拿到，会出现多个线程利用相同的websocket发送消息
                        //因此，这里要做同步处理
                        synchronized(webSocketService){
                            webSocketService.sendMessage(message);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        return consumer;

    }

//    //生产者
//    @Bean("danmuProducer")
//    public DefaultMQProducer danmuProducer() throws Exception {
//        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_DANMUS);
//        producer.setNamesrvAddr(nameServerAddr);
//        producer.start();
//        return producer;
//    }
//
//    //消费者
//    @Bean("danmuConsumer")
//    public DefaultMQPushConsumer danmuConsumer() throws Exception {
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_DANMUS);
//        consumer.setNamesrvAddr(nameServerAddr);
//        consumer.subscribe(UserMomentsConstant.TOPIC_DANMUS,"*");
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                MessageExt msg = msgs.get(0);
//                byte[] msgByte = msg.getBody();
//                String bodyStr = new String(msgByte);
//                JSONObject jsonObject = JSONObject.parseObject(bodyStr);
//                String seesionId = jsonObject.getString("seesionId");
//                String message = jsonObject.getString("message");
//                WebSocketService webSocketService = WebSocketService.WEBSOCKET_MAP.get(seesionId);
//                if(webSocketService.getSession().isOpen()){
//                    try{
//                        webSocketService.sendMessage(message);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//
//
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
//
//        consumer.start();
//        return consumer;
//
//    }
}
