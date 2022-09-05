## 19. 多级缓存

### 19.1 为什么要实现多级缓存？

之前学习的`Redis`分步缓存已经完成了高并发【主从复制】、高可用【包括持久化、分片存储、哨兵模式】的需求，还需要多级缓存吗？那就得先看分布式缓存是否能够承载得起像淘宝、京东这些访问量过亿级得项目了，显然光靠`Redis`是满足不了这样得需求的。

**从传统缓存解决方案说起：**

无论你是单节点的`Redis`还是高可用高并发的`Redis`集群，一般都采用传统的缓存策略：即发送请求到达`Tomcat`服务器之后，先查询`Redis`，如果`Redis`没有命中则查询数据库。这样做大大降低了`MySQL`数据库的压力。

![img](https://img-blog.csdnimg.cn/82a99692dd1642f2884d8f35736f1a92.png)

这样咋一看没什么问题，但是再定睛观察，你会发现，所有的请求第一站都是到达服务器，我们现在常用的就是`Tomcat`服务器，如此一来**服务器就会成为整个系统的性能瓶颈**。因为第一站就是服务器，所以如果服务器崩了的话，也就无从访问后面的`Redis`、`MySQL`数据库了。

第二点我们就看到`Redis`，我们知道`Redis`中的数据是会过期的，如果某一时刻有大批量的数据过期了，那么就会对数据库产生巨大的冲击。

以上就是传统缓存的解决方案。虽然加了`Redis`但是依然抵挡不住亿级请求。能撑得住上亿请求的只能靠多级缓存解决方案。

**多级缓存解决方案：**

多级缓存就是充分利用请求处理的每一个环节，在这每一个环节中都添加缓存，从而减轻服务器的压力，`Redis`的压力、`MySQL`数据库的压力从而大大提升服务性能：

1. 首先用户发出请求，第一站肯定是通过浏览器，所以可以在浏览器做缓存，缓存一些静态资源。
2. 然后第二站，我们并不像传统的缓存策略直接访问服务器，而是访问`Nginx`充当反向代理的角色。
3. 反向代理过来我们仍然不访问服务器，而是访问搭建的`Nginx`集群，该集群为本地缓存。这里的`Nginx`服务不再是一个**反向代理服务器**，而是一个编写**业务的Web服务器了**。
4. `Nginx`本地缓存集群没有数据未命中就到`Redis`缓存集群中找。
5. `Redis`集群数据未命中才到服务器，服务器还做了进程缓存集群。这个进程缓存其实就是`JVM`进程缓存。
6. 进程缓存数据未命中最后才到数据库，数据库还可以做集群。

这就形成了：用户请求 ---> 浏览器缓存 ----> `Nginx`集群反向代理 ---> `Nginx`集群本地缓存 ---> `Redis`集群缓存 ---> 服务器进程缓存集群 ---> 数据库集群

![img](https://img-blog.csdnimg.cn/9db2e4d863cc40f5a9d97fea59e5976a.png)

- 从上可知，除了之前学习`Redis`跟`MySQL`做的集群之外，我们还可以在`Nginx`中编写业务，实现`Nginx`本地查询而且可以将编写业务实现本地查询的`Nginx`做集群。如何在`Nginx`编写业务，就会用到`OpenResty`框架结合`Lua`语言。
- 除此之外，我们还可以在服务器中实现`JVM`进程缓存

关于缓存，一般我们会将缓存分为两类：

- 分布式缓存，例如`Redis`：
  - 优点：存储容量更大、可靠性更好、可以在集群间共享
  - 缺点：访问缓存**有网络开销**
  - 场景：缓存数据量较大、可靠性要求较高、需要在集群间共享
- 进程本地缓存，例如`HashMap、GuavaCache`：
  - 优点：读取本地内存，**没有网络开销**，速度更快
  - 缺点：存储容量有限、可靠性较低、无法共享【正因为此通常我们会在`Nginx`设置负载均衡策略为`iphash`】
  - 场景：性能要求较高，缓存数据量较小

为了学习多级缓存，所以需要先实现商品查询的业务。

### 19.2 多级缓存的案例搭建

1. 在`CentOS`中的`Docker`安装`MySQL`

   ```shell
   docker pull mysql:8.0.28
   cd /tmp
   mkdir mysql
   cd mysql
   
   docker run -p 3307:3306 --name mysql -v $PWD/conf:/etc/mysql/conf.d -v $PWD/logs:/logs -v $PWD/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 --privileged -d mysql:8.0.28
   
   touch /tmp/mysql/conf/my.cnf
   --->
   [mysqld]
   skip-name-resolve
   character_set_server=utf8
   datadir=/var/lib/mysql
   server-id=1000
   
   docker restart mysql
   
   设置下，此时本地 Navicat 才能访问：
   docker -it exec mysql bash
   mysql -u root -p
   use mysql
   delete from user where host="%" and user="root";
   # 设置允许使用root用户访问数据库的主机名称，%表示能使用所有的主机使用root用户访问
   update user set host = '%' where user = 'root';
   # 设置root用户密码，mysql_native_password表示密码认证的插件
   alter user 'root'@'%' identified with mysql_native_password by '123456';
   # 立即生效
   FLUSH PRIVILEGES;
   
   我这里是 VirtualBox NAT 记得做下端口映射，然后使用 Windows Navicat 连接即可
   ```

2. 导入`sql`

   ```sql
   create database item;
   
   use item;
   
   SET NAMES utf8mb4;
   SET FOREIGN_KEY_CHECKS = 0;
   
   -- ----------------------------
   -- Table structure for tb_item
   -- ----------------------------
   DROP TABLE IF EXISTS `tb_item`;
   CREATE TABLE `tb_item`  (
     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
     `title` varchar(264) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品标题',
     `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
     `price` bigint(20) NOT NULL COMMENT '价格（分）',
     `image` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
     `category` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类目名称',
     `brand` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
     `spec` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格',
     `status` int(1) NULL DEFAULT 1 COMMENT '商品状态 1-正常，2-下架，3-删除',
     `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
     `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
     PRIMARY KEY (`id`) USING BTREE,
     INDEX `status`(`status`) USING BTREE,
     INDEX `updated`(`update_time`) USING BTREE
   ) ENGINE = InnoDB AUTO_INCREMENT = 50002 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品表' ROW_FORMAT = COMPACT;
   
   -- ----------------------------
   -- Records of tb_item
   -- ----------------------------
   INSERT INTO `tb_item` VALUES (10001, 'RIMOWA 21寸托运箱拉杆箱 SALSA AIR系列果绿色 820.70.36.4', 'SALSA AIR', 16900, 'https://m.360buyimg.com/mobilecms/s720x720_jfs/t6934/364/1195375010/84676/e9f2c55f/597ece38N0ddcbc77.jpg!q70.jpg.webp', '拉杆箱', 'RIMOWA', '{\"颜色\": \"红色\", \"尺码\": \"26寸\"}', 1, '2019-05-01 00:00:00', '2019-05-01 00:00:00');
   INSERT INTO `tb_item` VALUES (10002, '安佳脱脂牛奶 新西兰进口轻欣脱脂250ml*24整箱装*2', '脱脂牛奶', 68600, 'https://m.360buyimg.com/mobilecms/s720x720_jfs/t25552/261/1180671662/383855/33da8faa/5b8cf792Neda8550c.jpg!q70.jpg.webp', '牛奶', '安佳', '{\"数量\": 24}', 1, '2019-05-01 00:00:00', '2019-05-01 00:00:00');
   INSERT INTO `tb_item` VALUES (10003, '唐狮新品牛仔裤女学生韩版宽松裤子 A款/中牛仔蓝（无绒款） 26', '韩版牛仔裤', 84600, 'https://m.360buyimg.com/mobilecms/s720x720_jfs/t26989/116/124520860/644643/173643ea/5b860864N6bfd95db.jpg!q70.jpg.webp', '牛仔裤', '唐狮', '{\"颜色\": \"蓝色\", \"尺码\": \"26\"}', 1, '2019-05-01 00:00:00', '2019-05-01 00:00:00');
   INSERT INTO `tb_item` VALUES (10004, '森马(senma)休闲鞋女2019春季新款韩版系带板鞋学生百搭平底女鞋 黄色 36', '休闲板鞋', 10400, 'https://m.360buyimg.com/mobilecms/s720x720_jfs/t1/29976/8/2947/65074/5c22dad6Ef54f0505/0b5fe8c5d9bf6c47.jpg!q70.jpg.webp', '休闲鞋', '森马', '{\"颜色\": \"白色\", \"尺码\": \"36\"}', 1, '2019-05-01 00:00:00', '2019-05-01 00:00:00');
   INSERT INTO `tb_item` VALUES (10005, '花王（Merries）拉拉裤 M58片 中号尿不湿（6-11kg）（日本原装进口）', '拉拉裤', 38900, 'https://m.360buyimg.com/mobilecms/s720x720_jfs/t24370/119/1282321183/267273/b4be9a80/5b595759N7d92f931.jpg!q70.jpg.webp', '拉拉裤', '花王', '{\"型号\": \"XL\"}', 1, '2019-05-01 00:00:00', '2019-05-01 00:00:00');
   
   -- ----------------------------
   -- Table structure for tb_item_stock
   -- ----------------------------
   DROP TABLE IF EXISTS `tb_item_stock`;
   CREATE TABLE `tb_item_stock`  (
     `item_id` bigint(20) NOT NULL COMMENT '商品id，关联tb_item表',
     `stock` int(10) NOT NULL DEFAULT 9999 COMMENT '商品库存',
     `sold` int(10) NOT NULL DEFAULT 0 COMMENT '商品销量',
     PRIMARY KEY (`item_id`) USING BTREE
   ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;
   
   -- ----------------------------
   -- Records of tb_item_stock
   -- ----------------------------
   INSERT INTO `tb_item_stock` VALUES (10001, 99996, 3219);
   INSERT INTO `tb_item_stock` VALUES (10002, 99999, 54981);
   INSERT INTO `tb_item_stock` VALUES (10003, 99999, 189);
   INSERT INTO `tb_item_stock` VALUES (10004, 99999, 974;
   INSERT INTO `tb_item_stock` VALUES (10005, 99999, 18649);
   
   SET FOREIGN_KEY_CHECKS = 1;
   ```

   一张商品表，一张库存表：

   - `tb_item`：商品表，包含商品的基本信息
   - `tb_item_stock`：商品库存表，包含商品的库存信息

   ![img](https://img-blog.csdnimg.cn/4642ce9ebd874d4c94f54afb2c74ad64.png)

3. 数据库准备完毕接下来就是写代码了，新建一个`SpringBoot`工程`06-multi-cache-demo`，导入依赖如下：

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
       <artifactId>multi-cache-demo</artifactId>
       <version>0.0.1-SNAPSHOT</version>
       <name>06-multi-cache-demo</name>
       <description>06-multi-cache-demo</description>
       <properties>
           <java.version>1.8</java.version>
       </properties>
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version>8.0.28</version>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
           </dependency>
           <dependency>
               <groupId>com.baomidou</groupId>
               <artifactId>mybatis-plus-boot-starter</artifactId>
               <version>3.5.2</version>
           </dependency>
           <dependency>
               <groupId>com.github.ben-manes.caffeine</groupId>
               <artifactId>caffeine</artifactId>
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

4. 导入必要的静态资源`resource/static/`

5. 修改配置文件`application.yml`：

   ```yaml
   server:
     port: 8081
   spring:
     application:
       name: item-service
     datasource:
       driver-class-name: com.mysql.cj.jdbc.Driver
       url: jdbc:mysql://192.168.56.1:3307/item?useSSL=false&serverTimezone=Asia/Shanghai
       username: root
       password: 123456
   mybatis-plus:
     type-aliases-package: com.kk.cache.pojo
     configuration:
       map-underscore-to-camel-case: true
     global-config:
       db-config:
         update-strategy: not_null
         table-prefix: tb_
         id-type: auto
   logging:
     level:
       com.kk: debug
     pattern:
       dateformat: HH:mm:ss:SSS
   ```

6. 创建`pojo.Item`商品类

   ```java
   package com.kk.cache.pojo;
   
   import com.baomidou.mybatisplus.annotation.TableField;
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   import java.util.Date;
   
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class Item {
       private Long id;//商品id
       private String name;//商品名称
       private String title;//商品标题
       private Long price;//价格（分）
       private String image;//商品图片
       private String category;//分类名称
       private String brand;//品牌名称
       private String spec;//规格
       private Integer status;//商品状态 1-正常，2-下架
       private Date createTime;//创建时间
       private Date updateTime;//更新时间
       @TableField(exist = false)
       private Integer stock;
       @TableField(exist = false)
       private Integer sold;
   }
   ```

7. 创建商品库存类`pojo.ItemStock`：

   ```java
   package com.kk.cache.pojo;
   
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class ItemStock {
       private Long id; //商品id
       private Integer stock; //商品库存
       private Integer sold; //商品销量
   }
   ```

8. 编写`Dao`层`mapper.ItemMapper`：

   ```java
   package com.kk.cache.mapper;
   
   import com.baomidou.mybatisplus.core.mapper.BaseMapper;
   import com.kk.cache.pojo.Item;
   
   @Mapper
   public interface ItemMapper extends BaseMapper<Item> {
   }
   ```

9. 编写`Dao`层`mapper.ItemStockMapper`：

   ```java
   package com.kk.cache.mapper;
   
   import com.baomidou.mybatisplus.core.mapper.BaseMapper;
   import com.kk.cache.pojo.ItemStock;
   
   @Mapper
   public interface ItemStockMapper extends BaseMapper<ItemStock> {
   }
   ```

10. 编写`service`层接口`service.ItemService`：

    ```java
    package com.kk.cache.service;
    
    import com.baomidou.mybatisplus.extension.service.IService;
    import com.kk.cache.pojo.Item;
    
    public interface ItemService extends IService<Item> {
        //存放商品时，需要修改库存，所以这里需要自定义一个存储商品的方法
        public abstract void saveItem(Item item);
    }
    
    ```

11. 编写`service`层接口`service.ItemStockService`：

    ```java
    package com.kk.cache.service;
    
    import com.baomidou.mybatisplus.extension.service.IService;
    import com.kk.cache.pojo.ItemStock;
    
    public interface ItemStockService extends IService<ItemStock> {
    }
    ```

12. 编写`service`层实现类`service.impl.ItemServiceImpl`：

    ```java
    package com.kk.cache.service.impl;
    
    import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
    import com.kk.cache.mapper.ItemMapper;
    import com.kk.cache.pojo.Item;
    import com.kk.cache.pojo.ItemStock;
    import com.kk.cache.service.ItemService;
    import com.kk.cache.service.ItemStockService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    
    @Service
    public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {
    
        @Autowired
        private ItemStockService itemStockService;
    
        @Override
        @Transactional
        public void saveItem(Item item) {
            //存放商品
            this.save(item);
            //存放商品的同时需要存放商品库存 ---> 跟 pojo 对应上
            ItemStock itemStock = new ItemStock();
            itemStock.setId(item.getId());
            itemStock.setStock(item.getStock());
            itemStock.setSold(item.getSold());
            itemStockService.save(itemStock);
        }
    }
    ```

13. 编写`service`层实现类`service.impl.ItemStockServiceImpl`：

    ```java
    package com.kk.cache.service.impl;
    
    import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
    import com.kk.cache.mapper.ItemStockMapper;
    import com.kk.cache.pojo.ItemStock;
    import com.kk.cache.service.ItemStockService;
    import org.springframework.stereotype.Service;
    
    @Service
    public class ItemStockServiceImpl extends ServiceImpl<ItemStockMapper, ItemStock> implements ItemStockService {
    }
    ```

14. 这里为了简单，将`ItemController ItemStockController`统一编写`controller`层实现类`controller.ItemController`：

    ```java
    package com.kk.cache.controller;
    
    import com.kk.cache.pojo.Item;
    import com.kk.cache.pojo.ItemStock;
    import com.kk.cache.service.ItemService;
    import com.kk.cache.service.ItemStockService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    
    @RestController
    @RequestMapping(value = "/item")
    public class ItemController {
        @Autowired
        private ItemService itemService;
    
        @Autowired
        private ItemStockService itemStockService;
    
        /**
         * 获取指定的商品信息
         * 需要先判断下商品是否已经被删除
         *
         * @param id
         * @return
         */
        @GetMapping("/{id}")
        public Item findById(@PathVariable("id") Long id) {
            ItemStock itemStock = itemStockService.getById(id);
    		if (itemStock == null) itemStock = new ItemStock(id, 0, 0);
    		Item item = itemService.query().ne("status", 3).eq("id", id).one();
    		if (item != null) {
                item.setStock(itemStock.getStock());
                item.setSold(itemStock.getSold());
            }
    		return item;
        }
    
        /**
         * 存储商品信息
         *
         * @param item
         */
        @PostMapping
        public void saveItem(@RequestBody Item item) {
            itemService.saveItem(item);
        }
    
        /**
         * 更新商品信息
         *
         * @param item
         */
        @PutMapping
        public void updateItem(@RequestBody Item item) {
            itemService.updateById(item);
        }
    
        /**
         * 删除商品信息
         * 不是真的物理删除，而是逻辑删除，更新状态 3 意为删除
         *
         * @param id
         */
        @DeleteMapping("/{id}")
        public void deleteById(@PathVariable("id") Long id) {
            itemService.update().set("status", 3).eq("id", id).update();
        }
    
        /**
         * 更新商品库存信息
         *
         * @param itemStock
         */
        @PutMapping(value = "/stock")
        public void updateStock(@RequestBody ItemStock itemStock) {
            itemStockService.updateById(itemStock);
        }
    
    
        /**
         * 查询指定的商品库存信息
         *
         * @param id
         * @return
         */
        @GetMapping("/stock/{id}")
        public ItemStock findStockById(@PathVariable("id") Long id) {
            return itemStockService.getById(id);
        }
    
    }
    ```

15. 由于前端展示的是商品列表，商品因为可能有好多，所以自然而然得分页展示，后端传递给前端的分页信息需要包装成一个类，这里我们创建一个分页存储类`pojo.PageDTO`，`DTO`就是数据传输对象的意思：

    这样就能将`total`跟当前页的所要展示的商品返还给前端了。可以看下前端的代码，是用`vue`些的。

    ```java
    package com.kk.cache.pojo;
    
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    
    import java.util.List;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class PageDTO {
        private Long total; //展示商品总数
        private List<Item> list; //展示的商品
    }
    ```

16. 实现分页查询，在`ItemController`中添加方法：`queryItemPage()`，路由为`/list`，注意这里的逻辑判断，源代码给的是没有的。

    ```java
    /**
     * 使用 MyBatisPlus 自带的分页功能分页查询商品
     * 根据前端传递的 page【当前页】 + size【每页显示数量】 获取商品集合，原理就是 (page - 1) * size
     * 这里还是用到了 stream 流操作 + lambda 表达式，这里使用 peek 表示为每个元素添加操作
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/list")
    public PageDTO queryItemPage(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "5") Integer size) {
        Page<Item> itemPage = itemService.query().ne("status", 3).page(new Page<>(page, size));
        List<Item> list = itemPage.getRecords().stream().peek(item -> {
            ItemStock itemStock = itemStockService.getById(item.getId());
    		if (itemStock != null) {
    			item.setStock(itemStock.getStock());
                item.setSold(itemStock.getSold());
    		} else {
    			item.setStock(0);
    			item.setSold(0);
    		}
        }).collect(Collectors.toList());
        Long total = itemPage.getTotal();
        return new PageDTO(total, list);
    }

17. 你以为到这里就结束了吗？其实还没有，因为每次请求页面都需要拦截看是否需要分页，所以我们得添加一个拦截器`configuration.MyConfiguration`，使用`MyBatisPlus`自带的拦截器即可，所以这里直接引用：

    ```java
    package com.kk.cache.configuration;
    
    import com.baomidou.mybatisplus.annotation.DbType;
    import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
    import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    
    @Configuration
    public class MyConfiguration {
        @Bean
        public MybatisPlusInterceptor mybatisPlusInterceptor() {
            MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
            mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
            return mybatisPlusInterceptor;
        }
    }

18. 启动类`Application.java`如下：

    ```java
    package com.kk.cache;
    
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    
    @SpringBootApplication
    public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }
    ```

19. 启动测试，检验系统是否正常，系统端口按照配置为`8081`：访问http://localhost:8081

    ![img](https://img-blog.csdnimg.cn/9d5b2cffc41e4904ae218ccbe527d6b6.png)

20. 商品查询会跳转到具体的购物页面，与上图商品数据管理的页面是分离的。

    ![img](https://img-blog.csdnimg.cn/fb1dcbb0acb54d99b2edce755daf8cc5.png)

    我们需要准备一个用于反向代理的`Nginx`服务器。如下图，将静态资源放到`Nginx`目录中，页面需要的数据通过`Ajax`技术向服务端即`Nginx`业务集群查询。

    ![img](https://img-blog.csdnimg.cn/ab7dab5f0cda495a98e21e13f08748ab.png)

21. 将`Ngixn`中的配置文件修改下，然后将其拷贝到一个非中文目录下，使用`Dos`命令`start nginx.exe`运行`Nginx`，然后访问：http://localhost/item.html?id=10001即可，可以看到一个假数据构成的页面展示

22. 我们可以看到页面是假数据展示的，所以需要通过向服务器发送`Ajiax`请求，从而查询商品数据，打开浏览器控制台可以看到页面有发起`Ajax`请求：

    ![img](https://img-blog.csdnimg.cn/ff35eb0568ad405bac0efb8d5e7ac619.png)

23. 而这个请求同样是监听`80`端口，所以被当前的`Nginx`反向代理了，查看`Nginx`的`conf`目录下的`nginx.conf`文件：

    ![img](https://img-blog.csdnimg.cn/4d3ae2f4acbe442ba68d9282b7405d91.png)

    关键配置如下：还可以继续添加集群节点：`server 192.168.56.1:8082;server 192.168.56.1:8083 `。可以看到`Windows`的`Nginx`监听了`80`端口，所以我们可以通过`url`地址：http://localhost/item.html?id=10001访问到静态资源。

    ![img](https://img-blog.csdnimg.cn/cc432359cfaf443e82f290ab3d6f44c7.png)

    而当我们发送的请求为：http://localhost/api/item时，会去找`Nginx`业务集群：目标服务器为`192.168.56.1:8082`

    ```nginx
    
    #user  nobody;
    worker_processes  1;
    
    events {
        worker_connections  1024;
    }
    
    http {
        include       mime.types;
        default_type  application/octet-stream;
    
        sendfile        on;
        #tcp_nopush     on;
        keepalive_timeout  65;
    
        upstream nginx-cluster{
            server 192.168.56.1:8082;
        }
        server {
            listen       80;
            server_name  localhost;
    
    		location /api {
                proxy_pass http://nginx-cluster;
            }
    
            location / {
                root   html;
                index  index.html index.htm;
            }
    
            error_page   500 502 503 504  /50x.html;
            location = /50x.html {
                root   html;
            }
        }
    }
    ```

    具体流程如下：

    ![img](https://img-blog.csdnimg.cn/f4bc97dbcf2a4796b8b7ce7c795d6a3c.png)

    上述就完成了整个架构的初步搭建，接下来要做的就是如何搭建`Nginx`业务集群、`Redis`缓存、进程缓存等等工作了。

### 19.3 `Lua`语法学习

这里突然插入`Lua`有点突然，不过没办法，因为要想实现`Nginx`中的业务代码我们使用的是`OpenResty`框架，它里面又很多写好的`Lua`库而且还给你自定义库，所以你得需要掌握一些关于`Lua`的基本语法。

`Nginx`业务是用`Lua`来进行编写的，就像我们在`Tomcat`服务器中用`Java`去写代码，在`Nginx`服务器中我们用`Lua`去写代码。为什么要用`Lua`别的不行吗？

不是不行，而是因为`Nginx`是基于`C`语言编写的，其插件扩展口不太友好，而`Lua`作为一种[语法糖](https://so.csdn.net/so/search?q=语法糖&spm=1001.2101.3001.7020)，恰好可以弥补这个缺点。所以我们可以利用`Lua`语法编写脚本充当`Nginx`插件，让部分业务在`Nginx`运行。

`Lua`是一种轻量小巧的脚本语言，用标准`C`语言编写并以源代码形式开放， 其设计目的是为了嵌入应用程序中，从而为应用程序提供灵活的扩展和定制功能。官网：https://www.lua.org/，`Lua`经常嵌入到`C`语言开发的程序中，例如游戏开发、游戏插件等。`Nginx`本身也是`C`语言开发，因此也允许基于`Lua`做拓展。

`CentOS`自带了`Lua`所以这里就无需安装了。

![img](https://img-blog.csdnimg.cn/007f8443d6714cb58a63e702bc4212b2.png)

学习使用，输出`Hello World!`：

1. `Linux`环境任意位置新建`hello.lua`文件：`touch hello.lua`

   ![img](https://img-blog.csdnimg.cn/0d911b32a08649a7a1004fcd89a5e070.png)

2. ```lua
   print("Hello World!");
   ```

3. 运行`lua`文件：`lua hello.lua`

   ![img](https://img-blog.csdnimg.cn/87ea580dd23b405d997a17e44a32e9f5.png)

**`Lua`常见数据类型：**

![img](https://img-blog.csdnimg.cn/8586042e92034755a7dd690c5a477ae0.png)

**`Lua`提供了`type()`函数用于判断一个变量的数据类型：**

![img](https://img-blog.csdnimg.cn/a8dbb4df88594d7b9e722449ca9e139f.png)

**`Lua`声明变量时要使用`local`关键字：**

```lua
-- 声明字符串，可以用单引号或双引号，
local str = 'hello'
-- 字符串拼接可以使用 ..
local str2 = 'hello' .. 'world'
-- 声明数字
local num = 21
-- 声明布尔类型
local flag = true
```

`Lua`中的`table`类型既可以作为数组，又可以作为`Java`中的`map`来使用。数组就是特殊的`table`，`key`是数组角标而已：

```lua
-- 声明数组 ，key为角标的 table
local arr = {'java', 'python', 'lua'}
-- 声明table，类似java的map
local map =  {name='Jack', age=21}
```

总体代码：

```lua
local str1 = "hello";
local str2 = "hello'..'world";
local num = 21;
local flag = true;
local arr = {"java", "pythone", "c", "c++", "lua"};
local map = {name="Jack", age=21};
print(str1);
print(str2);
print(num);
print(flag);
print(arr[1]);
print(map["name"]);
print(map.name);
```

**`Lua`循环：**

对于`table`，我们可以利用`for`循环来遍历。不过数组和普通`table`遍历略有差异。

遍历数组：

```lua
-- 声明数组 key为索引的 table
local arr = {'java', 'python', 'lua'}
-- 遍历数组
for index,value in ipairs(arr) do
    print(index, value) 
end
```

遍历普通`table`：

```lua
-- 声明map，也就是table
local map = {name='Jack', age=21}
-- 遍历table
for key,value in pairs(map) do
   print(key, value) 
end
```

**`Lua`条件控制：**

类似`Java`的条件控制，例如`if、else`语法：

```lua
if(布尔表达式)
then
   --[ 布尔表达式为 true 时执行该语句块 --]
else
   --[ 布尔表达式为 false 时执行该语句块 --]
end
----------------------------------------
local A = 123;
local B = 321;
if(A==123 and B==321)
then
    print("A == B");
else
    print("A != B");
end;
```

与`Java`不同，布尔表达式中的逻辑运算是基于英文单词：

![img](https://img-blog.csdnimg.cn/d96fb8278efb4b54a3c38ce3ec17a6f6.png)

**`Lua`函数：**

定义函数的语法：

```lua
function 函数名( argument1, argument2..., argumentn)
    -- 函数体
    return 返回值
end
```

例如，定义一个函数，用来打印数组：

```lua
function printArr(arr)
    for index, value in ipairs(arr) do
        print(index, value)
    end
end
local arr = {"java", "pythone", "c", "c++", "lua"};
printArr(arr);
```

例如：定义一个函数，用来打印`table`：

```lua
function printArr(map)
    if not map then
        print("Map 不能为空！");
    end;
    for key, value in pairs(map) do
        print(key, value)
    end;
end;
local map = {name="Jack", age=21};
printArr(map);
```

### 19.4 实现`Nginx`业务集群本地缓存

通常做`Nginx`业务编写，并不是直接用`Lua`上去就开干，而是使用`Nginx + LuaJIT`结合出来的`OpenResty`框架。以下是关于`OpenResty`的一些介绍：

`OpenResty®`是一个基于`Nginx`的高性能`Web`平台，用于方便地搭建能够处理超高并发、扩展性极高的动态 `Web`应用、`Web`服务和动态网关。具备下列特点：

- 具备`Nginx`的完整功能
- 基于`Lua`语言进行扩展，集成了大量精良的`Lua`库、第三方模块
- 允许使用`Lua`**自定义业务逻辑**、**自定义库**

官方网站： https://openresty.org/cn/

![img](https://img-blog.csdnimg.cn/18214f92e64543778f3b5416c637ea6d.png)

我们希望达到的多级缓存架构如图：

![img](https://img-blog.csdnimg.cn/01e81019a65148998e3f5ddb7b5ca6d3.png)

其中：

- `windows`上的`nginx`用来做反向代理服务，将前端的查询商品的`ajax`请求代理到`OpenResty`集群

- `OpenResty`集群用来编写多级缓存业务

#### 19.4.1 安装`OpenResty`

1. 首先要安装`OpenResty`的依赖开发库，执行命令：

   ```shell
   yum install -y pcre-devel openssl-devel gcc --skip-broken
   ```

2. 可以在你的`CentOS`系统中添加 `openresty` 仓库，这样就可以便于未来安装或更新我们的软件包（通过 `yum check-update` 命令）。运行下面的命令就可以添加我们的仓库：

   ```shell
   yum-config-manager --add-repo https://openresty.org/package/centos/openresty.repo
   ```

3. 如果提示说命令不存在，则运行：然后再重复上面的命令

   ```
   yum install -y yum-utils 
   yum-config-manager --add-repo https://openresty.org/package/centos/openresty.repo
   ```

4. 安装`OpenResty`软件包：

   ```shell
   yum install -y openresty
   ```

   默认情况下，`OpenResty`安装的目录是：`/usr/local/openresty`，如下图，并且可以看到里面的`nginx`目录，这也恰说明了`OpenResty`就是在`Nginx`基础上集成了一些`Lua`模块。

   ![img](https://img-blog.csdnimg.cn/8e213f2e907f4c829b375c0b84fbf5f2.png)

5. 安装`opm`工具，`opm`是`OpenResty`的一个管理工具，可以帮助我们安装一个第三方的`Lua`模块。如果你想安装命令行工具 `opm`，那么可以像下面这样安装 `openresty-opm` 包：

   ```shell
   yum install -y openresty-opm
   ```

6. 配置`Nginx`的环境变量

   ```shell
   vim /etc/profile
   export NGINX_HOME=/user/local/openresty/nginx
   export PATH=${NGINX_HOME}/sbin:$PATH
   ```

   ```shell
   source /etc/profile
   ```

7. `Nginx`的默认配置文件注释太多，影响后续我们的编辑，这里将`nginx.conf`中的注释部分删除，保留有效部分。修改`/usr/local/openresty/nginx/conf/nginx.conf`文件，内容如下：

   【**<font color="red">注：此时这个文件还没有跟`Windows Nginx`反向代理过来的`/api/item`进行结合，就是原本的样子</font>**】

   ```nginx
   #user  nobody;
   worker_processes  1;
   error_log  logs/error.log;
   
   events {
       worker_connections  1024;
   }
   
   http {
       include       mime.types;
       default_type  application/octet-stream;
       sendfile        on;
       keepalive_timeout  65;
   
       server {
           listen       8082;
           server_name  localhost;
           location / {
               root   html;
               index  index.html index.htm;
           }
           error_page   500 502 503 504  /50x.html;
           location = /50x.html {
               root   html;
           }
       }
   }
   ```

8. 启动和运行`OpenResty`，其实就是运行`Nginx`，运行方法跟在`windows`一样：

   ```shell
   # 启动nginx
   nginx
   # 重新加载配置
   nginx -s reload
   # 停止
   nginx -s stop
   ```

#### 19.4.2 反向代理流程

现在，商品详情页使用的是假的商品数据。不过在浏览器中，可以看到页面有发起`ajax`请求查询真实商品数据。这个请求如下：请求地址为`localhost`端口为`80`，然后就会被`windows`上安装的反向代理服务器`Nginx`接收到。然后将其代理给`Linux`的`OpenResty`。

![img](https://img-blog.csdnimg.cn/a2988749f08a4d0bb41830e6da841c18.png)

流程是这样子的：

1. `Windows`的反向代理服务器`Nginx`接收到请求，将该请求代理到`192.168.56.1`【图片是借来的，看看就好】的`8081`服务器。

   ![img](https://img-blog.csdnimg.cn/5eb01fb6de51407b885972d81eb0036a.png)

2. 我们需要在`OpenResty`中编写业务，查询商品数据并返回到浏览器。但是这次，我们先在`OpenResty`接收请求，返回假的商品数据。那么`OpenResty`获取到`Nginx`代理给它的请求呢？这就需要开启`OpenResty`的监听了。


#### 19.4.3 `OpenResty`监听请求

`OpenResty`的很多功能都依赖于其目录下的`Lua`库，需要在`Nginx.conf`中指定依赖库的目录，并导入依赖：

1. 添加对`OpenResty`的`Lua`模块加载：

   修改`/usr/local/openresty/nginx/conf/nginx.conf`文件，在其中的`http`下面，添加下面代码：

   ```nginx
   #lua 模块
   lua_package_path "/usr/local/openresty/lualib/?.lua;;";
   #c模块     
   lua_package_cpath "/usr/local/openresty/lualib/?.so;;";  
   ```

2. 监听`/api/item`路径：

   修改`/usr/local/openresty/nginx/conf/nginx.conf`文件，在`nginx.conf`的`server`下面，添加对`/api/item`这个路径的监听：

   ```nginx
   location  /api/item {
       # 默认的响应类型
       default_type application/json;
       # 响应结果由lua/item.lua文件来决定
       content_by_lua_file lua/item.lua;
   }
   ```

   这个监听，就类似于`SpringMVC`中的`@GetMapping("/api/item")`做路径映射。而`content_by_lua_file lua/item.lua`则相当于调用`item.lua`这个文件，执行其中的业务，把结果返回给用户。相当于`Java`中调用`service`。其实就是嗲用了`item.lua`脚本文件。

   整个的`OpenResty`整合的`Nginx`的配置文件`nginx.conf`如下：当访问`/api/item`时会访问到这里，并且调用`item.lua`脚本进行返回。

   ```nginx
   #user  nobody;
   worker_processes  1;
   error_log  logs/error.log;
   
   events {
       worker_connections  1024;
   }
   
   http {
       include       mime.types;
       default_type  application/octet-stream;
       sendfile        on;
       keepalive_timeout  65;
       lua_package_path "/usr/local/openresty/lualib/?.lua;;";
       lua_package_cpath "/usr/local/openresty/lualib/?.so;;";
       server {
           listen       8082;
           server_name  localhost;
           location / {
               root   html;
               index  index.html index.htm;
           }
           location  /api/item {
               default_type application/json;
               content_by_lua_file lua/item.lua;
           }
           error_page   500 502 503 504  /50x.html;
           location = /50x.html {
               root   html;
           }
       }
   }
   ```

3. 编写`Lua`脚本文件`item.lua`

   1. 在`/usr/loca/openresty/nginx`目录创建文件夹：`lua`

      ![img](https://img-blog.csdnimg.cn/840e6f93f1c64141b6a6960a0c4be07f.png)

   2. 在`/usr/loca/openresty/nginx/lua`文件夹下，新建文件：`item.lua`【这里制作的是假数据】

      ```lua
      ngx.say('{"id":10001,"name":"SALSA AIR","title":"RIMOWA 21寸托运箱拉杆箱 SALSA AIR系列果绿色 820.70.36.4","price":17900,"image":"https://m.360buyimg.com/mobilecms/s720x720_jfs/t6934/364/1195375010/84676/e9f2c55f/597ece38N0ddcbc77.jpg!q70.jpg.webp","category":"拉杆箱","brand":"RIMOWA","spec":"","status":1,"createTime":"2019-04-30T16:00:00.000+00:00","updateTime":"2019-04-30T16:00:00.000+00:00","stock":2999,"sold":31290}')
      ```

   3. 重新加载`Nginx`配置：

      ```nginx
      nginx -s reload
      ```

4. 记得启用服务器哦，然后重新测试访问下http://localhost/item?id=10001

   **<font color="red">注：因为这里是虚拟机，而且我的网络是`NAT`所以你的也是的话记得做个端口映射，将`8082`端口映射到`Windows`中去。</font>**

   确保启用了`Ngixn`业务服务器：

   ```shell
   nginx
   ps -ef | grep nginx
   ```

   ![img](https://img-blog.csdnimg.cn/bfabd9e5f86c480da0f8584e4960ec88.png)

   可以看到成功访问了！再次回顾下整个流程：

   ![img](https://img-blog.csdnimg.cn/f4bc97dbcf2a4796b8b7ce7c795d6a3c.png)

#### 19.4.4 请求参数处理

上述做的`OpenResty`返回的是假数据， 如果要返回真实数据，就需要根据用户访问的`id`来查询商品的信息，也就是说`Nginx`业务集群得先查询到数据库中的相关数据才可以。在这之前，得先解决一个问题，就是如何获取前端传递过来的商品`id`即请求参数呢？

`OpenResty`中提供了一些`API`用来获取不同类型的前端请求参数，我们通过这些`API`将其保存到`Lua`语法定义的变量之中： `ngx.req.get_headers()、ngx.req.get_uri_args()、ngx.req.get_post_args()、ngx.req.get_body_data()`

![img](https://img-blog.csdnimg.cn/e8b0406d4ed84895a90b5f3290d1a1a3.png)

通过发送的请求：http://localhost/api/item/id可以知道，商品`id`是通过路径占位符传递到`Nginx`的因此我们可以采用正则表达式匹配的方式来获取`id`：

1. 修改`OpenResty`中的`Nginx`配置文件`nginx.conf`通过正则表达式获取到前端传递的`id`：

   ```nginx
   location ~ /api/item/(\d+) {
       default_type application/json;
       content_by_lua_file lua/item.lua;
   }
   ```

2. 通过`Lua`变量获取传递进来的参数，匹配到的参数将会存放到`ngx.var`数组中，注意`Lua`中数组的下标是从`1`开始的：

   ```lua
   local id = ngx.var[1];
   ```

3. 通过`local id = ngx.var[1];`获取到的`id`将其拼接到`item.lua`中：

   ```lua
   local id = ngx.var[1];
   ngx.say('{"id":'..id..',"name":"SALSA AIR","title":"RIMOWA 21寸托运箱拉杆箱 SALSA AIR系列果绿色 820.70.36.4","price":17900,"image":"https://m.360buyimg.com/mobilecms/s720x720_jfs/t6934/364/1195375010/84676/e9f2c55f/597ece38N0ddcbc77.jpg!q70.jpg.webp","category":"拉杆箱","brand":"RIMOWA","spec":"","status":1,"createTime":"2019-04-30T16:00:00.000+00:00","updateTime":"2019-04-30T16:00:00.000+00:00","stock":2999,"sold":31290}')
   ```

4. 重新加载`OpenResty`中的`Nginx`：

   ```nginx
   nginx -s reload
   ```

5. 测试查看效果：http://localhost/item.html?id=188888888

   ![img](https://img-blog.csdnimg.cn/8b54a8e2b819443eb1cf3a629b3958f3.png)

现在我们获取到了商品`id`，我们需要通过这个`id`查询数据，因为我们目前只是搭建了`OpenResty`还没有建立缓存，并且也没有建立`Redis`缓存，所以我们这里先直接访问`Tomcat`服务器获取信息。

![img](https://img-blog.csdnimg.cn/2f66379c906b4193a8fe2caa2a86852f.png)

那么接下来要做的就是，在`Nginx`业务服务器中发送请求给`Tomcat`，根据商品`id`获取对应的商品信息：`Nginx`提供了内部`API`用于发送`http`请求：

```lua
local resp = ngx.location.capture("/path[请求路径]", {
	method = ngx.HTTP_GET, --请求方式
	args = {a=1, b=2},	--get请求方式的参数
})
```

返回的响应内容包括：

- `resp.status`：响应状态码
- `resp.header`：响应头
- `resp.body`：响应体即响应数据

因为我们需要访问的是`Tomcat`服务器，而这个`Tomcat`服务器跟`Nginx`不在同一个网络中，并且`/path`是不包含`ip、port`的所以需要编写一个`Server`来对这个路径进行反向代理。

```nginx
location /path {
    proxy_pass http://192.168.56.1:8081
}
```

原理类似下图：![img](https://img-blog.csdnimg.cn/35b54b8b467740cfba6b8efe460a8d73.png)

接下来就具体实际操作下：

1. 修改`OpenResty`中的配置文件，添加发往`Tomcat`的匹配路径：这样当调用`ngx.location.capture(/item)`就会代理到http://192.168.56.1:8081/item而不是`Nginx`自己的路径中。

   ```nginx
   location /item {
       proxy_pass http://192.168.56.1:8081
   }
   ```

2. 自定义发送`http`请求的工具解析返回的内容，当然你也可以直接写，只不过没那么优雅而已：我们知道在`OpenResty`启动时会加载两个目录的文件：

   ![img](https://img-blog.csdnimg.cn/756ce0bf12f041719d3d123cbcb1a8fe.png)

   所以我们将该工具放置到`/usr/local/openresty/lualib/common.lua`文件中：

   ```lua
   local function read_http(path, params)
       local resp = ngx.location.capture(path,{
           method = ngx.HTTP_GET,
           args = params,
       })
       if not resp then
           ngx.log(ngx.ERR, "http请求查询失败, path: ", path , ", args: ", args)
           ngx.exit(404)
       end
       return resp.body
   end
   -- export method
   local _M = {  
       read_http = read_http
   }  
   return _M
   ```

   这个工具将`read_http`函数封装到`_M`这个`table`类型的变量中，并且返回，这类似于导出。使用的时候，可以利用`require('common')`来导入该函数库，这里的`common`是函数库的文件名。

3. 实现商品查询，修改`item.lua`，使用刚刚的`read_http`工具类获取商品信息

   ```lua
   -- 引入自定义common工具模块，返回值是common中返回的 _M
   local common = require("common")
   -- 从 common中获取read_http这个函数
   local read_http = common.read_http
   -- 获取路径参数
   local id = ngx.var[1]
   -- 根据id查询商品
   local itemJSON = read_http("/item/".. id, nil)
   -- 根据id查询商品库存
   local itemStockJSON = read_http("/item/stock/".. id, nil)
   ```

   获取到商品跟库存的两个`JSON`格式的字符串，我们需要将这两个`JSON`格式的字符串拼接为同一个：需要先转化为`table`合并之后再转换为`JSON`格式字符串。

   ![img](https://img-blog.csdnimg.cn/7057b947e532446baf12c642dbd6aa15.png)

4. 使用`CJSON`转换`JSON`或者`table`，`CJSON`是`OpenResty`提供的用来处理`JSON`的序列化和反序列化。官方地址： https://github.com/openresty/lua-cjson/，使用前先引入模块：

   ```lua
   local cjson = require "cjson";
   ```

   序列化操作案例如下：

   ```lua
   local obj = {name="Jack", age=21};
   local json = cjson.encode(obj);
   print(obj);
   ```

   反序列化操作案例如下：

   ```lua
   local json = '{"name":"Jack", "age":21}';
   local obj = cjson.decode(obj);
   print(obj);
   ```

   将上述查询到的`itemJSON`和`itemStockJSON`转化为`table`格式然后进行拼接再转化为`JSON`格式：

   ```lua
   local cjson = require('cjson')
   local common = require(“common”)
   local read_http = common.read_http;
   local id = ngx.var[1];
   local itemJSON = read_http("/item/".. id, nil);
   local itemStockJSON = read_http("/item/stock/".. id, nil);
   local item = cjson.decode(itemJSON);
   local itemStock = cjson.decode(itemStockJSON);
   item.stock = itemStock.stock;
   item.sold = itemStock.sold;
   ngx.say(cjson.encode(item));

5. 重新加载`OpenResty Nginx`，观察效果，访问：http://localhost/item.html?id=10002，如果出错可以到`/usr/local/openresty/nginx/logs/error.log`查看错误日志，可以看到访问成功，一定要记得先配置好`nginx.conf`再写`Lua`脚本哦~

   配置文件如下：

   ```nginx
   #user  nobody;
   worker_processes  1;
   error_log  logs/error.log;
   
   events {
       worker_connections  1024;
   }
   
   http {
       include       mime.types;
       default_type  application/octet-stream;
       sendfile        on;
       keepalive_timeout  65;
       lua_package_path "/usr/local/openresty/lualib/?.lua;;";
       lua_package_cpath "/usr/local/openresty/lualib/?.so;;";  
       server {
           listen       8082;
           server_name  localhost;
           location / {
               root   html;
               index  index.html index.htm;
           }
           location ~ /api/item/(\d+) {
               default_type application/json;
               content_by_lua_file lua/item.lua;
           }
   		location /item {
   	   	 	proxy_pass http://192.168.56.1:8081;
   		}
           error_page   500 502 503 504  /50x.html;
           location = /50x.html {
               root   html;
           }
       }
   }
   ```

   ![img](https://img-blog.csdnimg.cn/20995c22893146d29a4c0f7d63b4dacb.png)

#### 19.4.5 更改`OpenResty`负载均衡策略：`iphash`改版

上述已经完成了从反向代理服务器`Nginx`再到`OpenResty`的`Nginx`业务服务器中，最后向`Tomcat`服务器发送请求，上述可以看到`Tomcat`是单机部署的，但是在实际开发过程中，`Tomcat`服务器一定是集群模式的【高并发高可用嘛】：

![](https://img-blog.csdnimg.cn/13d4f713ffa44aef8aa646bbfa75f64b.png)

因此作为由`Nginx`构成的业务集群，我们需要做负载均衡，这样才能向`Tomcat`集群发送请求。为了避免端口混乱：

1. 我这里将`Windows`跳转访问到`OpenResty`的地址的端口更改为`9091`，然后`nginx -s reload`
2. 其次我将`Linux`中的`OpenResty`的`Nginx`监听端口更改为`9091`，然后`nginx -s reload`
3. 完成后记得将`9091`从虚拟机`VirtualBox`开放出来否则`Windows`无法访问，将之前的`8081`改掉就行

然后就是对`OpenResty`访问`Tomcat`集群做负载均衡，默认的负载均衡规则时轮询模式，轮询的效率会跟着有无`JVM`进程缓存【日后肯定是要做`JVM`进程缓存的，只是现在还没做而已】而导致命中率低下，比如当你访问`/item/10001`时：

- 第一次会访问`8081`端口的`Tomcat`服务，会查询数据库，在该服务内部就形成了`JVM`进程缓存，所以就会访问`JVM`缓存【本地进程缓存】
- 第二次会访问`8082`端口的`Tomcat`服务，该服务内部没有`JVM`缓存（因为`JVM`缓存无法共享），会查询数据库
- ...

所以可以看到，访问每个服务器的第一次都会去查询数据库，之前几个服务器中做的本地进程缓存即`JVM`缓存一点效果都没有，缓存的命中率不是低不低的问题了，压根在第一次就命中不到。

那要如何解决这个本地进程缓存即`JVM`缓存没有解决的问题呢？很好办，你每次发送的请求若是同一个商品都去向同一个`Tomcat`服务器发送不就好了。这样`JVM`缓存在第二次`Tomcat`服务器被访问就一定可以访问得到了。

**具体怎么做呢？我们可以通过请求路径作为负载均衡的算法，`Nginx`已经实现了这一负载均衡策略，它会根据请求路径做一个`Hash`计算，然后将其`% Tomcat`服务器的数量。那么得到的余数肯定在`[0 - (Tomcat count - 1)]`之中，如此一来我们就可以通过余数【假设得到的余数为`n`】来访问第`n + 1`个服务器了。**

例如：

- 我们的请求路径是`/item/10001`
- `Tomcat`总数为`2`台`（8081、8082）`
- 对请求路径`/item/1001`做`hash`运算求余的结果为`1`
- 则访问第一个`Tomcat`服务，也就是`8081`

因为`Hash`值是根据请求路径计算出来的，所以只要请求路径不变`Hash`就不变，`Hash`不变访问到的服务器就不变，又因为请求路径跟`id`绑定一块，`id`变请求路径才会变，所以就是说只要`id`不变，访问的服务器就不会变，还是同一个。

**<font color="deepskyblue">实现：</font>**

其实这跟`Nginx`的负载均衡策略之一`iphash`没有什么区别，只不过`iphash`是根据`ip`来计算哈希的，所以要想通过`id`计算请求路径`hash`需要在配置文件做一些小小的改动：

修改`/usr/local/openresty/nginx/conf/nginx.conf`文件，实现基于`id`做`hash`计算，首先定义`Tomcat`集群的位置，然后将负载均衡策略设置为请求路径：

```nginx
upstream tomcat-cluster {
    hash $request_uri;
    server 192.168.56.1:8081;
    server 192.168.56.1:8082;
}
```

对整个`Tomcat`集群做反向代理，之前的代码是对一个，现在改成集群了：

将：

```nginx
location /item {
	proxy_pass http://192.168.56.1:8081;
}
```

更改为：

```nginx
location /item {
    proxy_pass http://tomcat-cluster;
}
```

整个配置文件`nginx.conf`为：

```nginx
#user  nobody;
worker_processes  1;
error_log  logs/error.log;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;
    lua_package_path "/usr/local/openresty/lualib/?.lua;;";
    lua_package_cpath "/usr/local/openresty/lualib/?.so;;";
    upstream tomcat-cluster {
        hash $request_uri;
        server 192.168.56.1:8081;
        server 192.168.56.1:8082;
    }
    server {
        listen       9091;
        server_name  localhost;
        location / {
            root   html;
            index  index.html index.htm;
        }
        location ~ /api/item/(\d+) {
            default_type application/json;
            content_by_lua_file lua/item.lua;
        }
		location /item {
	   		proxy_pass http://tomcat-cluster;
		}
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```

`OpenResty`重新加载配置文件`nginx.conf`：

```shell
nginx -s reload
```

拷贝一份`Tomcat`服务器将端口更改为`8082`：`VM Options: -Dserver.port=8082`，然后启动服务器。

![img](https://img-blog.csdnimg.cn/4236ac7b21994812accfdc5c686283b2.png)

访问：http://localhost/item/10001刷新`N`次，可以看到无论怎样都是访问的`8082`服务器：

![img](https://img-blog.csdnimg.cn/17e506ab6a3b4d89976ee8aea351301c.png)

访问：http://localhost/item/10002刷新`N`次，可以看到无论怎样都是访问的`8081`服务器：

![img](https://img-blog.csdnimg.cn/b8051399cd8d4bc58442002d60860754.png)

这样，我们就完成了让`OpenResty`的`Nginx`业务服务器基于请求路径的负载均衡策略。最终的其实就是这么一个设置：`hash $request_uri;`

```nginx
upstream tomcat-cluster {
	hash $request_uri;
	server 192.168.56.1:8081;
	server 192.168.56.1:8082;
}
```

#### 19.4.6 实现`Nginx`本地缓存

上面学习了这么多只是学习了整个流程，那么到底怎么做`Nginx`本地缓存呢？这就需要使用到`OpenResty`为`Nginx`提供的`shard dict`共享词典的功能了，共享词典可以让`Nginx`多个`worker`之间共享数据，实现本地缓存功能。

![img](https://img-blog.csdnimg.cn/5a27904b4acf434a866b5de004e53a9c.png)

具体操作：

1. 在`nginx.conf`的`http`下添加配置`lua_shared_dict`，表示开启共享词典即开启本地缓存，词典名称任意取，大小自定：

   ```nginx
   # 共享词典即本地缓存
   lua_shared_dict item_cache 150m;
   ```

2. 如何操作共享词典？

   ```lua
   -- 获取共享词典即本地缓存对象
   local item_cache = ngx.shared.item_cache;
   -- 存储数据，指定键值对并指定过期时间，单位为秒比如这里为 1000s，默认为 0 表示永不过期
   item_cache:set('key', 'value', 1000);
   -- 读取
   local val = item_cache:get('key');
   ```

3. 知道了如何开启共享词典并且如何操作共享词典之后，就可以在`item.lua`文件中实现本地缓存查询了，如果本地查询不到，按照架构图，将去`Redis`中查询，发送请求到`Redis`中查询我们还没有做，不过没关系，因为很简单，再然后如果`Redis`页查询不到，则会直接查询`Tomcat`服务器 ：

   ```lua
   -- 导入开启的共享词典，这里在 nginx.conf 设置的共享词典名称为：item_cache
   local item_cache = ngx.shared.item_cache;
   -- 封装查询函数【如果本地缓存没有就发送请求给 Redis，到 Redis 中查询】
   -- 参数为：key 表示查询的键，path 表示要请求的 Redis 请求路径，params 表示发送请求的参数, expire 到时设置本地缓存时数据的过期时间
   function read_data(key, expire, path, params)
       -- 查询本地缓存
       local val = item_cache:get(key);
       -- 如果没有查询到即 val 为 null ，则需要记录错误日志向 Redis 发送请求
       if not val then
           -- 记录没有本地缓存的日志
           ngx.log(ngx.ERR, "本地缓存查询失败，尝试查询 Redis，key: ", key);
           -- 向 Redis 发送请求，请求查询，这里查询 Redis 已经封装到了 read_redis() 函数中，传递 ip、port、key 即可
           val = read_redis("127.0.0.1", 6379, key);
           -- 如果 Reids 中也没有命中缓存，则通过 http 请求通过 Tomcat 服务器查询数据库
           if not val then
               ngx.log(ngx.ERR, "Redis 查询失败，尝试查询 http：key: ", key);
               val = read_http(path, params);
           end;
       end;
       -- 此时获取到数据，先将数据，先将数据写入到本地缓存中，然后反水数据
       item_cache:set(key, val, expire);
       return val;
   end;
   ```

4. 现在我们就写好了`read_data`查询本地缓存的业务代码了，修改`item.lua`中查询商品和库存的业务，使用刚刚写好的`read_data`函数：

   ![img](https://img-blog.csdnimg.cn/dfe39eedd2974a219eeb07fc058fc1a1.png)

   当进行第二次访问的时候，会更新过期时间，如果在过期时间内还没有二次访问，则`Nginx`本地缓存会将该缓存剔除，这里的缓存时间都是以`s`秒为单位的，`1800s`表示`30`分钟，`60s`表示`1`分钟，因为库存的更新频率是比较高的，如果缓存时间过程，可能会与数据库的查询较大，当然如果数据库的数据发生改变，我们需要做缓存同步，这个后面会学习到。

5. 这样我们其实就完成了本地缓存的业务编写了，只需将其整合到`item.lua`中即可，注意：`read_http`和`read_redis`表示从数据库跟`Redis`查询缓存，我们将其都存放在`lualib`文件夹中，有需要时直接导入获取即可：

   ```lua
   -- 导入 common 函数库中的：read_http read_redis
   local common = require('common');
   local read_http = common.read_http;
   local read_redis = common.read_redis;
   -- 导入 cjson 库，合并查询到的 itemJSON 以及 itemStockJSON
   local cjson = require('cjson');
   -- 导入本地词典
   local item_cache = ngx.shared.item_cache;
   -- 封装查询 Nginx 本地缓存的函数
   function read_data(key, expire, path, params)
       --获取共享词典本地缓存的数据
       local val = item_cache:get(key);
       if not val then
           ngx.log(ngx.ERR, "本地缓存查询失败，尝试查询Redis， key: ", key);
           val = read_redis("127.0.0.1", 6379, key);
           if not val then
               ngx.log(ngx.ERR, "redis查询失败，尝试查询http， key: ", key)
               val = read_http(path, params);
           end;
       end;
       item_cache:set(key, val, expire);
       return val;
   end;
   -- 获取请求路径传递的 id
   local id = ngx.var[1];
   -- 查询商品信息 key expire path params
   local itemJSON = read_data("item:id"..id, 1800, "/item/"..id, nil);
   -- 查询库存信息 key expire path params
   local itemStockJSON = read_data("item:stock:id"..id, 60, "/item/stock/"..id, nil);
   -- 将 JSON 数据转换为 table
   local item = cjson.decode(itemJSON);
   local itemStock = cjson.decode(itemStockJSON);
   -- 合并 table
   item.stock = itemStock.stock;
   item.sold = itemStock.sold;
   -- 将 table 转换为 JSON 数据返回
   ngx.say(cjson.encode(item));
   ```

   到这里，`Nginx`本地缓存的功能就实现好了。我们知道，当`Nginx`本地缓存没有命中数据时，就会到下一站的`Redis`中查询数据，所以下一站 —— `Redis`查询缓存，出发！

### 19.5 实现`Redis`缓存

在`19.4`小节中完成了`Nginx`反向代理到`OpenResty`的`Ngxin`业务集群的缓存操作`item.lua ---> read_data()`，当在`OpenResty`即`Nginx`本地缓存没有命中时，此时就会查询`Redis`缓存，看`Redis`中是否有数据。

所以我们需要整个查询`Redis`的缓存函数：`read_redis()`，我们将其编写在`lualib/common.lua`文件中，`OpenResty`已经封装好了`Redis`模块，我们直接拿来用即可：

```lua
-- 导入 Redis 因为 redis 文件在 resty 目录中，类似于 java 的包
 local redis = require('resty.redis');
-- 初始化 Redis 对象
local red = redis:new();
-- 设置 Redis 超时时间
red.set_timeputs(1000, 1000, 1000);
-- 查询 Redis 中的缓存
local function read_redis(ip, port, key)
    -- 连接 Redis
    local ok, err = red:connect(ip, port);
    if not ok then
        ngx.log(ngx.ERR, "连接 Redis 失败：", err);
        return nil;
    end;
    -- 连接 Redis 成功，查询缓存
    local resp, err = red:get(key);
    if not resp then
        ngx.log(ngx.ERR, "查询Redis失败: ", err, ", key = " , key)
    end;
    -- 当得到的数据为空报告信息即发送请求成功，但是 key 不存在
    if resp == ngx.null then
        resp = nil;
        ngx.log(ngx.ERR, "查询 Redis 数据为空，key = ", key);
    end;
    -- 断开 Redis 连接，这个是自定义的函数
    close_redis(red);
    return resp;
end;    
```

关闭`Redis`的`Lua`函数如下，关闭连接不是真的去把`Redis`关闭，因为读写需求很大，所以这里的做法是将其放入到`Redis`连接池中，不过也会做一个连接池的最大空闲时间，比如这里的设置，如果大于`10s`，则真正断开连接：

```lua
local function close_redis(red)
    -- 设置连接池连接的最大空闲时间，单位为毫秒，10s 没有人连接 Redis 则真正断开
    local pool_max_idle_time = 10000;
    -- 设置连接池大小
    local pool_size = 100;
    -- 设置保存连接
    local ok, err = red:set_keepalive(pool_max_idle_time, pool_size);
    if not ok then
        ngx.log(ngx.ERR, "放回 Redis 连接池失败: ", err)
    end;
end;
```

写完`read_redis`还需要将其导出，到时候给`item.lua`文件使用：

```lua
local _M = {
    read_http = read_http;
    read_redis = read_redis;
}
return _M;
```

整合上述，整个`Redis`查询如下，因为是放在`common.lua`文件的，所以干脆贴出整个`common.lua`的代码将之前写的发送`http`的函数也一并带上：

```lua
-- 导入 Redis 因为 redis 文件在 resty 目录中，类似于 java 的包
 local redis = require('resty.redis');
-- 初始化 Redis 对象
local red = redis:new();
-- 设置 Redis 超时时间
red.set_timeputs(1000, 1000, 1000);
local function close_redis(red)
    -- 设置连接池连接的最大空闲时间，单位为毫秒，10s 没有人连接 Redis 则真正断开
    local pool_max_idle_time = 10000;
    -- 设置连接池大小
    local pool_size = 100;
    -- 设置保存连接
    local ok, err = red:set_keepalive(pool_max_idle_time, pool_size);
    if not ok then
        ngx.log(ngx.ERR, "放回 Redis 连接池失败: ", err)
    end;
end;
-- 查询 Redis 中的缓存
local function read_redis(ip, port, key)
    -- 连接 Redis
    local ok, err = red:connect(ip, port);
    if not ok then
        ngx.log(ngx.ERR, "连接 Redis 失败：", err);
        return nil;
    end;
    -- 连接 Redis 成功，查询缓存
    local resp, err = red:get(key);
    if not resp then
        ngx.log(ngx.ERR, "查询Redis失败: ", err, ", key = " , key)
    end;
    -- 当得到的数据为空报告信息即发送请求成功，但是 key 不存在
    if resp == ngx.null then
        resp = nil;
        ngx.log(ngx.ERR, "查询 Redis 数据为空，key = ", key);
    end;
    -- 断开 Redis 连接，这个是自定义的函数
    close_redis(red);
    return resp;
end;
-- 发送 http 请求查询数据
local function read_http(path, params)
    -- 使用 ngx.location.capture 发送请求获取响应信息
    local resp = ngx.location.capture(path, {
        method = ngx.HTTP_GET;
        args = params;
    });
    if not resp then
        -- 记录错误信息，返回 404
		ngx.log(ngx.ERR, "http查询失败, path: ", path , ", args: ", args);
        ngx.exit(404);
	end;
    return resp.body;
end;    
-- 将 read_http 和 read_redis 方法导出
local _M = {
    read_http = read_http;
    read_redis = read_redis;
}
return _M;
```

现在，就实现了从`Ngixn`业务服务器中查询`Redis`缓存的功能，实现了多级缓存中的一环：

![img](https://img-blog.csdnimg.cn/f6e4d798752b4560a9db10f9bc8a07f5.png)

当请求进入`OpenResty`之后：

- 优先查询`Redis`缓存
- 如果`Redis`缓存未命中，再查询`Tomcat`

整个业务如下：

```lua
-- 导入 common 函数库中的：read_http read_redis
local common = require('common');
local read_http = common.read_http;
local read_redis = common.read_redis;
-- 导入 cjson 库，合并查询到的 itemJSON 以及 itemStockJSON
local cjson = require('cjson');
-- 导入本地词典
local item_cache = ngx.shared.item_cache;
-- 封装查询 Nginx 本地缓存的函数
function read_data(key, expire, path, params)
    --获取共享词典本地缓存的数据
    local val = item_cache:get(key);
    if not val then
        ngx.log(ngx.ERR, "本地缓存查询失败，尝试查询Redis， key: ", key);
        val = read_redis("127.0.0.1", 6379, key);
        if not val then
            ngx.log(ngx.ERR, "redis查询失败，尝试查询http， key: ", key)
            val = read_http(path, params);
        end;
    end;
    item_cache:set(key, val, expire);
    return val;
end;
-- 获取请求路径传递的 id
local id = ngx.var[1];
-- 查询商品信息 key expire path params
local itemJSON = read_data("item:id"..id, 1800, "/item/"..id, nil);
-- 查询库存信息 key expire path params
local itemStockJSON = read_data("item:stock:id"..id, 60, "/item/stock/"..id, nil);
-- 将 JSON 数据转换为 table
local item = cjson.decode(itemJSON);
local itemStock = cjson.decode(itemStockJSON);
-- 合并 table
item.stock = itemStock.stock;
item.sold = itemStock.sold;
-- 将 table 转换为 JSON 数据返回
ngx.say(cjson.encode(item));
```

#### 19.5.1 实现`Redis`缓存预热

`Redis`缓存查询是实现完了，但是现在有一个问题就是，`Redis`压根就没有数据呀，没有数据那查询`Redis`缓存还有啥意义呢？

当我们的整个系统刚开始运行的时候，因为还没有接收任何请求，所以`Redis`肯定是没有缓存数据的，因为这里是多级缓存的架构，能做成多级缓存那流量肯定是相当的大的了，所以如果直接启动整个系统，那么一时间大量查询商品的请求涌入过来，直奔数据库，就会给数据库造成很大的压力。这样子啥都不做，直接启动项目做我们称之为**冷启动**。

所以为了避免给数据库一下子接收许多请求，从而可能导致数据库崩溃的风险，所以在实际开发中，会利用大数据统计用户访问的热点数据，在整个系统启动的时候就将这些热点数据提前查询保存到`Redis`中，这就是**`Redis`的缓存预热**。

**<font color="deepskyblue">再次声明，这里是多级缓存，能够做多级缓存的肯定是流量相当大的，而且你不可能一样东西从一开始流量就非常巨大，所以在企业发展到有上亿流量的过程中，肯定会统计一些用户访问得最多的一些数据。就好比你有一家咖啡店，哪款咖啡卖得最火这个数据你作为老板你肯定是要知道也会知道的。</font>**这里也一样，在做成多级缓存的系统之后，之前统计到的哪些商品销量最好，最好的这些就是用户经常查询的，所以在系统启用时，就先将这些热点数据存储在`Redis`，这样就能防止一下子请求都打到数据库，导致出现数据库崩溃的风险。

因为这里我们只是学习，数据量很少，所以我们干脆在这里把所有的数据都先放入`Redis`缓存中。这里`Reids`我安装在`Docker`中，关于`Docker`如何安装`Redis`见`18.2`小节。

**<font color="red">注：这里只是模拟了单节点`Redis`的操作，实际开发中应该会将`Redis`做成集群，不过操作上没什么差别，只不过配置文件会有所变化而已。</font>**

安装完毕后，我们就需要导入数据了，这么重复性的工作肯定不能手动啊，那就需要使用`Java`查询数据库然后写入`Redis`中了，具体操作如下：

1. 引入依赖

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

2. 修改配置文件：

   ```yaml
   spring:
     redis:
       host: 192.168.150.101
       port: 6379
   ```

3. 因为`Redis`缓存预热需要在项目启动时就完成，所以先获取到`RedisTemplate`对象然后在启动时完成预热才行，那么就需要使用`InitializingBean`接口来实现，因为`InitializingBean`可以在对象被Spring创建并且成员变量全部注入后执行。然后查询全部数据库信息：

   ```java
   package com.kk.cache.handler;
   
   import com.alibaba.fastjson.JSON;
   import com.kk.cache.pojo.Item;
   import com.kk.cache.pojo.ItemStock;
   import com.kk.cache.service.ItemService;
   import com.kk.cache.service.ItemStockService;
   import org.springframework.beans.factory.InitializingBean;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.data.redis.core.RedisTemplate;
   import org.springframework.stereotype.Component;
   
   import java.util.List;
   
   @Component
   public class RedisHotHandler implements InitializingBean {
   
       @Autowired
       private RedisTemplate redisTemplate;
   
       @Autowired
       private ItemService itemService;
   
       @Autowired
       private ItemStockService itemStockService;
   
       //private static final ObjectMapper MAPPER = new ObjectMapper();
   
       @Override
       public void afterPropertiesSet() throws Exception {
           //查询商品信息 ---> 将其放入缓存，合并的事情查询 Redis 缓存的时候就做了，这里不用重复，然后放入 Redis 缓存中
           List<Item> itemList = itemService.list();
           for (Item item : itemList) {
               //转换为 JSON 数据，key 为 item:id，你使用 fastjson 或者 jackson 都可以
               String json = JSON.toJSONString(item);
               // String json = MAPPER.writeValueAsString(item);
               redisTemplate.opsForValue().set("item:id" + item.getId(), json);
           }
           //查询库存信息，放入 Redis 缓存中
           List<ItemStock> itemStocks = itemStockService.list();
           for (ItemStock itemStock : itemStocks) {
               String json = JSON.toJSONString(itemStock);
               redisTemplate.opsForValue().set("item:stock:id" + itemStock.getId(), json);
           }
       }
   }
   ```

到这里，就完成了`Redis`缓存预热。

### 19.6 使用`Caffeine`实现本地进程缓存

从`Ngxin`反向代理到`OpenResty`再到`Redis`，如果缓存都没有命中那么就会发送请求到`Tomcat`，为了最大化实现缓存，我们当然也可以在`Tomcat`中做本地缓存，这就是本地进程缓存也叫`JVM`缓存，即真正的缓存到本地。

自己实现本地进程缓存即`JVM`缓存那是相当的麻烦，所以通常我们会借用与一些框架来实现本地进程缓存，通常我们会选择`Caffeine`框架。`Caffeine`是一个基于`Java8`开发的，提供了近乎最佳命中率的高性能的本地缓存库。目前`Spring`内部的缓存使用的就是`Caffeine`。`GitHub`地址：https://github.com/ben-manes/caffeine

为什么选择使用`Caffeine`呢？

原因很简单，那就是因为**`Caffeine`**的性能非常好。下面就是官方给出的性能对比图：可以看到`Caffeine`的性能是有多给力。

![img](https://img-blog.csdnimg.cn/95211eeea1314035bff864547b7639a4.png)

作为一款优秀的缓存框架，那肯定就具有：存储缓存数据、查看缓存数据、清除缓存数据的功能了。如何使用呢？下面是简单的存储缓存书和使用缓存数据的案例：

1. 引入`Caffeine`依赖

   ```xml
   <dependency>
       <groupId>com.github.ben-manes.caffeine</groupId>
       <artifactId>caffeine</artifactId>
   </dependency>
   ```

2. 使用`Caffeine`：

   ```java
   package com.kk.cache;
   
   import com.github.benmanes.caffeine.cache.Cache;
   import com.github.benmanes.caffeine.cache.Caffeine;
   import org.junit.jupiter.api.Test;
   import org.springframework.boot.test.context.SpringBootTest;
   
   @SpringBootTest
   class ApplicationTests {
   
       @Test
       void contextLoads() {
           // 构建 Cache 对象
           Cache<String, String> cache = Caffeine.newBuilder().build();
           // 存储缓存数据
           cache.put("name", "Mike");
           // 查询缓存数据, getIfPresent 代表如果有就取出，没有返回 null
           String name = cache.getIfPresent("name");
           System.out.println("name = " + name);
           // 查询缓存数据还有另外一种方式：使用 get
           // 它跟 getIfPresent 的区别就是，如果查询到 null 会默认返回 Lambda 表达式中的值
           String defaultName = cache.get("defaultName", key -> {
               return "default Name is null";
           });
           System.out.println("default name = " + defaultName);
       }
   
   }
   ```

为了防止内存耗尽，所以基本上所有的缓存框架都会有清除缓存的功能，`Caffeine`也不例外，在`Caffeine`中，它提供了三种缓存清除策略：

- 基于时间的清除缓存策略

  这个很好理解，类比于`Redis`，在`Redis`缓存是有有效时间的，这里也是同样的意思，我们可以在创建缓存对象的时候统一设置缓存的有效时间。

  ```java
  Cache<String, String> cache = Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(10)).build();
  ```

- 基于容量的清除缓存策略

  意思就是设置缓存的数量上限，比如说只能存`1`个缓存：

  ```java
  Cache<String, String> cache = Caffeine.newBuilder().maximumSize(1).build();
  ```

- 基于引用的清除缓存策略

  设置缓存为软引用或者弱引用，使用`GC`来回收缓存数据，性能较差，不建议使用。

**<font color="red">在默认情况下，当一个缓存元素过期的时候，`Caffeine`不会自动立即将其清理和驱逐。而是再一次读或写操作后【因为中间会有段相隔时间】，或者在空闲时间完成对失的缓存效数据进行清除。`Redis`也是这样。</font>**

```java
Cache<String, String> cache = Caffeine.newBuilder().maximumSize(1).build();
// 存储缓存数据
cache.put("name1", "Mike1");
cache.put("name2", "Mike2");
cache.put("name3", "Mike3");
System.out.println("name = " + cache.getIfPresent("name1"));
System.out.println("name = " + cache.getIfPresent("name2"));
System.out.println("name = " + cache.getIfPresent("name3"));
```

比如这里，我明明设置了`maximumSize`为`1`但是还是查出了全部，这就是因为它没有立即清除，而是得过一阵子或者再一次进行读写的时候才会清除。

![img](https://img-blog.csdnimg.cn/22499789e6174180ba804f3880283a4d.png)

比如我设置个睡眠，看下效果：可以看到查询的缓存为`null`

![img](https://img-blog.csdnimg.cn/87768f577cb34c3f8ed4aeb044b9ccf6.png)

在了解了`Caffeine`之后，就可以使用它来做本地进程缓存即`JVM`缓存了，因为某一类的`Caffeine`在整个容器中有且只能有一个，所以直接搞一个配置类将其交给容器管理即可。

因为这里有商品和库存两个信息，所以我们让`Spring`管理两个`Caffeine`对象，一个存储商品缓存数据，一个存储库存缓存数据，并且让缓存的初始化大小为`100`，缓存上限为`10000`：

创建`CaffeineConfiguration`类：该业务中缓存的`key`为`Long`类型即`id`

```java
package com.kk.cache.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfiguration {
    @Bean
    public Cache<Long, Item> itemCache() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(10_000)
                .build();
    }

    @Bean
    public Cache<Long, ItemStock> itemStockCache() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(10_000)
                .build();
    }
}
```

修改表现层代码，先从缓存中取数据，没有的话再从数据库取数据，并将其存放在本地缓存中【使用`cache.get`，这些逻辑在`Lambda`表达式完成即可】：

```java
/**
 * 查询指定的商品库存信息
 *
 * @param id
 * @return
 */
@GetMapping("/stock/{id}")
public ItemStock findStockById(@PathVariable("id") Long id) {
    return itemStockCache.get(id, key -> {
        ItemStock itemStockDataBase = itemStockService.getById(key);
        itemStockCache.put(key, itemStockDataBase);
        //如果查询到的数据为 null，则新建对象返回
        return itemStockDataBase == null ? new ItemStock(id, 0, 0) : itemStockDataBase;
    });
}

/**
     * 获取指定的商品信息
     * 需要先判断下商品是否已经被删除
     *
     * @param id
     * @return
     */
@GetMapping("/{id}")
public Item findById(@PathVariable("id") Long id) {
    //获取缓存中 itemStock，没有的话就从数据库中查询，然后存储在本地
    ItemStock itemStock = findStockById(id);
    //获取缓存中的 item，没有的话就从数据库中查询，然后存储在本地
    Item item = itemCache.get(id, key -> {
        Item itemDatabase = itemService.query().ne("status", 3).eq("id", id).one();
        //存储在本地缓存
        itemCache.put(key, itemDatabase);
        return itemDatabase;
    });
    if (item != null) {
        item.setStock(itemStock.getStock());
        item.setSold(itemStock.getSold());
    }
    return item;
}
```

**到此，就是使用`Caffeine`完成本地进程缓存即`JVM`缓存的全过程。**

### 19.7 缓存同步

一直有这么个问题浮现在脑海中，如果数据库跟缓存中的数据不一致该怎么办，那就需要用到缓存同步，保证数据库、各个缓存的数据是一致的。

一般来说有三种常见数据同步策略：

1. **设置缓存有效期：**给缓存设置有效期，到期后自动查询，此时会再次查询数据库
   - 优势：简单、方便
   - 缺点：时效性差，自然而然的想到缓存存在到过期的这么一段时间数据不一致怎么办？本质上并没有做同步
   - 场景：更新频率较低，时效性要求低的业务
2. **同步双写：**在修改数据库中的数据时，直接修改缓存，这其实就是做一个分布式事务，修改的时候都修改
   - 优势：时效性强，缓存与数据库强一致
   - 缺点：有代码侵入，耦合度高，实现起来想想就麻烦，因为缓存的地方有多个
   - 场景：对一致性、时效性要求较高的缓存数据
3. **异步通知：**修改数据库时发送事件通知，相关服务监听到通知后修改缓存数据
   - 优势：低耦合，可以同时通知多个缓存服务
   - 缺点：时效性一般，可能存在中间不一致状态
   - 场景：时效性要求一般，有多个服务需要同步

因为异步通知用的时最多的，所以这里着重学习异步通知，异步通知的实现可以基于消息队列`MQ`或者是`Canel`来实现，`MQ`我们之前学习过，下面是基于`MQ`的逻辑图：

![img](https://img-blog.csdnimg.cn/faefdb59a24f4e33ba0570e626a9a7a9.png)

解读：依然有少量的代码侵入，步骤大体为：

- 商品服务完成对数据的修改后，只需要发送一条消息到`MQ`中。
- 缓存服务监听`MQ`消息，然后完成对缓存的更新

基于`Canal`的逻辑图如下：

![img](https://img-blog.csdnimg.cn/42a6c0a1d6fc44ef875a15ec107a1ac6.png)

解读：代码零侵入

- 商品服务完成商品修改后，业务直接结束，没有任何代码侵入
- `Canal`监听的是`MySQL`变化，不用侵入业务代码，当发现变化后，立即通知缓存服务
- 缓存服务接收到`Canal`通知，更新缓存

不知道有没有觉得这张图片似曾相识，是滴~当初在做`ElasticSearch`的数据同步时，我们就有提到过可以使用`Canal`来做数据同步，只不过当时我们选择了使用`MQ`来完成异步通知。这次接这个多级缓存的机会具体学习下使用`Canal`监听`MySQL binlog`来完成数据同步。

![img](https://img-blog.csdnimg.cn/e80ab9330ba54221b01a21e365bdf1b2.png)

所以我们选择使用`Canal`来实现数据同步是非常不错的一种选择，要使用，你就得先安装`Canal`，在此之前先简单认识下`Canal`。

#### 19.7.1 `Canal`简单介绍

`Canal `，译意为水道/管道/沟渠，`Canal`是阿里巴巴旗下的一款开源项目，基于`Java`开发。基于数据库增量日志解析，提供增量数据订阅消费。`GitHub`的地址：https://github.com/alibaba/canal

`Canal`是基于`MySQL`的主从同步来实现的，既然要做主从同步那就是要做`MySQL`集群了，`MySQL`主从同步的原理如下：

1. `MySQL master`将数据变更写入二进制日志`( binary log)`即`binlog`，其中记录的数据叫做`binary log events`
2. `MySQL salve`将`master`的`binary log events`拷贝到它的中继日志`(relay log)`
3. `MySQL slave`重放`relay log`中存储的`binlog events`事件，将数据变更反映它自己的数据，这就完成了主从复制

![img](https://img-blog.csdnimg.cn/6a98d1b4757741d2bd6ff42ccaa28c54.png)



听说`Canal`可以同步`Mysql`数据，它并没有用什么神奇的魔法，而是混入`Msqyl Slave`的队伍中，冒充`slave`从节点，从而监听`master`的`binary log`变化。再把得到的变化信息通知给`Canal`的客户端，进而完成对其它数据库的同步。【真正的二进制间谍了属于是，想必`MySQL`老大哥其实知道的，但是为了照顾大局，让`Canel`这样搞，毕竟`Canal`只是拿`binlog`的数据，虽然有一定的安全隐患，但也默许了】

![img](https://img-blog.csdnimg.cn/f0901c3122fe4af89363feb307ed9f64.png)

#### 19.7.2 安装`Canal`

`Canal`是基于`MySQL`的主从同步功能，因此必须先开启`MySQL`的主从功能才可以。这里用`Docker`运行的`MySQL`为例：

1. 打开`mysql`容器挂载的日志文件，我的在`/tmp/mysql/conf`目录：

   ![img](https://img-blog.csdnimg.cn/b1dc7d2da7204cb592d2d4599c744a3a.png)

2. 修改文件：

   ```sh
   vim /tmp/mysql/conf/my.cnf
   ```

   添加内容：

   ```ini
   log-bin=/var/lib/mysql/mysql-bin
   binlog-do-db=item
   ```

   配置解读：

   - `log-bin=/var/lib/mysql/mysql-bin`：设置`binary log`文件的存放地址和文件名，叫做`mysql-bin`
   - `binlog-do-db=heima`：指定对哪个`database`记录`binary log events`，这里记录`item`这个库

   最终效果：

   ```ini
   [mysqld]
   skip-name-resolve
   character_set_server=utf8
   datadir=/var/lib/mysql
   server-id=1000
   log-bin=/var/lib/mysql/mysql-bin
   binlog-do-db=item
   ```


3. 接下来添加一个仅用于数据同步的账户，出于安全考虑，这里仅提供对`item`这个库的操作权限：

   ```mysql
   [这里你可以使用 Navicat 都可以]
   docker exec -it mysql bash
   mysql -u root -p
   123456
   use mysql;
   select User from user;
   
   如果你是5.x 版本的MySQL：
   create user canal@'%' IDENTIFIED by 'canal' ;
   GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT,SUPER ON *.* TO 'canal'@'%' identified by 'canal';
   
   如果你是8.x版本的MySQL：
   create user canal@'%' IDENTIFIED by 'canal';
   GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT,SUPER ON *.* TO 'canal'@'%';
   FLUSH PRIVILEGES;
   ```

4. 重启`MySQL`容器：

   ```shell
   docker restart mysql
   ```

5. `MySQL`中使用`show master status`命令查看是否成功设置主从功能：

   ![img](https://img-blog.csdnimg.cn/02542ac2d8834e689bbf61df78d203c5.png)

开启完成`MySQL`的主从功能就可以安装`Canal`了，因为这里默认使用的网络就是`docker0`所以我们就不创建网络了，有需要的可以自行创建：

```shell
docker network create item
docker network connect item mysql
```

到`hub.docker`拉取镜像包，或者直接使用提供的资源进行加载，`800+M`还是有点大的建议直接后者搞起：

```shell
docker load -i canal.tar
```

创建`Canal`容器：

```shell
docker run -p 11111:11111 --name canal \
-e canal.destinations=item \
-e canal.instance.master.address=mysql:3306  \
-e canal.instance.dbUsername=canal  \
-e canal.instance.dbPassword=canal  \
-e canal.instance.connectionCharset=UTF-8 \
-e canal.instance.tsdb.enable=true \
-e canal.instance.gtidon=false  \
-e canal.instance.filter.regex=item\\..* \
-d canal/canal-server:v1.1.5
```

说明:【搞错的话可以删除容器再重新来一遍`docker rm -f canal`】

- `-p 11111:11111`：这是`canal`的默认监听端口，如果是`NAT`记得对外开放`11111`端口
- `destination`：`canal`的集群名字
- `-e canal.instance.master.address=mysql:3306`：数据库地址和端口，如果不知道`mysql`容器地址，可以通过`docker inspect 容器id`来查看
- `-e canal.instance.dbUsername=canal`：数据库用户名
- `-e canal.instance.dbPassword=canal` ：数据库密码
- `-e canal.instance.filter.regex=`：要监听的表名称

表名称监听支持的语法：

```
mysql 数据解析关注的表，Perl正则表达式.
多个正则之间以逗号(,)分隔，转义符需要双斜杠(\\) 
常见例子：
1.  所有表：.*   or  .*\\..*
2.  canal schema下所有表： canal\\..*
3.  canal下的以canal打头的表：canal\\.canal.*
4.  canal schema下的一张表：canal.test1
5.  多个规则组合使用然后以逗号隔开：canal\\..*,mysql.test1,mysql.test2 
```

`docker ps`查看运行容器状态。到这里`Canal`就安装完毕了。

#### 19.7.3 监听`Canal`

`Canal`提供了各种语言的客户端，当`Canal`监听到`binlog`变化时，会通知`Canal`的客户端。我们可以利用`Canal`提供的官方`Java`客户端，监听`Canal`通知消息。当收到变化的消息时，完成对缓存的更新。

![img](https://img-blog.csdnimg.cn/a19be385fae94e18963572189cf9df4e.png)

不过这里使用的是`GitHub`的第三方开源的`canal-starter`客户端。`Github`地址：https://github.com/NormanGyllenhaal/canal-client，它可以与`SpringBoot`完美整合，自动装配，比官方客户端要简单好用很多。

1. 引入依赖：

   ```xml
   <dependency>
       <groupId>top.javatool</groupId>
       <artifactId>canal-spring-boot-starter</artifactId>
       <version>1.2.1-RELEASE</version>
   </dependency>
   ```

2. 修改配置文件：

   ```yaml
   canal:
     destination: item # canal的集群名字，要与安装canal时设置的名称一致
     server: 192.168.56.1:11111 # canal服务地址
   ```

3. 编写`Canal`监听器

   通过实现`EntryHandler<T>`接口编写监听器，监听`Canal`消息。注意两点：

   - 实现类通过`@CanalTable("tb_item")`指定监听的表信息
   - `EntryHandler`的泛型是与表对应的实体类

   因为`Canal`推送给`canal-client`的时被修改的这一行数据`row`，而我们引入的`canal-client`则会将改行数据封装到`Item`实体类中，这个过程就需要知道数据库和实体类的映射关系：`@Transient @Id @Column【标记表中与属性名不一致的字段】`

   ```java
   package com.kk.cache.pojo;
   
   import com.baomidou.mybatisplus.annotation.TableField;
   import com.baomidou.mybatisplus.annotation.TableName;
   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;
   import org.springframework.data.annotation.Id;
   import org.springframework.data.annotation.Transient;
   
   import java.util.Date;
   
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   @TableName(value = "tb_item")
   public class Item {
       @Id//标记表中的id字段
       private Long id;//商品id
       @Column(name = "name")
       private String name;//商品名称
       private String title;//商品标题
       private Long price;//价格（分）
       private String image;//商品图片
       private String category;//分类名称
       private String brand;//品牌名称
       private String spec;//规格
       private Integer status;//商品状态 1-正常，2-下架
       private Date createTime;//创建时间
       private Date updateTime;//更新时间
       @TableField(exist = false)
       @Transient//标记不属于表中的字段
       private Integer stock;
       @TableField(exist = false)
       @Transient//标记不属于表中的字段
       private Integer sold;
   }
   ```

   当数据库执行了`增删改`时都应该更改缓存数据：本地进程缓存、`Redis`缓存

   ```java
   package com.kk.cache.handler;
   
   import com.github.benmanes.caffeine.cache.Cache;
   import com.kk.cache.handler.RedisHotHandler;
   import com.kk.cache.pojo.Item;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Component;
   import top.javatool.canal.client.annotation.CanalTable;
   import top.javatool.canal.client.handler.EntryHandler;
   
   @Component
   public class ItemCanalHandler implements EntryHandler<Item> {
       @Autowired
       private RedisHotHandler redisHandler;
       @Autowired
       private Cache<Long, Item> itemCache;
   
       @Override
       public void update(Item before, Item after) {
           // 写数据到JVM进程缓存
           itemCache.put(after.getId(), after);
           // 写数据到redis
           redisHandler.saveItem(after);
       }
   
       @Override
       public void delete(Item item) {
           // 删除数据到JVM进程缓存
           itemCache.invalidate(item.getId());
           // 删除数据到redis
           redisHandler.deleteItemById(item.getId());
       }
   }
   ```

   修改`RedisHotHandler`的代码：

   ```java
   package com.kk.cache.handler;
   
   import com.alibaba.fastjson.JSON;
   import com.kk.cache.pojo.Item;
   import com.kk.cache.pojo.ItemStock;
   import com.kk.cache.service.ItemService;
   import com.kk.cache.service.ItemStockService;
   import org.springframework.beans.factory.InitializingBean;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.data.redis.core.RedisTemplate;
   import org.springframework.stereotype.Component;
   
   import java.util.List;
   
   @Component
   public class RedisHotHandler implements InitializingBean {
   
       @Autowired
       private RedisTemplate redisTemplate;
   
       @Autowired
       private ItemService itemService;
   
       @Autowired
       private ItemStockService itemStockService;
   
       //private static final ObjectMapper MAPPER = new ObjectMapper();
   
       @Override
       public void afterPropertiesSet() throws Exception {
           //查询商品信息 ---> 将其放入缓存，合并的事情查询 Redis 缓存的时候就做了，这里不用重复，然后放入 Redis 缓存中
           List<Item> itemList = itemService.list();
           for (Item item : itemList) {
               //转换为 JSON 数据，key 为 item:id，你使用 fastjson 或者 jackson 都可以
               String json = JSON.toJSONString(item);
               // String json = MAPPER.writeValueAsString(item);
               redisTemplate.opsForValue().set("item:id" + item.getId(), json);
           }
           //查询库存信息，放入 Redis 缓存中
           List<ItemStock> itemStocks = itemStockService.list();
           for (ItemStock itemStock : itemStocks) {
               String json = JSON.toJSONString(itemStock);
               redisTemplate.opsForValue().set("item:stock:id" + itemStock.getId(), json);
           }
       }
   
       public void saveItem(Item item) {
           String json = JSON.toJSONString(item);
           redisTemplate.opsForValue().set("item:id:" + item.getId(), json);
       }
   
       public void deleteItemById(Long id) {
           redisTemplate.delete("item:id:" + id);
       }
   }
   ```

到这里，使用`Canal`完成异步缓存同步的操作就完成了。

到这里，整个多级缓存也就学习完毕了：

![img](https://img-blog.csdnimg.cn/c6a5318a40ac443694821e3c0c220007.png)