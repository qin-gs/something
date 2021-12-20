### Nginx



#### 简介

高性能的http 和 反向代理web服务器



#### 作用

正向代理，反向代理(用户不可知)

客户端   ->   代理   ->   服务器



负载均衡

动静分离



#### 命令

启动   ./nginx

停止   nginx -s  stop

安全停止  nginx  -s  quit

重载配置文件   nginx  -s  reload

查看进程   ps -aux | grep nginx

版本号   nginx -v



查看端口是否开放：firewall-cmd  --query-port=8080/tc

开放端口：firewall-cmd  --permanent  --add-port=80/tcp

移除开放端口：firewall--cmd  --permanent  --remove-port=8080/tcp

查看防火墙规则：firewall  -cmd  --list-all

重启防火墙：firewall-cmd  --reload





**配置**

 nginx.conf

1. 全局块：配置影响nginx全局的指令。一般有运行nginx服务器的用户组，nginx进程pid存放路径，日志存放路径，配置文件引入，允许生成worker process数等。

2. events块：配置影响nginx服务器或与用户的网络连接。有每个进程的最大连接数，选取哪种事件驱动模型处理连接请求，是否允许同时接受多个网路连接，开启多个网络连接序列化等。

3. http块：可以嵌套多个server，配置代理，缓存，日志定义等绝大多数功能和第三方模块的配置。如文件引入，mime-type定义，日志自定义，是否使用sendfile传输文件，连接超时时间，单连接请求数等。

4. server块：配置虚拟主机的相关参数，一个http中可以有多个server。

5. location块：配置请求的路由，以及各种页面的处理情况。



```
worker_processes  1; # worker 进程的数量

events {
    worker_connections  1024; # 每个 worker 支持的最大连接数
}

http {
    include       mime.types; # nginx 支持的媒体类型
    default_type  application/octet-stream; # 默认的媒体类型

    sendfile        on; # 开启高效传输模式

    keepalive_timeout  65; # 连接超时

    server { # server 块，表示一个独立的虚拟主机站点
        listen       80; # 提供服务的端口
        server_name  localhost; # 提供服务的主机名

        location / {
            root   html; # 站点的根目录
            index  index.html index.htm; # 默认的首页文件
        }

        error_page   500 502 503 504  /50x.html; # 出现对应的状态码，返回对应的页码
        location = /50x.html {
            root   html;
        }

    }

}


```

location指令的作用是根据用户请求的URI来执行不同的应用，也就是根据用户请求的网站URL进行匹配，匹配成功即进行相关的操作。

| 匹配符 |           匹配规则           | 优先级 |
| :----: | :--------------------------: | :----: |
|   =    |           精确匹配           |   1    |
|   ^~   |       以某个字符串开头       |   2    |
|   ~    |     区分大小写的正则匹配     |   3    |
|   ~*   |    不区分大小写的正则匹配    |   4    |
|   !~   |    区分大小写不匹配的正则    |   5    |
|  !~*   |   不区分大小写不匹配的正则   |   6    |
|   /    | 通用匹配，任何请求都会匹配到 |   7    |

```json
########### 每个指令必须有分号结束。#################
#user administrator administrators;  #配置用户或者组，默认为nobody nobody。
#worker_processes 2;  #允许生成的进程数，默认为1
#pid /nginx/pid/nginx.pid;   #指定nginx进程运行文件存放地址
error_log log/error.log debug;  #制定日志路径，级别。这个设置可以放入全局块，http块，server块，级别以此为：debug|info|notice|warn|error|crit|alert|emerg
events {
    accept_mutex on;   #设置网路连接序列化，防止惊群现象发生，默认为on
    multi_accept on;  #设置一个进程是否同时接受多个网络连接，默认为off
    #use epoll;      #事件驱动模型，select|poll|kqueue|epoll|resig|/dev/poll|eventport
    worker_connections  1024;    #最大连接数，默认为512
}
http {
    include       mime.types;   #文件扩展名与文件类型映射表
    default_type  application/octet-stream; #默认文件类型，默认为text/plain
    #access_log off; #取消服务日志    
    log_format myFormat '$remote_addr–$remote_user [$time_local] $request $status $body_bytes_sent $http_referer $http_user_agent $http_x_forwarded_for'; #自定义格式
    access_log log/access.log myFormat;  #combined为日志格式的默认值
    sendfile on;   #允许sendfile方式传输文件，默认为off，可以在http块，server块，location块。
    sendfile_max_chunk 100k;  #每个进程每次调用传输数量不能大于设定的值，默认为0，即不设上限。
    keepalive_timeout 65;  #连接超时时间，默认为75s，可以在http，server，location块。

    upstream mysvr {   
      server 127.0.0.1:7878;
      server 192.168.10.121:3333 backup;  #热备
    }
    error_page 404 https://www.baidu.com; #错误页
    server {
        keepalive_requests 120; #单连接请求上限次数。
        listen       4545;   #监听端口
        server_name  127.0.0.1;   #监听地址       
        location  ~*^.+$ {       #请求的url过滤，正则匹配，~为区分大小写，~*为不区分大小写。
           #root path;  #根目录
           #index vv.txt;  #设置默认页
           proxy_pass  http://mysvr;  #请求转向mysvr 定义的服务器列表
           deny 127.0.0.1;  #拒绝的ip
           allow 172.18.5.54; #允许的ip           
        } 
    }
}
```



