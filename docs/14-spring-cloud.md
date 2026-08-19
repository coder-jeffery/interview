# Spring Cloud

> 面试权重：高频（简历写了微服务几乎必问；大厂挖故障与取舍，中小厂挖组件怎么配）  
> 适合层级：中级及以上  
> 理论（CAP、要不要拆、幂等）看 [分布式](10-distributed.md)；本章讲 **Spring Cloud / Spring Cloud Alibaba 在项目里怎么接。**

国内 2026 年生产主流是 **Spring Boot 3 + Spring Cloud 202x + Spring Cloud Alibaba（Nacos / Sentinel / Seata / Gateway）**。Netflix 全家桶当历史对照讲，不要当现行默认。

## Q1: Spring Cloud 是什么？和 Boot、K8s 什么关系？

**30 秒答法**  
Cloud 是**微服务常用模式的一套约定与客户端**：发现、配置、网关、负载均衡、熔断、总线。它跑在 Boot 上，靠 BOM 对齐版本。  
它**不是**调度器。副本、升级、资源限制是 K8s 的事。现代部署常常是：K8s 管进程与探针，Cloud 管**服务间调用与流量治理**。服务发现也可以改用 K8s Service/DNS，这时 Nacos 不是必选项——要能说出你们为什么还留着它（配置、多语言、跨集群）。

**追问**  
- Cloud 版本为什么叫 `2024.0` 这种年号？（Train 版本，和 Boot 小版本有兼容矩阵，**不要混用 starter 版本号**）  
- 只有一个服务要不要上 Cloud？（不要。先 Boot 单体模块化）

## Q2: Netflix 组件退役后对等物（对比题必考）

**30 秒答法**

| 老 Netflix | 现在常见 | 一句话 |
|------------|----------|--------|
| Eureka | Nacos / Consul / K8s | 发现；国内 Nacos 兼配置 |
| Ribbon | Spring Cloud LoadBalancer | 客户端负载均衡 |
| Hystrix | Sentinel / Resilience4j | 熔断限流；Hystrix 已停更 |
| Zuul 1 | Spring Cloud Gateway | Zuul 1 阻塞 servlet；Gateway 基于 WebFlux |
| Config + Bus | Nacos 配置 / Consul KV | 动态配置；Bus 刷新全军要谨慎 |
| Sleuth | Micrometer Tracing + OTel | traceId 贯穿 |

口述加一句：**替换的不是注解名，是失败语义和线程模型。** 例如 Gateway 在 reactor 线程里不要调阻塞 JDBC。

## Q3: 一次请求怎么走？（用这个图串起所有组件）

**30 秒答法**

```text
客户端
  → Gateway（鉴权、限流、路由、灰度）
    → 服务发现选出实例（Nacos + LoadBalancer）
      → 目标服务 Filter / Sentinel 资源
        → OpenFeign 调下游（再发现、再负载均衡、再超时熔断）
          → DB / Redis / MQ
```

面试官要听你把**超时、重试、鉴权、观测**插进这张图：网关 200ms、服务 150ms、Feign 100ms、DB 50ms，由外到内递减；重试只放幂等读；`traceId` 从网关生成传到 Feign。

**追问**  
- 鉴权放网关还是服务？（网关挡无凭证；服务仍要校验**资源归属**，防内网水平越权）  
- 网关直连实例还是走 K8s Service？（直连依赖发现健康状态；走 Service 则二次负载均衡，要说清你们选哪条）

## Q4: Nacos 注册中心

**30 秒答法**  
实例启动向 Nacos 注册（临时实例用心跳；持久实例用于 DNS 类服务）。客户端拉或订阅实例列表，**本地缓存**。Nacos 挂了：已缓存的调用还能继续，新实例和变更感知不到——要讲清这个窗口。  
健康：客户端心跳失败会被摘除。下线应先 `deregister` 再停流量，不要杀进程等超时。

