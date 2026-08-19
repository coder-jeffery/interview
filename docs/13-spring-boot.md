# Spring Boot

> 面试权重：必会（中小厂问启动与自动装配；中高级挖配置、Starter、Actuator、升级）  
> 适合层级：所有人  
> 和 [Spring 框架](06-spring.md) 的分工：本章讲 **Boot 怎么把 Spring 跑起来**；IoC / AOP / 事务仍看框架章。

## Q1: Spring、Spring MVC、Spring Boot 各解决什么？

**30 秒答法**  
Spring：IoC/AOP，把对象和横切从业务里拆出去。  
Spring MVC：Servlet 上的 Web 框架，`DispatcherServlet` 管请求。  
Spring Boot：约定大于配置，用自动装配、嵌入式容器、起步依赖，把「能跑的生产应用」的默认选择填好。不是新容器模型，是**装配与运行方式**。

**追问**  
- 没有 Boot 能不能做 Web？（能，自己配 `web.xml`/Java Config、Tomcat、JSON、日志，只是烦）  
- Boot 是不是必须嵌入式 Tomcat？（不是。可打 war 外置容器；也可 WebFlux + Netty）  
- 「约定大于配置」翻车现场？（扫描包放错、同名配置覆盖、starter 把你不想要的 DataSource 拉起来）

## Q2: `SpringApplication.run` 启动过程（口述一条链）

**30 秒答法**  

1. 推断应用类型：Servlet / Reactive / None  
2. 加载 `ApplicationContextInitializer`、`ApplicationListener`（`spring.factories` / `Loader`）  
3. 准备 Environment：配置文件、命令行、系统属性、随机值  
4. 打印 Banner、创建 `ApplicationContext`  
5. 准备 Context：把 Environment 塞进去，触发 initializer  
6. **刷新容器**：Bean 定义加载 → 自动装配 → 实例化（核心在这里）  
7. 执行 `ApplicationRunner` / `CommandLineRunner`  
8. 发出 `ApplicationReadyEvent`；失败走 `FailureAnalyzer`

面试画这 8 步就够。别背源码行号。重点说：**Environment 先于 Bean 创建，所以 `@Value` 在构造阶段就能用；自动装配发生在 refresh。**

**追问**  
- `ApplicationRunner` vs `CommandLineRunner`？（前者参数已解析为 `ApplicationArguments`；都在容器就绪后、对外服务前。跑耗时任务会拖垮就绪探针）  
- `SmartInitializingSingleton` 呢？（所有单例创建完回调，比 Runner 更早）  
- 启动慢怎么查？（`--debug` 看自动配置报告；`spring-startup-report` / JFR；常见元凶：组件扫描过大、Hibernate、同步连 DB/MQ）

**踩坑**  
在 `main` 里 `run()` 返回前做业务初始化，和健康检查竞态：K8s 已经转发流量，Runner 还在跑。耗时初始化用懒加载或独立 job。

## Q3: 自动装配到底怎么生效？

**30 秒答法**  
`@SpringBootApplication` = `@SpringBootConfiguration` + `@ComponentScan` + `@EnableAutoConfiguration`。  
`@EnableAutoConfiguration` 导入 `AutoConfigurationImportSelector`，读取：

