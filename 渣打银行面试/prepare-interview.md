1.intro myself
2.jdk1.8,jdk8,jdk10,jdk17,jdk21,jdk24
    java 24.0.1 2025-04-15
    Java(TM) SE Runtime Environment (build 24.0.1+9-30)
    Java HotSpot(TM) 64-Bit Server VM (build 24.0.1+9-30, mixed mode, sharing)
3.开发工具：
    idea，eclipse，vscode
4.数据库：
    mysql，oracle，mmsql
5.springboot项目没有main方法，如何启动？
    在不使用springboot-tomcat ，springboot-starter时，引入web和tomcat， 继承springservletinit - springapplicationbuilder - 实现builder
6.@Transactional 什么生效  如何使用？
    AOP动态代理，
7.springboot类加载机制：
    springapplication.run() 加载机制：
        准备环境-刷新容器-后置收尾 
        spring.factories : SPI机制 预加载机制 初始化器，监听器； 此时未创建applicationContext
        run args[] 