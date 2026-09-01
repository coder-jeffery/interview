打包命令：
    docker build -t interview-web:0.0.1 .
    docker run -d --name interview-web -p 8080:8080 interview-web:0.0.1
    