### Docker

http://www.docker.com/

https://hub.docker.com/

#### 内容



#### 出现原因



#### 作用



#### 常用命令

```
docker version
docker info
docker 命令 --helper

https://docs.docker.com/reference/
```

镜像命令

```shell
查找当前镜像
docker image
-q 只显示id

(base) qgs@192 ~ % docker images
REPOSITORY    TAG       IMAGE ID       CREATED        SIZE
alpine/git    latest    4ee6a3b79e0c   10 days ago    27.1MB
hello-world   latest    18e5af790473   2 months ago   9.14kB

搜索镜像
docker search mysql --filter=STARS=1000

下载镜像
docker pull mysql
docker pull docker.io/mysql/mysql-server:latest(真实地址)
默认是 latest
分层(layer)下载(联合文件系统)

删除镜像
根据id删除
docker rmi -f 镜像id
全部删除
docker rmi -f $(docker images -aq)
```

容器命令

```shell
有了镜像才可以下载容器

新建容器并启动
docker run -parameter image
--name="a name" 容器名字
-d 							后台运行
-it							以交互方式运行，进入容器查看内容
-p							指定容器端口
		-p ip:主机端口:容器端口
		-p 主机端口:容器端口
		-p 容器端口
		容器端口
-P							随机指定端口

启动并进入容器
(base) qgs@192 ~ % docker run -it centos /bin/bash
[root@264704b66e0a /]# 
[root@264704b66e0a /]# exit

exit 停止容器并退出
control + p + q 容器不停止退出

列出运行容器
docker ps      正在运行的容器
				  -a   现在 + 曾经 运行的容器
				  -n=1 最近创建的容器
				  -q   显示容器编号
				  
删除容器
docker rm 容器id
docker rm -f $(docker ps -aq) # 删除所有容器两种方式
docker ps -a -q | xargs docker rm
```



```shell
docker start 容器id
docker restart 容器id
docker stop 容器id # 停止正在运行的容器
docker kill 容器id
```

#### 其他命令

```shell
后台启动容器
docker run -d centos # 返回容器id(无法启动)
容器后台运行必须有一个前台经常，否则会自动停止

日志
docker logs -f -t -tail 容器id
-tf # 显示日志
--tail number # 显示日志条数

查看进程
docker top 容器id

查看进行元数据
docker inspect 容器id # 容器详细数据

进入正在运行的容器
docker exec -it 容器id /bin/bash # 进入容器后开启一个新的终端
docker attch 容器id # 当前正在执行的终端，不启动新进程

从容器中复制文件到主机
docker cp 容器id:容器内路径 目的主机路径
docker cp 547f398cb41d:/home/cpfile.txt /Users/qgs/Desktop # 复制文件到桌面

```



```shell
部署nginx

docker search nginx
docker pull ngins:1.20.0
docker run -d --name nginx-1 -p 8765:80 nginx # 将本机的8765端口映射到docker的80端口
	-d 后台运行
	--name 起名
	-p 映射端口
	
	
部署tomcat
docker run -it -rm tomcat 
  --rm 运行完就删除
  
docker run -d -p 8081:8080 --name tomcat-01 tomcat # 映射本机的8081端口到docker的8080端口
docker exec -it tomcat-01 /bin/bash

部署 elasticsearch
docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms64m -Xmx512m" elasticsearch:7.6.2
curl localhost:9200

docker stats # 查看docker的内存使用情况
```



#### 可视化

portainer 图形化界面管理工具

```shell
docker run -d -p 9000:9000 --restart=always -v /var/run/docker.sock:/var/run/docker.sock --privileged=true portainer/portainer
```



```shell
提交容器

docker commmit -m="描述信息" -a="作者" 容器id 目标镜像名:tag

docker commit -m="tomcat加上webapp" -a="qgs" ea5d910ec335 tomcat-webapps:1.0
docker run -d -p 8080:8080 --name tomcat01 tomcat-webapps:1.0
```



#### 容器数据卷

一种同步机制(将数据持久化)

```shell
docker run -it -v 主机目录:容器目录

docker run -it -v /Users/qgs/Desktop/docker:/usr/local/tomcat/webapps -p 8080:8080 tomcat-webapps:1.0 /bin/bash


docker run -it -v /Users/qgs/Desktop/centos:/home centos /bin/bash

docker start 6798ad5a0ba1 
docker attach 6798ad5a0ba1


docker run -d -p 3306:3306 -v /Users/qgs/Desktop/docker/mysql/conf:/ect/mysql/conf.d -v //Users/qgs/Desktop/docker/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root mysql/mysql-server
-d 后台运行
-p 端口映射
-v 卷挂载
-e 环境配置
--name 别名

# 开启后需要创建用户然后授权，否则无法登录
docker exec -it mysql /bin/bash
mysql -uroot -p
create user 'root'@'%' identified by 'root';
grant all privileges on *.* to 'root'@'%';
```



具名挂载，匿名挂载

```shell
匿名挂载
-v 不提供本地地址


具名挂载
-v 后面提供名字，不提供目录
docker run -d -P --name nginx-1 -v name-nginx:/etc/nginx nginx

docker volume ls # 列出所有的卷
docker volume inspect mysql/mysql-server # 查看挂载信息

区分两者：
-v 容器内路径 # 匿名
-v 卷名:容器内路径 # 具名
-v /主机地址:容器内路径 # 指定路径挂载


docker run -d -P --name nginx-1 -v name-nginx:/etc/nginx:ro nginx
ro: 只能通过宿主机操作，容器内不能修改
rw: 
```



