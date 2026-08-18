# 并发

> 面试权重：高频（中大厂深挖）  
> 适合层级：中级及以上；校招掌握关键字和线程池即可

## Q1: 进程、线程、协程/虚拟线程

**30 秒答法**  
进程是 OS 资源分配单位；线程是 CPU 调度单位，同进程共享堆。Java 平台线程（1:1 映射 OS 线程）创建贵、栈大，所以用线程池。Java 21 虚拟线程是 JVM 调度的轻量线程（M:N），适合大量阻塞 IO，不适合长时间占 CPU 或持有 `synchronized` 的热路径（pinning）。

**追问**  
- 虚拟线程还要不要线程池？（不要池化虚拟线程；用信号量限制并发）  
- 什么操作会 pin 载体线程？（`synchronized` 持锁时阻塞；JNI；部分类库）优先用 `ReentrantLock`。  
- 和 Go goroutine 比？（都是 M:N；Java 要兼容现有阻塞 API 和 ThreadLocal 语义）

## Q2: `synchronized` 和 `ReentrantLock`

**30 秒答法**  
`synchronized` 是 JVM 内置锁：可重入、非公平（热点会偏向/轻量/膨胀）、退出自动释放，可配合 `wait/notify`。`ReentrantLock` 是 API 锁：可公平、可中断、可超时、可多条件队列（`Condition`），必须在 `finally` 里 `unlock`。默认非公平。没有额外需求就用 `synchronized`，更不容易漏解锁。

**原理（对象头）**  
Mark Word：偏向锁（JDK 15 默认关闭偏向，JDK 18 移除）→ 轻量级（CAS）→ 重量级（OS mutex）。面试按「会升级」讲即可，别背过时的默认开启偏向锁。

**追问**  
- 锁消除、锁粗化是什么？（JIT 证明无逃逸则去掉；相邻同步块合并）  
- 为什么 `wait` 必须在 synchronized 里？（要拥有 monitor；循环里等，防虚假唤醒）  
- 公平锁为什么慢？（排队，吞吐下降）

## Q3: `volatile`、JMM、happens-before

**30 秒答法**  
`volatile` 保证可见性和禁止指令重排序，**不保证复合操作原子性**（`i++` 仍要锁或 `AtomicInteger`）。JMM 定义：工作内存 vs 主内存是抽象模型。happens-before 是程序员该用的规则：解锁 HB 后续加锁、volatile 写 HB 后续读、线程 `start`/`join`、传递性等。单例双重检查必须 `volatile`（禁止半初始化发布）。

**追问**  
- 有了 volatile 为什么还要 synchronized？（原子性、互斥）  
- `final` 字段的安全发布？（构造结束前 freeze，其他线程看到正确初始化的 final）  
- as-if-serial 和 happens-before 的关系？（单线程内前者；跨线程靠 HB）

**踩坑**  
把 volatile 当锁用；在 64 位 JVM 上 long/double 已原子读写，volatile 的核心仍是可见性与排序，不是「把 long 变原子」。

## Q4: CAS、ABA、`Atomic` 与 `LongAdder`

**30 秒答法**  
CAS：用 CPU 指令比较并交换，乐观并发。ABA：值从 A→B→A，CAS 以为没变；`AtomicStampedReference` 用版本号。高并发计数用 `LongAdder`（分段 cell），不要用 `AtomicLong` 死磕同一缓存行。

**追问**  
- CAS 失败怎么办？（自旋；自旋过久浪费 CPU，所以锁会膨胀）  
- `Unsafe` / `VarHandle`？（现代代码用 VarHandle；Unsafe 是历史实现细节）

## Q5: AQS 是什么？`ReentrantLock` 怎么用它？

**30 秒答法**  
AQS（AbstractQueuedSynchronizer）用一个 `int state` + CLH 变体队列实现同步器。独占：`tryAcquire`/`tryRelease`；共享：`tryAcquireShared`（Semaphore、CountDownLatch、ReentrantReadWriteLock 读锁）。队列节点是 `waitStatus` + CAS 入队。`ReentrantLock.Sync`：state 是重入次数，owner 是当前线程。

