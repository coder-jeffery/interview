# Java 基础

> 面试权重：必会  
> 适合层级：所有人（中高级会把基础题当开场，用来决定后面挖多深）

## Q1: `==` 和 `equals` 差在哪？为什么重写 `equals` 必须重写 `hashCode`？

**30 秒答法**  
`==` 比较引用（基本类型比的是值）。`equals` 默认也是引用相等，业务相等要自己重写。`hashCode` 的契约是：`equals` 为 true 的两个对象，hash 必须相同。否则进不了 `HashMap`/`HashSet` 的同一桶，表现为「放进去取不出来」。

**原理**  
`Object.hashCode` 与身份相关；`String`/`Integer` 已按值重写。`HashMap` 定位：先 `hash` 再 `equals`。只改 `equals` 会导致逻辑相等的 key 散落到不同桶。

**追问**  
- `HashMap` 的 key 是可变对象，改了参与 `equals` 的字段会怎样？（丢失，get 为 null）  
- `String` 为什么适合当 key？（不可变，hash 可缓存）  
- `record` 的 `equals`/`hashCode` 是什么语义？（按所有组件字段）

**踩坑**  
Lombok `@Data` 会按全部字段生成 `equals`，用实体当 Set 元素或 Map key 时，id 以外的字段变化会把对象「弄丢」。实体相等通常只比业务主键。

## Q2: `String`、`StringBuilder`、`StringBuffer`，以及常量池

**30 秒答法**  
`String` 不可变，拼接在循环里会造大量临时对象。`StringBuilder` 可变、非线程安全，是局部拼接的默认选择。`StringBuffer` 方法带 `synchronized`，现代代码几乎不用，跨线程用别的并发手段。字面量进字符串常量池；`intern()` 把堆上的字符串纳入池并返回池中引用。

**原理**  
Java 9+ 字符串是 `byte[]` + coder（Latin-1/UTF-16）。编译期常量折叠：`"a" + "b"` 直接是 `"ab"`。循环里 `"a" + i` 仍会创建 builder（javac 会优化，但不要依赖直觉）。

**追问**  
- `new String("abc")` 几个对象？（字面量池里一份，堆上再一份，具体还看是否首次出现）  
- `String intern` 在 JDK 7 前后差异？（7 起池在堆，intern 可引用堆对象，不再一律复制到永久代）  
- 为什么不把所有字符串 intern？（池膨胀、equals 变身份比较的误用）

## Q3: 异常：Checked vs Unchecked，以及 try-with-resources

**30 秒答法**  
`RuntimeException` 和 `Error` 是 unchecked，其余继承 `Exception` 的是 checked，必须声明或捕获。业务可恢复的用受检或明确的业务异常；编程错误（NPE、非法参数）用 unchecked。IO/锁必须关，用 try-with-resources（实现 `AutoCloseable`），避免 `finally` 里二次异常把主异常吃掉。

**追问**  
- `Error` 为什么几乎不捕获？（`OutOfMemoryError` 捕获后进程往往已经不可用）  
- 异常对性能的影响？（栈填充贵，热路径不要用异常做控制流）  
- Spring 事务默认回滚哪些异常？（unchecked；checked 默认不回滚，见 Spring 章）

## Q4: 泛型擦除、`<? extends T>` 和 `<? super T>`

**30 秒答法**  
Java 泛型是编译期检查，运行时擦成裸类型（有限的桥方法/签名保留）。`List<String>` 运行时是 `List`。PECS：生产者用 `extends`（能取不能加，除了 null），消费者用 `super`（能加 T，取出只能当 Object）。

**原理**  
擦除导致不能 `new T()`、不能 `List<String>.class`、不能重载 `foo(List<A>)` 与 `foo(List<B>)`。数组是协变且运行时检查，泛型是不变且编译期检查——这是「为什么不用对象数组」的面试标准答案。

**追问**  
- 桥方法是什么？（擦除后为保持多态，编译器生成的合成方法）  
- 能用反射写出 `ArrayList<Integer>` 再 `add("str")` 吗？（能，这就是擦除）  
- `record`/`sealed` 和泛型有无关系？（无直接关系，别往一块扯）