#### 反向代理

```
# 访问 www.123.com 自动跳转到 http://127.0.0.1:8080
server {
        listen       80;
        server_name  www.123.com;

        location / {
            proxy_pass http://127.0.0.1:8080;
            index  index.html index.htm index.jsp;
        }
    }

listen
listen *:80 | *:8080 #监听所有80端口和8080端口
listen  IP_address:port   #监听指定的地址和端口号
listen  IP_address     #监听指定ip地址所有端口
listen port     #监听该端口的所有IP连接

server_name
匹配优先级：
  准确匹配
  通配符在开始时匹配
  通配符在结尾时匹配
  正则表达式匹配

可以只有一个名称，也可以有多个名称，中间用空格隔开。而每个名字由两段或者三段组成，每段之间用“.”隔开
server_name 123.com www.123.com
可以使用通配符“*”，但通配符只能用在由三段字符组成的首段或者尾端，或者由两端字符组成的尾端
server_name *.123.com www.123.*
还可以使用正则表达式，用“~”作为正则表达式字符串的开始标记。
server_name ~^www\d+\.123\.com$;

location
该指令用于匹配 URL

location [ = | ~ | ~* | ^~] uri {

}


proxy_pass
该指令用于设置被代理服务器的地址。可以是主机名称、IP地址加端口号的形式。

proxy_pass URL;
proxy_pass  http://www.123.com/uri;
URL 为被代理服务器的地址，可以包含传输协议、主机名称或IP地址加端口号，URI等。

index
该指令用于设置网站的默认首页。

index  filename ...;
index  index.html index.jsp;
后面的文件名称可以有多个，中间用空格隔开。

```



#### 负载均衡

1. 普通(加权)轮询算法

   ```
   OrdinaryPolling为负载均衡名称，可以任意指定，但必须跟vhosts.conf虚拟主机的pass段一致，否则不能转发后端的请求。weight配置权重，在fail_timeout内检查max_fails次数，失败则剔除均衡。
   
   upstream OrdinaryPolling {
       server   127.0.0.1:8080 weight=1 max_fails=2 fail_timeout=30s;
       server   127.0.0.1:8081 weight=1 max_fails=2 fail_timeout=30s;
   }
   
   server {
       listen       80;
       server_name  localhost;
   
       location / {
           proxy_pass http://OrdinaryPolling;
           index  index.html index.htm index.jsp;
       }
   }
   ```

2. 基于ip路由负载 (同一个ip的请求路由到相同的服务器)

   ```
   upstream OrdinaryPolling {
       ip_hash; # 根据ip进行路由
       server 127.0.0.1:8080;
       server 127.0.0.1:8081;
   }
   
   server {
       listen       80;
       server_name  localhost;
   
       location / {
           proxy_pass http://OrdinaryPolling;
           index  index.html index.htm index.jsp;
       }
   }
   ```

3. 基于服务器响应时间负载 (安装upstream_fair模块)

   根据服务器处理请求的时间进行负载，响应时间短的优先分配

   ```
   upstream OrdinaryPolling {
       fair; # 基于服务器响应时间负载
       server 127.0.0.1:8080;
       server 127.0.0.1:8081;
   }
   ```

4. 对不同域名实现负载均衡

   按访问url的hash结果来分配请求，使每个url定向到同一个后端服务器

   根据 location 实现
   
   ```
   upstream wordbackend {
       server 127.0.0.1:8080;
       server 127.0.0.1:8081;
   }
   
   upstream pptbackend {
       server 127.0.0.1:8082;
       server 127.0.0.1:8083;
   }
   
   server {
       listen       80;
       server_name  localhost;
   
       location /word/ {
           proxy_pass http://wordbackend;
           index  index.html index.htm index.jsp;
       }
       location /ppt/ {
           proxy_pass http://pptbackend;
           index  index.html index.htm index.jsp;
       }
   }
   ```



#### 静态分离

需要指定路径对应的目录。location/可以使用正则表达式匹配。并指定对应的硬盘中的目录

```
http://localhost/image/1.jpg  -> http://localhost/usr/local/static/1.jpg

location /image/ {
    root   /usr/local/static/;
    autoindex on;
}
```

