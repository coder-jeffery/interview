# JVM

> 面试权重：高频（中大厂、稳定性岗几乎必问；小厂/国企可能不问）  
> 适合层级：中级讲清模型和一次排查；高级要有参数和证据

## Q1: 运行时数据区（别把 JMM 和这块混了）

**30 秒答法**  
按 Java 虚拟机规范 + HotSpot 实现讲：

| 区域 | 线程私有？ | 装什么 | 溢出典型 |
|------|------------|--------|----------|
| 程序计数器 | 是 | 当前字节码位置 | 几乎不 OOM |
| 虚拟机栈 | 是 | 栈帧：局部变量、操作数栈 | `StackOverflowError` / `-Xss` 过小 |
| 本地方法栈 | 是 | Native | 同上 |
| 堆 | 否 | 对象、数组 | `OutOfMemoryError: Java heap space` |
| 方法区 / Metaspace | 否 | 类元数据、常量、静态变量（实现有差异） | `Metaspace` OOM |
| 直接内存 | 否 | NIO DirectByteBuffer 等 | `Direct buffer memory` |

JDK 8 起永久代没了，类元数据在本地内存的 Metaspace，用 `-XX:MaxMetaspaceSize` 兜住，防止 loader 泄漏把机器打满。

**追问**  
- 对象一定在堆上吗？（逃逸分析 + 标量替换后可以在栈上拆散，不保证分配）  
- 字符串常量池在哪？（JDK 7 起在堆）  
- JMM 是内存分区吗？（不是。JMM 是可见性/排序的抽象；这块是运行时存储）

## Q2: 对象怎么活下来？GC Roots 和算法

**30 秒答法**  
可达性分析：从 GC Roots（栈帧引用、静态变量、JNI、锁对象等）走不到的就是垃圾。引用计数有循环引用问题，HotSpot 不用它做主方案。  
算法：标记-清除（碎片）、复制（新生代，浪费一半）、标记-整理（老年代）。分代：新生代朝生夕死用复制；老年代存活久用整理/区域化。

**引用类型：** 强 > 软（内存紧时收）> 弱（下次 GC 收）> 虚（必须配合 ReferenceQueue，用于堆外释放）。`WeakHashMap` 的 key 是弱引用。

**追问**  
- finalize 呢？（已弃用，不确定何时跑，不要讲成资源释放方案）  
- 为什么要分代？（弱分代假说：大部分对象很快死）

## Q3: 收集器怎么选？G1 和 ZGC

**30 秒答法**  
现代服务默认讲 **G1**（JDK 9+ 默认）：堆分成 Region，有 Young/Old/Humongous，可设 `-XX:MaxGCPauseMillis` 目标，混合回收老区。大对象直接进 Humongous region。  
**ZGC**（JDK 21 已成熟）：面向超大堆、低停顿（亚毫秒～毫秒级目标），着色指针 + 读屏障，适合堆很大且停顿敏感。不是「永远比 G1 快」，吞吐有时略低，要压测。  
CMS 已移除，面试提「老年代并发标记清除、碎片、跟 G1 的历史对比」即可，不要当生产推荐。

**追问**  
- G1 的 SATB 是什么？（快照标记，漏标用写屏障补）  
- 什么时候 Full GC？（G1 失败时会退化，要查 Evacuation Failure / Humongous）  
- 新生代比例还要手动设吗？（G1 不建议乱调 `NewRatio`，让它按停顿目标调）  
- 对象分配 TLAB？（线程本地缓冲，减少堆上 CAS）

## Q4: 类加载与双亲委派

**30 秒答法**  
过程：加载 → 验证 → 准备（静态变量默认值）→ 解析（符号变直接引用，可延迟）→ 初始化（`<clinit>`）。  
双亲委派：加载器先请父加载器，父不能再自己加载。目的：保证 `java.lang.Object` 这类基础类只有一份，防止核心类被替换。  
打破委派：SPI（线程上下文加载器加载实现）、热部署、OSGi、Tomcat 的 WebApp 隔离。

**追问**  
- 准备和初始化的差别？（`static int a = 1`：准备阶段 a=0，初始化才是 1；`static final` 编译期常量更早就内联）  
- 自定义加载器要重写什么？（通常 `findClass`，不要随便重写 `loadClass` 除非真要打破委派）  
- 方法区 OOM 和动态生成类？（大量代理、Groovy、热加载没卸载）

## Q5: 常见 JVM 参数（能默写一组合理默认）

**30 秒答法（服务端起点，不是万能公式）**

```text
-Xms4g -Xmx4g
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/var/log/app/heap.hprof
-Xlog:gc*:file=/var/log/app/gc.log:time,uptime,level,tags:filecount=5,filesize=20m
```

堆大小先与容器内存对齐（留 OS/Metaspace/Direct/线程栈）。容器里用 `-XX:+UseContainerSupport`（较新 JDK 默认开）。

**追问**  
- 为什么 Xms=Xmx？（避免运行时扩堆的停顿和抖动）  
- Direct 内存怎么限？（`-XX:MaxDirectMemorySize`，Netty 另有自己的计数）  
- 线程栈 `-Xss` 乱减小的后果？（深递归/巨大局部变量直接爆栈）

## Q6: 线上排查路径（比背参数更重要）

**30 秒答法**  
先定症状：CPU 高、内存涨、RT 高、Full GC、线程打满。

1. **CPU 高：** `top -Hp` 找线程 → `jstack` / `jcmd Thread.print` 把 nid 转十六进制对栈。看是 GC 线程、业务死循环、还是锁竞争。  
2. **堆涨 / Full GC：** `jstat -gcutil` 看趋势 → 能接受停顿再 `jmap -dump` 或等 OOM dump → MAT 看 Dominator Tree / GC Root 路径。先怀疑缓存没上限、监听器没卸、线程池队列、ClassLoader 泄漏。  
3. **RT 高但 CPU 不高：** 线程多在 `TIMED_WAITING`/`WAITING` → 锁、远程调用、连接池、GC 停顿。看 GC 日志的 pause 是否对齐 RT 毛刺。  
4. **线程打满：** 命名好的线程池一眼能看出哪个池；看队列和拒绝策略。

**工具：** `jcmd`、`jstat`、`async-profiler` / Java Flight Recorder、MAT、GCEasy。不要一上来生产 `jmap -histo:live` 触发 Full GC 还不说。

**追问**  
- 容器里 PID 1 和 `jstat` 权限？（讲你实际用的 sidecar / arthas / 杀进程前的权限）  
- 没有 dump 怎么办？（先 jcmd GC.heap_info、抽样 profiler，不要赌一次 dump 打停服务）

## Q7: 一次能讲的事故模板（高级必备用）

按这个结构准备 90 秒：

1. 现象：P99 从 80ms 到 2s，Full GC 每分钟一次。  
2. 证据：GC 日志 old 区居高不下；dump 里某个 `ConcurrentHashMap` 占 6G。  
3. 根因：本地缓存无上限，热点 key 的 value 是 200KB 对象。  
4. 处理：限容 + TTL，先滚动发布；峰值用临时扩容顶住。  
5. 复盘：缓存必须有 size 和指标；发布清单加「无界 Map」检查。

没有真实事故就用「压测发现」讲，不要编造公司机密数字。
