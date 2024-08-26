## 仿B站项目启动流程

### 便捷启动

查看本机ip地址

将/Users/xgp/Desktop/env/rocketmq-4.9.3/conf/broker_docker.conf中的

> namesrvAddr=ip:9876
>
> brokerIP1= ip 
>
> rocketmq.config.namesrvAddr = ip:9876 (rocketmq-consle项目)

#### 启动es，kibana，redis，rocketmq

​	进入dokcer，打开相关项目

#### 启动后端项目

#### 启动前端项目

​	进入终端地址：/Users/xgp/Downloads/imooc-bilibili-master

​	启动命令：npm run serve

---

### 原始启动：

#### redis启动/关闭

​	redis-server	redis-cli shutdown

#### rocketmq

​	进入终端地址：/Users/xgp/Desktop/env/rocketmq-4.9.3/bin

​	启动名称服务器：sh mqnamesrv

​	启动跟踪服务器：sh mqbroker -n localhost:9876 autoCreateTopicEnable=true