- Boot 2.7+：`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- 更早：`META-INF/spring.factories` 的 `EnableAutoConfiguration`

每个自动配置类是普通 `@Configuration`，用条件注解决定生不生效。引入 `spring-boot-starter-web` 并不等于「无条件造 DispatcherServlet」，而是 **classpath 上有 Servlet 且你没自己声明时才造**。

常用条件（背场景不背全部注解名）：

| 条件 | 含义 |
|------|------|
| `@ConditionalOnClass` | 类路径有这个类 |
| `@ConditionalOnMissingBean` | 容器里还没有该类型/名称 |
| `@ConditionalOnProperty` | 配置项匹配 |
| `@ConditionalOnWebApplication` | Web 应用才生效 |
| `@AutoConfigureBefore/After` | 排序，避免 DataSource 还没好就去造 JdbcTemplate |

`--debug` 或 Actuator `conditions` 能看到「谁 match、谁 did not」。

**追问**  
- 为什么自己写个 `DataSource` `@Bean` 就能覆盖默认？（`DataSourceAutoConfiguration` 带 `OnMissingBean`）  
- `@AutoConfiguration` 和 `@Configuration`？（Boot 3 推荐前者，配合排序；本质仍是配置类）  
- 循环：自动配置类之间怎么避免互相依赖？（拆、`ObjectProvider` 延迟注入、`@AutoConfigureAfter`）  
- 扫描和自动装配的边界？（`@ComponentScan` 扫你的包；自动配置类通常不靠扫包，靠 imports 列表）

## Q4: 怎么写一个 starter？（中小厂高频手写题）

**30 秒答法**  
拆两个模块更干净：

1. `xxx-spring-boot-autoconfigure`：配置属性类 + 自动配置类 + `imports` 文件  
2. `xxx-spring-boot-starter`：几乎只依赖 autoconfigure 和第三方客户端

步骤：`@ConfigurationProperties("demo.client")` 收配置 → `@Bean` 有条件地暴露客户端 → 用户加依赖、写 yaml 就能用。  
不要在 starter 里扫用户的业务包。不要强行 `@ComponentScan("com.xxx")`。

**追问**  
- 属性类为什么要 `@EnableConfigurationProperties`？（让绑定生效；Boot 2.2+ 也可在自动配置上 `@EnableConfigurationProperties`）  
- 松散绑定？（`demo.client.connect-timeout` 对应 `connectTimeout`）  
- 校验？（`@Validated` + JSR-380；启动期失败比运行期 NPE 好）

## Q5: 外部化配置：优先级、Profile、`@ConfigurationProperties`

**30 秒答法**  
同一 key 谁覆盖谁，面试按「越靠近启动命令越优先」记：命令行 > 系统属性 > 环境变量 > `application-{profile}.yml` > `application.yml`（简化版，完整列表以当前 Boot 文档为准，别把 bootstrap 当默认）。  
`spring.profiles.active` 选环境。`@Profile("dev")` 控制 Bean。  
`@ConfigurationProperties` 适合一组配置（类型安全、IDE 提示、校验）；`@Value` 适合零散注入。不要把密码写进 Git，用环境变量 / 配置中心 / K8s Secret。

**追问**  
- `bootstrap.yml` 还要吗？（老 Spring Cloud 用来抢先连配置中心。较新版本用 `spring.config.import=nacos:` 一类，**不要**还当必考默认）  
- 配置刷新：`@RefreshScope` / `@ConfigurationProperties` 谁能热更新？（见 Cloud 章；连接池等 Bean 刷新等于重建，有风险）  
- 多文档 yaml `---`？（同一文件里用 `spring.config.activate.on-profile`）

**踩坑**  
`@Value` 在 `@Bean` 方法参数上可以；在非 Spring 管理的 `new` 对象上不行。静态字段注入是味道。

## Q6: 嵌入式容器、Servlet 还是 WebFlux？

**30 秒答法**  
默认 `spring-boot-starter-web` → Tomcat 嵌入，一个可执行 jar 里跑 Servlet。Jetty/Undertow 换 starter 即可。  
WebFlux：`spring-boot-starter-webflux`，Netty，响应式，和 MVC **不要混成一套 Dispatcher 还指望都是默认**（可以有限共存，面试说「新项目二选一」更稳）。  
2026 常见加分：MVC + **虚拟线程** 处理阻塞 IO，而不是强上 WebFlux。WebFlux 适合已有响应式链路和背压。

**追问**  
- 优雅停机？（`server.shutdown=graceful`，等 in-flight；K8s 还要 `preStop` 先摘端点）  
- 连接数、accept 队列、max-http-header-size 调过没有？（文件上传、大 cookie、网关头膨胀）  
- 打 war 和打 jar？（外置 Tomcat 不要用 `provided` 却还嵌入一份）

## Q7: Fat jar 怎么启动？分层镜像为什么出现？

**30 秒答法**  
Boot 插件把依赖放 `BOOT-INF/lib`，自己的类在 `BOOT-INF/classes`，`Main-Class` 是 `JarLauncher`（不是你的 `main` 直接当 Manifest 入口）。所以用 `java -jar` 才能解对 Classpath。  
分层 jar（`layertools`）：依赖层变化少、应用层变化勤，Docker 缓存才有效。面试能说到「别把 fat jar 当一层 COPY 完事」就加分。

**追问**  
- 为什么 IDE 里能跑、服务器 `java -cp` 报错？（缺了 Launcher 的嵌套 jar 协议）  
- Native Image？（AOT、反射配置、启动快内存低；第三方不兼容就别吹上了生产）

## Q8: Actuator 要暴露什么、不要暴露什么？

**30 秒答法**  
生产最少：`health`、`info`、`metrics`、`prometheus`。`health` 分 liveness/readiness（K8s 探针对着这两个，不要用「业务完全好了」当 liveness，否则死循环重启）。  
不要对公网裸露 `env`、`heapdump`、`shutdown`、`beans`。要鉴权，最好只内网。

**追问**  
- readiness 失败但进程活着？（依赖 DB 抖动时该不该让探针失败：看是否无状态、有没有降级。很多团队 readiness 绑关键依赖，liveness 只绑进程）  
- 自定义 health indicator？（下游挂了要反映到就绪，而不是 200 装活）

## Q9: 数据访问自动装配（Hikari、事务、JPA/MyBatis）

**30 秒答法**  
classpath 有 JDBC → `DataSourceAutoConfiguration` 建 Hikari 池（Boot 2+ 默认）。事务：`@EnableTransactionManagement` 已由 Boot 带上。MyBatis 靠 `mybatis-spring-boot-starter`，不要和 JPA 抢同一个库还不说清。  
池参数：`maximum-pool-size` 不是越大越好，要小于 DB `max_connections / 实例数`，并给运维和管理连接留余量。

**追问**  
- 多数据源怎么配？（排除默认自动配置，自己建两套 `DataSource` + `SqlSessionFactory`/`EntityManager`；事务要指定 manager）  
- 连接泄漏？（`leak-detection-threshold`；未关流式查询、事务挂起）

## Q10: 测试怎么写才像用过 Boot？

**30 秒答法**  
`@SpringBootTest` 拉满容器，慢，适合少量链路。切片：`@WebMvcTest`、`@DataJpaTest`、`@JsonTest`。Mock 下游用 `@MockBean`（注意它会替换容器里的 Bean，和 `@SpyBean` 别乱用）。集成测试用 Testcontainers 起真实中间件，比嵌入式 Redis 更接近生产。

**追问**  
- 为什么 CI 里 `@SpringBootTest` 全绿、生产挂？（测试用了 h2/内存 mq，没测到连接池、事务、时区）  
- `ApplicationContext` 缓存？（同类配置会复用；静态污染要 `@DirtiesContext`，会变慢）

## Q11: Spring Boot 3 升级面试怎么答？

**30 秒答法**  
硬性：Java 17+（现多 21）、`javax.*` → `jakarta.*`、Hibernate 6、部分配置键更名。  
可观测：Micrometer Tracing 替代 Sleuth 那套老用法。  
安全性：Spring Security 6 filter 链、默认行为变化。  
先升 Boot 再升业务库，用 `spring-boot-properties-migrator` 找废弃键。反射、`setAccessible`、非法访问在模块系统下会炸，和 [Java 新特性](05-java-modern.md) 一起讲。

**追问**  
- 必须上 Native 吗？（不必。多数团队停在 JVM + 虚拟线程就够）  
- 为什么 Boot 3 和 Cloud 版本必须一张兼容表？（见 Cloud 章，答「BOM 对齐」即可）

## Q12: 线上 Boot 应用排查清单

1. 起不来：自动配置冲突、连不上配置中心、端口占用、`FailureAnalyzer` 原文  
2. 起得慢：连库、Flyway、同步缓存预热  
3. 内存涨：Hikari 泄漏、无界缓存、日志 MDC  
4. CPU 高：Jackson 大 JSON、正则、误用 `Keys`/`Eureka` 心跳异常  
5. 流量进来但一直 503：readiness、优雅停机、线程池打满（Tomcat `max-threads` 或虚拟线程下的连接池）

准备一次「启动失败 / 探针失败」故事，比背装配名单更值钱。