**追问**  
- 临时 vs 持久实例？（AP 服务用临时；运维型用持久。别把所有服务设持久却不心跳）  
- 保护阈值 / 推空保护？（防止 Nacos 误判把实例列表推成空，导致全军 503。开了可能把流量打到死人上，要监控）  
- 元数据做什么？（版本、灰度、机房。路由和负载均衡按 metadata 过滤）  
- 集群：Nacos 自己的 Raft 与 AP/CP 切换？（配置中心偏 CP、服务发现可 AP。说「我们按官方建议：发现 AP、配置 CP」即可，别编节点数）  
- 和 Eureka 比？（Eureka AP、自我保护；Nacos 功能合配置、健康检查更灵活、国内生态好）

**踩坑**  
开发环境多个 bootstrap 指到同一 namespace，服务名冲突；心跳线程池打满导致误摘除；优雅下线没配，K8s 杀掉后发现还在打旧 Pod。

## Q5: Nacos 配置中心

**30 秒答法**  
`dataId` + `group` + `namespace` 三维隔离。启动用 `spring.config.import` 拉取，长轮询监听变更。敏感配置加密或走 Secret，不要明文进 Git 再贴到 Nacos 当「安全」。  
变更后：`@RefreshScope` 会销毁重建 Bean。**Hikari、线程池、Feign Client 重建有副作用**（连接抖、进行中请求失败）。配置要拆：能热更的业务开关 vs 必须重启的连接参数。

**追问**  
- 推全量 yaml 还是按 key？（按文件；大文件变更所有监听者刷新）  
- 配置中心挂了？（本地快照；启动期没有快照会起不来，要把失败策略讲出来）  
- 灰度配置？（按实例/标签发布，先 1 台再全量，能回滚）

## Q6: 客户端负载均衡与 OpenFeign

**30 秒答法**  
`@FeignClient(name = "order-service")` → 通过服务名解析实例 → LoadBalancer 选一个 → HTTP 调用。  
超时：连接超时 vs 读超时分开。重试：默认谨慎，**写接口默认不重试**。Fallback / FallbackFactory：降级返回要可识别，不要吞成空列表假装成功。  
契约：Spring MVC 注解；接口拆到 API 模块时注意 **时间对象、Jackson、异常解码** 两边一致。

**追问**  
- Feign 和 RestTemplate、WebClient？（Feign 声明式；RestTemplate 维护模式；WebFlux 用 WebClient。Boot 3 新代码更常见 RestClient / HttpExchange，面试提一句加分）  
- 为什么有时必须绝对 URL？（回调、第三方；这时不走发现）  
- GZIP、日志级别 `full` 打生产？（日志会打 body，PII 泄漏且慢）  
- 负载均衡策略？（默认轮询/随机一类；按延迟、同机房可用 metadata。热实例要结合限流）  
- 重试 + 超时放大？（网关重试一次、Feign 再重试，下游收到 3 倍流量。重试预算要全局算）

**踩坑**  
Feign 接口返回 `ResponseEntity` 却在 fallback 里返回 null；`@RequestMapping` 类级别路径两边不一致；忽略 404 被当成空。

## Q7: Spring Cloud Gateway

**30 秒答法**  
异步非阻塞（WebFlux）。路由 = **Predicate（匹配）+ Filter（加工）+ URI（目标）**。内置：Path、Header、Cookie、限流、重试、熔断、改写路径。  
鉴权 Filter 放全局，业务不要堆进网关。网关是水平扩展的无状态层，会话放 Redis 或 token。

**追问**  
- 和 Nginx 谁做网关？（TLS、静态、粗限流 Nginx 很强；鉴权、按服务发现路由、灰度规则 Gateway 更合适。常串联）  
- 限流用 Redis 计数？（分布式一致更好，多了一次网络；单机令牌桶实现简单有误差）  
- 为什么 Filter 里不能 `block()`？（reactor 线程被占，吞吐塌方）  
- 跨域、WebSocket、文件上传限制？（单独讲超时和 max header/body）  
- 灰度：Cookie / Header / 用户 ID 哈希到 `version=2` 实例。Nacos metadata 打标，LoadBalancer 过滤。

