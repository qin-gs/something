# Linux Shell脚本攻略



## 一、小试牛刀



### 终端中输出

双引号：允许出现特殊字符，可以通过 $ 访问变量

单引号：不会进行任何处理



```sh
echo "hello" # 自动添加换行符 -n 去除换行符
printf "%-5s %-10s %-4.2f\n" 2 James 90.9989 # 不会自动添加换行
# 格式替换符
# %-5s 左对齐宽度为 5 的字符串
# %c
# %d
# %4.2f 保留两位小数
```



### 环境变量

```sh
env
printevn # 查看环境变量
cat /proc/65741/environ | tr '\0' '\n' # 查看指定进程的环境变量
```

变量声明

```sh
varName=varValue # 不可以有空格，加上空格后表示等量关系测试
echo ${varName}, $varName # 访问变量内容，可以在双引号中引用

length=${#varName} # 获取变量值长度
echo $SHELL $0 # 获取当前使用的 shell
```



























































