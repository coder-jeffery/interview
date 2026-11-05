**1. 基础协议**

- TCP：三次握手、四次挥手、TIME_WAIT 和 CLOSE_WAIT 的成因与处理、拥塞控制、粘包、keepalive。
- HTTP/1.1、HTTP/2、HTTP/3：长连接、多路复用、队头阻塞、头部压缩，以及 QUIC 为什么基于 UDP。
- HTTPS/TLS：握手过程、证书链、SNI、双向认证（mTLS）、会话复用、TLS 终止与透传。
- DNS：解析流程、缓存与 TTL、负载均衡中的作用。
- WebSocket、gRPC、SSE 的代理方式。

**2. 网关核心能力**

- 反向代理与正向代理的区别，L4 与 L7 负载均衡的区别。
- 负载均衡算法：轮询、加权、最少连接、一致性哈希。
- 健康检查（主动和被动）、熔断、重试、超时、限流（令牌桶、漏桶、滑动窗口）。
- 认证鉴权：JWT、OAuth2、API Key、HMAC，以及 CORS。
- 灰度发布、金丝雀发布、流量镜像、路由匹配规则。

**3. Kong 本身相关**

- 基于 Nginx 和 OpenResty（Lua）：事件驱动模型、epoll、Nginx 的请求处理阶段。
- Kong 的核心概念：Service、Route、Upstream、Target、Consumer、Plugin，以及插件的执行阶段和优先级。
- 数据面与控制面分离、DB-less 模式、声明式配置、Kubernetes 里的 Kong Ingress Controller。
- 写自定义插件（Lua，或者 Go、Python 的 PDK）。

**4. 性能与排障**

- I/O 模型：阻塞、非阻塞、多路复用，以及 epoll 与 select 的区别。
- 连接池、上游 keepalive、文件描述符上限、内核参数调优。
- 用 `tcpdump`、Wireshark、`curl -v`、`ss`、`netstat` 抓包和排查，常见的 502、503、504 的含义与原因。
- 链路追踪、日志、监控指标（Prometheus）。

**5. 加分项**

- 服务网格（Istio）与 API 网关的区别，以及东西向和南北向流量的概念。
- 安全：WAF、防重放、防 DDoS、限流防刷