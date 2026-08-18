# Java 17 / 21 / 25

> 面试权重：加分  
> 适合层级：中高级（生产已上 17/21 的团队几乎必问「你们为什么升级 / 虚拟线程用了没」）

面试官要的不是 JEP 清单，而是：**你们卡在哪个 LTS、升级收益、坑、以及新特性有没有进过生产。**

当前现实（2026）：很多业务停在 **8/11**，新系统 **17/21**，最新 LTS 是 **25**。默认按 21 讲，25 当「了解最新 LTS」。

## Q1: 为什么从 8 升到 17/21？

**30 秒答法**  
安全补丁、容器感知、G1/ZGC 成熟、语言表达力（record、pattern matching、sealed）、可观测性（JFR）、以及生态（Spring Boot 3 要求 17+）。代价：反序列化/反射/非法访问（强封装）、扫描依赖、`javax.*` → `jakarta.*`（若同时上 Spring Boot 3）。

**追问**  
- 升级顺序？（先 8→11→17 再 21，每次跑测试和反射扫描，不要一次跳三级还不做 canary）  
- `--add-opens` 是方案还是债？（债。第三方库该升级就升级）

## Q2: Record、sealed、pattern matching

**30 秒答法**  
`record`：不可变数据载体，自动 `equals/hashCode/toString`，适合 DTO、值对象，不适合 JPA 实体（有身份、可变、无参构造要求）。  
`sealed`：限制谁能实现接口/继承类，配合 `switch` 穷尽检查，适合领域建模。  
模式匹配：`instanceof` 模式变量、`switch` 模式（17 预览，21 正式），减少强转。

**追问**  
- record 能被序列化框架当 JavaBean 用吗？（很多框架认 accessor 不认 `getX`；Jackson 需要模块或注解）  
- 为什么实体不用 record？（脏检查、代理、延迟加载要可变子类）

## Q3: 虚拟线程（Java 21，必问加分）

**30 秒答法**  
平台线程贵，所以用异步或线程池硬扛连接数。虚拟线程让「每个请求一个阻塞式线程」重新可行：JVM 在阻塞点卸载载体线程。写法仍是同步代码，排错栈更可读。  
适用：高并发阻塞 IO（JDBC、HTTP 调用、文件）。  
不适用：CPU 密集（还是要限制并行度）；长时间 `synchronized` 包住阻塞（pinning）；ThreadLocal 当大对象缓存（数量从几百变百万）。

**和响应式比：** WebFlux 仍适合已建成的流水线与背压；新项目若 IO 为主、团队更熟同步模型，虚拟线程往往更省心。两者不要叠床架屋。

**追问**  
- `synchronized` pinning 在后续 JDK 的进展？（持续在修；面试说「我知道风险，关键库已测过，锁用 ReentrantLock」比背版本号重要）  
- 要不要限制虚拟线程数量？（要限制**并发的阻塞资源**：连接池、信号量，而不是造虚拟线程池）  
- Spring Boot 怎么开？（Tomcat/Jetty 的虚拟线程 executor；配置项随版本变，讲你项目里实际的开关）

## Q4: Structured Concurrency、Scoped Values（21 预览 → 后续 LTS 演进）

**30 秒答法**  
结构化并发：一组子任务的生命周期绑在一块（父失败则取消兄弟），避免「fire-and-forget 的 Future 泄漏」。  
Scoped Values：替代部分 ThreadLocal，只读、有明确作用域，和虚拟线程更合得来（不靠可变的线程局部状态传递上下文）。

面试策略：没在生产用过就说「了解问题域，没上线」，不要假装写过 `StructuredTaskScope` 业务代码。

## Q5: Java 25 该知道什么（最新 LTS）

当加分项，用「特性 → 对业务的意义」说：

| 方向 | 面试怎么讲 |
|------|------------|
| 语言简化 | compact source / instance main：对生产服务几乎无感，对脚本和教学有用 |
| Scoped Values | 若已转正，讲「请求上下文传递，替代 ThreadLocal」 |
| 紧凑对象头等运行时 | 同样堆装更多小对象，对高密度缓存/消息对象有收益，要压测 |
| GC | Generational ZGC / Shenandoah 一类：低停顿路线继续走，选收集器仍靠 SLA 而不是追新 |

**原则：** 25 的预览特性不要当生产默认。公司没升 25，就明确「生产 21，25 我跟过 release note」。

## Q6: 其它常被点名的 9–21 特性（快速过）

- **模块系统（9）：** 强封装。面试重点是「非法反射失败」，不是自己做 JPMS 拆模块。  
- **`var`（10）：** 局部推断，别用在 API 边界。  
- **Text block（15）：** SQL/JSON 字面量。  
- **Switch 表达式（14）：** 穷尽、有返回值。  
- **Sequenced Collections（21）：** `getFirst`/`reversed`，统一 List/Deque/LinkedHashMap 的端点访问。  
- **`HttpClient`（11）：** 替代老 `HttpURLConnection`。  
- **G1 默认、ZGC 产品化：** 见 JVM 章。

## 怎么回答「你们用了哪些新特性」

好答案有项目痕迹：

> 我们 2025 年把网关后面的查询服务升到 21，主要吃容器内存感知和虚拟线程。下游 HTTP 调用从线程池改为虚拟线程后，同样 2C 下超时率下降，连接池成为新瓶颈，所以把池子和信号量一起调。record 只用在 API DTO，表实体仍是 class。没上 Structured Concurrency。
