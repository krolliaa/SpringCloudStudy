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
>   - 优点：架构简单 + 部署成本低
>   - 缺点：耦合度高 + 一旦项目很大打包时间也将非常长
>
> <hr>
>
> - 分布式架构：根据业务功能对系统进行拆分，每个业务模块作为独立项目开发，称为一个服务
>   - 优点：降低服务耦合 + 有利于服务升级扩展
>   - 问题：分布式需要考虑许多问题，比如访问量大的时候并且放置服务器故障你得做个集群然后你得做远程过程调用`RPC`，服务拆分粒度如何？服务集群地址如何维护？服务之间如何实现远程调用`RPC`？服务健康状态如何感知？
>
> <hr>
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
