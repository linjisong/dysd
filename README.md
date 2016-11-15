#DYSD
主旨在简化程序员开发任务

目前暂分为两个部分：

## dysd-util：工具包

基于apache commons、spring-core等构建的工具包

主要包括：

* 通用工具类Tool
* 日志
* 异常
* 配置
* 资源文件及XML文件解析
* Spring工具包



## dysd-dao：数据访问包

在mybatis基础之上进行扩展的数据访问

主要包括：

* 数据库方言
* 存储过程调用
* 物理分页
* 流式查询
* 批量执行
* 配置优化
  * 引入XSD进行SqlMapper配置校验
  * 语句级元素解析器
  * 脚本级元素解析器
  * SQL配置函数
  * 使用SpEL表达式配置SqlMapper



有太多不完善的，虽然贻笑方家，但也算是自己的一些实践。