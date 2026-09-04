面试经典：
    从前端页面-到后端服务-到数据库流程：
        前端页面请求-DNS解析（网络层）-TCP连接（传输层）-HTTPS握手（应用层）-HTTP请求-服务器处理-响应返回-浏览器解析渲染
    跨域问题 端口/域名
    登陆问题
    前端传输安全问题：
        传输层加密：HTTPS-TLS / RSA非对称加密
        AES使用同一把钥匙
        RSA使用非同一把钥匙

JVM：
    command:  java -XX:+PrintCommandLineFlags -version
    -XX:-AOTInvokeDynamicLinking -XX:ConcGCThreads=2 -XX:G1ConcRefinementThreads=9 -XX:InitialHeapSize=1073741824 -XX:MarkStackSize=4194304 
    -XX:MaxHeapSize=17179869184 -XX:MinHeapSize=6815736 -XX:+PrintCommandLineFlags -XX:ReservedCodeCacheSize=251658240 -XX:+SegmentedCodeCache 
    -XX:+UseCompressedOops -XX:+UseG1GC
    + 代表开启
    - 代表关闭

服务注册发现：
    AP：Eureka 无leader 30s client->server 90s  异步复制  客户端负载均衡 ｜ Ribbon 在消费端本地执行负载均衡算法
    CP：Nacos K8S DNS 
API网关：
    SpringCloud Gateway 


            
        
    
        