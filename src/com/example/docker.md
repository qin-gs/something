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

**容器 = 镜像 + 读写层**

一个运行态容器（running container）被定义为一个可读写的统一文件系统加上隔离的进程空间和包含其中的进程。正是文件系统隔离技术使得Docker成为了一个前途无量的技术。一个容器中的进程可能会对文件进行修改、删除、创建，这些改变都将作用于可读写层（read-write layer）。

![容器和镜像](./img/容器和镜像.png)



为了将零星的数据整合起来，提出了镜像层（image layer）这个概念。一个层并不仅仅包含文件系统的改变，它还能包含了其他重要信息。

元数据（metadata）就是关于这个层的额外信息，它不仅能够让Docker获取运行和构建时的信息，还包括父层的层次信息。需要注意，只读层和读写层都包含元数据。每一层都包括了一个指向父层的指针。如果一个层没有这个指针，说明它处于最底层。

docker create 命令为指定的镜像（image）添加了一个可读写层，构成了一个新的容器。

docker start命令为容器文件系统创建了一个进程隔离空间。注意，每一个容器只能够有一个进程隔离空间。

docker run 命令先是利用镜像创建了一个容器，然后运行这个容器

（docker run就是docker create和docker start两个命令的组合）

docker ps 命令会列出所有运行中的容器。这隐藏了非运行态容器的存在，如果想要找出这些容器，我们需要使用docker ps -a

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


docker run -d -p 3306:3306 -v /Users/qgs/Desktop/docker/mysql/conf:/ect/mysql/conf.d -v /Users/qgs/Desktop/docker/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root mysql/mysql-server
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
rw: 读写
```



#### Dockerfile

docker镜像的构建文件

```shell
# 构建文件内容(全大写)
FROM centos
VOLUME ["volume01", "volume02"] # 匿名挂载卷，容器内的路径
CMD echo "end..."
CMD /bin/bash

# 构建
docker build -f /构建文件地址 -t my-centos . # 当前目录

# 发布镜像
docker push
```



```shell
# dockerfile指令

from          # 基础镜像
maintainer    # 维护者信息
run           # 构建的时候运行的命令
add           # 加一下文件
workdir       # 工作目录
volume        # 挂载目录
expost        # 暴露端口
cmd           # 启动容器时执行的命令；如果指定了多条命令，只有最后一条会被执行。
entrypoint    # 追加
onbuild       # 配置当所创建的镜像作为其它新创建镜像的基础镜像时，所执行的操作指令。
copy          # 复制本地主机的 <src>（为 Dockerfile 所在目录的相对路径）到容器中的 <dest>
env           # 环境变量，会被后续 RUN 指令使用，并在容器运行时保持。
```

```shell
# 构建一个centos

FROM scratch
ADD centos-7-x86_64-docker.tar.xz /

LABEL \
    org.label-schema.schema-version="1.0" \
    org.label-schema.name="CentOS Base Image" \
    org.label-schema.vendor="CentOS" \
    org.label-schema.license="GPLv2" \
    org.label-schema.build-date="20201113" \
    org.opencontainers.image.title="CentOS Base Image" \
    org.opencontainers.image.vendor="CentOS" \
    org.opencontainers.image.licenses="GPL-2.0-only" \
    org.opencontainers.image.created="2020-11-13 00:00:00+00:00"

CMD ["/bin/bash"]

# --------------
FROM centos
MAINTAINER qgs

ENV MYPATH /usr/local    # 配置一些环境变量
WORKDIR $MYPATH          # 使用上面的变量

RUN yum -y install vim
RUN yum -y install net-tools

EXPOSE 80

CMD echo $MYPATH
CMD echo "end..."
CMD /bin/bash

# ------------
docker build -f dockerfile.txt -t centos-vim .
```





#### 数据卷容器

容器间进行数据共享(配置文件，数据文件)

```shell
docker run -it --name centos01 e2b8b2bb810d
control + p + q # 后台运行
docker run -it --name centos02 --volumes-from centos01  e2b8b2bb810d          
docker run -it --name centos03 --volumes-from centos01  e2b8b2bb810d 

--volumes-from 容器间数据共享

docker rm -f centos01 # 删除01之后其他还在
```

```shell
# 多个mysql数据共享

docker run -d -p 3306:3306 -v /ect/mysql/conf.d -v /var/lib/mysql -e MYSQL_ROOT_PASSWORD=root --name mysql01 mysql/mysql-server # 先启动一个

docker run -d -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root --name mysql02 --volumes-from mysql01 # 启动另一个
```



