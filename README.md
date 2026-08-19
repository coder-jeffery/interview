# Java 面试指南

面向 **Java 后端面试** 的高频问答手册。按面试官真实追问路径组织：先给 30 秒口述，再补原理、追问和踩坑。

默认按 **3–8 年中高级** 深度写。校招/初级可先走「必会」路径；资深/架构可把每题的追问当作压力测试。

## 怎么用

1. 先看 [备战策略](docs/00-how-to-prepare.md)，按层级选路径，不要从第一页线性刷到最后。
2. 每道题先自己口述 30 秒，再对照「30 秒答法」。说不出来的，才看原理。
3. 「追问」是面试官下一步会问的内容。能答上追问，才算过关。
4. 简历上写过的技术，对应章节必须能讲到代码/事故/指标，不能只背定义。

| 标记 | 含义 |
|------|------|
| 必会 | 绝大多数 Java 后端面试都会问 |
| 高频 | 中大厂、中高级面试高频 |
| 加分 | 能拉开档次，尤其 Java 21+ / 线上排查 / 架构取舍 |

## 学习路径

```text
校招 / 初级          Java 基础 → 集合 → MySQL → 网络 → Spring / Boot 入门
中级（3–5 年）       + 并发 → Redis → Spring 事务/循环依赖 → Boot 装配/配置 → 场景题
高级（5 年+）        + JVM 调优 → Spring Cloud 故障治理 → MQ 可靠性 → 项目深挖
```

**投入优先级（多数岗位）：** MySQL + Redis ≥ Java 集合/并发 > JVM > Spring 原理 > Boot 装配 > Cloud 组件名。

## 目录

### 备战

- [备战策略：层级、节奏、简历红线](docs/00-how-to-prepare.md)
- [项目深挖与场景题](docs/12-scenario.md)
- [临场速记](docs/99-cheatsheet.md)

### Java 核心

- [Java 基础](docs/01-java-basics.md) · 必会
- [集合](docs/02-collections.md) · 必会
- [并发](docs/03-concurrency.md) · 高频
- [JVM](docs/04-jvm.md) · 高频（中大厂）
- [Java 17 / 21 / 25](docs/05-java-modern.md) · 加分

### 框架与存储

- [Spring 框架](docs/06-spring.md) · 必会
- [Spring Boot](docs/13-spring-boot.md) · 必会
- [Spring Cloud](docs/14-spring-cloud.md) · 高频
- [MySQL](docs/07-mysql.md) · 必会
- [Redis](docs/08-redis.md) · 必会

### 分布式与基础

- [消息队列](docs/09-mq.md) · 高频
- [分布式与微服务](docs/10-distributed.md) · 高频
- [计算机网络](docs/11-network.md) · 必会

## 口述模板

遇到原理题，按这四句说，比堆名词更稳：

1. **是什么**：一句话定义 + 使用场景。
2. **为什么**：解决什么问题，不用会怎样。
3. **怎么做**：关键机制 / 关键参数 / 关键代码路径。
4. **坑在哪**：失效条件、性能代价、你在项目里怎么规避。

## 约定

- 默认运行时按 **Java 21 LTS** 讲，并注明与 8/11/17 的差异；**Java 25** 作为最新 LTS 补充。
- 集合/并发默认按 **HotSpot + OpenJDK** 实现讨论，不把规范与实现混为一谈。
- 不保证覆盖所有边角 API。目标是：**面试能讲清楚，追问能接住，项目能对上。**
