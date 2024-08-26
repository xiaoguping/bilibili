package com.imooc.bilibili.service.websocket;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibili.domain.constant.UserMomentsConstant;
import com.imooc.bilibili.service.config.WebSocketConfig;
import com.imooc.bilibili.service.util.RocketMqUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 作者：xgp
 * 时间：2024/8/14
 * 描述：
 */

@Component
@ServerEndpoint("/testWebSocketService")
public class TestWebSocketService {
    private Session session;

    private String sessionId;

    private Long userId;

    private static ApplicationContext APPLICATION_CONTEXT;

    public static final ConcurrentHashMap<String, TestWebSocketService> map = new ConcurrentHashMap<>();

    public static void setApplicationContext(ApplicationContext applicationContext) {
        TestWebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    @OnOpen
    public void onConnection(Session session) {
        this.session = session;
        sessionId = session.getId();

        if(map.contains(sessionId)){
            map.remove(sessionId);
            map.put(sessionId,this);

        }else {
            map.put(sessionId, this);
        }

        System.out.println("新的websocket生成:"+ sessionId);
    }

    @OnClose
    public void closeConnection(){
        if(map.containsKey(sessionId)){
            map.remove(sessionId);
        }
        System.out.println("一个websocket退出:" + sessionId);
    }


    @OnMessage
    public void onMessage(String message) throws Exception {
        for (Map.Entry<String, TestWebSocketService> entry : map.entrySet()) {
            TestWebSocketService testWebSocketService = entry.getValue();
            DefaultMQProducer danmuProducer = (DefaultMQProducer) APPLICATION_CONTEXT.getBean("danmuProducer");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", message);
            jsonObject.put("sessionId", testWebSocketService.getSessionId());
            Message msg = new Message(UserMomentsConstant.TOPIC_DANMUS, JSONObject.toJSONString(jsonObject).getBytes(StandardCharsets.UTF_8));
            RocketMqUtil.syncSendMsg(danmuProducer, msg);
        }


    }

    public String getSessionId() {
        return sessionId;
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    public Session getSession() {
        return this.session;
    }

}
