打包命令：
    docker build -t interview-web:0.0.1 .
    docker run -d --name interview-web -p 8080:8080 interview-web:0.0.1


**栈：线程私有，存变量引用，自动释放，栈溢出；**
**堆：全局共享，存对象实例，GC 回收，堆 OOM。**
    