**优雅发布**  
先扩新版本 → 网关按比例切 → 观察错误率 → 摘旧。和 K8s rolling 对齐：readiness 通了再进发现。

## Q8: Sentinel（限流 / 熔断 / 热点）

**30 秒答法**  
资源名通常是 URL 或 Feign 方法。规则：QPS 限流、线程数隔离、慢调用比例熔断、异常比例熔断、热点参数（秒杀用户/商品 ID）。  
和 Hystrix 比：Sentinel 规则可动态推送，关注流量形态而不是线程池隔离那一套（Hystrix 舱壁是线程池/信号量）。  
失败要有**可观测的降级结果**和开关，避免熔断误伤把核心链路切到空实现还不报警。

**追问**  
- 令牌桶 vs 漏桶 vs 滑动窗口？（面试能说：突发用令牌；匀速用漏桶；统计窗口用滑动。Sentinel 默认偏滑动窗口计数）  
- 网关限流 vs 服务限流 vs 数据库？（层层都有配额，最底下 DB 连接才是硬顶）  
- 集群限流？（共享配额，依赖可靠计数，有误差；不要幻想全球精确 QPS）  
- 和 Resilience4j？（更偏代码 API、和 Spring Cloud CircuitBreaker 抽象；国内阿里栈用 Sentinel 多）

## Q9: 分布式事务 Seata（简历写了就必挖）

**30 秒答法**  
优先避免跨服务事务。Seata 常见 AT：拦截 SQL，记前后镜像，提交分支，协调者失败则反向补偿。全局锁防止脏写。  
要讲清楚：**AT 不是免费 XA**，长事务、大 SQL、未覆盖的 ORM 都会疼。TCC 适合资金类但侵入大。Saga 适合长流程补偿。

**追问**  
- undo_log 表谁建？（每个业务库一张）  
- 隔离级别？（AT 默认全局写锁，读未提交脏读窗口要业务接受）  
- TM / RM / TC 角色？（谁开全局事务、谁注册分支、谁持久化状态）  
- 和本地消息表怎么选？（能异步最终一致就 Outbox；要同步多库提交再考虑 Seata）

没上过 Seata 就明确说「评估过，我们用消息表」，比假装配过 `seata-server` 强。

## Q10: 链路追踪与配置刷新事故

**30 秒答法**  
Micrometer Tracing / OTel：网关生成 traceId，Feign / JDBC 插桩。没有日志关联，微服务等于无法排障。采样率生产常 <100%，排障时临时拉高。  
Bus/`RefreshScope` 全量刷新：曾经把连接池打光、令全站抖。生产配置变更要灰度、分类、可回滚。

**追问**  
- 日志里只有 spanId 没有业务单号？（还要 MDC 里放 `orderId`）  
- 跨线程？（线程池要传上下文；虚拟线程同样要 scoped value / TaskDecorator）

## Q11: 服务雪崩怎么讲成故事

**30 秒答法**  
下游 RT 从 50ms 到 2s → Feign 线程/连接占满 → 上游也满 → 网关 504。  
止血：下游熔断、网关限流、关掉非核心调用、扩容只对已熔断的下游无效。  
根治：超时预算、舱壁（不同下游不同连接池）、缓存、异步化、压测过的熔断阈值。  
准备一组你们的真实阈值（哪怕是「Feign 读超时 800ms，熔断 50% 慢调用」）。

## Q12: 面试怎么证明「不是只会贴注解」

准备这 6 个数字/事实，比背组件清单有效：

1. 服务个数、核心链路深度（网关后几跳）  
2. Nacos 挂 2 分钟时用户侧表现  
3. Feign 超时和重试次数  
4. 网关限流维度和阈值  
5. 一次错误的配置热更新  
6. 一次灰度或回滚

对不上项目就说「了解」，把深度留给 [场景题](12-scenario.md)。
