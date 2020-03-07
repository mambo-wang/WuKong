# 从零开始搭建java后端全技术栈

###悟空，齐天大圣，七十二般变化，无所不能
#### 包含组件

- dubbo + zookeeper 服务间远程调用

- redis高性能缓存

- rabbitMQ异步消息队列

- mybatis数据层

- poi表格操作工具（接受过多个生产环境考验）

- aop + redis + 自定义注解 实现接口限流
#### todo list

-  邮件集成
-  kafka集成
- 实用工具类
- 等等


## 一、 Dubbo集成
### 1. dubbo核心工作原理

![dubbo核心工作原理](https://raw.githubusercontent.com/Baijq/others/master/images/dubbo-img/dubbo-3.png)

节点 | 角色说明
--|--
Provider|暴露服务的服务提供方
Registry|服务的注册与发现的注册中心，如zookeper(推荐)、multicast、redis、simple
Consumer|调用远程服务的服务消费方
Monitor|统计服务的调用次数和调用时间的监控中心
Container|服务运行容器

#### 调用流程

- 服务器负责启动，加载，运行服务提供者。

- 服务提供者在启动时，向注册中心注册自己所提供的服务

- 服务消费者在启动时，向注册中心订阅自己所需的服务

- 注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于TCP长连接推送变更数据给消费者

- 服务消费者从提供的服务列表中，基于软负载均衡算法，选一台提供者进行调用，如果失败，则选择另一台调用

- 服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到检测中心

### 2. dubbo特点

1. Dubbo 支持 RPC 调用，服务之间的调用性能会很好，效率很高

2. 支持多种序列化协议，如 Hessian(默认)、HTTP、WebService

3. 对比springcloud

![dubbo和SpringCloud对比](https://raw.githubusercontent.com/Baijq/others/master/images/dubbo-img/dubbo-4.png)

## 二、 springboot-Dubbo搭建

想要使用Dubbo的话，按照前面的原理图，我们主要使用它的服务远程调用功能，也就是两个项目之间相互通讯，即RPC

直接上图，先大致看看项目结构：

![dubbo示例项目结构](https://raw.githubusercontent.com/Baijq/others/master/images/dubbo-img/dubbo%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84.png)

dubbo-provider是所谓的服务提供者，springboot项目 

dubbo-consumer是服务消费者，springboot项目

dubbo-api是服务提供者的接口API，最普通的maven项目

>这里可以会想起dubbo工作原理的图：

- 0 dubbo-provider先在容器里初始化启动
- 1 dubbo-provider去注册中心去注册服务，注册中心使用zookeeper
- 2 dubbo-consumer去注册中心发现需要的服务
- 3 注册中心返回dubbo-provider的服务
- 4 dubbo-consumer直接去调用dubbo-provider

