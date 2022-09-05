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
   ```

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

