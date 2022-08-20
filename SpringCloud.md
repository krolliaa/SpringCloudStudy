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

#### 17.8.5 `Topic`

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

### 17.9 消息转换器

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

## 18. `ElasticSearch`

`ELasticsearch`是一款非常强大的开源搜索引擎，具备非常多强大功能，可以帮助我们从海量数据中快速找到需要的内容，可以用来实现搜索、日志统计、分析、系统监控等功能。

`Kibana Logstash Beats`都是可替代的，只有`ElasticSearch`是不可替代的，它是`elastic stack`的核心，负责存储、搜索、分析数据。`ElasticSearch`的核心技术就是倒排索引。

![img](https://cdn.xn2001.com/img/2021/20210918202359.png)

### 18.1 `ElasticSearch`核心技术 —— 倒排索引

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

### 18.2 `ElasticSearch`文档和字段

`ElasticSearch`是面向文档`Document`存储的，可以是数据库中的一条商品数据，一个订单信息。文档数据会被序列化`JSON`格式然后存储在`ElasticSearch`中。而`JSON`文档中往往包含着很多的字段，其实就是类似数据库中的列。

![img](https://cdn.xn2001.com/img/2021/20210918212707.png)

### 18.3 `ElasticSearch`索引和映射

**索引`Index`**其实就是相同类型的文档的集合，所以可以把索引看作是数据库里面的表。例如：

- 所有用户文档，就可以组织在一起，称为用户的索引
- 所有商品的文档，可以组织在一起，称为商品的索引
- 所有订单的文档，可以组织在一起，称为订单的索引

![img](https://cdn.xn2001.com/img/2021/20210918213357.png)

**在`18.2`学习了文档跟字段，现在又学习了索引，就可以整合以下，索引里面包含着文档，文档里面有字段。跟以前学习的`MySQL`对比以下就是：索引相当于数据库表而文档相当于记录也就是行而字段就相当于列也就是属性。**

除此之外，我们知道数据库中会有一些约束信息，用来定义表的结构、字段名称、类型等信息。`ElasticSearch`也有这样的概念，在索引库中用映射`mapping`的概念来表示，映射是用于索引中文档的字段约束信息的。

### 18.4 `ElasticSearch`和`MySQL`

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

### 18.5 安装`ElasticSearch`

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

### 18.6 安装`Kibana`

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

### 18.7 安装`ik`分词插件

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

### 18.8 操作索引库之`Mapping`

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

### 18.9 增删改查索引库

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

### 18.10 `ElasticSearch`文档操作

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

### 18.11 使用`RestClient`操作`ElasticSearch`

`ES`官方提供了各种不同语言的客户端，用来操作`ES`。这些客户端的本质就是组装 `DSL`语句，通过`http`请求发送给`ES`。官方文档地址：https://www.elastic.co/guide/en/elasticsearch/client/index.html。

`ElasticSearchRestHighLevel`需要引入版本号，在`8.x`的`ElasticSearch`中已经被弃用了，新版本使用的是`ElasticSearch Java API Client`。这里由于`ElasticSearch`的版本是`7.12.1`的所以还是可以使用`Java Rest Client`。

![img](https://cdn.xn2001.com/img/2021/20210919234405.png)

后面将使用 `Java HighLevel Rest Client`操作`ElasticSearch`。

#### 18.11.1 初始化`hotel-demo`项目

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

#### 18.11.2 初始化`RestHighLevelClient`

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

#### 18.11.3 使用`RestHighLevelClient`创建、删除、判断索引是否存在

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

#### 18.11.4 使用`RestHighLevelClient`增删改查文档

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

### 18.12 `DSL`文档查询【重点】

`ElasticSearch`提供了基于`JSON`的`DSL(Domain Specific Language)`来定义查询。常见的查询类型包括：

1. **查询所有：**查询所有数据，一般测试使用。例如：`match_all`
2. **全文检索：**即`full text`查询，利用分词器对用户输入内容进行分词，然后使用倒排索引在索引库中匹配，例如：`match_query`以及`multi_match_query`
3. **精确查询：**根据精确词条值查找数据，一般就是查找`keyword`、数值、日期、`boolean`等类型字段，例如：`ids range term`
4. **地理查询：**即`geo`查询，根据经纬度查询。例如：`geo_distance、geo_bounding_box`
5. **复合查询：**即`compound`查询，复合查询可以将上述的各种查询条件组合起来，合并查询条件。例如：`bool、function_score`

#### 18.12.1 查询所有

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

#### 18.12.2 全文检索

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

#### 18.12.3 精确查询

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

#### 18.12.4 地理坐标查询

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

#### 18.12.5 复合查询

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

##### 18.12.5.1 算分函数查询

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

##### 18.12.5.2 布尔查询

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

### 18.13 搜索结果处理

#### 18.13.1 排序

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

#### 18.13.2 分页

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

#### 18.13.3 高亮

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

### 18.14 `DSL`总体结构如下

![img](https://cdn.xn2001.com/img/2021/20210921235330.png)

### 18.15 使用`RestHighLevelClient`进行文档查询

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

