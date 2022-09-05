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