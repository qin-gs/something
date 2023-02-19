



检查用户是否为 超级用户

```sh
if [ $UID -eq 0 ]; then
  echo "this is root user"
fi

# 添加环境变量
prepend() {
  [ -d "$2" ] && eval $1=\"$2\$\{$1:+':'\$$1\}\" && export $1 ;
  # ${param:+expre} 当 param 有值且不为空是，使用 expre 的值
}
prepend PATH /bin/bash
```



### 1.5 数学运算

```sh
no1=3
n02=4
let result=no1+no2 # 使用 let 进行运算，不需要添加 $
result=$[ no1 + no2 ]
result=$(( no1 + no2 ))
echo $result

# 浮点数运算 bc
result=`echo "4 * 0.25" | bc`
# 设定精度
echo "scale=2;1/3" | bc
# 进制转换
echo "obase=2;$result" | bc # obase-转换后 ibase-转换前
# 平方 开房
echo "sqrt(100)" | bc #Square root
echo "10^10" | bc #Square
```



### 1.6 文件描述符 重定向

- 0 stdin 标准输入
- 1 stdout 标准输出
- 2 stderr 标准错误

- &gt; 清空内容后添加

- &gt;&gt; 追加

```sh
ls + 2>err.txt # 将标准错误冲重定向到文本
cmd 2>&1 all.txt # 将标准错误转换成标准输出，使两者重定向到同一个文本
cmd &> all.txt # 可以重定向到 /dev/null 清除信息

# 保存信息的同时作为管道后续命令的输入 (tee 命令)
ls -al | tee ls.txt | grep hello.java

# 标准输入 将文件重定向到命令
cat <<EOF>log.txt

# 自定义文件描述符
```



### 1.7 数组与关联数组

- 数组：使用整数作为索引
- 关联数组：使用字符串作为数组索引



```sh
# 定义数组
array=(first, second, thrid)
array[4]="forth"

echo ${array[1]} # 输出指定元素
echo ${array[*]}
echo ${array[@]} # 两种方式输出所有元素
echo ${#array[*]} # 获取数组长度

# 关联数组
declare -A arr # 将变量声明为关联数组
arr=([apple]=100 [orange]=150) # 初始化
arr["banana"]=200
echo ${!arr[*]} # 获取所有索引 @也可以
```



### 1.8 别名

```sh
alias rm='cp $@ ~/backup && rm $@'
unalias rm

\rm # 转义命令，执行原本的命令
```



### 1.9 采集终端信息

```sh
tput # 获取终端信息

echo -e "enter password:"
stty -echo # 禁止将密码发到终端
read password
stty echo # 恢复
echo "password read"
```





















 