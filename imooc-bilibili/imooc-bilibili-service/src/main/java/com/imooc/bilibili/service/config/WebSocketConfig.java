package com.imooc.bilibili.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 作者：xgp
 * 时间：2024/5/6
 * 描述：
 */

@Configuration
public class WebSocketConfig {


    //用来发现webSocket服务，是WebSocket实现的前提
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }




}