**追问**  
- 公平 vs 非公平在 AQS 里差在哪？（公平先看队列有没有人；非公平先 CAS 一把）  
- 条件队列和同步队列？（`ConditionObject` 是 wait 队列，signal 时挪回同步队列）  
- `CountDownLatch` 能 reset 吗？（不能，重复用 `CyclicBarrier` 或 `Phaser`）

口述时画三样就够：**state、队列、模板方法。** 不要背全部源码行号。

## Q6: 线程池：参数、拒绝策略、事故

**30 秒答法**  
`ThreadPoolExecutor`：core → 队列 → max → 拒绝。这点经常说反：不是先加到 max 再入队。  
关键参数：`corePoolSize`、`maxPoolSize`、`keepAliveTime`、`workQueue`、`threadFactory`、`RejectedExecutionHandler`。  
队列：无界 `LinkedBlockingQueue` 会让 max 失效（任务堆在队列，OOM 或假死）。有界队列 + 明确拒绝/调用者跑（`CallerRunsPolicy`）才可控。  
拒绝策略：Abort 抛异常、CallerRuns 背压到提交方、Discard/DiscardOldest 丢任务（要能接受丢失）。

**追问**  
- 为什么不建议 `Executors.newFixedThreadPool`？（无界队列）  
- IO 密集 vs CPU 密集怎么定大小？（CPU：核数+1；IO：看阻塞比，压测，不要公式教条）  
- 线程池怎么优雅关闭？（`shutdown` 再 `awaitTermination`，超时 `shutdownNow`，处理 Interrupted）  
- 任务异常为什么「消失」？（`execute` 的异常在线程里，可能只打到 UncaughtHandler；`submit` 的异常在 `Future.get`）

**踩坑**  
用默认 `Object.toString()` 当线程名，线上没法看栈。必须自定义 `ThreadFactory` 命名。

## Q7: `ThreadLocal` 内存泄漏

**30 秒答法**  
`ThreadLocalMap` 的 key 是弱引用，value 是强引用。线程池里线程长驻，value 不 `remove` 就一直挂在线程上：泄漏 + 请求串数据。用完 `remove`，最好 `try/finally`。不要拿 ThreadLocal 当参数传递的「图省事」，虚拟线程数量大时更危险（Java 21 有改进，但仍应 `remove`）。

**追问**  
- InheritableThreadLocal 的坑？（拷贝的是创建时快照；线程池 worker 早就创建好了，传不下去。用 TransmittableThreadLocal 这类方案要讲清楚 classloader 与泄漏）  
- 弱引用 key 为什么还泄漏？（key 被回收后 entry 变成 `null -> value`，value 靠线程活着）

## Q8: `CompletableFuture` 与异步编排

**30 秒答法**  
`supplyAsync`/`thenApply`/`thenCompose`（扁平化）/`thenCombine`（两个独立结果）/`exceptionally`/`whenComplete`。默认跑在 `ForkJoinPool.commonPool`，服务端必须传入业务线程池，否则和并行流抢公共池。超时用 `orTimeout`（Java 9+）或自己 `completeOnTimeout`。

**追问**  
- `thenApply` vs `thenApplyAsync`？（前者可能在完成线程继续跑，后者切到指定池）  
- 阻塞在 `join` 上会怎样？（吃掉工作线程；虚拟线程场景可以阻塞，平台线程要避免在 IO 池里 join 另一堆任务）

## Q9: 死锁、活锁、饥饿

**30 秒答法**  
死锁四条件：互斥、占有且等待、不可抢占、循环等待。预防：固定加锁顺序、tryLock 超时、缩小临界区。排查：`jstack` 看 `Found one Java-level deadlock`，或 `jcmd Thread.print`。活锁：一直在重试；饥饿：公平性不足，低优先级一直拿不到。

**追问**  
- 数据库死锁和 Java 死锁处理差异？（InnoDB 会选一个回滚；JVM 不会自动解开）  
- 如何在代码层检测？（不要线上轮询 ThreadMXBean 当主方案；预防 + 超时更重要）
