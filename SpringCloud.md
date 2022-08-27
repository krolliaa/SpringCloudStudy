---
title: SpringCloud Study
categories: 
- Framework
tags: 
- 框架
- SpringCloud
---

# `SpringCloud`

## 1. 微服务

微服务技术不等于`SpringCloud`它们两个属于包含关系。下列包括记录员【注册中心】 + 管理员【配置中心】 + 安全员【服务网关】 + 守护员【分布式缓存】 + 侦察员【分布式搜索】 + 存储员【消息队列】 + 监察员`A`【分布式日志服务】 + 监察员`B`【分系统监控链路追踪 】 + 一群服务员【`Jenkins` + `Docker` + `K8S` + `RANCHER`】

- 微服务首先要做的第一件事情就是拆分，根据功能从单体中拆分一个一个模块，从而形成一个<font color="pink">【服务集群】</font>

- 拆分出来的模块可能有好几千个，想光靠人去处理效率非常低，所以就有了<font color="pink">【注册中心】</font>，它相当于一个记录员，记录了每一个模块的`IP`还有端口以及各个模块都有什么功能，当有一个模块想调用另外一个模块的功能的时候，它不需要自己去记录对方模块的`IP`、端口等信息只需要去找注册中心这个记录员就好了【拉取或注册服务信息】。

- 每一个服务或者你叫它模块也可以都有一个相对应的配置文件，这么多的模块成千上万个，难道要修改配置的时候还要从`IDEA`一个个去翻一个个去找吗？当然不是，这时候就需要有一个<font color="pink">【配置中心】</font>，它相当于一个管理员，管理配置的，如果某天你想更改某个服务的配置只需要到配置中心去改就好了，不用一个个去找这么麻烦，这个管理员它会去通知相关的服务更新配置且热更新【修改即改不用重启服务】

- 有了各个服务再加上有了记录员注册中心以及管理员配置中心，此时用户其实就可以访问服务集群里的服务了，但是用户可不可以访问以及怎么知道该访问的服务在哪里呢？总不能随便让人进来访问我的服务，万一这人是来捣乱或者图谋不轨的怎么办？所以此时就需要一个安全员，也就是<font color="pink">【服务网关】</font>，一方面对用户身份做校验，并且可以把用户的请求路由到各个服务模块，并且可以在此过程中做一些负载均衡，服务模块处理然后把数据返回给用户

- 到现在看似好像都没什么问题了？但是有一个问题，就是万一用户的数量太多太多了都去访问数据库，万一数据库崩了怎么办？所以呢为了保险起见，我们需要一个保护数据库任务的守护员，这个守护员就是<font color="pink">【分布式缓存】</font>，用户来了先去访问这个分布式缓存，未命中再去访问数据库，分担了数据库不小的压力

- 现在好了吧，我有了服务集群、记录员注册中心、管理员配置中心、还有安全员服务网关以及守护员分布式缓存保护数据库，你没话讲了吧，不，我还有话讲，万一你这个要搜索要获取非常非常海量数据的满足特定条件的一两条数据，你该怎么办？啊这...所以就要还要有侦察员<font color="pink">【分布式搜索】</font>，记录员 + 管理员 + 安全员 + 守护员 + 侦察员，各司其职形成了微服务架构

- 到这就真的完毕了吗？假设现在某个用户访问了`A`模块，然后又访问了`B`模块，一直一直这样访问到`Z`模块，那么这条访问链路不用想那是相当相当的长，这边用户等的就可捉急了，咋办？此时就要引入转储员<font color="pink">【消息队列】</font>，模块`A`只需要去通知各个模块完成他们的功能即可，然后`A`模块直接就完成了它的任务返回给了用户，这就大大缩短了响应时间，可以处理秒杀高并发

- 再来思考一个问题？如此庞大的一个服务架构，万一某一处出了点问题，好排查吗？当然不好排查。所以就需要有一个实时监视这整个服务的监察员`A`<font color="pink">【分布式日志服务】</font>，它可以去统一存储统计分析整个服务的日志还有我们的监察员`B`<font color="pink">【系统监控链路追踪】</font>，它可以去实时监视每一个服务节点的运行状态，`CPU`占用信息等

- 最后这么庞大的一个微服务集群，人工部署是不是非常的麻烦比较效率低下呢？这时候就要请一群服务员了，让他们去帮我们自动化部署整个项目，持续集成<font color="pink">【`Jenkins`、`Docker`、`K8S`、`RANCHAER`】</font>

  ​	![](https://img-blog.csdnimg.cn/67a95be4c2724b8bae997f029147f87f.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

## 2. 如何学习

微服务篇可以看到整个微服务也忒复杂了，把这个学好出去岂不是横着走？那么这么复杂的微服务该如何学习呢？

重要的就是练习练习再练习，达到熟练的程度

分九步走【实用篇`1-4` + 高级篇`5-9`】：

1. 微服务治理：`Eureka`、`Nacos`、`OpenFeign`、网关`GateWay`<font color="red">**【具体看：`6 7 8 9 10 11 12`】**</font>
2. `Docker`：`Docker原理`、`Docker使用`、`Dockerfile`、`DockerCompose`<font color="red">**【具体看：`13`】**</font>
3. 异步通信：同步和异步、`MQ`技术选型、`SpringAMQP`、消费者限流<font color="red">**【具体看：`14`】**</font>
4. 分布式搜索：`DSL`语法、`HighLevelClient`、拼音搜索、自动补全、竞价排名、地理搜索、聚合统计、分片集群<font color="red">**【具体看：`15`】**</font>
5. 微服务保护：流量控制、系统保护、熔断降级、服务授权
6. 分布式事务：`XA`模式、`TCC`模式、`AT`模式、`Saga`模式
7. 分布式缓存：数据持久化、`Redis`主从集群、哨兵机制、`Redis`分片集群
8. 多级缓存：多级缓存分层、`Nginx`缓存、`Redis`缓存、`Canal`数据同步
9. 可靠消息服务：消息三方确认、惰性队列、延迟队列、镜像集群、仲裁队列

## 3. 微服务架构的演变

> -  单体架构：将业务所有的功能集中在一个项目中开发，打包部署即可
>    - 优点：架构简单 + 部署成本低
>    - 缺点：耦合度高 + 一旦项目很大打包时间也将非常长
>
> <hr>
>
>
> - 分布式架构：根据业务功能对系统进行拆分，每个业务模块作为独立项目开发，称为一个服务
>   - 优点：降低服务耦合 + 有利于服务升级扩展
>   - 问题：分布式需要考虑许多问题，比如访问量大的时候并且放置服务器故障你得做个集群然后你得做远程过程调用`RPC`，服务拆分粒度如何？服务集群地址如何维护？服务之间如何实现远程调用`RPC`？服务健康状态如何感知？
>
> <hr>
>
>
> - 如何解决分布式上面所阐述的问题，当前最好的解决方案就是 ---> 微服务【还是分布式架构】
>   - 微服务是一种经过良好架构设计的分布式架构方案，微服务机构特征：
>     - 单一职责：微服务拆分粒度更小，每一个服务都对应唯一的业务能力，做到单一职责避免重复业务开发
>     - 面向服务：微服务对外暴露业务接口
>     - 自治：符合敏捷开发的思路，团队独立、技术独立、数据独立【有自己独立的数据库】、部署独立【用户可以根据自己的需求去访问】
>     - 隔离性强：服务调用做好隔离、容错、降级、避免出现级联问题

总结：

1. 单体架构特点：简单方便、高度耦合、扩展性差，适合小型项目，比如：学生管理系统
2. 分布式架构特点：松耦合、扩展性好但是架构复杂、难度大，适合大型项目，比如：京东、淘宝
3. 微服务：一种良好的分布式架构方案
   - 优点：拆分粒度更小、服务更独立、耦合度更低
   - 缺点：架构非常复杂、运维、监控、部署难度提高

在国内常用微服务方案落地实现的具体技术框架有：`SpringCloud` + `Dubbo` + `SpringCloud Alibaba`

## 4. 微服务技术对比和企业常用

|                |            `Dubbo`             | `SpringCloud`【整合，只支持`Feign`方式】 | `SpringCloud Alibaba`【越来越火，重点，支持`Feign`又支持`Dubbo`】 |
| -------------- | :----------------------------: | :--------------------------------------: | :----------------------------------------------------------: |
| 注册中心       | `Zookeeper`、`Redis`【别人的】 |              Eureka、Consul              |                     `Nacos`】、`Eureka`                      |
| 远程过程调用   |      `Dubbo`协议【核心】       |           Feign（`http`协议）            |                       `Dubbo`、`Feign`                       |
| 配置中心       |               无               |           `SpringCloudConfig`            |                 `SpringCloudConfig`、`Nacos`                 |
| 服务网关       |               无               |       `SpringCloudGateway`、`Zuul`       |                 `SpringCloudGateway`、`Zuul`                 |
| 服务监控和保护 |     `dubbo-admin`、功能弱      |                `Hystrix`                 |                          `Sentinel`                          |

- 企业常用选型第一种：`SpringCloud + Feign`，使用`SpringCloud`技术栈，服务接口采用`Restful`风格，服务远程过程调用采用`Feign`方式

- 企业常用选型第二种：`SpringCloudAlibaba + Feign`，使用`SpringCloudAlibaba`技术栈，服务接口采用`Restful`风格，服务调用采用`Feign`方式

- 企业常用选型第三种：`SpringCloudAlibaba + Dubbo`，使用`SpringCloudAlibaba`技术栈，服务接口采用`Dubbo`协议标准，服务远程过程调用采用`Dubbo`方式

- 企业常用选型第四种：`Dubbo`原始模式，基于`Dubbo`老旧技术体系，服务接口采用`Dubbo`协议标准，服务远程过程调用采用`Dubbo`方式

  ![](https://img-blog.csdnimg.cn/7bb5d2a2acee45daae5fdc7165cacafc.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

## 5. 初步认识`SpringCloud`

`SpringCloud`是目前国内甚至可以说是全球使用最广泛的微服务架构，集成了各种微服务软件功能组件，并基于`SpringBoot`实现了这些组件的自动装配，开箱即用。

![](https://img-blog.csdnimg.cn/fade331c62d2415391bf3d8347260c12.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

`SpringCloud`为什么能够推广开来就是因为基于`SpringBoot`实现了许多组件的自动装配，这样拿过来就可以直接使用了，而不用去学习原生的方法，这就是为什么`SpringCloud`可以推广开来，本质上靠的是`SpringBoot`【人家是由很扎实的基础的】

`SpringCloud`和`SpringBoot`存在兼容性 问题，最佳适应版本需要前往`SpringCloud`的官网查看

本次学习使用的`SpringCloud`版本为：`Hoxton.SR10`，对应`SpringBoot 2.3.x`

## 6. 服务拆分和远程调用

注意事项：

1. 不同的微服务，不要重复开发相同业务，如果出现了那说明做的有问题
2. 微服务数据独立，不要访问其它服务的数据库，这就从根源上杜绝了耦合性的业务【分库分表】
3. 微服务可以将自己的业务暴露为接口，供其它服务使用

`Demo`：

项目结构：`cloud-demo` ---> `order-service`订单服务 + `user-service`用户服务

数据独立：`cloud-user.sql`[用户] + `cloud-order.sql`[订单]

1. 创建订单服务数据库：`cloud_order.database`[直接导入]
2. 创建用户服务数据库：`cloud_user.database`[直接导入]
3. 直接导入资源文件`cloud-demo`


如何实现远程服务调用？之前在前端我们可以通过`Ajax`查询出数据，那么在`order`这一段能不能通过`http`请求查询出`user`呢？显然该方案是可行的。

问题就成为了如何在`order`这一段发送`http`请求呢？`Spring`提供了一个工具可以用来发送`http`请求 ---> `RestTemplate` ---> 通过`RestTemplate`远程调用`http://localhost:8081/user/{userId}`获取到`User`将其赋予给`order-service`模块的`pojo.User`。【该项任务是业务处理的一部分所以放到`service`层中去写代码】

上述代码已放置`github`仓库：

![](https://img-blog.csdnimg.cn/ee944eb5299c44b58cb304e97d74053d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

## 7. 服务提供者和服务消费者

- 服务提供者：一次业务中，被其它微服务调用的服务（暴露接口给其它微服务调用）
- 服务消费者：一次业务中，调用其它微服务的服务（调用其它微服务提供的接口）
- 一个服务既可以是提供者也可以是消费者

## 8. `Eureka`注册中心

之前我们的`order`服务去调用`user`服务的时候采用的是硬编码的方式即直接写代码的方式：试想这种会造成什么问题？

![](https://img-blog.csdnimg.cn/a5909ac5b9f3400a9af7e04f64eabb9d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

1. 项目创建过程中有许多环境，生产环境测试环境开发环境，这些个`URL`地址肯定不一样，难道每换一个环境就要更改一次代码吗？这显然非常的麻烦

2. 现在这里只有一个`user-service`，端口是`8081`，试想如果`user-service`做了集群，除了`8081`还有`8082 8083`等等端口，那难道每次调用的时候都去更改代码吗？如果更改了代码，做集群还有意义吗？显然是没有的。而且万一挑选的那一台服务是不健康的挂了的呢？你如何确保可以准确的调用该接口呢？

那要怎么解决呢？`Eureka`注册中心解决这种硬编码带来的大问题：

1. `Eureka`注册中心将自身称为**<font color="red">`eureka-server`注册中心服务端</font>**，将其余的像`order-service`和`user-service`这些统称为是**<font color="red">`eureka-client`注册中心客户端</font>**。
2. 秉承一个服务既可能是服务调用者也可能是服务提供者的理念，所有的服务只要存在都会将自己的注册服务信息注册到`eureka-server`注册中心服务端中，该动作叫做**<font color="red">注册服务信息</font>**
3. 那如果一个服务挂了怎么办？不用担心，因为`eureka-server`有**<font color="red">心跳检测</font>**，**每隔`30s`，`eureka-client`注册中心客户端需要发送1次心跳**，注册中心服务端会更新注册服务列表，对不正常的服务信息进行删除，服务调用者可以拉取到最新的服务信息
4. 当某个服务调用者需要某个服务的时候就会从`eureka-server`注册中心服务端中调用服务，比如这里做了集群有三个`user-service`，都会拉取过来，该动作叫做**<font color="red">拉取服务提供者的信息</font>**
5. 拉取过来具体选哪个服务调用需要使用到**<font color="red">负载均衡技术</font>**
6. 确定了哪个服务之后`order-service`就会通过**<font color="red">远程调用</font>**调用某个端口的服务提供者`user-service`

​	![](https://img-blog.csdnimg.cn/90213662777c4cc2b6b6fa27816f4f72.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

问题描述及解答：

1. **<font color="deepskyblue">消费者该如何获取服务提供者的具体信息？</font>**
   - 服务【无论是服务提供者还是服务调用者`eureka-client`】启动时都会向注册中心服务端`eureka-server`注册服务信息，注册中心服务端会保存这些信息
   - 服务调用者即消费者会从注册中心服务端`eureka-server`拉取服务信息
2. **<font color="deepskyblue">如果有多个服务提供者，消费者该如何选择？</font>**
   - 消费者即服务调用者会通过负载均衡技术，从从注册中心服务端拉取过来的服务选择一个
3. **<font color="deepskyblue">消费者如何感知提供者健康状态？</font>**
   - 每隔`30s`服务提供者就会向`eureka-server`注册中心服务端发送心跳请求，报告当前健康状态
   - 对于不正常的服务信息，注册中心服务端`eureka-server`会将其从服务信息列表中删除
   - 服务调用者即消费者可以从注册中心服务端中拉取到最新的服务信息

【注：`eureka`也是一个微服务，需要注册`eureka`自己本身】

### 8.1 搭建`eureka-server`

1. 创建项目，配置`spring-cloud-starter-netflix-eureka-server`依赖

   ```xml
   <!--版本可以不写，因为在父pom已经编写好-->
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
   </dependency>
   ```

2. 编写启动类，添加`@EnableEurekaServer`注解

   ```java
   package com.zwm.eureka;
   
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
   
   @SpringBootApplication
   @EnableEurekaServer
   public class EurekaServiceApplication {
       public static void main(String[] args) {
           SpringApplication.run(EurekaServiceApplication.class, args);
       }
   }
   ```

3. 添加`application.properties`文件，编写该配置文件

   ```properties
   # 服务端口[任意]
   server.port=10086
   # 服务名称 - 微服务名称[为了做服务注册]
   spring.application.name=eureka-server
   # 配置eureka地址信息[eureka 也是一个微服务，把自己也注册在eureka身上][为了做服务注册]
   eureka.client.service-url.defaultZone=http://127.0.0.1:10086/eureka
   ```

4. 点击`IDEA`端口可以直接跳转到`eureka`管理页面，访问：`http://localhost:10086`[没有后缀]

   ![](https://img-blog.csdnimg.cn/fb33d846a96e4292a1855a60b8785267.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

### 8.2 搭建`eureka-client`

将`user-service`和`order-service`服务注册到`EurakaServer`：

1. 在`user-service`的`pom.xml`中引入依赖：`spring-cloud-starter-netflix-eureka-client`

   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
   	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
   </dependency>
   ```

2. 在`application.properties`配置注册服务：

   ```properties
   spring.application.name=user-service
   eureka.client.service-url.defaultZone=http://127.0.0.1:10086/eureka
   ```

- 按照上述步骤可以注册`order-service`，这里不再赘述

`IDEA`可以模拟启动多个服务：`ctrl+D`或者右键服务选择`	Copy Configuration`然后配置一下`VM options: -Dserver.port=8082` ---> `apply` ---> `ok`

![](https://img-blog.csdnimg.cn/57c02b3dbf9d43fba47d870f62418b4f.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

完成后`eureka-service`显示：

![](https://img-blog.csdnimg.cn/87a5506f10994a8f8274f1740c500cc0.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

到这里就完成了将服务注册到`eureka`

### 8.3. `Eureka` 服务发现

1. 修改源代码，将其改为`spring.application.name`的名称，这个服务已经在注册中心`eureka`中注册好了，所以直接调用消费即可

   ![](https://img-blog.csdnimg.cn/a5909ac5b9f3400a9af7e04f64eabb9d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

   ```java
   public List<Order> findAllOrders() {
       List<Order> orderList = orderMapper.selectAllOrders();
       for (Order order : orderList) {
           //String url = "http://localhost:8081/getUserById?id=" + order.getUserId();
           String url = "http://user-service/getUserById?id=" + order.getUserId();
           order.setUser(restTemplate.getForObject(url, User.class));
       }
       return orderList;
   
   }
   ```

2. 给`RestTemplate`上加入`@LoadBanlanced`引入负载均衡

   ```java
   @Bean
   @LoadBalanced
   public RestTemplate getRestTemplate() {
       return new RestTemplate();
   }
   ```

重启服务，成功实现`order-service`通过注册中心拉取`user-service`注册到`eureka`的服务信息，并引入负载均衡，通过在`application.properties`中配置`mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl`可以发现`UserApplication:8081`和`UserApplication:8082`都被调用了

![](https://img-blog.csdnimg.cn/538bea1b57aa4349b884e8ed4234278d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

### 8.4. `@LoadBalanced`注解原理 ---> `Ribbon`负载均衡流程

为什么我们在`RestTemplate`加入了`@LoadBalanced`就可以完成负载均衡？中途到底发生了什么事情？这就引出了我们要学习`SpringCloud`的第二个组件 ---> `Ribbon`，它是用来完成负载均衡的！



之前我们是将`order-service`中的`OrderService`的`URL`地址改成了：`http://user-service/getUserById?id=1`，我们直接访问这个`URL`地址，发现根本无法访问，说明这个地址被拦截了：

![](https://img-blog.csdnimg.cn/7348388b2832429cbd23038b2085bb9d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

那是谁帮我们拦截解析了呢？答案是`Ribbon`组件

1. `OrderService`发送请求：`http://user-service/getUserById?id=x`给`Ribbon`负载均衡
2. `Ribbon`从`eureka-server`中拉取`user-service`
3. `eureka-server`返回`user-service`服务列表给`Ribbon`负载均衡
4. 最后`Ribbon`轮询获取到`user-service`服务

![](https://img-blog.csdnimg.cn/d802ea08315f4a1c88f7a518e44e1fdb.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

`@LoadBalanced`这个注解就表示`order-service`发送的请求会被`Ribbon`组件拦截解析，那么`Ribbon`是如何实现拉取服务，又是如何实现负载均衡的呢？其原理是什么呢？最好的方法就是深入源码一探究竟。

【注：该`eureka`版本为`2.2.7RELEASE`，新版本的源代码可能已经改动】

1. `order-service`发送`http://user-service/getUserById?id=x`请求，该请求是由`RestTemplate`发送的，因为在`RestTemplate`上加入了`@LoadBanlanced`，所以该请求会传入到`LoadBanlancerInterceptor.java`进行拦截

   ![](https://img-blog.csdnimg.cn/d90d44a68a9d4ba9ab175ef74c8483af.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

   可以看到`LoadBalancerInterceptor`实现了`ClientHttpRequestInterceptor`接口，我们点进去看看

2. 我们可以看见`ClientHttpRequestInterceptor`接口描述第一句话就是作用：`对客户端HTTP请求进行拦截`

   ![](https://img-blog.csdnimg.cn/6204f36bc1454f40896524ea30f48c9a.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

   也就是说这也刚好反向验证了`LoadBalancerInterceptor`拦截器对服务调用者发起的请求进行了拦截，实现了`ClientHttpRequestInterceptor`中的`intercept`方法

   ![](https://img-blog.csdnimg.cn/ba9ed98b77e447cb929c91cead934f26.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

3. 根据最上面的流程图，下一步`Ribbon`就是要获取到服务名称，然后根据服务名称从`eureka-server`中拉取服务列表

   ![](https://img-blog.csdnimg.cn/0e5cd3e838634411b69b09989c4e704c.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

   获取到服务名称然后是如何进行拉取的呢？--->`RibbonLoadBalancerClient.java`

   ![](https://img-blog.csdnimg.cn/87f92e54360e4f83b093f3847c4bf5ec.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

   继续进入：

   ![](https://img-blog.csdnimg.cn/c50e08b08e174784b8b1688d76b8eecd.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

   可以发现通过`DynamicServerListLoadBalancer.java`以及刚刚获取到的服务名称进一步获取到了`LoadBalancer`对象，通过`LoadBalancer`可以获取到服务列表

4. 按照流程，从拦截请求再到获取服务名称再到获取服务列表，下一步就是要做负载均衡，对服务列表进行轮询，使用的是`IRule`接口，负载均衡轮询选择实在上述`getServer()`完成的，进入到`getServer()`看看

   ![](https://img-blog.csdnimg.cn/88f8f66e80164e68ae1264e2d817af8f.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

   继续进入：

   ![](https://img-blog.csdnimg.cn/b39488337e2349ff9ead72859cc1ae70.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

   负载均衡肯定有一套规则在里面，是以什么规则进行负载均衡？

   ![](https://img-blog.csdnimg.cn/8620a678238b430a8619dc25d3b42bdc.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

   通过查看得知这是一个`IRule`对象，但是`IRule`是一个接口，那么它就一定有实现类，通过`Ctrl+H`可以查看某个接口的实现类：

   ![](https://img-blog.csdnimg.cn/60ae5043a6ea40f992d01b2d631c9008.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

   这里边就有一个`RoundRobinRule`表示的是轮询的意思，也就是说`RoundRobinRule`实现了轮询规则，就是按照这个方式来轮询查找服务列表中的服务

   ![](https://img-blog.csdnimg.cn/8445db49eb2f4e32a0c799d0c377b9e9.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

通过上述源代码的分析，我们也就知道了个大概，知道`Robbin`组件进行了如下操作：

1. 通过`LoadBalancerInterceptor`拦截请求后并且从`URL`地址中获取服务名称
2. 再通过`RibbonLoadBalancerClient`以及服务名称调用第三步获取服务列表
3. `DynamicServerListLoadBalancer`从`eureka-server`中拉取服务列表
4. 再通过实现`IRule`接口的`RobbinRoundRule`轮询规则进行负载均衡从服务列表中选出一台服务
5. 最后通过`RibbonLoadBalancerClient`替换`URL`地址，发送请求获取服务

下面这张图应该在`order-service`和`RibbonLoadBalancerClient`之间再加上一个`LoadBalancerInterceptor`：

![](https://img-blog.csdnimg.cn/8973eaf72e254a68b03226ef976783fb.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

### 8.5.`Eureka`注册中心拉取服务列表的`Robbin` --->  负载均衡策略

`Ribbon`完成负载均衡靠得是一套规则，规则由`Rule`接口来定义，`Rule`接口的每一个子接口都是一种规则：

![](https://img-blog.csdnimg.cn/0804c04f7c944c2c8ef7e9f9790af037.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

**实现负载均衡的策略多种多样，默认是使用`RoundRobbinRule`轮询的方式选择服务器**

![](https://img-blog.csdnimg.cn/65ac29316cef46d992846a9c4f5dea16.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

先看看这种轮询的方式，可以看到`UserApplication1`中查找的是`1 3 5`，那么自然`UserApplication2`中查找的是`2 4 6`，这也证明了是通过轮询的方式

![](https://img-blog.csdnimg.cn/f1b848a73e744a88b34cf8da33c4201d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

通过代码的方式更换负载均衡策略，注意重启服务器，这种方式是全局配置，`order-service`调用所有的服务使用的负载均衡策略就都变成了随机选择的方式：

```java
@Bean
public RandomRule randomRule() {
    return new RandomRule();
}
或者
@Bean
public IRule randomRule() {
    return new RandomRule();
}
```

重新发起请求，可以看到选择服务器的变化，在`id`为`3 4 5 6`的时候选择了`UserApplication1`作为服务器，随机选择，说明负载均衡策略更改成功

![](https://img-blog.csdnimg.cn/ac8c64a6fe3343779281f09417fa2f71.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

有时候，我就想调用某个服务实行的是`A`负载均衡策略，调用`B`服务的时候施行的时候`B`负载均衡策略，可以做到吗？当然可以，这种实行负载均衡策略的方式需要在配置文件中配置`application.properties` ---> 针对某个微服务而言：

```properties
user-service.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule
```

### 8.6 `Ribbon`饥饿加载

`Ribbon`默认使用的是懒加载，什么是懒加载呢？就是客户端第一次访问的时候才会去创建`RobbinLoadBalancerClient`对象，所以第一访问的时间都会非常的漫长，如下图，可以看到足足达到`715ms`之久：

![](https://img-blog.csdnimg.cn/0b7ae9183e4d425d9dcc93ccf876659f.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

为了加快访问速度，可以改变懒加载，配置成是饥饿加载，这样只要服务在启动的时候创建`LoadBalancerClient`对象，从而降低了第一次访问时间过长的现象，配置如下：`ribbon.eager-load.client` ---> 项目启动时直接去拉取`userservice`的集群，多个用","隔开

```properties
ribbon.eager-load.enabled=true
ribbon.eager-load.clients=user-service
```

重启服务，观察加载时间，如下图可以看到在`order-service`服务启动时就加载了`LoadBalancerClient`对象：

![](https://img-blog.csdnimg.cn/e07b2e8ac65c436a98caa1e9253d8d9f.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

客户端访问的时间，可以看到饥饿加载相比于懒加载的时间足足降低了一半还少：

![](https://img-blog.csdnimg.cn/8b634ff21d894df6831e2c5141907563.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

## 9. `Nacos`注册中心

### 9.1 `Nacos`注册中心安装

`Nacos`是阿里巴巴开发的一个产品，现在是`SpringCloud`的一个组件。比`Eureka`的功能更多一点，在国内`Nacos`更受欢迎，所以学习`Nacos`还是有必要的。而且`Nacos`是`Java`语言实现的。

有人说`Eureka`停止更新了，其实这是个错误的说法，`Eureka`有两种版本，一种是一点几的版本，一种是二点几的版本，停止的是二点几的版本，而且这个版本的`Eureka`压根就还没有出来就停止了。【`2022`年`2.x`的版本被重新放出来了】

安装：`github` ---> 下载`release` ---> 解压 ---> 默认端口是`8848`  ---> 可以在配置文件中自定义端口 ---> 启动：`startup.cmd -m standalone[单机模式，-m 是 mode 的意思]` ---> 登录：`http://192.168.0.106:8848/nacos/index.html` --> 账号密码均为：`nacos`

![](https://img-blog.csdnimg.cn/d12925e16eb342d2b02b03ad4acc9355.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

![](https://img-blog.csdnimg.cn/d5d5f511c23142d2a3c890b8a1964384.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

### 9.2. `Nacos`实现注册与发现

【`Nacos`和`Eureka`的区别在于`Eureka`是需要你自己手动配置服务端，而`Nacos`直接就是安装包安装好了使用命令打开就是服务端，所以在`Nacos`只需要配置客户端即可】

1. 引入依赖 ---> `Nacos`是阿里巴巴提供的所以我们在父`pom.xml`引入`spring-cloud-alibaba`管理依赖，日后就不用再多加管理

   ```xml
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-alibaba-dependencies</artifactId>
       <version>2.2.5.RELEASE</version>
       <type>pom</type>
       <scope>import</scope>
   </dependency>
   ```

2. 注释掉`eureka`在`pom`文件中的依赖还有配置文件中的信息

3. 在`user-service`和`order-service`添加`Nacos`的依赖

   ```xml
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
   </dependency>
   ```

4. 将`user-service`和`order-service`中的`application.properties`配置`eureka`的配置注释掉配置`nacos`的配置 ---> 声明`Nacos`服务端所在的位置，监听的端口是什么？ ---> 这里需要将`eureka`中的信息注释掉 

   ```properties
   spring.cloud.nacos.server.addr=localhost:8848
   ```

通过`Nacos`的后台可以看到成功地将服务注册到了`Nacos`上：

![](https://img-blog.csdnimg.cn/1b00acd58cc84fbd945cac00c06fedc1.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

![](https://img-blog.csdnimg.cn/2faaded4a346487c9657da82da90f82d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAQ3JBY0tlUi0x,size_20,color_FFFFFF,t_70,g_se,x_16)

可以看到`Nacos`的后台比`Eureka`看起来更加清爽明了，看着就很舒服。输入：`http://localhost:8080/getAllOrders`验证服务发现，可以看到是可以正常访问的，表明服务发现没问题。启动各项服务，通过访问`URL`地址可以发现跟`Eureka`的功能是完全一样的，并且界面更加友好。

### 9.3 `Nacos`分级存储模型

此处`order-service:8080 user-service:8081 user-service:8082 user-service:8083`我们都可以称之为一个个实例，这样的实例可能在超大型项目中有成百上千个，可能在北京广州上海深圳杭州都存放着各个实例，当我们访问某项服务的时候肯定是访问距离最近的服务才是最快的，也正因此，`Nacos`可以引入集群，从而引出`Nacos`分级存储模型：

![](https://cdn.xn2001.com/img/2021/20210901091928.png)

这样搞的目的就是为了：服务调用的时候尽可能选择本地集群中的服务，避免跨集群调用，因为这样延迟很高，只有在本地集群中需要的服务访问不了的时候再去访问其它集群。

### 9.4 `Nacos`配置集群

```properties
spring.cloud.nacos.discovery.cluster-name: Beijing
```

直接复制多份`user-service`，将其加入到不同的集群 ---> `VM Options`：[三个集群 ---> 北京 深圳 杭州]

```java
-Dserver.port=8082 -Dspring.cloud.nacos.discovery.cluster-name=Beijing
-Dserver.port=8083 -Dspring.cloud.nacos.discovery.cluster-name=Shenzhen
-Dserver.port=8084 -Dspring.cloud.nacos.discovery.cluster-name=Shenzhen
-Dserver.port=8085 -Dspring.cloud.nacos.discovery.cluster-name=Hangzhou
-Dserver.port=8086 -Dspring.cloud.nacos.discovery.cluster-name=Hangzhou    
```

然后将`order-service`分别拉入到`Beijing Shenzhen Hangzhou`的集群中并分别访问`URL`，观察`IDEA`中显示的信息，可以发现都是访问集群里的服务，只有在集群中的服务挂掉的时候才去跨集群访问。【记得把上次设置的`RandomRule`给注释掉否则负载均衡策略会选择随机负载均衡。】

![](https://img-blog.csdnimg.cn/2ec0ce78a7d4438ea4d71f71cef81d95.png)

### 9.5 `Nacos`配置负载均衡规则

第一种情况：`order-service`位于`Beijing`集群，访问`http://localhost:8080/order/103`，可以看到只会访问同时`Beijing`集群的两个`user-service`

![](https://img-blog.csdnimg.cn/635ed404649c4d609f147ecf7027ed99.png)

![](https://img-blog.csdnimg.cn/4740ff18f3874282b86f69194b36d8ff.png)

第二种情况：`order-service`位于`Shenzhen`集群，可以看到同样的效果，但是还访问了其它的

![](https://img-blog.csdnimg.cn/e6073e8d15e14d1183a7d6c2496208b6.png)

第三种情况：`order-service`位于`Hangzhou`集群【跟上述一致】也是还访问了其它的

第四种情况：`order-service`位于`Hangzhou`集群，但是集群中的`user-service:8085 8086`挂掉了，也是访问了多个。

为什么会这样呢？原因是`Nacos`的默认负载均衡的策略就是轮询，如果我们要配置按地区查找，我们可以看到是一个个接着顺序访问的，如果要按地区轮询我们应该修改下配置类中的`Bean`。 ---> `order-service.configuration.MyConfiguration ---> NacosRule`并且是在集群中随机选择服务。

```java
@Bean
public IRule nacosRule() {
    return new NacosRule();
}
```

此时再去访问就是按照就近集群来访问，如果某个集群中所有服务都挂了，再去访问其它的集群服务，而且可以在日志中看到发生了跨集群访问。

**除了使用配置类还可以在配置文件中直接配置负载均衡规则：**

```properties
user-service:
  ribbon:
    NFLoadBalancerRuleClassName: spring.cloud.nacos.ribbon.NacosRule
```

### 9.6 `Nacos`配置负载均衡权重配置【服务平滑升级】

1. `Nacos`控制台可以设置实例的权重值，值在`0-1`之间
2. 同个集群内的多个实例，权重越高被访问的频率越高
3. 权重设置为`0`的时候则该服务将完全不被访问

![](https://cdn.xn2001.com/img/2021/20210901092020.png)

![](https://cdn.xn2001.com/img/2021/20210901092026.png)

**合理配置权重可以做到平滑升级，比如：先把`user-service:8080`的权重调节为`0`让用户流向`user-service:8081`，等`user-service:8080`升级完成之后再将权重从`0`更改为`0.1`让一部分用户先体验升级版本，若稳定，再将权重上调。这就可以做到平滑升级。**

### 9.7 `Nacos`环境隔离`namespace`

![](https://cdn.xn2001.com/img/2021/20210901092032.png)

首先想明白一个问题：为什么有了集群还要有`namespace`这样的东西呢？是不是觉得多此一举？其实不然。集群/服务都是针对业务来进行划分的，而`namespace`命名空间是针对注入开发环境生产环境测试环境这样不同环境下诞生的产物。

这样我们就可以在不同环境下使用不同的集群、实例了。

而`Group`，就是不同环境下相关度比较高的业务，比如订单业务和支付业务是高度相关的，是概念上的划分，就可以把这两个业务都放到同一个`Group`中去，你可以使用也可以不使用。而`Service/Data`就是服务，再往下就是各个集群再往下就是实例了。

创建命名空间的步骤如下：

![](https://cdn.xn2001.com/img/2021/20210901092038.png)

![](https://cdn.xn2001.com/img/2021/20210901092050.png)

![](https://cdn.xn2001.com/img/2021/20210901092059.png)

![](https://cdn.xn2001.com/img/2021/20210901092114.png)

如何指定服务实例的命名空间？以`order-service`为例：

```yaml
spring:
  cloud:
    nacos:
      server-addr: 192.168.0.105:8848
      discovery:
        cluster-name: Beijing
        namespace: xxxxxx
```

**<font color="red">每个`namespace`都有不同的唯一`id`，并且·不同`namespace`之间相互隔离即不同`namespace`的服务互不可见。</font>**

访问结果如下：

```json
{
    "timestamp": "2022-08-14T12:34:04.243+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "",
    "path": "/order/103"
}
```

### 9.8 `Nacos`注册中心的细节

相同点：

- **`Nacos`和`Eureka`都支持服务注册和服务拉取**
- **都支持服务提供者心跳方式做检测**

不同点：

- **`Nacos`存在临时和非临时实例**

  - **临时实例：**

    所有的实例在没有配置的时候都是临时实例，这个可以在控制台看到临时实例是`true`。每隔一段时间，临时实例需要主动去向注册中心报告我还活着，否则心跳检测不通过，`Nacos`会直接剔除掉该项服务。

    对于临时实例，消费者是定时主动拉取服务。

  - **非临时实例：**

    `Nacos`主动去询问生产者【非临时实例】，并且主动推送变更消息到消费者。告诉消费者服务更新了，让其赶紧去更新。并且当非临时实例宕机的时候，也不会从服务列表中剔除。

    ```yaml
    spring:
      cloud:
        nacos:
          discovery:
            ephemeral: false #设置为非临时实例
    ```

- 学过`Zookeeper`的应该知道`CAP`不可能三角，`Nacos`集群**默认采用AP方式(可用性)**，当集群中存在非临时实例时，**采用`CP`模式(一致性)**；而**`Eureka`只采用`AP`方式**，不可切换。

<hr/>

## 10. 配置中心

当微服务部署的实例越来越多的时候，达到上百上千个的时候，你自己去一个个的修改并且还需要重启服务器或是啥的，就非常令人感到恶心了。所以需要一种可以统一管理配置的地方，它不仅可以统一配置集中管理还可以实现更改配置后热更新。而这，正是配置中心所能完成的

### 10.1 `Nacos`配置中心

`Nacos`非常强大，此前我们看到它可以作为注册中心使用，现在，你将再一次看到它的强大 —— 它可以作为配置中心使用。一方面：**`Nacos`可以配置集中管**理，另一方面：**可以在配置变更的时候及时通知微服务并实现配置的热更新**。

### 10.2 `Nacos`创建配置

![](https://cdn.xn2001.com/img/2021/20210901092159.png)

![](https://cdn.xn2001.com/img/2021/20210901092206.png)

默认直接就是热更新的，要实现热更新的配置才放到`Nacos`，否则还是放到本地中会比较好【比如数据库连接信息】。

### 10.3 微服务读取`Nacos`配置中心配置

在没有配置中心的情况下，微服务读取配置的流程如下：读取本地的配置文件然后根据配置信息创建`Spring`容器，最后加载`Bean`：

![](https://cdn.xn2001.com/img/2021/20210901092215.png)

而当加入了诸如`Nacos`的配置中心之后，微服务会先去读取配置中心的配置，然后再读取本地的配置文件：

![](https://cdn.xn2001.com/img/2021/20210901092223.png)

正因为读取`Nacos`配置中心的配置文件在本地配置文件之前，如果你在本地配置文件中配置要读取配置中心的配置文件，那这样的配置根本不起效果。所以我们需要在别的地方做功夫。那就是比`Nacos`配置中心读取配置文件的优先级还要高的地方 —— `bootstrap.yml`文件【存放于本地】。

![](https://cdn.xn2001.com/img/2021/20210901092228.png)

知道了微服务读取配置中心的配置流程，就可以做具体工作了：

1. 在需要读取配置中心的微服务中，引入`Nacos`配置中心依赖：【注意该版本有漏洞】

   ```xml
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
       <version>2.2.5.RELEASE</version>
   </dependency>
   ```

2. 创建`bootstrap.yml`文件：【**<font color="red">注意，这里的`spring.profiles.active`不是必须的，你只要`spring.application.name`对得上也是完全可以的</font>**】

   ```yaml
   spring:
     application:
       name: order-service
     profiles:
       active: dev
     cloud:
       nacos:
         server-addr: 192.168.0.105:8848
         config:
           file-extension: yml
   ```

本地微服务会先读取：`${spring.cloud.nacos.server-addr}`中`Nacos`配置中心的地址，然后再通过读取`${spring.application.name}`+`${spring.profiles.active}`+`${spring.cloud.nacos.config.file-extension}`形成文件名：`${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}`。将其作为`id`读取配置中心的配置。

在这里就会去读取在配置中心的：`order-service-dev.yml`。验证是否读取成功，验证如下:

```java
package com.kk.order.controller;

import com.kk.order.pojo.Order;
import com.kk.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Value(value = "${pattern.dateformat}")
    private String patternFormat;

    @GetMapping(value = "/now")
    public String getNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(patternFormat));
    }
}
```

![](https://img-blog.csdnimg.cn/3938485837dc42aebebe38a013725d5c.png)

### 10.4 `Nacos`配置中心实现热更新

实现热更新有两种方式，其原理我猜正是隔段时间自动去访问配置中心的配置：

1. 使用`@Value`读取配置时，搭配`@RefreshScope`

   ```java
   package com.kk.order.controller;
   
   import com.kk.order.configuration.MyConfigurationProperties;
   import com.kk.order.pojo.Order;
   import com.kk.order.service.OrderService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.boot.context.properties.EnableConfigurationProperties;
   import org.springframework.cloud.context.config.annotation.RefreshScope;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   
   import java.time.LocalDateTime;
   import java.time.format.DateTimeFormatter;
   
   @RestController
   @RequestMapping("/order")
   @RefreshScope
   public class OrderController {
       @Autowired
       private OrderService orderService;
   
       @Autowired
       private MyConfigurationProperties myConfigurationProperties;
   
       @GetMapping(value = "/{orderId}")
       public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
           return orderService.queryOrderById(orderId);
       }
   
       @Value(value = "${pattern.dateformat}")
       private String patternFormat;
   
       @GetMapping(value = "/now")
       public String getNow() {
           return LocalDateTime.now().format(DateTimeFormatter.ofPattern(patternFormat));
       }
   }
   ```

2. 使用`@ConfigurationProperties`读取配置文件

   ```java
   package com.kk.order.controller;
   
   import com.kk.order.configuration.MyConfigurationProperties;
   import com.kk.order.pojo.Order;
   import com.kk.order.service.OrderService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.boot.context.properties.EnableConfigurationProperties;
   import org.springframework.cloud.context.config.annotation.RefreshScope;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   
   import java.time.LocalDateTime;
   import java.time.format.DateTimeFormatter;
   
   @RestController
   @RequestMapping("/order")
   @EnableConfigurationProperties(value = {MyConfigurationProperties.class})
   public class OrderController {
       @Autowired
       private OrderService orderService;
   
       @Autowired
       private MyConfigurationProperties myConfigurationProperties;
   
       @GetMapping(value = "/now2")
       public String getNow2() {
           return LocalDateTime.now().format(DateTimeFormatter.ofPattern(myConfigurationProperties.getDateformat()));
       }
   }
   ```

   ```java
   package com.kk.order.configuration;
   
   import lombok.Data;
   import org.springframework.boot.context.properties.ConfigurationProperties;
   
   @Data
   @ConfigurationProperties(prefix = "pattern")
   public class MyConfigurationProperties {
       private String dateformat;
   }
   ```

### 10.5 `Nacos`配置中心环境共享

前面有提到过说是`dev`环境即`spring.profile.active`不是必须的，那有跟没有的区别在于什么呢？就在于没有`dev`的不仅可以在`dev`环境中使用还可以在`product`生产环境中以及`test`测试环境中使用，**这就形成了环境共享**。

1. 修改表现层代码：

   ```java
   package com.kk.order.controller;
   
   import com.kk.order.configuration.MyConfigurationProperties;
   import com.kk.order.pojo.Order;
   import com.kk.order.service.OrderService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.boot.context.properties.EnableConfigurationProperties;
   import org.springframework.cloud.context.config.annotation.RefreshScope;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   
   import java.time.LocalDateTime;
   import java.time.format.DateTimeFormatter;
   
   @RestController
   @RequestMapping("/order")
   @EnableConfigurationProperties(value = {MyConfigurationProperties.class})
   public class OrderController {
       
       @Autowired
       private MyConfigurationProperties myConfigurationProperties;
   
       @GetMapping(value = "/testShare")
       public Object getShare() {
           return myConfigurationProperties;
       }
   }
   ```

2. 更改配置类代码：

   ```java
   package com.kk.order.configuration;
   
   import lombok.Data;
   import org.springframework.boot.context.properties.ConfigurationProperties;
   
   @Data
   @ConfigurationProperties(prefix = "pattern")
   public class MyConfigurationProperties {
       private String dateformat;
       private String sharedvalue;
   }
   ```

3. 控制台添加：`order-service.yml`

   ```yaml
   pattern:
    sharedvalue: "共享环境"
   ```

4. 使用`copy configuration`复制一份服务器，修改`Profile Active`以及`VM Options: -Dserver.port=8081`然后启动服务器

   分别查询：`http://localhost:8080/order/testShare`和`http://localhost:8081/order/testShare`获取到：

   ![](https://img-blog.csdnimg.cn/5eeb3f828fb74e1092732ea160db0257.png)

   ![](https://img-blog.csdnimg.cn/fad7eea538164619ad86af6fe714e9b8.png)

   可以发现在`test`环境中无法获取到`order-service-dev.yml`配置文件中的`dateformat`，但是可以获取到`order-service.yml`配置文件中的配置信息。而`dev`环境二者都可以获取。这就说明了配置文件是可以指定环境，也可以不指定的，你说这是共享也行。

- 不用想也知道，`Nacos`配置中心配置的指定环境配置文件优先级是最高的，其次是无指定环境最后是本地。

  ![](https://cdn.xn2001.com/img/2021/20210901092501.png)

- 那么问题来了？不同微服务之间是否也可以共享在`Nacos`配置中心的配置文件，当然可以，只需要更换名称，然后在不同的微服务之间的`bootstrap.yml`做个声明即可，该声明有两种方式：

  1. 使用`extensionc-configs`
  2. 使用`shared-configs`

  比如：

  在`order-service`中使用：`extension-configs`

  ```yaml
  spring:
    application:
      name: order-service
    cloud:
      nacos:
        server-addr: 116.25.152.11:8848
        config:
          file-extension: yml
          extension-configs:
            - dataId: diffrentserviceshare.yml
  ```

  在`user-service`中使用：`shared-configs`

  ```yaml
  spring:
    application:
      name: user-service
    cloud:
      nacos:
        server-addr: 116.25.152.11:8848
        config:
          file-extension: yml
          shared-configs:
            - dataId: diffrentserviceshare.yml
  ```

  在`Nacos`控制台中创建配置文件以及配置信息：

  ![](https://img-blog.csdnimg.cn/27cb8d0aad5548ccbf143b6260af466b.png)

  然后更改`order-service`跟`user-service`中的`MyConfigurationProperties.java`【没有就创建】：

  ```java
  package com.kk.user.coniguration;
  
  import lombok.Data;
  import org.springframework.boot.context.properties.ConfigurationProperties;
  
  @Data
  @ConfigurationProperties(prefix = "pattern")
  public class MyConfigurationProperties {
      private String dateformat;
      private String sharedvalue;
      private String sharedservice;
  }
  ```

  然后在表现层中读取即可：

  ```java
  @GetMapping("/testShare")
  public Object getShare() {
      return myConfigurationProperties;
  }
  ```

  最后直接使用`Postman`进行验证：

  ![](https://img-blog.csdnimg.cn/9b06d1636f784f21ad7ba6a22dbdb858.png)

  ![](https://img-blog.csdnimg.cn/a8d9a9a91d454f21850b0759b52413ee.png)

  可以看到两个不同服务之间通过`Nacos`以及在`bootstrap.yml`做的配置实现了数据共享。

### 10.6 搭建`Nacos`集群

- **想想为什么需要做`Nacos`集群？**
- **我们知道`Nacos`是安装式的程序，有没有想过万一`Nacos`服务端挂了，那我们做的所有的微服务都将不可使用，所以为了实现<font color="red">高可用</font>防止危险情况，所以需要做`Nacos`集群 ---> 使用`Nginx`代理。**

架构如下：

![img](https://cdn.xn2001.com/img/2021/202108181959897.png)

其中包含`3`个`Nacos`节点，然后一个负载均衡器`Nginx`代理`3`个`Nacos`，我们计划的`Nacos`集群如下图，`MySQL`的主从复制后续再添加。

![img](https://cdn.xn2001.com/img/2021/202108182000220.png)

三个`Nacos`节点的地址：

| 节点`    | `IP`            | Port   |
| :------- | :-------------- | :----- |
| `Nacos1` | `192.168.0.105` | `8845` |
| `Nacos2` | `192.168.0.105` | `8846` |
| `Nacos3` | `192.168.0.105` | `8847` |

1. 初始化数据库：

   `Nacos`默认数据存储在内嵌数据库`Derby`中，不属于生产可用的数据库。官方推荐的最佳实践是使用带有主从的高可用数据库集群，主从模式的高可用数据库。这里我们以单点的数据库为例。

   ```sql
   create database nacos;
   
   CREATE TABLE `config_info` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `data_id` varchar(255) NOT NULL COMMENT 'data_id',
     `group_id` varchar(255) DEFAULT NULL,
     `content` longtext NOT NULL COMMENT 'content',
     `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     `src_user` text COMMENT 'source user',
     `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
     `app_name` varchar(128) DEFAULT NULL,
     `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
     `c_desc` varchar(256) DEFAULT NULL,
     `c_use` varchar(64) DEFAULT NULL,
     `effect` varchar(64) DEFAULT NULL,
     `type` varchar(64) DEFAULT NULL,
     `c_schema` text,
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = config_info_aggr   */
   /******************************************/
   CREATE TABLE `config_info_aggr` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `data_id` varchar(255) NOT NULL COMMENT 'data_id',
     `group_id` varchar(255) NOT NULL COMMENT 'group_id',
     `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
     `content` longtext NOT NULL COMMENT '内容',
     `gmt_modified` datetime NOT NULL COMMENT '修改时间',
     `app_name` varchar(128) DEFAULT NULL,
     `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';
   
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = config_info_beta   */
   /******************************************/
   CREATE TABLE `config_info_beta` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `data_id` varchar(255) NOT NULL COMMENT 'data_id',
     `group_id` varchar(128) NOT NULL COMMENT 'group_id',
     `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
     `content` longtext NOT NULL COMMENT 'content',
     `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
     `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     `src_user` text COMMENT 'source user',
     `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
     `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = config_info_tag   */
   /******************************************/
   CREATE TABLE `config_info_tag` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `data_id` varchar(255) NOT NULL COMMENT 'data_id',
     `group_id` varchar(128) NOT NULL COMMENT 'group_id',
     `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
     `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
     `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
     `content` longtext NOT NULL COMMENT 'content',
     `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     `src_user` text COMMENT 'source user',
     `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = config_tags_relation   */
   /******************************************/
   CREATE TABLE `config_tags_relation` (
     `id` bigint(20) NOT NULL COMMENT 'id',
     `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
     `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
     `data_id` varchar(255) NOT NULL COMMENT 'data_id',
     `group_id` varchar(128) NOT NULL COMMENT 'group_id',
     `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
     `nid` bigint(20) NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (`nid`),
     UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
     KEY `idx_tenant_id` (`tenant_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = group_capacity   */
   /******************************************/
   CREATE TABLE `group_capacity` (
     `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
     `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
     `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
     `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
     `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
     `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
     `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
     `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_group_id` (`group_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = his_config_info   */
   /******************************************/
   CREATE TABLE `his_config_info` (
     `id` bigint(64) unsigned NOT NULL,
     `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
     `data_id` varchar(255) NOT NULL,
     `group_id` varchar(128) NOT NULL,
     `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
     `content` longtext NOT NULL,
     `md5` varchar(32) DEFAULT NULL,
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
     `src_user` text,
     `src_ip` varchar(50) DEFAULT NULL,
     `op_type` char(10) DEFAULT NULL,
     `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
     PRIMARY KEY (`nid`),
     KEY `idx_gmt_create` (`gmt_create`),
     KEY `idx_gmt_modified` (`gmt_modified`),
     KEY `idx_did` (`data_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';
   
   
   /******************************************/
   /*   数据库全名 = nacos_config   */
   /*   表名称 = tenant_capacity   */
   /******************************************/
   CREATE TABLE `tenant_capacity` (
     `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
     `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
     `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
     `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
     `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
     `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
     `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
     `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
     `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_tenant_id` (`tenant_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';
   
   
   CREATE TABLE `tenant_info` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
     `kp` varchar(128) NOT NULL COMMENT 'kp',
     `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
     `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
     `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
     `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
     `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
     `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
     KEY `idx_tenant_id` (`tenant_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';
   
   CREATE TABLE `users` (
   	`username` varchar(50) NOT NULL PRIMARY KEY,
   	`password` varchar(500) NOT NULL,
   	`enabled` boolean NOT NULL
   );
   
   CREATE TABLE `roles` (
   	`username` varchar(50) NOT NULL,
   	`role` varchar(50) NOT NULL,
   	UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
   );
   
   CREATE TABLE `permissions` (
       `role` varchar(50) NOT NULL,
       `resource` varchar(255) NOT NULL,
       `action` varchar(8) NOT NULL,
       UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
   );
   
   INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);
   
   INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
   ```

2. 配置`Nacos`，先配好一个然后复制多份即可：

   进入`Nacos`的`conf`目录，修改配置文件`cluster.conf.example`，重命名为`cluster.conf`，注意在`Nacos 2.x`中会需要使用两个端口，所以如果你使用的是：`8845 8846 8847`连续的，则第二个会启动失败，所以这里最好是换成隔几个端口。

   ```conf
   116.25.152.11:8850
   116.25.152.11:8860
   116.25.152.11:8870
   ```

   然后修改`application.properties`文件，添加数据库配置

   ```properties
   server.port=8850
   
   spring.datasource.platform=mysql
   db.num=1
   db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
   db.user.0=root
   db.password.0=123456
   ```

   将该文件夹复制多问，并且更改每一个`Nacos`的`server .port`

3. 在各个`Nacos`启用`startup.cmd`

4. 安装`Nginx`然后在配置文件中进行如下配置`http`内部：

   ```properties
   upstream nacos-cluster {
   	server 116.25.152.11:8850;
   	server 116.25.152.11:8860;
   	server 116.25.152.11:8870;
   }
   
   server {
   	listen       80;
   	server_name  localhost;
   
   	location /nacos {
   		proxy_pass http://nacos-cluster;
   	}
   }
   ```

5. 启动`Nginx`服务`start nginx.exe`，访问：`http://116.25.152.11:80`，看到如下页面说明所有配置均成功

   ![img](https://img-blog.csdnimg.cn/351706d0cc00488181a7656fbdb4a0de.png)

6. 客户端若需要使用，只需修改配置端口和`IP`即可

   ```yaml
   spring:
     cloud:
       nacos:
         server-addr: 116.25.152.11:80
   ```

**<font color="red">实际部署时，需要给做反向代理的`Nginx`服务器设置一个域名，这样后续如果有服务器迁移`Nacos`的客户端也无需更改配置。`Nacos`的各个节点应该部署到多个不同服务器，做好容灾和隔离工作。</font>**

## 11. 远程调用

一直到这一部分，我们想远程调用都使用的是`RestTemplate`，我们再来看看使用`RestTemplate`进行远程调用的代码：

```java
String url = "http://user-service/user/" + order.getUserId();
User user = restTemplate.getForObject(url, User.class);
```

可以看到这样子的代码：

- 可读性差
- 参数`URL`复杂难以维护

为了更好用更方便更直观的调用想调用，所以我们决定采用`Feign`：其作用就是帮助我们**<font color="red">优雅的实现`http`请求的发送</font>**，解决上面提到的问题。![img](https://img-blog.csdnimg.cn/3cfd43ee11ea41f2ba33656ae801c5a0.png)

### 11.1 `Feign`

### 11.2 `Feign`实现远程调用

1. `order-service`引入依赖

   ```xml
   <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-feign -->
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-feign</artifactId>
       <version>1.4.7.RELEASE</version>
   </dependency>
   ```

2. 修改配置，这里直接使用注解`@EnableFeignClients`即可

   ```java
   package com.kk.order;
   
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.cloud.openfeign.EnableFeignClients;
   
   @SpringBootApplication
   @EnableFeignClients
   public class OrderApplication {
       public static void main(String[] args) {
           SpringApplication.run(OrderApplication.class, args);
       }
   }
   ```

3. 新建要远程调用的接口：`order.client.UserClient`

   ```java
   package com.kk.order.client;
   
   import com.kk.user.pojo.User;
   import org.springframework.cloud.openfeign.FeignClient;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   
   @FeignClient(value = "user-service")
   public interface UserClient {
       @GetMapping("/user/{id}")
       User findById(@PathVariable(value = "id") Long id);
   }
   ```

4. 使用`UserClient`即可直接进行远程调用

   ```java
   package com.kk.order.service;
   
   import com.kk.order.client.UserClient;
   import com.kk.order.mapper.OrderMapper;
   import com.kk.order.pojo.Order;
   import com.kk.order.pojo.User;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Service;
   
   @Service
   public class OrderServiceFeign {
       @Autowired
       private OrderMapper orderMapper;
   
       @Autowired
       private UserClient userClient;
   
       public Order queryOrderById(Long id) {
           Order order = orderMapper.findById(id);
           User user = userClient.findById(order.getUserId());
           order.setUser(user);
           return order;
       }
   }
   ```

5. 新建`Controller`进行验证：

   ```java
   package com.kk.order.controller;
   
   import com.kk.order.pojo.Order;
   import com.kk.order.service.OrderService;
   import com.kk.order.service.OrderServiceFeign;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   
   @RestController
   @RequestMapping("/order")
   public class OrderController1 {
       @Autowired
       private OrderServiceFeign orderServiceFeign;
   
       @GetMapping(value = "/{orderId}")
       public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
           return orderServiceFeign.queryOrderById(orderId);
       }
   }
   ```

这样看来，`Feign`可比使用`RestTemplate`干净优雅多了。

### 11.3 `Feign`自定义配置

`Feign`可以支持很多的自定义配置，如下表所示：

| 类型                     | 作用             | 说明                                                         |
| :----------------------- | :--------------- | :----------------------------------------------------------- |
| **`feign.Logger.Level`** | 修改日志级别     | 包含四种不同的级别：`NONE`、`BASIC`、`HEADERS`、`FULL`       |
| `feign.codec.Decoder`    | 响应结果的解析器 | `http`远程调用的结果做解析，例如解析`json`字符串为`java`对象 |
| `feign.codec.Encoder`    | 请求参数编码     | 将请求参数编码，便于通过`http`请求发送                       |
| `feign.Contract`         | 支持的注解格式   | 默认是`SpringMVC`的注解                                      |
| `feign.Retryer`          | 失败重试机制     | 请求失败的重试机制，默认是没有，不过会使用`Ribbon`的重试     |

一般需要配置的就是：`fegin.Logger.Level`日志，**默认的为`NONE`**。

- **`NONE`：不记录任何日志信息，这是默认值。**
- **`BASIC`：仅记录请求的方法，`URL`以及响应状态码和执行时间**
- **`HEADERS`：在`BASIC`的基础上，额外记录了请求和响应的头信息**
- **`FULL`：记录所有请求和响应的明细，包括头信息、请求体、元数据**

通过配置文件`application.yml`进行配置：【注意得先有配置信息：`logging.level.com.kk: debug`否则无效】

1. 可以针对某一个远程调用的服务进行配置

   ```yaml
   feign:
     client:
       config:
         user-service:
           loggerLevel: FULL
   ```

2. 也可以对所有要远程调用的服务进行配置

   ```yaml
   feign:
     client:
       config:
         default:
           loggerLevel: FULL
   ```

通过代码进行自定义配置，同样分为全局和局部：

1. 声明一个`MyFeignConfiguration`类

   ```java
   package com.kk.order.configuration;
   
   import feign.Logger;
   import org.springframework.context.annotation.Bean;
   
   public class MyFeignConfiguration {
       @Bean
       public Logger.Level feignLoggerLevel() {
           return Logger.Level.BASIC;
       }
   }
   ```

2. 全局配置 ---> `OrderApplication.java`

   ```java
   package com.kk.order;
   
   import com.kk.order.configuration.MyFeignConfiguration;
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.cloud.openfeign.EnableFeignClients;
   
   @SpringBootApplication
   @EnableFeignClients(defaultConfiguration = {MyFeignConfiguration.class})
   public class OrderApplication {
       public static void main(String[] args) {
           SpringApplication.run(OrderApplication.class, args);
       }
   }
   ```

3. 局部配置 ---> `UserClient.interface`

   ```java
   package com.kk.order.client;
   
   import com.kk.order.configuration.MyFeignConfiguration;
   import com.kk.order.pojo.User;
   import org.springframework.cloud.openfeign.FeignClient;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   
   @FeignClient(value = "user-service", configuration = {MyFeignConfiguration.class})
   public interface UserClient {
       @GetMapping("/user/{id}")
       User findById(@PathVariable(value = "id") Long id);
   }
   ```

### 11.4 `Feign`性能优化

首先了解下为什么要对`Feign`做性能优化？这就涉及到`Feign`的底层客户端是实现：默认采用`URLConnection`，但是它不支持连接池

其余还有两种：

- `Apache HttpClient`：支持连接池
- `OKHttp`：支持连接池

此外还有就是在上节学习到的日志，使用`NONE BASIC`即可，再往上`FULL HEADER`内容太多会影响性能。

这里使用`Apache HttpClient`作为更改客户端演示。

1. 引入依赖：

   ```xml
   <!--httpClient的依赖 -->
   <dependency>
       <groupId>io.github.openfeign</groupId>
       <artifactId>feign-httpclient</artifactId>
   </dependency>
   ```

2. 修改配置：

   ```yaml
   feign:
     client:
       config:
         default:
           loggerLevel: BASIC #日志级别为 BASIC
     httpclient:
       enabled: true #使用httpclient作为客户端
       max-connections: 200 #连接池最大连接数量
       max-connections-per-route: 50 #每个路径的最大连接数
   ```

3. 找到`feign-core`核心包找到`FeignClientFactoryBean`中的`loadBalance`方法，打断点然后`Deubg`模式启动`order-service`

   ![img](https://img-blog.csdnimg.cn/3c8e61894ca54c649ba978ba2195ae7a.png)

   ![img](https://img-blog.csdnimg.cn/2e3e6afc87db467d906b205a89454058.png)

   可以看到此时`Feign`底层使用的就是`HttpClient`。

### 11.5 `Feign`最佳实践

最佳实践：指的是企业经常会用到的一些方式

- 继承方式：一样的代码可以通过继承来共享

  1. 定义一个 API 接口，利用定义方法，并基于 SpringMVC 注解做声明
  2. Feign 客户端、Controller 都集成该接口

  优点

  - 简单
  - 实现了代码共享

  缺点

  - 服务提供方、服务消费方紧耦合
  - 参数列表中的注解映射并不会继承，因此 Controller 中必须再次声明方法、参数列表、注解

  ![img](https://cdn.xn2001.com/img/2021/20210901092803.png)

- 抽取方式：将 FeignClient 抽取为独立模块，并且把接口有关的 pojo、默认的 Feign 配置都放到这个模块中，提供给所有消费者使用。

  例如：将 UserClient、User、Feign 的默认配置都抽取到一个 feign-api 包中，所有微服务引用该依赖包，即可直接使用。

  ![img](https://cdn.xn2001.com/img/2021/20210901092811.png)

这里使用第二种方式做一个演示：

1. 创建`feign-api`模块，引入依赖：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <parent>
           <artifactId>01-cloud-demo</artifactId>
           <groupId>com.kk</groupId>
           <version>1.0</version>
       </parent>
       <modelVersion>4.0.0</modelVersion>
   
       <artifactId>feign-api</artifactId>
   
       <properties>
           <maven.compiler.source>8</maven.compiler.source>
           <maven.compiler.target>8</maven.compiler.target>
       </properties>
   
       <dependencies>
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-openfeign</artifactId>
           </dependency>
       </dependencies>
   </project>
   ```

2. 创建`com.kk.pojo.User com.kk.clients.UserClient`

   ```java
   package com.kk.pojo;
   
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class User {
       private Long id;
       private String username;
       private String address;
   }
   ```

   ```java
   package com.kk.clients;
   
   import com.kk.pojo.User;
   import org.springframework.cloud.openfeign.FeignClient;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   
   @FeignClient(value = "user-service")
   public interface UserClient {
       @GetMapping("/user/{id}")
       User findById(@PathVariable Long id);
   }
   ```

3. 此时`feign-api`模块就创建好了，我们将在`order-service`的`User`以及`UserClient`删除，然后导入`feign-api`依赖：

   ```xml
   <dependency>
       <groupId>com.kk</groupId>
       <artifactId>feign-api</artifactId>
       <version>1.0</version>
   </dependency>
   ```

4. 标红的地方导入包

5. 当所需要的包不在`SpringApplication`扫描范围之内，这些`FeignClient`就不能使用。所以需要添加包在哪个位置：【否则服务无法正常启动】：`@EnableFeignClients(basePackages = "com.kk.clients")`

   ```java
   package com.kk.order;
   
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.cloud.openfeign.EnableFeignClients;
   
   @SpringBootApplication
   @EnableFeignClients(basePackages = "com.kk.clients")
   public class OrderApplication {
       public static void main(String[] args) {
           SpringApplication.run(OrderApplication.class, args);
       }
   }
   ```

   除此之外还可以指定字节码文件：`@EnableFeignClients(clients = UserClient.class)`

   ```java
   package com.kk.order;
   
   import com.kk.clients.UserClient;
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.cloud.openfeign.EnableFeignClients;
   
   @SpringBootApplication
   @EnableFeignClients(clients = UserClient.class)
   public class OrderApplication {
       public static void main(String[] args) {
           SpringApplication.run(OrderApplication.class, args);
       }
   }
   ```

## 12. `Gateway`网关

有了服务集群，还晓得了注册中心、配置中心、远程调用。可是还有个大问题，就是你内部的许多服务有时候只对一些内部人员访问，并不是所有人都可以访问的，所以就需要有样东西可以给所有来访问服务的请求把把关。这就需要使用到`Getway`网关兄弟了。

![img](https://cdn.xn2001.com/img/2021/20210901092857.png)

### 12.1 网关可以干什么

网关的核心功能特性【可以干什么】？

- **身份验证，权限校验**
- **服务路由，负载均衡**
- **请求限流**

在`SpringCloud`中网关的实现包括两种：`Gateway`和`Zuul`。`Zuul`是基于`Servlet`实现的，属于阻塞式编程。而`Spring Cloud Gateway`则是基于`Spring5`中提供的`WebFlux`，属于响应式编程的实现，具备更好的性能。

### 12.2 网关初步使用

1. 创建`Gateway`模块，引入依赖

   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-gateway</artifactId>
       <version>3.1.3</version>
   </dependency>
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
   </dependency>
   ```

2. 修改配置文件

   ```yaml
   server:
     #网关端口
     port: 10010
   spring:
     application:
       #网关名称
       name: gateway
     cloud:
       nacos:
         #Nacos 地址
         server-addr: 192.168.0.105:8848
       gateway:
         #网关路由配置
         routes:
             #路由 id，自定义【唯一即可，随便你写什么都可以】
           - id: user-service
             #uri http://localhost:8081 路由的目标地址就是 http 固定地址，lb 就是负载均衡，后面跟着服务名称
             uri: lb://user-service
             #路由断言，也就是判断请求是否符合路由规则的条件，按照路径匹配，以/user/开头就符合
             predicates:
               - Path=/user/**
   ```

   将符合`Path`规则的一切请求都代理到`uri`参数指定的地址，上面的`application.yml`中我们将`/user/**`开头的请求全部代理到`lb://user-service`，其中的`lb`就是负载均衡即`LoadBalance`，根据服务名拉取服务列表，实现负载均衡。

   简单点就是：本来我们访问`http://localhost:8081/user/1`是直接访问服务的，但是现在我们访问就需要经过网关了，所有的`/user/**`都会被网关所拦截 ，转发给`lb://user-service`。

   如果你有多个`predicates`时候，请求需要同时满足全部才可以通过网关访问服务。

   整个流程如下：

   ![img](https://cdn.xn2001.com/img/2021/202108220127419.png)

3. 访问网址：`http://localhost:10010/user/1`

   出错：

   ```json
   {
       "timestamp": "2022-08-15T13:17:15.228+00:00",
       "path": "/user/1",
       "status": 503,
       "error": "Service Unavailable",
       "message": "",
       "requestId": "02d4469c-2"
   }
   ```

   错误原因：

   ```
   由于springcloud2020弃用了Ribbon，因此Alibaba在2021版本nacos中删除了Ribbon的jar包，因此无法通过lb路由到指定微服务，出现了503情况。
   所以只需要引入springcloud loadbalancer包即可。
   ```

   解决办法：

   ```xml
   <!--客户端负载均衡loadbalancer-->
   <dependency>
   	<groupId>org.springframework.cloud</groupId>
   	<artifactId>spring-cloud-starter-loadbalancer</artifactId>
   </dependency>
   ```

4. 再次访问：可以正常访问

   ```json
   {
       "id": 1,
       "username": "柳岩",
       "address": "湖南省衡阳市"
   }
   ```

总结下：路由配置需要配置如下几个方面

1. 路由`id`：`[user-service]`唯一标识，完全可以自定义，一般写跟注册中心的服务名称一样即可
2. 路由目标`uri`：`[lb://user-service]`路由的目标地址，`http`代表固定地址，`lb`代表根据服务名负载均衡
3. 路由断言`predicates`：其实就是判断路由的规则，符合规则就转发到路由目标`uri`
4. 路由过滤器`filters`：对请求或响应做处理

### 12.3 断言工厂

我们在配置文件中写的断言规则只是字符串，这些字符串会被`Predicate Factory`读取并处理，转变为路由判断的条件。比如在我们的案例中：`predicates: - Path=/user/**`就是一个规则，这些路由规则是被：`org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory`所处理的，像这样的断言工厂在`SpringCloud`中还有十几个。

|              |                                |                                                              |
| :----------- | :----------------------------- | :----------------------------------------------------------- |
| 名称         | 说明                           | 示例                                                         |
| After        | 是某个时间点后的请求           | `- After=2037-01-20T17:42:47.789-07:00[America/Denver]`      |
| Before       | 是某个时间点之前的请求         | `- Before=2031-04-13T15:14:47.433+08:00[Asia/Shanghai]`      |
| Between      | 是某两个时间点之前的请求       | `- Between=2037-01-20T17:42:47.789-07:00[America/Denver], 2037-01-21T17:42:47.789-07:00[America/Denver]` |
| Cookie       | 请求必须包含某些cookie         | `- Cookie=chocolate, ch.p`                                   |
| Header       | 请求必须包含某些header         | - Header=X-Request-Id, \d+                                   |
| Host         | 请求必须是访问某个host（域名） | - Host=`**.somehost.org`, `**.anotherhost.org`               |
| Method       | 请求方式必须是指定方式         | - Method=GET,POST                                            |
| Path         | 请求路径必须符合指定规则       | - Path=/red/{segment},/blue/**                               |
| Query        | 请求参数必须包含指定参数       | - Query=name, Jack或者- Query=name                           |
| `RemoteAddr` | 请求者的`ip`必须是指定范围     | `- RemoteAddr=192.168.1.1/24`                                |
| Weight       | 权重处理                       |                                                              |

> 官方文档：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gateway-request-predicates-factories

一般的，只需要掌握`Path`和各个规则的意思基本就可以应对各种工作场景了。

比如：

```yaml
predicates:
  - Path=/order/**
  - After=2031-04-13T15:14:47.433+08:00[Asia/Shanghai]
```

当前为`2022`年，根本不到`2031`年所以这个路由根本就不会被转发到`uri`路由目标中。可以看到访问结果为：`404`

```json
{
    "timestamp": "2022-08-15T13:36:10.943+00:00",
    "path": "/user/1",
    "status": 404,
    "error": "Not Found",
    "message": null,
    "requestId": "e617f3ca-1"
}
```

### 12.4 过滤器工厂

`GatewayFilter`是网关中提供的一种过滤器，可以对进入网关的请求和微服务返回的响应做处理。

![img](https://cdn.xn2001.com/img/2021/202108220133487.png)

`Spring`提供了`31`种不同的路由过滤器工厂。

>官方文档：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gatewayfilter-factories

经常使用的过滤器有：

| **名称**               | **说明**                     |
| :--------------------- | :--------------------------- |
| `AddRequestHeader`     | 给当前请求添加一个请求头     |
| `RemoveRequestHeader`  | 移除请求中的一个请求头       |
| `AddResponseHeader`    | 给响应结果中添加一个响应头   |
| `RemoveResponseHeader` | 从响应结果中移除有一个响应头 |
| `RequestRateLimiter`   | 限制请求的流量               |

下面以`AddRequestHeader`作为演示范例演示一遍：

**需求**：给所有进入`user-service`的请求添加一个请求头：`sign=kk.com is eternal`，只需要修改`gateway`服务的`application.yml`文件，添加路由过滤即可

```yaml
server:
  #网关端口
  port: 10010
spring:
  application:
    #网关名称
    name: gateway
  cloud:
    nacos:
      #Nacos 地址
      server-addr: 192.168.0.104:8848
    gateway:
      #网关路由配置
      routes:
          #路由 id，自定义【唯一即可】
        - id: user-service
          #uri http://localhost:8081 路由的目标地址就是 http 固定地址，lb 就是负载均衡，后面跟着服务名称
          uri: lb://user-service
          #路由断言，也就是判断请求是否符合路由规则的条件，按照路径匹配，以/user/开头就符合
          predicates:
            - Path=/user/**
            #- After=2031-04-13T15:14:47.433+08:00[Asia/Shanghai]
          filters:
            - AddRequestHeader=sign, kk.com is eternal
```

验证时，可以使用`@RequestHeader`获取请求参数，可以在`user-service`的`controller`中看是否可以提取：

```java
package com.kk.user.controller;

import com.kk.user.pojo.User;
import com.kk.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController2 {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}")
    public User queryById(@PathVariable("id") Long id, @RequestHeader(value = "sign", required = false) String sign) {
        log.warn(sign);
        return userService.queryById(id);
    }
}
```

访问`http://localhost:10010/user/1`观察结果，可以看到控制台中有`warn`信息显示：

![img](https://img-blog.csdnimg.cn/770b684a2da644a89b37d8be50735c04.png)

若想全局过滤路由，`SpringGateway`也是可以实现的：

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - AddRequestHeader=sign, kk.com is eternal
```

- 过滤器的作用就是对路由的请求跟响应做处理，比如添加一个请求头
- 配置在路由下的过滤器只能对当前的路由做过滤，想要全局配置也是可以的使用`spring.cloud.gateway.default-filters`

### 12.5 全局过滤器

上面的`SpringGateway`虽然很好用而且也有全局过滤`default-filters`，并且提供了`31`之多，但是这些东西都是别人固定好的，有时候我们想实现自己的一些路由过滤规则就很难受。于是就冒出了个全局过滤器`GlobalFilter`，**它也可以做到拦截请求，但不同的时它可以做自己的业务逻辑即自定义。**

**需求**：定义全局过滤器，拦截请求，判断请求的参数是否满足下面条件

- 参数中是否有`authorization`
- `authorization`参数值是否为`admin`

记得添加`@Component`将该全局过滤器交由`Spring`容器管理：

```java
package com.kk.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取第一个匹配的 authorization 参数即可
        String authorization = exchange.getRequest().getQueryParams().getFirst("authorization");
        //对参数进行判断，如果是 admin 就放行，否则不放行
        if ("admin".equals(authorization)) return chain.filter(exchange);
        //设置状态码，拦截返回
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    /**
     * 设置过滤器优先级，值越低优先级越高 ---> 也可以使用 @Order 注解
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
```

访问网址：`http://localhost:10010/user/1?authorization=admin111`和`http://localhost:10010/user/1?authorization=admin`验证即可。

### 12.6 过滤器顺序

请求进入网关会碰到三类过滤器：`DefaultFilter`、当前路由的过滤器、`GlobalFilter`。

请求路由后，会将三者合并到一个**过滤器链（集合）**中，排序后依次执行每个过滤器。

排序规则如下：

- 每一个过滤器都必须指定一个 int 类型的 order 值，**order 值越小，优先级越高，执行顺序越靠前**。
- `GlobalFilter`通过实现`Ordered`接口，或者使用`@Order`注解来指定`order`值，由我们自己指定。
- 路由过滤器和`defaultFilter`的`order`由`Spring`指定，**默认是按照声明顺序从1递增**。
- 当过滤器的`orde`值一样时，**会按照` defaultFilter`> 路由过滤器 > `GlobalFilter`的顺序执行**。

![img](https://cdn.xn2001.com/img/2021/202108230002747.png)

### 12.7 跨域问题

什么是跨域？跨域就是域名不一致，主要包括：

- 域名不同：比如`www.baidu.com`跟`www.baidu.org`的域名就是不一样的，再或者`www.jd.com`跟`www.miaosha.jd.com`也是不一样的
- 域名相同，端口不同：比如`www.baidu.com:1111`跟`www.baidu.com:2222`

**<font color="red">跨域问题是浏览器这边限制下产生的问题：浏览器禁止请求的发起者与服务端发生跨域`Ajax`请求，请求被浏览器拦截。</font>**即：浏览器 + `Ajax`

**解决方案：`CROS`**

```yaml
spring:
  cloud:
    gateway:
      globalcors: # 全局的跨域处理
        add-to-simple-url-handler-mapping: true # 网关特有，解决options请求被拦截问题，询问让不让访问
        corsConfigurations:
          '[/**]':
            allowedOrigins: # 允许哪些网站的跨域请求 allowedOrigins: “*” 允许所有网站
              - "http://localhost:8090"
            allowedMethods: # 允许的跨域ajax的请求方式
              - "GET"
              - "POST"
              - "DELETE"
              - "PUT"
              - "OPTIONS"
            allowedHeaders: "*" # 允许在请求中携带的头信息
            allowCredentials: true # 是否允许携带cookie
            maxAge: 360000 # 这次跨域检测的有效期，浏览器将在该有效期内不再发送请求，直接使用
```

验证：

1. 创建`index.html` ---> 很简单的功能，就是用`axios`发生`Ajax`请求【此时服务端先别配置上述`CROS`】

   ```html
   <!DOCTYPE html>
   <html lang="en">
   <head>
       <meta charset="UTF-8">
       <title>Title</title>
   </head>
   <body>
   <script src="https://unpkg.com/axis/dist/axios.min.js"></script>
   <script>
       axios.get("http://localhost:10010/user/1?authorization=admin")
       .then(resp => console.log(resp.data))
       .catch(err => console.log(err))
   </script>
   </body>
   </html>
   ```

2. 启用服务，查看控制台，`vscode`可以使用`--liver-server`，可以看到出现了跨越问题

   ![img](https://img-blog.csdnimg.cn/62eaf29ef693434aa0af3e12696db535.png)

3. 添加如下配置：

   ```yaml
   spring:
     cloud:
       gateway:
         globalcors: # 全局的跨域处理
           add-to-simple-url-handler-mapping: true # 网关特有，解决options请求被拦截问题，询问让不让访问
           corsConfigurations:
             '[/**]':
               allowedOrigins: # 允许哪些网站的跨域请求 allowedOrigins: “*” 允许所有网站
                 - "http://localhost:8090"
               allowedMethods: # 允许的跨域ajax的请求方式
                 - "GET"
                 - "POST"
                 - "DELETE"
                 - "PUT"
                 - "OPTIONS"
               allowedHeaders: "*" # 允许在请求中携带的头信息
               allowCredentials: true # 是否允许携带cookie
               maxAge: 360000 # 这次跨域检测的有效期，浏览器将在该有效期内不再发送请求，直接使用
   ```

4. 再访问时就可以解决跨域问题了，这个会`CV`大法即可

## 13. `Docker`

见笔记：https://krolliaa.github.io/2022/08/16/Docker/

## 14. `MessageQueue`

### 14.1 同步通讯和异步通讯

- 同步通讯：就像打电话，需要实时响应。

- 异步通讯：就像发邮件，不需要马上回复。

![img](https://cdn.xn2001.com/img/2021/20210904133345.png)

两种方式各有优劣，打电话可以立即得到响应，但是你却不能跟多个人同时通话。发送邮件可以同时与多个人收发邮件，但是往往响应会有延迟。

之前学习的`Feign`调用就属于同步方式，虽然调用可以实时得到结果，但是存在以下问题：**耦合度高、性能下降、资源浪费、级联失败**。![img](https://cdn.xn2001.com/img/2021/20210904133517.png)

**同步调用的优点**：时效性较强，可以实时得到结果。

**同步调用的缺点**：业务代码**耦合度高**、一旦某个业务出现错误则整个系统都出现错误【**级联失败**】，需要等待调用业务完成才可以完成业务等待时间长【**性能下降**】，等待的时间业务仍然占用着`CPU`资源这就肯定会造成【**资源浪费**】。同步调用的缺点即有：**耦合度高、性能下降、资源浪费、级联失败**。

**<font color="deepskyblue">所以为了解决同步调用：耦合度高、级联失败、性能下降、资源浪费的问题，异步调用方案就出现了。</font>**

以购买商品为例，用户支付后需要调用订单服务完成订单状态修改，调用物流服务，从仓库分配响应的库存并准备发货。在事件模式中，支付服务是事件发布者`publisher`，在支付完成之后只需要发布一个支付成功的事件`event`，事件中带上订单`id`。订单服务和物流服务是事件订阅者`Consumer`，订阅支付成功的事件，监听到事件后完成自己的业务即可。最开始是当发布者发布消息的时候，直接从线程池中拿线程来用，但是如果流量爆满，线程池中的线程总有不够用的一天，这会导致内存爆满。

这就是异步调用，于是为了解决异步调用线程池数量问题，就引出了**<font color="red">消息队列</font>**。

![img](https://cdn.xn2001.com/img/2021/20210904145001.png)

就是说为了解决发布者和订阅者之间的耦合度以及异步处理。中间搞了一个`Broker`，这就大大降低了系统耦合度。除了拥有**系统解耦**、**异步处理**，除此之外，`Broker`即`MQ`它还可以对过往流量进行监控，并且适当的削峰，即**流量削峰**、**流量监控**，并且对来去的消息进行**消息收集**和**消息广播**，而且还满足**最终一致性**即不管发布者发来什么，要消费的订阅者都会从消息队列中取数据，生产者无需管也无需担心自己发的消息没有被消费的问题。

总结下就是：**异步处理、系统解耦、流量削峰、流量监控、消息广播、消息收集、最终一致性**。

**但很明显，这样的架构变复杂了，而且你的消息队列必须做得非常安全可靠，并且支持高并发高可用。**

![img](https://cdn.xn2001.com/img/2021/20210904144714.png)

**大多数时候，我们并不需要多线程异步处理的需求，而是追求时效性，所以大多数还是使用的是同步通讯。**

### 14.2 消息队列

比较常见的`MQ`实现：`ActiveMQ`、`RabbitMQ`、`RocketMQ`、`Kafka`。若是有海量数据不太关心数据安全性的果断选择`Kafka`。若需要自己做定制可以使用`RokcetMQ`大多数时候选择`RabbitMQ`即可。

| **`RabbitMQ`** | **`ActiveMQ`**            | **`RocketMQ`**                      | **`Kafka`** |              |
| :------------- | :------------------------ | :---------------------------------- | :---------- | ------------ |
| 公司/社区      | `Rabbit`                  | `Apache`                            | 阿里        | `Apache`     |
| 开发语言       | `Erlang`                  | `Java`                              | `Java`      | `Scala&Java` |
| 协议支持       | `AMQP、XMPP、SMTP、STOMP` | `OpenWire、STOMP、REST、XMPP、AMQP` | 自定义协议  | 自定义协议   |
| 可用性         | 高                        | 一般                                | 高          | 高           |
| 单机吞吐量     | 一般                      | 差                                  | 高          | 非常高       |
| 消息延迟       | 微秒级                    | 毫秒级                              | 毫秒级      | 毫秒以内     |
| 消息可靠性     | 高                        | 一般                                | 高          | 一般         |

有没想过为什么`RabbitMQ`的消息延迟可以做到微秒级？这正是因为`RabbitMQ`是基于`Erlang`语言开发的。

- 它是 一款专门为交换机软件开发的语言，而且是分布式的面向并发的。
- `Erlang`可以做到进程间上下文切换效率远高于`C`语言。
- `Erlang`还有者跟原生`Socket`一样的延迟。
- `Erlang`是虚拟机解释运行的语言所以可以跨平台部署`RabbitMQ`。

### 14.3 使用`Docker`安装`RabbitMQ`

1. 拉取`RabbitMQ`镜像：

   ```shell
   docker pull rabbitmq:3-management
   ```

2. 运行`RabbitMQ`容器，用户名：`admin`，密码：`123456`，容器名：`RabbitmqContainer`，服务端口`5672`，`web`管理端口`15672`，主机名：`mq1`

   ```shell
   docker run -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=123456 --name RabbitMQContainer --hostname rabbitmq -p 15672:15672 -p 5672:5672 -d rabbitmq:3-management
   ```

3. `VirtualBox`配置端口映射`15672`然后访问地址：`http://192.168.56.1:15672`即可

### 14.4 `RabbitMQ`中的角色及其基本结构

- `publisher`：生产者【寄件人】
- `consumer`：消费者【收件人】
- `exchange`：交换机，负责消息路由【快件分拨中心】
- `queue`：队列，存储消息【丰巢快递柜】
- `virtualHost`：虚拟主机，**隔离不同租户**的`exchange`、`queue`、消息隔离

![img](https://cdn.xn2001.com/img/2021/20210904172912.png)

### 14.5 `RabbitMQ`的底层实现原理：`AMQP`协议

- 协议就是规范，而`AMQP`协议就是`RabbitMQ`的规范，规定了`RabbitMQ`的对外接口。
- 学习`RabbitMQ`本质就是学习`AMQP`协议。

> 1. 生产者生产消息然后贴上`Routing Key`路由键，这个路由键就相当于快递的收件地址`Queue`。
> 2. 这个快递到快递分拨中心的路程我们称其为`Connection`连接。因为去往快递分拨中心肯定不止一个快递，也许有好多好多个快递正在赶往快递分拨中心，这一条条到快递分拨中心的我们称其为`Channel`叫做一条条信道。
> 3. 然后快递就到快递分拨中心这里，快递分拨中心在消息中间件里头叫做`exchange`就是交换机的意思。
> 4. 到了快递分拨中心以后，快递分拨中心要将快递分拨中心跟收件地址进行绑定不然快递员不知道从哪里拿快递，然后快递员就可以快递到收件地址`Queue`可以是蜂巢，也可以是菜鸟驿站。
> 5. 随后消费者就可以从菜鸟驿站中取出快递拿来消费使用了。
> 6. 消费者拿快递的通道跟生产者的一样，去往队列的路有好多条，而且此时肯定同时也有好多消费者正在前往拿快递的路上，所以就会有多个信道，这些信道放到一块就组成了连接。【连接其实使用的就是`TCP`连接】
> 7. 整个快递系统起主导作用的就是消息中间件，这里称呼为`Message Broker`用于接收和分发快递。
> 8. 因为一个`Broker`快递系统忙不过来，所以这个大大的`Message Broker`就创建了好多`Virtual Host`，就是虚拟`Broker`，将多个单元隔开。

整个快递系统最核心的组件就是：快递分拨中心 ---> `Exchange`交换机 ---> 它承担了非常重要的功能即`RabbitMQ`的核心功能 ---> **路由转发**：`Exchange`根据路由键`Routing Key`和绑定关系`Binding Key`为消息提供路由，将消息转发至相应队列。

![](https://img-blog.csdnimg.cn/9f0e5d464ed5452a89cf95226dd8ad50.png)

### 14.6 `RabbitMQ`官方模型介绍

`RabbitMQ`官方提供了`5`个不同的`Demo`示例，对应了不同的消息模型。

![img](https://cdn.xn2001.com/img/2021/20210904173739.png)

### 14.7 `RabbitMQ`官方`API`实现`HelloWorld`模型

非常非常简单的发布订阅模型，就是发布者将消息发布到队列中然后消费者从队列中取出消息进行消费。

![img](https://cdn.xn2001.com/img/2021/20210904200637.png)

官方的`HelloWorld`是基于最基础的消息队列模型来实现的，只包括三个角色：

- `publisher`：消息发布者，将消息发送到队列`queue`
- `queue`：消息队列，负责接受并缓存消息
- `consumer`：订阅队列，处理队列中的消息

使用`RabbitMQ`官方提供的`API`即可实现这一模型：

1. 引入依赖主要就是`spring-boot-starter-amqp`

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.7.2</version>
           <relativePath/> <!-- lookup parent from repository -->
       </parent>
       <groupId>com.kk</groupId>
       <artifactId>message-queue-demo</artifactId>
       <version>0.0.1-SNAPSHOT</version>
       <name>02-message-queue-demo</name>
       <description>02-message-queue-demo</description>
       <properties>
           <java.version>1.8</java.version>
       </properties>
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter</artifactId>
           </dependency>
           <dependency>
               <groupId>com.fasterxml.jackson.dataformat</groupId>
               <artifactId>jackson-dataformat-xml</artifactId>
               <version>2.9.10</version>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-amqp</artifactId>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
           </dependency>
       </dependencies>
   
       <build>
           <plugins>
               <plugin>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-maven-plugin</artifactId>
               </plugin>
           </plugins>
       </build>
   
   </project>
   ```

2. 创建发布者测试类：

   ```java
   package com.kk.publisher;
   
   import com.rabbitmq.client.Channel;
   import com.rabbitmq.client.Connection;
   import com.rabbitmq.client.ConnectionFactory;
   import org.junit.jupiter.api.Test;
   import org.springframework.boot.test.context.SpringBootTest;
   
   import java.io.IOException;
   import java.util.concurrent.TimeoutException;
   
   @SpringBootTest()
   public class PCTest {
       @Test
       public void HelloWorldTest() throws IOException, TimeoutException {
           //1.创建连接工厂
           ConnectionFactory connectionFactory = new ConnectionFactory();
           connectionFactory.setHost("192.168.56.1");
           connectionFactory.setPort(5672);
           connectionFactory.setUsername("admin");
           connectionFactory.setPassword("123456");
           //2.创建连接
           Connection connection = connectionFactory.newConnection();
           //3.创建连接通道
           Channel channel = connection.createChannel();
           //4.创建队列
           String queueName = "hello.world.queue";
           channel.queueDeclare(queueName, true, false, false, null);
           //5.发布消息
           String message = "Hello World Type RabbitMQ";
           channel.basicPublish("", queueName, null, message.getBytes());
           //6.关闭通道，关闭连接
           channel.close();
           connection.close();
       }
   }
   ```

   启动测试类，然后观察`http://192.168.56.1:15672/#/queues`中的队列情况。

3. 创建消费者测试类：

   ```java
   package com.kk.publisher;
   
   import com.rabbitmq.client.Channel;
   import com.rabbitmq.client.Connection;
   import com.rabbitmq.client.ConnectionFactory;
   import org.junit.jupiter.api.Test;
   import org.springframework.boot.test.context.SpringBootTest;
   
   import java.io.IOException;
   import java.util.concurrent.TimeoutException;
   
   @SpringBootTest()
   public class PCTest {
       @Test
       public void HelloWorldTest() throws IOException, TimeoutException {
           //1.创建连接工厂
           ConnectionFactory connectionFactory = new ConnectionFactory();
           connectionFactory.setHost("192.168.56.1");
           connectionFactory.setPort(5672);
           connectionFactory.setUsername("admin");
           connectionFactory.setPassword("123456");
           //2.创建连接
           Connection connection = connectionFactory.newConnection();
           //3.创建连接通道
           Channel channel = connection.createChannel();
           //4.创建队列
           String queueName = "hello.world.queue";
           channel.queueDeclare(queueName, true, false, false, null);
           //5.发布消息
           String message = "Hello World Type RabbitMQ";
           channel.basicPublish("", queueName, null, message.getBytes());
           //6.关闭通道，关闭连接
           channel.close();
           connection.close();
       }
   }
   ```

   启动测试类，然后观察`http://192.168.56.1:15672/#/queues`中的队列情况以及控制台消息，可以看到控制台中打印了消息并且消息队列中的消息被消费了。

### 14.8 `SpringAMQP`实现发布订阅模型

`SpringAMQP`是基于`RabbitMQ`封装的一套模板，并且还利用 `SpringBoot`对其实现了自动装配，使用起来非常方便。`SpringAMQP`的官方地址：https://spring.io/projects/spring-amqp。

![img](https://cdn.xn2001.com/img/2021/20210904202046.png)

![img](https://cdn.xn2001.com/img/2021/20210904202056.png)

`SpringAMQP`提供了三个功能：

- 自动声明队列、交换机及其绑定关系
- 基于注解的监听器模式，异步接收消息
- 封装了`RabbitTemplate`工具，用于发送消息

#### 14.8.1 `Hello-World`

使用`Spring AMQP`实现`Hello-World`发布订阅模型：

![img](https://cdn.xn2001.com/img/2021/20210904211238.png)

1. 消费者和生产者都需要引入依赖：

   ```xml
   <!--AMQP依赖，包含RabbitMQ-->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-amqp</artifactId>
   </dependency>
   ```

2. 消费者和生产者都需要修改配置：

   ```yaml
   spring:
     rabbitmq:
       host: 192.168.56.1
       port: 5672
       virtual-host: /
       username: admin
       password: 123456
   ```

3. 生产者使用`RabbitTemplate`发送消息：

   ```java
   package com.kk.publisher;
   
   import org.junit.jupiter.api.Test;
   import org.springframework.amqp.rabbit.core.RabbitTemplate;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.boot.test.context.SpringBootTest;
   
   @SpringBootTest
   public class SpringAMQPTest {
   
       @Autowired
       private RabbitTemplate rabbitTemplate;
   
       @Test
       public void testSimple() {
           String queueName = "hello.world.queue";
           String message = "人类毁于傲慢。";
           rabbitTemplate.convertAndSend(queueName, message);
       }
   }
   ```

   消费者监听消费：

   ```java
   package com.kk.consumer;
   
   import org.springframework.amqp.rabbit.annotation.RabbitListener;
   import org.springframework.stereotype.Component;
   
   @Component
   public class MyConsumer {
       @RabbitListener(queues = {"hello.world.queue"})
       public void testSimple(String message) {
           System.out.println("消费者消费消息：" + message);
       }
   }
   ```

#### 14.8.2 `Work-Queue`

![img](https://cdn.xn2001.com/img/2021/20210904211238.png)

使用`Spring AMQP`实现`Work-Queue`发布订阅模型：

`Work queues`，也被称为`（Task queues）`，任务模型。简单来说就是**让多个消费者绑定到一个队列，共同消费队列中的消息【轮询消费】**。

当消息处理比较耗时的时候，可能生产消息的速度会远远大于消息的消费速度。长此以往，消息就会堆积越来越多，无法及时处理。此时就可以使用`work`模型，多个消费者共同处理消息处理，速度就能大大提高了。

1. 声明一个队列：

   ```java
   package com.kk.consumer.config;
   
   import org.springframework.amqp.core.Queue;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   
   @Configuration
   public class RabbitMQConfiguration {
       @Bean
       public Queue workQueue() {
           return new Queue("work.queue");
       }
   }
   ```

2. 创建生产者：

   ```java
   @Test
   public void testWork() throws InterruptedException {
       String queueName = "work.queue";
       String message = "世界人民 --- ";
       for (int i = 0; i < 50; i++) {
           rabbitTemplate.convertAndSend(queueName, (message + i));
           Thread.sleep(20);
       }
   }

3. 创建消费者：

   ```java
   package com.kk.consumer;
   
   import org.springframework.amqp.rabbit.annotation.RabbitListener;
   import org.springframework.stereotype.Component;
   
   @Component
   public class MyConsumer {
       @RabbitListener(queues = {"hello.world.queue"})
       public void testSimple(String message) {
           System.out.println("消费者消费消息：" + message);
       }
   
       @RabbitListener(queues = {"work.queue"})
       public void testWork1(String message) {
           System.out.println("消费者...1...消费消息：" + message);
       }
   
       @RabbitListener(queues = {"work.queue"})
       public void testWork2(String message) {
           System.out.println("消费者...2...消费消息：" + message);
       }
   }
   ```

启动测试、消费者项目，可以看到不同的消费者轮询消费队列中的消息：

```shell
消费者...2...消费消息：世界人民 --- 0
消费者...1...消费消息：世界人民 --- 1
消费者...2...消费消息：世界人民 --- 2
消费者...1...消费消息：世界人民 --- 3
消费者...2...消费消息：世界人民 --- 4
消费者...1...消费消息：世界人民 --- 5
消费者...2...消费消息：世界人民 --- 6
消费者...1...消费消息：世界人民 --- 7
消费者...2...消费消息：世界人民 --- 8
消费者...1...消费消息：世界人民 --- 9
消费者...2...消费消息：世界人民 --- 10
......
```

这样的消费者轮询消费并没有考虑到不同消费者可能存在不同的处理能力。`RabbitMQ`默认有一个消息预取机制，还没消费就会预取下一个消息到下一个消费者手中，通过`prefetch`可以实现限制每次只能取1条消息，消费完毕后才能取下一个消息。

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        prefetch: 1
```

`Work`模型的使用：

- 多个消费者绑定到一个队列，同一条消息只会被一个消费者处理
- 通过设置`prefetch`来控制消费者预取的消息数量

#### 14.8.3 `Fanout`

![img](https://cdn.xn2001.com/img/2021/20210904213455.png)

图中可以看到，在订阅模型中，多了一个`exchange`角色，而且过程略有变化

- `Publisher`：生产者，也就是要发送消息的程序，但是不再发送到队列中，**而是发给`exchange`（交换机）**
- `Consumer`：消费者，与以前一样，订阅队列，没有变化
- `Queue`：消息队列也与以前一样，接收消息、缓存消息
- `Exchange`：交换机，一方面，接收生产者发送的消息；另一方面，知道如何处理消息，例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作，取决于`Exchange`的类型。`Exchange`有以下`3`种类型：
  - `Fanout`：广播，将消息交给所有绑定到交换机的队列
  - `Direct`：定向，把消息交给符合指定`routing key`的队列
  - `Topic`：通配符，把消息交给符合`routing pattern`（路由模式） 的队列

**`Exchange`（交换机）只负责转发消息，不具备存储消息的能力**，因此如果没有任何队列与`Exchange`绑定，或者没有符合路由规则的队列，那么消息会丢失！

`Fanout`，英文翻译是扇出，在`MQ`中我们也可以称为广播。

![img](https://cdn.xn2001.com/img/2021/20210912160350.png)

在广播模式下，消息发送流程是这样的：

- 可以有多个队列
- 每个队列都要绑定到`Exchange`（交换机）
- 生产者发送的消息，只能发送到交换机，交换机来决定要发给哪个队列，生产者无法决定
- 交换机把消息发送给绑定过的所有队列
- 订阅队列的消费者都能拿到消息

1. 声明一个广播类型的交换机以及三个队列

   ```java
   @Bean
   public FanoutExchange fanoutExchange() {
       return new FanoutExchange("fanout.exchange");
   }
   
   @Bean
   public Queue fanoutQueue1() {
       return new Queue("fanout.queue1");
   }
   @Bean
   public Queue fanoutQueue2() {
       return new Queue("fanout2.queue2");
   }
   @Bean
   public Queue fanoutQueue3() {
       return new Queue("fanout.queue3");
   }
   ```

2. 绑定交换机跟队列

   ```java
   @Bean
   public Binding bindingFanoutQueue1() {
       return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
   }
   @Bean
   public Binding bindingFanoutQueue2() {
       return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
   }
   @Bean
   public Binding bindingFanoutQueue3() {
       return BindingBuilder.bind(fanoutQueue3()).to(fanoutExchange());
   }
   ```
   
3. 运行项目，可以查看到交换机绑定了三个队列：

   ![img](https://img-blog.csdnimg.cn/938201a6be6b450dad10196d7c2b779b.png)

4. 消费者监听三个队列，实时消费：[创建三个消费者]

   ```java
   package com.kk.consumer;
   
   import org.springframework.amqp.rabbit.annotation.RabbitListener;
   import org.springframework.stereotype.Component;
   
   @Component
   public class MyFanoutConsumer {
       @RabbitListener(queues = {"fanout.queue1"})
       public void testFanout1(String message) {
           System.out.println("广播模式...消费者...1...消费消息：" + message);
       }
   
       @RabbitListener(queues = {"fanout.queue2"})
       public void testFanout2(String message) {
           System.out.println("广播模式...消费者...2...消费消息：" + message);
       }
   
       @RabbitListener(queues = {"fanout.queue3"})
       public void testFanout3(String message) {
           System.out.println("广播模式...消费者...3...消费消息：" + message);
       }
   }
   ```

5. 生产者发布消息：

   ```java
   @Test
   public void testFanout() {
       String message = "Fanout Message";
       rabbitTemplate.convertAndSend("fanout.exchange", "", message.getBytes());
   }
   ```

6. 运行项目，查看结果：可以看到虽然只生产了`1`条消息，但是在广播模式下，发送给了3个队列，丝毫不用理会`Routing Key`，这就是广播模式

   ```java
   广播模式...消费者...1...消费消息：Fanout Message
   广播模式...消费者...3...消费消息：Fanout Message
   广播模式...消费者...2...消费消息：Fanout Message
   ```

7. 除了使用配置类的方式配置绑定关系，还可以使用注解的方式配置绑定关系，声明新的队列

   这里的`type = "fanout"`是一定要写的，因为默认是`direct`直连模式类型。

   ```java
   @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "fanout.queue4"), exchange = @Exchange(value = "fanout.exchange", type = "fanout"))})
   public void testFanout4(String message) {
       System.out.println("广播模式...消费者...4...消费消息：" + message);
   }
   ```

#### 14.8.4 `Direct`

在`Fanout`模式中，一条消息，会被所有订阅的队列都消费。但是，在某些场景下，我们希望不同的消息被不同的队列消费。这时就要用到`DirectExchange`

![img](https://cdn.xn2001.com/img/2021/20210912182822.png)

在`Direct`模型下：

- 队列与交换机的绑定，不能是任意绑定了，而是要指定一个`RoutingKey`（路由`key`）
- 消息的发送方向`Exchange`发送消息时，也必须指定消息的 `RoutingKey`。
- `Exchange`不再把消息交给每一个绑定的队列，而是根据消息的`Routing Key`进行判断，只有队列的`Routingkey`与消息的 `Routing key`完全一致，才会接收到消息

1. 声明直连交换机、3个队列、绑定交换机跟队列

   ```java
   package com.kk.consumer.config;
   
   import org.springframework.amqp.core.Binding;
   import org.springframework.amqp.core.BindingBuilder;
   import org.springframework.amqp.core.DirectExchange;
   import org.springframework.amqp.core.Queue;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   
   @Configuration
   public class RabbitMQConfiguration2 {
       @Bean
       public DirectExchange directExchange() {
           return new DirectExchange("direct.exchange");
       }
   
       @Bean
       public Queue directQueue1() {
           return new Queue("direct.queue1");
       }
   
       @Bean
       public Queue directQueue2() {
           return new Queue("direct.queue2");
       }
   
       @Bean
       public Queue directQueue3() {
           return new Queue("direct.queue3");
       }
   
       @Bean
       public Binding bindingDirectQueue1() {
           return BindingBuilder.bind(directQueue1()).to(directExchange()).with("cold.coffee");
       }
   
       @Bean
       public Binding bindingDirectQueue2() {
           return BindingBuilder.bind(directQueue2()).to(directExchange()).with("hot.coffee");
       }
   
       @Bean
       public Binding bindingDirectQueue3() {
           return BindingBuilder.bind(directQueue3()).to(directExchange()).with("spicy.coffee");
       }
   }
   ```

2. 创建生产者发送消息

   ```java
    @Test
    public void testDirect() {
        String message = "Direct Cold Coffee Message";
        rabbitTemplate.convertAndSend("direct.exchange", "cold.coffee", message.getBytes());
    }
   ```

3. 创建消费者消费消息

   ```java
   package com.kk.consumer;
   
   import org.springframework.amqp.rabbit.annotation.Exchange;
   import org.springframework.amqp.rabbit.annotation.Queue;
   import org.springframework.amqp.rabbit.annotation.QueueBinding;
   import org.springframework.amqp.rabbit.annotation.RabbitListener;
   import org.springframework.stereotype.Component;
   
   @Component
   public class MyDirectConsumer {
   
       @RabbitListener(queues = {"direct.queue1"})
       public void testDirect1(String message) {
           System.out.println("消费者 ① 消费消息 冷咖啡：" + message);
       }
   
       @RabbitListener(queues = {"direct.queue2"})
       public void testDirect2(String message) {
           System.out.println("消费者 ② 消费消息 热咖啡：" + message);
       }
   
       @RabbitListener(queues = {"direct.queue3"})
       public void testDirect3(String message) {
           System.out.println("消费者 ③ 消费消息 辣咖啡：" + message);
       }
   }
   ```

4. 运行项目，可以发现只有消费者`1`消费了消息，因为生产者发送的`Routing Key ---> cold.coffee`，而绑定关系也是`cold.coffee`，所以就只有消费者`1`消费了消息

   ```java
   直连模式 Direct 消费消息 冷咖啡：Direct Cold Coffee Message
   ```

   可以尝试改成`hot.coffee`，那就只有消费者`2`消费了消息

   ```java
   直连模式 Direct 消费消息 冷咖啡：Direct Cold Coffee Message
   直连模式 Direct 消费消息 热咖啡：Direct Hot Coffee Message
   ```

5. 使用注解创建第四个队列并与`direct.exchange`交换机绑定，绑定关系有两个`sweet.coffee`和`hot.coffee`，所以当发送的路由键为`hot.coffee`，理应第二个队列跟第四个队列都收得到其余队列收不到，而为`sweet.coffee`时只有第四个队列消费得了消息。

   ```java
   @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "direct.queue4"), exchange = @Exchange(value = "direct.exchange", type = "direct"), key = {"sweet.coffee", "hot.coffee"})})
   public void testDirect4(String message) {
       System.out.println("消费者 ④ 消费消息 热咖啡和甜咖啡：" + message);
   }
   ```

6. 运行项目，结果与预期一致：

   ```java
   消费者 ④ 消费消息 热咖啡和甜咖啡：Direct Hot Coffee Message
   消费者 ② 消费消息 热咖啡：Direct Hot Coffee Message
   ```

#### 14.8.5 `Topic`

`Topic ` 与 `Direct`相比，都是可以根据`RoutingKey`把消息路由到不同的队列。只不过`Topic `类型可以让队列在绑定`Routing key` 的时候使用通配符！

![img](https://cdn.xn2001.com/img/2021/20210912194016.png)

通配符规则：

`#`：匹配一个或多个词

`*`：只能匹配一个词

```
item.#`：能够匹配 item.spu.insert 或者 item.spu
item.*`：只能匹配 item.spu
```

将上述图例转化为代码，全部使用注解：

```java
package com.kk.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MyTopicConsumer {

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic.queue1"), exchange = @Exchange(value = "topic.exchange", type = "topic"), key = {"china.#"})})
    public void testTopic1(String message) {
        System.out.println("消费者...1...消费消息：" + message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic.queue2"), exchange = @Exchange(value = "topic.exchange", type = "topic"), key = {"japan.#"})})
    public void testTopic2(String message) {
        System.out.println("消费者...2...消费消息：" + message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic.queue3"), exchange = @Exchange(value = "topic.exchange", type = "topic"), key = {"#.weather"})})
    public void testTopic3(String message) {
        System.out.println("消费者...3...消费消息：" + message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic.queue4"), exchange = @Exchange(value = "topic.exchange", type = "topic"), key = {"#.news"})})
    public void testTopic4(String message) {
        System.out.println("消费者...4...消费消息：" + message);
    }
}
```

```java
@Test
public void testTopic() {
    String message = "Direct china.weather.news Message";
    rabbitTemplate.convertAndSend("topic.exchange", "china.weather.news", message.getBytes());
}
```

```java
消费者...1...消费消息：Direct china.weather.news Message
消费者...4...消费消息：Direct china.weather.news Message
```

### 14.9 消息转换器

`Spring`会把你发送的消息序列化为字节发送给`MQ`，接收消息的时候，还会把字节反序列化为`Java`对象。

**默认情况下`Spring`采用的序列化方式是`JDK`序列化。**

我们可以去试一下效果：直接发送一个`Map`类型的数据给`object.queue`

```java
package com.kk.consumer;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyObjectConsumer {

    @RabbitListener(queuesToDeclare = {@Queue(value = "object.queue")})
    public void testTopic1(Map<String, String> map) {
        System.out.println("接收 Map 消息：" + map);
    }
}
```

先停止消费者【先启动消费者是为了创建队列】，然后生产者发送消息：

```java
@Test
public void testObject() {
    Map<String, String> map = new HashMap<>();
    map.put("name", "张麻子");
    rabbitTemplate.convertAndSend("object.queue", map);
}
```

![img](https://img-blog.csdnimg.cn/535f76d79afb4f5d8b80785fcaf09f64.png)

可以看到发送的消息体积非常大，可读性很差，所以推荐使用`JSON`格式进行序列化和反序列化。

1. 生产者和消费者均引入：`jackson`依赖

2. 生产者和消费者均添加配置类，修改消息转换器

   ```java
   package com.kk.consumer.config;
   
   import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
   import org.springframework.amqp.support.converter.MessageConverter;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   
   @Configuration
   public class MyConfiguration {
       @Bean
       public MessageConverter jsonMessageConverter(){
           return new Jackson2JsonMessageConverter();
       }
   }
   ```

3. 此时再发送和接收消息就靠的是`jackson`来进行消息转换了。

   ![img](https://img-blog.csdnimg.cn/86f249d02d9a44d19731c695645c1354.png)

## 15. `ElasticSearch`

`ELasticsearch`是一款非常强大的开源搜索引擎，具备非常多强大功能，可以帮助我们从海量数据中快速找到需要的内容，可以用来实现搜索、日志统计、分析、系统监控等功能。

`Kibana Logstash Beats`都是可替代的，只有`ElasticSearch`是不可替代的，它是`elastic stack`的核心，负责存储、搜索、分析数据。`ElasticSearch`的核心技术就是倒排索引。

![img](https://cdn.xn2001.com/img/2021/20210918202359.png)

### 15.1 `ElasticSearch`核心技术 —— 倒排索引

了解倒排索引之前，首先得了解下正向索引，因为倒排索引就是相较于正向索引而言的，而`MySQL`的查询就是基于正向索引。

下表`tb_goods`中，`id`为索引，若是根据`id`查询，在`MySQL`中直接走索引则查询速度是非常快的，但是许多情况下，我们需要做的是模糊查询，对于正向索引来所只能是逐行对数据进行扫描，扫描流程如下：

1. 用户搜索数据，搜索出所有`title`含有`手机`的记录
2. 逐行获取数据，比如`id`为`1`的数据
3. 判断该记录中的`title`是否符合用户搜索条件
4. 如果符合则将记录放入结果集，不符合则丢弃。

逐行扫描，也就是全表扫描，随着数据量增加，其查询效率也会越来越低。当数据量达到数百万时，其效率可想而知。所以就引出了倒排索引这种技术。

![img](https://cdn.xn2001.com/img/2021/20210918202527.png)

倒排所以有两个非常重要的概念：

1. **<font color="red">文档`document`</font>**：用来搜索的数据，其中的每一条数据就是一个文档。例如一个网页、一个商品信息
2. **<font color="red">词条`term`</font>**：对文档数据，利用某种分词算法对数据进行分词，得到的具备含义的词语就是词条，比如说：”我是中国人“这样一条数据，就可以划分为：`我`，`是`，`中国人`，`中国`，`国人`这样的几个词条。

**创建倒排索引**其实就是对正向索引的一种特殊处理，流程如下：

- 将每一个文档的数据利用算法进行分词，得到一个个词条
- 创建表，每行数据包括词条、词条所在的文档`id`、位置等信息
- 因为词条唯一性，可以给词条创建所以，例如`hash`表结构索引

**说得简单点就是：词条 ---> 包含词条的文档`id`**

![img](https://cdn.xn2001.com/img/2021/20210918203514.png)

**倒排索引的搜索流程**如下（以搜索"华为手机"为例）

1. 用户输入条件`"华为手机"`进行搜索
2. 对用户输入内容**分词**，得到词条：`华为`、`手机`
3. 拿着词条在倒排索引中查找，可以得到包含词条的文档`id`有`1、2、3`
4. 拿着文档`id`到正向索引中查找具体文档

![img](https://cdn.xn2001.com/img/2021/20210918203815.png)

**虽然要先查询倒排索引，再查询正向索引，但是词条和文档`id`都建立了索引，查询速度非常快！无需全表扫描。**

> - 为什么一个叫做正向索引，一个叫做倒排索引呢？
>
>   - **正向索引**是最传统的，根据`id`索引的方式，但根据词条查询时需要先逐个获取每一个文档，然后判断文档中是否包含所需要的词条，**是根据文档找词条的过程**。
>
>   - **倒排索引**则相反，是先找到用户要搜索的词条，根据得到的文档`id`获取该文档，**是根据词条找文档的过程**。

### 15.2 `ElasticSearch`文档和字段

`ElasticSearch`是面向文档`Document`存储的，可以是数据库中的一条商品数据，一个订单信息。文档数据会被序列化`JSON`格式然后存储在`ElasticSearch`中。而`JSON`文档中往往包含着很多的字段，其实就是类似数据库中的列。

![img](https://cdn.xn2001.com/img/2021/20210918212707.png)

### 15.3 `ElasticSearch`索引和映射

**索引`Index`**其实就是相同类型的文档的集合，所以可以把索引看作是数据库里面的表。例如：

- 所有用户文档，就可以组织在一起，称为用户的索引
- 所有商品的文档，可以组织在一起，称为商品的索引
- 所有订单的文档，可以组织在一起，称为订单的索引

![img](https://cdn.xn2001.com/img/2021/20210918213357.png)

**在`18.2`学习了文档跟字段，现在又学习了索引，就可以整合以下，索引里面包含着文档，文档里面有字段。跟以前学习的`MySQL`对比以下就是：索引相当于数据库表而文档相当于记录也就是行而字段就相当于列也就是属性。**

除此之外，我们知道数据库中会有一些约束信息，用来定义表的结构、字段名称、类型等信息。`ElasticSearch`也有这样的概念，在索引库中用映射`mapping`的概念来表示，映射是用于索引中文档的字段约束信息的。

### 15.4 `ElasticSearch`和`MySQL`

| **`MySQL`** | **`Elasticsearch`** | **说明**                                                     |
| :---------- | :------------------ | :----------------------------------------------------------- |
| `Table`     | `Index`             | 索引`(index)`，就是文档的集合，类似数据库的表`(table)`       |
| `Row`       | `Document`          | 文档`（Document）`，就是一条条的数据，类似数据库中的行`（Row）`，文档都是`JSON`格式 |
| `Column`    | `Field`             | 字段`（Field）`，就是`JSON`文档中的字段，类似数据库中的列`（Column）` |
| `Schema`    | `Mapping`           | 映射`（Mapping）`是索引中文档的约束，例如字段类型约束。类似数据库的表结构`（Schema）` |
| `SQL`       | `DSL`               | `DSL`是`ElasticSearch`提供的`JSON`风格的请求语句，用来操作`ElasticSearch`，实现`CRUD` |

- `Mysql`：擅长事务类型操作，可以确保数据的安全和一致性
- `ElasticSearch`：擅长海量数据的搜索、分析、计算

因此在企业中，往往是两者结合使用：

- 如果是对安全性要求较高的写操作，应交由`MySQL`
- 如果是对查询性能要求较高的搜索需求，应交由`ElasticSearch`
- 两者再基于某种方式，实现数据的同步，保证一致性

![img](https://cdn.xn2001.com/img/2021/20210918213631.png)

### 15.5 安装`ElasticSearch`

**添加网络：**

因为想使用`kibana`容器实现数据可视化，所以需要让`ElasticSearch`和`kibana`容器互联 ---> 形成一个互联网。

```sh
docker network create es-net
```

**加载镜像：**

这里我们采用`elasticsearch的7.12.1`版本的镜像，这个镜像体积非常大，接近`1G`。资料提供了镜像的`tar`包：上传到虚拟机中，然后运行命令加载即可：同理还有`kibana`的tar包也需要这样做。

```sh
docker load -i es.tar
docker load -i kibana.tar
```

**运行：**

运行`docker`命令，部署单点`elasticsearch`：

```sh
docker run -d \
	--name es \
    -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
    -e "discovery.type=single-node" \
    -v es-data:/usr/share/elasticsearch/data \
    -v es-plugins:/usr/share/elasticsearch/plugins \
    --privileged \
    --network es-net \
    -p 9200:9200 \
    -p 9300:9300 \
elasticsearch:7.12.1
```

命令解释：

- `-e "cluster.name=es-docker-cluster"`：设置集群名称
- `-e "http.host=0.0.0.0"`：监听的地址，可以外网访问
- `-e "ES_JAVA_OPTS=-Xms512m -Xmx512m"`：内存大小
- `-e "discovery.type=single-node"`：非集群模式
- `-v es-data:/usr/share/elasticsearch/data`：挂载逻辑卷，绑定`es`的数据目录
- `-v es-logs:/usr/share/elasticsearch/logs`：挂载逻辑卷，绑定`es`的日志目录
- `-v es-plugins:/usr/share/elasticsearch/plugins`：挂载逻辑卷，绑定`es`的插件目录
- `--privileged`：授予逻辑卷访问权
- `--network es-net` ：加入一个名为`es-net`的网络中
- `-p 9200:9200`：端口映射配置

这里我用的是`Virtual Box NAT`所以需要做一个端口映射然后，在浏览器中输入：http://192.168.56.1:9200 即可看到`elasticsearch`的响应结果：

![img](https://cdn.xn2001.com/img/2021/20210918214620.png)

### 15.6 安装`Kibana`

`kibana`可以给我们提供一个`elasticsearch`的可视化界面，便于我们学习。运行`docker`命令，部署`kibana`：

```sh
docker run -d \
--name kibana \
-e ELASTICSEARCH_HOSTS=http://es:9200 \
--network=es-net \
-p 5601:5601  \
kibana:7.12.1
```

- `--network es-net` ：加入一个名为`es-net`的网络中，与`elasticsearch`在同一个网络中
- `-e ELASTICSEARCH_HOSTS=http://es:9200"`：设置`elasticsearch`的地址，因为`kibana`已经与`elasticsearch`在一个网络，因此可以用容器名直接访问`elasticsearch`
- `-p 5601:5601`：端口映射配置

`kibana`启动一般比较慢，需要多等待一会，可以通过命令追踪启动日志：

```sh
docker logs -f kibana
```

接着在浏览器输入地址访问：http://192.168.56.1:5601即可看到结果：

![img](https://cdn.xn2001.com/img/2021/20210918214722.png)

`kibana`中提供了一个`DevTools`界面，这个界面中可以编写`DSL`来操作`ElasticSearch`。并且对`DSL`语句有自动补全功能。

访问http://192.168.56.1:5601/app/dev_tools#/console即可进入控制台界面。

### 15.7 安装`ik`分词插件

**在线安装`ik`分词插件：**

直接从`github`上下载的速度非常非常慢，推荐使用离线安装`ik`分词插件的方式：

```shell
# 进入容器内部
docker exec -it 36a9abb17b70 /bin/bash

# 在线下载并安装
./bin/elasticsearch-plugin  install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.12.1/elasticsearch-analysis-ik-7.12.1.zip

#退出
exit
#重启容器
docker restart elasticsearch
```

**离线安装`ik`分词插件：**

此前我们在运行`ElasticSearch`容器的时候就已经将数据卷挂在好了，可以看到我们将挂载点挂在到了`/usr/share/elasticsearch/data`和`/usr/share/elasticsearch/plugins`两个目录中：

```shell
docker run -d \
	--name es \
    -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
    -e "discovery.type=single-node" \
    -v es-data:/usr/share/elasticsearch/data \
    -v es-plugins:/usr/share/elasticsearch/plugins \
    --privileged \
    --network es-net \
    -p 9200:9200 \
    -p 9300:9300 \
elasticsearch:7.12.1
```

当然我们还可以使用`inspect`查看下插件挂载的数据卷位置：

```sh
docker volume inspect es-plugins
```

```json
[
    {
        "CreatedAt": "2022-05-06T10:06:34+08:00",
        "Driver": "local",
        "Labels": null,
        "Mountpoint": "/var/lib/docker/volumes/es-plugins/_data",
        "Name": "es-plugins",
        "Options": null,
        "Scope": "local"
    }
]
```

**解压缩分词器安装包：**

进入到`/var/lib/docker/volumes/es-plugins/_data`中，将资料中的`ik`分词器解压缩，重命名为`ik`，然后使用`xftp`将资料上传到该目录：

![img](https://cdn.xn2001.com/img/2021/20210918215615.png)

进入到`ElasticSearch`容器，可以看到挂载没问题，上传也没问题：

```shell
docker ps
docker exec -it 36a9abb17b70 bash
cd plugins/
ls
```

![img](https://img-blog.csdnimg.cn/c7ef4aa2e78048839031824fb01b9986.png)

**重启容器：**

```shell
docker restart es
docker logs -f es
```

**测试分词器分词：**

`ik`分词器包含两种模式：

* `ik_smart`：最少切分
* `ik_max_word`：最细切分

使用`ik_max_word`：

```json
GET /_analyze
{
  "analyzer":"ik_max_word",
  "text":"恰同学少年，风华正茂"
}
```

```json
{
  "tokens" : [
    {
      "token" : "恰",
      "start_offset" : 0,
      "end_offset" : 1,
      "type" : "CN_CHAR",
      "position" : 0
    },
    {
      "token" : "同学",
      "start_offset" : 1,
      "end_offset" : 3,
      "type" : "CN_WORD",
      "position" : 1
    },
    {
      "token" : "少年",
      "start_offset" : 3,
      "end_offset" : 5,
      "type" : "CN_WORD",
      "position" : 2
    },
    {
      "token" : "风华正茂",
      "start_offset" : 6,
      "end_offset" : 10,
      "type" : "CN_WORD",
      "position" : 3
    },
    {
      "token" : "风华",
      "start_offset" : 6,
      "end_offset" : 8,
      "type" : "CN_WORD",
      "position" : 4
    },
    {
      "token" : "正",
      "start_offset" : 8,
      "end_offset" : 9,
      "type" : "CN_CHAR",
      "position" : 5
    },
    {
      "token" : "茂",
      "start_offset" : 9,
      "end_offset" : 10,
      "type" : "CN_CHAR",
      "position" : 6
    }
  ]
}
```

使用`ik_smart`：

```shell
GET /_analyze
{
  "analyzer":"ik_smart",
  "text":"恰同学少年，风华正茂"
}
```

```shell
{
  "tokens" : [
    {
      "token" : "恰",
      "start_offset" : 0,
      "end_offset" : 1,
      "type" : "CN_CHAR",
      "position" : 0
    },
    {
      "token" : "同学",
      "start_offset" : 1,
      "end_offset" : 3,
      "type" : "CN_WORD",
      "position" : 1
    },
    {
      "token" : "少年",
      "start_offset" : 3,
      "end_offset" : 5,
      "type" : "CN_WORD",
      "position" : 2
    },
    {
      "token" : "风华正茂",
      "start_offset" : 6,
      "end_offset" : 10,
      "type" : "CN_WORD",
      "position" : 3
    }
  ]
}
```

**扩展词词典：**

随着互联网的发展，“造词运动”也越发的频繁。出现了很多新的词语，在原有的词汇列表中并不存在。比如：“奥力给”，“传智播客” 等。

所以我们的词汇也需要不断的更新，`ik`分词器提供了扩展词汇的功能。

1. 打开`ik`分词器`config`目录，在`IKAnalyzer.cfg.xml`配置文件内容添加：


![img](https://cdn.xn2001.com/img/2021/20210918221159.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
        <comment>IK Analyzer 扩展配置</comment>
        <!--用户可以在这里配置自己的扩展字典 *** 添加扩展词典-->
        <entry key="ext_dict">ext.dic</entry>
</properties>
```

2. 新建一个`ext.dic`，可以参考`config`目录下复制一个配置文件进行修改

```properties
奥力给
麦乐鸡侠
```

3. 重启`ElasticSearch`

```sh
docker restart es
docker logs -f elasticsearch
```

4. 测试效果：

```json
GET /_analyze
{
  "analyzer":"ik_max_word",
  "text":"麦乐鸡侠奥力给！"
}
```

```shell
{
  "tokens" : [
    {
      "token" : "麦乐鸡侠",
      "start_offset" : 0,
      "end_offset" : 4,
      "type" : "CN_WORD",
      "position" : 0
    },
    {
      "token" : "奥力给",
      "start_offset" : 4,
      "end_offset" : 7,
      "type" : "CN_WORD",
      "position" : 1
    }
  ]
}
```

> 注意当前文件的编码必须是`UTF-8`格式，严禁使用`Windows`记事本编辑

**禁用词词典：**

在互联网项目中，在网络间传输的速度很快，所以很多语言是不允许在网络上传递的，如：关于宗教、政治等敏感词语，那么我们在搜索时也应该忽略当前词汇。

`ik`分词器也提供了强大的停用词功能，让我们在索引时就直接忽略当前的停用词汇表中的内容。

1. `IKAnalyzer.cfg.xml`配置文件内容添加：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
   <properties>
           <comment>IK Analyzer 扩展配置</comment>
           <!--用户可以在这里配置自己的扩展字典-->
           <entry key="ext_dict">ext.dic</entry>
            <!--用户可以在这里配置自己的扩展停止词字典  *** 添加停用词词典-->
           <entry key="ext_stopwords">stopword.dic</entry>
   </properties>
   ```

2. 在`stopword.dic`添加停用词

   ```xml
   习大大
   ```

3. 重启`ElasticSearch `

   ```shell
   # 重启服务
   docker restart elasticsearch
   docker restart kibana
   
   # 查看 日志
   docker logs -f elasticsearch
   ```

4. 测试效果：

   ```shell
   GET /_analyze
   {
     "analyzer":"ik_max_word",
     "text":"习大大奥力给！"
   }
   ```

   ```shell
   {
     "tokens" : [
       {
         "token" : "奥力给",
         "start_offset" : 3,
         "end_offset" : 6,
         "type" : "CN_WORD",
         "position" : 0
       }
     ]
   }
   ```

### 15.8 操作索引库之`Mapping`

索引库我们之前学习过就相当于`MySQL`数据库中的表，而`Mapping`映射就相当于是表结构`schema`，所以操作映射就是在操作`ES`的索引结构：

`mapping`是对索引库中文档的约束，常见的`mapping`属性包括：

- `type`：字段数据类型，常见的简单类型有
  - 字符串：`text`（可分词的文本）、`keyword`（精确值，例如：品牌、国家、`ip`地址）
  - 数值：`long、integer、short、byte、double、float`
  - 布尔：`boolean`
  - 日期：`date`
  - 对象：`object`

- `index`：是否创建索引，默认为`true`
- `analyzer`：使用哪种分词器，可以是`ik_max_word`也可以是`ik_smart`
- `properties`：该字段的子字段

```json
{
    "age": 21,
    "weight": 52.1,
    "isMarried": false,
    "info": "小桥流水人家",
    "email": "abcdefg@163.com",
    "score": [99.1, 99.5, 98.9],
    "name": {
        "firstName": "李",
        "lastName": "德"
    }
}
```

对应德每个字段映射`mapping`如下：

- `age`：类型为`integer`，`index`为`true`所以参与搜索，无需分词器
- `weight`：类型为`float`，`index`为`true`所以参与搜索，无需分词器
- `isMarried`：类型为`boolean`，`index`为`true`所以参与搜索，无需分词器
- `info`：类型为字符串，需要分词，因此是`text`，参与搜索，`index`为`true`，分词器可以用`ik_smart`
- `email`：类型为字符串，但是不需要分词，因此是`keyword`，不参与搜索，因此设置`index`为`false`，无需分词器
- `score`：虽然是数组，**但是我们只看元素的类型**，类型为`float`，参与搜索，`index`为`true`，无需分词器
- `name`：类型为`object`，需要定义多个子属性
  - `name.firstName`：类型为字符串，不需要分词，`keyword`；参与搜索，`index`为`true`；无需分词器
  - `name.lastName`：类型为字符串，不需要分词，`keyword`；参与搜索，`index`为`true`；无需分词器

下面的`value`是我自己添加的不用理会：

```json
{
    "age": {
        "type":"integer",
        "index":"true",
        "value":"21"
    }
    "weight": {
    	"type":"float",
    	"index":"true",
    	"value":"52.1"
	},
    "isMarried": {
        "type":"boolean",
        "index":"true",
        "value":"false"
    },
    "info": {
        "type":"text",
        "index":"true",
        "analyzer":"ik_max_word"
        "value":"小桥流水人家"  
    },
    "email": {
        "index":"false",
        "value":"abcdefg@163.com"
    },
    "score": {
        "type":"float",
        "index":"true",
        "value":[99.1, 99.5, 98.9]
    }
    "name": {
        "type":"text"
        "properties":{
    		"firstName": {
        		"type":"keyword"
        		"value":"李"
    		}
	        "lastName": "德"   
         }
    }
}
```

### 15.9 增删改查索引库

了解了`mapping`是怎么一回事就可以创建索引库和映射了，创建索引库和映射的示例如下：**索引库**就相当于表，**映射**相当于表结构，这里面的`properties`就相当于一个个的属性即列在这里表示**字段**，合在一起形成了行也就是记录在这里表示的是**文档**。

**`index mapping document term`就是`ElasticSearch`最重要的四要素。**

```json
PUT /索引库名称
{
  "mappings": {
    "properties": {
      "字段名":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "字段名2":{
        "type": "keyword",
        "index": "false"
      },
      "字段名3":{
        "properties": {
          "子字段": {
            "type": "keyword"
          }
        }
      }
      // ...略
    }
  }
}
```

需求：创建一张名为`/kk`的索引，要求有：`info email name`三个字段，`info`参与搜索和分词且类型为`text`，`email`不参与搜索类型为关键字`keyword`，`name`有两个子字段一个为`firstName`另一个为`lastName`，类型均为`keyword`：

```json
PUT /kk
{
    "mappings":{
        "properties":{
            "info":{
                "type":"text",
                "index":"true",
                "analyzer":"ik_smart"
            },
            "email":{
                "type":"keyword",
                "index":"false"
            },
            "name":{
                "properties":{
                    "firstName":{
                        "type":"keyword"
                    },
                    "lastName":{
                        "type":"keyword"
                    }
                }
            }
        }
    }
}
```

执行，可以看到索引创建成功：

![img](https://img-blog.csdnimg.cn/383f52d086a64ab3b0353f1f9878afc2.png)

我们使用一个真实的数据库来创建一个索引库：

![img](https://cdn.xn2001.com/img/2021/20210919164626.png)

可以看到表名为：`tb_hotel`对应着索引名`/hotel`，字段数据类型可以参考表结构`schema`的名称和类型，不参与搜索的比如经纬度还有酒店图片这些都不用参与搜索，分不分词看内容，内容是一个整体比如`id`就不用分词，分词器我们可以选择`ik_max_word`：

```json
PUT /hotel
{
  "mappings":{
    "properties":{
      "id":{
        "type":"keyword",
        "index":"true"
      },
      "name":{
        "type":"text",
        "index":"true",
        "analyzer":"ik_max_word"
      },
      "address":{
        "type":"keyword",
        "index":"false"
      },
      "price":{
        "type":"integer"
      },
      "score":{
        "type":"integer"
      },
      "brand":{
        "type":"keyword",
        "copy_to":"all"
      },
      "city":{
        "type":"keyword",
        "copy_to":"all"
      },
      "starName":{
        "type":"keyword"
      },
      "business":{
        "type":"keyword"
      },
      "location":{
        "type":"geo_point"
      },
      "pic":{
        "type":"keyword",
        "index":"false"
      },
      "all":{
        "type":"text",
        "analyzer":"ik_max_word"
      }
    }
  }
}
```

特殊字段说明：

- `location`：地理坐标，里面包含精度、纬度
- `all`：一个组合字段，其目的是将多字段的值利用 `copy_to` 合并，提供给用户搜索，这样一来就只需要搜索一个字段就可以得到结果，性能更好。

> ES中支持两种地理坐标数据类型：
>
> - `geo_point`：由纬度`（latitude）`和经度`（longitude）`确定的一个点。例如：`"32.8752345, 120.2981576"`
> - `geo_shape`：有多个`geo_point`组成的复杂几何图形。例如一条直线，`"LINESTRING (-77.03653 38.897676, -77.009051 38.889939)"`

**修改索引库：**

**<font color="red">索引库一旦创建就无法修改</font>**，但是允许添加新的字段到`mapping`中。添加字段的语句如下：

```json
PUT /索引库名/_mapping
{
  "properties": {
    "新字段名":{
      "type": "integer"
    }
  }
}
```

比如往`/kk`索引添加字段`age`：

```json
PUT /kk/_mapping
{
  "properties":{
    "age":{
      "type":"keyword"
    }
  }
}
```

**删除索引库：**

```json
DELETE /索引库名
```

**查询索引库：**

```json
GET /索引库名
```

### 15.10 `ElasticSearch`文档操作

这些操作就是`DSL`语句，类似于`SQL`语句：

**增添文档：**

文档`Document`相当于数据库中的一条条记录，创建文档的结构如下：

```json
POST /索引库名/_doc/文档id
{
    "字段1": "值1",
    "字段2": "值2",
    "字段3": {
        "子属性1": "值3",
        "子属性2": "值4"
    }
    // ...
}
```

比如往`/kk`索引库中添加一个文档：

```json
POST /kk/_doc/1
{
  "info":"我不会Java",
  "email":"java@qq.com",
  "name":{
    "firstname":"张",
    "lastname":"三"
  }
}
```

执行，结果如下：

```json
{
  "_index" : "kk",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 1,
  "result" : "created",
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "_seq_no" : 0,
  "_primary_term" : 1
}
```

**修改文档：**

修改文档有两种方式，一种是全量修改，另外一种是增量修改。

- 全量修改：删除原来的文档，然后新增一个相同`id`的修改后的文档

  ```json
  PUT /{索引库名}/_doc/id
  {
      "字段1": "值1",
      "字段2": "值2",
      // ... 略
  }
  ```

  ```json
  PUT /kk/_doc/1
  {
    "info":"我不会program",
    "email":"c@qq.com",
    "name":{
      "firstname":"李",
      "lastname":"四"
    }
  }
  ```

- 增量修改：只修改匹配`id`文档中的部分字段

  ```json
  POST /{索引库名}/_update/文档id
  {
      "doc": {
           "字段名": "新的值",
      }
  }
  ```

  ```json
  POST /kk/_update/1
  {
    "doc":{
      "name":{
        "firstname":"王",
        "lastname":"五"
      }
    }
  }
  ```

**删除文档：**

```json
DELETE /{索引库名}/_doc/{id}
```

**查询文档：**

```json
GET /{索引库名称}/_doc/{id}
```

### 15.11 使用`RestClient`操作`ElasticSearch`

`ES`官方提供了各种不同语言的客户端，用来操作`ES`。这些客户端的本质就是组装 `DSL`语句，通过`http`请求发送给`ES`。官方文档地址：https://www.elastic.co/guide/en/elasticsearch/client/index.html。

`ElasticSearchRestHighLevel`需要引入版本号，在`8.x`的`ElasticSearch`中已经被弃用了，新版本使用的是`ElasticSearch Java API Client`。这里由于`ElasticSearch`的版本是`7.12.1`的所以还是可以使用`Java Rest Client`。

![img](https://cdn.xn2001.com/img/2021/20210919234405.png)

后面将使用 `Java HighLevel Rest Client`操作`ElasticSearch`。

#### 15.11.1 初始化`hotel-demo`项目

1. 首先得在`mysql`中创建数据库和表，将酒店表的数据拉进去【资料有】

2. 创建`hotel-demo`，`pom.xml`如下：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.7.2</version>
           <relativePath/> <!-- lookup parent from repository -->
       </parent>
       <groupId>com.kk</groupId>
       <artifactId>03-hotel-demo</artifactId>
       <version>1.0</version>
   
       <properties>
           <java.version>1.8</java.version>
           <elasticsearch.version>7.12.1</elasticsearch.version>
       </properties>
   
       <dependencies>
           <!--amqp-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-amqp</artifactId>
           </dependency>
           <dependency>
               <groupId>org.elasticsearch.client</groupId>
               <artifactId>elasticsearch-rest-high-level-client</artifactId>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
           <dependency>
               <groupId>com.baomidou</groupId>
               <artifactId>mybatis-plus-boot-starter</artifactId>
               <version>3.4.2</version>
           </dependency>
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version>8.0.28</version>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <optional>true</optional>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
               <exclusions>
                   <exclusion>
                       <groupId>org.junit.vintage</groupId>
                       <artifactId>junit-vintage-engine</artifactId>
                   </exclusion>
               </exclusions>
           </dependency>
           <!--FastJson-->
           <dependency>
               <groupId>com.alibaba</groupId>
               <artifactId>fastjson</artifactId>
               <version>1.2.71</version>
           </dependency>
           <dependency>
               <groupId>org.apache.commons</groupId>
               <artifactId>commons-lang3</artifactId>
           </dependency>
       </dependencies>
   
       <build>
           <plugins>
               <plugin>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-maven-plugin</artifactId>
                   <configuration>
                       <excludes>
                           <exclude>
                               <groupId>org.projectlombok</groupId>
                               <artifactId>lombok</artifactId>
                           </exclude>
                       </excludes>
                   </configuration>
               </plugin>
           </plugins>
       </build>
   </project>

3. 修改配置文件`application.yml`：

   ```yaml
   server:
     port: 80
   spring:
     datasource:
       driver-class-name: com.mysql.cj.jdbc.Driver
       url: jdbc:mysql://localhost:3306/es?useSSL=false&serverTimezone=Asia/Shanghai
       username: root
       password: 123456
     rabbitmq:
       host: 192.168.56.1
       port: 5672
       username: admin
       password: 123456
       virtual-host: /
   logging:
     level:
       com.kk.hotel: debug
     pattern:
       dateformat: yyyy-MM-dd HH:mm:ss
   mybatis-plus:
     configuration:
       map-underscore-to-camel-case: true
     type-aliases-package: com.kk.hotel.pojo
   ```

4. 创建`pojo.Hotel`实体类

   ```java
   package com.kk.hotel.pojo;
   
   import com.baomidou.mybatisplus.annotation.IdType;
   import com.baomidou.mybatisplus.annotation.TableId;
   import com.baomidou.mybatisplus.annotation.TableName;
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   //解决实体名和表名映射问题
   @TableName(value = "tb_hotel")
   public class Hotel {
       //解决主键自动生成问题，IdType.INPUT 表示用户自定义输入类型
       @TableId(value = "id", type = IdType.INPUT)
       private Long id;
       private String name;
       private String address;
       private Integer price;
       private Integer score;
       private String brand;
       private String city;
       private String starName;
       private String business;
       private String longitude;
       private String latitude;
       private String pic;
   }
   ```

5. 创建`HotelMapper`接口继承`BaseMapper`

   ```java
   package com.kk.hotel.mapper;
   
   import com.baomidou.mybatisplus.core.mapper.BaseMapper;
   import com.kk.hotel.pojo.Hotel;
   import org.apache.ibatis.annotations.Mapper;
   
   @Mapper
   public interface HotelMapper extends BaseMapper<Hotel> {
   }
   ```

6. 创建`HotelService`接口以及实现类`HotelServiceImpl`

   ```java
   package com.kk.hotel.service;
   
   import com.baomidou.mybatisplus.extension.service.IService;
   import com.kk.hotel.pojo.Hotel;
   
   public interface HotelService extends IService<Hotel>  {
   }
   ```

   ```java
   package com.kk.hotel.service.impl;
   
   import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
   import com.kk.hotel.mapper.HotelMapper;
   import com.kk.hotel.pojo.Hotel;
   import com.kk.hotel.service.HotelService;
   import org.springframework.stereotype.Service;
   
   @Service
   public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService {
   }

7. 创建`Web`层：`HotelController`

   ```java
   package com.kk.hotel.controller;
   
   import com.kk.hotel.service.HotelService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   
   @RestController
   @RequestMapping(value = "/hotel")
   public class HotelController {
       @Autowired
       private HotelService hotelService;
   }
   ```

8. 创建启动类：

   ```java
   package com.kk.hotel;
   
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   
   @SpringBootApplication
   public class HotelApplication {
       public static void main(String[] args) {
           SpringApplication.run(HotelApplication.class, args);
       }
   }
   ```

9. 因为该项目是用来练习操作`ElasticSearch`的所以需要再创建一个文档实体类，该文档中所有的字段都根据`Hotel`实体类生成：

   ```java
   package com.kk.hotel.pojo;
   
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   @Data
   @NoArgsConstructor
   public class HotelDoc {
       private Long id;
       private String name;
       private String address;
       private Integer price;
       private Integer score;
       private String brand;
       private String city;
       private String starName;
       private String business;
       private String location;
       private String pic;
   
       public HotelDoc(Hotel hotel) {
           this.id = hotel.getId();
           this.name = hotel.getName();
           this.address = hotel.getAddress();
           this.price = hotel.getPrice();
           this.score = hotel.getScore();
           this.brand = hotel.getBrand();
           this.city = hotel.getCity();
           this.starName = hotel.getStarName();
           this.business = hotel.getBusiness();
           this.location = hotel.getLatitude() + ", " + hotel.getLongitude();
           this.pic = hotel.getPic();
       }
   }
   ```

#### 15.11.2 初始化`RestHighLevelClient`

在`ElasticSearch`提供的`API`中，`ElasticSearch`一切交互都封装在一个名为`RestHighLevelClient`的类中，必须先完成这个对象的初始化，建立与`ElasticSearch`的连接。

```xml
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.12.1</version>
</dependency>
```

也可以集中管理版本：

```xml
<properties>
    <java.version>1.8</java.version>
    <elasticsearch.version>7.12.1</elasticsearch.version>
</properties>
```

创建配置类，引入`RestHighLevelClient`，交由`Spring`容器管理该对象：

```java
package com.kk.hotel.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESConfiguration {
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.56.1:9200")));
    }
}
```

如果你是单纯的测试使用可以在测试类中使用`@BeforeEach`创建对象在`@AfterEach`中销毁。

```java
package com.kk.hotel;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class HotelTest {

    private RestHighLevelClient restHighLevelClient;

    public static String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"address\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"score\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"brand\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"city\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"starName\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"business\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"location\":{\n" +
            "        \"type\": \"geo_point\"\n" +
            "      },\n" +
            "      \"pic\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";


    @BeforeEach
    public void createRestHighLevelClient() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.56.1:9200")));
    }

    @AfterEach
    public void CloseRestHighLevelClient() throws IOException {
        if (this.restHighLevelClient != null) restHighLevelClient.close();
    }
}
```

#### 15.11.3 使用`RestHighLevelClient`创建、删除、判断索引是否存在

测试使用`RestHighLevelClient`创建映射：

```java
package com.kk.hotel;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class HotelTest {

    private RestHighLevelClient restHighLevelClient;

    public static String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"address\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"price\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"score\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"brand\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"city\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"starName\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"business\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"location\":{\n" +
            "        \"type\": \"geo_point\"\n" +
            "      },\n" +
            "      \"pic\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";


    @BeforeEach
    public void createRestHighLevelClient() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.56.1:9200")));
    }

    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("hotel");//相当于 PUT /hotel
        createIndexRequest.source(HotelTest.MAPPING_TEMPLATE, XContentType.JSON);//映射中内容 JSON 格式
        restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);//使用 RestHighLevelClient 创建映射
    }

    @AfterEach
    public void CloseRestHighLevelClient() throws IOException {
        if (this.restHighLevelClient != null) restHighLevelClient.close();
    }
}
```

可以看到创建映射成功：

![img](https://img-blog.csdnimg.cn/5d9e6b94145b44a2aaf4ddc2564c4390.png)

测试使用`RestHighLevelClient`删除索引：

```java
@Test
public void testDeleteIndex() throws IOException {
    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("hotel");
    restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
}
```

![img](https://img-blog.csdnimg.cn/47635712c84d40feb638080292fbd5dd.png)

测试判断索引库是否存在：

```java
@Test
public void testExistIndex() throws IOException {
    GetIndexRequest getIndexRequest = new GetIndexRequest("hotel");
    boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    System.out.println(exists);
}
```

#### 15.11.4 使用`RestHighLevelClient`增删改查文档

**创建文档：**

文档也好索引也，操作时需要根据`MySQL`数据库中的信息创建，不要随意创建以防数据错乱。所以这里创建文档我们随便以一条记录为例，例如：`id = 61083`的酒店信息。【在此之前需要测试下使用`MyBatis-Plus`操作数据库是否是通的，这里就不做这个演示了】

注意这里转化为字符串，需要使用：`JSON.toJSONString()`否则到时候取数据为`null`，因为格式不对。

```java
@Test
public void testCreateDoc() throws IOException {
    Hotel hotel = hotelService.getById(61083L);
    HotelDoc hotelDoc = new HotelDoc(hotel);
    IndexRequest indexRequest = new IndexRequest("hotel").id(hotelDoc.getId().toString());
    indexRequest.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
    restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
}
```

![img](https://img-blog.csdnimg.cn/3da03e0799c043e4affe23ea7f6f4a3a.png)

**删除文档：**

```java
@Test
public void testDeleteDoc() throws IOException {
    DeleteRequest deleteRequest = new DeleteRequest("hotel").id(String.valueOf(61083L));
    restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
}
```

![img](https://img-blog.csdnimg.cn/76dc4f196be44e9b949beacfa1070ccf.png)

**查询文档：**

```java
@Test
public void testGetDoc() throws IOException {
    GetRequest getRequest = new GetRequest("hotel").id(String.valueOf(61083L));
    GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
    String responseSourceAsString = getResponse.getSourceAsString();//获取内容字符串 ---> 转换为对象
    HotelDoc hotelDoc = JSON.parseObject(responseSourceAsString, HotelDoc.class);
    System.out.println(hotelDoc);
}
```

**修改文档：**

说过在`ES`中修改有两种全量修改跟增量修改，全量修改其实相当于新增，而增量修改就是局部，全量修改在`RestHighLevleClient`中跟新增文档是一样的，一摸一样，所以这里学习增量修改即可。

```java
@Test
public void testUpdateDoc() throws IOException {
    UpdateRequest updateRequest = new UpdateRequest("hotel", "61083");
    updateRequest.doc("price", "99999", "starName", "四钻");
    restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
}
```

**批量导入文档：**

平常使用时肯定不会一条条的存储，肯定都是批量的导入。批量导入需要使用`BulkRequest`。`BulkRequest`相当于一个专门存储`Request`的容器。

```java
public void testBulkDoc() throws IOException {
    BulkRequest bulkRequest = new BulkRequest();
    Long[] hotelIds = {56977L, 60922L, 395799L, 636080L, 2011785622L};
    for (int i = 0; i < hotelIds.length; i++) {
        Hotel hotel = hotelService.getById(hotelIds[i]);
        HotelDoc hotelDoc = new HotelDoc(hotel);
        bulkRequest.add(new IndexRequest("hotel").id(hotelDoc.getId().toString()).source(JSON.toJSONString(hotelDoc), XContentType.JSON));
    }
    restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
}
```

![img](https://img-blog.csdnimg.cn/5e1b73487169482b8b38bc10da5027e4.png)

**<font color="red">细心的你一定可以发现，索引相关的操作都是`CreateIndexRequest GetIndexRequest DeleteIndexRequest`三个单词组成，而文档相关的操作都是`IndexRequest GetRequest DeleteRequest`两个单词组成，因为文档是包在索引里面的，有索引才有文档，索引大，所以索引用三个单词，文档用两个单词。</font>**

### 15.12 `DSL`文档查询【重点】

`ElasticSearch`提供了基于`JSON`的`DSL(Domain Specific Language)`来定义查询。常见的查询类型包括：

1. **查询所有：**查询所有数据，一般测试使用。例如：`match_all`
2. **全文检索：**即`full text`查询，利用分词器对用户输入内容进行分词，然后使用倒排索引在索引库中匹配，例如：`match_query`以及`multi_match_query`
3. **精确查询：**根据精确词条值查找数据，一般就是查找`keyword`、数值、日期、`boolean`等类型字段，例如：`ids range term`
4. **地理查询：**即`geo`查询，根据经纬度查询。例如：`geo_distance、geo_bounding_box`
5. **复合查询：**即`compound`查询，复合查询可以将上述的各种查询条件组合起来，合并查询条件。例如：`bool、function_score`

#### 15.12.1 查询所有

```json
// 查询所有
GET /indexName/_search
{
  "query": {
    "match_all": {
    }
  }
}
```

![img](https://img-blog.csdnimg.cn/593e8698108341b0b704c8889818cfe3.png)

#### 15.12.2 全文检索

全文检索查询的基本流程如下：

- 对用户搜索的内容做分词，得到词条
- 根据词条去倒排索引库中匹配得到文档`id`
- 根据文档`id`获取文档然后返回给用户

比较常见的应用场景如：百度输入框搜索、电商商城的输入框搜索 ---> 输入词条 ---> 类型为`text` ---> 分词器分词 ---> 倒排索引库检索

常见的全文检索包括：【<font color="red">注：**字段**就相当于是**`MySQL`中的列**，而且查询的字段类型需为`text`，`hotel`索引库只有`all name`两个字段是`text`的类型，其余不是，所以你在其余字段中查询就算一模一样也是查询不出来的。`all`里面包含着：`name brand city`，所以要想全文检索品牌和城市可以在`all`字段中检索</font>】

- `match`：单字段查询，也就是只查询一个字段中是否符合条件
- `multi_match`：多字段查询，也就是在多个字段中查询是否符合条件

比如使用单字段查询`name`字段中包含`世界`的酒店：

```json
GET /hotel/_search
{
  "query":{
    "match":{
      "name":"世界"
    }
  }
}
```

再比如使用多字段查询，查询出`name`或者`brand`有世界二字的酒店：

```json
GET /hotel/_search
{
    "query":{
        "multi_match":{
            "fields":["name", "all"],
            "query":"深圳"
        }
    }
}
```

- 可以看到多字段查询`multi_match`其实是包含了单字段查询`match`的，所以你可以直接用多字段进行全文。

- 但是检索的字段越多，其性能影响也会越来越大，所以我们尽量在创建影视`mapping`的时候就是使用`copy_to`将多个字段合并为一个字段比如：`"copy_to":"all"`，然后使用单字段查询。

  ```json
  GET /hotel/_search
  {
      "query":{
          "match":{
              "all":"深圳"
          }
      }
  }
  ```

#### 15.12.3 精确查询

精确查询一般是查询特定的某个关键字，比如`keyword`类型、数值、日期`boolean`等类型的字段，所以不会对搜索条件进行分词，进行分词的只有`text`。

- `term`：根据词条精确查询

  因为精确查询的字段搜是不分词的字段，因此查询的条件也必须是**不分词**的词条。查询时，用户输入的内容跟自动值完全匹配时才认为符合条件。如果用户输入的内容过多，反而搜索不到数据。

  而且只支持单个字段的查询，比如我即要`id = `又要`brand = `就查询不到

  ```json
  GET /hotel/_search
  {
      "query":{
          "term":{
              "id":{
                  "value":"60487"
              }
          }
      }
  }
  ```

  这样子是查询不到的：

  ```json
  GET /hotel/_search
  {
    "query":{
      "term":{
        "id":{
          "value":"60487"
        },
        "brand":{
          "value":"君悦"
        }
      }
    }
  }
  ```

- `range`：根据值得范围查询

  范围查询，一般应用在对数值类型做范围过滤的时候。比如做价格范围过滤、日期范围的过滤等。

  ```json
  GET /hotel/_search
  {
    "query":{
      "range":{
        "price":{
          "gte":"2500",
          "lte":"3000"
        }
      }
    }
  }
  ```

  ![img](https://cdn.xn2001.com/img/2021/20210921182858.png)

#### 15.12.4 地理坐标查询

地理坐标查询，其实就是根据经纬度查询，官方文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-queries.html

常见的使用场景包括：

- 携程：搜索我附近的酒店

  ![img](https://cdn.xn2001.com/img/2021/20210921183030.png)

- 滴滴：搜索我附近的出租车

  ![img](https://cdn.xn2001.com/img/2021/20210921183033.png)

- 微信：搜索我附近的人

**矩形范围查询：**

矩形范围查询，也就是`geo_bounding_box`查询，查询坐标落在某个矩形范围的所有文档，查询时，需要指定矩形的**左上**、**右下**两个点的坐标，然后画出一个矩形，落在该矩形内的都是符合条件的点。

![img](https://cdn.xn2001.com/img/2021/20210921183124.gif)

```json
GET /hotel/_search
{
  "query":{
    "geo_bounding_box":{
      "location":{
        "top_left":{
          "lat":31.1,
          "lon":121.5
        },
        "bottom_right":{
          "lat":30.9,
          "lon":121.7
        }
      }
    }
  }
}
```

**附近查询：**

附近查询，也叫做距离查询`（geo_distance）`：查询到指定中心点小于某个距离值的所有文档。

在地图上找一个点作为圆心，以指定距离为半径，画一个圆，落在圆内的坐标都算符合条件：

![img](https://cdn.xn2001.com/img/2021/20210921183215.gif)

查询深圳会展中心方圆`100km`的酒店：会展中心的经纬度为`(22.31, 114.03)`

```json
GET /hotel/_search
{
  "query":{
    "geo_distance":{
      "distance":"100km",
      "location":"22.31,114.03"
    }
  }
}
```

可以发现查询到：`56`家酒店

![img](https://img-blog.csdnimg.cn/e45be03a9d024222acf67946459d4440.png)

将其范围缩小到`25km`方圆之内：可以看到之查询到`6`家酒店

![img](https://img-blog.csdnimg.cn/b600142c853e4126ac71302123b38b32.png)

除此之外还可以针对这些查询的酒店结果，做一个排序得到与当前位置与各个酒店之间的距离：

```json
GET /hotel/_search
{
    "query":{
        "geo_distance":{
            "distance":"25km",
            "location":"22.70,113.73"
        }
    },
    "sort":[
        "_geo_distance":{
        	"location":"22.70,113.73",
        	"order":"asc",
        	"unit":"km"
        }
    ]
}
```

可以看到当前位置和深圳会展中心的希尔顿酒店仅大约有`5km`的距离。

![img](https://img-blog.csdnimg.cn/9f2597c1941f4a6e9b5e5e077df1fb8c.png)

#### 15.12.5 复合查询

复合`（compound）`查询：复合查询可以将其它简单查询组合起来，实现更复杂的搜索逻辑。

- `fuction score`：算分函数查询，可以控制文档相关性算分，控制文档排名
- `bool query`：布尔查询，利用逻辑关系组合多个其它的查询，实现复杂搜索

**相关性算分：**

当我们利用`match`查询时，文档结果会根据与搜索词条的关联度打分`(_score)`，返回结果时按照分值降序排列。例如，我们搜索 "虹桥如家"，结果如下：

```json
[
  {
    "_score" : 17.850193,
    "_source" : {
      "name" : "虹桥如家酒店真不错",
    }
  },
  {
    "_score" : 12.259849,
    "_source" : {
      "name" : "外滩如家酒店真不错",
    }
  },
  {
    "_score" : 11.91091,
    "_source" : {
      "name" : "迪士尼如家酒店真不错",
    }
  }
]
```

`ElasticSearch`早期使用的打分算法是 **`TF-IDF`算法**，公式如下：

![img](https://cdn.xn2001.com/img/2021/20210921205752.png)

在后来的`5.1`版本升级中，`ElasticSearch`将算法改进为**`BM25`算法**，公式如下：

![img](https://cdn.xn2001.com/img/2021/20210921205757.png)

`TF-IDF`算法有一各缺陷，就是词条频率越高，文档得分也会越高，单个词条对文档影响较大。而`BM25`则会让单个词条的算分有一个上限，曲线更加平滑。

![img](https://cdn.xn2001.com/img/2021/20210921205831.png)

##### 15.12.5.1 算分函数查询

根据相关度打分是比较合理的需求，但有时候也不能够满足我们的需求。以百度为例，你搜索的结果中，并不是相关度越高排名越靠前，而是谁给的钱多排名就越靠前。

**要想制相关性算分，就需要利用`ElasticSearch`中的 `function score`查询。**

![img](https://cdn.xn2001.com/img/2021/20210921210256.png)

`function score`查询中包含四部分内容：

- **原始查询**条件：`query`部分，基于这个条件搜索文档，并且基于`BM25`算法给文档打分，**原始算分**`（query score)`
- **过滤条件**：`filter`部分，符合该条件的文档才会**重新算分**
- **算分函数**：符合`filter`条件的文档要根据这个函数做运算，得到的函数算分`（function score）`，有四种函数：
  - **`weight`**：函数结果是常量
  - **`field_value_factor`**：以文档中的某个字段值作为函数结果
  - **`random_score`**：以随机数作为函数结果
  - **`script_score`**：自定义算分函数算法
- **运算模式**：算分函数的结果、原始查询的相关性算分，两者之间的运算方式，包括：
  - **`multiply`**：相乘
  - **`replace`**：用`function score`替换`query score`
  - **`sum、avg、max、min`**

`function score`的运行流程如下：

1. 根据**原始条件**查询搜索文档，并且计算相关性算分，称为**原始算分**`（query score）`
2. 根据**过滤条件**，过滤文档
3. 符合**过滤条件**的文档，基于**算分函数**运算，得到**函数算分**`（function score）`
4. 将**原始算分**`（query score）`和**函数算分**`（function score）`基于**运算模式**做运算，得到最终结果，作为相关性算分。

因此最关键的就是三个点：过滤条件、算分函数、运算模式

- 过滤条件：决定哪些文档的算分被修改
- 算分函数：决定函数算分的算法
- 运算模式：决定最终算分结果

例如：因为如家给我们打钱了，所以让品牌为`如家`的酒店排名靠前一些

```json
GET /hotel/_search
{
    "query":{
        //原始算分
        "function_score":{
            //原始查询，可以是任意条件比如全文检索、精确查询、地理位置查询等
            "query":{
                
            },
            //算分函数
            ”functions”：[
            	{
    	        	//过滤上述原始查询的文档，这里填写过滤条件
	            	"filter":{
            			"term":{
	        	    		"brand":"如家"            
				        }
			        },
	    			//算分权重，为10
    				"weight":"10"
				}
	        ],
			/原始算分和函数算分得到的结果的运算模式：这里为加权模式
			"boost_mode":"sum"
        }
    }
}
```

**<font color="red">其实很好理解，就是查询文档得到原始算分，然后过滤文档通过算分函数得到函数算分，原始算分和函数算分通过运算模式得到最终算分。</font>**

比如我这里单纯的查询深圳的酒店：可以看到排名第一的是一家叫君越的酒店

```json
GET /hotel/_search
{
  "query": {
    "match": {
      "all": "深圳"
    }
  }
}
```

![img](https://img-blog.csdnimg.cn/db795be870fd4b399f295c0fd37bddaa.png)

当我们做了算分函数查询之后：

```json
GET /hotel/_search
{
  "query": {
    "function_score": {
      "query": {
        "multi_match": {
          "fields": ["all"],
          "query": "深圳"
        }
      },
      "functions": [
        {
          "filter": {
            "term": {
              "brand": "如家"
            }
          },
          "weight": "10"
        }
      ],
      "boost_mode": "sum"
    }
  }
}
```

![img](https://img-blog.csdnimg.cn/6a0177d081284eb38a2afaaf1dbfac1c.png)

可以看到如家酒店的排名一下就上去了，而且`score`是原始算分的基础上`+10`得到的。

##### 15.12.5.2 布尔查询

布尔查询是一个或多个查询子句的组合，每一个子句就是一个**子查询**。子查询的组合方式有

- **`must`**：必须匹配每个子查询，类似“与”
- **`should`**：选择性匹配子查询，类似“或”
- **`must_not`**：必须不匹配，**不参与算分**，类似“非”
- **`filter`**：必须匹配，**不参与算分**

比如在搜索酒店时，除了关键字搜索外，我们还可能根据品牌、价格、城市等字段做过滤，**每一个不同的字段，其查询的条件、方式都不一样，必须是多个不同的查询，而要组合这些查询，就必须用`bool`查询了。**需要注意的是，搜索时，**<font color="red">参与打分的字段越多，查询的性能也越差</font>**。因此这种多条件查询时，建议这样做：

- 搜索框的关键字搜索，是全文检索查询，使用`must`查询，参与算分
- 其它过滤条件，采用`filter`查询，不参与算分

例如：查询城市在上海，酒店品牌为皇冠假日或者华美达，并且价格一定在`500`以上且评分在`45`分以上：

```json
GET /hotel/_search
{
    "query":{
        "bool":{
            "must":[
                {
                	"term":{
                		"city":{
                			"value":"上海"
                		}
                	}
                }
            ],
            "should":[
                {
					        "term":{
                		"city":{
                			"value":"皇冠假日"
                		}
					        }
                },
                {
					        "term":{
                		"brand":{
                			"value":"华美达"
                		}
					        }
                }
            ],
            "must_not":[
                {
                  "range":{
                    "price":{
                      "lte":500
                    }
                  }
                }
            ],
            "filter":[
                {
                  "range":{
                    "score":{
                      "gte":"45"
                    }
                  }
                }
            ]
        }
    }
}
```

需求：搜索名字包含“如家”，价格不高于`400`，在坐标`31.21,121.5`周围`10km`范围内的酒店。除此之外对以该坐标为中心到酒店的距离做一个排序，最近的排最前面。

- 名称搜索，属于全文检索查询，应该参与算分，放到`must`中
- 价格不高于`400`，用`range`查询，属于过滤条件，不参与算分，放到`must_not`中
- 周围`10km`范围内，用`geo_distance`查询，属于过滤条件，不参与算分，放到`filter`中
- 排序使用`sort`

```json
GET /hotel/_search
{
  "query":{
    "bool":{
        "must":[
          {
            "multi_match":{
              "fields":["name"],
              "query":"如家"
            }  
          }
        ],
        "must_not":[
          {
            "range":{
              "price":{
                "gte":"400"
              }
            }
          }
        ],
        "filter":[
          {
            "geo_distance":{
              "distance":"10km",
              "location":"31.21,121.5"
            }
          }
        ]
    }
  },
  "sort":[
    {
      "_geo_distance":{
        "location":"31.21,121.5",
        "order":"asc",
        "unit":"km"
      }
    }
  ]
}
```

![img](https://img-blog.csdnimg.cn/6b20d9e07dca42bcb3b209fbff6d28ef.png)

### 15.13 搜索结果处理

#### 15.13.1 排序

`ElasticSearch`默认是根据相关度算分`（_score）`来排序，但是也支持自定义方式对搜索结果排序，官方文档在：https://www.elastic.co/guide/en/elasticsearch/reference/current/sort-search-results.html。可以排序字段类型有：`keyword`类型、数值类型、地理坐标类型、日期类型等。`keyword`、数值、日期类型排序的语法基本一致。

```json
GET /indexName/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "FIELD": "desc"  // 排序字段、排序方式ASC、DESC
    }
  ]
}
```

排序条件是一个数组，也就是可以写多个排序条件。按照声明的顺序，当第一个条件相等时，再按照第二个条件排序。

需求描述：酒店数据按照用户评价`（score)`降序排序，评价相同的按照价格`(price)`升序排序。

```json
GET /hotel/_search
{
  "query":{
    "match_all":{}
  },
  "sort":[
    {
      "score":{
        "order":"desc"
      }
    },
    {
      "price":{
        "order":"asc"
      }
    }
  ]
}
```

可以看到查询出来的结果，会先按照`score`进行降序排序，如果`score`相同再按照`price`进行升序排序。

![img](https://img-blog.csdnimg.cn/b8a97e2e7e4e4d29a2064cb794e62ecb.png)

按地理位置到指定经纬坐标的距离进行排序：

```json
GET /hotel/_search
{
  "query":{
    "match_all":{}
  },
  "sort":[
    {
      "_geo_distance":{
        "location":"31.034661,121.612282",
        "order":"desc",
        "unit":"km"
      }
    }
  ]
}
```

![img](https://img-blog.csdnimg.cn/0d05985d07ba460e97c8df215c58818a.png)

> 经纬度获取链接：https://lbs.amap.com/demo/jsapi-v2/example/map/click-to-get-lnglat

#### 15.13.2 分页

`ElasticSearch` 默认情况下只返回`top10`的数据。而如果要查询更多数据就需要修改分页参数了。

`ElasticSearch`通过修改`from`和`size`参数来控制要返回的分页结果：类似`MySQL`中的`limit`。

- `form`：表示从第几个文档开始
- `size`：总共查询多少个文档

```json
GET /hotel/_search
{
  "query":{
    "match_all":{
      
    }
  },
  "from": 0,
  "size": 201
}
```

查询`990~1000`的数据：

```json
GET /hotel/_search
{
  "query":{
    "match_all":{
      
    }
  },
  "from": 990,
  "size": 10
}
```

**<font color="red">注意：`ElasticSearch`内部分页时，必须先查询`0~1000`条，然后截取其中的`990 ~ 1000`的这`10`条数据。</font>**

![img](https://cdn.xn2001.com/img/2021/20210921234503.png)

查询`TOP1000`，如果`es`是单点模式，这并无太大影响。但是`ElasticSearch`将来一定是集群，例如我集群有5个节点，我要查询`TOP1000`的数据，并不是每个节点查询`200`条就可以了。节点`A`的`TOP200`，在另一个节点可能排到`10000`名以外了。

**因此要想获取整个集群的`TOP1000`，必须先查询出每个节点的`TOP1000`，汇总结果后，重新排名，重新截取`TOP1000`。**

![img](https://cdn.xn2001.com/img/2021/20210921234555.png)

**<font color="red">当查询分页深度较大时，汇总数据过多，对内存和`CPU`会产生非常大的压力，因此 `ElasticSearch`会禁止`from+ size`超过`10000`的请求。</font>**

![img](https://img-blog.csdnimg.cn/de5fde12819d42a3bdb283df182fd17f.png)

针对深度分页，`ES`提供了两种解决方案，[官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/current/paginate-search-results.html)：

- `search after`：分页时需要排序，原理是从上一次的排序值开始，查询下一页数据。官方推荐使用的方式。
- `scroll`：原理将排序后的文档id形成快照，保存在内存。官方已经不推荐使用。

分页查询的常见实现方案以及优缺点

- **`from + size`【用的最多】**
  - 优点：支持随机翻页
  - 缺点：深度分页问题，默认查询上限（`from + size`）是`10000`
  - 场景：百度、京东、谷歌、淘宝这样的随机翻页搜索
- **`after search`**
  - 优点：没有查询上限（单次查询的`size`不超过`10000`）
  - 缺点：只能向后逐页查询，不支持随机翻页
  - 场景：没有随机翻页需求的搜索，例如手机向下滚动翻页
- **`scroll`**
  - 优点：没有查询上限（单次查询的`size`不超过`10000`）
  - 缺点：会有额外内存消耗，并且搜索结果是非实时的
  - 场景：海量数据的获取和迁移。从`ES7.1`开始不推荐，建议用`after search`方案。

#### 15.13.3 高亮

我们在百度，京东搜索时，关键字会变成红色，比较醒目，这叫高亮显示：

![img](https://cdn.xn2001.com/img/2021/20210921234711.png)

高亮显示的实现分为两步：

1. 给文档中的所有关键字都添加一个标签，例如`<em>`标签
2. 页面给`<em>`标签编写`CSS`样式

前提条件：能分词才会有高亮即查询的数据类型为`text`

```json
GET /hotel/_search
{
  "query":{
    "match":{
      "all":"如家"
    }
  },
  "highlight":{
    "fields":{
      "name":{
        "pre_tags": "<em>",
        "post_tags": "</em>"
      }
    }
  }
}
```

如果要对非搜索字段高亮，则需要添加一个属性：`required_field_match=false`

```json
GET /hotel/_search
{
  "query":{
    "match":{
      "all":"如家"
    }
  },
  "highlight":{
    "fields":{
      "name":{
        "pre_tags": "<em>",
        "post_tags": "</em>"
      }
    },
    "require_field_match": "false"
  }
}
```

### 15.14 `DSL`总体结构如下

![img](https://cdn.xn2001.com/img/2021/20210921235330.png)

### 15.15 使用`RestHighLevelClient`进行文档查询

1. 用`RestHighLevelClient`进行文档查询使用：`SearchRequest`即可。这对应着第一行：`GET /hotel/_search`
2. 对于里面的参数用`searchRequest.source().query(QueryBuilders.matchAllQuery());`表示，对应的语句为：`"query":{"match_all":{}}`，
3. 发送请求：`SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);`

![img](https://cdn.xn2001.com/img/2021/20211016170057.png)

```java
@Test
public void testSearchDoc() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.matchAllQuery()).from(0).size(200);
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHit[] hits = searchResponse.getHits().getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

**`match`查询：单字段匹配查询**

```java
@Test
public void testSearchDocMatch() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.matchQuery("name", "如家")).from(0).size(100);
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHit[] hits = searchResponse.getHits().getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

**`multimatch`查询：多字段查询**

```java
@Test
public void testSearchDocMultiMatch() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.multiMatchQuery("君悦", "name", "all")).from(0).size(200);
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    System.out.println("所有符合条件的酒店数量：" + searchResponse.getHits().getTotalHits().value);
    SearchHit[] hits = searchResponse.getHits().getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

**`term`词条匹配精确查询：**

```java
@Test
public void testSearchDocTerm() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.termQuery("brand", "君悦"));
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    System.out.println("酒店数量：" + searchResponse.getHits().getTotalHits().value + " 家");
    SearchHit[] hits = searchResponse.getHits().getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

**`range`范围精确查询：**

```java
@Test
public void testSearchDocRange() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.rangeQuery("price").gte(2500).lte(3000));
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    System.out.println("酒店数量：" + searchResponse.getHits().getTotalHits().value + " 家");
    SearchHit[] hits = searchResponse.getHits().getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

**`bool`布尔查询：**

例如：酒店必须在深圳并且品牌是如家或者汉庭或者是希尔顿，且价格不低于`300`，并且距离于`22.70,113.70`不超过`5km`且按顺序显示距离值。

```java
GET /hotel/_search
{
  "query":{
    "bool":{
      "must":[
        {
          "match":{
            "name":"如家汉庭希尔顿"
          }
        }
      ],
      "must_not": [
        {
          "range":{
            "price":{
              "lte":300
            }
          }
        }
      ], 
      "filter":[
        {
          "geo_distance":{
            "distance":"5km",
            "location":"22.53,114.06"
          }
        }
      ]
    }
  },
  "sort":[
    {
      "_geo_distance":{
        "location":"22.53,114.06",
        "order":"asc",
        "unit":"km"
      }
    }
  ],
  "from":"0",
  "size":"200"
}
```

```java
@Test
public void testSearchDocBool() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("name","如家汉庭希尔顿"))
                    .mustNot(QueryBuilders.rangeQuery("price").lte(300))
                    //GeoDistance.ARC精确查询 || GeoDistance.PLANE模糊查询
                    .filter(QueryBuilders.geoDistanceQuery("location").distance(3, DistanceUnit.KILOMETERS).point(22.53D, 114.06D).geoDistance(GeoDistance.ARC)))
            .from(0)
            .size(200);
    searchRequest.source().sort(SortBuilders.geoDistanceSort("location",new GeoPoint(22.53D, 114.06D)).unit(DistanceUnit.KILOMETERS).order(SortOrder.ASC));
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    System.out.println("酒店数量：" + searchResponse.getHits().getTotalHits().value + " 家");
    SearchHit[] hits = searchResponse.getHits().getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        double distance = new BigDecimal(String.valueOf(hit.getSortValues()[0])).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println("距离目的地大约" + distance + "公里 ---> " + hotelDoc);
    }
}
```

**排序、分页：**

搜索最便宜的十家酒店：

```java
@Test
public void testSearchDocSortAndPage() throws IOException {
    int page = 1, size = 10;
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().sort("price", SortOrder.ASC);
    searchRequest.source().from((page - 1) * size).size(size);
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHit[] hits = searchResponse.getHits().getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

**高亮：**

会自动给分词添加`<em></em>`标签。

```
@Test
public void testSearchDocHighLight() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.matchQuery("name", "汉庭"))
            .highlighter(new HighlightBuilder().field("name").requireFieldMatch(true));
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHit[] hits = searchResponse.getHits().getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        Map<String, HighlightField> highLightFieldMap = hit.getHighlightFields();
        if (!CollectionUtils.isEmpty(highLightFieldMap)) {
            HighlightField highlightField = highLightFieldMap.get("name");
            if (highlightField != null) {
                String name = highlightField.getFragments()[0].string();
                hotelDoc.setName(name);
            }
        }
        System.out.println(hotelDoc);
    }
}
```

**地理坐标查询：**

```java
GET /hotel/_search
{
  "query":{
    "geo_distance":{
      "location":"22.53,114.06",
      "distance":"5km"
    }
  },
  "sort": [
    {
      "_geo_distance": {
        "location":"22.53,114.06",
        "unit": "km", 
        "order": "asc"
      }
    }
  ]
}
```

```java
@Test
public void testSearchDocGeo() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.geoDistanceQuery("location").distance(5, DistanceUnit.KILOMETERS).point(new GeoPoint("22.53,114.06")));
    searchRequest.source().sort(SortBuilders.geoDistanceSort("location", new GeoPoint("22.53,114.06")).order(SortOrder.ASC).unit(DistanceUnit.KILOMETERS));
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHit[] hits = searchResponse.getHits().getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

**算分函数查询：**

```json
GET /hotel/_search
{
  "query":{
    "function_score":{
      "query":{
        "match_all":{}
      },
      "functions":[
        {
          "filter":{
            "term":{
              "brand":"汉庭"
            }
          },
          "weight": 10
        }
      ],
      "boost_mode": "sum"
    }
  },
  "from": 0,
  "size": 200
}
```

```java
@Test
public void testSearchDocFunctionScore() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().query(QueryBuilders.functionScoreQuery(QueryBuilders.matchAllQuery(),
            new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("brand", "汉庭"), ScoreFunctionBuilders.weightFactorFunction(10))
            }).boostMode(CombineFunction.SUM));
    searchRequest.source().from(0).size(200);
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHit[] hits = searchResponse.getHits().getHits();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        System.out.println(hotelDoc);
    }
}
```

### 15.16 使用`ElasticSearch`查询酒店

第一个`bug`：需要重新`clean`下然后在`target`中才会出现`static`相关资源，否则访问不到。![img](https://img-blog.csdnimg.cn/705fc8cbd78a4dc3bd255437f0541732.png)

#### 15.16.1 基本搜索和分页

1. 定义类，接收前端请求参数

   ```java
   package com.kk.hotel.pojo;
   
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class RequestParam {
       private String key;//搜索关键字
       private Integer page;//当前页码 (page - 1) * size
       private Integer size;//显示条数
       private String sortBy;//排序字段
   }
   ```

2. 定义分页的结果类：这里的列表名称必须为`hotels`否则前端拿不到，改的话前端都要改相当麻烦，所以还是乖乖写`hotels`吧

   ```java
   package com.kk.hotel.pojo;
   
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   import java.util.List;
   
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class PageResult {
       private Long total;
       private List<HotelDoc> hotels;
   }
   ```

3. 定义`Controller`接口接收前端请求：

   ```java
   @PostMapping(value = "/list")
   public PageResult search(@RequestBody RequestParam requestParam) {
       return hotelService.search(requestParam);
   }

4. 添加`service`接口方法

   ```java
   package com.kk.hotel.service;
   
   import com.baomidou.mybatisplus.extension.service.IService;
   import com.kk.hotel.pojo.Hotel;
   import com.kk.hotel.pojo.PageResult;
   import com.kk.hotel.pojo.RequestParam;
   
   public interface HotelService extends IService<Hotel>  {
       PageResult search(RequestParam requestParam);
   }

5. 实现类实现`search`方法：完成全文搜索功能

   ```java
   package com.kk.hotel.service.impl;
   
   import com.alibaba.fastjson.JSON;
   import com.baomidou.mybatisplus.extension.api.R;
   import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
   import com.kk.hotel.mapper.HotelMapper;
   import com.kk.hotel.pojo.Hotel;
   import com.kk.hotel.pojo.HotelDoc;
   import com.kk.hotel.pojo.PageResult;
   import com.kk.hotel.pojo.RequestParam;
   import com.kk.hotel.service.HotelService;
   import org.elasticsearch.action.search.SearchRequest;
   import org.elasticsearch.action.search.SearchResponse;
   import org.elasticsearch.client.RequestOptions;
   import org.elasticsearch.client.RestHighLevelClient;
   import org.elasticsearch.index.query.QueryBuilders;
   import org.elasticsearch.search.SearchHit;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Service;
   import org.springframework.util.CollectionUtils;
   
   import java.io.IOException;
   import java.util.ArrayList;
   import java.util.List;
   
   @Service
   public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService {
   
       @Autowired
       private RestHighLevelClient restHighLevelClient;
   
       @Override
       public PageResult search(RequestParam requestParam) {
           try {
               //实现全文检索
               int page = requestParam.getPage();
               int size = requestParam.getSize();
               String key = requestParam.getKey();
               SearchRequest searchRequest = new SearchRequest("hotel");
               //健壮性判断 ---> 如果 key 为空或者为空字符串则查询全部，否则进行全文检索
               if (key == null || "".equals(key))
                   searchRequest.source().query(QueryBuilders.matchAllQuery()).from((page - 1) * size).size(size);
               else searchRequest.source().query(QueryBuilders.matchQuery("name", key)).from((page - 1) * size).size(size);
               SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
               return handleSearchResult(searchResponse);
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }
   
       //封装处理请求的函数
       public PageResult handleSearchResult(SearchResponse searchResponse) {
           PageResult pageResult = new PageResult();
           pageResult.setTotal(searchResponse.getHits().getTotalHits().value);
           SearchHit[] hits = searchResponse.getHits().getHits();
           List<HotelDoc> hotelDocList = new ArrayList<>();
           for (SearchHit hit : hits) {
               String sourceAsString = hit.getSourceAsString();
               HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
               hotelDocList.add(hotelDoc);
           }
           pageResult.setHotels(hotelDocList);
           return pageResult;
       }
   }
   ```

#### 15.16.2 过滤

实现过滤条件：完成按条件筛选功能 ---> 城市、星级、品牌、价格

修改`RequestParams`，添加：`brand city starName minPrice maxPrice`

```java
package com.kk.hotel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestParam {
    private String key;//搜索关键字
    private Integer page;//当前页码 (page - 1) * size
    private Integer size;//显示条数
    private String sortBy;//排序字段
    private String brand;//品牌
    private String city;//城市
    private String startName;//星级
    private Integer minPrice;//最小价格
    private Integer maxPrice;//最大价格
}
```

- `city`:精确匹配
- `brand`：精确匹配
- `starName`：精确匹配
- `price`：范围匹配

多个条件组合在一起使用布尔查询且为`AND`关系，并且不为空才需要添加，为空不添加。

注意：`BoolQueryBuilder`应该使用同一个，如果你在每一个`query()`中使用的都是`QueryBuilders.boolQuery()`则会覆盖，不信可以结尾打印下`SearchRequest`

```java
package com.kk.hotel.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.hotel.mapper.HotelMapper;
import com.kk.hotel.pojo.Hotel;
import com.kk.hotel.pojo.HotelDoc;
import com.kk.hotel.pojo.PageResult;
import com.kk.hotel.pojo.RequestParam;
import com.kk.hotel.service.HotelService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public PageResult search(RequestParam requestParam) {
        try {
            //实现全文检索
            int page = requestParam.getPage();
            int size = requestParam.getSize();
            SearchRequest searchRequest = new SearchRequest("hotel");
            //按条件过滤信息
            handlerFilter(requestParam, searchRequest);
            searchRequest.source().from((page - 1) * size).size(size);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return handleSearchResult(searchResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //过滤
    public void handlerFilter(RequestParam requestParam, SearchRequest searchRequest) {
        if (requestParam != null) {
            String key = requestParam.getKey();
            //健壮性判断 ---> 如果 key 为空或者为空字符串则查询全部，否则进行全文检索
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (key == null || "".equals(key))
                searchRequest.source().query(boolQuery.must(QueryBuilders.matchAllQuery()));
            else searchRequest.source().query(boolQuery.must(QueryBuilders.matchQuery("name", key)));
            String city = requestParam.getCity();
            String brand = requestParam.getBrand();
            String starName = requestParam.getStarName();
            Integer minPrice = requestParam.getMinPrice();
            Integer maxPrice = requestParam.getMaxPrice();
            String location = requestParam.getLocation();
            //健壮性判断 ---> 如果 key 为空或者为空字符串则查询全部，否则进行全文检索
            if (city != null && !"".equals(city)){
                searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("city", city)));
            }
            if (brand != null && !"".equals(brand)) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("brand", brand)));
            }
            if (starName != null && !"".equals(starName)) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("starName", starName)));
            }
            if (minPrice != null && maxPrice != null) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.rangeQuery("price").lte(maxPrice).gte(minPrice)));
            }
            if (location != null && !"".equals(location)) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.rangeQuery("").lte(maxPrice).gte(minPrice)));
            }
            //System.out.println(searchRequest.toString());
        }
    }

    //封装处理请求的函数
    public PageResult handleSearchResult(SearchResponse searchResponse) {
        PageResult pageResult = new PageResult();
        pageResult.setTotal(searchResponse.getHits().getTotalHits().value);
        SearchHit[] hits = searchResponse.getHits().getHits();
        List<HotelDoc> hotelDocList = new ArrayList<>();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            hotelDocList.add(hotelDoc);
        }
        pageResult.setHotels(hotelDocList);
        return pageResult;
    }
}
```

#### 18.16.3 我附近的酒店

根据坐标排个序，最前面显示最近距离的酒店

```java
if (location != null && !"".equals(location)) {
    searchRequest.source().sort(SortBuilders.geoDistanceSort("location", new GeoPoint(location)).unit(DistanceUnit.KILOMETERS).order(SortOrder.ASC));
}
```

#### 15.16.4 显示距离

```java
//封装处理请求的函数
public PageResult handleSearchResult(SearchResponse searchResponse) {
    PageResult pageResult = new PageResult();
    pageResult.setTotal(searchResponse.getHits().getTotalHits().value);
    SearchHit[] hits = searchResponse.getHits().getHits();
    List<HotelDoc> hotelDocList = new ArrayList<>();
    for (SearchHit hit : hits) {
        String sourceAsString = hit.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
        if(hit.getSortValues().length > 0) hotelDoc.setDistance(hit.getSortValues()[0]);
        hotelDocList.add(hotelDoc);
    }
    pageResult.setHotels(hotelDocList);
    return pageResult;
}
```

#### 15.16.5 广告置顶

```json
# 给索引库新增一个叫isAD的字段，类型是布尔类型
PUT /hotel/_mapping
{
  "properties":{
    "isAD":{
      "type": "boolean"
    }
  }
}

# 给索引库id为45845的记录赋值，让其isAD字段为true（用于测试广告竞价排名，该记录会靠前）
POST /hotel/_update/45845
{
  "doc": {  
    "isAD":true
  }
}


GET hotel/_doc/45845
```

给有广告的进行算分函数加权即可：

```java
//过滤
public void handlerFilter(RequestParam requestParam, SearchRequest searchRequest) {
    if (requestParam != null) {
        String key = requestParam.getKey();
        //健壮性判断 ---> 如果 key 为空或者为空字符串则查询全部，否则进行全文检索
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (key == null || "".equals(key))
            searchRequest.source().query(boolQuery.must(QueryBuilders.matchAllQuery()));
        else searchRequest.source().query(boolQuery.must(QueryBuilders.matchQuery("name", key)));
        String city = requestParam.getCity();
        String brand = requestParam.getBrand();
        String starName = requestParam.getStarName();
        Integer minPrice = requestParam.getMinPrice();
        Integer maxPrice = requestParam.getMaxPrice();
        String location = requestParam.getLocation();
        //健壮性判断 ---> 如果 key 为空或者为空字符串则查询全部，否则进行全文检索
        if (city != null && !"".equals(city)){
            searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("city", city)));
        }
        if (brand != null && !"".equals(brand)) {
            searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("brand", brand)));
        }
        if (starName != null && !"".equals(starName)) {
            searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("starName", starName)));
        }
        if (minPrice != null && maxPrice != null) {
            searchRequest.source().query(boolQuery.filter(QueryBuilders.rangeQuery("price").lte(maxPrice).gte(minPrice)));
        }
        if (location != null && !"".equals(location)) {
            searchRequest.source().sort(SortBuilders.geoDistanceSort("location", new GeoPoint(location)).unit(DistanceUnit.KILOMETERS).order(SortOrder.ASC));
        }
        FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(boolQuery, new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("isAD", true), ScoreFunctionBuilders.weightFactorFunction(10))
        });
        searchRequest.source().query(functionScoreQuery);
    }
}
```

![img](https://img-blog.csdnimg.cn/a9257d54a2354c45928d6b9e417749d1.png)

**记得在`HotelDoc`实体类添加`isAD`属性。**

### 15.17 `DSL`数据聚合

**聚合**可以让我们极其方便的实现对数据的统计、分析、运算。

- 什么品牌的手机最受欢迎？
- 这些手机的平均价格、最高价格、最低价格？
- 这些手机每月的销售情况如何？

在`ElasticSearch`实现这些统计功能比数据库的`SQL`要方便的多，而且查询速度非常快，可以实现近实时搜索效果。

常见的聚合有三类：

- 桶聚合`Bucket Aggregations`：用来对文档进行分组
  - `TermAggregation`：按照文档字段值分组，例如按照品牌值分组、按照国家分组
  - `Date Histogram`：按照日期阶梯分组，例如一周为一组，或者一月为一组
- 度量聚合`Metric Aggregations`：用来计算一些值，比如**最大值最小值平均值**等。
  - `Avg`：求平均值
  - `Max`：求最大值
  - `Min`：求最小值
  - `Stats`：同时求`max、min、avg、sum`等
- 管道聚合`Pipeline Aggregations`：用来聚合其它聚合的结果【聚合的聚合】

<font color="red">**注意**：参加聚合的字段必须是`keyword`、日期、数值、布尔类型</font>

#### 15.17.1 桶聚合`Bucket Aggregation`

例如：我们要统计所有数据中的酒店品牌有几种，其实就是按照品牌对数据分组。此时可以根据酒店品牌的名称做聚合，也就是`Bucket`聚合。

```json
GET /hotel/_search
{
  "size": 0,//结果不显示文档，只包含聚合结果
  "aggs":{//定义聚合
    "brandAggregation":{//聚合名称：给聚合结果起名字
      "terms":{//聚合类型：文档类型 + 日期类型
        "field":"brand",//参与聚合的字段
        "size":100//聚合结果数量
      }
    }
  }
}
```

结果如下：

![img](https://cdn.xn2001.com/img/2021/20211022184007.png)

默认情况下，`Bucket`聚合会统计`Bucket`内的文档数量，记为`_count`，并且按照`_count`降序排序。你可以在上图看到这一事实，当然我们也可以指定`order`属性，自定义聚合的排序方式。

```json
GET /hotel/_search
{
  "size":0,
  "aggs":{
    "brandAggregation":{
      "terms":{
         "field":"brand",
         "order":{
           "_count":"asc"
         },
         "size": 20
      }
    }
  }
}
```

默认情况下，Bucket 聚合是对索引库的所有文档做聚合，但真实场景下，用户会输入搜索条件，因此聚合必须是对搜索结果聚合。那么聚合必须添加限定条件。我们可以限定要聚合的文档范围，只要添加`query`条件即可；

```json
GET /hotel/_search
{
  "query":{
    "range":{
      "price":{
        "gte": 2000
      }
    }
  },
  "size":0,
  "aggs":{
    "brandAggregation":{
      "terms":{
         "field":"brand",
         "order":{
           "_count":"asc"
         },
         "size": 20
      }
    }
  }
}
```

可以看到聚合的品牌明显变少了：

![img](https://img-blog.csdnimg.cn/5f45f337fc7f4371acfe9627bd14d108.png)

#### 15.17.2 度量聚合`Metric Aggregation`

上面，我们对酒店按照品牌分组，形成了一个个桶。现在我们需要对桶内的酒店做运算，获取每个品牌的用户评分的`min、max、avg`等值，使用`stats`就是包含了所有的类型包括`count max min avg sum`。

```json
GET /hotel/_search
{
  "size": 0,
  "aggs":{
    "brand_aggs":{
      "terms":{
        "field":"brand",
        "size": 20
      },
      "aggs":{
        "score_aggs":{
          "stats":{
            "field":"score"
          }
        }
      }
    }
  }
}
```

![img](https://cdn.xn2001.com/img/2021/20211022184847.png)

```json
GET /hotel/_search
{
  "size": 0,
  "aggs":{
    "brand_aggs":{
      "terms":{
        "field":"brand",
        "size": 20
      },
      "aggs":{
        "max_price":{
          "max":{
            "field":"price"
          }
        }
      }
    }
  }
}
```

#### 15.17.3 使用`RestClient`执行数据聚合

```java
@Test
public void testSearchDocAggregation() throws IOException {
    SearchRequest searchRequest = new SearchRequest("hotel");
    searchRequest.source().aggregation(AggregationBuilders.terms("brand_aggregation").field("brand").size(20));
    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    Aggregations aggregations = searchResponse.getAggregations();
    Terms brandAggregation = aggregations.get("brand_aggregation");
    List<? extends Terms.Bucket> buckets = brandAggregation.getBuckets();
    for (Terms.Bucket bucket : buckets) {
        String keyAsString = bucket.getKeyAsString();
        System.out.println(keyAsString);
    }
}
```

#### 15.17.4 多条件聚合

需求：搜索页面中的品牌、城市等信息不应该是写死的而是索引库中有什么把它传递给前端页面，而完成这些需要使用到聚合索引。

![img](https://img-blog.csdnimg.cn/8838273a22cb45f4826d73dbda40d3ad.png)

```java
GET /hotel/_search
{
  "size": 0,
  "aggs":{
    "brand_aggs":{
      "terms":{
        "field":"brand"
      }
    },
    "city_aggs":{
      "terms":{
        "field": "city"
      }
    },
    "starName_aggs":{
      "terms":{
        "field":"starName"
      }
    }
  }
}
```

接着我们的`hotel_demo`继续做，在`HotelService`中定义一个`Map<String, List<String>> filters`方法，用于返还索引库中存在的：品牌、城市、星级。

```java
@Override
public Map<String, List<String>> filters() {
    try {
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().aggregation(AggregationBuilders.terms("brand_aggregation").field("brand"));
        searchRequest.source().aggregation(AggregationBuilders.terms("city_aggregation").field("city"));
        searchRequest.source().aggregation(AggregationBuilders.terms("starName_aggregation").field("starName"));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms brandAggregation = aggregations.get("brand_aggregation");
        List<? extends Terms.Bucket> brandBuckets = brandAggregation.getBuckets();
        List<String> brandList = getList(brandBuckets);
        Terms cityAggregation = aggregations.get("city_aggregation");
        List<? extends Terms.Bucket> cityBuckets = cityAggregation.getBuckets();
        List<String> cityList = getList(cityBuckets);
        Terms starNameAggregation = aggregations.get("starName_aggregation");
        List<? extends Terms.Bucket> starNameBuckets = starNameAggregation.getBuckets();
        List<String> starNameList = getList(starNameBuckets);
        Map<String, List<String>> map = new HashMap<>();
        map.put("city", cityList);
        map.put("brand", brandList);
        map.put("starName", starNameList);
        return map;
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}
public List<String> getList(List<? extends Terms.Bucket> buckets) {
    List<String> keywordList = new ArrayList<>();
    for (Terms.Bucket bucket : buckets) {
        String keyAsString = bucket.getKeyAsString();
        keywordList.add(keyAsString);
    }
    return keywordList;
}
```

#### 15.17.5 带过滤条件的多条件聚合

对过滤出来的条件应该还要结合搜索框，字段等信息进行过滤。所以应该在`filter`中加上参数，使用`search()`中的代码。比如`1500`元价格以上的酒店只有`喜来登、希尔顿、皇冠假日、维也纳`四个品牌，那在品牌一来只该显示这四个品牌而不能显示其它品牌。

![img](https://img-blog.csdnimg.cn/599b43f3f02b4684b5daf0606b08a33e.png)

**主要是：`filters getList handlerFilter`三个方法**

```java
package com.kk.hotel.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.hotel.mapper.HotelMapper;
import com.kk.hotel.pojo.Hotel;
import com.kk.hotel.pojo.HotelDoc;
import com.kk.hotel.pojo.PageResult;
import com.kk.hotel.pojo.RequestParam;
import com.kk.hotel.service.HotelService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.lucene.search.function.ScoreFunction;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements HotelService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public PageResult search(RequestParam requestParam) {
        try {
            //实现全文检索
            int page = requestParam.getPage();
            int size = requestParam.getSize();
            SearchRequest searchRequest = new SearchRequest("hotel");
            //按条件过滤信息
            handlerFilter(requestParam, searchRequest);
            searchRequest.source().from((page - 1) * size).size(size);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return handleSearchResult(searchResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, List<String>> filters(RequestParam requestParam) {
        try {
            SearchRequest searchRequest = new SearchRequest("hotel");
            handlerFilter(requestParam, searchRequest);
            searchRequest.source().aggregation(AggregationBuilders.terms("brand_aggregation").field("brand"));
            searchRequest.source().aggregation(AggregationBuilders.terms("city_aggregation").field("city"));
            searchRequest.source().aggregation(AggregationBuilders.terms("starName_aggregation").field("starName"));
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Terms brandAggregation = aggregations.get("brand_aggregation");
            List<? extends Terms.Bucket> brandBuckets = brandAggregation.getBuckets();
            List<String> brandList = getList(brandBuckets);
            Terms cityAggregation = aggregations.get("city_aggregation");
            List<? extends Terms.Bucket> cityBuckets = cityAggregation.getBuckets();
            List<String> cityList = getList(cityBuckets);
            Terms starNameAggregation = aggregations.get("starName_aggregation");
            List<? extends Terms.Bucket> starNameBuckets = starNameAggregation.getBuckets();
            List<String> starNameList = getList(starNameBuckets);
            Map<String, List<String>> map = new HashMap<>();
            map.put("city", cityList);
            map.put("brand", brandList);
            map.put("starName", starNameList);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getList(List<? extends Terms.Bucket> buckets) {
        List<String> keywordList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
            keywordList.add(keyAsString);
        }
        return keywordList;
    }

    //过滤
    public void handlerFilter(RequestParam requestParam, SearchRequest searchRequest) {
        if (requestParam != null) {
            String key = requestParam.getKey();
            //健壮性判断 ---> 如果 key 为空或者为空字符串则查询全部，否则进行全文检索
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (key == null || "".equals(key))
                searchRequest.source().query(boolQuery.must(QueryBuilders.matchAllQuery()));
            else searchRequest.source().query(boolQuery.must(QueryBuilders.matchQuery("name", key)));
            String city = requestParam.getCity();
            String brand = requestParam.getBrand();
            String starName = requestParam.getStarName();
            Integer minPrice = requestParam.getMinPrice();
            Integer maxPrice = requestParam.getMaxPrice();
            String location = requestParam.getLocation();
            //健壮性判断 ---> 如果 key 为空或者为空字符串则查询全部，否则进行全文检索
            if (city != null && !"".equals(city)) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("city", city)));
            }
            if (brand != null && !"".equals(brand)) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("brand", brand)));
            }
            if (starName != null && !"".equals(starName)) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.termQuery("starName", starName)));
            }
            if (minPrice != null && maxPrice != null) {
                searchRequest.source().query(boolQuery.filter(QueryBuilders.rangeQuery("price").lte(maxPrice).gte(minPrice)));
            }
            if (location != null && !"".equals(location)) {
                searchRequest.source().sort(SortBuilders.geoDistanceSort("location", new GeoPoint(location)).unit(DistanceUnit.KILOMETERS).order(SortOrder.ASC));
            }
            FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(boolQuery, new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("isAD", true), ScoreFunctionBuilders.weightFactorFunction(10))
            });
            searchRequest.source().query(functionScoreQuery);
        }
    }

    //封装处理请求的函数
    public PageResult handleSearchResult(SearchResponse searchResponse) {
        PageResult pageResult = new PageResult();
        pageResult.setTotal(searchResponse.getHits().getTotalHits().value);
        SearchHit[] hits = searchResponse.getHits().getHits();
        List<HotelDoc> hotelDocList = new ArrayList<>();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);
            if (hit.getSortValues().length > 0) hotelDoc.setDistance(hit.getSortValues()[0]);
            hotelDocList.add(hotelDoc);
        }
        pageResult.setHotels(hotelDocList);
        return pageResult;
    }
}
```

![img](https://img-blog.csdnimg.cn/478f58aa8c3f4cbfa14879a2539eb0b4.png)

### 15.18 自动补全

用户在搜索框输入字符时，我们应该提示出与该字符有关的搜索项，提示完整词条的功能，就是自动补全了。

#### 15.18.1 拼音分词器

如果我们需要根据拼音字母来推断，因此要用到拼音分词功能。要实现根据字母做补全，就必须对文档按照拼音分词。插件地址：https://github.com/medcl/elasticsearch-analysis-pinyin

将该压缩包解压之后上传至数据卷，查看数据卷位置可使用如下代码：

```sh
docker volume inspect es-plugins
```

上传后重启`ElasticSearch`：

```sh
docker restart 2b29dc422080[es 容器 id]
```

测试拼音分词器：

```json
POST /_analyze
{
    "text":"爱干净住汉庭",
    "analyzer:"pinyin"
}
```

结果如下：

```json
{
  "tokens" : [
    {
      "token" : "ai",
      "start_offset" : 0,
      "end_offset" : 0,
      "type" : "word",
      "position" : 0
    },
    {
      "token" : "agjzht",
      "start_offset" : 0,
      "end_offset" : 0,
      "type" : "word",
      "position" : 0
    },
    {
      "token" : "gan",
      "start_offset" : 0,
      "end_offset" : 0,
      "type" : "word",
      "position" : 1
    },
    {
      "token" : "jing",
      "start_offset" : 0,
      "end_offset" : 0,
      "type" : "word",
      "position" : 2
    },
    {
      "token" : "zhu",
      "start_offset" : 0,
      "end_offset" : 0,
      "type" : "word",
      "position" : 3
    },
    {
      "token" : "han",
      "start_offset" : 0,
      "end_offset" : 0,
      "type" : "word",
      "position" : 4
    },
    {
      "token" : "ting",
      "start_offset" : 0,
      "end_offset" : 0,
      "type" : "word",
      "position" : 5
    }
  ]
}
```

默认的拼音分词器会将每个汉字单独分为拼音，而我们希望的是每个词条形成一组拼音，需要对拼音分词器做个性化定制，形成自定义分词器。

`ElasticSearch`中分词器`（analyzer）`的组成包含三部分：

- `character filters`：在`tokenizer`之前对文本进行处理。例如删除字符、替换字符
- `tokenizer`：将文本按照一定的规则切割成词条`(term)`。例如`keyword`，就是不分词的，`ik_smart`也是
- `tokenizer filter`：将`tokenizer`输出的词条做进一步处理。例如大小写转换、同义词处理、拼音处理

![img](https://cdn.xn2001.com/img/2021/20211023021451.png)

声明自定义分词器的语法如下：

```json
PUT /test
{
  "settings": {
    "analysis": {
      "analyzer": { // 自定义分词器
        "my_analyzer": {  // 分词器名称
          "tokenizer": "ik_max_word",
          "filter": "py"
        }
      },
      "filter": { // 自定义tokenizer filter
        "py": { // 过滤器名称
          "type": "pinyin", // 过滤器类型，这里是pinyin
		  "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "analyzer": "my_analyzer",
        "search_analyzer": "ik_smart"
      }
    }
  }
}
```

测试：

```json
POST /test/_analyze
{
  "text":"爱干净住汉庭",
  "analyzer":"my_analyzer"
}
```

测试结果如下：

![img](https://img-blog.csdnimg.cn/1ac0d9c134274fcaab6de0599aa48005.png)

<font color="red">**注意：为了避免搜索到同音字，搜索时不要使用拼音分词器**</font>

![img](https://cdn.xn2001.com/img/2021/20211023022355.png)

`ElasticSearch`提供了 [Completion Suggester](https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-suggesters.html) 查询来实现自动补全功能。这个查询会匹配以用户输入内容开头的词条并返回；为了提高补全查询的效率，对于文档中字段的类型有一些约束。

- 参与补全查询的字段必须是`completion`类型。
- 字段的内容一般是用来补全的多个词条形成的数组。

```json
PUT test
{
  "mappings": {
    "properties": {
      "title":{
        "type": "completion"
      }
    }
  }
}
```

然后插入下面的数据

```json
// 示例数据
POST test/_doc
{
  "title": ["Sony", "WH-1000XM3"]
}
POST test/_doc
{
  "title": ["SK-II", "PITERA"]
}
POST test/_doc
{
  "title": ["Nintendo", "switch"]
}
```

查询的 DSL 语句如下

```json
// 自动补全查询
GET /test/_search
{
  "suggest": {
    "title_suggest": {
      "text": "s", // 关键字
      "completion": {
        "field": "title", // 补全查询的字段
        "skip_duplicates": true, // 跳过重复的
        "size": 10 // 获取前10条结果
      }
    }
  }
}
```

之前的酒店索引库是不存在拼音分词器的，所以需要重建索引库：

```json
// 酒店数据索引库
PUT /hotel
{
  "settings": {
    "analysis": {
      "analyzer": {
        "text_anlyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        },
        "completion_analyzer": {
          "tokenizer": "keyword",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "id":{
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword",
        "copy_to": "all"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart"
      },
      "suggestion":{
          "type": "completion",
          "analyzer": "completion_analyzer"
      }
    }
  }
}
```

**`JavaAPI`**

[![img](https://cdn.xn2001.com/img/2021/20211025113007.png)](https://cdn.xn2001.com/img/2021/20211025113007.png)





解析响应的代码如下：

![img](https://cdn.xn2001.com/img/2021/20211025113011.png)![](https://cdn.xn2001.com/img/2021/20211025113011.png)

### 15.19 数据同步

`ElasticSearch`中的数据来自于`MySQL`数据库，因此`MySQL`数据发生改变时，`ElasticSearch`也必须跟着改变，这个就是`ElasticSearch`与`MySQL`之间的**数据同步**。

![img](https://cdn.xn2001.com/img/2021/20211025163219.png)

常见的数据同步方案有三种：

1. 同步调用
2. 异步通知
3. 监听`binlog`

#### 15.19.1 同步调用

![img](https://cdn.xn2001.com/img/2021/20211025163313.png)

- `hotel-demo`对外提供接口，用来修改`elasticsearch`中的数据
- 酒店管理服务在完成数据库操作后，直接调用`hotel-demo`提供的接口

**优点：实现简单、粗暴**

**缺点：业务耦合度高**

#### 15.19.2 异步通知

![img](https://cdn.xn2001.com/img/2021/20211025163346.png)

- `hotel-admin`对`MySQL`数据库数据完成增、删、改后，发送`MQ`消息
- `hotel-demo`监听`MQ`，接收到消息后完成`ElasticSearch`数据修改

**优点：低耦合，实现难度一般**

**缺点：依赖`MQ`的可靠性**

异步通知的实现方式：

![img](https://cdn.xn2001.com/img/2021/20211025163734.png)

1. 引入`RabbitMQ`依赖，记得修改配置文件`application.yml`

   ```xml
   <!--amqp-->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-amqp</artifactId>
   </dependency>
   ```

2. 创建交换机、队列名称信息【增改其实是一类消息】

   ```java
   public class MQConstants {
       /**
        * 交换机
        */
       public final static String HOTEL_EXCHANGE = "hotel.topic";
       /**
        * 监听新增和修改的队列
        */
       public final static String HOTEL_INSERT_QUEUE = "hotel.insert.queue";
       /**
        * 监听删除的队列
        */
       public final static String HOTEL_DELETE_QUEUE = "hotel.delete.queue";
       /**
        * 新增或修改的RoutingKey
        */
       public final static String HOTEL_INSERT_KEY = "hotel.insert";
       /**
        * 删除的RoutingKey
        */
       public final static String HOTEL_DELETE_KEY = "hotel.delete";
   }
   ```

3. 配置交换机、队列对象

   ```java
   @Configuration
   public class MqConfig {
       @Bean
       public TopicExchange topicExchange() {
           return new TopicExchange(MqConstants.HOTEL_EXCHANGE, true, false);
       }
   
       @Bean
       public Queue insertQueue() {
           return new Queue(MqConstants.HOTEL_INSERT_QUEUE, true);
       }
   
       @Bean
       public Queue deleteQueue() {
           return new Queue(MqConstants.HOTEL_DELETE_QUEUE, true);
       }
   
       @Bean
       public Binding insertQueueBinding() {
           return BindingBuilder.bind(insertQueue()).to(topicExchange()).with(MqConstants.HOTEL_INSERT_KEY);
       }
   
       @Bean
       public Binding deleteQueueBinding() {
           return BindingBuilder.bind(deleteQueue()).to(topicExchange()).with(MqConstants.HOTEL_DELETE_KEY);
       }
   }
   ```

4. 发送消息【在前端发送增删改`MySQL`数据的时候就更新`ElasticSearch`，可以在`Controller`层完成发送，三个地方】

   ```java
   update:
   rabbitTemplate.convertAndSend(MQConstants.HOTEL_EXCHANGE, MQConstants.HOTEL_INSERT_KEY, hotel.getId());
   
   insert:
   rabbitTemplate.convertAndSend(MQConstants.HOTEL_EXCHANGE, MQConstants.HOTEL_INSERT_KEY, hotel.getId());
   
   delete:
   rabbitTemplate.convertAndSend(MQConstants.HOTEL_EXCHANGE, MQConstants.HOTEL_DELETE_KEY, id);
   ```

5. 接收消息

   ```java
   @Component
   public class HotelListener {
   
       @Autowired
       private HotelService hotelService;
   
       /**
        * 监听酒店新增或修改的业务
        *
        * @param id 酒店id
        */
       @RabbitListener(queues = MqConstants.HOTEL_INSERT_QUEUE)
       public void listenHotelInsertOrUpdate(Long id) {
           hotelService.insertById(id);
       }
   
       /**
        * 监听酒店删除的业务
        *
        * @param id 酒店id
        */
       @RabbitListener(queues = MqConstants.HOTEL_DELETE_QUEUE)
       public void listenHotelDelete(Long id) {
           hotelService.deleteById(id);
       }
   }
   ```

   ```java
   @Override
   //增加跟修改都可以在这里完成
   public void insertById(Long id) {
       try {
           // 根据id查询酒店数据
           Hotel hotel = getById(id);
           // 转换为文档类型
           HotelDoc hotelDoc = new HotelDoc(hotel);
           IndexRequest request = new IndexRequest("hotel").id(hotel.getId().toString());
           request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
           client.index(request, RequestOptions.DEFAULT);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }
   
   @Override
   public void deleteById(Long id) {
       try {
           DeleteRequest request = new DeleteRequest("hotel", id.toString());
           client.delete(request, RequestOptions.DEFAULT);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }
   ```

#### 15.19.3 监听`binlog`

![img](https://cdn.xn2001.com/img/2021/20211025163428.png)

- `MySQL`开启`binlog`功能
- `MySQL`完成增、删、改操作都会记录在`binlog`中
- `hotel-demo`基于`canal`监听`binlog`变化，实时更新`ElasticSearch`中的内容

**优点：完全解除服务间耦合**

**缺点：开启`binlog`增加数据库负担、实现复杂度高**

### 15.20 `ElasticSearch`集群

单机的`Elasticsearch`做数据存储，必然面临两个问题：海量数据存储问题、单点故障问题。

- 解决海量数据存储问题：将索引库从逻辑上拆分为`N`个分片`shard`存储到多个节点
- 单点故障问题：将分片数据在**不同节点**备份`replica`，比如分片一放到第二个节点，分片三放到第三个节点

#### 15.20.1 `ElasticSearch`集群相关概念

- 集群`(cluster)`：一组拥有共同的`cluster name`的节点。

- 节点`(node)`：集群中的一个`ElasticSearch`实例

- 分片`(shard)`：索引可以被拆分为不同的部分进行存储，拆分的部分称之为分片`shard`。在集群环境下，一个索引的不同分片可以拆分到不同的节点中，解决数据量太大，单点存储量有限的问题。

  如下图，我们把索引库数据分成`3`片：`shard0`、`shard1`、`shard2`，除此之外分片还有主分片跟副本分片的概念：

  - 主分片`（Primary shard）`：相对于副本分片的定义。

  - 副本分片`（Replica shard）`每个主分片可以有一个或者多个副本，数据和主分片一样

  ![img](https://cdn.xn2001.com/img/2022/202205071006783.png)

数据备份可以保证高可用，但是每个分片备份在一个节点上（即一台服务器上），所需要的节点数量就会非常多，成本太高。为了在高可用跟成本间需求平衡，可以这样操作：

- 首先对数据分片，存储到不同节点
- 然后对每个分片进行备份，放到对方节点，**完成互相备份**

![img](https://cdn.xn2001.com/img/2022/202205071006790.png)

现在，每个分片都有`1`个备份，存储在3个节点：

- `node0`：保存了分片`0`和`1`
- `node1`：保存了分片`0`和`2`
- `node2`：保存了分片`1`和`2`

#### 15.20.2 搭建`ElasticSearch`集群

我们会在单机上利用`Docker`容器运行多个`Elasticsearch`实例来模拟集群。可以直接使用`docker-compose`来完成，这要求你的`Linux`虚拟机至少有**`4G`**以上的内存空间。

`docker-compose.yml`：

```yaml
version: '2.2'
services:
  es01:
    image: elasticsearch:7.12.1
    container_name: es01
    environment:
      - node.name=es01
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es02,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data01:/usr/share/elasticsearch/data
    ports:
      - 9200:9200
    networks:
      - elastic
  es02:
    image: elasticsearch:7.12.1
    container_name: es02
    environment:
      - node.name=es02
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data02:/usr/share/elasticsearch/data
    ports:
      - 9201:9200
    networks:
      - elastic
  es03:
    image: elasticsearch:7.12.1
    container_name: es03
    environment:
      - node.name=es03
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es02
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data03:/usr/share/elasticsearch/data
    networks:
      - elastic
    ports:
      - 9202:9200
volumes:
  data01:
    driver: local
  data02:
    driver: local
  data03:
    driver: local
networks:
  elastic:
    driver: bridge
```

修改 Linux 系统权限，修改 `/etc/sysctl.conf` 文件

```sh
vi /etc/sysctl.conf
```

添加下面的内容

```sh
vm.max_map_count=262144
```

让配置生效：

```sh
sysctl -p
```

通过docker-compose启动集群

```sh
docker-compose up -d
```

#### 15.20.3 `ElasticSearch`集群状态监控

`kibana`可以监控`Elasticsearch`集群，但是更推荐使用`[cerebro]`(https://github.com/lmenezes/cerebro)下载解压打开`/bin/cerebro.bat`，访问 http://localhost:9000 即可进入管理界面：

![img](https://cdn.xn2001.com/img/2022/202205071018030.png)

输入任意节点的地址和端口，点击`connect`：绿色的线条代表集群处于绿色（健康状态）

![img](https://cdn.xn2001.com/img/2022/202205071018761.png)

#### 15.20.4 使用`cerebro`创建索引库

创建索引库：

![](https://cdn.xn2001.com/img/2022/202205071020771.png)

填写索引库信息：

![img](https://cdn.xn2001.com/img/2022/202205071023380.png)

回到首页，即可查看索引库分片效果：

![img](https://cdn.xn2001.com/img/2022/202205071024397.png)

#### 15.20.5 `ElaticSearch`集群职责划分

![img](https://cdn.xn2001.com/img/2022/202205071029258.png)

**默认情况下，集群中的任何一个节点都同时兼职上述四种角色。**

真实的集群一定要将集群职责分离：

- `master`节点：对`CPU`要求高，但是内存要求低
- `data`节点：对`CPU`和内存要求都高
- `coordinating`节点：对网络带宽、`CPU`要求高

职责分离可以让我们根据不同节点的需求分配不同的硬件去部署。而且避免业务之间的互相干扰。

![img](https://cdn.xn2001.com/img/2022/202205071029263.png)

#### 15.20.6 `ElasticSearch`集群脑裂问题

在`ElasticSearch 7.x`之后已经默认解决了脑裂问题，所以简单了解下知道原理即可，不用做任何的操作。

脑裂是因为集群中的节点失联导致的。例如一个集群中，因为网络阻塞等原因导致主节点`node1`与其它节点失联。

![img](https://cdn.xn2001.com/img/2022/202205071029267.png)

此时，`node2`和`node3`认为`node1`宕机，就会重新选主。

![img](https://cdn.xn2001.com/img/2022/202205071344676.png)

当`node3`当选后，集群继续对外提供服务，`node2`和`node3`自成集群，`node1`自成集群，两个集群数据不同步，出现数据差异。当网络恢复后，因为集群中有两个`master`节点，集群状态的不一致，出现脑裂的情况。一下子出现了两个主节点，这样的情况脑子都裂开了，所以称为脑裂问题。

![img](https://cdn.xn2001.com/img/2022/202205071344829.png)

解决脑裂的方案是，要求选票超过 **(`eligible`节点数量+1)/2** 才能当选为`master`，`eligible`就是候选节点，有资格成为主节点的就是候选节点，候选节点的数量加上`1`除以`2`就是选票，当所有的票数超过选票就选择该节点为主节点。比如这里：`node1`是一个主节点，`node2`和`node3`是一组候选节点，当选票为：`4/2 = 2`所以当选票为`>= 2`时可以升为主节点，因为`node1`只有`1`票，而`node2 node3`有`2`票，所以最终结果是`node3`当上了主节点`master`。

对应配置项是`discovery.zen.minimum_master_nodes`，在版本 7.0 以后，已经成为默认配置，因此一般不会发生脑裂问题。

#### 15.20.7 `ElasticSearch`集群分布式存储

当新增文档时，应该保存到不同分片，保证数据均衡，那么`coordinating node`如何确定数据该存储到哪个分片呢？`coordinating node`就是用来做请求路由，负载均衡的。它该如何确定数据存储到哪个分片`shard`呢？

`ElasticSearch`会通过`hash`算法来计算文档应该存储到哪个分片：

![img](https://cdn.xn2001.com/img/2022/202205071412275.png)

- `_routing`默认是文档的`id`
- 算法与分片数量有关，因此索引库一旦创建，分片数量不能修改

新增的一个文档将存储到哪个分片的流程如下：

1. 新增一个`id=1`的文档
2. 对`id`做`hash`运算，假如得到的是`2`，则应该存储到`shard-2`
3. `shard-2`的主分片在`node3`节点，将数据路由到`node3`，`node3`保存文档
4. 同步给`shard-2`的副本分片`2(R-2)`，在`node2`节点
5. 返回结果给`coordinating-node`节点`(node1)`

![img](https://cdn.xn2001.com/img/2022/202205071412283.png)

#### 15.20.8 `ElasticSearch`集群分布式查询

`ElasticSearch`查询分成两个阶段：

- `scatter phase`：分散阶段，`coordinating node`会把请求分发到每一个分片。
- `gather phase`：聚集阶段，`coordinating node`汇总`data node`的搜索结果，并处理为最终结果集返回给用户。

![img](https://cdn.xn2001.com/img/2022/202205071422105.png)

#### 15.20.9 `ElasticSearch`集群故障转移

集群的`master`节点即主节点会监控集群中的节点状态，如果发现有节点宕机，会立即将宕机节点的分片数据迁移到其它节点，确保数据安全，这个叫做故障转移。

例如一个集群结构如图，三个都是健康的，标星星的代表主节点`master`其它两个节点是从节点。

![img](https://cdn.xn2001.com/img/2022/202205071516134.png)

突然，`node1`发生了故障。

![img](https://cdn.xn2001.com/img/2022/202205071516139.png)

宕机后的第一件事，需要重新选主，例如选中了`node2`。

![img](https://cdn.xn2001.com/img/2022/202205071516685.png)

`node2`成为主节点后，会检测集群监控状态，将`node1`上的数据迁移到`node2`、`node3`，确保数据依旧正常访问。

![img](https://cdn.xn2001.com/img/2022/202205071516805.png)

模拟话可以使用：`docker-compose stop es01`来模仿，然后观察`cerebro`的监控信息，后面再次启动`es01`时可以看到虽然不能再次称为主节点`master`但是可以将分散的数据恢复到`es01`中。

## 16. 微服务保护`Sentinel`

### 16.1 初识`Sentinel`

#### 16.1.1 什么是雪崩问题？

在服务集群中，一个微服务会依赖于许多其它微服务。比如下图：

![img](https://img-blog.csdnimg.cn/6f184cd13cce4aab9aff8ca2cbe374e9.png)

但是现在服务`D`出现了故障，所以导致服务`A`需要服务`D`的通道被阻塞了无法正常运行。服务`A`对于服务`D`的请求肯定不止一个，随着时间的推移，服务`A`对于服务`D`的请求将会越来越多越来越多。`Tomcat`资源总有会被耗尽的时候，那么就会导致依赖于`D`服务的`A`服务总有一天也会崩掉，出现故障。

![img](https://img-blog.csdnimg.cn/124feeeeb22c4beab0499e683ab56a0d.png)

要知道，在微服务架构的服务集群中，这样的依赖与被依赖的关系可能有成千上百个，所以一旦某个微服务出现故障后其依赖于它的微服务出现故障，就会一个接着一个的出现故障，这样出现故障的微服务会越来越多越来越多，一发不可收拾，这就是微服务**雪崩问题**。

具体说明下雪崩问题：**<font color="red">微服务调用链路中的某个服务出现故障从而引起整个链路所有的微服务都不可用</font>**，这就是雪崩问题。

#### 16.1.2 解决雪崩问题

解决雪崩问题的常见方式有四种：

1. **<font color="red">超时处理</font>**：设定超时时间，请求的时间超过所设定的超时时间代表没有响应此时返回错误信息，并不无休止等待。

   ![img](https://img-blog.csdnimg.cn/19489e6351074a17ab0b8e207808a9d8.png)

   超时处理的这种方式只能缓解雪崩问题，因为你设定超时时间本身就需要时间，假设你请求超时时间为`0.1s`，但是就是在这等待响应的`0.1s`之内有上亿个请求进来需要访问，也就是进入请求的速度和数量远大于你的超时时间，那你的微服务同样会跨，从而同样会导致雪崩问题，所以只能说是缓解了雪崩问题而无法说解决了雪崩问题。**【已经出现故障了】**

2. **<font color="red">舱壁模式</font>**：先解释下什么是舱壁，就是一艘大轮船它会用隔板将船舱一块一块的隔开，形成舱壁，如果此时大轮船撞击到了一块冰山，导致船舱进水，但是因为有舱壁的存在，船舱只是部分进水并不会影响大轮船整体，这就像是护城河。

   类似地，在程序设计中可以限定每个微服务使用线程的数量，避免耗尽服务器【比如`Tomcat`】资源，因此也叫线程隔离。整个微服务就是整个服务器【比如`Tomcat`】就可以看作是整艘大轮船，整个服务集群就是船舱，限定每个微服务使用的线程数量就是加上挡板，形成舱壁。这样当某个微服务出现故障挂掉的时候也不至于出现微服务雪崩的现象。舱壁模式也可以叫做线程隔离、线程限制。**【已经出现故障了】**

   舱壁模式解决了超时处理的问题，就是杜绝了雪崩问题，而超时处理只是缓解，但是舱壁模式也存在不足的地方：假设现在某个微服务比如`C`服务挂掉了，因为发送请求的服务比如`A`服务有线程数量，所以它还是会去访问`C`服务，但是此时`C`服务已经挂掉了所以这样的访问显然非常浪费资源。

   如果我结合舱壁模式+超时处理是不是可以更好的处理雪崩问题？

   这样的想法挺不错的，但是考虑下极端情况，我虽然设定了超时处理，但是我在很短很短的时间内容，比如超时时间是`0.00001s`但是我在`0.00001s`之内的请求数量就占满了限定的线程数，所以这样做仍然无法解决资源浪费的问题。

   ![img](https://img-blog.csdnimg.cn/b390bcf2022b4193aa5eb2d4b615c67f.png)

   ![img](https://img-blog.csdnimg.cn/7b13cd211fb2426e95b9ffb895f16877.png)

3. **<font color="red">熔断降级</font>**：由**断路器**统计业务的异常比例，如果**超出阈值**就会**熔断**该业务，拦截访问该业务的一切请求。比如发了三次请求，`1`次成功`2`次失败，则比例达到`60%`假设设定的阈值为`50%`此时断路器就会执行熔断操作。熔断有三种状态，关闭开放半开放，半开放回去尝试连接服务看下请求的服务是否回复正常。**【已经出现故障了】**

   ![img](https://img-blog.csdnimg.cn/12d218ae339b4b358db3f7e2b31f35bd.png)

   ![img](https://img-blog.csdnimg.cn/9e0e8494c35943fa96ad8c51e9d3a084.png)

4. **<font color="red">流量控制</font>**：限制业务访问的`QPS`【每秒处理请求数量】，避免服务因流量的突增而故障。此时就可以使用到`Sentinal`了。这就相当于把雪崩问题扼杀在摇篮之中。`Sentinel`就是专门用来做做流量控制的一个框架。

   ![img](https://img-blog.csdnimg.cn/14c9744063fd4f6a9ba4cf07192409a9.png)

总结：

- 如何避免因瞬间高并发而导致服务故障？ ---> 流量控制【预防】
- 如何避免因服务故障引起雪崩问题？ ---> 超时处理、舱壁模式、熔断降级【**船舱超时导致保险丝熔断**】【补救】

### 16.2 微服务保护技术对比

在SpringCloud当中支持多种服务保护技术：

- [`Netfix Hystrix`](https://github.com/Netflix/Hystrix)
- [`Sentinel`](https://github.com/alibaba/Sentinel)
- [`Resilience4J`](https://github.com/resilience4j/resilience4j)

早期比较流行的是`Hystrix`框架，但目前国内实用最广泛的还是阿里巴巴的`Sentinel`框架，这里我们做下对比。

|                | **Sentinel**                                   | **`Hystrix`**                   |
| -------------- | ---------------------------------------------- | ------------------------------- |
| 隔离策略       | 信号量隔离                                     | 线程池隔离/信号量隔离           |
| 熔断降级策略   | 基于慢调用比例或异常比例                       | 基于失败比率                    |
| 实时指标实现   | 滑动窗口                                       | 滑动窗口（基于`RxJava`）        |
| 规则配置       | 支持多种数据源                                 | 支持多种数据源                  |
| 扩展性         | 多个扩展点                                     | 插件的形式                      |
| 基于注解的支持 | 支持                                           | 支持                            |
| 限流           | 基于`QPS`，支持基于调用关系的限流              | 有限的支持                      |
| 流量整形       | 支持慢启动、匀速排队模式                       | 不支持                          |
| 系统自适应保护 | 支持                                           | 不支持                          |
| 控制台         | 开箱即用，可配置规则、查看秒级监控、机器发现等 | 不完善                          |
| 常见框架的适配 | `Servlet、Spring Cloud、Dubbo、gRPC`等         | `Servlet、Spring Cloud Netflix` |

### 16.3 `Sentinel`介绍和安装

•**丰富的应用场景**：Sentinel 承接了阿里巴巴近 10 年的双十一大促流量的核心场景，例如秒杀（即突发流量控制在系统容量可以承受的范围）、消息削峰填谷、集群流量控制、实时熔断下游不可用应用等。

•**完备的实时监控**：Sentinel 同时提供实时的监控功能。您可以在控制台中看到接入应用的单台机器秒级数据，甚至 500 台以下规模的集群的汇总运行情况。

•**广泛的开源生态**：Sentinel 提供开箱即用的与其它开源框架/库的整合模块，例如与 Spring Cloud、`Dubbo`、`gRPC` 的整合。您只需要引入相应的依赖并进行简单的配置即可快速地接入 Sentinel。

•**完善的** **`SPI`** **扩展点**：Sentinel 提供简单易用、完善的 `SPI` 扩展接口。您可以通过实现扩展接口来快速地定制逻辑。例如定制规则管理、适配动态数据源等。

安装可以参考下列：

1. 下载

   sentinel官方提供了`UI`控制台，方便我们对系统做限流设置。大家可以在https://github.com/alibaba/Sentinel/releases下载。也可以直接使用资料下载好的`jar`包。

2. 运行

   将jar包放到任意非中文目录，执行命令：

   ```sh
   java -jar sentinel-dashboard-1.8.1.jar
   ```

   如果要修改Sentinel的默认端口、账户、密码，可以通过下列配置：

   | **配置项**                       | **默认值** | **说明**   |
   | -------------------------------- | ---------- | ---------- |
   | server.port                      | 8080       | 服务端口   |
   | sentinel.dashboard.auth.username | sentinel   | 默认用户名 |
   | sentinel.dashboard.auth.password | sentinel   | 默认密码   |

   例如，修改端口：

   ```sh
   java -Dserver.port=8090 -jar sentinel-dashboard-1.8.1.jar
   ```

3. 访问

   访问http://localhost:8080页面，就可以看到sentinel的控制台了：需要输入账号和密码，默认都是：sentinel，发现一片空白，什么都没有，这是因为我们还没有与微服务整合。

   ![img](https://img-blog.csdnimg.cn/61b20a143deb4ef59fcec224295b4e48.png)

![img](https://img-blog.csdnimg.cn/2ad9787f4cff418f959a6d3da2e624e6.png)

也可以到`hub.docker`使用`docker`进行安装：

1. `docker pull bladex/sentinel-dashboard:1.8.0`
2. `docker run --name sentinel  -d -p 8858:8858 -d  bladex/sentinel-dashboard:1.8.0`
3. 访问`http://192.168.56.1:8858`

### 16.4 `SpringBoot`整合`Sentinel`

我们在`order-service`中整合`sentinel`，并连接`sentinel`的控制台，步骤如下：

1. 引入sentinel依赖

   ```xml
   <!--sentinel-->
   <dependency>
       <groupId>com.alibaba.cloud</groupId> 
       <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
   </dependency>
   ```

2. 配置控制台

   修改application.yaml文件，添加下面内容：

   ```yaml
   spring:
     cloud: 
       sentinel:
         transport:
           dashboard: 192.168.56.1:8858
   ```

3. 访问`order-service`的任意端点

   打开浏览器，访问http://localhost:8080/order/101，这样就能触发`sentinel`的监控【刷新，发送请求多次】。然后访问`sentinel`控制台，查看效果：

   ![img](https://img-blog.csdnimg.cn/f0708b5835f54ba4b513b4f57de0519a.png)

   

### 16.5 使用`Sentinel`进行流量控制

> 预判风险是防范风险的前提

**簇点链路：**以`SpringMVC`为例，当请求进入微服务时，首先会访问`DispatcherServlet`然后进入`Controller`、`Service`、`Mapper`这样的一个调用链叫做簇点链路。

簇点链路中每一个接口就是一个资源，默认情况下`Sentinel`会监控`SpringMVC`中的每一个端点`Endpoint`即`Controller`中的一个个方法。因此一个端点就是簇点链路中的一个资源。比如刚刚访问的`/order/{orderId}`就是`OrderController`中的端点。

![img](https://img-blog.csdnimg.cn/8b838bd995a34f318100da386bcd0913.png)

无论是流量控制还是熔断降级都是针对簇点链路中的资源来设置的，可以在`Sentinel`的簇点链路设置界面设置规则。可以设定的规则一共有四种：

1. 流控：流量控制
2. 降级：熔断降级
3. 热点：热点参数限流，是限流的一种
4. 授权：请求的权限控制

比如新增流控规则：表示限制`/order/{orderId}`这个资源的单机`QPS`即每秒允许请求次数为`1`次，超出这个次数的请求会被拦截并报错。

![img](https://img-blog.csdnimg.cn/235a42cc6d4044f78cd59a4a98e788f1.png)

此时如果在`1s`发送多次请求会被拒绝，并且页面会显示被`Sentinel`锁定流量控制的相关信息：

![img](https://img-blog.csdnimg.cn/a904abd8b06f4deda6c5a633f6c149a5.png)

如果需要模拟高并发可以使用`Jmeter`，可以将`QPS`设置成不能超过`5`，然后测试。使用`JMeter`进行压力测试，直接打开`sentinel测试.jmx`：

![img](https://img-blog.csdnimg.cn/6512cb4936054f7aa58ec552970f2252.png)

**注意，不要点击菜单中的执行按钮来运行。**

![img](https://img-blog.csdnimg.cn/ddfd0f1e7573424cbb9ca7d85c6deb9a.png)

查看结果树结果：可以看到`1s`发送`10`次有`5`次是失败的，因为`QPS = 5`

![img](https://img-blog.csdnimg.cn/d1121346fd5946e1b0ab2b898ef12c11.png)

### 16.6 （流控模式）流量控制的三种模式

在添加限流规则时，点击高级选项，可以选择三种流控模式：

![img](https://img-blog.csdnimg.cn/5262ece0d7db42ceaa02eea89908b749.png)

#### 16.6.1 直连模式

直连模式就是默认的模式，我们刚刚做的实验`/order/{orderId}`使用的流控模式就是直连模式，这种流控模式`Sentinel`会统计当前资源的请求，触发阈值的时候就会对当前资源直接进行限流。

#### 16.6.2 关联模式

关联模式涉及到优先级的问题，这种流控模式会统计与当前资源相关的另外一个资源的请求，当对另外一个资源的请求数量达到请求设定的阈值的时候就会触发阈值，对当前资源进行限流。

这样说可能稍微有点抽象，简单说就是“**权力越大享用资源越多**”，可以举一个例子：

- 对于订单状态，通常会有两种操作，一是查询订单，二是修改订单，要知道修改订单的优先级肯定比查询订单的优先级高，而查询和修改订单的操作都会争抢数据库锁，产生竞争。所以为了避免竞争。当修改订单的请求达到阈值时证明需要修改订单的请求数量很多，那么就应该触发查询订单阈值，将查询订单这个资源进行限流。比如这样配置规则，当`/write`资源的请求数量触发阈值时，对`/read`资源进行限流从而避免影响到`/write`资源：

  ![img](https://cdn.xn2001.com/img/2022/202205081602252.png)

- 也就是说：对于当前资源，我设定一个阈值，当被关联的资源请求数量达到这个阈值，我就将优先级低的当前资源进行流量限流。

何时使用关联模式？**在满足二者存在竞争关系并且存在优先级高低之分**就可以也应该使用关联模式。

![img](https://cdn.xn2001.com/img/2022/202205081602290.png)

模拟关联模式流量控制：

1. 在`OrderController`中新增两个端点：`/order/query`和`/order/update`，只是套个壳能接收请求即可无需实现具体业务。

   ```java
   @GetMapping(value = "/query}")
   public String query() {
       return "查询订单成功！";
   }
   
   @GetMapping(value = "/update}")
   public String update() {
       return "修改订单成功！";
   }
   ```

2. 配置流控规则：当`/order/update QPS`请求数量超过`5`时，对`/order/query`进行限流。

   ![img](https://img-blog.csdnimg.cn/31b6af76fdf34266af585cfb68f4fa6f.png)

   ![img](https://img-blog.csdnimg.cn/17593f0679ad424ba1a3a39323b77662.png)

3. 使用`JMeter`进行测试

   ![img](https://img-blog.csdnimg.cn/e141bf9be46443fbbafc4aa85e964a96.png)

   ![img](https://img-blog.csdnimg.cn/0c016279cd034cb6b20bbe4212c521aa.png)

4. 启动然后`5`次请求之后浏览器查看`localhost:8080/order/query`，可以看到被限流了。

   ![img](https://img-blog.csdnimg.cn/3c5e7e10dd4f4ed6a833f034c992b878.png)

#### 16.6.3 链路模式

 只针对从指定链路访问到本资源的请求做统计，判断是否超过阈值。

例如有两条请求链路

- `/test1 --> /common`
- `/test2 --> /common`

如果只希望统计从`/test2`进入到`/common`的请求，则可以这样配置

![img](https://cdn.xn2001.com/img/2022/202205081632892.png)

**实战案例**

有查询订单和创建订单业务，两者都需要查询商品。针对从查询订单进入到查询商品的请求统计，并设置限流。

**链路模式中，是对不同来源的两个链路做监控。但是`Sentinel`默认会给进入`SpringMVC`的所有请求设置同一个`root`资源，会导致链路模式失效。我们需要关闭这种对`SpringMVC`的资源聚合，修改`order-service`服务的`application.yml`文件**

```yaml
spring:
  cloud:
    sentinel:
      web-context-unify: false # 关闭context整合
```

1. 在`OrderService`中添加一个`queryGoods`方法，不用实现业务，因为默认`Service`服务层的方法是不被`Sentinel`所监控的，所以需要我们自己通过`@SentinelResource`注解来标记要监控的方法。

   ```java
   @SentinelResource
   public String queryGoods() {
       return "查询商品成功！";
   }
   ```

2. 在`OrderController`中，改造`/order/query`端点，调用`OrderService`中的`queryGoods`方法

   ```java
   @GetMapping(value = "/query")
   public String query() {
       return orderService.queryGoods() + " 查询订单成功！";
   }
   ```

3. 在`OrderController`中添加一个`/order/save`端点，调用`OrderService`的`queryGoods`方法

   ```java
   @GetMapping(value = "/save")
   public String save() {
       return orderService.queryGoods() + " 存储订单成功！";
   }
   ```

   ![img](https://img-blog.csdnimg.cn/122f7098ffb64d9d9fbacb37f736a68b.png)

4. 给`queryGoods`设置限流规则，从`/order/query`进入`queryGoods`的方法限制`QPS`必须小于`2`

   ![img](https://cdn.xn2001.com/img/2022/202205081633212.png)

5. 使用`JMeter`测试：可以看到这里`200`个用户，`50`秒内发完，`QPS`为4，超过了我们设定的阈值`2`

   ![img](https://cdn.xn2001.com/img/2022/202205081633203.png)

6. 一个`http`请求是访问`/order/save`，运行的结果是 /order/save 完全不受影响。

   ![img](https://cdn.xn2001.com/img/2022/202205081858739.png)

   ![img](https://cdn.xn2001.com/img/2022/202205081858880.png)

7. 访问`/order/query`，运行结果是`/order/query`，每次只有`2`个通过。

   ![img](https://cdn.xn2001.com/img/2022/202205081633221.png)

![img](https://cdn.xn2001.com/img/2022/202205081633245.png)

### 16.7 流控效果

#### 16.7.1 快速失败

细心的小伙伴会发现在流控的高级选项中，还有一个流控效果选项，前面我们的测试都是基本快速失败的。快速失败的意思就是只要请求超过设定的`QPS`阈值，就立即拒绝并抛出`FlowException `异常，是默认的处理方式。之前的案例中，都是用的是快速失败的流控效果。

![img](https://cdn.xn2001.com/img/2022/202205081907653.png)

#### 16.7.2 `Warm Up`

`Warm Up`表示预热的意思，虽然也会像对超出阈值的请求做出立即拒绝并抛出异常，但是这种流控效果的阈值会动态变化，从一个较小的值主键增加到最大的阈值。

为什么要这么做呢？

因为阈值一般是一个微服务所能承担的最大的`QPS`，但是一个服务在刚刚启动的时候，一切资源尚未初始化，如果直接将`QPS`跑到最大值，可能导致服务器瞬间宕机。

`Warm Up`预热流动效果就是用来应对冷启动服务的一种方案，请求阈值的初始值为：`maxThreshold / coldFactor`，持续指定时长之后会主键增大到`maxThreashold`，而`coldFactor`的默认值为`3`。

比如我的`QPS`的`maxThresold = 10`，`coldFactor = 3`，则请求阈值的初始值为：`10 /3 = 3`，持续指定时长即预热时间后，假设为`5s`，阈值会慢慢扩大到`10`。

![img](https://img-blog.csdnimg.cn/160cbeee9fde4b549011f63dd1e070f7.png)

**案例**：给`/order/{orderId}`这个资源设置限流，最大`QPS`为`10`，利用`Warm Up`预热流动效果，预热时长为`5`秒。

![img](https://img-blog.csdnimg.cn/b996e06ed320424793aa9f08251ad3e9.png)

`JMeter`进行压力测试：

![img](https://img-blog.csdnimg.cn/2a313ac720dd4a359125567b175455fe.png)

刚刚启动时，大部分请求失败，成功的只有`3`个，说明`QPS`被限定在`3`，然后阈值会在`5s`之内慢慢扩大到`10`

![img](https://img-blog.csdnimg.cn/05b9470ce10b4c08ac7165aaddc8f023.png)

![img](https://img-blog.csdnimg.cn/714f09cf2ac24080a5467aae53791747.png)

可以到到`Sentinel`控制台查看实时监控：

![img](https://img-blog.csdnimg.cn/fe1c9c9dc0ac47648d232d53fb7bbeb8.png)

一段时间后：

![img](https://img-blog.csdnimg.cn/cccb26f3a0f2424da9721f3d99e7378e.png)

这种`Warm Up`预热流动效果主要应用于系统长期处于低水位的情况，当情况开始激增的时候，直接把系统拉升到高水位可能瞬间把系统压垮，通过“冷启动”可以让通过的流量缓慢增加，在一定时间内主键增加到阈值上限，给冷系统一个预热的时间，避免冷系统被压垮的情况。

#### 16.7.3 排队等待

简单了解下：排队等待就是让所有的请求按照先后次序排队执行，两个请求的间隔不能小于指定时长。

![img](https://cdn.xn2001.com/img/2022/202205090037622.png)

**到底什么是排队等待？**

当请求超过`QPS `阈值时，「快速失败」和 「`Warm Up`」会拒绝新的请求并抛出异常。而排队等待则是让所有请求进入一个队列中，然后按照阈值允许的时间间隔依次执行。后来的请求必须等待前面执行完成，如果请求预期的等待时间超出最大时长，则会被拒绝。（就是先排队等，如果你等的时间太长就直接拒绝你非常人性化），排队等待这种方式严格控制了请求通过的间隔时间，即让请求以均与速度通过，对应的是**漏桶算法**。

例如：

`QPS = 5`，在排队等待此种流控效果中意味着每`200ms`处理一个队列中的请求，`timeout = 2000ms`即最长预期等待时长为`2000ms`这个是自己设置的，如果某请求预期等待时长超过了`2000ms`则该请求就会被拒绝并抛出异常。

如现在一下子来了`12`个请求，因为每`200ms`执行一个请求【因为`QPS = 5`】，第一个请求不用等，第二个请求等`200ms`即可，第三个请求等`(3 - 1) * 200 = 400ms`即可，以此类推 ——>

- 第`06`个请求的预期等待时长` = 200 * (6 - 1) = 1000ms`
- 第`12`个请求的预期等待时长` = 200 * (12-1) = 2200ms`

如果预期等待时长超过了所设置的最长等待时长，则`Sentinel`会拒绝这个请求并抛出异常。

现在假设流控效果不是排队等待，而使用的是快速失败，则如果现在在`1s`之内有`10`个请求，但是第`2s`只有`1`个请求，此时`QPS`的曲线是这样的：

![img](https://img-blog.csdnimg.cn/845d3e7edffb49f2b9ddcc96d6f02b1a.png)

如果使用排队等待的流控效果，所有进入的请求都要排队，以固定的`200ms`的间隔执行，`QPS`会变的很平滑

![img](https://cdn.xn2001.com/img/2022/202205090016702.png)

排队等待的流控下过主要用于处理间隔性突发的流量，例如消息队列。想想一下这样的场景：

在某一秒突然有大量的请求到来，而接下来的几秒则处于空闲状态，我们希望系统能够在接下来的空闲时间逐步处理这些请求，而不是在第一秒的时候就直接拒绝多余的请求，这样很不友好，此时就可以使用排队等待的流控效果。

**案例**：给`/order/{orderId}`这个资源设置限流，最大`QPS`为`10`，利用排队等待的流控效果，超时时长设置为`5s`。

![img](https://img-blog.csdnimg.cn/24f06513fe8f45f5a4e68c63e0bb6d58.png)

再去`Sentinel`查看实时监控的`QPS`曲线，可以看到非常的平滑，一致保持在 10，但是超出的请求没有被拒绝，而是放入队列。因此**响应时间**（等待时间）会越来越长。当队列满了或者预期等待时长超过了最长等待时长之后，才会有部分请求失败。

![img](https://img-blog.csdnimg.cn/6a51d6ca18934985baee2ecdef60dede.png)

### 16.8 热点参数限流

之前的限流是统计访问某个资源的所有请求，判断是否超过`QPS`阈值。而「热点参数限流」是**分别统计参数值相同的请求**，判断是否超过`QPS`阈值。【一个是所有，一个是分类讨论】

比如：访问`/goods/{id}`的请求中，`id`参数值会有变化，「热点参数限流」会根据参数值分别统计`QPS`，统计结果：

![img](https://img-blog.csdnimg.cn/43e123f1736b4f198b9abe3b84a84dbd.png)

当`id=1`的请求触发阈值被限流时，`id`值不为`1`的请求则不受影响。

配置示例：对`hot`这个资源的`0`号参数（也就是第一个参数）做统计，每`1s`**相同参数值**的请求数不能超过`5`

![img](https://img-blog.csdnimg.cn/1e2528acb9f64c7c80d266c6d761fe5e.png)

假设上面的例子是一个商品查询接口，那么刚才的配置中，对这个接口的所有商品一视同仁，`QPS`都限定为`5`。而在实际开发过程中，可能部分商品是热点商品，例如秒杀商品，我们希望这部分商品的`QPS`限制与其它商品不一样，阈值高一些，那么就需要配置热点参数限流的高级选项了。

![img](https://cdn.xn2001.com/img/2022/202205090050071.png)

整体的含义就是：对`0`号的`long`类型参数限流，每`1`个相同参数的`QPS`不能超过`5`，有如下两个例外

- 如果参数值是`100`，则每`1s`允许的`QPS`为`10`
- 如果参数值是`101`，则每`1s`允许的`QPS`为 `15`

**案例需求**：给`/order/{orderId}`这个资源添加「热点参数限流」，规则如下

- 默认的热点参数规则是每`1s`请求量不超过`2`
- 给`102`这个参数设置例外：每`1s`请求量不超过`4`
- 给`103`这个参数设置例外：每`1s`请求量不超过`10`

**注意事项**：热点参数限流对默认的`SpringMVC`资源无效，需要利用 `@SentinelResource`注解标记资源。所以得先让该资源出现才可以。

```java
@SentinelResource(value = "hot")
@GetMapping(value = "/{orderId}")
public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
    return orderServiceFeign.queryOrderById(orderId);
}
```

重启服务器访问该方法，可以看到`hot`资源出现了：

![img](https://img-blog.csdnimg.cn/5a9e1800faf54d9f92c09981e6e1a4b0.png)

对该资源进行热点参数限制：

![img](https://img-blog.csdnimg.cn/371b3c679f2241dca552f1bf99ca992e.png)

![img](https://img-blog.csdnimg.cn/0120868a49da4aabb33149fe27b965a3.png)

此时热点参数限流就添加好了，我们使用`JMeter`进行测试，访问`102 103`的订单限流阈值分别为`4 10`其余的均为`2`：我们有`500`个用户在`100s`之内发送完毕`500`个请求，每次即每秒发送`5`个请求。

![img](https://img-blog.csdnimg.cn/e01ce66e0eed42678c04af8177c6f6f7.png)

我们分别访问：

- http://localhost:8080/order/101
- http://localhost:8080/order/102
- http://localhost:8080/order/103

![img](https://img-blog.csdnimg.cn/878d2d50db174df2816d28364173f3bc.png)

![img](https://img-blog.csdnimg.cn/1cb3755a9cee4bf8968d8fceb8df2988.png)

![img](https://img-blog.csdnimg.cn/2e30f84b34ed4735890ea473951c7e0d.png)

然后观察结果可以看到：

- `/order/101`在`1s`之内发送了`5`次请求但是只有`2`次成功其余均被拒绝，此后每`1s`均是如此。

  ![img](https://img-blog.csdnimg.cn/f72cdbe355664a959a336d98c4256c27.png)

- `/order/102`在`1s`之内发送了`5`次请求但是有`4`次成功只有`1`个均被拒绝，此后每`1s`均是如此。

  ![img](https://img-blog.csdnimg.cn/c8707051d9fb4a96992fc49751208531.png)

- `/order/103`在`1s`之内发送了`5`次请求但是有`5`次都成功因为小于其设定的阈值`QPS = 10`，此后每`1s`均是如此。

  ![img](https://img-blog.csdnimg.cn/02a2afd3aa154718963acfa4e5d4fa93.png)

上述例子充分说明了**热点参数限流**。

### 16.9 隔离和降级

流量控制是一种预防手段，可以尽可能地避免由于高并发导致服务出现故障从而导致出现雪崩问题，但是服务不仅仅会因为高并发而出现故障，还可能有其它的原因导致服务故障，从而也会导致雪崩问题。

那服务已经故障的情况下如何处理雪崩问题呢？这就需要**舱壁模式即线程隔离**跟**熔断降级**了。让我们简单先回顾下这两个的原理：

**舱壁模式（线程隔离）**：调用者在调用服务提供者的时候，给每一个调用的请求分配独立线程池，出现故障的时候，最多把这个线程池内的资源消耗完毕，避免将所有调用者的资源耗尽。

![img](https://cdn.xn2001.com/img/2022/202205091001949.png)

**熔断降级**：调用者这边加入熔断器，统计对服务提供者调用的失败比例，如果失败比例过高则熔断该业务，不允许访问该服务的提供者，直接拒绝。

![img](https://cdn.xn2001.com/img/2022/202205091203825.png)

可以看到不管是舱壁模式还是熔断降级都是在服务消费者也就是服务调用方这边完成操作的，都是去对该服务消费者即服务调用方发起的**远程调用**进行线程隔离或者熔断降级的操作。而之前我们学习的远程调用先是使用`RestTemplate`但是后面发现其代码不够优雅等问题所以找来了`Feign`帮忙。既然线程隔离和熔断降级都在远程调用完成，所以我们需要将`Feign`跟`Sentinel`进行一个整合，这样才能让`Sentinel`起到微服务保护的作用。

#### 16.9.1 `Feign`整合`Sentinel`

1. 引入依赖，此前我们已经引入过了

2. 在`OrderService`中修改`application.yaml`，开启`Feign`的`Sentinel`功能

   ```yaml
   feign:
     sentinel:
       enabled: true #开启Feign对Sentinel的支持
   ```

3. 编写服务降级逻辑代码，服务降级逻代码指的是服务访问失败后返回给用的一个友好提示或者是默认结果，可以使用`FeignClient`来编写服务降级逻辑代码，一共有两种方式

   1. 方式一：使用`FallbackClass`，但无法对远程调用的异常做处理
   2. 方式二：使用`FallbackFactory`，可以对远程调用的异常做处理，因为要做服务降级逻辑代码的编写故可以选择`FallbackFactory`这种方式

   此前我们将`Feign`封装到了`feign-api`项目中，所以现在也需要在该项目中实现`FallbackFactory`，创建`com.kk.clients.fallbackfactory.UserClientFallbackFactory`

   降级逻辑代码如下：

   ```java
   package com.kk.clients.fallbackfactory;
   
   import com.kk.clients.UserClient;
   import com.kk.pojo.User;
   import lombok.extern.slf4j.Slf4j;
   import org.springframework.cloud.openfeign.FallbackFactory;
   
   @Slf4j
   public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
       @Override
       public UserClient create(Throwable throwable) {
           return new UserClient() {
               @Override
               public User findById(Long id) {
                   log.error("查询用户失败", throwable);
                   return new User();
               }
           };
       }
   }
   ```

4. 将`UserClientFallbackFactory`注册为一个`Bean`：

   ```java
   package com.kk.configuration;
   
   import com.kk.clients.fallbackfactory.UserClientFallbackFactory;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   
   @Configuration
   public class FeignConfiguration {
       @Bean
       public UserClientFallbackFactory userClientFallbackFactory() {
           return new UserClientFallbackFactory();
       }
   }
   ```

5. 在远程调用处使用`UserClientFallbackFactory`对象

   ```java
   package com.kk.clients;
   
   import com.kk.clients.fallbackfactory.UserClientFallbackFactory;
   import com.kk.pojo.User;
   import org.springframework.cloud.openfeign.FeignClient;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   
   @FeignClient(value = "user-service", fallbackFactory = UserClientFallbackFactory.class)
   public interface UserClient {
       @GetMapping("/user/{id}")
       User findById(@PathVariable Long id);
   }

6. `clean`然后重新`install`下`feign-api`，然后重启`order-service`访问一次查询订单业务接口，然后查看`Sentinel`的控制台，可以看到新的`Feign`客户端簇点链路：

   此时就可以在这个远程调用做相关的线程隔离、熔断机制、流量控制、超时等待处理雪崩问题的操作。

   ![img](https://img-blog.csdnimg.cn/785abe9095ed46d088c33f8261781d4e.png)


#### 16.9.2 使用舱壁模式处理远程调用

舱壁模式即线程隔离有两种方式实现：

- **线程池隔离**：这是我们第一接触舱壁模式即线程隔离时脑海中所想的场景，就是给每一个服务都分配一个线程池，使用每一个服务都只能从线程池中取线程，利用线程池来实现隔离效果
- **信号量隔离**（`Sentinel`默认采用）：这是`Sentinel`默认采用的方式，这种信号量隔离的方式不创建线程池而是采用计数器的方式，记录业务使用的线程数量，当该线程数量达到信号量的上限时，就禁止新的请求

下面这幅图很形象地描述出线程隔离和信号量隔离的两种不同方式：

![img](https://img-blog.csdnimg.cn/d34d9478e170437e84fdfc1cc3e3002b.png)

两种方式的优缺点：

- **线程池隔离**：基于线程池模式，有额外的开销因为线程池需要先创建先消耗资源但是隔离控制更强，而且在线程池模式中还可以加入主动超时，而因为是线程所以肯定支持异步调用，适用于场景：低扇出【扇出就是引用其它类或方法即调用的服务少适合线程池隔离】
- **信号量隔离**：基于计数器模式，实现简单，轻量级开销小因为不需要创建线程池，而是来一个请求计数一个请求，没有线程所以不支持异步调用也不支持主动超时，适用于高频服务调用、高扇出扇出就是引用其它类或方法即调用的服务多适合信号量隔离】的场景

![img](https://img-blog.csdnimg.cn/c84b0c384ac040f2a7bffc14180af63d.png)

对于舱壁模式即线程隔离，`Sentinel`默认采用的隔离方式是：信号量隔离，而对于`Netflix`开发的`Hystrix`则两种线程隔离都是可以的，一是线程池隔离，二是信号量隔离两种实现方式都可以，但遗憾的是`2018`年`Hystrix`就停止更新了。

【**<font color="red">注意：无论是线程池隔离还是信号量隔离都是实现线程隔离的方式即都是实现舱壁模式的方式，这是一种理论，实现这个理论的方式就是线程池隔离/信号量隔离，线程池你一开始就需要开个线程池消耗较大，但是信号量不用。这个需要分清楚。</font>**】

可以在`Sentinel`的控制台中实现舱壁模式：

![img](https://cdn.xn2001.com/img/2022/202205091500754.png)

- `QPS`：就是每秒的请求数，之前已经演示过。
- 线程数：是该资源能使用的`Tomcat`线程数的最大值。也就是通过限制线程数量，实现**线程隔离**（舱壁模式）。

**案例需求**：给`order-service`服务中的`UserClient`的查询用户接口设置流控规则，线程数不能超过`2`，然后利用`JMeter`测试。

![img](https://img-blog.csdnimg.cn/0da950c8f5ce4f72a8e9fd707774a073.png)

使用`JMeter`测试：我们在上面指定了同时进来的线程数量不能超过`2`，现在在`JMeter`中我们设定在同一时间有`10`个线程访问：

![img](https://img-blog.csdnimg.cn/72fffd4c5c574dd7bbd1bcdb491e1dfd.png)

可以看到，`10`个线程只有`2`个线程可以调用`UserClient`而其它`8`个都不允许调用，返回的`User`为`null`。

![img](https://img-blog.csdnimg.cn/0186ab5f123f47138d54e6838005bcf8.png)

#### 16.9.3 使用熔断降级处理远程调用

熔断降级是解决雪崩问题的重要手段，它解决了线程隔离占用资源导致资源浪费的弊端。熔断降级其实就是靠断路器去统计服务调用的异常数、异常比例、慢请求比例。如果超出阈值就会像电路保险丝一样熔断该服务，即拦截调用该服务的一切远程调用请求，而当该被调用服务恢复正常时，断路器会放行访问该服务的请求。

那么断路器是如何知道要不要熔断的呢？又如何知道要不要放行的呢？原来是因为有状态机的存在，断路器控制熔断和放行都是靠状态机来完成的，状态机有打开、关闭、半开放三种状态，断路器会根据这三种状态伺机而行。

如下图就是一个断路器的状态机：

![img](https://img-blog.csdnimg.cn/5d6e8f3a18df49a59b764287065df27d.png)

具体学习下**状态机的三种状态**：

- **<font color="red">`closed`</font>：**关闭状态，表示熔断关闭，不熔断，断路器放行所有请求，并开始统计异常比例、慢请求比例。会去判断是否达到熔断条件，这一步叫做【熔断策略】，当达到熔断条件的时候状态机就会切换到`open`状态即熔断打开的状态。
- **<font color="red">`open`</font>：**打开状态，前面说过断路器统计的异常数量、异常请求比例、慢请求比例一旦达到熔断条件即熔断策略，就会进行熔断，拒绝所有访问该被熔断的远程服务的请求，快速失败，并且返回降级逻辑代码中返回的数据，`5s`后，`open`状态就会进入半开放状态即`half-open`状态。
- **<font color="red">`half-open`</font>：**半开放状态，会在一段时间后放行一次请求【其本质就是测试下被调用服务是否恢复正常了，你也可以理解为是心跳检测】，根据请求返回的执行结果进行如下操作：
  1. 若返回结果是正常的则代表请求成功，此时状态机会切换到`closed`状态对该服务不再进行熔断
  2. 若返回结果是异常的则代表请求失败，此时状态机回切换到`open`状态继续熔断远程调用，拒绝所有请求，`5s`之后又进入`half-open`半开状态，如此反复循环，直到在某次半开状态的是否测试请求返回的结果是正常的。

我们前面说过熔断器是否熔断即状态机是否进入`open`状态的标志是熔断器统计的异常数、异常比例、慢请求比例是否达到了熔断条件这就是熔断策略，在熔断降级中断路器的熔断策略有三种：

##### 16.9.3.1 熔断策略 --- 异常比例

**异常比例**

异常比例指的是：在统计指定时间内的远程调用时，如果调用的次数超过指定的请求数，并且抛出异常的数量比例达到了指定的比例阈值即超过了指定的异常数时就会触发熔断。

例如，异常比例设置如下，统计最近`1000ms`即`1s`内的请求，如果请求量超过`10`次，并且异常比例不低于 `0.4`即`10`次请求中有`4`次出现了异常，则触发熔断。

【最小请求数的意思是即是异常比例达到了，但是所有的请求数量没有超过该最小请求数也不会发生熔断，比如只发送了`4`个但是最小请求数是`5`个，虽然按比例达到了`80%`但是仍然不会触发熔断】

![img](https://img-blog.csdnimg.cn/a71c0ba2d5394806af6b372e8143a9a2.png)

**案例需求**：给`UserClient`的查询用户接口设置降级规则，统计时间为`1s`，最小请求数量为`5`，失败阈值比例为`0.4`，熔断时长为`5s`

我们在服务调用的`controller`代码中当`id=1`的时候故意抛出异常：

```java
@GetMapping(value = "/{id}")
public User queryById(@PathVariable("id") Long id, @RequestHeader(value = "sign", required = false) String sign) {
    log.warn(sign);
    if(id == 1l) throw new RuntimeException("故意抛出异常，测试异常比例熔断机制");
    return userService.queryById(id);
}
```

设置熔断规则：

![img](https://cdn.xn2001.com/img/2022/202205091520694.png)

测试，因为后台已经抛出异常了，所以直接快速刷新`5`次即可，然后访问`id = 102`可以看到为`null`，因为远程调用被熔断了，所以所有的请求在`5s`之内都会被拒绝，因为前面有做`Feign`跟`Sentinel`的整合所以当发起调用`UserClient`时由于发生了熔断所以就会返回一个`null`的`User`：

![img](https://img-blog.csdnimg.cn/4404cb7f807c424da94eaed03f8a399a.png)

![img](https://img-blog.csdnimg.cn/f979cbfe758a44c1b4d646703858d4f2.png)

##### 16.9.3.2 熔断策略 --- 异常数

**异常数**

异常数跟异常比例没什么区别，指的是在一段时间内断路器统计比如`1000ms`之内，如果所有的请求一起的数量比如`10`个【最小请求数】，请求异常请求的数量大于指定的数量则比如指定为`2`，则触发熔断

![img](https://img-blog.csdnimg.cn/633fd578966c4b5db22ec1a3fe839771.png)

可以按照异常比例中的案例更改下，也可以得到同样的效果，当`1s`内发送的请求数量为`5`但是异常`>= 2`则触发熔断机制。

![img](https://img-blog.csdnimg.cn/019ab84d8c7b4752a0a0a31b4775d305.png)

##### 16.9.3.3 熔断策略 --- 慢请求比例

**慢请求比例**

什么是慢请求呢？当业务的响应时长即`RT`大于最大指定的响应时长即最大`RT`则认为这是一个慢请求，如果在指定时间内，请求数量超过设定的最小请求数并且慢请求的比例大于设定的阈值，则触发熔断。

例如下图，设置最大`RT`超过`500ms`的调用即发送的请求响应时长超过了`500ms`则认为这是一个是慢请求，统计最近`10000ms`即`1s`内的请求，如果请求量超过`10`次，并且慢调用比例不低于`0.5`，则触发熔断，状态机会进入`open`状态，熔断时长为`5s`，然后进入`half-open`状态，放行一次请求做测试，若测试请求成功则状态机进入`closed`状态。

![img](https://img-blog.csdnimg.cn/4edfb520b8304de0bd0148a60a07afd5.png)



**案例需求**：给`UserClient`的查询用户接口设置降级规则，慢调用的`RT`阈值为`50ms`，统计时间为`1s` ，最小请求数量为`5`，失败阈值比例为`0.4`，即`5`个请求里面有`2`个请求是慢请求就触发熔断，熔断时长为`5s`；

设定熔断规则：

![img](https://img-blog.csdnimg.cn/69f88e1dc77e4ff392f831440c30f3a9.png)

可以在后端处理远程调用请求的地方做一下`sleep`，睡个`60ms`：

```java
@GetMapping(value = "/{id}")
public User queryById(@PathVariable("id") Long id, @RequestHeader(value = "sign", required = false) String sign) throws InterruptedException {
    log.warn(sign);
    if(id == 1l) throw new RuntimeException("故意抛出异常，测试异常比例熔断机制");
    else if(id == 2l) Thread.sleep(60);
    return userService.queryById(id);
}
```

`1s`之内疯狂刷新`5`次请求，查询：http://localhost/order/102，可以看到触发了熔断机制`User`的结果为`null`：

![img](https://img-blog.csdnimg.cn/f68e8553d7624c82aff0c8a446ed9d86.png)

可以通过查询其它请求验证触发了熔断机制：http://localhost/order/103

![img](https://img-blog.csdnimg.cn/906df6c94352454088184c24abbfb305.png)

### 16.10 授权规则

授权规则可以对请求方即调用方的来源做判断和控制。有白名单和黑名单两种方式，比如我们希望对`test`的访问设置白名单，只有`appA`和`appB`的请求才能访问。

- 白名单：来源`（origin）`在白名单内的调用者允许访问
- 黑名单：来源`（origin）`在黑名单内的调用者不允许访问

点击左侧菜单的授权，可以看到授权规则：

![img](https://img-blog.csdnimg.cn/373018b235bd4bfe8f570ecddee1a795.png)

- 资源名：就是受保护的资源，例如：`/order/{orderId}`
- 流控应用：就是来源者的名单
  - 如果是勾选白名单，则名单中的来源被许可访问。
  - 如果是勾选黑名单，则名单中的来源被禁止访问。

比如我们允许请求从 gateway 到 order-service，不允许浏览器访问 order-service，那么白名单中就要填写**网关的来源名称（origin）**。

![img](https://img-blog.csdnimg.cn/9bf6c40ce2ed44f09330e7dcbe89be01.png)

`Sentinel`是通过`RequestOriginParser`这个接口的`parseOrigin()`方法来获取请求的来源的。你在白名单的设置只是设置了说你是只能让来源于`gateway`的访问，但是怎么判断这个来源是哪里来的呢？你可以自己去获取。你可以对这些来源做一个加密的工作都是可以的。

该接口有个`parseOrigin`方法，作用就是从`request`的对象中，获取请求者的`orgin`值并返回。**<font color="red">默认情况下，`Sentinel`不管请求者从哪里来也就是来源是哪里，返回值永远是`default`</font>**。因此我们需要做特殊化，自定义这个接口的实现，让不同的请求，返回的是不同的来源即不同的值，而不再是`default`。【**<font color="deepskyblue">实际生产活动中肯定远比这复杂，不可能只是搞个`orgin`验证就完事，这里只是一个简单的学习过程，重要的是内在思想</font>**】

```java
public interface RequestOriginParser() {
    /**
     * 从请求request对象中获取origin，获取方式自定义
     */
    String parseOrigin(HttpServletRequest request);
}
```

例如：在`order-service`服务中，定义一个类实现`RequestOriginParser`接口：

```java
package com.kk.order.sentinel;


import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestHeaderOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        String origin = httpServletRequest.getHeader("origin");
        if(Strings.isEmpty(origin)) origin = "blank";
        return origin;
    }
}
```

之前有做一个专门的网关`Gateway`，我们让所有来自网关的请求都带上`origin`头并且内容就为`gateway`，这个实现很简单，我们只需要修改`Gateway`的配置文件`application.yml`即可：

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - AddRequestHeader=origin,gateway
```

![img](https://img-blog.csdnimg.cn/77d5937c687540c49965e4df804c2b61.png)

然后前面学习`Gateway`只做了`user-service`的，所以我们需要添加`order-service`的路由：

```java
- id: order-service
  uri: lb://order-service
  predicates:
    - Path=/order/**
  filters:
    - AddRequestHeader=sign, kk.com is eternal
```

如此一来，凡是从网关过来的路由到微服务的请求都会带上`orgin`头。而不经过网关的访问就没有这个头。

接下来在`Sentinel`的控制台中的授权规则一栏添加授权规则，放行`orgin`值为`gateway`的请求：

![img](https://img-blog.csdnimg.cn/32b9bd8f184245649760dbd93311661a.png)

然后就可以重启：`Gateway OrderS-Service`访问了，可以看到：

- 当访问http://localhost:8080/order/103时，是无法访问的，显示：`Blocked by Sentinel (flow limiting)`被`Sentinel`锁住限流

  ![img](https://img-blog.csdnimg.cn/9ade4205c3c248e08c5862a9dee4e4c4.png)

- 当访问http://localhost:10010/order/103?authorization=admin是可以访问的

  ![img](https://img-blog.csdnimg.cn/1fd702fc7f6a453199d786d9874a8bfd.png)

而且会在控制台中打印来源：

![img](https://img-blog.csdnimg.cn/7dc0119decca411ebc77469959690370.png)

上述有个不太好的地方就是访问`8080:103`但是返回的结果却跟流控、熔断降级等的异常都是一样的，都是显示：`Blocked by Sentinel (flow limiting)`这样根本无法具体得知是流控了还是来源不符合授权规则。所以当然就有自定义抛出返回异常的需求，`Sentinel`肯定也想到了这一点，只要实现了`BlockException`接口即可：其中`BlockException`就是被`Sentinel`拦截时抛出的异常，它含有多个不同的子类：

| **异常**               | **说明**           |
| :--------------------- | :----------------- |
| `FlowException`        | 限流异常           |
| `ParamFlowException`   | 热点参数限流的异常 |
| `DegradeException`     | 降级异常           |
| `AuthorityException`   | 授权规则异常       |
| `SystemBlockException` | 系统规则异常       |

```java
public interface BlockExceptionHandler {
    /**
     * 处理请求被限流、降级、授权拦截时抛出的异常：BlockException
     */
    void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception;
}
```

根据此，我们就可以在访问的资源处自定义抛出异常了：

```java
package com.kk.order.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MySentinelExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        //根据 e 的不同返回不同的显示界面
        String msg = "未知异常";
        int status = 429;
        if (e instanceof FlowException) msg = "请求被限流了";
        else if (e instanceof ParamFlowException) msg = "请求被热点参数限流";
        else if (e instanceof DegradeException) msg = "请求被降级了";
        else if (e instanceof AuthorityException) {
            msg = "请求没有权限访问";
            status = 401;
        }
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(status);
        httpServletResponse.getWriter().println("{\"msg\": " + msg + ", \"status\": " + status + "}");//这里可以自定义一个类写，会更好
    }
}
```

重启`order-service`进行测试，可以看到自定义异常成功：

![img](https://img-blog.csdnimg.cn/536307a7b7b2476ab568a845e4ae225a.png)

### 16.11 规则持久化

不知有没有发现，当你重启服务器的时候，之前在`Sentinel`设定的规则也随之消失了，这是因为`Sentinel`所有的规则都是在内存存储的，并没有做持久化，在生产环境中，我们当然不想因为重启就把之前辛辛苦苦耗费心思的规则白白丢失掉，所以我们要对规则做持久化，避免丢失。

规则是否能够持久化，取决于规则的管理模式，`Sentinel`支持三种规则管理模式：

- 原始模式：`Sentinel`的默认模式，将规则保存在内存中，重启服务后规则就会丢失
- `pull`模式
- `push`模式

#### 16.11.1 `pull`模式

控制台`Sentinel Dashboard`将配置好的规则推送到`Sentinel`客户端，客户端将这些规则保存到本地文件或者数据库中，此后`Sentinel Dashboard`不定时地就去本地文件或者数据库中查询，更新本地规则。

![img](https://img-blog.csdnimg.cn/7c0bfdcb47504eabb7b790fb2c4c5941.png)

#### 16.11.2 `push`模式

控制台`Sentinel Dashboard`将配置好的规则推送到远程的配置中心，比如`Nacos`，`Sentinel`客户端监听配置中心`Nacos`，然后获取配置变更的推送消息，完成本地配置的更新。这是一种比较友好的模式。

![img](https://img-blog.csdnimg.cn/7f04a16943fd4c0b863e2b5c8e6b6df3.png)

它具体是如何操作的呢？我们可以模拟下：首先修改`order-service`让其监听`Nacos`中的`Sentinel`配置的规则。

1. 在`order-service`中引入`Sentinel`监听`Nacos`的依赖：

   ```XML
   <dependency>
       <groupId>com.alibaba.csp</groupId>
       <artifactId>sentinel-datasource-nacos</artifactId>
   </dependency>
   ```

2. 修改配置文件：

   ```yaml
   spring:
     cloud:
       sentinel:
         datasource:
           flow:
             nacos:
               server-addr: localhost:8848 # nacos地址
               dataId: orderservice-flow-rules
               groupId: SENTINEL_GROUP
               rule-type: flow # 还可以是：degrade、authority、param-flow
   ```

3. 修改`Sentinel`的源码，因为`SentinelDashboard`默认是不支持`Nacos`的持久化的所以需要修改源码。`Sentinel`的源码官方只开放了一半，另一半用于商业化。

   1. 解压`Sentinel`源码包：

      ![img](https://cdn.xn2001.com/img/2022/202205101404663.png)

   2. 然后用`IDEA`打开这个项目，结构如下：

      ![img](https://cdn.xn2001.com/img/2022/202205101404666.png)

   3. 在`sentinel-dashboard`源码的`pom`文件中，`nacos`的依赖默认的`scope`是`test`，只能在测试时使用，这里要去除：

      ![img](https://cdn.xn2001.com/img/2022/202205101405160.png)

   4. 将`sentinel-datasource-nacos`依赖的`scope`去掉，改成下面这样：

      ```xml
      <dependency>
          <groupId>com.alibaba.csp</groupId>
          <artifactId>sentinel-datasource-nacos</artifactId>
      </dependency>
      ```

   5. 在`sentinel-dashboard`的`test`包下，已经编写了对`nacos`的支持，我们需要将其拷贝到`main`中：

   [![img](https://cdn.xn2001.com/img/2022/202205101405035.png)](https://cdn.xn2001.com/img/2022/202205101405035.png)

   6. 然后，还需要修改测试代码中的`NacosConfig`类：

      ![img](https://cdn.xn2001.com/img/2022/202205101406914.png)

   

   7. 修改其中的`Nacos`地址，让其读取`application.properties`中的配置：

      ![img](https://cdn.xn2001.com/img/2022/202205101404688.png)

   8. 在`sentinel-dashboard`的`application.properties`中添加`Nacos`地址配置：

      ```properties
      nacos.addr=192.168.56.1:8848
      ```

   9. 另外，还需要修改 `com.alibaba.csp.sentinel.dashboard.controller.v2` 包下的`FlowControllerV2`：

      ![img](https://cdn.xn2001.com/img/2022/202205101404531.png)

   10. 让我们添加的`Nacos`数据源生效：

       ![img](https://cdn.xn2001.com/img/2022/202205101407560.png)

   11. 接下来，还要修改前端页面，添加一个支持`nacos`的菜单。修改 `src/main/webapp/resources/app/scripts/directives/sidebar/` 目录下的`sidebar.html`文件：

       ![img](https://cdn.xn2001.com/img/2022/202205101404891.png)

   12. 将其中的这部分注释打开：

       ![img](https://cdn.xn2001.com/img/2022/202205101408124.png)

   13. 修改其中的文本：

       ![img](https://cdn.xn2001.com/img/2022/202205101408073.png)

   14. 运行`IDEA`中的`maven`插件，编译和打包修改好的`sentinel-dashboard`：

       ![](https://cdn.xn2001.com/img/2022/202205101408008.png)

   15. 启动：

       ```sh
       java -jar sentinel-dashboard.jar
       ```

   16. 如果要修改`Nacos`地址，可以添加参数：

       ```sh
       java -jar -Dnacos.addr=localhost:8848 sentinel-dashboard.jar
       ```

## 17. 分布式事务【重点 + 难点】

### 17.1 什么是分布式事务？分布式事务如何产生的？

在传统的数据库事务中，必须满足四个原则即`ACID`原则即原子性、一致性、隔离性、永久性。

![img](https://cdn.xn2001.com/img/2022/202205231445816.png)

传统的单体架构只有一个数据库可以满足`ACID`原则，那在微服务中呢？事务是否可以满足这四大原则呢？

比方说现在有一个案例：能满足`ACID`原则吗？

- 当用户下单的时候，会去调用订单服务创建订单，然后会调用账户服务去扣减余额，最后会调用库存服务去扣减商品库存。订单服务、账户服务、库存服务都有自己的数据库，无论是创建订单还是扣减余额还是扣减商品库存都会去操作属于它的数据库。

  ![img](https://cdn.xn2001.com/img/2022/202205231447738.png)

- 理想的情况就是：创建订单 ---> 扣除余额 ---> 扣除库存数量，这一系列操作构成了一个事务，如果中间某一环节出错了，那么整个事务都应该撤销，比如中间扣除账户余额的操作失败了，那么应该撤销之前的创建订单。一连串的操作要么操作全部成功，要么操作全部失败，不允许部分成功或者部分失败，这就是分布式系统中的事务。

1. 创建数据库，名为`seata_demo`

   ```sql
   /*
    Navicat Premium Data Transfer
   
    Source Server         : local
    Source Server Type    : MySQL
    Source Server Version : 50622
    Source Host           : localhost:3306
    Source Schema         : seata_demo
   
    Target Server Type    : MySQL
    Target Server Version : 50622
    File Encoding         : 65001
   
    Date: 24/06/2021 19:55:35
   */
   
   SET NAMES utf8mb4;
   SET FOREIGN_KEY_CHECKS = 0;
   
   -- ----------------------------
   -- Table structure for account_tbl
   -- ----------------------------
   DROP TABLE IF EXISTS `account_tbl`;
   CREATE TABLE `account_tbl`  (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `money` int(11) UNSIGNED NULL DEFAULT 0,
     PRIMARY KEY (`id`) USING BTREE
   ) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;
   
   -- ----------------------------
   -- Records of account_tbl
   -- ----------------------------
   INSERT INTO `account_tbl` VALUES (1, 'user202103032042012', 1000);
   
   -- ----------------------------
   -- Table structure for order_tbl
   -- ----------------------------
   DROP TABLE IF EXISTS `order_tbl`;
   CREATE TABLE `order_tbl`  (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `commodity_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `count` int(11) NULL DEFAULT 0,
     `money` int(11) NULL DEFAULT 0,
     PRIMARY KEY (`id`) USING BTREE
   ) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;
   
   -- ----------------------------
   -- Records of order_tbl
   -- ----------------------------
   
   -- ----------------------------
   -- Table structure for storage_tbl
   -- ----------------------------
   DROP TABLE IF EXISTS `storage_tbl`;
   CREATE TABLE `storage_tbl`  (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `commodity_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `count` int(11) UNSIGNED NULL DEFAULT 0,
     PRIMARY KEY (`id`) USING BTREE,
     UNIQUE INDEX `commodity_code`(`commodity_code`) USING BTREE
   ) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;
   
   -- ----------------------------
   -- Records of storage_tbl
   -- ----------------------------
   INSERT INTO `storage_tbl` VALUES (1, '100202003032041', 10);
   
   SET FOREIGN_KEY_CHECKS = 1;
   ```

   此时订单数据库为空：

   ![img](https://img-blog.csdnimg.cn/399777e13b014a7ea5af90bb6ec1a805.png)

   有一个账户：账户里面有`1000`块钱

   ![img](https://img-blog.csdnimg.cn/dd3615c9df354f63b026cc0f5ae214a1.png)

   有一个库存，里面`id=1`的商品数量为`10`：

   ![img](https://img-blog.csdnimg.cn/21946a6574fe43279b1aeea1034e7ae2.png)

2. 导入写好的`seata-demo`，修改下数据库密码跟`Nacos`的配置信息

   ![img](https://img-blog.csdnimg.cn/ee3e981a92ed4ae2a0158ee2a659bb62.png)

3. 启动`Nacos`以及所有微服务

   ![img](https://img-blog.csdnimg.cn/c1e67d13b4cd4510acd5912c7f8162df.png)

4. 测试下单功能，发出`Post`请求：http://localhost:8082/order?userId=user202103032042012&commodityCode=100202003032041&count=2&money=200

   ![img](https://img-blog.csdnimg.cn/abd328488d1145829196a9128bd62112.png)

   可以看到页面显示正常，返回结果为订单`id = 1`。

5. 上述是正常的情况，执行完毕之后，库存的数量从`10`变成了`8`，此时再次访问，这次需要商品的数量为`10`，因为没有那么多商品，所以订单肯定需要创建失败，此时三个数据库，订单表不应该出现新的订单，账户表中余额不应该有变化，库存表也不应该有任何变化，但实际情况是如何的呢？

   http://localhost:8082/order?userId=user202103032042012&commodityCode=100202003032041&count=10&money=200

   ![img](https://img-blog.csdnimg.cn/87cec5a7784249599a922d39e61318ec.png)

6. 请求发送失败因为库存中的数量不够`10`，查看订单表、库存表发现都没问题没啥变化，但是但是！此时再次访问账户表，发现账户余额变少了！！！原先是`800`变成了`600`！！！

   ![img](https://img-blog.csdnimg.cn/c021593592af42b1ae1818e0bcf6f508.png)

从上述操作中就得知，单纯的这种想要达成分布式的事务根本无法遵循事务`ACID`四大原则。因为在下一个数据库被操作时，上一个数据库的操作已经提交了，所以你想回滚也是无法回滚的。

用官方的话述说就是：**在分布式系统下，一个业务跨越多个服务或者数据源【比如这里一个下单业务就跨了`3`个服务】，每个服务都是一个分支事务，要保证所有的分支事务最终状态一致，但是事实上这些分支事务并无法解决状态最终一致的问题，这样的事务我们称之为分布式事务。**

比如这里的：创建订单、扣除余额、扣除库存就是三个分支事务，要保证这三个分支事务的**最终状态一致即要么都成功要么都失败**，这就是分布式事务。

**<font color="red">分布式产生的根本原因就是</font>：一个业务需要跨服务或者数据源才能完成。**

### 17.2 如何解决分布式事务的问题？

#### 17.2.1 `CAP`不可能三角

1998年，加州大学的计算机科学家`Eric Brewer`提出，分布式系统有三个指标。分别是一致性、可用性、分区容错性。

> - Consistency（一致性）
> - Availability（可用性）
> - Partition tolerance （分区容错性）

![img](https://img-blog.csdnimg.cn/b0ab0d87b23b4b0aba24a5305958e117.png)

说是`CAP`不可能三角的原因是，这三个指标在分布式当中无法同时被满足，所以叫做`CAP`不可能三角。只能满足两个。为什么呢？我们先来理解一下各个指标的意思：

1. 一致性`Consitency`

   一致性指的是用户访问分布式系统中的任意节点，得到的数据都必须保持一致也就是所谓的强一致性，也就是说你数据更新完毕，然后我访问任意一个节点得到的数据都是完全一致的。比如现在有两个节点，这两个节点的初始数据是一样的：

   ![img](https://img-blog.csdnimg.cn/85c293b8255748dd8f5c51d88edc7d2e.png)

   倘若此时修改了节点`node01`的数据`data`，`v0`变成了`v1`，那么此时两个节点的数据就不一致了：

   ![img](https://img-blog.csdnimg.cn/8e32a1d0af444f1eaf31ea3f519f588a.png)

   要想保持一致性，必须做数据同步：

   ![img](https://img-blog.csdnimg.cn/5b89d2c459054502a97624202cb20a64.png)

2. 可用性`Avalibility`

   可用性指的是用户访问集群中的任意健康节点，必须可以得到响应而不是超时或者拒绝即所谓的高可用，**比如有个节点因为为了满足强一致性等待数据同步而导致阻塞了来往的请求，那么该节点就不满足高可用性即不满足这里的可用性`Avalibility`。**意思就是说只要你是一个健康节点我访问你你就必须给我响应。

   如图，这是一个有三个节点的集群，访问任何一个节点都可以及时得到响应：

   ![img](https://img-blog.csdnimg.cn/6dad6e55a0b344e98fdd3daf45dff538.png)

   但此时`node03`节点可能因为故障或不知道什么原因比如正在发生数据同步导致无法访问了，导致发送给`node03`节点的请求被拒绝或者阻塞了。此时就代表该节点不可用即不满足可用性。

   ![img](https://img-blog.csdnimg.cn/8907ee7b5aff488f816017bf6968298f.png)

3. 分区容错性`Partition tolerance`

   分区容错性需要拆开来看，分区代表的其实是网络意义上的分区，因为网络肯定是不可靠的，存在延迟等各种可能出现的问题，所以节点之间可能出现无法通讯的问题是非常正常的，此时容错就冒出来了，指的是允许出现网络故障但是整个系统依然能够正常的提供服务。

   ![img](https://img-blog.csdnimg.cn/542f76ba591e4d579ad5484127aa9d5d.png)

对可用性、一致性、分区容错性有了一定的了解之后，再来说说为什么这三个指标无法同时被满足？要知道网络肯定不是`100%`可靠的，就是你是无法保证网络是完全一致好的健康的，肯定有时候是会出现故障的，而你的系统肯定不能因为网络故障就宕机停止服务了，所以分区容错性`Partition Tolerance`是一定要被满足的。**<font color="red">即`CAP`中的`P`是一定要被满足的</font>**。

那`CA`呢？如图，假设现在第三个节点即`node03`因为网络故障从而无法访问，所有发给`node03`节点的请求都被阻塞或者拒绝了。因为已经保证了分区容错性，所以此时`node01`跟`node02`这两个节点还是可以被访问的，然后在某一时刻，`node02`节点上的数据发生了变化，此时`node01`就会发生数据同步，到这里一切都还很正常，但问题就出现在因为网络故障导致无妨正常访问的`node03`节点中：

![img](https://img-blog.csdnimg.cn/0b59090baf0c4b8b98500fbb5146b525.png)

1. 如果要满足一致性，那么就表示`node03`节点也需要进行数据同步。此时你想象一幅场景，当网络回复正常的一瞬间，`node03`可以访问了，那么此时返回的数据必然是跟`node01`以及`node02`的节点数据是不一致的，这就违反了一致性原理，所以为了避免这种情况的出现，你就必须先停止`node03`对外服务，锁定它的读操作和写操作将`node03`的数据同步更新之后再对外开放，但这一过程肯定就导致`node03`节点是不可用的了。此时就是满足了一致性但是不满足可用性。
2. 相反的，如果你保证了节点`node03`是可以被访问的，那么数据一致性必然就无法得到满足了。

上述就构成**<font color="red">`CAP`不可能三角</font>**。所以就只能满足`CP`或者只能满足`AP`两个原理 了。

- 问一个问题：之前我们学习的`ElasticSearch`集群是`CP`还是`AP`呢？

  > 那当然是`CP`了，因为无论你访问哪一个节点你的数据肯定是要求一致的，如果有节点`ES`因为网络出现故障的时候，就会将该节点从集群中剔除，`ES`会将分片分散到其它的健康节点当中。该故障节点就无法访问，牺牲了可用性，所以`ES`集群满足的是`CP`原则。

#### 17.2.2 `Base`理论

通过学习`CAP`不可能三角我们直到一致性和可用性是无法同时满足的，但是但是！无论是可用性和一致性我们都无法放弃啊，因为这两个都非常重要，那么有没有一种折中的方法可以追求一个平衡点呢？可能`Base`理论可以帮助到我们。

`Base`理论其实就是对`CAP`的一种解决思路，包含了三个思想：

- **`Bascially Available`（基本可用**）：分布式系统在出现故障的时候，允许损失部分可用性，即保证核心可用
- **`Soft Stata`（软状态）**：在一定时间内，允许出现中间状态，比如临时的不一致状态
- **`Eventually Consistent`（最终一致性）**：虽然无法保证强一致性，但是在软状态接收后，最终达到数据一致

**<font color="red">也就是说，某个节点出现故障，允许损失部分可用，当节点恢复健康时会到一种软状态允许数据临时不一致，但是所有的数据最后都一定是保持一致的，这就是最终一致性。</font>这就是`Base`理论。**

**一阴一阳之谓道。**

#### 17.2.3 解决分布式事务问题

分布式事务的最大问题其实就是各个分支事务的数据一致性问题，因此在遵守`CAP`的不可能三角，借鉴`Base`理论，有两种解决思路：

1. `AP`模式【可用性、分区容错性】【最终一致】：各个分支事务分别执行和提交，允许出现结果不一致即处于一种软状态，临时的数据不一致没关系，随后采用弥补措施恢复数据即可，实现数据的最终一致性。保证高可用，但是弱一致性。
2. `CP`模式【一致性、分区容错性】【强一致】：各个分支事务分别执行完毕之后互相等待，然后同时提交，如果中途有部分出错就全部不提交，同时回滚，实现数据的最终一致性，因为是`CP`模式所以需要保证强一致性，但是借鉴了`Base`理论，所以保证弱可用性即可。

在分布式系统中，通常都是一致性大于可用性，因为通常数据的一致是非常非常重要的。为了解决分布式事务，各个分支事务必须需要能够感知到彼此的事务状态才能保证状态一致，也就是各个分支事务相互之间要互相告诉即通讯，**因此需要一个事务协调者来协调每一个事务的参与者即一个事务协调者来帮助各个分支事务来感知其它分支事务的状态使它们达成一致**。**分支事务**构在一起形成一个整体就构成了**全局事务**。

盲猜事务协调者在`CP`模式下：你一个分支事务完成`OK`我等记下来了，然后另一个分支事务完成`OK`我登陆下来了，然后我下一个分支事务没有完成，我也登记下来，然后同时回滚，如果都成功就表示最终数据一致，同时提交。

![img](https://img-blog.csdnimg.cn/40819e1f1e0543988d4ff18001090ba5.png)

### 17.3 `Seata`

`Seata`是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。`Seata`将为用户提供了`AT`、`TCC`、`SAGA`和`XA`事务模式，为用户打造一站式的分布式解决方案。

官网地址：http://seata.io/，其中的文档、播客中提供了大量的使用说明、源码分析。

![img](https://img-blog.csdnimg.cn/807e99128f9c4cd18db778bc7fb2907f.png)

#### 17.3.1 `Seata`的架构

- **`TC (Transaction Coordinator)` -** **事务协调者**：维护全局和分支事务的状态，协调全局事务提交或回滚。
- **`TM (Transaction Manager)` -** **事务管理器**：定义全局事务的范围、开始全局事务、提交或回滚全局事务。
- **`RM (Resource Manager)` -** **资源管理器**：管理分支事务处理的资源，与 TC 交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。

![img](https://img-blog.csdnimg.cn/eab8b7dd0d764a2aa16795030e1a4cd7.png)

`Seata`基于上述架构提供了四种不同的分布式事务解决方案：无论哪种方案，都离不开 TC，也就是事务的协调者。

- `XA`模式：强一致性分阶段事务模式，牺牲了一定的可用性，无业务侵入
- `TCC`模式：最终一致的分阶段事务模式，有业务侵入
- `AT`模式：最终一致的分阶段事务模式，无业务侵入，也是`Seata`的默认模式
- `SAGA`模式：长事务模式，有业务侵入

![img](https://img-blog.csdnimg.cn/b6129c670b2d49699a2d995af8f8fb7c.png)

#### 17.3.2 `Seata`的部署

因为离不开`TC`所以首先需要部署`Seata`的`TC-Server`：**【坑：这里不要使用`Docker`部署，可能会因为`IP`问题无法实现理想的效果。因为`Seata`跟`Nacos`是在同一网关，所以你访问都是直接通过局域网上到`Nacos`的，而你的服务又是在另外一个网络，导致无法访问，所以不要不要不要在这里使用`Docker`部署，浪费了好多时间...】**

1. 下载`seata-server`包：https://seata.io/zh-cn/blog/download.html

2. 解压到非中文目录中

3. 修改`conf`目录下的`registry.conf`文件，为了让`TC`服务的集群可以共享配置，我们选择了`Nacos`作为统一配置中心。因此需要在`Nacos`配置中心即访问http://192.168.56.1:8848/nacos/中配置好`Seata-Server`的配置文件`seataServer.properties`。

   注意因为这里是在`Docker`，所以需要使用`docker inspect nacos-quick`查看下`nacos`的容器`ip`然后再定义

   ```yaml
   registry {
     # tc服务的注册中心类，这里选择nacos，也可以是eureka、zookeeper等
     type = "nacos"
   
     nacos {
       # seata tc 服务注册到 nacos的服务名称，可以自定义
       application = "seata-server"
       serverAddr = "172.17.0.2:8848"
       group = "DEFAULT_GROUP"
       namespace = ""
       cluster = "SH"
       username = "nacos"
       password = "nacos"
     }
   }
   
   config {
     # 读取tc服务端的配置文件的方式，这里是从nacos配置中心读取，这样如果tc是集群，可以共享配置
     type = "nacos"
     # 配置nacos地址等信息
     nacos {
       serverAddr = "172.17.0.2:8848"
       namespace = ""
       group = "DEFAULT_GROUP"
       username = "nacos"
       password = "nacos"
       dataId = "seataServer.properties"
     }
   }
   ```

<hr/>

使用`Docker`部署也说下，不要使用这是雷：【记得配置镜像加速器，否则下载特别慢，`Seata-Server`的默认端口为`8091`】，还需要注意的是下载的`seata-server`版本需要为`1.4.2`的，否则可能配置出现问题

1. ```sh
   docker pull seataio/seata-server:1.4.2
   ```

2. ```sh
   docker run --name seata-server -p 8091:8091 seataio/seata-server:1.4.2
   ```

   ![img](https://img-blog.csdnimg.cn/65cd777e3aa54bca908da63f5ecc4b47.png)

   后台启动用这个：可以将`registry.conf`直接挂载到本地，方便修改

   ```sh
   docker run --name seata-server -v /var/lib/docker/volumes/seata/resources/registry.conf:/seata-server/resources/registry.conf -p 8091:8091 seataio/seata-server:1.4.2 -m file >nohup.out 2>1 &
   ```

3. ```sh
   docker exec -it seata-server /bin/sh
   docker logs -f seata-server
   ```

4. 修改配置文件即可`vi`

<hr/>

访问`nacos`可以发现注册上了`seata-server`：

![img](https://img-blog.csdnimg.cn/93d1bfdcf8284147aea80c2c316dce04.png)

配置`seataServer.properties`：注意这里的`mysql`地址。

```properties
store.mode=db
store.db.datasource=druid
store.db.dbType=mysql
store.db.driverClassName=com.mysql.cj.jdbc.Driver
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useSSL=false&serverTimezone=Asia/Shanghai
store.db.user=root
store.db.password=123456
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
transport.serialization=seata
transport.compressor=none
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898

Docker 安装 MySQL：
https://cloud.tencent.com/developer/article/1957621
```

`TC`事务协调综合会在管理分布式事务的时候，记录事务相关数据到数据库中，所以需要提前创建好这些表。

1. 新建一个名为`seata`的数据库，运行`SQL`，表的名字需要跟上述在`seataServer.properties`配置的一样，这些表主要记录全局事务、分支事务、全局锁信息。

   ```sql
   SET NAMES utf8mb4;
   SET FOREIGN_KEY_CHECKS = 0;
   
   -- ----------------------------
   -- Table structure for branch_table
   -- ----------------------------
   DROP TABLE IF EXISTS `branch_table`;
   CREATE TABLE `branch_table`  (
     `branch_id` bigint(20) NOT NULL,
     `xid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
     `transaction_id` bigint(20) NULL DEFAULT NULL,
     `resource_group_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `resource_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `branch_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `status` tinyint(4) NULL DEFAULT NULL,
     `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `application_data` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `gmt_create` datetime(6) NULL DEFAULT NULL,
     `gmt_modified` datetime(6) NULL DEFAULT NULL,
     PRIMARY KEY (`branch_id`) USING BTREE,
     INDEX `idx_xid`(`xid`) USING BTREE
   ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
   
   -- ----------------------------
   -- Records of branch_table
   -- ----------------------------
   
   -- ----------------------------
   -- Table structure for global_table
   -- ----------------------------
   DROP TABLE IF EXISTS `global_table`;
   CREATE TABLE `global_table`  (
     `xid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
     `transaction_id` bigint(20) NULL DEFAULT NULL,
     `status` tinyint(4) NOT NULL,
     `application_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `transaction_service_group` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `transaction_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `timeout` int(11) NULL DEFAULT NULL,
     `begin_time` bigint(20) NULL DEFAULT NULL,
     `application_data` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `gmt_create` datetime NULL DEFAULT NULL,
     `gmt_modified` datetime NULL DEFAULT NULL,
     PRIMARY KEY (`xid`) USING BTREE,
     INDEX `idx_gmt_modified_status`(`gmt_modified`, `status`) USING BTREE,
     INDEX `idx_transaction_id`(`transaction_id`) USING BTREE
   ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
   
   -- ----------------------------
   -- Records of global_table
   -- ----------------------------
   
   
   -- ----------------------------
   -- Records of lock_table
   -- ----------------------------
   
   SET FOREIGN_KEY_CHECKS = 1;

2. 重启`seata-server`服务器

   ```sh
   docker restart seata-server
   ```

3. 可以看到服务器运行成功：

   ![img](https://img-blog.csdnimg.cn/93d1bfdcf8284147aea80c2c316dce04.png)

#### 17.3.3 `Seata`微服务集成

1. 各个微服务中引入依赖，直接加在父工程即可

   ```xml
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
       <exclusions>
           <!--版本较低，1.3.0，因此排除-->
           <exclusion>
               <artifactId>seata-spring-boot-starter</artifactId>
               <groupId>io.seata</groupId>
           </exclusion>
       </exclusions>
   </dependency>
   <!--seata starter 采用1.4.2版本-->
   <dependency>
       <groupId>io.seata</groupId>
       <artifactId>seata-spring-boot-starter</artifactId>
       <version>1.4.2</version>
   </dependency>
   ```

2. 修改配置文件，添加`seata-server`的`TC`服务信息，每一个服务都要加

   ```yaml
   seata:
     registry: # TC服务注册中心的配置，微服务根据这些信息去注册中心获取tc服务地址
       type: nacos # 注册中心类型 nacos
       nacos:
         server-addr: 192.168.56.1:8848 # nacos地址
         namespace: "" # namespace，默认为空
         group: DEFAULT_GROUP # 分组，默认是DEFAULT_GROUP
         application: seata-server # seata服务名称
         username: nacos
         password: nacos
     tx-service-group: seata-demo # 事务组名称
     service:
       vgroup-mapping: # 事务组与cluster的映射关系
         seata-demo: SH
   ```

   小知识：**<font color="red">微服务是如何根据这些配置寻找到`TC`地址的呢？</font>**

   我们直到确定一个具体的实例需要直到四个信息：

   1. `namespace`：命名空间，默认为`public`
   2. `group`：分组
   3. `application`：服务名
   4. `cluster`：集群名

   这四个信息在配置文件中都可以找到：结合起来就是：`public@DEFAULT_GROUP@seata-server@SH`

   ![img](https://img-blog.csdnimg.cn/6d03e297ac6247fd94f909e5ae05fbd4.png)

3. 启动微服务，`Seata-Server`控制台上会显示连接的信息，显示`RM register success`资源管理器注册成功！

   ![img](https://cdn.xn2001.com/img/2022/202205242305852.png)

我们前面学习过`Seata`基于上述架构提供了四种不同的分布式事务解决方案，一个个学习下这四种模式：

- `XA`模式：强一致性分阶段事务模式，牺牲了一定的可用性，无业务侵入
- `TCC`模式：最终一致的分阶段事务模式，有业务侵入
- `AT`模式：最终一致的分阶段事务模式，无业务侵入，也是`Seata`的默认模式
- `SAGA`模式：长事务模式，有业务侵入

![img](https://img-blog.csdnimg.cn/b6129c670b2d49699a2d995af8f8fb7c.png)

#### 17.3.4 `XA`模式的分布式解决方案

`XA`规范是`X/Open`组织定义的分布式事务处理`（DTP，Distributed Transaction Processing）`标准，`XA`规范描述了全局的`TM`与局部的`RM`之间的接口，几乎所有主流的数据库都对`XA`规范提供了支持。`XA`模式追求的是强一致性，隔离性采用完全隔离，所以是无代码入侵的，但是正是因为完全隔离这种隔离性所以性能差。

`XA`是规范，目前主流数据库都实现了这种规范，实现的原理都是**基于两阶段提交**。

`TC`去通知`RM`做工作，`RM`资源管理器即一个一个的微服务，做完了一些工作后先不提交而是告诉`TC`我都搞好了，看要不要提交，这是第一阶段，然后`TC`确认所有的`RM`即一个一个的服务是正常的则告诉它们说提交吧，于是它们就去提交事务，一起提交。若出现了异常，就通知它们回滚。保证强一致性。

正常情况的两阶段：

![img](https://img-blog.csdnimg.cn/ef5ae39280f446e0b3492f586820f075.png)

异常情况的两阶段：

![img](https://img-blog.csdnimg.cn/d0604b89987c46fd9bbd692e601967bb.png)

第一阶段：

1. 事务协调者`TC`**通知**每个事务参与者执行本地事务
2. 本地事务**执行完毕后报告**事务执行状态给事务协调者，此时事务不提交，继续持有数据库锁

第二阶段：

- 事务协调者基于第一阶段`RM`发送过来的报告决定下一步操作
  1. 如果第一阶段成功，则通知所有`RM`一起提交事务
  2. 如果第二阶段有任意一个参与者失败了，则通知所有事务参与者回滚事务【保证强一致性】

`Seata`对原始的`XA`模式做了简单的封装和改造，以适应自己的事务模型，基本架构如下：

![img](https://img-blog.csdnimg.cn/0206c06a02d44ed8afa053db6e90ac01.png)

1. `RM`资源管理器率先进行第一阶段工作：
   1. 注册分支事务到`TC`事务协调者
   2. 执行分支业务的`SQL`语句但是不提交
   3. 报告事务的执行状态给`TC`事务协调者

2. `TC`事务协调者根据`RM`报告的事务执行状态进行第二阶段的工作：
   - 事务协调者`TC`检测各个分支事务的执行状态
     1. 如果各个分支事务都成功，则告诉所有`RM`一起提交事务
     2. 如果各个分支事务但凡有一个分支事务执行失败，则告诉所有的`RM`一起回滚事务

3. `RM`资源管理器即一个一个的服务根据从`TC`事务协调者那里接收的指令，提交或者回滚操作执行

`XA`模式的优点：

- 各个分支事务构成强一致性，数据安全，从单个分支事务来看都满足`ACID`的原则
- 常用的数据库都支持，而且实现简单，并且没有代码侵入，没有代码侵入的意思就是没有引入依赖即引入或者继承了别的包或者框架，高度自主研发

`XA`模式的缺点：

- 因为在`RM`的第一阶段要先将事务注册到`TC`然后才执行了`SQL`并且还不能提交，而是将事务状态报告给`TC`，直到`TC`判断完毕，才能根据从`TC`传递过来的指令确认提交还是回滚，所以需要在等待`TC`传递指令之前需要持有数据库锁进行等待。一直等到提交/回滚即第二阶段后才可以释放数据库锁资源，可能有时导致耗时很长，因而性能较差。
- 高度依赖关系型数据库实现事务。所以一旦换个数据库，比如我换成了一个非关系型数据库`Redis`，那就没法实现`XA`模式了。

**<font color="red">【想一想，其实`XA`模式没做什么很高深的东西，其实就是依赖于关系型数据库已经做好的的事务来完成的，所以高度依赖关系型数据库，`Seata TC`在这里的作用只是单纯的做决定，协调，因为关系型数据库的事务都是遵循`ACID`原则的所以这种模式的数据一致性一定是强一致性的，但是需要长时间占用数据库锁资源，这一点不太好。】</font>**

**实现`XA`模式：**

`Seata`的`starter`已经完成了`XA`模式的自动装配，实现起来非常简单，步骤如下：

1. 修改配置，开启`XA`模式：

   ```yaml
   seata:
     data-source-proxy-mode: XA
   ```

2. 发起全局事务的入口添加`@GlobalTransactional`注解，因为这里是创建订单后扣除余额，扣除库存数量，所以这里的全局入口自然是在服务层的创建订单处：

   ```java
   @Override
   @Transactional
   @GlobalTransactional
   public Long create(Order order) {
       // 创建订单
       orderMapper.insert(order);
       try {
           // 扣用户余额
           accountClient.deduct(order.getUserId(), order.getMoney());
           // 扣库存
           storageClient.deduct(order.getCommodityCode(), order.getCount());
       } catch (FeignException e) {
           log.error("下单失败，原因:{}", e.contentUTF8(), e);
           throw new RuntimeException(e.contentUTF8(), e);
       }
       return order.getId();
   }
   ```

3. 重启服务并测试http://localhost:8082/order?userId=user202103032042012&commodityCode=100202003032041&count=10&money=200，现在库存数量还是`8`个，此前做的试验，扣掉了`400`所以账户还有`600`。观察账户余额是否还会出现变化？可以发现访问失败，不再出现原有分布式事务存在的数据不一致问题即账户余额也不会发生变化。

   **说明使用`Seata`实现`XA`模式这种分布式解决方案生效。**

   ![img](https://img-blog.csdnimg.cn/462d139f891746449cf000f3d715fd2c.png)

#### 17.3.5 `AT`模式的分布式解决方案

`XA`模式虽然保证了强一致性但是我们也知道它的缺点就是需要等待`TC`指令，占有数据库锁资源的时间比较长。而`AT`模式就是用来弥补`XA`模式占有数据库锁资源时间长的缺陷的。

`AT`模式也是分阶段提交的但是弥补了`XA`模式长时间占用数据库锁资源的缺陷，但这也比如就导致做不到强一致性了。

![img](https://img-blog.csdnimg.cn/69c176ac758546bdbfd68a397285091f.png)

1. `RM`资源管理器率先进行第一阶段的工作：【可以发现相比于`XA`模式执行`SQL`可以直接提交，然后多了一个记录数据快照的步骤】
   - 向`TC`注册分支事务
   - 记录更新前后的数据快照`undo-log`包括`after-image`以及`before-image`
   - 执行分支业务的`SQL`直接提交，不用等待`TC`指令
   - 向`TC`报告自己的分支事务状态
2. `TC`事务协调者进行第二阶段的工作：
   - 如果各个分支事务都正常，就删除`undo-log`数据快照即可
   - 如果有一个分支事务不正常，就发送指令让分支事务根据`undo-log`数据快照将数据恢复到更新之前的数据
3. `RM`资源管理器进入第二阶段的工作：
   - 如果分支事务出错，才会进行这一阶段的工作，根据`undo-log`将数据恢复到更新之前的数据

上述说的是不是有些许抽象呢？简单的说就是旧数据会被进行一个快照存储起来，然后你就可以随意提交了，当`TC`收集到的分支事务所报告的有一个事务状态是不正常的，只需要让这些分支事务的数据恢复到快照之前即可。

举个例子：

- 现有一个分支业务的`SQL`语句是这样的：`update tb_accoynt set monet = money - 10 where id = 1;`即让`id = 1`的账户余额减少`10`块钱。

  服务要执行这个分支事务的流程如下：

  1. `RM`资源管理器即一个个服务向`TC`注册分支事务：”报告，我要注册一个分支事务“，`TC`接收
  2. `RM`资源管理器数据更新前的数据快照：”分支事务做一个数据快照，记录更改前的数据“
  3. `RM`执行`SQL`语句然后直接提交
  4. `RM`向`TC`报告事务执行状态，是正常的还是不正常的，执行成功？还是执行失败？

  到这里就完成了阶段一。

  然后进入阶段二：

  1. `TC`通过`RM`传递过来的事务状态的报告判断该事务到底是不是可以提交
  2. 如果可以提交，就让`RM`删除旧数据的数据快照
  3. 如果不可以提交，就让`RM`根据数据快照`undo-log`恢复到修改之前的数据

  然后`RM`根据传递过来`TC`指令做相应的操作即可。到这里整个分布式事务执行完毕。

  ![img](https://img-blog.csdnimg.cn/cab0b01a731246fbb702b1c9c5b897dc.png)

【**<font color="red">想想其实就是把之前旧的数据保存起来，然后`TC`协调判断到底全局事务即整个分布式事务到底可不可以提交，如果可以，那就把这个旧数据的数据快照删除避免存储资源的浪费，如果不可以就让该恢复数据的分支事务根据数据快照进行回滚即可。这就是`AT`模式，关键点就在于那个数据快照，有了它`RM`执行了`SQL`语句之后就可以直接提交了。虽然可能导致临时短暂的数据不一致也就是存在软状态，但是因为可以根据数据快照将数据进行恢复，所以虽然不是强一致性可以做到最终一致性。这样也是非常好的一种模式。</font>**】

总结：

1. `XA`模式和`AT`模式最大的区别是什么？

   - 我想，最大的区别就在于**执行`SQL`语句之后是否可以直接提交**，可以直接提交释放数据库锁资源的的就是`AT`模式，而不可以直接提交需要等待`TC`事务协调者的指令的**长期占用数据库锁资源**的就是`XA`模式。

   - 除此之外，`XA`模式跟`TA`模式还有一些区别，比如：`XA`模式恢复数据利用的是关系型数据库本身的**事务回滚**，而`AT`模式则依靠的是**数据快照**来恢复数据。
   - 而且，正是因为`XA`模式是一个要等待`TC`指令才能决定事务提交还是回滚，所以是**强一致性、性能较差**的，但是另外`AT`模式执行`SQL`之后就可以直接提交了，所以存在软状态即允许数据出现短暂的不一致但是数据满足最终一致性，既然如此`AT`模式就是**弱一致性即最终一致、性能较好的**的。

上述所有原理`Seata`分布式事务框架都帮我们解决好了。所以使用起来还是非常简单的，不用我们自己去整什么数据快照啥的。

##### 17.3.5.1 `AT`模式的脏写问题

通过一个例子来说明一下这个脏写问题，现在我有一张`account`账户表，要执行的`sql`语句为：`update account set money = money - 10 where id = 1;`意思就是去修改`id = 1`的账户余额，将其余额扣掉`10`块钱。

1. 现在事务一要执行这条`SQL`语句，在执行`SQL`语句之前会向`TC`发起注册请求，然后会保存`DB`锁即数据库锁，然后将修改当前数据做一个快照，即`money = 100`
2. 执行业务`SQL`：`set money = 90`
3. 提交事务，释放数据库锁，此时数据库中的数据为`90`
4. 然后向`TC`报告事务状态
5. 但是此时又有一个事务来了，它获取到了刚刚事务一释放的数据库锁，并且也向`TC`注册了分支事务，然后保存了当前数据的快照，即`money = 90`
6. 事务二执行了`SQL`语句
7. 事务二提交了数据，此时`money = 80`，提交完毕后随机释放了数据库锁
8. `TC`事务协调这判定事务一在整个分布式事务中有其它事务出现错误，所以下指令让事务一进行恢复数据
9. 事务一接收到`TC`恢复数据的指令，于是根据数据快照将数据进行恢复，此时恢复到`money = 100`

到这里就出现了脏写问题，事务二相当于白做了一次操作，白做相当于血脉不纯，这就叫脏写。

![img](https://img-blog.csdnimg.cn/63fe373a711d4f75adbd23205a90d2e8.png)

那该如何解决`AT`模式的脏写问题呢？ ---> 引入全局锁。

##### 17.3.5.2 `AT`模式的写隔离

**全局锁就是：`TC`记录当前正在操作某行数据的事务，该事务持有全局锁，具备执行权。`TC`通过一张表来记录。`xid[事务ID] table[哪张表] pk[哪一行的主键]`**，如图：

![img](https://img-blog.csdnimg.cn/f03c0bcdc9cc467d8ba7eb30270e7ca8.png)

引入全局锁就是在事务提交之前，先去拿全局锁，全局锁锁的是当前数据，即哪张表的哪个字段正在被使用，这就大大缩小了数据的范围，不像`DB`锁所得是数据库的表。在`XA`模式中，只有`DB`锁，而占有了`DB`锁，其它事务无法进行任何相关增删改查的操作。

拿到全局锁之后，事务二此时再想提交，提交前因为也要拿到全局锁，因为全局锁此时被事务一占用，所以无法提交，就会进入等待全局锁的状态。此时事务一拿到的是全局锁，事务二拿到的是`DB`锁。

假设一种最极端的情况，此时如果事务一需要进行恢复数据的操作，那么肯定就需要`DB`锁，但是`DB`锁被事务二拿到了，而事务二所需要的全局锁又在事务一那里，这就产生了死锁。

那问题就又成了如何转变为死锁问题，因为事务二是后面来的，所以如果出现死锁局面，会进行重试操作，默认重试`30次`,每次`10ms`也就是总尝试时长为`300ms`，`300ms`过后事务一获取到`DB`锁，然后根据快照恢复数据。

**<font color="red">为什么`AT`模式虽然也要有锁即全局锁性能还是比`XA`模式占有数据库锁的性能要高呢？</font>**

> 这是因为全局锁的范围跟`DB`锁的范围是不一样的，`DB`锁其实是被整个分支事务所持有的，锁的是整张表，而全局锁其实锁的是`TC`记录的当前正在操作某行数据，被正在操作某行数据的事务所持有，锁的对象不同一个是整张表一个是某一行，范围大大的缩小了。那么你就算正在操作该行，其它行也是可以正常被操作的。这就大大提高了性能。

`AT`模式的优点：

- 第一阶段执行`SQL`语句之后直接提交事务，不占用数据库锁即本地锁，释放了资源，性能较好
- 利用全局锁实现了写隔离
- 没有代码侵入就可以实现数据的提交或者恢复

`AT`模式的缺点：

- 无法保证强一致性，因为执行完`SQL`就提交了事务，需要恢复数据才会根据快照进行将数据恢复到原先的样子，所以就存在数据短暂的不一致问题，是弱一致性，最终一致性的。
- 在提交事务之前需要创建修改前后的数据快照而且后续还要进行删除，会影响性能，但是仍然比`XA`模式的性能高，因为`AT`模式使用的是全局锁，而`XA`模式使用的是本地锁。

`AT`模式的实现也是非常简单的，但是因为需要使用到全局锁，而全局锁的持有者即某个事务需要通过`TC`事务协调者将这一信息记录在表里，表示该事务持有全局锁，具有执行权。所以我们需要创建一张全局锁表：`lock_table`。

除此之外，因为在事务执行`SQL`之前还需要对数据进行快照，所以还需要创建一张表用于记录修改前后的数据。因而还要创建一张`undo_log`表。

**<font color="red">即总共需要创建两张表：`lock_table`和`undo_log`表。</font>**

1. 创建全局锁表`lock_table`和数据快照表`undo_log`，注意全局锁是交给`TC`来操作的，而`undo_log`是交给`RM`操作的，两张表存放的位置需要注意以下

   ```sql
   -- ----------------------------
   -- Table structure for lock_table
   -- ----------------------------
   DROP TABLE IF EXISTS `lock_table`;
   CREATE TABLE `lock_table`  (
     `row_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
     `xid` varchar(96) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `transaction_id` bigint(20) NULL DEFAULT NULL,
     `branch_id` bigint(20) NOT NULL,
     `resource_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `table_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `pk` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
     `gmt_create` datetime NULL DEFAULT NULL,
     `gmt_modified` datetime NULL DEFAULT NULL,
     PRIMARY KEY (`row_key`) USING BTREE,
     INDEX `idx_branch_id`(`branch_id`) USING BTREE
   ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
   ```

   ```sql
   -- ----------------------------
   -- Table structure for undo_log
   -- ----------------------------
   DROP TABLE IF EXISTS `undo_log`;
   CREATE TABLE `undo_log`  (
     `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
     `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
     `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
     `rollback_info` longblob NOT NULL COMMENT 'rollback info',
     `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
     `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
     `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
     UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
   ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Compact;
   ```

2. 然后修改配置文件`application.yml`将分布式解决方案更改为`AT`模式即可：默认就是`AT`模式

   ```yaml
   seata:
     data-source-proxy-mode: AT # 默认就是AT
   ```

3. 在整个业务的入口加入注解`@GlobalTransactional`：

   ```java
   @Override
   @GlobalTransactional
   public Long create(Order order) {
       // 创建订单
       orderMapper.insert(order);
       try {
           // 扣用户余额
           accountClient.deduct(order.getUserId(), order.getMoney());
           // 扣库存
           storageClient.deduct(order.getCommodityCode(), order.getCount());
       } catch (FeignException e) {
           log.error("下单失败，原因:{}", e.contentUTF8(), e);
           throw new RuntimeException(e.contentUTF8(), e);
       }
       return order.getId();
   }
   ```

4. 重启测试服务，故意错误访问http://localhost:8082/order?userId=user202103032042012&commodityCode=100202003032041&count=10&money=200，查看结果：

   重点看账户表，可以很清楚的看到执行了`SQL`语句，并且提交了然后才做的回滚处理。这就是`AT`模式。

   ![img](https://img-blog.csdnimg.cn/8a724b3e9a704d038f48f34298c59813.png)

#### 17.3.6 `TCC`模式的分布式解决方案

`TC`模式其实跟`AT`模式非常相似，每个阶段都是独立的事务，不同点在于，`AT`的数据恢复是根据`undo_log`表来完成，而`TCC`模式则是通过人工编码的方式来完成的。需要实现三个方法：

- `Try`：资源的检测和预留
- `Confirm`：完成资源操作业务，要求`Try`成功`Confirm`一定要能成功
- `Cancel`：预留资源释放，可以理解为`try`的反向操作

仍然是举个例子来说明`TCC`模式的原理：

现有一个扣减用户余额的业务，假设账户`A`原来的余额是`100`块钱，需要扣除`30`块钱

- `TCC`模式阶段一`Try`预留资源：检查余额是否充足，如果充足，则将冻结余额增加`30`块钱，将账户中的可用余额扣除`30`块钱。然后报告给TC`

  ![img](https://img-blog.csdnimg.cn/7fe5a0460fe94ac8ac58c541b0b14d1c.png)

  余额充足，可以冻结：此时**总金额 = 冻结余额 + 可用余额**

  ![img](https://img-blog.csdnimg.cn/3d271c8dd8e24d6f9319340db8ae7e2d.png)

- `TCC`模式阶段二之`Confirm`完成资源操作业务：如果要提交，就从冻结金额中扣除`30`，确认可以提交之后，因为之前扣除这项动作已经完成，所以直接清除冻结余额即可。

  ![img](https://img-blog.csdnimg.cn/44dbfe47a52d42919d023215555c0031.png)

  总金额 = 冻结金额 + 可用金额 = 0 + 70 = 70 元

- `TCC`模式阶段二之`Cancel`释放预留资源：如果需要回滚`Cancel`，则清除冻结余额`30`，可用余额增加`30`

  ![img](https://img-blog.csdnimg.cn/7fe5a0460fe94ac8ac58c541b0b14d1c.png)

`Seata`中的`TCC`模式仍然沿用的是`AT`模式的分布式解决方案架构，如图：

1. 首先`RM`会向`TC`注册事务然后进入`Try`将要操作的资源预留出来，然后随后向`TC`报告事务状态
2. 如果`TC`确认提交，则将让`RM`完成预留资源的业务操作即`Confirm`操作
3. 如果`TC`确认回滚，则让`RM`完成释放预留资源的操作即`Cancel`操作

![img](https://img-blog.csdnimg.cn/c129c0a02555452098dbc8fcd282a953.png)

总结：

1. `TCC`模式的每一个阶段分别是做什么的？
   - `Try`：对资源进行检查和预留
   - `Confirm`：完成预留资源的业务操作和提交
   - `Cancel`：释放预留资源
2. `TCC`的优点是什么：
   - `TCC`二阶段完成直接释放数据库资源，性能非常好
   - 相比于`AT`模式无需生成数据快照，也无需全局锁，性能很强
   - 不用依赖关系型数据库的事务，而是依赖于补偿操作，所以可以应用于非关系型数据库
3. `TCC`的缺点是什么：
   - 有代码侵入需要实现三个接口即`Try Confirm Cancel`非常的麻烦。
   - 存在软状态，允许短暂的数据不一致，不是强一致性的而是最终一致。
   - `Confirm`和`Cancel`虽然就是处理结果的，但是其本身也需要考虑失败的情况，需要做好幂等处理考虑健壮性。

再来看看这张图：

![img](https://img-blog.csdnimg.cn/b6129c670b2d49699a2d995af8f8fb7c.png)

并不是所有的分支事务都适合使用`TCC`模式的，比如这里的`order-service`只是需要增加，所以并不需要预留什么资源，最具有代表性的是这里的`account-service`所以这里只演示`account-service`，当然`storage-service`也是可以使用`TCC`模式的，不同的分布式解决方案是可以混用的。

需求如下：

- 修改`account-service`，编写实现`Try Confirm Cancel`三个接口的实现类
- `Try`实现类完成添加冻结余额，扣减可用余额的操作
- `Confirm`实现类完成清除冻结余额的操作
- `Cancel`实现类完成清除冻结余额恢复可用余额的操作

隐藏需求：

- 保证`Confirm Cancel`实现类的幂等性即健壮性，即每次处理的结果是一致的，不会因为多次请求而改变结果，需要判断是否已经执行过了

- 允许<font color="red">空回滚</font>

  空回滚就是因为在`Try`要执行`SQL`的时候被阻塞了即`Try`压根就还没执行，因为此，`TM`就会跟`TC`说出现了超时错误要求回滚全局事务，此时`TC`就下指令给各个分支事务要求回滚，但是那个没有执行`Try`阶段的分支事务压根就没有做预留资源等操作，直接进入到了`Cancel`阶段，此时当然不能报错，也不能真正地去做回滚因为根本无法做回滚，那要如何呢？就是做空回滚，返回一个正常或者说明信息即可。

- 避免<font color="red">业务悬挂</font>

  空回滚后，过了一会儿发现那个出错的事务突然好了，`Try`被执行了，但此时因为全局事务全部都回滚了，此时就永远无法执行`Confirm`跟`Cancel`了，所以这是一个需要避免的问题解决办法就是在空回滚之后阻止之后会执行`Try`操作，避免业务悬挂。

那我怎么直到我要做的是空回滚还是避免业务悬挂呢？只需要记录事务当前执行的状态即可。即为了实现空回滚、避免业务悬挂、实现幂等性等要求，所以我们必须在操作数据库`Try`冻结余额的同时，记录当前事务的`id`和执行状态，那么就需要用到一张表：

```sql
CREATE TABLE `account_freeze_tbl` (
  `xid` varchar(128) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `freeze_money` int(11) unsigned DEFAULT '0' COMMENT '冻结金额',
  `state` int(1) DEFAULT NULL COMMENT '事务状态，0:try，1:confirm，2:cancel',
  PRIMARY KEY (`xid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
```

那么：

1. 如何实现`Try`？
   - 在`account_freeze_tbl`记录冻结金额和事务状态
   - 修改`account`表， 扣除可用余额
2. 如何实现`Confirm`？
   - 根据`xid`即事务`id`删除`account_freeze_tbl`表的冻结余额
3. 如何实现`Cancel`？
   - 修改`account_freeze_tbl`的冻结余额，修改为`0`，事务状态进入`cancel`状态即`state = 2`
   - 修改`account`表，增加可用余额
4. 如何判断是否是空回滚？
   - 通过`xid`查询`account_freeze_tbl`，如果没有查询到任何事务即结果为`null`说明还没有做`Try`需要做空回滚
5. 如何避免业务悬挂？
   - 通过`xid`查询`account_freeze_tbl`，如果查询到状态为`cancel`即`state = 2`，则拒绝`try`业务执行

> `Seata`全局事务的`id`可以通过`RootContext.getXID();` 获取，
>
> 也可以通过`BusinessActionContext`参数的`getXid()`方法获取。

`TCC`的`Try、Confirm、Cancel`方法都需要在接口中基于注解来声明。

- 首先是接口上要用`@LocalTCC`
- 逻辑方法用注解`@TwoPhaseBusinessAction(name="try 方法名", commitMethod="confirm 方法名", rollbackMethod="cancel 方法名") `注明 
- 在该方法参数上加入 `@BusinessActionContextParameter(paramName="try 方法的参数")`，可以使得该参数传入`BusinessActionContext` 类，便于`confirm`和`cancel`读取。

声明三个接口实现`TCC`所需要的三个类，这里为了简便，直接转换为声明一个接口实现三个方法。

```java
package cn.itcast.account.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface AccountTCCService {
    @TwoPhaseBusinessAction(name = "deduct", commitMethod = "confirm", rollbackMethod = "cancel")
    public abstract void deduct(@BusinessActionContextParameter(paramName = "userId") String userId, @BusinessActionContextParameter(paramName = "money") int money);
    public abstract void confirm(BusinessActionContext businessActionContext);
    public abstract void cancel(BusinessActionContext businessActionContext);
}
```

编写实现类：

```java
package cn.itcast.account.tcc.impl;

import cn.itcast.account.entity.AccountFreeze;
import cn.itcast.account.mapper.AccountFreezeMapper;
import cn.itcast.account.mapper.AccountMapper;
import cn.itcast.account.tcc.AccountTCCService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountTCCServiceImpl implements AccountTCCService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountFreezeMapper accountFreezeMapper;

    @Override
    @Transactional
    public void deduct(String userId, int money) {
        //获取当前事务的 id
        String xid = RootContext.getXID();
        //避免业务悬挂，查询 account_freeze 表中，看有无该 xid 已经存在了，如果存在即不为 null 则表示这是一个空回滚不需要继续执行 Try 操作
        if (accountFreezeMapper.selectById(xid) != null) return;
        //1.扣除可用余额
        accountMapper.deduct(userId, money);
        //2.记录冻结余额
        AccountFreeze accountFreeze = new AccountFreeze();
        accountFreeze.setXid(xid);
        accountFreeze.setUserId(userId);
        accountFreeze.setFreezeMoney(money);
        accountFreeze.setState(AccountFreeze.State.TRY);
        accountFreezeMapper.insert(accountFreeze);
    }

    @Override
    public boolean confirm(BusinessActionContext businessActionContext) {
        //confirm 阶段就是删除掉冻结余额
        //获取事务 id
        String xid = businessActionContext.getXid();
        //删除该事务
        int count = accountFreezeMapper.deleteById(xid);
        //判断 confirm 是否执行正常
        return count == 1;
    }

    @Override
    public boolean cancel(BusinessActionContext businessActionContext) {
        String xid = businessActionContext.getXid();
        //恢复可用余额
        AccountFreeze accountFreeze = accountFreezeMapper.selectById(xid);
        //幂等性，如果为CANCEL表示已经做过了
        if (accountFreeze.getState() == AccountFreeze.State.CANCEL) return true;
        //如果这是一个超时的操作，TC 会让该事务直接进入 Cancel 段，所以需要先判断下是不是空的
        //RM 会在注册分支事务之前该分支就会被 TM 所调用，进行检查测试，所以如果查到的数据为 null 说明该分支事务被阻塞无法访问
        //此时直接进行空回滚
        if (accountFreeze == null) {
            accountFreeze = new AccountFreeze();
            String userId = businessActionContext.getActionContext("userId").toString();
            accountFreeze.setUserId(userId);
            accountFreeze.setFreezeMoney(0);
            accountFreeze.setState(AccountFreeze.State.CANCEL);
            accountFreeze.setXid(xid);
            accountFreezeMapper.insert(accountFreeze);
            return true;
        }
        accountMapper.refund(accountFreeze.getUserId(), accountFreeze.getFreezeMoney());//后台已经做好可用+冻结 = 恢复
        //修改冻结余额，更改状态为 Cancel
        accountFreeze.setFreezeMoney(0);
        accountFreeze.setState(AccountFreeze.State.CANCEL);
        int count = accountFreezeMapper.updateById(accountFreeze);
        return count == 1;
    }
}
```

更改调用的服务：

```java
package cn.itcast.account.web;

import cn.itcast.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 虎哥
 */
@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountTCCServiceImpl accountService;

    @PutMapping("/{userId}/{money}")
    public ResponseEntity<Void> deduct(@PathVariable("userId") String userId, @PathVariable("money") Integer money){
        accountService.deduct(userId, money);
        return ResponseEntity.noContent().build();
    }
}
```

`TCC`模式的代码是不需要去记忆的，根本上是要理解`TCC`模式，它是分两阶段执行`Try`跟`Confirm/Cancel`，然后为了实现空回滚，你需要在`Cancel`方法中写，而要实现防止业务悬挂需要在`Try`中写。而空回滚又是导致业务悬挂的原因，所以空回滚和防止业务悬挂的代码需要相辅相成写。

许多人觉得`TCC`复杂，我想是因为引入了额外的一张表而且还需要自己去管理，但是其实就是按逻辑写`CRUD`然后要注意状态的改变而已，没什么太难的地方。

访问http://localhost:8082/order?userId=user202103032042012&commodityCode=100202003032041&count=10&money=200进行测试：

![img](https://img-blog.csdnimg.cn/b497be4dadb24806998325804810c98d.png)

最后面的`Cancel`记录会存储在数据库中，因为每此的事务`id`是不同的，所以经常性地去清理记录即可。

#### 17.3.7 `SAGA`模式的分布式解决方案

`SAGA`模式指的是分布式事务执行过程中，依次执行各参与者的正向操作，如果所有正向操作均执行成功，那么分布式事务提交。如果任意一个正向操作执行失败，那么分布式事务会去退回去执行前面各参与者的逆向回滚操作，回滚已提交的参与者，使分布式事务回到初始状态。

![img](https://img-blog.csdnimg.cn/2a63a6880c434e46a88c537728b4b799.png)

提供的是一种长事务的解决方案，也分为两个阶段：

1. 第一阶段：直接提交事务
2. 第二阶段：成功则什么都不做，失败则通过编写补充业务来进行回滚

`Saga`模式的优点：

- 事务参与者可以基于事件驱动实现异步调用，吞吐高，并发能力强
- 第一阶段直接提交事务，无锁，性能好
- 不用编写`TCC`中的三个阶段，实现简单

`Saga`模式的缺点：

- 软状态持续时间不确定，时效性差
- 没有锁，没有事务隔离，会有脏写

再看看四种分布式解决方案的对比图：

![img](https://img-blog.csdnimg.cn/b6129c670b2d49699a2d995af8f8fb7c.png)

## 18.分布式缓存

### 18.1 `Redis`单节点的问题

单个`Redis`的问题：数据丢失问题、存储能力问题、并发能力问题、故障恢复问题

1. **<font color="red">数据丢失问题</font>**：`Redis`是基于内存存储的，服务重启可能会丢失数据
2. **<font color="red">并发能力问题</font>**：单节点的`Redis`虽然并发能力挺不错但是还是无法满足特定场景比如`618`
3. **<font color="red">故障恢复问题</font>**：单节点的`Redis`一旦不可用，对整个系统的影响是致命性的
4. **<font color="red">存储能力问题</font>**：`Redis`是基于内存存储的，单节点的无法满足海量存储的需求

![img](https://img-blog.csdnimg.cn/a3e33d8255e1484d89c404b85e91a548.png)

- 针对`Redis`是基于内存存储的，所以一断电服务器一重启内存中的数据就丢失这一问题即**数据丢失问题**，要想办法**实现`Redis`的持久化**。实现了持久化。
- 针对`Redis`单个节点的高并发能力虽然很不错了但是还不够强的问题即**并发能力问题**，可以**搭建`Redis`集群**。实现了高并发。
- 针对`Redis`单个节点一旦故障就会对整个系统造成致命性的打击即**故障恢复问题**，所以我们不仅要搭建`Redis`集群去解决并发能力的问题还要搭**建的是`Redis`主从集群**，并且**创建`Redis`哨兵去实现健康检测和自动恢复**。实现了高可用。

- 针对`Redis`是基于内存存储这一特点，而内存存储一定是有一个上限的，所以为了在这个上限之内做文章，可以学习`ElasticSearch`的做法，将数据进行分配，分别存储到集群不同的`Redis`当中即**搭建`Redis`分片集群，利用插槽机制实现动态扩容**。实现了高存储。

**<font color="deepskyblue">所以为了解决`Redis`单节点的数据丢失问题、存储能力问题、故障恢复问题、并发能力问题，我们要搭建一个主从+分片的`Redis`集群，利用插槽机制对存储容量进行扩容，并且创建`Redis`哨兵，对集群中的各个`Redis`节点进行健康检测以及自动恢复，且为了防止数据丢失，需要实现持久化。</font>**

### 18.2 `Redis`安装

这里演示的是`CentOS`安装`Redis`，当然你可以使用`Docker`部署`Redis`，这里正是选的`Docker`部署。方便又简单。顺带简单说下`Redis`常规安装：

```shell
yum install -y gcc tcl
tar -zxvf redis-6.2.4.tar.gz
cd redis-6.2.4
make && make install
---> 修改配置文件
# 绑定地址，默认是127.0.0.1，会导致只能在本地访问。修改为0.0.0.0则可以在任意IP访问
bind 0.0.0.0
# 数据库数量，设置为1
databases 1
---> 启动 Redis
redis-server redis.conf
---> 停止 Redis
redis-cli shutdown
```

`docker`：

```shell
docker pull redis:6.2.4
doicker run --name redis -p 6379:6379 -d redis:6.2.4

在/var/lib/docker/volumes/redis/redis.conf配置好配置文件、创建好文件夹然后再执行。可以使用xftp拉取配置文件。

docker run -p 6379:6379 --name redis -v /var/lib/docker/volumes/redis/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/data:/data -d redis:6.2.4 redis-server /etc/redis/redis.conf --appendonly yes[开启持久化]

连接 redis：docker exec -it redis redis-cli
```

### 18.3 `Redis`持久化

`Redis`实现持久化有两种方式：`RDB`模式的持久化 + `AOF`模式的持久化

#### 18.2.1 `RDB`模式持久化

`RDB`的全称为：`Redis Database Backup file`即`Redis`数据备份文件，也称做`Reids`数据快照。简单来说就是把内存中的所有数据都写入到到磁盘中，当`Redis`的实例发生故障而导致重启时，内存的数据虽然丢失了，但是可以通过读取写入到磁盘的数据备份文件即数据快照恢复数据。数据快照文件就是`RDB`文件，默认保存在`Redis`实例当前运行的目录。

1. `save`：如何生成数据备份文件即`RDB`文件呢，可以连上`redis-cli`后使用`save`命令。由于`Redis`是单线程的，所以使用`save`命令，主线程会来执行生成数据备份文件，从而阻塞了其余所有的命令，也就是说这个时候其它人无法对`Redis`进行读写操作。而且`RDB`的本质就是将数据写入到磁盘中，所以这个消耗的时间是比较长的。只有等`save`完毕后，等来一个`ok`主线程才会去执行其它的操作。适合在`Redis`不想继续执行选择宕机的时候执行。

   ![img](https://img-blog.csdnimg.cn/f77f0c54345d428da562e7c0c9bc1c17.png)

2. `bgsave`：后台方式的生成数据恢复文件，该方式不用主进程来完成，而是开启一个子进程来完成生成数据恢复文件的操作，避免主进程收到影响，适合在`Redis`还要正常运行的情况执行。

   ![img](https://img-blog.csdnimg.cn/ac01fa87b5544f33bc97a4cfbf679aa3.png)

   **<font color="red">为什么`bgsave`可以做到不影响主进程呢？因为`bgsave`的原理用到了`fork`技术。</font>**

   `bgsave`开始的时候会`fork`主进程得到子进程，子进程共享主进程的内存数据。完成`fork`之后读取内存数据然后写入`RDB`文件。子进程的操作几乎是零阻塞的，为什么说是几乎呢？因为子进程读取内存数据写入`RDB`文件确实是零阻塞的，但是从主进程那里`fork`出子进程这一过程主进程只可以干这一件事。

   - `fork`底层到底是如何实现的呢？简单说就是在`Linux`中主进程都是通过操作虚拟内存来操作真实的物理内存的，而虚拟内存跟物理内存的映射关系是通过一张映射关系表即页表来进行维护的，所以拷贝页表其实相当于就做了数据共享，因为能够操作的数据其所在真实物理内存位置是一样的。拷贝一张页表的速度肯定比直接拷贝数据的速度要快的多得多，这就是为什么`bgsave`可以比`save`快的原因。
     - 假设现在物理内存中有数据`A`跟数据`B`，在`Linux`系统当中所有的进程都没有办法直接操作物理内存，而是通过操作`Linux`操作系统给每一个进程分配的虚拟内存来操作真实的物理内存。也就是说主进程只能操作虚拟内存。
     - 然后`Linux`操作系统会维护一个虚拟内存跟物理内存之间的映射关系表，该表称之为页表。
     - 所以，主进程操作虚拟内存，操作系统通过映射关系表即页表在物理内存中到找到真正的存储位置。这样一来，主进程就能对真实的物理内存进行读写了。
     - `fork`时，主进程会创建一个子进程，`fork`的过程不是将内存数据做拷贝，而是将映射关系表即页表做拷贝，拷贝给子进程。所以当子进程在操作自己的虚拟内存时，因为页表跟主进程是一样的，所以操作的真实物理内存也是一样的。这样就实现了子进程和主进程的内存共享，而无需花费大量的时间去直接拷贝内存数据，这就是`bgsave`和`save`的区别。
     - 然后子进程就会通过操作虚拟内存从而获取到真实物理内存中的数据进而将数据往磁盘写入新的`RDB`文件，替换旧的`RDB`文件。是写好新的以后拿新的去替换，而不是修改旧的`RDB`文件。

   - **<font color="red">这时候就会有另外一个问题冒出来了：因为主进程跟子进程是异步关系，那如果此时主进程写入跟子进程读取是同一个数据，不会造成脏写脏读问题吗？为什么不会呢？</font>**

     - 原来子进程采用的是**`copy-on-write`技术**，`copy-on-write`技术是啥么呢？就是子进程会将当前页表中对应的物理内存数据设定成`read-only`也就是这些数据只读，任何一个进程想要修改都不能直接修改。

     - 当主进程它真的有需要要写入更新数据的时候，那主进程必须先拷贝一份这个要操作的数据，操作时会对这个拷贝出来的数据即该数据的副本进行修改。
     - 然后页表即虚拟内存跟物理内存的映射关系表会发生变化，当主进程去读取该数据的时候，读取的是或者说操作系统通过页表找的是该数据的副本。这样就避免了脏写问题。

   - **<font color="red">但是这样做，理论上可能会出现这么一种极端的情况：内存溢出问题</font>**

     - 即当子进程在写入`RDB`文件的时候，物理内存中的所有数据都被操作修改了一遍，那就意味着物理内存中所有的数据都需要被拷贝一遍，这样数据占用的内存空间就成倍增加了。假设原先的数据占用了`16g`的内存空间。那就意味着现在需要`32g`的内存空间才能存储原数据跟副本数据。
     - 这种极端情况虽然极其少见，但是你也需要预防，要不内存崩了咋办？所以呢`Redis`通常会**预留一些内存空间**，不会把所有的内存全部交给`Redis`导致内存溢出。

   ![img](https://img-blog.csdnimg.cn/eee866a690ee43778e43bdd98a268907.png)

3. 每次停止`Redis`服务的时候会自动进行生成数据恢复文件，即自动执行一次`save`命令

   - 使用`docker exec -it redis-cli shutdown`停止`redis`

   - 使用`docker logs -f redis-cli`查看日志可以看到在停止`redis`的时候，`redis`做了一次`save`操作，创建了数据恢复文件。

     ![img](https://img-blog.csdnimg.cn/beb4f3d34b1b49ceaa9eeebc20716cfb.png)

   - 因为这里做了数据卷，所以可以在宿主机器即我的`CentOS`挂载的数据卷看到生成的数据恢复文件：

     ![img](https://img-blog.csdnimg.cn/ac8af152bd4641418ed9f4f4c84883fb.png) 

4. `Redis`可以设置每隔一个时间就自动做持久化操作，可以在配置文件`redis.conf`中找到

   ![img](https://img-blog.csdnimg.cn/fec1e610eae249b8a2a575a6bbaf2ad8.png)

   **<font color="red">注：如果使用的是`save ""`则表示禁用`RDB`机制</font>**

   - `save 3600 1`表示在`3600s`之内至少有`1`个`key`修改就执行**`bgsave`**
   - `save 300 100`表示在`300s`之内至少有`100`个`key`修改就执行**`bgsave`**
   - `save 60 1000`表示在`60s`之内至少有`1000`个`key`修改就执行**`bgsave`**

   还有一些额外的设置：

   ```shell
   # 是否压缩，不建议开启，因为压缩需要消耗内存资源，虽然数据恢复文件变小了，但是消耗大量的内存资源，并不值得
   rdbcompression yes
   # RDB 文件名称，默认叫做 dump.rdb
   dbfilename dump.rdb
   # RDB 保存的目录
   dir ./
   ```

通过`bgsave`我们也了解到了`RDB`模式的一些底层实现细节，我们可以总结以下：

1. **`RDB`方式的`bgsave`基本流程是什么？**

   - 主进程`fork`出子进程，子进程拷贝主进程页表，读取共享内存数据写入磁盘`RDB`文件，写完以后会将新的`RDB`文件替换掉旧的`RDB`文件。
   - 内存数据因子进程在做`RDB`，所以数据是只读的即`read-only`采用`copy on write`当主进程要写入时操作的是复制出来的数据。
   - `Redis`会预留内存空间，防止复制过多数据导致内存溢出。

2. **`RDB`会在什么时候执行？`save 60 1000`代表什么含义？**

   - `RDB`会在手动执行`save bgsvae`指令以及`Redis`服务被停止时，以及出发了`save`操作的时候就会执行。
   - `save 60 1000`代表在`60s`内至少有`1000`个数据被修改过就会执行`bgsave`操作生成新的`RDB`文件替换旧的。

3. **`RDB`有什么缺点呢？**

   - 缺点就是可能出现数据丢失问题。某次操作的`save/bgsave`跟下一次`save/bgsave`存在一个时间差，这个时间段里面的数据可能会因为突然出现的故障而没有被及时的保存下来，造成数据丢失。

   - 还有就是耗时问题。

     1. 主进程`fork`出子进程这一过程是比较耗时的。
     2. 除此之外，可能因为数据量比较大，子进程或者主进程将数据写入`RDB`文件耗时是比较长的。
     3. 再加上可能设置了压缩操作，压缩`RDB`文件也是需要耗时的。

     所以总的来说`RDB`的速度是比较慢的，耗时是比较长的。万一耗时的过程出现了故障，就很难处理。所以我们当然想要一种可以很快保存数据的工具/模式。

#### 18.2.2 `AOF`模式持久化

`AOF`全称为：`Append Only File`追加文件，`Redis`处理的所有写操作记录到`AOF`文件当中，是一个逐渐累加的过程。所以你完全可以将`AOF`看作是一个纯记录写命令的操作。

这里的`$3`的意思是记录命令包含的字符的长度，表示长度为`3`。恢复时将`AOF`文件中操作执行一遍就可以了。

![img](https://img-blog.csdnimg.cn/20287e1ff6474f2e9e2b0db8f59fb58e.png)

`AOF`模式的持久化默认是关闭的，如果需要开启需要到`redis.conf`中进行修改，将`append-only`打开设置为`yes`然后配置要添加的`AOF`文件名称：【注：使用`save ""`可以将`RDB`持久化禁用】

![img](https://img-blog.csdnimg.cn/3b1718eb241c437aa92a81ed40ad5029.png)

`AOF`记录操作的频率也可以通过`redis.conf`配置文件进行配置：`appendsync always`、`appendsync everysec`、`appendsync no`。

默认为`appendsync everysec`先将写入操作记录到`AOF`缓冲区中，每隔`1s`将`AOF`缓冲区的数据写入到`AOF`文件，这种方案主进程只关心写入到缓冲区，是内存层面的操作，速度非常快。但是也有一定的问题就是可能这`1s`内的数据可能因为故障丢失了，牺牲了一定的可靠性。但是实现了更高的性能。这是可以被接受的。

如果使用`appendsync no`还不如使用`rdb`，虽然性能很好但是安全性非常差，可能丢失大量的数据。

![img](https://img-blog.csdnimg.cn/e4afc8e8789a44cda89ff2b31c6e8757.png)

三种策略即`appendsync always/everysec/no`的比较：

![img](https://img-blog.csdnimg.cn/565acd39c3f640cc86d0f6c7b8b10eca.png)

同时开启`RDB`和`AOF`，当`Redis`重启时会优先加载`AOF`来恢复数据，因为通常情况下`AOF`文件保存的数据会比`RDB`保存的数据要完整。

使用`AOF`：可以看到操作被记录下来了

![img](https://img-blog.csdnimg.cn/fe3c3f8a1c5f44b9bf8adfdfe948aa47.png)

执行`docker stop redis`，查看可以发现没有生成`RDB`文件：说明`RDB`禁用成功。

![img](https://img-blog.csdnimg.cn/0935bd57b611444ab9ac01662de0aa56.png)

重启`redis-server`会如何呢？会去读取`AOF`文件，如何确认这一步骤？可以修改下`AOF`文件中的数据：

![img](https://img-blog.csdnimg.cn/09e9b43895f84f87807591848cb5d6b7.png)

重启时如果有`k1 num`说明确实读取了`AOF`文件：可以发现确实有。

![img](https://img-blog.csdnimg.cn/7d5d9a4ca1194f3e9723e6d7d02e1e4b.png)

因为`AOF`会记录每一个操作，肯定会记录许多多余的操作，因为会对相同的一个`key`记录多次写操作，但其实只有最后一次写操作才有意义，所以其文件大小肯定要比`RDB`要大。为了让`AOF`更高效地执行，应该让它执行重写功能即`bgrewriteaof`，用最少地命令达到相同地效果。执行`bgrewriteaof`即可。

![img](https://img-blog.csdnimg.cn/20a20b4bbc234864a5cc0cabc6c57e77.png)

![img](https://img-blog.csdnimg.cn/de4ad208278c46178089ec14f4f420c0.png)

体积变小，对命令进行了编码操作，最小的命令达到了相同效果。

因为执行`bgrewriteaof`需要消耗大量的`CPU`和内存资源，所以不用经常执行重写命令，那么什么时候去执行`bgrewriteaof`命令比较合理呢？可以通过`redis-conf`进行配置：

![img](https://img-blog.csdnimg.cn/69b94a8c33f841ffa701c17c9b2935a5.png)

#### 18.2.3 `RDB`和`AOF`的对比

![img](https://img-blog.csdnimg.cn/adbea15f61b6498482994acf0db83a1f.png)

再次声明：`Redis`支持同时开启`RDB`和`AOF`，在这种情况下当`Redis`重启的时候会优先载入`AOF`文件来恢复原始的数据，因为在通常情况下`AOF`文件保存的数据集要比`RDB`文件保存的数据集完整。

`Redis`的新版本是有计划将这两种持久化模式进行整合合二为一的。

### 18.4 `Redis`主从复制

#### 18.4.1 为什么需要主从复制？

单节点的`Redis`虽然并发能力不错但是遇到特殊场景比如`618`很大可能还是顶不住的，所以为了进一步提高`Redis`的并发能力，需要搭建集群。

一般来说，`Redis`的读占大多数，而写通常比读少得多，所以一般我们会采用主从复制的方式实现读写分离。主节点`Redis`负责写，然后通过复制快照和向缓冲区`repl_baklog`写入命令的方式，将其复制给各个从节点。这样做能把大量的`CPU`跟内存资源交给读操作。

#### 18.4.2 搭建`Redis`主从复制

![img](https://img-blog.csdnimg.cn/3e411d3a12ce4df1a0a28d16de5790aa.png)

了解了为什么需要搭建主从复制的集群（为了实现高可用高并发，实现读写分离），首先得学习下如何搭建主从复制的`Redis`集群。

我们演示下一主二仆的这种架构，当然你也可以根据需求搭建更多的从节点，一主二仆的方式在这里完全足够演示学习了。知道怎么搭建就行。

里我们会在同一台虚拟机中开启`3`个`redis`实例，模拟主从集群，信息如下：这里做了`NAT`地址转换。

|      `IP`      | `PORT` |   角色   |
| :------------: | :----: | :------: |
| `192.168.56.1` | `7001` | `master` |
| `192.168.56.1` | `7002` | `slave`  |
| `192.168.56.1` | `7003` | `slave`  |

要想搭建三个`Redis`实例，那么它们三个的配置文件必然得不相同，而且得在不同的配置文件和目录中，配置文件所在的目录就是不同`Redis`节点的工作目录。

创建目录，因为这里使用的是`Docker`天然就有容器，所以无需创建，但是还是贴出虚拟机下的工作指令

```shell
# 进入/tmp目录
cd /tmp
# 创建目录
mkdir 7001 7002 7003
```

![img](https://img-blog.csdnimg.cn/71bf6575b4554c0e8670e0614e06c665.png)

使用`docker`：

```shell
cd /var/lib/docker/volumes/redis
mkdir 7001
mkdir 7002
mkdir 7003
cd 7001
vim redis.conf :wq
mkdir data
cp -r data ../7002/.
cp -r data ../7003/.
cp redis.conf ../7002/.
cp redis.conf ../7003/.
```

```shell
docker run -p 7001:7001 --name redis7001 -v /var/lib/docker/volumes/redis/7001/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7001/data:/data -d redis:6.2.4 redis-server /etc/redis/redis.conf

docker run -p 7002:7002 --name redis7002 -v /var/lib/docker/volumes/redis/7002/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7002/data:/data -d redis:6.2.4 redis-server /etc/redis/redis.conf

docker run -p 7003:7003 --name redis7003 -v /var/lib/docker/volumes/redis/7003/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7003/data:/data -d redis:6.2.4 redis-server /etc/redis/redis.conf

docker ps
```

![img](https://img-blog.csdnimg.cn/6f2e19aaaf494d73ba4030a435e31666.png)

将之前`6379`的`Redis`的配置文件`redis.conf`复制给`7001 7002 7003`，记得更改下端口，以及把`bind`改为`bind 0.0.0`开启`save`自动触发，关闭`AOF`持久化。

```shell
bind 0.0.0.0
port 7001/7002/7003
save 3600 1...
appendonly no

cd ..
cp redis.conf 7001/.
cp redis.conf 7002/.
cp redis.conf 7003/.

# 方式一：逐个拷贝
cp redis-6.2.4/redis.conf 7001
cp redis-6.2.4/redis.conf 7002
cp redis-6.2.4/redis.conf 7003
# 方式二：管道组合命令，一键拷贝
echo 7001 7002 7003 | xargs -t -n 1 cp redis-6.2.4/redis.conf
```

因为这里是`NAT`地址转换的方式，所以直接访问：`192.168.56.1:7001/7002/7003`这种方式，就可以了。而如果是虚拟机，因为虚拟机本身有多个`ip`所以可以绑定一个实例`ip`。例如下列绑定：

```shell
# 逐一执行
sed -i '1a replica-announce-ip 192.168.150.101' 7001/redis.conf
sed -i '1a replica-announce-ip 192.168.150.101' 7002/redis.conf
sed -i '1a replica-announce-ip 192.168.150.101' 7003/redis.conf

# 或者一键修改
printf '%s\n' 7001 7002 7003 | xargs -I{} -t sed -i '1a replica-announce-ip 192.168.150.101' {}/redis.conf
```

重启`redis-server`：

```shell
docker restart redis7001
docker restart redis7002
docker restart redis7003
```

如果是虚拟机的启动可以这样启动：

```shell
# 第1个
redis-server 7001/redis.conf
# 第2个
redis-server 7002/redis.conf
# 第3个
redis-server 7003/redis.conf
```

`XShell`打开三个终端进行监听：

```shell
docker logs -f redis7001
docker logs -f redis7002
docker logs -f redis7003
```

要想停止某个服务可以进行如下操作：

```shell
docker exec -it redis7001 redis-cli shutdown
docker exec -it redis7002 redis-cli shutdown
docker exec -it redis7003 redis-cli shutdown
```

上述就完成了初步搭建，接下来就是开启主从关系：

现在三个实例还没有任何关系，要配置主从可以使用`replicaof`或者`slaveof`（`5.0`以前）命令。

有临时和永久两种模式：

- 修改配置文件（永久生效）

  - 在`redis.conf`中添加一行配置：`slaveof <masterip> <masterport>`

- 使用`redis-cli`客户端连接到`redis`服务，执行`replicaof`命令（重启后失效）：

  - ```shell
    replicaof <masterip> <masterport>
    ```

<strong><font color='red'>注意</font></strong>：在`5.0`以后新增命令`replicaof`，与`salveof`效果一致。

这里方式二比较简单，我们使用方式一进行一个演示：

```shell
docker inspect redis7001 获取ip:172.17.0.6 7001
cd 7002
vim redis.conf :?replicaof
添加：replicaof 172.17.0.6 7001

cd 7003
vim redis.conf :?replicaof
添加：replicaof 172.17.0.6 7001

重启 7002 7003 的 Redis 服务
docker restart redis7001
docker restart redis7002
```

可以主从关系已经成功搭建好：

![img](https://img-blog.csdnimg.cn/4e4091e09f89412d9e544a5a5fd9f755.png)

可以进一步尝试，当我们尝试向`redis7002`或者`redis7003`写入数据时，会发现写不进去：`7002 7003`显示只读，只有主节点`Redis7001`才可以写数据。

![img](https://img-blog.csdnimg.cn/8053ff63bd8f4aeabc09c7b1a4b81c15.png)

以上就完成了主从复制的集群搭建。

#### 18.4.3 `Redis`主节点是如何将数据复制给从节点的？即如何实现的数据同步？

##### 18.4.3.1 全量同步

- `Redis`主节点和从节点间的第一次同步，称之为**<font color="red">全量同步</font>**，因为会发送整个备份数据给从节点即发送`RDB`文件。当`Redis`的主节点和从节点第一次建立连接的时候，就会执行全量同步，会将`master`即主节点中所有的数据全部拷贝给`salve`节点即从节点。流程如下：

  ![img](https://img-blog.csdnimg.cn/5ed7ee07ba2e442e8434c2b043973d77.png)

  **第一阶段：**

  1. 首先执行命令：`replicaof <masterip> <masterport>`
  2. 然后`slave`节点即从节点会向主节点`master`请求数据同步
  3. `master`会判断这是不是第一次数据同步
  4. 若判断这是第一次数据同步，就返回`master`的数据库版本信息，将来可以基于数据库版本做一个控制
  5. `slave`保存`master`的数据库版本信息

  **第二阶段：**

  1. `master`主节点执行`bgsave`通过`fork`出子进程让子进程生成数据恢复文件`RDB`，并且子进程在生成`RDB`这个期间，主进程没有闲着，它会把在这期间用户执行的命令全部存储到`repl_backlog`中
  2. 将子进程生成的`RDB`文件发送给从节点`slave`
  3. 从节点`slave`清空自己的本地数据，加载从主节点`master`发送过来的`RDB`文件

  **第三阶段：**

  1. 主节点`master`发送存储在`repl_baklog`中的命令给`slave`从节点
  2. 从节点执行接收到的命令

全量同步是需要生成`RDB`文件的，我们只要生成`RDB`文件是非常消耗资源的，所以只有在第一次同步数据的时候才会使用，既然如此，判断是不是第一次数据同步就变得相当重要了。

**<font color="red">那么主节点`master`如何判断数据是不是第一次同步呢？</font>**

- **`Replication Id`**:`replication`是复制的意思，简称`replid`，是数据集的标记，`id`一致表示是同一数据集。每一个`master`都有一个唯一的`replid`，`salve`会继承`master`节点的`id`。也就是当`slave`从节点第一次执行`replicaof <masterip> <masterport>`然后向`master`主节点请求数据同步后会发送自身的`replicationid`和偏移量`offset`，`master`不是会进行一次判断是否是第一次数据同步吗？就是根据`replicationid`来判断的，如果你是携带的`replid`跟我的`replid`是一样的，那说明你不是第一次来请求数据同步了，如果你是第一次来的，`master`就会发送`replid offset`以及数据库版本信息等返回给`slave`从节点。

**<font color="red">从节点`slave`如何知道下一次是什么时候去同步呢？</font>**

- **`offset`**：偏移量，随着记录在`repl_baklog`中的命令增多`offset`会不断增大。当`salve`从节点完成数据同步即完成加载`repl_baklog`中的命令时，也会响应的记录当前同步的偏移量`offset`，如果`slave`从节点的偏移量`offset`小于主节点`master`的偏移量时，就说明从节点`slave`目前的数据已经落后于`master`主节点的数据了，此时就需要向主节点请求数据同步了。

所以当`slave`请求数据同步的时候，肯定需要向`master`主节点声明自己的复制`id`即`replicationid[repliid]`和偏移量`offset`，`master`主节点才知道是不是第一次同步数据以及要同步哪些数据给从节点`slave`。

当从节点启动服务时，如果此时就在配置文件中设定了主从复制即`replicaof <masterip> <masterport>`或者在启动的时候设定了命令，那么就会在启动后自动的做一次请求数据同步的操作：这里的`sync`代表的就是向主节点`master`发送请求数据同步。

![img](https://img-blog.csdnimg.cn/4e4091e09f89412d9e544a5a5fd9f755.png)

而`master`主节点这边接收到了请求，就会判断从节点发送的`replicationid`跟我自身的`replicationid`是否一致，如果不一致，就会返还数据库版本以及`replicationid`以及`offset`给`slave`从节点。然后开始进行`bgsave`：

![img](https://img-blog.csdnimg.cn/53d35a88f11a4a35a1cc95095d011fdf.png)

接着从节点就会开始做全量同步：

![img](https://img-blog.csdnimg.cn/8561345d7a0d4d1b88003589721ec797.png)

接收主节点`master`发送过来的`RDB`然后清空自己的旧数据，加载`RDB`文件。

![img](https://img-blog.csdnimg.cn/8a903d283de34c0998c395948117dcd8.png)

完整流程描述：

- `slave` 节点请求增量同步
- `master` 节点判断`replid`，发现不一致，拒绝增量同步，选择全量同步
- `master` 将完整内存数据生成`RDB`，发送`RDB`给`slave`
- `slave` 清空本地数据，加载`master`的`RDB`
- `master` 将`RDB`期间的命令记录在`repl_baklog`，并持续将`repl_backlog`中的命令发送给`slave`
- `slave`执行接收到的命令，保持与`master`之间的同步

##### 18.4.3.2 增量同步

主从第二次同步以及后面都是**<font color="red">增量同步</font>**：

全量同步需要先做`RDB`，然后将`RDB`文件通过网络传输给`slave`从节点，成本太高。因此除了第一次做全量同步，其它大多数时候`slave`与`master`都是做**增量同步**。

什么是增量同步？就是只更新`slave`与`master`存在差异的部分数据。而怎么知道数据差异在哪里则根据偏移量`offset`。

![img](https://img-blog.csdnimg.cn/70e44b59c88d433e9e6708e289b24d20.png)

`repl_backlog`的原理：

`repl_backlog`该文件是一个固定大小的数组，只不过数组是环形的。也就是说当下标到达数组末尾后，会再次从`0`开始读写，这样数组头部的数据就会被覆盖，`repl_backlog`会记录`Redis`处理过的命令日志以及偏移量`offset`，包括`master`当前的`offset`以及从节点`slave`已经拷贝的`offset`都会记录。

![img](https://img-blog.csdnimg.cn/81bc200c0d534027b0a5140822a45426.png)

`slave`和`master`之间的偏移量差异，就是`slave`需要进行增量同步的数据了。随着不断有新的命令写入，`master`的偏移量`offset`肯定会越变越大，`slave`也会不断地追赶`master`的偏移量，知道数组被填满为止。

![img](https://img-blog.csdnimg.cn/9e265bc8b1ed4c75aa717c163648dea8.png)

当数组填满时或者更准确的此时`master`将`repl_backlog`的最后一个下标填充了，当此时又有新的命令写进来，那就会覆盖最开始的旧数据，为什么这些数据可以被覆盖呢？就不怕这些数据没有拷贝到`slave`从节点丢失吗？完全不用担心，因为我们前面说过`slave`从节点会不断追赶`master`的`offset`偏移量，所以只要跟图片那样是绿环说明数据已经被从节点同步了，那这个数据就没有在`repl_backlog`存在的必要的，这样就不必浪费资源了。这是一个动态追赶的过程。

上述所说的是理想情况，设想一下，当主节点`master`一直有新命令写入，那么相应地也会去向`repl_backlog`写入新的命令，假设现在`slave`从节点阻塞了，那就表明`master`虽然一直有向`repl_backlog`写入新的数据，但是因为`slave`阻塞了，导致`slave offset`的偏移量`offset`保持不动。

![img](https://img-blog.csdnimg.cn/b5651472d2c647df9c30f6d1b4fb1f63.png)

所以总会有那么一个极端的时刻，就是`master`的`offset`将整个`repl_backlog`都填满了，此时`offset`回到了原点即`0`但是此时的`slave offset`肯定是要大于`master offset = 0`的，此时`slave`恢复正常，`master`要同步数据已经无法做增量同步，因为自身偏移量`master.offset < slave.offset`出现这种情况只可能是因为`slave`阻塞导致之前没有及时跟上做增量同步，因为总有新的命令写进来，就算`master.offset = 0`了还是会写进来，**此时已经无法完成增量同步了，只能做全量同步**。

![img](https://img-blog.csdnimg.cn/96f5a4ceddd7411d8d78cc01a4f54775.png)

总结一下就是：**<font color="red">`repl_backlog`大小是有上限的，因为这是一个数组而且不会做自动扩容。写满后就会覆盖最早已经被从节点`slave`同步过的数据，如果从节点`slave`断开时间太久，导致尚未进行同步的数据被覆盖了，那么`slave`就无法基于`repl_backlog`做增量同步了，只能再次做全量同步。</font>**

#### 18.4.4 主从复制的优化

主从复制可以保证主节点`master`和从节点`slave`的数据一致性，所以非常重要，而且这样做还能做到读写分离，并发能力和可用性都大大提高了。但其实这种主从模式的集群还可以优化得更好：

- 在`master`中配置 `repl-diskless-sync yes` 启用无磁盘复制，避免全量同步时的磁盘`IO`。

  本来是先向磁盘写入文件然后通过网络将文件传输给从节点，无磁盘复制就是直接通过网络将数据传输给从节点，无需磁盘写入，可以大大提高速度，尤其是在数据量很大的时候。【适用于磁盘`IO`比较慢网络速度快的时候】

- 控制`Redis`单节点的内存上限，使其不要太大，从而减少`RDB`导致的过多磁盘`IO`

上述是通过加快速度，这并不是最好的办法，最好的办法其实是尽可能地减少全量同步，尽量发生多一些地增量同步：

- 我们知道增量同步需要使用到`repl_backlog`，而`repl_backlog`有大小，所以适当提高`repl_backlog`的大小，发现`slave`宕机时尽快实现故障恢复，尽可能避免全量同步

- 限制一个`master`上的`slave`节点数量，因为每一个从节点刚上线的时候都会去做一次全量同步。从节点一多，内存压力就大了。如果实在是太多`slave`，则可以采用**主-从-从**链式结构，让从节点从上一个从节点拷贝数据，减少`master`主节点压力。

  ![img](https://img-blog.csdnimg.cn/082f6b3cd939421493c336c66c1fb999.png)

#### 18.4.5 主从复制总结

简述全量同步和增量同步区别？

- 全量同步：`master`将完整内存数据生成`RDB`，发送`RDB`到`slave`。后续命令则记录在`repl_baklog`，通过缓冲区发送给`slave`。
- 增量同步：`slave`在发送数据同步请求时会发送自己 的`offset`给`master`，`master`获取`repl_backlog`中从`offset`之后的命令发送给`slave`。

什么时候执行全量同步？

- `slave`从节点第一次连接`master`主节点时
- `slave`节点断开时间太久，`repl_backlog`中的`offset`已经被覆盖导致新的命令覆盖掉了之前的新命令

什么时候执行增量同步？

- `slave`从节点不是第一次向主节点`master`发送数据同步请求时

- `slave`从节点断开又恢复，并且在`repl_baklog`中能找到`offset`时

### 18.5 `Redis`哨兵

#### 18.5.1 为什么要有哨兵？

有没有想过：**`slave`从节点挂了宕机了恢复后可以向`master`主节点同步数据，那如果`master`主节点宕机了恢复了可以找持久化数据，但是宕机的这段时间数据怎么办呢？**

**所以需要监控`master`主节点，一旦它挂掉了，就立马让一个`slave`替代掉`master`主节点。这个工作就是`Redis`哨兵的工作。**哨兵的英文名为：**`Sentinel`**

这也就说明了为什么需要哨兵：监控节点、从节点升级为主节点实现自动恢复。

#### 18.5.2 哨兵的作用

`Redis`提供了哨兵`Sentinel`机制来实现主从集群的自动故障恢复，哨兵的结构如下：

![img](https://img-blog.csdnimg.cn/f193fce31549472a94fd7225c671a1d8.png)

可以看到哨兵本身也是需要做集群的，哨兵的作用如下：**监控节点 + 故障恢复**

1. **监控节点**：`Sentinel`哨兵会不断地检查`master`主节点和`slave`从节点是否按预期正常工作。

   哨兵`Sentinel`是如何监控节点，获取节点当前的服务状态呢？其实，`Sentinel`是**基于心跳机制**从而检测`Redis`集群中各个节点的服务状态的，哨兵`Sentinel`每隔**`1s`**就会向每一个实例发送`ping`命令。

   - **<font color="red">主观下线</font>**：如果`Sentinel`哨兵发送了`ping`命令给某节点，该节点没有在规定时间内相同，那么哨兵`Sentinel`会认为该节点已经下线即主观下线。[有可能是因为网络阻塞，所以说是主观察]

   - **<font color="red">客观下线</font>**：如果超过指定数量`quorum`的`Sentinel`都认为该节点服务状态为不健康，认为该节点已经主观下线，那么该实例则数据客观下线。`quorum`的值最好超过`Sentinel`哨兵集群总数量的一半。[有可能还是因为网络阻塞，但大多数哨兵都如此认为，所以说是客观]

     ![img](https://img-blog.csdnimg.cn/9828106cf4c04d41be0be891b123f4cd.png)

     比如这里有三个哨兵，有两个哨兵认为主节点`master`服务状态为不健康即`quorum`的值超过了总哨兵数量的`50%`，那么此时就从主观下线成了客观下线。

2. **故障恢复**：如果确定`master`发生故障，哨兵`Sentinel`会立即将一个`slave`从节点提升为`master`主节点，此时就算原先的主节点恢复健康也只能当从节点，而以被提升的主节点为主节点。

   **<font color="deepskyblue">那么存在多个从节点`salve`我们该如何选择呢即选择依据是什么呢？</font>**

   1. 首先会判断`slave`从节点跟`master`节点**<font color="red">断开时间的长短</font>**，时间越长证明数据丢失的可能就越多，就越不能选择该从节点，如果该断开时间超过了指定值`down-after-millseconds * 10`则会排除该`slave`节点。
   2. 此时排除了多个节点之后剩下一些没有超过指定时间`down-after-millseconds * 10`的从节点，此时就会按照从节点`slave`的`slave-priority`从**<font color="red">节点优先级</font>**来判断，`slave-priority`的值越小表示优先级越高，如果`slave-priority = 0`则表示用不参加主节点的选举（还挺有个性）。新版本中叫做`replica-priority`默认值为`100`，如果没有特定去配，每个从节点的`replica-priority`优先级都是一样的。
   3. 如果`slave-priority`都一样呢，则判断`slave`从节点的`offset`**<font color="red">偏移量</font>**，`offset`越靠近`master`的`offset`表明该从节点获取到数据越新，此时优先级会再往上升一层次。
   4. 最后从不仅没有超过指定断开时长`down-after-millseconds * 10`，而且`slave-priority`都很小且一样，并且`offset`偏移量都一样，跟`master.offset`的距离相差最小，这种情况该怎么办呢？此时就要论先来后到了，生小孩总是先出生的大，老大排`1`，老二排`2`以此类推，所以判断此时可选的`slave`从节点谁的**<font color="red">`id`</font>**最小，谁最小证明谁越早出现【这个只是方便记忆，可能并不是如此】，此时选择`id`最小的，它的优先级最大。

   **<font color="deepskyblue">选择了一个从节点作为`master`之后具体该如何将其升级为主节点呢？</font>**

   1. 哨兵`sentinel`将给备选节点即要升为主节点的从节点发送`slaveof no one`意思就是不要再当奴隶了！的意思，此时该备选节点就升级成为了`master`，新的主节点诞生了！！！

   2. 然后哨兵`sentinel`会给其它所有的从节点发送`slaveof 192.168.150.101 7002`【假设选择的备选节点的`ip`和`port`是这个】的命令，让其它从节点将自己的主人设置为该新的主节点`master`。

   3. 最后，哨兵`sentinel`将故障的旧的主节点标记为`slave`，防止日后该旧的主节点跟现任新主节点起冲突造成矛盾，标记为`slave`之后会在恢复健康之后，称为新的主节点`master`的从节点，开始从`master`主节点同步数据。

      【**<font color="red">果然是枪杆子里出政权！！！</font>**，哨兵`sentinel`代表的就是军队，主节点虽然叫主节点，其实还是受`sentinel`的管控，无论主节点还是从节点都看起来不过是哨兵`sentinel`集群的棋子，而哨兵的幕后黑手就是操纵整个`Redis`的人！！！就是你！！！】

      ![img](https://img-blog.csdnimg.cn/6b66f1c7601d40e3ac5dbbcdb039212a.png)

3. **通知功能**：充当服务来源，比如`Java`客户端，由于主从节点可能不一定一直都是同一个，因为存在故障恢复，所以从节点也有可能变成主节点，所以我们无法以硬编码写死的方式获取到主节点的地址。这时候就用到哨兵了。当主从发生改变，哨兵`Sentinel`会充当`Java`客户端的服务来源，会将最新的消息推送给`Java`的`Redis`客户端。

**哨兵作用小总结：**

- `Sentinel`哨兵的作用是什么？
  - **健康检测** ---> 心跳机制，每隔`1s`发送`ping`，若在指定时间内节点没有响应，则该哨兵认为该节点主观下线【不一定真的故障，可能是网络阻塞】，然后其它哨兵会跑来确认，如果`quorum`即认为主观下线的哨兵数量超过总哨兵数量的一半，则认为客观下线，此时判定为故障
  - **故障恢复** ---> 通过断开时长、从节点优先级、偏移量、从节点`id`一步步判断，选定一个备选节点充当主节点，先通过`slaveof no one`的命令告诉该备选节点将其升级为主节点此时新主节点诞生，然后通过`slave <newmasterip> <newmasterport>`告诉其它从节点从而设定新的主从关系，最后将旧主节点标记为`slave`防止其恢复后不满与新的主节点产生冲突，当它恢复时会立即转变为从节点，因为已经被做了标记，没有别的选择。然后从新的主节点中同步数据。一代王朝就此陨落。
  - **通知服务** ---> 充当服务来源告诉客户端主从关系已更改即将最新消息推送给客户端
- `Sentinel`如何判断一个`Redis`实例是否健康？
  - 基于心跳机制实现健康检测，哨兵会每隔`1s`就发送`ping`命令，如果在规定时间内没有得到该节点的响应，则该哨兵主观判断其下线即主观下线，并通知其它哨兵前来做进一步的验证
  - 其余哨兵如果判定该实例下线，若主观判定下线的哨兵数量即`quorum`超过了总哨兵数量的一半，则主观下线转化为客观下线，判定该实例确实出现了故障，此时将该实例下线【选出备选节点 ---> 通知备选节点 ---> 更改集群主从关系 ---> 将旧主节点标记为`slave`】。
- 故障转移需要哪些步骤？
  - 首先根据断开时长没有超过`down-after-millseconds * 10`的、从节点优先级`replica-priority`【默认都为`100`】越高的即数值越小的、偏移量`offset`越大的、从节点`id`越小的逐步选出备选节点
  - 通过`slaveof no one`命令告诉备选节点它已被升为主节点
  - 告诉其它从节点，更改主从关系`slaveof <newmasterip> <newmasterport>`
  - 将旧主节点标记为`slave`，当其恢复健康时执行`slaveof <newmasterip> <newmasterport>`将其设定为从节点并设定主从关系

#### 18.5.3 搭建哨兵架构

这里我们搭建一个三节点形成的`Sentinel`哨兵集群，来监管之前的`Redis`主从集群。如图：

![img](https://img-blog.csdnimg.cn/6fcbf125cab149a9babc1f7eb77a3f2f.png)

三哨兵`sentinel`实例信息如下：

| 节点 |        IP         |  PORT   |
| ---- | :---------------: | :-----: |
| `s1` | `192.168.150.101` | `27001` |
| `s2` | `192.168.150.101` | `27002` |
| `s3` | `192.168.150.101` | `27003` |

因为这里是在`Docker`为了更方便，这里不重新创建容器了，然后为了做出哨兵集群以及区分不同的哨兵，会将`sentinel1`放到容器`redis7001`中，`sentinel2`放到容器`redis7002`中，然后`sentinel3`放到容器`redis7003`中。这样就能做出一个哨兵集群。

为了方便，直接在不同的`Redis`实例中的`data`目录创建`sentinel.conf`文件。

```shell
cd 7001/data
mkdir sentinel1
cd sentinel1
vim sentinel.conf

port 27001
sentinel monitor mymaster 172.17.0.6 7001 2
sentinel down-after-milliseconds mymaster 5000
sentinel failover-timeout mymaster 60000
dir "/data/sentinel1"
:wq

pwd：/var/lib/docker/volumes/redis/7001/data/sentinel1/
mkdir ../../../7002/data/sentinel2
mkdir ../../../7003/data/sentinel3
cp sentinel.conf ../../../7002/data/sentinel2/
cp sentinel.conf ../../../7003/data/sentinel3/
记得修改下各个配置文件的端口：27001 27002 27003
以及各个配置文件的 dir "/data/sentinel1" "/data/sentinel2" "/data/sentinel3"

启动哨兵：
docker exec -it redis7001 redis-sentinel /data/sentinel1/sentinel.conf
docker exec -it redis7002 redis-sentinel /data/sentinel2/sentinel.conf
docker exec -it redis7003 redis-sentinel /data/sentinel3/sentinel.conf
```

启动，可以看到主节点、从节点以及其余的哨兵：

![img](https://img-blog.csdnimg.cn/db5316d38c7a48b88195f4a5e4d51ecc.png)

到这里，哨兵模式就搭建完成了。

现在尝试让`master`即`7001`的`Redis`实例宕机即关闭，此时`Sentinel1`也会随之关闭：

```shell
docker stop redis7001
```

查看`sentinel`哨兵日志：可以看到选择了`redis7003`为主节点，更改集群的主从关系，标记原主节点为`slave`。形成了新的局面。

![img](https://img-blog.csdnimg.cn/ba790fdf992044ad8445a44a517e45ff.png)

此时将`redis7001`重新启动，查看`sentinel1`日志：

![img](https://img-blog.csdnimg.cn/792e3ebd943d4cafa7765ff025b384e3.png)

可以看到，物是人非，此时做主的已是`redis7003`。

#### 18.5.4 `RedisTemplate`的哨兵模式

在哨兵`sentinel`集群监管下的`Redis`主从集群，其节点会因为自动故障转移而发生变化，`Redis`的客户端需要知道确切的主从关系才能知道主节点是哪个，因为有故障恢复，所以需要有人及时地将节点地服务状态推送给`Redis`客户端，这就是哨兵的第三个作用：`服务通知功能`。

创建一个简单的`Redis Demo`工程：

1. 搭建`SpringBoot`工程

2. 引入依赖：

   ```xml
   
