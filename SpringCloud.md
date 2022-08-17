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

1. 微服务治理：`Eureka`、`Nacos`、`OpenFeign`、网关`GateWay`
2. `Docker`：`Docker原理`、`Docker使用`、`Dockerfile`、`DockerCompose`
3. 异步通信：同步和异步、`MQ`技术选型、`SpringAMQP`、消费者限流
4. 分布式搜索：`DSL`语法、`HighLevelClient`、拼音搜索、自动补全、竞价排名、地理搜索、聚合统计、分片集群
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

## 11. `Nacos`配置中心

`Nacos`非常强大，此前我们看到它可以作为注册中心使用，现在，你将再一次看到它的强大 —— 它可以作为配置中心使用。一方面：**`Nacos`可以配置集中管**理，另一方面：**可以在配置变更的时候及时通知微服务并实现配置的热更新**。

### 11.1 `Nacos`创建配置

![](https://cdn.xn2001.com/img/2021/20210901092159.png)

![](https://cdn.xn2001.com/img/2021/20210901092206.png)

默认直接就是热更新的，要实现热更新的配置才放到`Nacos`，否则还是放到本地中会比较好【比如数据库连接信息】。

### 11.2 微服务读取`Nacos`配置中心配置

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

### 11.3 `Nacos`配置中心实现热更新

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

### 11.4 `Nacos`配置中心环境共享

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

## 12. 搭建`Nacos`集群

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

## 13. 远程调用

一直到这一部分，我们想远程调用都使用的是`RestTemplate`，我们再来看看使用`RestTemplate`进行远程调用的代码：

```java
String url = "http://user-service/user/" + order.getUserId();
User user = restTemplate.getForObject(url, User.class);
```

可以看到这样子的代码：

- 可读性差
- 参数`URL`复杂难以维护

为了更好用更方便更直观的调用想调用，所以我们决定采用`Feign`：其作用就是帮助我们**<font color="red">优雅的实现`http`请求的发送</font>**，解决上面提到的问题。![img](https://img-blog.csdnimg.cn/3cfd43ee11ea41f2ba33656ae801c5a0.png)

## 14.`Feign`

### 14.1 `Feign`实现远程调用

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

### 14.2 `Feign`自定义配置

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

### 14.3 `Feign`性能优化

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

### 14.4 `Feign`最佳实践

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

## 15. `Gateway`网关

有了服务集群，还晓得了注册中心、配置中心、远程调用。可是还有个大问题，就是你内部的许多服务有时候只对一些内部人员访问，并不是所有人都可以访问的，所以就需要有样东西可以给所有来访问服务的请求把把关。这就需要使用到`Getway`网关兄弟了。

![img](https://cdn.xn2001.com/img/2021/20210901092857.png)

### 15.1 网关可以干什么

网关的核心功能特性【可以干什么】？

- **身份验证，权限校验**
- **服务路由，负载均衡**
- **请求限流**

在`SpringCloud`中网关的实现包括两种：`Gateway`和`Zuul`。`Zuul`是基于`Servlet`实现的，属于阻塞式编程。而`Spring Cloud Gateway`则是基于`Spring5`中提供的`WebFlux`，属于响应式编程的实现，具备更好的性能。

### 15.2 网关初步使用

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

### 15.3 断言工厂

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

### 15.4 过滤器工厂

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

### 15.5 全局过滤器

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

### 15.6 过滤器顺序

请求进入网关会碰到三类过滤器：`DefaultFilter`、当前路由的过滤器、`GlobalFilter`。

请求路由后，会将三者合并到一个**过滤器链（集合）**中，排序后依次执行每个过滤器。

排序规则如下：

- 每一个过滤器都必须指定一个 int 类型的 order 值，**order 值越小，优先级越高，执行顺序越靠前**。
- `GlobalFilter`通过实现`Ordered`接口，或者使用`@Order`注解来指定`order`值，由我们自己指定。
- 路由过滤器和`defaultFilter`的`order`由`Spring`指定，**默认是按照声明顺序从1递增**。
- 当过滤器的`orde`值一样时，**会按照` defaultFilter`> 路由过滤器 > `GlobalFilter`的顺序执行**。

![img](https://cdn.xn2001.com/img/2021/202108230002747.png)

### 15.7 跨域问题

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

## 16. `Docker`

## 17. `MessageQueue`

### 17.1 同步通讯和异步通讯

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

### 17.2 消息队列

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

### 17.3 使用`Docker`安装`RabbitMQ`

1. 拉取`RabbitMQ`镜像：

   ```shell
   docker pull rabbitmq:3-management
   ```

2. 运行`RabbitMQ`容器，用户名：`admin`，密码：`123456`，容器名：`RabbitmqContainer`，服务端口`5672`，`web`管理端口`15672`，主机名：`mq1`

   ```shell
   docker run -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=123456 --name RabbitMQContainer --hostname rabbitmq -p 15672:15672 -p 5672:5672 -d rabbitmq:3-management
   ```

3. `VirtualBox`配置端口映射`15672`然后访问地址：`http://192.168.56.1:15672`即可

### 17.4 `RabbitMQ`中的角色及其基本结构

- `publisher`：生产者【寄件人】
- `consumer`：消费者【收件人】
- `exchange`：交换机，负责消息路由【快件分拨中心】
- `queue`：队列，存储消息【丰巢快递柜】
- `virtualHost`：虚拟主机，**隔离不同租户**的`exchange`、`queue`、消息隔离

![img](https://cdn.xn2001.com/img/2021/20210904172912.png)

### 17.5 `RabbitMQ`的底层实现原理：`AMQP`协议

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

### 17.6 `RabbitMQ`官方模型介绍

`RabbitMQ`官方提供了`5`个不同的`Demo`示例，对应了不同的消息模型。

![img](https://cdn.xn2001.com/img/2021/20210904173739.png)

### 17.7 `RabbitMQ`官方`API`实现`HelloWorld`模型

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

### 17.8 `SpringAMQP`实现发布订阅模型

`SpringAMQP`是基于`RabbitMQ`封装的一套模板，并且还利用 `SpringBoot`对其实现了自动装配，使用起来非常方便。`SpringAMQP`的官方地址：https://spring.io/projects/spring-amqp。

![img](https://cdn.xn2001.com/img/2021/20210904202046.png)

![img](https://cdn.xn2001.com/img/2021/20210904202056.png)

`SpringAMQP`提供了三个功能：

- 自动声明队列、交换机及其绑定关系
- 基于注解的监听器模式，异步接收消息
- 封装了`RabbitTemplate`工具，用于发送消息

#### 17.8.1 `Hello-World`

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

#### 17.8.2 `Work-Queue`

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

#### 17.8.3 `Fanout`

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

#### 17.8.4 `Direct`



#### 17.8.5 `Topic`

