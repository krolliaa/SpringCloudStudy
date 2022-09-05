## 20. 服务异步通讯

### 20.1 传统`MQ`的问题

以前学习的`MQ`感觉已经很不错了，但其实还是有一些问题我们并不明白消息队列是如何作保障的：

1. **消息可靠性的问题：**如何确保发送的消息至少被消费一次
2. **延迟消费问题：**这其实是业务上的问题了，就是如何实现消息的延迟投递即我怎么才能到点发送消息
3. **消息堆积问题：**如何解决数百万消息堆积，无法及时消费的问题
4. **高可用问题：**如何避免单点`MQ`故障而导致整个消息订阅模型不可用的问题

![img](https://img-blog.csdnimg.cn/92ccf280e4ba499080ffe33af5eb8a82.png)

### 20.2 消息可靠性

我们现在来解决第一大问题，如何确保消息队列发布订阅过程是可靠的？也就是如何确保从生产者生产的消息一定到了消费者手中被他所消费呢？

我们知道消息从被发送到消费者接收，中间会经历很多个过程，大体过程如下图：消息从发布者到消费者，可能丢失的地方在：

1. 发布者到交换机的中途中
2. 交换机到消息队列的中途中
3. 消息队列宕机，因为消息对立而是基于内存的所以导致存储的消息丢失
4. 消费者自身还没来得及消费就宕机了导致数据丢失

![img](https://img-blog.csdnimg.cn/22de4b7b4d4b4d90b6834851678b9fba.png)

所以消息丢失可以分为三种类型：【完全就是按消息经历过程来分类的】

1. 发送时丢失**【生产者导致丢失】**
   - 生产者`publisher`发送的消息没有送达到交换机`exchange`
   - 消息到达交换机`exchange`却没有到达消息队列`MessageQueue`
2. `MQ`宕机导致消息队列中的消息丢失**【`MQ`导致丢失】**
3. 消费者`consumer`接收到消息之后还没来得及消费就宕机了导致消息丢失**【消费者导致丢失】**

所以保证消息的可靠性，其本质就是解决上述可能存在的消息丢失问题即：发送过程丢失、消息队列宕机导致丢失、消费者宕机导致丢失。

#### 20.2.1 `MQ`如何解决发送路上的丢失

发送路上有两种情况会导致数据丢失，一是消息在发布者到达交换机的路上丢失，二是消息在交换机到达队列的路上丢失。它们都可以使用`RabbitMQ`提供的**生产者确认机制**即`publisher confirm`来确保消息传递的可靠性，该机制就可以避免消息发送到交换机的过程中丢失。

- **我们先来看生产确认机制如何解决第一种：<font color="red">生产者到交换机路上的丢失</font>** `publisher-confirm`

  当消息发送到交换机之后会返回一个结果给发送者，表示消息是否处理成功，因为交换机是属于`MQ`的一部分，所以返回处理结果给发送者这一工作会交给`MQ`来完成，`MQ`在发送者到交换机的这一过程返回`publisher-confirm`发送者确认，这其中发送者确认又分两种情况：

  - 消息成功投递到投递到了交换机，返回`ACK`确认信号[`acknowledge`]
  - 消息没有投递到交换机，返回`NACK`不确认信号

- **然后再来看生产确认机制如何解决第二种：<font color="red">交换机到达队列路上的丢失</font>** `publisher-return`

  在消息从交换机推送到队列的这一过程失败后，说明路由失败，生产确认机制就会返回发送者回执`publisher-return`

  - 消息投递到了交换机但是没有路由到队列。返回`ACK` + 路由失败原因。

![img](https://img-blog.csdnimg.cn/47ad3a2943a94304be22ac54ec624d90.png)

上面确实可以让生产者知道有消息在发送到队列`Queue`的过程中丢失的问题，但问题是，生产者怎么知道丢失或者确认收到的是哪条消息呢？所以我们要在生产者确认机制发送的确认消息即`MQ`发送的`publisher-confirm/publisher-return`里面设置一个唯一的标识即全局唯一`id`，以区分不同消息，避免`ack`冲突。

总结起来就是一句话：**`MQ`会通过生产确认机制确保消息发送过程的可靠性。**

为了演示生产者确认机制，需要先搭建一个`demo`项目，因为这学习的正是`MQ`高级部分，所以项目名称取名为：`mq-advanced-demo`。直接导入即可。然后需要在`docker`安装`rabbitmq`，记得对外开放端口：`5672 15672`。

```
docker run --name rabbitmq -p 15672:15672 -p 5672:5672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -v /var/lib/docker/volumes/mq-plugins/_data:/usr/local/src -d rabbitmq:3.8-management
```

1. 开启消息队列的生产者确认机制 ---> 修改配置文件：

   ```yaml
   spring:
     rabbitmq:
       publisher-confirm-type: correlated
       publisher-returns: true
       template:
         mandatory: true
   ```

   配置信息说明如下：

   - `publisher-confirm-type`：开启`publisher-confirm`，有两种类型：
     - `simple`：意思就是生产者发送完消息以后**同步等待**消息队列`MQ`的`publisher-confirm`结果，直到等到为止，如果没等到就一直等到超时，这种方式会导致代码的阻塞
     - `correlated`：**异步回调**，自定义`ConfirmCallback`，`MQ`返回结果时会调用这个回调类`ConfirmCallback`，这样生产者就不用一直等待了，发完这条消息就继续干它的事情比如发送下一条消息，有结果了就拿来看看，然后做处理。
   - `publisher-return`：**开启`publisher-return`功能**，同样是基于`callback`回调机制，不过定义的回调类实现的接口为`ReturnCallback`。
   - `template.mandatory`：定义消息路由失败时的策略，`publisher-return`只是告知发送者说这个消息丢失了，但是发送者要怎么处理消息路由失败还没说，这个就是用来说明策略的，如果为`true`就会调用`ReturnCallback`实现方法，如果为`false`则会直接丢弃消息

2. 编写`publisher-confirm`回调函数：`ConfirmCallback`

   `ConfirmCallback`可以在发送消息时指定，因为每个业务处理`confirm`成功或失败的逻辑不一定相同。

   在`publisher`服务的`com.kk.mq.spring.SpringAmqpTest`类中，定义一个单元测试方法：

   **<font color="red">注：我这里跟源代码提供的`log`等级不一样，他的是`debug error`，我这里是`info`，因为我要显示后续效果，否则打印不出来。</font>**

   ```java
   @Test
   public void testPublisherConfirm() throws InterruptedException {
       String message = "Hello, Spring AMQP!";
       //这里的 UUID.randomUUID().toString() 的意思是定义唯一全局 ID
       CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
       //添加回调函数
       correlationData.getFuture().addCallback(result -> {
           if (result.isAck()) {
               log.info("消息从发送者到交换机的过程成功，ID:{}", correlationData.getId());
           } else {
               log.info("发送者到交换机的过程消息丢失，消息发送到路由器失败, ID:{}, 原因{}", correlationData.getId(), result.getReason());
           }
       }, throwable -> {
           log.error("消息从发送者到交换机的过程中发生异常, ID:{}, 原因{}", correlationData.getId(), throwable.getMessage());
       });
       rabbitTemplate.convertAndSend("task.direct", "task", message, correlationData);
       //等待 ACK 回执
       Thread.sleep(2000);
   }
   ```

3. 编写`ReturnCallback`回调函数，用于`publisher-return`发送回执，如果在消息要从交换机到达队列的过程消息丢失，就会调用这个`ReturnCallback`回调函数 ---> 该回调函数需要通过`RabbitTemplate`来设置，所以需要在应用启动式，`Bean`工厂创建好之后创建好回调函数`ReturnCallback`：

   ```java
   package com.kk.mq.config;
   
   import lombok.extern.slf4j.Slf4j;
   import org.springframework.amqp.rabbit.core.RabbitTemplate;
   import org.springframework.beans.BeansException;
   import org.springframework.context.ApplicationContext;
   import org.springframework.context.ApplicationContextAware;
   import org.springframework.context.annotation.Configuration;
   
   @Slf4j
   @Configuration
   public class CommonConfig implements ApplicationContextAware {
       @Override
       public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
           RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
           rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
               log.info("交换第到队列的过程消息丢失，发送失败，应答码{}，原因{}，交换机{}，路由键{}，消息{}", replyCode, replyText, exchange, routingKey, message);
           });
       }
   }
   ```

4. 先来测试下`ConfirmCallback`中消息在发送者到交换机之间丢失的情况，浏览器输入http://192.168.56.1:15672登录`RabbitMQ`的`Web`管理界面【后面都是这样，不再赘述】，可以看到没有`task.direct`的交换机，所以消息是到达不了交换机的，我们此时点击发送：

   ![img](https://img-blog.csdnimg.cn/9e662aef88c141dfbc02b57ac427acf9.png)

   观察`IDEA`中返回的情况：可以发现确实触发了生产确认机制调用了`ConfirmCallback`，然后发送了丢失的消息

   ![img](https://img-blog.csdnimg.cn/9286a9472d004882a5006c7d75a8624c.png)

5. 再来看下如果有交换机，但是没有队列的情况：

   ![img](https://img-blog.csdnimg.cn/e3664376f306496fb9345e1ace995b9c.png)

   发送消息，查看结果：可以看到调用了发送消息时`CorrelationData`设置的`ConfrimCallback`回调函数以及在`CommonConfig`中应用启动时设置的`ReturnCallback`回调函数。可以看到消息到达了交换机但是在交换机到队列中的过程丢失了。

   ![img](https://img-blog.csdnimg.cn/90d65cdbd4164b90b3dc3252d1f2e95d.png)

6. 再继续创建好队列，绑定交换机`direct.task`以及路由`task`：

   ![img](https://img-blog.csdnimg.cn/2418e9508c4442f6a04f44610fbaca3c.png)

   ![img](https://img-blog.csdnimg.cn/3f82fe8b5d14472f9f0fbf3fd7f6f0c2.png)

   然后发送消息查看结果：可以看到控制台没有返回任何信息，查看队列可以看到队列中已经有消息了。

   ![img](https://img-blog.csdnimg.cn/73036919f58d4c3589cb89206dffe7b0.png)

   点击`Queue`界面中的`Get Message`，可以看到确实是我们在`Java`中所发送的消息：`Hello, Spring AMQP!`

   ![img](https://img-blog.csdnimg.cn/c9e24c7d5caa4e44900c9a88499c99c4.png)

**<font color="deepskyblue">到这里，确保消息在发送过程中：包括从发送者到交换机、交换机到队列不会丢失就已经学习完成了 ---> 使用的是</font><font color="red">发送者确认机制</font>。**

总结：

- `SpringAMQP`中处理消息确认的几种情况：
  - `publisher-confirm`
    1. 消息成功从发送者手中发送到交换机，返回`ACK`
    2. 消息从发送者手发送给交换机的过程失败，返回`NACK`
    3. 消息在发送过程中发生异常没有收到任何回执返回异常处理
  - `publisher-return`
    1. 消息从交换机发送给队列的过程失败，返回`ACK + 路由失败原因`

#### 20.2.2 `MQ`如何解决`MQ`宕机导致数据丢失

上一小节我们学习了如何避免消息在发送过程丢失的问题，此时消息就会到达本站`RabbitMQ`手中，准确地说是已经投递到了消费者要消费的队列中，那如果此时`RabbitMQ`宕机了咋办，我们知道`MQ`中的数据是存储在内存中，那这样岂不是数据都丢失了？

想想还有什么跟`MQ`很像，数据也是存储在内存之中的呢？没错，就是`Redis`，当时我们是如何解决`Redis`数据丢失的问题的呢？没错，正是那个宝贝 —— 持久化！！！

所以针对消息队列为了避免消息丢失，我们需要做**<font color="red">消息持久化</font>font>**。

创建的消息队列和交换机的时候，创建的时候默认它们就是持久的这个也可以在创建的是否，若是`Java`代码可以通过设置`true / false`来设置是否`Durable`即持久化，若是`Web`管理界面可设置`Durability`中`Transient`是否是短暂的还是`Durable`持久的来设置交换机或者队列的持久化。

重点是消息的持久化，`SpringAMQP`默认会将消息持久化，所以就算我们重启`RabbitMQ`也可以在队列中照常看到消息。

可以通过`MessageBuidler`创建`Message`对象，使得该消息不被持久化：

```java
Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT).build();
```

点击`IDEA`发送消息然后：可以看到此时队列是有消息的：

![img](https://img-blog.csdnimg.cn/b72557138f824fb48e0e8e63598160b5.png)

然后重启`Docker rabbitmq`：

```shell
docker restart rabbitmq
docker logs -f rabbitmq
```

可以发现这时队列中的消息不见了，正是因为消息没有做持久化：

![img](https://img-blog.csdnimg.cn/4825626e15374f47b0f6d8e6c9a0fbad.png)

如果将`MessageBuilder`中的`setDeliveryMode`更改为`MessageDeliveryMode.PERSISTENT`或者干脆直接发送`message`而不是`msg`【通过`MessageBuilder`创建】因为默认`SpringAMQP`就已经实现了消息持久化了：

![img](https://img-blog.csdnimg.cn/b72557138f824fb48e0e8e63598160b5.png)

#### 20.2.3 `MQ`如何解决消费者宕机导致数据丢失

通过前面两个小节的学习就知道了消息队列通过发送者确认机制以及交换机、队列、消息的持久化实现了消息发送过程【发送者--->交换机，交换机--->队列】、队列的可靠性。

到了这一步就是消息从队列发送到消费者，消费者消费消息了。那么在这里，`MQ`又该如何在消费者这里完成它的可靠性呢？如何防止在消费者消费之前的那一刻消费者宕机了导致消息丢失呢？这就需要使用到**<font color="red">阅后即焚机制</font>**。

阅后即焚机制：消费者会向`RabbitMQ`发送`ACK`回执，表明自己已经处理了消息，然后`RabbitMQ`接收到该`ACK`回执后会删除队列中的消息。

但是什么时候发送`ACK`回执呢？

- 是在消息还没到消费者手里就发送吗？显然不是
- 那是在消息到了消费者手中就立马发送吗？当然也不是，万一还没消费消息就删除了，不也无法实现可靠性嘛
- 那是在消息到了消费者手中并被消费了然后发送吗？没错就是这个了

`SpringAMQP`提供了三种消费者确认模式：

1. `manual`：**手动**`ACK`，在业务代码运行完毕之后调用`API`发送`ACK`，自己根据业务需求，判断消费者什么时候返回`ack`。
2. `auto`：**自动**`ACK`，由`Spring`检测`Listener`代码是否出现异常，若正常返回`ack`，若出现异常则抛出异常返回`nack`。该模式类似事务机制。
3. `none`：**关闭**`ack`，`MQ`假定消费者获取消息后会成功处理，因此消息投递后立即被删除，这种模式无法保证消息的可靠性。

通常使用的消费者确认消息的机制为：`auto`模式。

##### 20.2.3.1 阅后即焚机制之`none`模式

在消费者`consumer`的配置文件`application.yaml`中即可配置消费者消费返回`ack`的模式：

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: none/auto/manual
```

在`Consumer`模块下中的`CommonConfig`配置类声明`consumer.exchange`交换机、`consumer.queue`队列以及绑定该交换机跟队列，`Routing Key`设置为：`consumer.confirm`：

```java
package com.kk.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("consumer.exchange");
    }

    @Bean
    public Queue directQueue() {
        return new Queue("consumer.queue");
    }

    @Bean
    public Binding bindingDirectExchangeAndDirectQueue() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("consumer.confirm");
    }
}
```

消费者监听该消息进行消费：

```java
package com.kk.mq.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SpringRabbitListener {
    @RabbitListener(queues = "consumer.queue")
    public void listenSimpleQueue(String msg) {
        System.out.println("消费者接收到 consumer.queue 的消息：【" + msg + "】");
    }
}
```

生产者发送消息：

```java
@Test
public void testPublisherConfirm() throws InterruptedException {
    String message = "Hello, Consumer Queue!";
    //这里的 UUID.randomUUID().toString() 的意思是定义唯一全局 ID
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    //添加回调函数
    correlationData.getFuture().addCallback(result -> {
        if (result.isAck()) {
            log.info("消息从发送者到交换机的过程成功，ID:{}", correlationData.getId());
        } else {
            log.info("发送者到交换机的过程消息丢失，消息发送到路由器失败, ID:{}, 原因{}", correlationData.getId(), result.getReason());
        }
    }, throwable -> {
        log.error("消息从发送者到交换机的过程中发生异常, ID:{}, 原因{}", correlationData.getId(), throwable.getMessage());
    });
    Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
    rabbitTemplate.convertAndSend("consumer.exchange", "consumer.confirm", msg, correlationData);
    //等待 ACK 回执
    Thread.sleep(2000);
}
```

现在模拟下`none`模式，在消费者还没处理消费就抛出异常，代码如下：

```java
package com.kk.mq.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SpringRabbitListener {
    @RabbitListener(queues = "consumer.queue")
    public void listenSimpleQueue(String msg) {
        System.out.println(1 / 0);
        System.out.println("消费者接收到 consumer.queue 的消息：【" + msg + "】");
    }
}
```

此时先运行消费者模块，然后发送者发送消息：可以看到虽然抛出了异常但是队列中的消息依然被删除了，这是因为我们设置了`none`模式，所以消息从队列被投递出去之后，消息队列就立马将该消息从队列中删除了。

![img](https://img-blog.csdnimg.cn/6f423671e92e4d3ba6c57e778c741970.png)

![img](https://img-blog.csdnimg.cn/2050d7aa264f40bc9f66956cf5461f88.png)

##### 20.2.3.2 阅后即焚机制之`auto`模式

将上述配置文件中的`spring.rabbitmq.listener.simple.acknowledge-mode`设置为`auto`

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: none/auto/manual
```

然后在异常处打断点，观察`RabbitMQ Web`页面的效果：可以看到因为出现异常，所以消费者返回了`nack`给消息队列

![img](https://img-blog.csdnimg.cn/e32f5ef7aada4cff852ccbd2345ba44c.png)

继续执行，因为出现了异常，所以消息队列中的消息没有被删除，而是又一次等待直到被正常消费：

![img](https://img-blog.csdnimg.cn/8af1e09bc01a41fd93d373c3ce8b5842.png)

##### 20.2.3.3 消费失败重传机制

在上述`auto`模式的测试时【不使用`debug`模式】，不知道有无有人发现，当消费者出现异常后，消息会不断`requeue`（重入队）到队列，再重新发送给消费者，然后再次异常，再次`requeue`，无限循环，导致`mq`的消息处理飙升，带来不必要的压力，你完全可以在`IDEA`控制栏中看到这一情况，一直不断不断地报错：

![img](https://img-blog.csdnimg.cn/e220a3a42bbd46dca7e14e77c7efb20f.png)

这该如何解决呢？这就涉及到消费失败重试机制了。

**消息本地重试：**

我们可以利用`Spring`的`retry`机制，在消费者出现异常时利用本地重试，而不是无限制的`requeue`到`mq`队列。然后会发送`ACK`确认消息给`MQ`，`MQ`会将该消息删除。

修改配置文件即可：

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        retry:
          enabled: true # 开启消费者失败重试
          initial-interval: 1000 # 初识的失败等待时长为1秒
          multiplier: 1 # 失败的等待时长倍数，下次等待时长 = multiplier * last-interval
          max-attempts: 3 # 最大重试次数
          stateless: true # true无状态；false有状态。如果业务中包含事务，这里改为false
```

重启`consumer`服务，重复之前的测试。可以发现：

- 在重试`3`次后【按照上面设置地`max-attempts`来】，`SpringAMQP`会抛出异常`AmqpRejectAndDontRequeueException`，说明本地重试触发了
- 查看`RabbitMQ`控制台，发现消息被删除了，说明最后`SpringAMQP`返回的是`ack`，`mq`删除了消息

**消息本地重试结论：达到最大重试次数后，消息会被丢弃，由Spring内部机制决定的。**

- 开启本地重试时，消息处理过程中抛出异常，不会`requeue`到队列，而是在消费者本地重试
- 重试达到最大次数后，`Spring`会返回`ack`，消息会被丢弃

当重试达到最大次数之后还是失败后会让实现了`MessageRecovery`的实现类来处理，实现`MessageRecovery`接口有三种方式：

- **`RejectAndDontRequeueRecoverer`：重试耗尽后，直接reject，丢弃消息。默认就是这种方式**

- `ImmediateRequeueMessageRecoverer`：重试耗尽后，返回`nack`，消息重新入队

- `RepublishMessageRecoverer`：重试耗尽后，将失败消息投递到指定的交换机

我们来尝试将重试之后的操作改为第三种，将消息投递到指定的交换机：

```java
@Bean
public DirectExchange errorMessageExchange(){
    return new DirectExchange("error.direct");
}

@Bean
public Queue errorQueue(){
    return new Queue("error.queue", true);
}

@Bean
public Binding errorBinding(Queue errorQueue, DirectExchange errorMessageExchange){
    return BindingBuilder.bind(errorQueue).to(errorMessageExchange).with("error");
}

@Bean
public MessageRecoverer messageRecoverer() {
    return new RepublishMessageRecoverer(rabbitTemplate, "error.exchange", "error");
}
```

![img](https://img-blog.csdnimg.cn/4b036138f1b946c5a388996e8ddafc8b.png)

**总结：**

- 如何确保消息在消费者这里的可靠性？
  1. 开启阅后即焚机制，模式有`manual/auto/none`，手动、自动、关闭，将其设置为自动以后消费者会向消息队列发送`uack`然后一直重试
  2. 消息重试机制，默认为`RejectAndDontRequeueRecoverer`就是当最大的重试次数耗尽后，会发送`ACK`给消息队列，然后消息队列会将该消息删除，也可以编写`Bean ---> MessageRecover`覆盖`RejectAndDontRequeueRecoverer`，可以使用：`ImmediateRequeueMessageRecoverer`或者`RepublishMessageRecoverer`，分别表示返回重试最大次数后`nack`重新入队、重试最大次数后将消息发送给指定队列。

#### 20.2.4 消息可靠性总结

1. 发送过程这里：使用**发送者确认机制**

   - 消息从发送者到交换机：`publisher-confirm`

     1. 会使用发送者确认机制，从生产者到交换机，若丢失返回`publisher-confirm-nack`

     2. 如果没有丢失则消息队列【交换机】发送`publisher-confirm-ack`

        - 如果在配置文件中设置的是`publisher-confrim:simple`则表示消息从发送方到交换机开始发送后一直等待交换机即消息队列返回结果，直到超时才肯罢休，可能造成代码阻塞

        - 如果在配置文件中设置的是`publisher-confirm:correlated`则表示异步嗲用，即消息从发送方到交换机开始发送后，发发送者就可以自己干自己的事情去了，不用等待返回结果，当交换机接收到或者没接收到都会去调用`ConfirmCallback`回调函数

   - 消息从交换机到队列：`publisher-return`
     - 设置`spring.rabbitmq.publisher-returns:true`表示开启`publisher-return`功能，不开启则无法保证消息从交换机到队列的可靠性，然后需要进一步设置该功能开启后如果数据真的在交换机到队列这一过程丢失该怎么办，有两种选择：`true or false`，如果为`true`，则会调用`RabbitTemplate`在创建后设置的回调函数即`rabbitTemplate.setReturnCallback`。如果为`false`，则消息队列会直接将该消息丢弃。

2. 到了消息队列这里：使用**持久化**

   - 默认`SpringAMQP`创建的交换机、队列、消息都是持久化的，其中消息可以通过设置`MessageBuidler..setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT)`来设置消息是否持久化。

3. 到了消费者这里：使用**阅后即焚机制+消息重试机制**

   1. 需手动开启阅后即焚机制：`spring.rabbitmq.listener.simple.acknowledge-mode: none/auto/maunal`
      - 如果为`none`，当消费者消费时出现异常，照样会返回`ack`，然后消息队列丢弃该消息
      - 如果为`auto`，则会一直尝试重新接收该消息，很消耗消息队列资源，所以需要开启消息重试机制

   2. 需要手动开启**重试机制**：

      重试机制需要在配置文件中`application.yml`中配置，一般在达到最大重试次数就会触发处理消息处理器：有三种消息处理器模式可选，通过定义`Bean ---> MessageConver`即可覆盖，分别是：

      1. `RejectAndDontRequeueRecoverer`：重试耗尽后，不再接收该消息，消息被消息队列删除
      2. `ImmediateRequeueMessageRecoverer`：重试耗尽后，消费者返回`UACK`给消息队列，消息队列将消息再次入队
      3. `RepublishMessageRecoverer`：重试耗尽后，将消息返还给跟指定的交换机绑定的队列

```java
package com.kk.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CommonConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("交换机到队列的过程消息丢失，发送失败，应答码{}，原因{}，交换机{}，路由键{}，消息{}", replyCode, replyText, exchange, routingKey, message);
        });
    }
}

@Test
public void testPublisherConfirm() throws InterruptedException {
    String message = "Hello, Consumer Queue!";
    //这里的 UUID.randomUUID().toString() 的意思是定义唯一全局 ID
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    //添加回调函数
    correlationData.getFuture().addCallback(result -> {
        if (result.isAck()) {
            log.info("消息从发送者到交换机的过程成功，ID:{}", correlationData.getId());
        } else {
            log.info("发送者到交换机的过程消息丢失，消息发送到路由器失败, ID:{}, 原因{}", correlationData.getId(), result.getReason());
        }
    }, throwable -> {
        log.error("消息从发送者到交换机的过程中发生异常, ID:{}, 原因{}", correlationData.getId(), throwable.getMessage());
    });
    Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
    rabbitTemplate.convertAndSend("consumer.exchange", "consumer.confirm", msg, correlationData);
    //等待 ACK 回执
    Thread.sleep(2000);
}
```

```yaml
spring:
  rabbitmq:
      listener:
      simple:
        prefetch: 1
        acknowledge-mode: none/auto/manual
        retry:
          enabled: true # 开启消费者失败重试
          initial-interval: 1000 # 初识的失败等待时长为1秒
          multiplier: 1 # 失败的等待时长倍数，下次等待时长 = multiplier * last-interval
          max-attempts: 3 # 最大重试次数
          stateless: true # true无状态；false有状态。如果业务中包含事务，这里改为false
```

到这里，我们就解决了`MQ`的第一个问题：消息可靠性问题。接下来我们将处理：延迟消息问题。

### 20.3 死信队列和延迟队列

#### 20.3.1 什么是死信队列？

要谈死信队列首先得谈死信，那什么是死信呢？

当一个队列中的消息满足下列情况之一时，可以成为死信`（dead letter）`：

1. 消费者使用`basic.reject`【拒绝】或`basic.nack`声明消费失败，并且消息的`requeue`参数设置为`false`

2. 消息是一个过期消息，超时无人消费
3. 要投递的队列消息满了，无法投递

即**消费失败、消息过期、队列满了**就会造成死信，说白了就是要被删除的消息就是死信。

如果这个包含死信的队列配置了`dead-letter-exchange`属性，指定了一个交换机，那么队列中的死信就会投递到这个交换机中，而这个交换机称为**死信交换机**（`Dead Letter Exchange`，检查`DLX`）。所以死信交换机就是专门路由给死信队列的，而死信队列专门存放死信消息。

如图，一个消息被消费者拒绝了，变成了死信：

![img](https://img-blog.csdnimg.cn/29a7eabf9c6e4163867eedaea710a367.png)

因为`simple.queue`绑定了死信交换机`dl.direct`，因此死信会投递给这个交换机：

![img](https://img-blog.csdnimg.cn/f9654756cbf64f128d4b10d7095e0605.png)

一般这个死信交换机会绑定了一个队列，则消息最终会进入这个存放死信的队列：

![img](https://img-blog.csdnimg.cn/8feaabb444954576b2015b8302560da7.png)

另外，队列将死信投递给死信交换机时，必须知道两个信息才能确保投递的消息能到达死信交换机，并且正确的路由到死信队列。：

- 死信交换机的名称
- 死信交换机与死信队列绑定的`RoutingKey`

**<font color="red">【吐槽：这不跟实现消息可靠性消费者那里尝试消息重试设置的策略为：`RepublishMessageRecoverer`一样么，将消息投递给指定队列】</font>**

#### 20.3.2 利用死信交换机接收死信

在失败重试策略中，默认的`RejectAndDontRequeueRecoverer`会在本地重试次数耗尽后，发送`reject`给`RabbitMQ`，消息变成死信被丢弃掉。

能不能不丢弃掉这个死信呢？当然可以，我们可以添加一个死信交换机，然后将一个交换机和一个队列绑定在一块。这样消息变成死信后也不会丢弃，而是最终投递到死信交换机，路由到与死信交换机绑定的队列。

![img](https://img-blog.csdnimg.cn/8feaabb444954576b2015b8302560da7.png)

演示下，使用`SpringAMQP`定义死信交换机、死信队列：

```java
// 声明普通的 simple.queue队列，并且为其指定死信交换机：dl.direct
@Bean
public Queue simpleQueue2(){
    return QueueBuilder.durable("simple.queue") // 指定队列名称，并持久化
        .deadLetterExchange("dl.direct") // 指定死信交换机
        .build();
}

// 声明死信交换机 dl.direct
@Bean
public DirectExchange dlExchange(){
    return new DirectExchange("dl.direct", true, false);
}
// 声明存储死信的队列 dl.queue
@Bean
public Queue dlQueue(){
    return new Queue("dl.queue", true);
}
// 将死信队列 与 死信交换机绑定
@Bean
public Binding dlBinding(){
    return BindingBuilder.bind(dlQueue()).to(dlExchange()).with("simple");
}
```

#### 20.3.3 小结

- 什么样的消息会成为死信？
  - 被消费者`reject`/`nack`的
  - 消息超时未消费的
  - 队列已满无法投递
- 死信交换机的使用场景是什么？
  - 某队列只要绑定了死信交换机，死信会投递到死信交换机，没有绑的不会
  - 可以利用死信交换机收集所有消费者处理失败的消息（死信），交由人工处理，进一步提高消息队列的可靠性【集中统一管理死信，很棒的`IDEA`】

#### 20.3.4 过期时间`TTL`

一个队列中的某消息如果超时未消费，则会变为死信，超时分为两种情况【整体 + 局部】：

- 消息所在的队列设置了超时时间
- 消息本身设置了超时时间

![img](https://img-blog.csdnimg.cn/5042c6e643094c7ca3427638b8f3fbf5.png)

可以定义一个接收超时死信的死信交换机，`Routing Key`设置为`ttl`：

```java
@RabbitListener(bindings = @QueueBinding(
    value = @Queue(name = "dl.ttl.queue", durable = "true"),
    exchange = @Exchange(name = "dl.ttl.direct"),
    key = "ttl"
))
public void listenDlQueue(String msg){
    log.info("接收到 dl.ttl.queue的延迟消息：{}", msg);
}
```

当声明一个队列时，可以给将来存储在该队列的所有消息一个`TTL`：

```java
@Bean
public Queue ttlQueue(){
    return QueueBuilder.durable("ttl.queue") // 指定队列名称，并持久化
        .ttl(10000) // 单位为毫秒 ms，设置队列的超时时间，10秒
        .deadLetterExchange("dl.ttl.direct") // 指定死信交换机
        .build();
}
```

注：该队列绑定的死信交换机为`dl.ttl.direct`，成为死信的消息会被投递到死信交换机绑定的队列

声明交换机，将上面定义的`ttl.queue`队列跟`ttl.direct`交换机绑定：

```java
@Bean
public DirectExchange ttlExchange(){
    return new DirectExchange("ttl.direct");
}
@Bean
public Binding ttlBinding(){
    return BindingBuilder.bind(ttlQueue()).to(ttlExchange()).with("ttl");
}
```

##### 20.3.4.1 指定队列的`TTL`

发送消息给`ttl.direct`交换机，然后到了`ttl.queue`，等待`10s`以上，然后观察死信交换机绑定的死信队列`dl.queue`，可以看到该队列会得到`ttl.queue`过期的消息：

```java
@Test
public void testTTLQueue() {
    // 创建消息
    String message = "hello, ttl queue";
    // 消息ID，需要封装到CorrelationData中
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    // 发送消息
    rabbitTemplate.convertAndSend("ttl.direct", "ttl", message, correlationData);
    // 记录日志
    log.debug("发送消息成功");
}
```

查看发送消息到`ttl.queue`的日志：

![img](https://img-blog.csdnimg.cn/4dce1b7397404f45bf3255993d7b722f.png)

查看过期的消息被投递到`dl.queue`死信队列的日志：

![img](https://img-blog.csdnimg.cn/1fa49614e87e48cc8b6e025f5d0b8f5a.png)

同时你也可以看到时间刚好是`10s`说明过期时间`TTL = 10s`。

##### 20.3.4.2 指定消息的`TTL`

跟设置持久化一样，同样可以通过`MessageBuilder`创建`Message`对象的时候给该消息设置过期时间：`5000`表示`5s`，这里单位是`ms`

```java
@Test
public void testTTLMsg() {
    // 创建消息
    Message message = MessageBuilder
        .withBody("hello, ttl message".getBytes(StandardCharsets.UTF_8))
        .setExpiration("5000")
        .build();
    // 消息ID，需要封装到CorrelationData中
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    // 发送消息
    rabbitTemplate.convertAndSend("ttl.direct", "ttl", message, correlationData);
    log.debug("发送消息成功");
}
```

查看发送消息到`ttl.queue`的日志：

![img](https://img-blog.csdnimg.cn/d64887c75435403bb3ef3a9c8d2a7488.png)

查看过期的消息被投递到`dl.queue`死信队列的日志：

![img](https://img-blog.csdnimg.cn/8012e70753454ae6a16a088ce9bdf4f4.png)

同时你也可以看到时间刚好是`5s`说明该消息过期时间`TTL = 5s`。

##### 20.3.4.3 过期时间

- 消息超时的两种方式是？

  - 给队列设置`TTL`属性，进入队列后超过`TTL`时间的消息变为死信

  - 给消息设置`TTL`属性，队列接收到消息超过`TTL`时间后变为死信

- 如何实现发送一个消息`20`秒后消费者才收到消息？

  - 给消息的目标队列指定死信交换机
  - 将消费者监听的队列绑定到死信交换机
  - 发送消息时给消息设置超时时间为`20`秒

#### 20.3.5 延迟队列

前面学习了死信队列跟`TTL`，试想下，如果`TTL`到了我们将其发送给一个死信队列，这样不就构成了延迟消费吗？！没错！这就是延迟队列`Delay Queue`模式！

**多久之后要做什么事情而且用户量很大的，都可以考虑使用延迟队列！**

延迟队列的使用场景包括：

- 延迟发送短信
- 用户下单，如果用户在`15`分钟内未支付，则自动取消
- 预约工作会议，`20`分钟后自动通知所有参会人员

**<font color="red">而且关于延迟队列有个非常`NICE`的东西：</font>**

因为延迟队列的需求非常多，所以`RabbitMQ`的官方也推出了一个插件，原生支持延迟队列效果。

这个插件就是`DelayExchange`插件。参考`RabbitMQ`的插件列表页面：https://www.rabbitmq.com/community-plugins.html

![img](https://img-blog.csdnimg.cn/ac0ebc344064441e9df0165ac0aede86.png)

使用方式可以参考官网地址：https://blog.rabbitmq.com/posts/2015/04/scheduling-messages-with-rabbitmq

#### 20.3.6 安装`DelayExchange`插件实现延迟队列效果

去对应的`GitHub`页面下载`3.8.9`版本的插件，地址为https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/tag/3.8.9这个对应`RabbitMQ`的`3.8.5`以上版本。

因为我们是基于`Docker`安装，所以需要先查看`RabbitMQ`的插件目录对应的数据卷，使用如下命令可以查看数据卷，然后将已经下载好的插件，上传到`Linux`中即可。

进入到`rabbitmq`安装插件即可：

```shell
docker exec -it rabbitmq bash
```

因为这个插件我上传到的是`/usr/local/src/`目录中，所以先移动到`/plugins`中：

```shell
mv rabbitmq_delayed_message_exchange-3.8.9-0199d11c.ez /plugins
```

然后开启延迟队列插件：

```shell
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

![img](https://img-blog.csdnimg.cn/aa73c5dd34774761b8ad7c0f523d2a60.png)

#### 20.3.7 `DelayExchange`的原理

`DelayExchange`需要将一个交换机声明为`delayed`类型。当我们发送消息到`delayExchange`时，流程如下：

- 接收消息
- 判断消息是否具备`x-delay`属性
- 如果有`x-delay`属性，说明是延迟消息，持久化到硬盘，读取`x-delay`值，作为延迟时间
- 返回`routing not found`结果给消息发送者
- `x-delay`时间到期后，重新投递消息到指定队列

本质上还是：消息/队列设置`TTL`，到点就投送到指定死信队列。

#### 20.3.8 延迟发送消息的具体操作

插件让延迟队列变得异常简单：声明一个交换机，交换机的类型可以是任意类型，只需要设定`delayed`属性为`true`即可，然后声明队列与其绑定即可。

1. 声明`DelayExchange`交换机

   1. 基于注解方式

      ```java
      @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "delay.queue", durable = "true"), exchange = @Exchange(name = "delay.direct", durable = "true", delayed = "true"), key = "delay"))
      public void listenDelayedQueue(String msg) {
      	log.info("接收到 delay.queue 的延迟消息：{}", msg);
      }
      ```

   2. 基于`@Bean`配置类的方式

      ```java
      @Bean
      public DirectExchange delayedExchange() {
          return ExchangeBuilder.directExchange("delay.direct").delayed().durable(true).build();
      }
      ```

2. 基于配置类的的声明队列方式【注解的在声明交换机的时候已经做了】：

   ```
   @Bean
   public Queue delayedQueue() {
       return new Queue("delay.queue");
   }
   ```

3. 绑定交换机跟队列：

   ```java
   @Bean
   public Binding delayedBinding() {
       return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with("delay");
   }
   ```

4. 发送消息，因为是延迟队列需要带`x-delay`属性，本质就是声明`TTL`，要不之前我们需要`MessageBuilder.setExpiration(ms)`来做`TTL`，然后还要声明：存储的交换机、存储的队列，死信交换机、死信队列，如果是给整个存储队列设置`TTL`此时还需要在声明存储队列的时设置`ttl`。因为存储队列绑定私信交换机，所以需要用到`QueueBuilder.durable("存储队列名称").deadLetterExchange("死信交换机名称").build();`

   现在有了延迟队列的插件，直接声明交换机、队列，交换机带上`delayed()[true]`，然后发送的消息设置`Header x-delay`其实就是过期时间，然后就可以实现延迟队列的效果了，非常简单。

   ```java
   @Test
   public void testPublisherConfirm() throws InterruptedException {
       String message = "Hello, Consumer Queue!";
       //这里的 UUID.randomUUID().toString() 的意思是定义唯一全局 ID
       CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
       //添加回调函数
       correlationData.getFuture().addCallback(result -> {
           if (result.isAck()) {
               log.info("消息从发送者到交换机的过程成功，ID:{}", correlationData.getId());
           } else {
               log.info("发送者到交换机的过程消息丢失，消息发送到路由器失败, ID:{}, 原因{}", correlationData.getId(), result.getReason());
           }
       }, throwable -> {
           log.error("消息从发送者到交换机的过程中发生异常, ID:{}, 原因{}", correlationData.getId(), throwable.getMessage());
       });
       Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).setHeader("x-delay", 10000).build();
       rabbitTemplate.convertAndSend("delay.direct", "delay", msg, correlationData);
       log.debug("消息发送成功！");
   }
   ```

#### 20.3.9 总结

- 延迟队列插件的使用步骤包括哪些？
  - 声明一个交换机，添加`delayed`属性为`true`
  - 发送消息时，添加`x-delay`头，值为超时时间

**到这里就解决了消息延迟发送的问题，满足了特定业务上的需求，接下来就需要解决下一个问题了 —— 消息堆积问题：可能一下子有太多的请求导致可能有几百万的消息还没来得及消费，就堆积在了消息队列中，这个问题是怎么产生的呢？又该如何解决呢？**

### 20.5 解决消息堆积问题的惰性队列

当生产者发送消息的速度超过了消费者处理消息的速度，就会导致队列中的消息堆积，直到队列存储消息达到上限。我们前面说过，队列满了之后再投递的消息就会变成死信，死信如果没有转到死信队列绑定的队列，可能就会被丢弃，这就是**消息堆积问题**。

![img](https://img-blog.csdnimg.cn/12f4ab0b5a194360be1903ab40b0ee58.png)

这些消息不是因为没用才被丢，而是因为客观原因：生产者速度太快了，队列满了导致的，所以这些消息很可能还是有价值的，因此我们要想办法解决消息堆积问题。

- 第一个思路：我们知道是消费者跟不上生产者的生产速度，那增加消费者，提高消费速度不就好了。
- 第二个思路：能不能想办法吧消息队列的容量扩大，这样能存放的消息就更多了。

虽然两个思路都有各自的缺陷，但承认都是解决消息堆积问题的好办法！但是还有没有更好的办法呢？

我们知道消息堆积问题本质就是因为容量不够导致的，而且我们还知道消息队列是将消息存放在内存中的，这下我们就发现了华点，内存的容量小但是还有磁盘啊，我磁盘的容量肯定比内存大得多得多啊。所以我们就引出了第三个思路。

- 第三个思路：对于容量满的消息队列，还要存放的消息，将其放到磁盘当中而非内存，当消费者需要消费该消息时就从磁盘中读取。磁盘很大，可以支持上百万条的消息存储。

第三个思路虽然舍弃了速度和性能上的追求，但是但是！相比于无价的数据可言是值得的！！

而实现了第三个思路的，正是从`RabbitMQ`的`3.6.0`版本开始推出的：`Lazy Queues`的概念！即惰性队列！

惰性队列的特点就是我们上面提到的，我们整理一下：

- 接收到消息后直接存入磁盘而非内存
- 消费者要消费消息时才会从磁盘中读取并加载到内存
- 支持数百万条的消息存储

#### 20.5.1 使用命令方式设置惰性队列

可以在`RabbitMQ`内部中使用命令的方式设置惰性队列，要设置一个队列为惰性队列，只需要在声明队列时，指定`x-queue-mode`属性为`lazy`即可。可以通过命令行将一个运行中的队列修改为惰性队列：

```shell
rabbitmqctl set_policy Lazy "^lazy-queue$" '{"queue-mode":"lazy"}' --apply-to queues
```

命令解读：

- `rabbitmqctl` ：`RabbitMQ`的命令行工具
- `set_policy` ：添加一个策略
- `Lazy` ：策略名称，可以自定义
- `"^lazy-queue$"` ：用正则表达式匹配队列的名字
- `'{"queue-mode":"lazy"}'` ：设置队列模式为lazy模式
- `--apply-to queues  `：策略的作用对象，是所有的队列

#### 20.5.2 基于注解方式设置惰性队列

```java
@RabbitListener(queuesToDeclare = @Queue(value = "lazy.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")))
public void listenLazyQueue(String msg) {
    log.info("接收到 lazy.queue 的延迟消息：{}", msg);
}
```

#### 20.5.3 基于`Bean`方式设置惰性队列

```java
@Bean
public Queue lazyQueue() {
    return QueueBuilder.durable("lazy.queue").lazy().build();
}
```

可以看到基于配置类的方式设置惰性队列是最简单的。

#### 20.5.4 惰性队列总结

- 消息堆积问题的解决方案？
  - 第一种思路：交换机再绑定多个消费者或者给消费者开线程池，提高消费速度
  - 第二种思路：扩大消息队列的容量，本质就是扩大内存【不现实】
  - 第三种思路：使用惰性队列，将消息存储到硬盘中
- 惰性队列的优点有哪些？
  - 基于硬盘存储消息，消息存储上限高
  - 没有间歇性的`page-out`直接写磁盘，而不是先写内存再到磁盘，性能比较高
- 惰性队列的缺点有哪些？
  - 速度肯定是没有内存那么快的，受限于硬盘`I/O`
  - 基于磁盘存储所以消息时效性比较低

到这里就就觉了消息可靠性问题、消息延迟推送问题、消息堆积问题，只剩下了最后一个问题即高可用的问题。

### 20.6 `MQ`集群

一想到高并发高可用，就想到做集群，没错这样的思路非常正确，`MQ`也可以做集群，因为`RabbitMQ`是基于`Erlang`这门语言编写的而`Erlang`这门语言拥有跟原生`Socket`一样的延迟，效率非常高，而且进程间上下文切换效率远高于`C`语言，天然支持集群模式。`RabbitMQ`的集群有两种模式：

- **普通集群：**是一种分布式集群，将队列分散到集群的各个节点，从而提高整个集群的并发能力。
- **镜像集群：**是一种主从集群，再普通集群的基础上，添加了主从备份的功能，提高了集群的数据可用性。

镜像集群虽然支持主从备份，但是从主从同步不是强一致的，某些情况下可能具有丢失数据的风险。因此`RabbitMQ`在`3,8`版本之后，推出了全新的功能：**仲裁队列**。用于替代镜像集群，底层采用`Raft`协议确保主从节点的数据一致性。

#### 20.6.1 普通集群

普通集群，也叫标准集群`classic cluster`，具备以下特征：

1. 会在集群的各个节点间共享部分数据，包括：交换机、队列元信息。不包含队列中的消息。

2. 当访问集群某节点时，如果队列不在该节点，会从数据所在节点传递到当前节点并返回。

3. 队列所在节点宕机，队列中的消息就会丢失。

   **<font color="red">比如有两个消息队列`MQ1、MQ2`，如果你的消息在`MQ1`但是你连接的交换机绑定的队列是`MQ2`，此时`MQ2`就会去`MQ1`队列拉取消息然后返回消息。如果`MQ1`宕机消息就会丢失。</font>**

普通集群的结构如下：

![img](https://img-blog.csdnimg.cn/281966932a2645209e4324951c0d5e17.png)

#### 20.6.2 安装普通集群

计划部署如下：

| 主机名 | `WEB`控制台端口   | `AMQP`通信端口    |
| ------ | ----------------- | ----------------- |
| `mq1`  | `8081 ---> 15672` | `8071 ---> 5672`  |
| `mq2`  | `8082 ---> 15672` | `8072 ---> 5672`  |
| `mq3`  | `8083 ---> 15672` | `8073  ---> 5672` |

`RabbitMQ`底层依赖于`Erlang`，而`Erlang`虚拟机就是一个面向分布式的语言，默认就支持集群模式。

**<font color="red">集群模式中的每个`RabbitMQ`节点使用`cookie`来确定它们是否被允许相互通信。</font>**

这里的`cookie`是什么意思呢？其实啊，要使两个`MQ`节点能够通信，它们必须具有相同的共享秘密，称为**`Erlang cookie`**。`cookie`只是一串最多`255`个字符的字母数字字符。

因为在同一个集群中的节点肯定是需要相互通信的，也正因此它们就需要相同的`Cookie`，有了它才可以通信。

可以通过获取某个节点的`cookie`值作为整个集群的`cookie`。执行下面的命令：

```shell
docker exec -it rabbitmq cat /var/lib/rabbitmq/.erlang.cookie
```

可以看到cookie值如下：

```shell
UPSROPMOTDLBSLLQEPBO
```

接下来，停止并删除当前的所有`RabbitMQ`容器，搭建集群。

```sh
docker rm -f rabbitmq
```

在`/tmp`目录新建一个配置文件`rabbitmq.conf`：

```sh
cd /tmp
# 创建文件
touch rabbitmq.conf
```

文件内容如下：

```nginx
loopback_users.guest = false
listeners.tcp.default = 5672
cluster_formation.peer_discovery_backend = rabbit_peer_discovery_classic_config
cluster_formation.classic_config.nodes.1 = rabbit@mq1
cluster_formation.classic_config.nodes.2 = rabbit@mq2
cluster_formation.classic_config.nodes.3 = rabbit@mq3
```

再创建一个文件，记录cookie

```sh
cd /tmp
# 创建cookie文件
touch .erlang.cookie
# 写入cookie
echo "UPSROPMOTDLBSLLQEPBO" > .erlang.cookie
# 修改cookie文件的权限
chmod 600 .erlang.cookie
```

准备三个目录，`mq1、mq2、mq3`：

```sh
cd /tmp
# 创建目录
mkdir mq1 mq2 mq3
```

然后拷贝`rabbitmq.conf、cookie`文件到`mq1、mq2、mq3`：

```sh
# 进入/tmp
cd /tmp
# 拷贝
cp rabbitmq.conf mq1
cp rabbitmq.conf mq2
cp rabbitmq.conf mq3
cp .erlang.cookie mq1
cp .erlang.cookie mq2
cp .erlang.cookie mq3
```

启动集群，需要为`RabbitMQ`创建一个网络【因为是集群】，否则集群不可用，创建一个网络的命令如下：

```sh
docker network create mq-net
```

创建节点容器：

```sh
docker run -d --net mq-net \
-v ${PWD}/mq1/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf \
-v ${PWD}/mq1/.erlang.cookie:/var/lib/rabbitmq/.erlang.cookie \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin \
--name mq1 \
--hostname mq1 \
-p 8071:5672 \
-p 8081:15672 \
rabbitmq:3.8-management
```

```sh
docker run -d --net mq-net \
-v ${PWD}/mq2/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf \
-v ${PWD}/mq2/.erlang.cookie:/var/lib/rabbitmq/.erlang.cookie \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin \
--name mq2 \
--hostname mq2 \
-p 8072:5672 \
-p 8082:15672 \
rabbitmq:3.8-management
```

```sh
docker run -d --net mq-net \
-v ${PWD}/mq3/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf \
-v ${PWD}/mq3/.erlang.cookie:/var/lib/rabbitmq/.erlang.cookie \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin \
--name mq3 \
--hostname mq3 \
-p 8073:5672 \
-p 8083:15672 \
rabbitmq:3.8-management
```

我这里`CentOS`的做的网络是`NAT`地址转换，如果你也一样的话记得做一个端口映射：`8081 8082 8083`

可以登录`RabbitMQ`的`Web`控制页面，可以看到集群中的节点信息：

![img](https://img-blog.csdnimg.cn/dc4abce2a7cc4a1ea20e2524df2f61ee.png)

在`mq1`节点上添加一个队列，然后观察`mq2 mq3`控制台：

![img](https://img-blog.csdnimg.cn/8bac5a00e8ed46cba830e9cc2fdd79b2.png)

可以看到在`mq2 mq3`这两个控制台都可以看到`mq1`控制台创建的队列：

![img](https://img-blog.csdnimg.cn/aff03f14693c4ed0af37f1b1f204a1d7.png)

还可以进行数据共享的测试：

点击刚刚在`mq1`创建的`simple.queue`队列，进入管理页面：

![img](https://img-blog.csdnimg.cn/88b6389d2a3a49fbbe19b82312ae39c8.png)

给这个`simple.queue`队列发送一个消息：

![img](https://img-blog.csdnimg.cn/7bf71f1af80e4881b693d199f7c6e0d2.png)

结果在`mq2`、`mq3`上都能看到这条消息：

![img](https://img-blog.csdnimg.cn/3e480f411c4a455b824693c70cceb0e7.png)

如果此时让`mq1`宕机，此时通过`mq2 mq3`是访问不到`simple.queue`的，因为在普通集群中，`mq2 mq3`会去拉取`mq1`中的`simple.queue`队列。但此时`mq1`已经宕机，所以就拉取不到了，自然也就无法访问了。

![img](https://img-blog.csdnimg.cn/4d88abd34c664668b0d91859f1210dc8.png)

这也恰恰说明了不同节点上的队列是不会将本节点的消息拷贝给其它节点的。会造成业务数据上的损失，若宕机，可用性丢失。

#### 20.6.3 镜像集群

在刚刚的普通集群中，一旦创建队列的主机宕机，队列就会不可用。不具备高可用能力。如果要解决这个问题，可以使用镜像集群，镜像集群本质是主从模式，具备下面的特征：

1. 交换机、队列、队列中的消息会在各个`mq`的镜像节点之间同步备份。
2. 创建队列的节点被称为该队列的**主节点，**备份到的其它节点叫做该队列的**镜像**节点。
3. 一个队列的主节点可能是另一个队列的镜像节点。
4. 所有操作都是主节点完成，然后同步给镜像节点。用户发送给队列的一切请求，例如发送消息、消息回执默认都会在主节点完成，如果是从节点接收到请求，也会路由到主节点去完成。**镜像节点仅仅起到备份数据作用**。当主节点接收到消费者的`ACK`时，所有镜像都会删除节点中的数据。
5. 主宕机后，镜像节点会替代成新的主。

![img](https://img-blog.csdnimg.cn/6db7c93810324f5d9a3fdec88d9e03a3.png)

镜像模式的配置有3种模式：

| `ha-mode`         | `ha-params`         | 效果                                                         |
| :---------------- | :------------------ | :----------------------------------------------------------- |
| 准确模式`exactly` | 队列的副本量`count` | 集群中队列副本（主服务器和镜像服务器之和）的数量。`count`如果为`1`意味着单个副本：即队列主节点。`count`值为`2`表示`2`个副本：`1`个队列主和`1`个队列镜像。换句话说：`count = 镜像数量 + 1`。如果群集中的节点数少于`count`，则该队列将镜像到所有节点。如果有集群总数大于`count+1`，并且包含镜像的节点出现故障，则将在另一个节点上创建一个新的镜像。 |
| `all`             | `(none)`            | 队列在群集中的所有节点之间进行镜像。队列将镜像到任何新加入的节点。镜像到所有节点将对所有群集节点施加额外的压力，包括网络`I / O`，磁盘`I / O`和磁盘空间使用情况。推荐使用`exactly`，设置副本数为`（N / 2 +1）`。 |
| `nodes`           | *`node names`*      | 指定队列创建到哪些节点，如果指定的节点全部不存在，则会出现异常。如果指定的节点在集群中存在，但是暂时不可用，会创建节点到当前客户端连接到的节点。 |

这里我们以`rabbitmqctl`命令作为案例来讲解配置语法。

**`exactly`模式：**

```
rabbitmqctl set_policy ha-two "^two\." '{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}'
```

- `rabbitmqctl set_policy`：固定写法
- `ha-two`：策略名称，自定义
- `"^two\."`：匹配队列的正则表达式，符合命名规则的队列才生效，这里是任何以`two.`开头的队列名称
- `'{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}'`: 策略内容
  - `"ha-mode":"exactly"`：策略模式，此处是`exactly`模式，指定副本数量
  - `"ha-params":2`：策略参数，这里是`2`，就是副本数量为`2`，`1`主`1`镜像
  - `"ha-sync-mode":"automatic"`：同步策略，默认是`manual`【手动的】，即新加入的镜像节点不会同步旧的消息。如果设置为`automatic`【自动的】，则新加入的镜像节点会把主节点中所有消息都同步，会带来额外的网络开销

**`all`模式：**

```
rabbitmqctl set_policy ha-all "^all\." '{"ha-mode":"all"}'
```

- `ha-all`：策略名称，自定义
- `"^all\."`：匹配所有以`all.`开头的队列名
- `'{"ha-mode":"all"}'`：策略内容
  - `"ha-mode":"all"`：策略模式，此处是`all`模式，即所有节点都会称为镜像节点

**`nodes`模式：**

```
rabbitmqctl set_policy ha-nodes "^nodes\." '{"ha-mode":"nodes","ha-params":["rabbit@nodeA", "rabbit@nodeB"]}'
```

- `rabbitmqctl set_policy`：固定写法
- `ha-nodes`：策略名称，自定义
- `"^nodes\."`：匹配队列的正则表达式，符合命名规则的队列才生效，这里是任何以`nodes.`开头的队列名称
- `'{"ha-mode":"nodes","ha-params":["rabbit@nodeA", "rabbit@nodeB"]}'`: 策略内容
  - `"ha-mode":"nodes"`：策略模式，此处是`nodes`模式
  - `"ha-params":["rabbit@mq1", "rabbit@mq2"]`：策略参数，这里指定副本所在节点名称

#### 20.6.4 安装镜像集群

需求：使用`exactly`模式的镜像，因为集群节点数量为`3`，因此镜像数量就设置为`2`。

```shell
docker exec -it mq1 rabbitmqctl set_policy ha-two "^two\." '{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}'
```

在`mq1`节点中创建一个新的队列，然后查看`mq1 mq2`控制台：

![img](https://img-blog.csdnimg.cn/54daa85685734d019af52adfa544e521.png)

可以看到无论是在`mq1`还是`mq2`都可以看到队列，到这里为止，跟普通集群没什么差别，看起来差别在于`Features`特征上有个`ha-two`：

![img](https://img-blog.csdnimg.cn/4fb2e16af079493d9472ee787d023e36.png)

同样的，跟普通集群一样，也可以进行数据共享，关键是看某个节点宕机的时候，比如`mq1`宕机，但此时仍然可以消费`two.queue`中的消息：

```shell
docker stop mq1
```

![img](https://img-blog.csdnimg.cn/5510e1b2290940c5be35e98c9337ba16.png)

查看`two.queue`队列状态，可以发现依然是健康的，因为主节点从`rabbit@mq1`切换到了`rabbit@mq2`：

![img](https://img-blog.csdnimg.cn/6d09c2426e3745ca961978db41575375.png)

#### 20.6.5 仲裁队列

仲裁队列是`RabbitMQ3.8`版本以后才有的新功能，用来替代镜像队列，具备下列特征：

- 与镜像队列一样，都是主从模式，支持主从数据同步，之前的镜像集群主从之间的数据备份存在一定的间隔性所以不是强一致性的。
- 使用非常简单，没有复杂的配置
- 主从同步基于`Raft`协议，强一致

简单地说就是仲裁队列是为了强一致性新推出的，而且操作简单。

#### 20.6.6 添加仲裁队列

在任意控制台添加一个队列，一定要选择队列类型为`Quorum`类型：

![img](https://img-blog.csdnimg.cn/10c2a20cf74049d292998c2d4110ca2d.png)

在任意控制台查看队列：

![img](https://img-blog.csdnimg.cn/5a1dc2943c014fe78d02ceac9cc49fa0.png)

可以看到，仲裁队列的 + 2字样。代表这个队列有2个镜像节点。

若要测试，可以参照之前的普通集群、镜像集群做测试，查看数据共享以及宕机后队列是否可用。

#### 20.6.7 集群扩容

因为仲裁队列默认的镜像数为`5`。如果你的集群有`7`个节点，那么镜像数肯定是`5`，而我们集群只有`3`个节点，因此镜像数量就是`3`。为了达到这个效果，我们需要先做集群扩容然后进行仲裁队列副本扩容即可，先做集群节点扩容：

1. 开启一个新的`MQ`容器，不用指定配置文件，等等可以使用`rabbitmqctl`将其拉入集群，但是前提是得在同一个网络，所以这里需要使用`--net`，除此之外还要配置下`cookie`，否则无法跟家族成员进行通信

   ```
   docker run -d --net mq-net \
   -v ${PWD}/.erlang.cookie:/var/lib/rabbitmq/.erlang.cookie \
   -e RABBITMQ_DEFAULT_USER=admin \
   -e RABBITMQ_DEFAULT_PASS=admin \
   --name mq4 \
   --hostname mq4 \
   -p 8074:5672 \
   -p 8084:15672 \
   rabbitmq:3.8-management
   ```

2. 进入`mq4`控制台

   ```shell
   docker exec -it mq4 bash
   ```

3. 停止`mq4`进程

   ```sh
   rabbitmqctl stop_app
   ```

4. 重置RabbitMQ中的数据：

   ```sh
   rabbitmqctl reset
   ```


5. 加入`mq`集群：【记得之前要是进行了让`mq1`宕机的测试，需要先启动`mq1`，否则会一直启动报错】

   ```sh
   rabbitmqctl join_cluster rabbit@mq1
   ```

6. 启动`mq4`

   ```
   rabbitmqctl start_app
   ```

7. 观察`mq1 web`控制台：可以看到扩容成功

   ![img](https://img-blog.csdnimg.cn/065a6985336342e897a28567fa63c196.png)

#### 20.6.8 仲裁队列副本扩容

我们先查看下`quorum.queue`这个队列目前的副本情况，进入`mq1`容器：

```sh
docker exec -it mq1 bash
```

执行命令：

```sh
rabbitmq-queues quorum_status "quorum.queue"
```

结果，可以看到只有`mq1 mq2 mq3`，没有刚刚新加入的节点`mq4`作为镜像副本：

![img](https://img-blog.csdnimg.cn/83361db1250346fcbaf969d69e6a8fb3.png)

现在，我们让`mq4`也加入进来：

```sh
rabbitmq-queues add_member "quorum.queue" "rabbit@mq4"
```

结果：

![img](https://img-blog.csdnimg.cn/22ea761cb255440da56b87892921d961.png)

再次查看：

```sh
rabbitmq-queues quorum_status "quorum.queue"
```

![img](https://img-blog.csdnimg.cn/0130618fdd6f40c197f0b9dacb6e5e40.png)

查看控制台，发现`quorum.queue`的镜像数量也从原来的` +2 `变成了` +3`：

![img](https://img-blog.csdnimg.cn/f2e5290383a449a7936a426479853eaa.png)

在`Java`中也同样可以创建仲裁队列，使用`quorum()`即可。

```java
@Bean
public Queue quorumQueue() {
    return QueueBuilder
        .durable("quorum.queue") // 持久化
        .quorum() // 仲裁队列
        .build();
}
```

需要连接时，在配置文件进行如下配置即可

```yaml
spring:
  rabbitmq:
    addresses: 192.168.56.1:8071, 192.168.56.1:8072, 192.168.56.1:8073
    username: admin
    password: admin
    virtual-host: /
```

到这里，整个`MQ`集群就学习完毕啦~:smiley:
