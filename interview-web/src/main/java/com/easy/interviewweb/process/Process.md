Java进程：
    进程是操作系统资源分配的最小单位；
        每个java程序创建一个jvm进程；
        进程具有独立的内存空间，文件句柄，cpu时间片；操作系统资源最小单位
        线程：CPU调度最小单位；
        JVM虚拟机：
            同一个进程共享堆内存，栈内存每个线程独享。
API:
    Restful架构：
        面向资源方式 http/json
    RPC架构：
        面向方法调用方式 http2二进制
数据格式：
    json/xml/二进制/avro/protobuf/yaml/Thrift