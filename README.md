# rabbitMQ 学习
## AMQP
##### 1.定义
    AMQP：Advanced Message Queuing Protocol 高级消息队列协议
##### 2.AMQP模型
![AMQP](https://note.youdao.com/yws/api/personal/file/886C8A093DD540C3AAB224AFC531485A?method=getImage&version=2030&cstk=lt740Vcr "AMQP")
+ exchange 交换机 <br/>
  接收发布应用程序发送的消息，并根据一定的规则将这些消息路由到“消息队列”。
  exchange的几个类型：
  + 
+ queue 消息队列 <br/>
  存储消息，直到这些消息被消费者安全处理完为止。
+ binding 路由表<br/>
  定义了exchange和message queue之间的关联，提供路由规则。

##### 3.AMQP协议
    AMQP协议是一个二进制协议，拥有一些现代特点：多信道、协商式、异步、安全、跨平台、中立、高效。
![三层结构](https://img-blog.csdn.net/20140819105815365?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTk5VTGps/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
+ 模型层<br/>
  模型层定义了一套命令（按功能分类），客户端应用可以利用这些命令来实现它的业务功能。
+ 会话层<br/>
  会话层负责将命令从客户端应用传递给服务器，再将服务器的应答传递给客户端应用，会话层为这个传递过程提供可靠性、同步机制和错误处理
+ 传输层<br/>
  传输层提供帧处理、信道复用、错误检测和数据表示。

#### 4.AMQP命令
![AMQP](https://note.youdao.com/yws/api/personal/file/4FF15CF5264A4284BA1D91FFC94D6B69?method=download&shareKey=8dea1b6829f328b850f8e62b728f561b "AMQP")
![AMQP](https://note.youdao.com/yws/api/personal/file/0032CDDFBDF64291AE34150FC585C793?method=download&shareKey=8dea1b6829f328b850f8e62b728f561b "AMQP")

## rabbitMQ安装
#### 0.版本对应
+ rabbitmq与erlang版本对应关系 http://www.rabbitmq.com/which-erlang.html
+ erlang 版本列表 https://bintray.com/rabbitmq/rpm/erlang
+ rabbitmq 版本列表 https://github.com/rabbitmq/rabbitmq-server/releases

#### 1.安装erl
+ 1.下载安装包 `wget https://bintray.com/rabbitmq/rpm/download_file?file_path=erlang%2F21%2Fel%2F7%2Fx86_64%2Ferlang-21.1.1-1.el7.centos.x86_64.rpm`
+ 2.yum安装erlang `yum install erlang`
+ 3.测试erlang安装:`erl`进入erlang命令行模式
+ 4.两次`ctrl+c` 退出命令行模式

#### 2.安装rabbitmq
+ 下载安装包 `wget https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.7.8/rabbitmq-server-3.7.8-1.el7.noarch.rpm`
+ 安装rpm包 `yum install rabbitmq-server-3.7.8-1.el7.noarch.rpm`
    + 可能会遇到的报错：
     `Error: Package: rabbitmq-server-3.7.8-1.el7.noarch (/rabbitmq-server-3.7.8-1.el7.noarch)
           Requires: erlang >= 19.3
           Installed: erlang-R16B-03.18.el7.x86_64 (@epel)
               erlang = R16B-03.18.el7` <br/>
    安装的erlang版本与rabbitmq不一致，重装erlang/rabbitmq <br/>
    卸载已安装的erlang及其依赖 `yum remove -y erlang*`
+ 启动rabbitmq服务 启动命令 `service rabbitmq-server start`
+ 查看rabbitmq启动状态 `service rabbitmq-server status`
+ 日志目录 `/var/log/rabbitmq/`
+ 模拟使用rabbitmq http://tryrabbitmq.com

#### 3.rabbitmq管理命令
+ 官网命令文档 http://www.rabbitmq.com/rabbitmqctl.8.html
+ 查看rabbitmq中用户列表 `rabbitmqctl list_users`
+ 创建用户命令 `rabbitmqclt add_user username password`
+ 赋予用户权限命令 `rabbitmqctl set_permissions -p '/' username '.*' '.*' '.*'` <br/>命令原型为 `rabbitmqctl [-n <node>] [-l] [-q] set_permissions [-p <vhost>] <username> <conf> <write> <read>`
+ 赋予用户角色命令 `rabbitmqctl set_user_tags username administrator`
  | tag | 说明 | 权限 |
  | --- | ---- | ---- |
  |administrator|管理员|管理员权限|
  |monitoring|监控|监控权限|
  |policymaker|决策|决策权限|
  |management|项目所有者|该项目的权限|
  |none|none|不能访问 management plugin|
  |自定义|自定义|自定义|
+ 开启rabbitmq控制管理后台命令 `rabbitmq-plugins enable rabbitmq_management`
+ `http://{ip}:15672`
+ rabbitmq-plugins disable rabbitmq_management关闭管理插件  <br>
进入控制台，guest用户默认不允许登录，可创建新的管理员用户进行登录

#### rabbitMQ集群
+ 单机部署多节点
    + 资料 https://www.jb51.net/article/135989.htm <br>
    + 创建一个新的节点，并赋予新的端口号及新的web管理端口号<br>`RABBITMQ_NODE_PORT=5673 RABBITMQ_SERVER_START_ARGS="-rabbitmq_management listener [{port,15673}]" RABBITMQ_NODENAME=rabbit1 rabbitmq-server -detached`
    + 停止RabbitMQ application`rabbitmqctl -n rabbit1 stop_app`
    + 重置节点元数据`rabbitmqctl -n rabbit1 reset`<br/>
      reset 可以把一个node从一个集群中移出，forget_cluster_node则是可以在另一个节点上把某一个node移出自己的集群
    + 将节点添加到集群`rabbitmqctl -n rabbit1 join_cluster rabbit`
    + 启动新增的节点`rabbitmqctl -n rabbit1 start_app`
    + 查看集群状态`rabbitmqctl cluster_status`
    + 查看节点所在集群状态 `rabbitmqctl -n rabbit1 cluster_status`
    + 加入时候设置节点为内存节点（默认加入的为磁盘节点）`rabbitmqctl join_cluster rabbit@rmq-broker-test-1 --ram`
    + 修改的节点的类型 `rabbitmqctl change_cluster_node_type disc | ram`
    + 当一个节点停掉，再启动的时候会检查所属的集群，若在停掉期间被集群移除（集群移除节点只能在其停用期间移除），则无法启动，并报错：`{:error, {:inconsistent_cluster, 'Node \'rabbit2@instance-uq2095jp\' thinks it\'s clustered with node \'rabbit@instance-uq2095jp\', but \'rabbit@instance-uq2095jp\' disagrees'}}`这时候可以将节点重新加入集群，就可以启动了如果不想再加入集群，需要重置节点元数据信息`rabbitmqctl -n rabbit2 reset`
    + 更多操作 http://www.rabbitmq.com/rabbitmqctl.8.html
+ 镜像队列
  + 
## rabbitMQ 介绍

#### 消息状态：
+ ready：消费者还能从队列中获取到消息的条数
+ unacked: 消费者还没有返回确认的消息条数
+ total： 队列中消息总数 total=ready+unacked

#### simple queue
![simple](https://note.youdao.com/yws/api/personal/file/0408AB1D723E4EE7BAEA1D54873B447A?method=download&shareKey=8dea1b6829f328b850f8e62b728f561b "simple")
+ 默认exchange，队列名作为routingKey
#### work queue
![work](https://note.youdao.com/yws/api/personal/file/4234BE2F91204BCE8A0753314FD9DE81?method=download&shareKey=8dea1b6829f328b850f8e62b728f561b "work")
+ 轮询分发Round-robin dispatching
    + 1.消息队列中的消息/新产生的消息，轮询现有消费者，平均分配到每个消费者
    + 2.如果确定其中某个消费者无法**确认消息(ack)**，没确认的消息会变为ready，再次轮询分配到现有消费者
    + 3.ready数量为0表示所有消息已指定消费者，无法再被其他消费者消费。即使有空闲的消费者，而某个消费者还有大量消息没有消费完也不会分配到空闲的消费者
    + 4.total = ready + unacked
    + 5.当autoAck = true时，消费者接收到消息就进行了ack，并不代表消费者完成了消费回调
    + 6.当autoAck = false时，需要在消费回调中主动ack消息，一般在消费完成之后进行ack，表示消息消费完成
+ 公平分发Fair dispatch
    + 1.设置每次发送给消费者的消息数量qos=N，此时autoAck只能为false，也就是必须手动确认消息
    + 2.每次消费 ready - N, unacked + N
    + 4.N条消息全部确认，才会继续发送给该消费者下N条消息
    + 5.ready不为0时，就不会出现空闲的消费者，解决了消费能力差的消费端消息堆积的问题

#### publish/subscribe
![fanout](https://note.youdao.com/yws/api/personal/file/D84E36561AF74C01BC5973FECB63D8E6?method=download&shareKey=8dea1b6829f328b850f8e62b728f561b "fanout")
+ fanout类型的exchange，会将所有发送到该exchange的消息发送到所有绑定的队列，跟routingKey无关
+ 其中某一队列遵从work队列的轮询分发和公平分发

#### routing
![routing](https://note.youdao.com/yws/api/personal/file/D7D1FC56D531442CB7987D98CC3D402C?method=download&shareKey=8dea1b6829f328b850f8e62b728f561b "routing")
+ direct类型的exchange，队列绑定到交换机时需指定routingKey

#### topic
![topic](https://note.youdao.com/yws/api/personal/file/D967CD3D1A8D4D24B187134E388C37A1?method=download&shareKey=8dea1b6829f328b850f8e62b728f561b "topic")
+ 匹配字符
    + \* 星号匹配一个词
    + \# 井号匹配0个或多个词
+ routingKey 可以通过通配符进行路由

#### rpc
![rpc](https://note.youdao.com/yws/api/personal/file/3DB783E67D6B47F5AC031DA3F5B6E198?method=download&shareKey=8dea1b6829f328b850f8e62b728f561b "rpc")
+ 生产者在发送消息的配置属性中，配置好correlation_id和reply_to队列
+ 消费者接收到消息，处理好消息再将处理结果发送到reply_to队列，结果同时配置好correlation_id
+ 生产者发送消息后同步等待reply_to队列，拿到消息匹配结果同时配置好correlation_id成功后，取出处理结果

#### 消息确认机制与消息持久化
+ 消费端消息应答
  + 自动确认模式
    `boolean autoAck = true;`<br/>
    **一旦rabbitmq把消息分发给消费者，就会从broker的内存中删除。**<br>
    这种情况下如果杀死正在执行的消费者，就会丢失正在处理的消息。
  + 消息应答模式
    `boolean autoAck = false;`<br/>
    如果有一个消费者挂掉，就会交付给其他消费者，rabbitmq支持消息应答，消费者发送一个消息应答告诉rabbitmq这个消息我已经处理完成，可以删除，然后rabbitmq就删除内存中的消息
  + 消息应答模式默认开启，即`boolean autoAck = false;`
+ 生产者消息确认
  + 事务机制
    + client发送Tx.Select 开启事务
    + broker发送Tx.Select-Ok(之后publish)
    + client发送Tx.Commit 提交事务
    + broker发送Tx.Commit-Ok 
    + 性能很差
  + confirm模式
    + publisher confirm模式并不是默认打开的，需要调用confirm.select方法将channel设置成confirm模式。当开启了confirm模式之后，只有当一条消息被所有的mirrors接受之后，publisher才会收到这条消息的confirm，也就是一个basic.ack方法。
    + waitForConfirms 同步confirm，单个或批量确认消息
    + addConfirmListener 异步confirm
    + 性能好
+ 消息持久化
  + `boolean durable = true;` 开启持久化
  + `boolean durable = false;` 关闭持久化
  + **rabbitmq 不允许重新定义（不同参数）已存在的队列**
#### 部分api
+ exchangeDeclare 
  + exchange，交换器名称 
  + type，交换器类型，如fanout,direct,topic
  + durable，是否持久化，持久化可以将交换器存盘，在服务器重启后不会丢失相关信息
  + autoDelete，是否自动删除。自动删除的前提是至少有一个队列或交换器与这个交换器绑定，之后所有与这个交换器绑定的队列或交换器都与此解绑，并不是与此连接的客户端都断开
  + internal，是否是内置的，内置的交换器客户端无法直接发送消息到这个交换器中，只能通过交换器路由到交换器的方式
  + argument 其他一些结构化参数
+ queueDeclare
  + queue，队列名称
  + durable，是否持久化
  + exclusive，是否排他，如果一个队列声明为排他，该队列仅对首声声明它的连接可见，并在连接断开后自动删除
  + autoDelete，是否自动删除。自动删除前提是：至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时，才自动删除
  + arguments，其他参数
    + x-message-ttl 队列中消息过期时间
    + x-max-priority 1.队列开启优先级，2.队列最大优先级
    + x-expires 队列的过期时间，当队列未使用(没有任何消费者、没有被重新声明、过期时间段内未调用过Basic.Get命令)时，会被删除。服务器重启后，持久化的队列过期时间会重新计算，x-expires单位为毫秒，不能设置为0
+ basicPublish
  + exchange 交换器
  + routingKey 路由键
  + mandatory 告诉服务器至少将消息路由到一个队列中，否则将消息返回生产者(basic.return)，为false则丢弃
  + immediate 是否立即投递，告诉服务器，如果该消息关联的队列上有消费者，立即投递，没有消费者，则返回消息给生产者，不用将消息存入队列等待
  + props 
    + expiration 单条消息过期时间（若x-message-ttl也设置，取最短的ttl。由队列属性设置的过期时间，消息到期后，会直接从队列中抹去；单独设置设置的，在投递前进行判断）
    + priority 优先级
+ basicConsume 
  + queue，队列名称
  + autoAck，是否自动确认，建议为false，不自动确认
  + callback，消费者的回调函数
## RabbitMQ 应用
#### MQ基本应用
+ 跨系统异步通信
+ 多应用间解耦
+ 应用内部业务异步处理
+ 事件驱动架构
+ 流量削峰
+ 日志处理
+ 通讯
+ 用到特殊的路由规则，发布订阅等业务场景
+ 延迟通知（延迟队列）

#### 例：延迟队列
场景：订单生单成功后，30分钟未支付自动取消
###### 消息的ttl
+ 队列设置：
  + x-expires：队列ttl，当队列未使用(没有任何消费者、没有被重新声明、过期时间段内未调用过Basic.Get命令)时，会被删除。服务器重启后，持久化的队列过期时间会重新计算，x-expires单位为毫秒，不能设置为0
  + x-message-ttl ：队列中消息的ttl
+ 发布消息设置：
  + expiration：单条消息ttl（若x-message-ttl也设置，取最短的ttl）
+ 消息抛出
  + 消息过期后，抛出需要满足两个条件，1个是消息过期，1个实在队列头部 
  + 若消息已过期，并且不在头部，当消费者消费掉头部未过期的消息之后，过期的消息仍会被抛出，不会被消费者消费掉。
###### 死信交换器DLX：dead letter exchange
+ dlx和普通交换器并没有区别，可以绑定多个队列
+ 队列声明的时候，可以设置死信参数
  + x-dead-letter-exchange 指定队列的死信交换器
  + x-dead-letter-routing-key 指定dlx的路由键，可与延迟队列的路由键不同
+ 消息满足一定的条件会进入死信路由
  +  一个消息被Consumer拒收了，并且reject方法的参数里requeue是false。也就是说不会被再次放在队列里，被其他消费者使用。
  +  消息过期
  +  队列的长度限制满了。排在前面的消息会被丢弃或者扔到死信路由上。
###### 延迟队列的实现
![](https://note.youdao.com/yws/api/personal/file/B7DE169A173E4B29B07664A43C47918A?method=download&shareKey=7a23ade64eb533f5b45c88080b1203a3)
+ queue1作为延迟队列，用于存储消息等到过期后转到任务队列
+ queue2作为任务队列，已经经过延迟的消息，在这里被消费

## RabbitMQ分布式
#### mq组件
+ RabbitMQ进程模型
  ![](https://note.youdao.com/yws/api/personal/file/9833BA112BF047239AD29034BA466781?method=download&shareKey=3e260a704bc75c4842bf7ba13f43bf42)
+ RabbitMQ流控
  + RabbitMQ可以对内存和磁盘使用量设置阈值，当达到阈值后，生产者将被阻塞（block），直到对应项恢复正常。除了这两个阈值，RabbitMQ在正常情况下还用流控（Flow Control）机制来确保稳定性
+ amqqueue进程与Paging 
  ![](https://note.youdao.com/yws/api/personal/file/D67C4326163A4CC48EB26A7C67BFA4FC?method=download&shareKey=0c85ca5627f978e0274e6b52d8204836)
+ RabbitMQ使用两个参数来限制使用系统的内存，避免系统被自己独占。
  +  `[{rabbit, [{vm_memory_high_watermark_paging_ratio, 0.75},          {vm_memory_high_watermark, 0.4}]}].  `
  + `vm_memory_high_watermark`：表示RabbitMQ使用内存的上限为系统内存的40%
  + `vm_memory_high_watermark_paging_ratio`：表示当RabbitMQ达到0.4*0.75=30%，系统将对queue中的内容启用paging机制，将message等内容换页到disk 中。 
+ 节点类型 disc | ram
  + 两个节点做测试 disc + ram
    + 


#### 集群

![](https://note.youdao.com/yws/api/personal/file/2A90C81A5C1B41368A7DEF36DB02E7BB?method=download&shareKey=818e439636a98839c71e8260bfbb61ab)
+ 集群中每个节点都存有一份元数据
  +  exchange meta： 交换器名称、类型和属性
  +  binding meta： 如何将消息路由到队列
  +  queue mata： 队列名称和它的属性
  +  vhost meta: 为vhost内的队列、交换器和绑定提供命名空间和安全属性
+ 队列中的


#### 镜像队列
