## 18. 分布式缓存【重点 + 难点】

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
docker run --name redis -p 6379:6379 -d redis:6.2.4

在/var/lib/docker/volumes/redis/redis.conf配置好配置文件、创建好文件夹data然后再执行。可以使用xftp拉取配置文件。
更改下 bind 0.0.0.0
更改下 protected-mode no

docker run -p 6379:6379 --name redis -v /var/lib/docker/volumes/redis/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/data:/data -d redis:6.2.4 redis-server /etc/redis/redis.conf --appendonly yes[开启AOF持久化]

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
docker run -p 7001:7001 -p 27001:27001 --name redis7001 -v /var/lib/docker/volumes/redis/7001/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7001/data:/data -d redis:6.2.4 redis-server /etc/redis/redis.conf

docker run -p 7002:7002 -p 27002:27002 --name redis7002 -v /var/lib/docker/volumes/redis/7002/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7002/data:/data -d redis:6.2.4 redis-server /etc/redis/redis.conf

docker run -p 7003:7003 -p 27003:27003 --name redis7003 -v /var/lib/docker/volumes/redis/7003/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7003/data:/data -d redis:6.2.4 redis-server /etc/redis/redis.conf

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

| 节点 |       `IP`        |  PORT   |
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

vim /var/lib/docker/volumes/redis/7001/data/sentinel1/sentinel.conf
vim /var/lib/docker/volumes/redis/7002/data/sentinel2/sentinel.conf
vim /var/lib/docker/volumes/redis/7003/data/sentinel3/sentinel.conf

cp /var/lib/docker/volumes/redis/7001/data/sentinel1/sentinel.conf /var/lib/docker/volumes/redis/7002/data/sentinel2/sentinel.conf

cp /var/lib/docker/volumes/redis/7001/data/sentinel1/sentinel.conf /var/lib/docker/volumes/redis/7003/data/sentinel2/sentinel.conf

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
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.7.3</version>
           <relativePath/> <!-- lookup parent from repository -->
       </parent>
       <groupId>com.kk</groupId>
       <artifactId>redis-demo</artifactId>
       <version>0.0.1-SNAPSHOT</version>
       <name>05-redis-demo</name>
       <description>05-redis-demo</description>
       <properties>
           <java.version>1.8</java.version>
       </properties>
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-data-redis</artifactId>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <version>1.18.24</version>
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

3. 配置`application.yaml`配置文件：

   ```yaml
   spring:
     redis:
       sentinel:
         master: mymaster #指定的主节点名称
         nodes:
           - 192.168.56.1:27001
           - 192.168.56.1:27002
           - 192.168.56.1:27003
   ```

4. 创建`HelloController`

   ```java
   package com.kk.redisdemo.controller;
   
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.data.redis.core.StringRedisTemplate;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.RestController;
   
   @RestController
   public class HelloController {
   
       @Autowired
       private StringRedisTemplate redisTemplate;
   
       @GetMapping("/get/{key}")
       public String hi(@PathVariable String key) {
           return redisTemplate.opsForValue().get(key);
       }
   
       @GetMapping("/set/{key}/{value}")
       public String hi(@PathVariable String key, @PathVariable String value) {
           redisTemplate.opsForValue().set(key, value);
           return "success";
       }
   }
   ```