## Q5: 反射、动态代理、SPI

**30 秒答法**  
反射：运行时读 Class、调方法、改字段，Spring/序列化/测试都靠它，代价是检查与无法内联。动态代理：JDK 代理只能代理接口（`InvocationHandler`）；CGLIB/ByteBuddy 代理类（子类拦截）。SPI：`ServiceLoader` 读 `META-INF/services/`，Dubbo/JDBC 驱动加载是典型场景。

**追问**  
- 为什么 Spring 有的 Bean 是 JDK 代理，有的是 CGLIB？（有接口且 `proxyTargetClass=false` 时走 JDK；类代理或 `proxyTargetClass=true` 走 CGLIB）  
- `setAccessible(true)` 在模块系统下？（JDK 16+ 强封装，非法反射会失败，需要 `--add-opens`）  
- SPI 和 Spring Factories / `imports` 的差别？（加载时机、隔离、是否懒加载）

**踩坑**  
反射改 `final` 字段在现代 JDK 越来越不可靠；不要把这当生产技巧讲。

## Q6: 深拷贝、浅拷贝、`clone`

**30 秒答法**  
浅拷贝复制引用，内部可变对象共享。深拷贝递归复制。`Object.clone` 是浅拷贝，还要求 `Cloneable`，坑多。生产里更常见：拷贝构造、静态工厂、序列化（重）、MapStruct、或直接当不可变设计避免拷贝。

**追问**  
- 为什么 `Cloneable` 被批评？（标记接口却抛 `CloneNotSupportedException`，类型系统帮不上忙）  
- 不可变对象要不要拷贝？（不需要，这是不可变的收益）

## Q7: `Integer` 缓存、自动装箱

**30 秒答法**  
`Integer.valueOf(-128..127)` 默认缓存（可用 `-XX:AutoBoxCacheMax` 扩）。`==` 比装箱对象会在缓存区间「碰巧相等」、区间外不相等。比较包装类型用 `equals`，或先拆箱但注意 NPE。

**追问**  
- `int` 和 `Integer` 比较时发生什么？（Integer 拆箱，null 就 NPE）  
- Stream 里 `mapToInt` 和 `map(Integer::intValue)` 的装箱开销？（热路径用原始流）

## Q8: 接口 vs 抽象类，以及 Java 8+ 接口演进

**30 秒答法**  
抽象类可以有字段、构造、非 public 方法，表达「is-a + 共享状态」。接口表达能力（can-do），可多实现。Java 8 起接口可有 default/static；Java 9 可有 private 方法。一旦需要状态或初始化顺序，抽象类更合适；一旦需要多继承能力，接口更合适。

**追问**  
- default 方法冲突怎么解决？（子类必须覆盖，或 `A.super.m()`）  
- 为什么不在接口里放业务状态？（多实现菱形、序列化、初始化无构造）

## Q9: `final`、`static`、类初始化顺序

**30 秒答法**  
`final` 变量只能赋一次；字段 final 保证构造完成后对其他线程的可见性（安全发布的一种）。`static` 属于类。初始化：父类静态 → 子类静态 → 父类实例块/构造 → 子类实例块/构造。静态循环依赖会导致一个类看到对方的默认值。

**追问**  
- 为什么 `static final` 常量可以内联到其他类？（编译期常量，改值要重编依赖方）  
- 安全发布除了 final 还有什么？（volatile、锁、并发容器）

## Q10: `Optional`、Stream 常见误用

**30 秒答法**  
`Optional` 用来明确「可能没有返回值」，不要当字段、不要 `Optional.of(null)`（NPE），不要 `get()` 不检查。Stream 是惰性的，用完即失效；有状态副作用（在 `map` 里改外部 List）是面试减分项。需要短路用 `findFirst`/`anyMatch`，需要并行先确认无共享可变状态且数据够大。

**追问**  
- 并行流用什么池？（公共 `ForkJoinPool.commonPool`，会互相抢，生产常用自定义池）  
- `Collectors.toMap` 遇到重复 key？（默认抛异常，要传 merge function）
