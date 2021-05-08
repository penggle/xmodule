# ElasticSearch入门教程



## 1、ElasticSearch简介

> Elaticsearch，简称为es， es是一个开源的高扩展的分布式全文检索引擎，它可以近乎实时的存储、检索数据；本
> 身扩展性很好，可以扩展到上百台服务器，处理PB级别的数据。es也使用Java开发并使用Lucene作为其核心来实
> 现所有索引和搜索的功能，但是它的目的是通过简单的RESTful API来隐藏Lucene的复杂性，从而让全文搜索变得
> 简单。

## 2、ElasticSearch与Solr的对比

- Solr 利用 Zookeeper 进行分布式管理，而 Elasticsearch 自身带有分布式协调管理功能;
- Solr 支持更多格式的数据，而 Elasticsearch 仅支持json文件格式；
- Solr 官方提供的功能更多，而 Elasticsearch 本身更注重于核心功能，高级功能多有第三方插件提供；
- Solr 在传统的搜索应用中表现好于 Elasticsearch，但在**处理实时搜索应用时效率明显低于 Elasticsearch**；

## 3、ElasticSearch安装与配置

### 3.1、安装

> 注意：**es使用java开发，使用lucene作为核心，需要配置好java环境！（jdk1.8以上）**

![es01](https://img-blog.csdnimg.cn/20200520150547117.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2NjM5MjMy,size_16,color_FFFFFF,t_70)

### 3.2、配置

- JDK配置

  es最低支持JDK1.8，如果配置了ES_JAVA_HOME则优先使用该JDK，否则使用JAVA_HOME指定的JDK。

  当然较新版本(7.x)的es都自带了JDK(见es的安装目录下的jdk目录)。

- config/jvm.option文件

  ```shell
  #设置heap大小
  -Xms1g
  -Xmx1g
  ```

- config/elasticsearch.yml文件

  ```yaml
  #设置集群名称
  cluster.name: es-examples
  #设置节点名称
  node.name: node-1
  #node.name: ${HOSTNAME}
  #es根目录下的data目录
  path.data: data
  #es根目录下的logs目录
  path.logs: logs
  #支持跨域请求
  http.cors.enabled: true
  http.cors.allow-origin: "*"
  #自动创建索引规则(+代表允许自动创建,-代表不允许自动创建)
  action.auto_create_index: +aaa,-bbb,+ccc
  #完全关闭自动创建索引
  #action.auto_create_index: false
  ```

### 3.3、启动

点击ElasticSearch下的bin目录下的elasticsearch.bat启动。

启动时也可带上`elasticsearch.yml`配置文件中的配置参数(有点类似于SpringBoot)：

```shell
./bin/elasticsearch.bat -Ecluster.name=my_cluster -Enode.name=node_1
```

观察启动控制台日志，无异常启动成功后，在浏览器输入http://127.0.0.1:9200，出现以下内容，代表es启动成功：

```json
{
  "name" : "node-1",
  "cluster_name" : "es-examples",
  "cluster_uuid" : "k-Hoy57hS-qu6a_2KowlDA",
  "version" : {
    "number" : "7.12.1",
    "build_flavor" : "default",
    "build_type" : "zip",
    "build_hash" : "3186837139b9c6b6d23c3200870651f10d3343b7",
    "build_date" : "2021-04-20T20:56:39.040728659Z",
    "build_snapshot" : false,
    "lucene_version" : "8.8.0",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```

[以windows服务形式安装es](https://www.elastic.co/guide/en/elasticsearch/reference/current/zip-windows.html#windows-service)

### 3.4、安装图形化插件([elasticsearch-head](https://github.com/mobz/elasticsearch-head))

chrome浏览器中安装elasticsearch-head插件



## 4、ElasticSearch相关概念

### 4.1、概述

Elasticsearch是面向文档(document oriented)的，这意味着它可以存储整个对象或文档(document)。然而它不仅
仅是存储，还会索引(index)每个文档的内容使之可以被搜索。在Elasticsearch中，你可以对文档（而非成行成列的数据）进行索引、搜索、排序、过滤。Elasticsearch比传统关系型数据库如下：

> Relational DB ‐> Databases ‐> Tables ‐> Rows            ‐> Columns
> Elasticsearch  ‐> Indices       ‐> Types  ‐> Documents ‐> Fields

### 4.2、核心概念

- ##### 索引库（indices)

  > indices是index的复数，代表许多的索引，即一系列索引的集合。**可类比mysql中的数据库**

- ##### 类型（type）

  > 类型是模拟mysql中的table概念，一个索引库下可以有不同类型的索引，比如商品索引，订单索引，其数据格式不同。不过这会导致索引库混乱，因此未来版本中会移除这个概念

- ##### 文档（document）

  > 存入索引库原始的数据。比如每一条商品信息，就是一个文档，类似于数据库的一行数据

- ##### 字段（field）

  > 文档中的属性，类似于数据库中列

- ##### 映射配置（mapping）

  > mapping是处理数据的方式和规则方面做一些限制，如某个字段的数据类型、默认值、分析器、是否被索引等等，这些都是映射里面可以设置的，其它就是处理es里面数据的一些使用规则设置也叫做映射，按着最优规则处理数据对性能提高很大，因此才需要建立映射，并且需要思考如何建立映射才能对性能更好。**相当于mysql中的创建表的过程，设置主键外键等等**

- ##### 集群（cluster）

  > 一个集群就是由一个或多个节点组织在一起，它们共同持有整个的数据，并一起提供索引和搜索功能。一个集群由 一个唯一的名字标识，这个名字默认就是“elasticsearch”。这个名字是重要的，因为一个节点只能通过指定某个集 群的名字，来加入这个集群。

- ##### 节点（node）

  > 一个节点是集群中的一个服务器，作为集群的一部分，它存储数据，参与集群的索引和搜索功能。和集群类似，一 个节点也是由一个名字来标识的，默认情况下，这个名字是一个随机的漫威漫画角色的名字，这个名字会在启动的 时候赋予节点。这个名字对于管理工作来说挺重要的，因为在这个管理过程中，你会去确定网络中的哪些服务器对 应于Elasticsearch集群中的哪些节点。

  > 一个节点可以通过配置集群名称的方式来加入一个指定的集群。默认情况下，每个节点都会被安排加入到一个叫 做“elasticsearch”的集群中，这意味着，如果你在你的网络中启动了若干个节点，并假定它们能够相互发现彼此， 它们将会自动地形成并加入到一个叫做“elasticsearch”的集群中。

  > 在一个集群里，只要你想，可以拥有任意多个节点。而且，如果当前你的网络中没有运行任何Elasticsearch节点， 这时启动一个节点，会默认创建并加入一个叫做“elasticsearch”的集群。

- ##### 分片和复制（shards&replicas）

  > 一个索引可以存储超出单个结点硬件限制的大量数据。比如，一个具有10亿文档的索引占据1TB的磁盘空间，而任一节点都没有这样大的磁盘空间；或者单个节点处理搜索请求，响应太慢。为了解决这个问题，Elasticsearch提供了将索引划分成多份的能力，这些份就叫做分片。当你创建一个索引的时候，你可以指定你想要的分片的数量。每个分片本身也是一个功能完善并且独立的“索引”，这个“索引”可以被放置到集群中的任何节点上。分片很重要，主要有两方面的原因： 1）允许你水平分割/扩展你的内容容量。 2）允许你在分片（潜在地，位于多个节点上）之上进行分布式的、并行的操作，进而提高性能/吞吐量。

  > 至于一个分片怎样分布，它的文档怎样聚合回搜索请求，是完全由Elasticsearch管理的，对于作为用户的你来说，这些都是透明的。

  > 在一个网络/云的环境里，失败随时都可能发生，在某个分片/节点不知怎么的就处于离线状态，或者由于任何原因消失了，这种情况下，有一个故障转移机制是非常有用并且是强烈推荐的。为此目的，Elasticsearch允许你创建分片的一份或多份拷贝，这些拷贝叫做复制分片，或者直接叫复制。

  > 复制之所以重要，有两个主要原因： 在分片/节点失败的情况下，提供了高可用性。因为这个原因，注意到复制分片从不与原/主要（original/primary）分片置于同一节点上是非常重要的。扩展你的搜索量/吞吐量，因为搜索可以在所有的复制上并行运行。总之，每个索引可以被分成多个分片。一个索引也可以被复制0次（意思是没有复制）或多次。一旦复制了，每个索引就有了主分片（作为复制源的原来的分片）和复制分片（主分片的拷贝）之别。分片和复制的数量可以在索引创建的时候指定。在索引创建之后，你可以在任何时候动态地改变复制的数量，但是你事后不能改变分片的数量。

  > **默认情况下，Elasticsearch中的每个索引被分片5个主分片和1个复制**，这意味着，如果你的集群中至少有两个节点，你的索引将会有5个主分片和另外5个复制分片（1个完全拷贝），这样的话每个索引总共就有10个分片。



## 5、ElasticSearch客户端操作

实际开发中，主要有三种方式可以作为es服务的客户端：

- 使用elasticsearch-head插件
- 使用elasticsearch提供的Restful接口直接访问
- 使用elasticsearch提供的API（即ES提供给各语言的SDK）进行访问



### 5.1 使用Restful接口直接访问

我们需要使用http请求，介绍两款接口测试工具：postman和Talend API tester。

其中我们选择基于Chrome浏览器插件的Talend API tester。



#### 5.1.1 索引APIs

1. ##### 索引管理APIs

   - ###### 创建索引(Create Index)

     - **请求**

       ```http
       PUT /<index>
       ```

     - **描述**

       创建一个新的索引。您可以使用create index API向Elasticsearch集群添加新索引。创建索引时，可以指定以下参数:

       - 索引设置
       - 索引中字段的映射
       - 索引别名

     - **Path参数**

       **<index>**，字符串类型的索引名称。索引名称必须满足以下条件：

       - 只能是小写形式
       - 不能包含：`\`, `/`, `*`, `?`, `"`, `<`, `>`, `|`, ` ` (space character), `,`, `#`
       - 7.0之前的索引可以包含冒号(:)，但这已被弃用，在7.0+中将不支持
       - 不能以-，_，+开头
       - 不能是`.`或`..`
       - 不能超过255字节(**注意，它是字节**，所以多字节字符将更快地计数到255限制)
       - 以`.`开头的索引将会被弃用，插件管理的隐藏索引和内部索引外除外

     - **Query参数**

       - **wait_for_active_shards**

         (可选，字符串)操作前必须激活的分片副本数量。设置为1到索引中的碎片总数(number_of_replicas+1)。默认值:1，即主分片。

       - **master_timeout**

         (可选，时间单位)**等待连接**到主节点的时间。如果在超时时间到期之前没有收到响应，则请求失败并返回错误。默认为30秒。

       - **timeout**

         (可选，时间单位)**等待响应**的时间。如果在超时时间到期之前没有收到响应，则请求失败并返回错误。默认为30秒。

     - 

   - 

2. ##### 映射管理APIs

##### 