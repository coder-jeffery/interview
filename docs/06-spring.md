# Spring 框架

> 面试权重：必会（中小厂问得更多；大厂会往事务、循环依赖、代理上挖）  
> 适合层级：所有人  
> 启动、自动装配、Starter、Actuator 看 [Spring Boot](13-spring-boot.md)；网关/发现/Feign/Sentinel 看 [Spring Cloud](14-spring-cloud.md)。

## Q1: IoC / DI 解决什么问题？Bean 生命周期

**30 秒答法**  
IoC 把对象创建和依赖装配交给容器，业务代码依赖接口而不是 `new`。生命周期（简化口述）：

1. 解析配置（注解/`@Bean`/自动装配）  
2. 实例化  
3. 填充属性（注入）  
4. `BeanNameAware` / `BeanFactoryAware` 等 Aware  
5. `BeanPostProcessor.postProcessBeforeInitialization`  
6. `@PostConstruct` / `InitializingBean.afterPropertiesSet` / `init-method`  
7. `postProcessAfterInitialization`（**AOP 代理通常在这里包一层**）  
8. 使用  
9. `@PreDestroy` / `DisposableBean` / `destroy-method`

**追问**  
- `BeanFactory` vs `ApplicationContext`？（后者是前者的超集：事件、国际化、AOP、Web）  
- 为什么注入推荐构造器？（不可变、必依赖、便于测试、避免循环时半初始化字段）  
- `FactoryBean` 和普通 Bean？（`getObject` 才是产品；`&beanName` 才是工厂本身）

## Q2: 循环依赖 Spring 怎么破？为什么构造器循环破不了？

**30 秒答法**  
单例 + 字段/Setter 注入：三级缓存。

| 缓存 | 放什么 |
|------|--------|
| singletonObjects | 成品 |
| earlySingletonObjects | 早期暴露的引用（可能已是代理） |
| singletonFactories | 能生产早期引用的工厂，用于需要时生成代理 |

A 实例化后先把自己工厂放进三级缓存，再注入 B；B 需要 A 时从工厂拿到早期 A（可能是代理），B 完成；A 再注入 B，A 完成。  
**构造器注入循环：** 实例化都走不完，没有「半成品」可暴露，默认失败。`@Lazy` 是常见权宜之计，本质是推迟，不是设计胜利。

**追问**  
- 为什么要三级而不是两级？（AOP：要在早期暴露**代理**而不是原始对象，工厂延迟决定是否造代理）  
- 原型 Bean 能循环依赖吗？（不能按单例那套解决）  
- 循环依赖是不是代码坏味道？（是。拆模块、事件、中间层，不要把 `@Lazy` 当架构）

## Q3: AOP 原理、JDK 代理 vs CGLIB

**30 秒答法**  
切面在运行时用代理把增强织进目标方法。接口优先时 JDK 动态代理；类代理用 CGLIB/ByteBuddy 生成子类。Spring Boot 2.x+ 默认常是 CGLIB（`proxyTargetClass=true` 一类配置）。  
**自调用不走代理：** `this.b()` 不会进切面，事务/缓存注解失效。拆到另一个 Bean，或注入自身（仍是权宜）。

**追问**  
- 通知顺序？（`@Around` 能包住整个调用；同类多个切面用 `@Order`，数字小的靠外）  
- `final` 方法能被 CGLIB 增强吗？（不能，子类无法覆盖）  
- AspectJ 编译期织入和 Spring AOP 差在哪？（后者只方法调用、只 Spring Bean）

## Q4: Spring 事务为什么会失效？传播级别怎么讲？

**30 秒答法**  
`@Transactional` 本质是代理 + `PlatformTransactionManager`（数据源事务：连接绑定到 ThreadLocal）。失效常见原因：

1. 方法不是 public（标准代理模式）  
2. 自调用  
3. 异常被吃掉 / 抛的是 checked 且没设 `rollbackFor`  
4. 多线程：子线程拿不到同一 ThreadLocal 连接  
5. 非 Spring 管理的对象  
6. 库不支持事务（某些 NoSQL 数据源）

默认只回滚 `RuntimeException`/`Error`。

传播（背最常用三个就行）：

| 传播 | 含义 |
|------|------|
| `REQUIRED`（默认） | 有就加入，没有就新建 |
| `REQUIRES_NEW` | 挂起当前，新开一个；内层回滚不影响已提交的外层，但要注意锁与超时 |
| `NOT_SUPPORTED` | 挂起事务，非事务执行 |

**追问**  
- 大事务有什么害？（锁持有久、连接占用、回滚段）怎么拆？（查询先出事务，写入收口；消息发在事务后，或 outbox）  
- `REQUIRES_NEW` 在同一数据源上怎么实现？（再拿一条连接，连接池要够）  
- 只读事务？（驱动/方言可能优化；不是「一定不写」的安全保证）

## Q5: Spring MVC 一次请求怎么走？

**30 秒答法**  
`DispatcherServlet` 收请求 → 处理器映射找 Controller → 适配器调方法 → 参数解析/校验 → 业务 → 返回值处理（视图或 `@ResponseBody` 消息转换）→ 异常解析器。拦截器：`preHandle` / `postHandle` / `afterCompletion`。Filter 在 Servlet 容器层，比拦截器更早，适合鉴权、包装 Request。

**追问**  
- 拦截器和 AOP 谁先？（Filter → 拦截器 → Controller，AOP 在 Bean 方法上，可能包住 Controller 方法）  
- 全局异常怎么做？（`@ControllerAdvice` + `@ExceptionHandler`，别在每个 Controller try-catch）

## Q6: Spring Boot 自动装配（摘要）

**30 秒答法**  
起步依赖引入一堆库；`@SpringBootApplication` = `@Configuration` + `@ComponentScan` + `@EnableAutoConfiguration`。自动装配：`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`（Boot 2.7+；老的是 `spring.factories`）列出配置类，配合 `@ConditionalOnClass` / `OnMissingBean` / `OnProperty` 决定生不生效。所以「引入 starter 就有 DataSource」是条件装配，不是魔法。

完整启动链、自定义 Starter、配置优先级、Actuator、Boot 3 见 [Spring Boot](13-spring-boot.md)。

**追问**  
- 如何自定义 starter？（自动配置类 + 条件 + 配置属性 + imports 文件）  
- 如何排除某条自动配置？（`exclude`、或自己先声明 Bean 让 `OnMissingBean` 失败）  
- `@SpringBootApplication` 扫描范围？（默认当前包及子包，放错包会「明明写了 @Service 却没注入」）

## Q7: `@Transactional` + 缓存 / 消息 的经典坑

**30 秒答法**  
先写库再发消息：消息可能在事务提交前被消费到，读不到数据 → **事务后提交再发**，或 **Outbox 表**。  
先删缓存再写库：写失败会空缓存；先写库再删缓存仍有并发窗口，要用延迟双删或订阅 binlog（Canal）做失效。面试讲清楚**窗口存在**，比声称「我们用了最佳方案」更可信。

## Q8: Spring Boot 3 / 云原生相关（点到即可）

- `javax.*` → `jakarta.*`  
- 可观测性：Micrometer Tracing，替很多老 Sleuth 用法  
- Native Image（GraalVM）：反射要配置，启动快、内存低，构建复杂  
- 虚拟线程：嵌入式容器可切虚拟线程执行器（见 [Java 新特性](05-java-modern.md)）

展开与升级路径见 [Spring Boot Q11](13-spring-boot.md)。没做过 Native 就不要展开。