5. 因为起初没有将容器的`27001`等端口开放，所以这里需要重新设置下容器：直接复制粘贴，别多想

   ```shell
   vim /var/lib/docker/volumes/redis/7001/redis.conf
   replica-announce-ip 192.168.56.1
   replica-announce-port 7001
   
   vim /var/lib/docker/volumes/redis/7002/redis.conf
   replica-announce-ip 192.168.56.1
   replica-announce-port 7002
   replicaof 192.168.56.1 7001
   :wq
   
   vim /var/lib/docker/volumes/redis/7003/redis.conf
   replica-announce-ip 192.168.56.1
   replica-announce-port 7003
   replicaof 192.168.56.1 7001
   :wq
   
   vim /var/lib/docker/volumes/redis/7001/sentinel.conf
   port 27001
   sentinel monitor mymaster 192.168.56.1 7001 2
   sentinel down-after-milliseconds mymaster 5000
   sentinel failover-timeout mymaster 60000
   dir "/data"
   
   vim /var/lib/docker/volumes/redis/7002/sentinel.conf
   port 27002
   sentinel monitor mymaster 192.168.56.1 7001 2
   sentinel down-after-milliseconds mymaster 5000
   sentinel failover-timeout mymaster 60000
   dir "/data"
   
   vim /var/lib/docker/volumes/redis/7003/sentinel.conf
   port 27003
   sentinel monitor mymaster 192.168.56.1 7001 2
   sentinel down-after-milliseconds mymaster 5000
   sentinel failover-timeout mymaster 60000
   dir "/data"
   
   sentinel monitor mymaster 172.20.0.2 7001 2
   :wq
   ```

   ```shell
   docker stop redis7001
   
   docker stop redis7002
   
   docker stop redis7003
   
   docker stop sentinel1
   
   docker stop sentinel2
   
   docker stop sentinel3
   
   docker rm -f redis7001
   
   docker rm -f redis7002
   
   docker rm -f redis7003
   
   docker rm -f sentinel1
   
   docker rm -f sentinel2
   
   docker rm -f sentinel3
   
   docker run -p 7001:7001 --name redis7001 -v /var/lib/docker/volumes/redis/7001/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7001/data:/data -d redis:6.2.4
   
   docker run -p 7002:7002 --name redis7002 -v /var/lib/docker/volumes/redis/7002/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7002/data:/data -d redis:6.2.4
   
   docker run -p 7003:7003 --name redis7003 -v /var/lib/docker/volumes/redis/7003/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7003/data:/data -d redis:6.2.4
   
   docker run -p 27001:27001 --name sentinel1 -v /var/lib/docker/volumes/redis/7003/sentinel.conf:/etc/redis/sentinel.conf -d redis:6.2.4
   
   docker run -p 27002:27002 --name sentinel2 -v /var/lib/docker/volumes/redis/7003/sentinel.conf:/etc/redis/sentinel.conf -d redis:6.2.4
   
   docker run -p 27003:27003 --name sentinel3 -v /var/lib/docker/volumes/redis/7003/sentinel.conf:/etc/redis/sentinel.conf -d redis:6.2.4
   
   docker ps
   ```

   ![img](https://img-blog.csdnimg.cn/4abe5280ac934c6384ff6db397030629.png)

   ```shell
   docker exec -it redis7001 redis-server /etc/redis/redis.conf
   
   docker exec -it redis7002 redis-server /etc/redis/redis.conf
   
   docker exec -it redis7003 redis-server /etc/redis/redis.conf
   
   docker exec -it sentinel1 redis-sentinel /etc/redis/sentinel.conf
   
   docker exec -it sentinel2 redis-sentinel /etc/redis/sentinel.conf
   
   docker exec -it sentinel3 redis-sentinel /etc/redis/sentinel.conf
   ```

   然后还要修改下`NAT`地址转换。

6. 配置主从读写分离

   ```java
   package com.kk.redisdemo;
   
   import io.lettuce.core.ReadFrom;
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
   import org.springframework.boot.autoconfigure.neo4j.ConfigBuilderCustomizer;
   import org.springframework.context.annotation.Bean;
   
   @SpringBootApplication
   public class Application {
       public static void main(String[] args) {
           SpringApplication.run(Application.class, args);
       }
   
       @Bean
       public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
           return configBuilderCustomizer -> configBuilderCustomizer.readFrom(ReadFrom.REPLICA_PREFERRED);
       }
   }
   ```

   这里的`ReadFrom`是配置`Redis`的读取策略，是一个枚举，包括如下选择：

   - `MASTER`：从主节点中读取
   - `MASTER_PREFERRED`：优先从主节点中读取，`master`不可用才读取`replica`
   - `REPLICA`：从从节点中读取
   - `REPLICA_PREFERRED`：优先从从节点中读取，`replica`不可用才读取`master`

### 18.6 `Redis`分片集群

- 前面为了避免故障从而导致`Redis`崩溃以至于数据丢失，于是我们学习了持久化，解决了**数据丢失问题**
- 单机`Redis`并发能力不够强，我们整出了`Redis`主从模式的集群，解决了**并发能力问题**
- 搭建的集群实例不知道健康状态，于是整出了`Sentinel`哨兵，解决了**故障恢复问题**
- **现在到了最后一个，就是存储能力的问题我们该如何解决呢？**

我们知道内存是有上限的，我们不可能把所有的内存空间全部分配给`Redis`，因为随着时间的消逝，`Redis`存储的数据会越来越多，越来越多，如果有海量数据，单纯的依靠简单的内存存储机制是无法满足需求的。这就是`Redis`的存储能力问题。

而且，我们之前搭建的主从模式的集群，实现了读写分离，但是是应对大部分读的，那如果此时有一个场景写的需求很大，光主节点去执行写操作肯定是不够的，这又该如何解决呢？

也就是说主从+哨兵的模式可以解决高并发读、高可用的问题，但是依然有两个问题没有解决：

- **海量数据存储问题**
- **高并发写的问题**

**<font color="red">分片集群</font>**就可以解决这两个问题，可想而知是有多牛这玩意。

#### 18.6.1 分片集群的结构

分片集群的结构是这样子的：

- 为了解决高并发写的问题，所以有了多个主节点`master`

- 同时为了保持高可用，每个主节点`master`还是有多个从节点`slave`

- `master`之间通过`ping`指令监测彼此的健康状态，此时就不需要哨兵了，`master`主节点覆盖了哨兵的角色，掌控了军权，如果有足够数量的主节点认为某个主节点`master`主观下线，则转变为客观下线，将该主节点`master`服务下线，然后从该主节点的从节点选一个备选节点将其升级为主节点。属于是合作攻击了这是。

- 这么多主节点，`Redis`客户端访问谁呢？事实上，该客户端可以访问集群中的任意一个节点，因为这些节点会有一个路由表，它们每一个节点都会最终将该请求转发到正确的节点上。这里还是覆盖了哨兵的其中一个作用即 —— 通知功能。

  ![img](https://img-blog.csdnimg.cn/fd7d16254eb1417887943380af1108b8.png)

#### 18.6.2 搭建分片集群

我们按下述的结构搭建下：`3`个主节点，每个主节点有一个从节点即一共`3`个从节点。

![img](https://img-blog.csdnimg.cn/95e916eb1ddb4940bd25c1b266d7b522.png)

|      `IP`      | `PORT` |   角色   |
| :------------: | :----: | :------: |
| `192.168.56.1` | `7001` | `master` |
| `192.168.56.1` | `7002` | `master` |
| `192.168.56.1` | `7003` | `master` |
| `192.168.56.1` | `8001` | `slave`  |
| `192.168.56.1` | `8002` | `slave`  |
| `192.168.56.1` | `8003` | `slave`  |

删除之前所有的哨兵和节点，因为我这里是`Docker`搭建的，所以删除的就是容器。

```sh
docker rm -f redis7001
docker rm -f redis7002
docker rm -f redis7003
docker rm -f redis8001
docker rm -f redis8002
docker rm -f redis8003
```

在`/var/lib/docker/volumes/redis/`目录下，创建`7001 7002 7003 8001 8002 8003`目录，用作数据卷。这里每个文件夹都有一个`data`目录以及`redis.conf`，这是缩小版的`redis.conf`直接复制修改即可：

**<font color="red">注意：分片集群中`slave`从节点不需要配置`replicaof 192.168.56.1 7001`，并且不再是`replica-announce-ip`而是`cluster-announce-ip`的模式</font>**

每个节点改动的就是`port cluster-announce-ip cluster-announce-bus-port`

```sh
bind 0.0.0.0
port 7001
protected-mode no
daemonize yes
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
cluster-announce-ip 192.168.56.1
cluster-announce-port 7001
cluster-announce-bus-port 17001
appendonly yes
appendfsync everysec
no-appendfsync-on-rewrite no
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
loglevel notice
logfile "/data/redis.log"
```

`Docker`启动即可：

```shell
docker rm -f redis7001

docker rm -f redis7002

docker rm -f redis7003

docker rm -f redis8001

docker rm -f redis8002

docker rm -f redis8003

docker run -p 7001:7001 -p 17001:17001 --name redis7001 -v /var/lib/docker/volumes/redis/7001/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7001/data:/data -d redis:6.2.4

docker run -p 7002:7002 -p 17002:17002 --name redis7002 -v /var/lib/docker/volumes/redis/7002/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7002/data:/data -d redis:6.2.4

docker run -p 7003:7003 -p 17003:17003 --name redis7003 -v /var/lib/docker/volumes/redis/7003/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7003/data:/data -d redis:6.2.4

docker run -p 8001:8001 -p 18001:18001 --name redis8001 -v /var/lib/docker/volumes/redis/8001/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/8001/data:/data -d redis:6.2.4

docker run -p 8002:8002 -p 18002:18002 --name redis8002 -v /var/lib/docker/volumes/redis/8002/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/8002/data:/data -d redis:6.2.4

docker run -p 8003:8003 -p 18003:18003 --name redis8003 -v /var/lib/docker/volumes/redis/8003/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/8003/data:/data -d redis:6.2.4
```

```sh
这里一个个执行：
docker exec -it redis7001 redis-server /etc/redis/redis.conf

docker exec -it redis7002 redis-server /etc/redis/redis.conf

docker exec -it redis7003 redis-server /etc/redis/redis.conf

docker exec -it redis8001 redis-server /etc/redis/redis.conf

docker exec -it redis8002 redis-server /etc/redis/redis.conf

docker exec -it redis8003 redis-server /etc/redis/redis.conf
```

![img](https://img-blog.csdnimg.cn/e7790d3612f4453ba2c5062b17aca8ef.png)

此时服务都启动了，但是它们都是独立的，彼此之间没有任何关联。接下来我们要用命令一步步地创建集群，在`Redis 5.0`之前，创建集群是很麻烦的一件事情，但是在这之后，创建集群的命令集成到了`redis-cli`中。

```sh
docker exec -it redis7001 bash

redis-cli -h 192.168.56.1 -p 7001 --cluster create --cluster-replicas 1 192.168.56.1:7001 192.168.56.1:7002 192.168.56.1:7003 192.168.56.1:8001 192.168.56.1:8002 192.168.56.1:8003 
```

这里的`1`代表的是每个主节点只有一个从节点，为什么不用指定哪个是主哪个是从，这里面前面：`节点总数 ➗ (replicas + 1)`，其中`replicas`就是我们设置的`1`，所以前面`3`个就是主节点。

因为我是`NAT`地址转化，记得开启`7001 7002 7003 8001 8002 8003`的端口映射，否则一直出现`Connection Refused`的情况。

而且还要开启`17001 17002 17003 18001 18002 18003`的总线端口映射，否则会一直停留在：`Waiting for the cluster to join...`

![img](https://img-blog.csdnimg.cn/c3d175e48bbc4e4a9ca211e328ea5704.png)

通过`redis-cli -h 192.168.56.1 -p 7001 cluster nodes`可以查看集群状态：

![img](https://img-blog.csdnimg.cn/8fbb2649ba914b8e9d078f22671385d0.png)

分片集群的测试：【注意我这是进入了容器直接做，你也可以在虚拟机或者`windows`中做】

注意操作集群时要加入`-c`参数表示集群模式，否则无法写入，因为写入时会重定向到另外一个节点，到这里，虽然还无法弄懂为什么，但是已经开始可以感受到分片集群的一点点魅力了。

连接任意一个实例比如我的连接：`redis-cli -c -h 192.168.56.1 -p 7001`，然后写入几条数据：

![img](https://img-blog.csdnimg.cn/d86f958c32d14bb78d676acbf797fef4.png)

再连接另外一个主节点，查看数据：`redis-cli -c -h 192.168.56.1 -p 7002`

![img](https://img-blog.csdnimg.cn/b14c56ca8e24457e80943a491f353cc1.png)

可以看到很哇塞，每个节点【是无论是哪个节点哦，任意】都可以访问到再其它节点创建的数据。可以看到分片集群相当牛逼。

#### 18.6.3 分片集群存储原理散列插槽

可以看到每一个主节点`master`都有一个`hash slot`，这个东西就叫做散列插槽。这有什么用呢？比如下图：

![img](https://img-blog.csdnimg.cn/2f211a08c3544643893e62daeda0140a.png)

 为了实现分片集群，即提高高并发写能力，`Redis`会把每一个`master`主节点映射到`0~16383`供`16384`个插槽上`hash slot`，如上图：`[0 - 5460]`被映射给第一个主节点，`[5461 - 10922]`映射给了第二个主节点，而最后的`[10923 - 16383]`被映射给了最后一个主节点。

当用户写入数据时，数据不是跟存储的节点所绑定，而是跟插槽所绑定，这样做有什么好处呢？当该节点过掉的时候，插槽就会携带着数据做一个转移，转移到其它节点，这样就不会导致因节点发生故障而宕机导致数据丢失。就好比如各个节点只是一块地盘，如果这个地盘已经不适合生存了，插槽首领就会带领数据子民离开这片土地而去寻找更适合居住的地方。数据子民就不会死。

那么数据是如何跟插槽所绑定的呢？原来是根据`key`然后计算得到插槽值，分两种情况：

- `key`中如果包含`{}`且`{}`里面含有字符，则`{}`里的字符就是有效部分
- `key`中如果不包含`{}`，那么整个`key`就都是有效部分

例如：如果`key`是`{kk}num`，那么就根据`{kk}`计算，如果`key`是`num`，那么根据`num`计算。计算方式是通过`CRC16`算法得到一个`hash`值，然后将该`hash % 16384`从而得到的数据都在`0 - 16383`。这就得到了插槽值即`slot`值。【字符 ---> 哈希值 ---> 余法得到插槽值】

如下图，存储`a`时，算出来的`slot`插槽值在第三个主节点的插槽映射范围之内，所以需要存储在第三个主节点中。`redis-cli -h 192.168.56.1 -p 7001 -c`

![img](https://img-blog.csdnimg.cn/ef22d694e3e74179a9bf08c5dfc7feae.png)

总结下：

- `Redis`是如何判断某个`key`应该在哪个实例？

  - 将`[0 - 16384]`个插槽映射分配给不同的实例
  - 通过`CRC16`算法对`key`的有效部分进行计算，获取到哈希值然后通过余法得到插槽值即`slot`值
  - 根据计算出来的插槽值即`slot`值找到符合映射关系的实例存储即可

- 如何将同一类的数据固定保存在同一个实例？

  - 使用相同的有效部分：

    我们知道有效部分分为两种，一种是有`{}`一种是没有`{}`的，如果我们想将一类数据存储在特定的实例，我们可以给这个`key`加上`{}`部分，使之成为有效部分。

    ![img](https://img-blog.csdnimg.cn/366953e8ae69479cbdaeddae26c95a65.png)

    可以看到有`{phone}`的都存储到了`7002`实例中。

#### 18.6.4 分片集群之集群伸缩

作为一个分片集群最重要的一个功能就是可以动态的增加或者移除节点，这就称之为集群伸缩。我们可以通过`redis-cli --cluster`命令添加或者移除节点。

可以通过`redis-cli --cluster help`查看关于该命令的帮助信息：

![img](https://img-blog.csdnimg.cn/921188f67d424dd6ba8e3a527bf3634d.png)

比如添加节点：

![img](https://img-blog.csdnimg.cn/64cf4f8e40f3408eb79f442cfcd9ec0b.png)

需求：向集群中添加一个新的`master`节点，并向其中存储`num = 10`

- 启动一个新的`redis`实例，端口为`7004`
- 添加`7004`到之前的集群，并作为一个`master`节点
- 给`7004`节点分配插槽，使得`num`这个key可以存储到`7004`实例

```
cd /var/lib/docker/volumes/redis/
mkdir 7004
cd 7004
mkdir data
vim redis.conf ---> 拷贝过去即可
docker run -p 7004:7004 -p 17004:17004 --name redis7004 -v /var/lib/docker/volumes/redis/7004/redis.conf:/etc/redis/redis.conf -v /var/lib/docker/volumes/redis/7004/data:/data -d redis:6.2.4

记得做 NAT 地址转换的映射

docker exec -it redis7004 bash
redis-server /etc/redis/redis.conf
redis-cli --cluster add-node 192.168.56.1:7004 192.168.56.1:7001
```

可以看到成功向集群中添加了某节点：

![img](https://img-blog.csdnimg.cn/5a7e6a5b2390442a823fcd66522fa477.png)

查看下该集群的节点信息：`redis-cli -h 192.168.56.1 -p 7001 cluster nodes`，可以看到`7004`端口的节点已经添加到集群了。

![img](https://img-blog.csdnimg.cn/95fcc3a9cba54dd5a4a6393ea6152918.png)

但是我们可以看到此时的`7004`式没有插槽的，我们前面存储`num`的时候知道`num`计算出来的插槽值在`7001`实例中，所以我们得将这部分插槽分配到`7004`中，这样就可以将`num`存储到`7004`中。

1. 确认是哪一部分插槽：别忘了`-c`，可以看到`num`跟插槽`2765`绑定在了一块中。

   ```shell
   redis-cli -h 192.168.56.1 -p 7003 -c
   ```

   ![img](https://img-blog.csdnimg.cn/9e6ce0e68ea249cfb7114abbb3a8d88f.png)

2. 将插槽分配给`7004`实例：我们移动`0 - 2765`给`7004`也就是移动`2766`个插槽

   ```shell
   redis-cli --cluster help 查询分配帮助 ---> reshard
   redis-cli --cluster reshard 192.168.56.1 7001
   2766
   复制上面的 id 值，因为要移动到 7004，所以这里粘贴 7004 的 id 值即可
   然后就是数据/插槽源，来源于 7001 所以复制 7001 的 id 值即可
   输入 done
   ```

   一下是特别省出来一个插槽当作案例，查看效果：

   ![img](https://img-blog.csdnimg.cn/3dd37f4867f643788dc7c0ca9d4da181.png)

   查看集群中各个节点的状态：可以看到`7004`实例的占据了`0 - 2765`一共`2766`个插槽。

   ```shell
   redis-cli -h 192.168.56.1 -p 7001 cluster nodes
   ```

   ![img](https://img-blog.csdnimg.cn/e2c33372fc2d4484a5608aed2abf36f9.jpeg)

3. 连接到`7001`然后`set num 10`观察效果：可以看到重定向到了`7004`实例去了。

   ```shell
   redis-cli -h 192.168.56.1 -p 7001 -c
   set num 10
   ```

   ![img](https://img-blog.csdnimg.cn/f56b784c3a7346ec92386d194b027015.png)

回顾下：

1. 可以通过`redis-cli -h 192.168.56.1 -p 7001 --cluster help`任意一个集群节点来查询集群操作的帮助文档
2. 向集群中添加节点：可以通过`redis-cli -h 192.168.56.1 -p 7004 --cluster add-node 192.168.56.1:7005 192.168.56.1:7001`【前提当然是你得有这个实例节点，如果不添加`--cluster-slave --cluster-master-id <arg>`默认表示为主节点】
3. 向集群中添加插槽：可以通过`redis-cli -h 192.168.56.1 -p 7001 --cluster reshard 192.168.56.1:7001【任意一个集群中的节点】`

自己摸索下如何删除集群中的一个节点：

1. 查询帮助：`redis-cli -h 192.168.56.1 -p 7001 --cluster help`可以看到一个`del-node`的帮助，可以看到需要节点`id`

2. 查询`id`：`redis-cli -h 192.168.56.1 -p 7001 cluster nodes`查询到`7004`的节点`id`为：`b4228ec1e6f226358bb0707191d8f186acd9e965`

3. 删除节点：`redis-cli -h 192.168.56.1 -p 7001 --cluster del-node 192.168.56.1:7004  b4228ec1e6f226358bb0707191d8f186acd9e965`

4. 报错，说是要该节点里面有数据，你得先把数据移走即移走插槽：

   ![img](https://img-blog.csdnimg.cn/b28d2ae7cdc241dab437d35f1b8a2fd1.png)

5. 所以得使用先将插槽移走，我这里移动到`7001`实例中去，当然你也可以使用`all`分配给所有其余主节点：`redis-cli -h 192.168.56.1 -p 7001 --cluster reshard 192.168.56.1:7001`根据提示完成操作即可

6. 再次删除`7004`节点：`redis-cli -h 192.168.56.1 -p 7001 --cluster del-node 192.168.56.1:7004 b4228ec1e6f226358bb0707191d8f186acd9e965`此时可以看到成功从集群中移除了该节点：

   ![img](https://img-blog.csdnimg.cn/b6e3c3ac503f4760a9757703d48e2732.png)

#### 18.6.5 分片集群之故障转移

##### 18.6.5.1 自动故障转移

学习分片集群结构的时候就知道了，分片集群是没有哨兵的，因为被主节点`master`掌控了军权，将哨兵`sentinel`灭了。

因为有多个主节点，为了防止独裁，所以刚开始多个主节点之间商量好使用心跳机制决定互相监督，搞个权力分散，殊不知当一个主节点实例变弱【出现故障导致宕机】时，另外的主节点之间会强强联合，某个主节点先发现该`master`的弱点，心生一计，主观认为该`master`应该主观下线。知道自己一个人的力量不够，于是去找多个`master`，强强联手，导致该故障主节点`master`被击败，客观下线。

为了安抚该主节点数据的民心，它们会在这个`master`的国家中的从节点选出一位，让它成为新的主节点。维持了整个系统的权力平衡。**暗流涌动下，权力的游戏还在继续。**

**主节点`master`们不知道的是，它们的生死其实全部掌握在操控整个系统的人身上。**

- 持续查看集群节点的状态：`watch redis-cli -p 7001 cluster nodes`

  `Docker`安装的默认`redis`实例中是没有`watch`的，可以执行以下命令进行安装：

  ```shell
  apt-get update
  apt-get upgrade
  apt-get install watch
  ```

  然后就可以进行持续监控集群节点了：

  ![img](https://img-blog.csdnimg.cn/5c3e58c138fe4401ae189c2c12a2b0db.png)

- 然后尝试让`7002`宕机，观察效果：`redis-cli -h 192.168.56.1 -p 7002 shutdown`

  可以看到主节点`fail + disconnected`，然后可以看到`8001`的从节点变成了`master`：

  ![img](https://img-blog.csdnimg.cn/a5a2d89ddfab461099e2aa89425a0972.png)

- 启动`7002`，观察`7002`的状态：`redis-server -h 192.168.56.1 -p 7002 /etc/redis/redis.conf`，可以看到`7002`实例成了从节点。

  ![img](https://img-blog.csdnimg.cn/7d8075d5d7bc41e3a1e872ee956e6c4d.png)

**总结下，当当集群中有一个`master`宕机会发生什么呢？**

1. 该宕机主节点跟其它主节点失去连接
2. 疑似宕机，被其余主节点标记为`fail`
3. 确定该故障主节点`master`下线，自动提升一个从节点为主节点，并且将该旧主节点标记为从节点

##### 18.6.5.2 手动故障转移

上述是自动故障转移，有了自动的干嘛还需要手动的，是自动的不够香吗？当然不是，自动的虽然香，但是有时候机器可以用，但是这个机器太过老旧了需要做些维护，然后它的从节点的性能不是特别强，现在我引入了一个新的性能超级强吊打这两台机器的机器，此时我们就需要特定这台性能超强的机器，作为该主节点的从节点，然后手动的将该从节点替换掉主节点，这样就实现了故障转移。

这就是为什么我们还需要手动故障转移 ，不是因为自动的不够香而是我们需要更多的自主性。

那么，手动故障转移要怎么做呢？

利用`cluster failover`命令可以手动地让分片集群中地某个`master`节点宕机，然后将主节点切换到执行`cluster failover`命令地这个`slave`节点中。**其实就是变相地夺权。**夺权最好地方式就是避免太多地人员伤亡，而且是亲手执行，要夺权的从节点深知这一点，所以只会让原`master`宕机。自己坐上王位。

流程如下：

![img](https://img-blog.csdnimg.cn/d8205a460b77418cb48c6f9ef9927563.png)

1. 要成为主节点的`slave`，这里我们称其为夺权者，夺权者首先会威胁`master`退位，要求其告知所有的客户端，拒绝它们的请求
2. 夺权者从`master`那里获取到当前的数据偏移量`offset`
3. 夺权者实现数据同步更新，直到数据的`offset`偏移量跟`master`一致
4. 夺权者跟`master`开始故障转移
5. 夺权者将所有的操作全部完成，标记自己是`master`并广播告知所有人故障转移的结果
6. 客户端眼中只有利益，它们对于谁是`master`根本不在乎，收到广播，它们的读请求会被原`master`所处理。

手动`failover`支持三种不同的模式：【一般默认，不用选特定模式】

- 缺省：默认的流程即走完上述`6`步
- `force`：强制夺权，省略了照顾数据子民的两步，即`2-3`，一般这种事情都是类似唐太宗李世民做的。
- `takeover`：暴力夺权，执行进行第`5`步忽略数据的一致性，不管子民了，武动夺取一切，忽略`master`和其它`master`的意见，不得民心的做法。需要很强很强的能力，一般是明成祖朱棣干得活。

具体操作为：【在自动故障转移，我们的`7002`节点一心想夺取皇位，于是乎心心准备了很久，终于发起了夺权】

1. 使用`redis-cli`连接到`7002`节点
2. 执行`cluster failover`命令`redis-cli -h 192.168.56.1 -p 7002 cluster failover`

**权力，说到底还是权力。**

![img](https://img-blog.csdnimg.cn/f78e14c0c0c9405d9a26f86e14f739da.png)

#### 18.6.6 `RedisTemplate`操作分片集群

`RedisTemplate`的底层同样是基于生菜`lettuce`实现分片集群。使用步骤跟哨兵模式一致，只有在配置信息不同，其余的完全一致。访问测试看下日志验证下即可。

```yaml
logging:
  level:
    io.lettuce.core: debug
  pattern:
    dateformat: MM-dd HH:mm:ss:SSS
#spring:
#  redis:
#    sentinel:
#      master: mymaster
#      nodes:
#        - 192.168.56.1:27001
#        - 192.168.56.1:27002
spring:
  redis:
    cluster:
      nodes:
        - 192.168.56.1:7001
        - 192.168.56.1:7002
        - 192.168.56.1:7003
        - 192.168.56.1:8001
        - 192.168.56.1:8002
        - 192.168.56.1:8003
```