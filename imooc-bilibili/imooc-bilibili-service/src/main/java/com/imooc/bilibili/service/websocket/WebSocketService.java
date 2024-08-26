package com.imooc.bilibili.service.websocket;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibili.domain.Danmu;
import com.imooc.bilibili.domain.Exception.ConditionException;
import com.imooc.bilibili.domain.constant.UserMomentsConstant;
import com.imooc.bilibili.service.DanmuService;
import com.imooc.bilibili.service.util.RocketMqUtil;
import com.imooc.bilibili.service.util.TokenUtil;
import io.netty.util.internal.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者：xgp
 * 时间：2024/5/6
 * 描述：
 */

@Component
@ServerEndpoint("/imserver/{token}")  //被此注解标注的类表示它是一个与websocket相关的服务类
public class WebSocketService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //记录当前的长连接记录
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);


    //WebSocket在服务器内部是多例模式，使用Map来保存每个服务器对应的websocket服务
    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    private static final Object lock = new Object();  // 同步锁对象

    //Session是WebSocket内部，客户端和服务端的通信方式
    private Session session;

    private String sessionId;

    private Long userId;


    //全局上下文，所有WebSocketService公用
    private static ApplicationContext APPLICATION_CONTEXT;



    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    //连接成功时，会调用此注解标注的方法
    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token){
        try{
            this.userId = TokenUtil.verifyToken(token);
        }catch (Exception e){

        }

        sessionId = session.getId();
        this.session = session;

        if(WEBSOCKET_MAP.contains(sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            WEBSOCKET_MAP.put(sessionId,this);
        }else{
            WEBSOCKET_MAP.put(sessionId,this);
            //在线人数加1
            ONLINE_COUNT.getAndIncrement();
        }

        logger.info("客户端"+sessionId+"连接成功"+"  当前在线人数为："+ONLINE_COUNT.get());

    }


    @OnClose
    public void closeConnection(){
        if(WEBSOCKET_MAP.containsKey(sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();
        }
        logger.info("客户端"+sessionId+"退出"+"  当前在线人数为："+ ONLINE_COUNT.get());
    }

    @OnMessage
    public void onMessage(String message){
        System.out.println("onMessage:"+message);

        logger.info("用户信息："+ sessionId + ",报文：" + message );
        if(!StringUtil.isNullOrEmpty(message)) {
            try {
                //将接收到的弹幕，群发到所有在线的客户端
                for (Map.Entry<String, WebSocketService> entry : WEBSOCKET_MAP.entrySet()) {
                    WebSocketService webSocketService = entry.getValue();
                    DefaultMQProducer danmuProducer = (DefaultMQProducer) APPLICATION_CONTEXT.getBean("danmuProducer");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", message);
                    jsonObject.put("sessionId", webSocketService.getSessionId());
                    Message msg = new Message(UserMomentsConstant.TOPIC_DANMUS, JSONObject.toJSONString(jsonObject).getBytes(StandardCharsets.UTF_8));
                    RocketMqUtil.syncSendMsg(danmuProducer, msg);
                }


                //如果是登陆用户发送的弹幕，则保存到redis、mysql
                //游客的弹幕不会做任何保存,也不会更新在前端页面
                if (this.userId != null) {


                    Danmu danmu = JSONObject.parseObject(message, Danmu.class);
                    danmu.setUserId(userId);
                    danmu.setCreateTime(new Date());

                    //保存弹幕到mysql
                    //保存弹幕到redis
                    DanmuService danmuService = (DanmuService) APPLICATION_CONTEXT.getBean("danmuService");

                    danmuService.asyncAddDanmu(danmu);

                    danmuService.addDanmusToRedis(danmu);

                }

                //告诉前端，弹幕发送成功
                this.sendMessage("3");


            } catch (Exception e) {
                logger.info("弹幕接收出现问题！");
                e.printStackTrace();
            }
        }

    }

//    @Transactional
//    //保存弹幕到mysql
//    //保存弹幕到redis
//    public void saveDanmu(Danmu danmu) {
//        DanmuService danmuService = (DanmuService) APPLICATION_CONTEXT.getBean("danmuService");
//        // 保存弹幕到 MySQL
//        danmuService.asyncAddDanmu(danmu);
//        // 保存弹幕到 Redis
//        danmuService.addDanmusToRedis(danmu);
//    }

    @OnError
    public void onError(Throwable error){

    }


    //定时任务，每5秒刷新在线人数
//    @Scheduled(fixedRate = 5000)
    private void noticeOnlineCount() throws IOException{
        for(Map.Entry<String, WebSocketService> entry : WEBSOCKET_MAP.entrySet()){
            WebSocketService webSocketService = entry.getValue();
            if(webSocketService.session.isOpen()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("onlineCount",ONLINE_COUNT.get());
                jsonObject.put("msg","当前在线人数为" + ONLINE_COUNT.get());
                webSocketService.sendMessage(jsonObject.toJSONString());
            }
        }
    }

    public void sendMessage(String message) throws IOException {

        this.session.getBasicRemote().sendText(message);
    }


    public Session getSession() {
        return this.session;
    }

    public String getSessionId() {
        return sessionId;
    }

}
