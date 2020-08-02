# 从零开始搭建java后端全技术栈（悟空商城-秒杀）
![image](https://github.com/mambo-wang/WuKong/blob/master/doc/wukong.jpg)
### 悟空，齐天大圣，七十二般变化，无所不能

## 点赞趋势

[![Stargazers over time](https://starchart.cc/mambo-wang/WuKong.svg)](https://starchart.cc/mambo-wang/WuKong)
#### 文件目录
- doc：项目文档
- wukong-common：通用配置、工具类、常量类、model、统一异常处理、dubbo api
- wukong-consumer：商品、秒杀接口、dubbo服务提供者、rmq消费者、kafka消息生产者
- wukong-provider：订单、用户、支付接口、dubbo服务消费者、rmq生产者消费者、seata分布式事务发起者
- wukong-scripture：mongoDB日志接口、kafka消息消费者、算法等基础技能演练场
- wukong-west-sky：sh安装脚本、dockerfile、docker-compose配置文件
- xxl-job-admin：xxl-job开源任务调度框架的调度中心服务
#### 包含组件
- dubbo + zookeeper 服务间远程调用，服务注册。
- redis高性能缓存、分布式锁、计数器。
- rabbitMQ异步消息队列实现点对点、发布订阅；最大努力通知分布式事务方案
- mybatis数据层 + sharding-jdbc分库分表、读写分离。
- poi表格操作工具，提供傻瓜式接口实现数据导入导出（接受过多个生产环境考验）
- aop + redis + 自定义注解 实现接口限流、日志记录
- kafka + aop + mongodb 实现操作日志传递、入库
- 德鲁伊druid数据库连接池
- 阿里巴巴seata  at/tcc分布式事务
- jsoup + 邮件集成 + 观察者模式实现凤凰网新闻爬取、订阅、推送
- xxl-job 分布式任务调度
#### todo list
- [集成Hystrix实现服务集群容错降级](https://www.bilibili.com/video/BV1zt411M7pF?p=23)

