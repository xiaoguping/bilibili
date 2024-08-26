package com.imooc;

import com.imooc.bilibili.service.websocket.TestWebSocketService;
import com.imooc.bilibili.service.websocket.WebSocketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 作者：xgp
 * 时间：2024/4/18
 * 描述：
 */
@SpringBootApplication()


//开启定时任务
@EnableScheduling
//开启异步功能
@EnableAsync
public class ImoocBilibiliApp {
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(ImoocBilibiliApp.class,args);
        WebSocketService.setApplicationContext(app);
        TestWebSocketService.setApplicationContext(app);
    }
}
