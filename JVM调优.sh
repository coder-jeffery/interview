#command:
# jvm打印垃圾收集器
java -XX:+PrintCommandLineFlags -version


# 查看java进程号
jinfo -flags <pid>

# 查看gc 1000ms输出一次
jstat -gc pid 1000

# 查看gc容量
jstat -gccapacity  pid

# 查看堆内存
jmap -heap <pid>

#打印gc日志
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.log -jar app.jar

# jvm参数
vmoption