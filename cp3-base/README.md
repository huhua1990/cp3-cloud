# cp3-base

## 仅供个人学习使用

## 简介
`cp3-base` 是 [cp3-cloud](https://github.com/huhua1990/cp3-cloud) 项目的基础核心工具包，开发宗旨是打造一套兼顾 SpringBoot 和 SpringCloud 项目的公共基础工具类。

## 结构介绍
* cp3-annotation
* cp3-boot
* cp3-cache-starter
* cp3-cloud-starter
* cp3-core
* cp3-databases
* cp3-dependencies
* cp3-dozer-starter
* cp3-injection-starter
* cp3-jwt-starter
* cp3-log-starter
* cp3-mq-starter
* cp3-security-starter
* cp3-swagger2-starter
* cp3-uid
* cp3-validator-starter
* cp3-xss-starter
* cp3-zipkin-client-starter

## 主要功能点
- Mvc封装： 通用的 Controller、Service、Mapper、全局异常、全局序列化、反序列化规则
- SpringCloud封装：请求头传递、调用日志、灰度、统一配置编码解码规则等
- 关联数据注入：优雅解决 跨库表关联字段回显、跨服务字段回显
- 持久层增强：增强MybatisPlus Wrapper操作类、数据权限
- 枚举、字典等字段统一传参、回显格式： 解决前端即要使用编码，有要回显中文名的场景。
- 在线文档：对swagger、knife4j二次封装，实现配置即文档。
- 后端表单统一校验：本组件将后端配置的jsr校验规则返回给前端，前端通过全局js，实现统一的校验规则。
- 缓存：封装redis缓存、二级缓存等，实现动态启用/禁用redis
- XSS： 对表单参数、json参数进行xss处理
- 统一的操作日志： AOP方式记录操作日志
- 接口权限
- 快去看源码和文档，发现更多功能吧


## 项目代码地址
* cp3-base | https://github.com/huhua1990/cp3-base | 核心工具类：boot和cloud 项目的公共抽象 |
* cp3-cloud | https://github.com/huhua1990/cp3-cloud | 微服务项目 |

如果觉得对您有任何一点帮助，请点右上角 "Star" 支持一下吧，谢谢！

## 友情链接 & 特别鸣谢
* MyBatis-Plus：[https://mybatis.plus/](https://mybatis.plus/)
* knife4j：[http://doc.xiaominfo.com/](http://doc.xiaominfo.com/)
* hutool：[https://hutool.cn/](https://hutool.cn/)
* kkfileview：[https://kkfileview.keking.cn](https://kkfileview.keking.cn)
* lamp: [https://github.com/zuihou](https://github.com/zuihou)
* j2cache：[https://gitee.com/ld/J2Cache](https://gitee.com/ld/J2Cache)

