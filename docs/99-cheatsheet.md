# 临场速记

面试前 30 分钟只看这一页。细节回对应章节。

## 口述四句

定义 → 场景 → 机制 → 坑。

## Java

- `equals` 相等则 `hashCode` 必须相等；Map key 不要用可变对象。
- `String` 不可变；循环拼接用 `StringBuilder`。
- 泛型擦除；PECS：`extends` 取、`super` 放。
- JDK 8+ `HashMap`：数组+链表+红黑树；容量 2 的幂；阈值 8/64；非线程安全。
- `ConcurrentHashMap`：桶锁 + CAS；禁止 null；`computeIfAbsent` 不要重入。
- `volatile`：可见 + 有序，不保证 `i++` 原子。
- 线程池：core → **队列** → max → 拒绝；禁用无界队列的 `Executors`。
- 虚拟线程：IO 密集；不要池化；小心 `synchronized` pinning；限的是连接池不是线程数。

## JVM

- 堆 + Metaspace + 栈；JMM 不是内存分区图。
- G1 默认；大堆低停顿看 ZGC。
- 双亲委派保核心类；SPI 用线程上下文加载器打破。
- 排查：症状 → 指标 → GC 日志 / 栈 / dump；容器内存要留余量。

## Spring

- 循环依赖：单例 + setter 三级缓存；构造器循环默认失败。
- `@Transactional` 失效：自调用、非 public、checked 异常、异常被吃、异步线程。
- 代理自调用不走切面。
- Boot 自动装配：条件注解 + `AutoConfiguration.imports`；自己声明 Bean 可挡住默认 DataSource。
- Boot 启动：Environment 先于 refresh；`--debug` 看装配报告。
- 配置越靠近命令行越优先；`@ConfigurationProperties` 优于零散 `@Value`。
- Actuator：liveness 绑进程，readiness 绑依赖；`env`/`heapdump` 不对公网。
- Cloud 请求链：Gateway → Nacos + LoadBalancer → Sentinel → Feign。
- Netflix 对照：Eureka→Nacos，Ribbon→LoadBalancer，Hystrix→Sentinel，Zuul→Gateway。
- Feign：读写超时分开；写默认不重试；热更新连接池会抖。
- Nacos 挂了靠本地缓存；推空保护防 503，也可能打到死人。
- 超时由外到内递减；重试预算全局算，防止雪崩。

## MySQL

- InnoDB 聚簇主键；二级索引存主键，要回表；能覆盖则不回表。
- 最左前缀；范围查询截断后面列。
- RR + MVCC：快照读 vs 当前读；间隙锁防幻（当前读）。
- 优化：`EXPLAIN` → 索引 → 改 SQL → 才分库。
- 更新链路：redo prepare + binlog + commit。

## Redis

- 执行单线程；大 key / `KEYS` 会卡全局。
- 穿透：空值/布隆；击穿：单飞/逻辑过期；雪崩：TTL 随机 + 降级。
- 锁：`SET NX EX` + token + Lua 删；只适合效率互斥，账务靠 DB 约束。
- 必须设 `maxmemory` 和淘汰策略。

## MQ

- 丢消息三段：生产确认、副本刷盘、消费先处理再 ack。
- 至少一次 → 消费幂等。
- 顺序：同一 key/队列；热点与队头阻塞是代价。
- 库和消息同成败：Outbox / 事务消息。

## 分布式

- 先问要不要拆服务。
- 超时由外到内递减；重试必须幂等。
- 限流保护自己，熔断保护自己不被下游拖死，降级牺牲非核心。

## 网络

- `TIME_WAIT` 在主动关闭方；`CLOSE_WAIT` 是应用没关连接。
- HTTP/2 多路复用仍受 TCP 队头阻塞；HTTP/3 用 QUIC。
- HTTPS：非对称握手，对称加密数据。

## 场景题第一句

澄清流量、一致性、失败时用户看到什么 → 再给最小可上线方案。
