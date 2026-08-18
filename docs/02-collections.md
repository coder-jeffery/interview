# 集合

> 面试权重：必会  
> 适合层级：所有人（HashMap 是中高级面试的分水岭）

## Q1: `ArrayList` vs `LinkedList` vs `CopyOnWriteArrayList`

**30 秒答法**  
`ArrayList`：连续数组，随机访问 O(1)，尾部插入均摊 O(1)，中间插入要搬内存。默认容量 10，扩容 1.5 倍。  
`LinkedList`：双向链表，头尾 O(1)，按 index 访问 O(n)。缓存不友好，现代 JVM 上几乎总是慢于 `ArrayList`，面试别说「插入多用 LinkedList」除非真的只在头尾且测量过。  
`CopyOnWriteArrayList`：写时复制整个数组，读无锁、写很贵。适合读多写极少（监听器列表）。迭代器是快照，看不到写之后的更新。

**追问**  
- `ArrayList.remove(int)` 和 `remove(Object)` 在全是 Integer 的列表上会怎样？（重载陷阱）  
- 为什么 `CopyOnWrite` 迭代不会 `ConcurrentModificationException`？（快照）  
- 扩容时为什么不是 2 倍？（1.5 倍更省内存，且方便用位移：`old + old>>1`）

## Q2: `HashMap` 在 JDK 8 之后怎么工作？

**30 秒答法**  
数组 + 链表 + 红黑树。`hash` 扰动：`h ^ (h >>> 16)`，让高位也参与低位取模。下标：`(n - 1) & hash`，所以容量必须是 2 的幂。负载因子 0.75。链表长度 ≥8 且数组长度 ≥64 才树化；树节点 ≤6 退回链表。JDK 8 用尾插，避免 JDK 7 头插在并发扩容时的死环（仍不支持并发，只是不再那么容易死循环）。

**原理要点**

| 点 | 值 / 行为 |
|----|-----------|
| 默认容量 | 16 |
| 扩容 | 2 倍，元素要么原地，要么 `index + oldCap` |
| 树化阈值 | 8（泊松分布下极少到 8，真到了多半是 hash 差或攻击） |
| key/value | 允许一个 null key、多个 null value |
| 线程安全 | 否 |

**追问**  
- 为什么树化还要数组 ≥64？（小数组先扩容比树化更划算）  
- 自定义 key 的 `hashCode` 只返回 1 会怎样？（退化成一条链表/一棵树，O(n)）  
- `get` 时先比 hash 再 `==` 再 `equals`，为什么先 `==`？（同一引用快路径，String intern 场景受益）  
- resize 时高低位拆分怎么做？（hash 与 oldCap 的那一位是 0 还是 1）

**踩坑**  
遍历时删除必须用迭代器 `remove`，或 `removeIf`。`HashMap` 作缓存没有淘汰，会 OOM。并发用 `ConcurrentHashMap`，不要 `Collections.synchronizedMap` 混用迭代。

## Q3: `ConcurrentHashMap` 怎么保证并发？和 Hashtable、Collections.synchronizedMap 比？

**30 秒答法**  
CHM 不允许 null key/value（避免并发下 `get==null` 分不清「没有」还是「就是 null」）。JDK 8 取消分段锁：桶级 `synchronized` 锁住第一个节点，配合 CAS 初始化/扩容协助。`size` 是估算再精确计数（`CounterCell` 类似 LongAdder）。读基本无锁（volatile 节点数组）。  
`Hashtable` 方法级 synchronized，太粗。`synchronizedMap` 同样粗，迭代还要自己持锁。

**追问**  
- `computeIfAbsent` 在 JDK 8 有什么坑？（同 key 重入可能死锁；JDK 9 有修复，仍不要在 mapping function 里再改同一个 map）  
- 扩容时读会不会读到旧值？（transfer 用 forwarding node，读会协助或转发到新表）  
- 为什么 `ConcurrentHashMap` 的迭代是弱一致？（不抛 CME，不保证看到全部最新更新）  
- `putIfAbsent` vs `computeIfAbsent`？（前者传入已创建对象，后者懒创建；注意函数副作用）

## Q4: `HashMap`、`LinkedHashMap`、`TreeMap`、`IdentityHashMap`

**30 秒答法**  
- `LinkedHashMap`：插入序或访问序。`accessOrder=true` 可做 LRU 骨架（重写 `removeEldestEntry`）。  
- `TreeMap`：红黑树，key 必须可比较，O(log n)，可范围查询。  
- `IdentityHashMap`：用 `==` 和 `System.identityHashCode`，序列化/ASM 场景，不是通用 Map。

**追问**  
- LRU 为什么生产更常用 Caffeine 而不是手写 LinkedHashMap？（大小、过期、刷新、命中率统计、并发）  
- TreeMap 能放 null key 吗？（自然排序不行，comparator 允许的话可以，别用）

## Q5: fail-fast vs fail-safe

**30 秒答法**  
`ArrayList`/`HashMap` 的迭代器看 `modCount`，结构改了就 `ConcurrentModificationException`——这是尽力检测，不是保证。`ConcurrentHashMap`、`CopyOnWriteArrayList` 弱一致/快照，不抛 CME。增强 for 底层是迭代器，循环里 `list.remove` 会踩 fail-fast。

**追问**  
- 单线程也会 CME 吗？（会，增强 for 里直接 remove）  
- `modCount` 是 volatile 吗？（ArrayList 不是，本意不是并发机制）

## Q6: `HashSet` 和 `LinkedHashSet` 的本质

**30 秒答法**  
`HashSet` 就是 `HashMap` 的 key 集合，value 是共享的 `PRESENT` 对象。所以 `HashSet` 的复杂度、null、线程安全全部跟 `HashMap` 走。`LinkedHashSet` 保插入序。

## Q7: 优先级队列与 `Comparable`/`Comparator`

**30 秒答法**  
`PriorityQueue` 是小顶堆（默认），出队 O(log n)，不能 `null`。只保证堆顶最值，不要假设整数组有序。`Comparator` 和 `equals` 不一致时，`TreeMap`/`PriorityQueue` 会表现「比较相等但 equals 不等」，当 set 用会丢元素。

**追问**  
- 如何实现 TopK？（大小为 K 的堆）  
- 为什么 `PriorityQueue` 不是线程安全的？并发用 `PriorityBlockingQueue`，但那是无界的，生产要自己限流。
