# ElasticSearch入门教程



## 1、ElasticSearch简介



## 2、ElasticSearch与Solr的对比



## 3、ElasticSearch相关概念



## 4、ElasticSearch客户端操作

实际开发中，主要有三种方式可以作为es服务的客户端：

- 使用elasticsearch-head插件
- 使用elasticsearch提供的Restful接口直接访问
- 使用elasticsearch提供的API（即ES提供给各语言的SDK）进行访问



### 4.1 使用Restful接口直接访问

我们需要使用http请求，介绍两款接口测试工具：postman和Talend API tester。

其中我们选择基于Chrome浏览器插件的Talend API tester。



#### 4.1.1 索引APIs

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