



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

\rm # 转义命令，执行原本的命令，不使用别名
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



### 1.10 日期

```sh
date +%s # 获取时间戳 (1970-now 的秒)
date "+%Y %B %d"

# 计算命令执行时间
start=${date +%s}
echo "exec commands"
end=${date +%s}
diff=${( end - start )}
echo "总花费 ${diff}s"
```



### 1.11 调试脚本

```sh
bash -x script.sh # 执行运行的每一条命令

for i in {1..6} # `seq 1 6`
do
  set -x # 部分调试
  echo ${i}
  set +x
done
```



### 1.12 函数

```sh
# 函数：参数可以在函数体中任意位置使用
# 别名：只能将参数放在命令末尾

# 函数定义
function fName() {}
fName() {
  echo $0 $1 $2 # 输出脚本名称，参数
  echo "$@" # 将所有参数作为一个整体输出
  echo "$*" # 将所有参数分开输出
  
  for i in `seq 1 10`
  do
    echo $i is $1 # 后面使用了 shift，这里用 $1 就能不断访问所有的参数
    shift # 将参数左移一个位置
  done
}

# 函数调用
fName arg1 arg2

# 函数导出，将函数作用域扩展到子进程中
export -f fName
```



### 1.13 管道连接过滤器

```sh
# 将命令的输出赋值给变量
out=$(ls sh.txt | cat -n)
out=`ls sh.txt | cat -n`
echo ${out}

# 利用 shell 定义一个子 shell
# 后续所有的改变仅限于子 shell 中
(cd /bin ; ls)
out=${cat sh.txt} # 会丢失换行
out="${cat sh.txt}" # 会保留换行
```



### 1.14 读入字符

```sh
# 读入 n 个字符存入便利 v
read -n 2 v
echo ${v}

read -s v # 无回显
read -p "enter password: " v # 显示提示信息
read -t 10 v # 限时读取
read -d ":" v # 以特定字符作为结尾
```



### 1.15 持续运行至成功

```sh
repeat() {
  while true # while :;
  do
    $@ && return; # 运行传入的命令至运行成功
  done
}
```



### 1.16 字段分隔符 与 迭代器

```sh
oltIfs=${IFS}
IFS=,
for item in "1,2,3" # 使用,作为分隔符
do
  echo ${item}
done
IFS=oldIfs
```



#### for 循环

```sh
list=(( "a" "b" "c" ))

for v in list
do
  echo ${v}
done

for ((i=0;i<1-;i++)) {
  echo ${i}
}

while condition
do
   echo "hello"
done

x=0
until [ $x -eq 9 ];
do
  let x++
  echo $x
done
```



### 1.17 比较 和 测试

#### 条件

```sh
x=0
if [ ${x} -lt 3 ];
then
  echo "x=${x} < 3"
else if [ ${x} -lt 7 ];
then
  echo "3 < x=${x} < 7"
else
  echo "x=${x} >= 7"
fi


[ ${x} -lt 3] && echo "x < 3"
```



#### 算术 逻辑 比较

```sh
-gt：大于。
-lt：小于。
-ge：大于或等于。
-le：小于或等于。

-a 与操作
-o 或操作
```



#### 文件系统相关测试

```sh
[ -f $file_var ]：如果给定的变量包含正常的文件路径或文件名，则返回真。
[ -x $var ]：如果给定的变量包含的文件可执行，则返回真。
[ -d $var ]：如果给定的变量包含的是目录，则返回真。
[ -e $var ]：如果给定的变量包含的文件存在，则返回真。
[ -c $var ]：如果给定的变量包含的是一个字符设备文件的路径，则返回真。
[ -b $var ]：如果给定的变量包含的是一个块设备文件的路径，则返回真。
[ -w $var ]：如果给定的变量包含的文件可写，则返回真。
[ -r $var ]：如果给定的变量包含的文件可读，则返回真。
[ -L $var ]：如果给定的变量包含的是一个符号链接，则返回真。
```



#### 字符串比较

```sh
[[ $str1 == $str2 ]]
[[ $str1 != $str2 ]]

[[ -z $str1 ]]：如果str1为空串，则返回真。
[[ -n $str1 ]]：如果str1不为空串，则返回真。

# test 命令可以用来测试条件，避免太多的括号
```



### 1.18  使用配置文件定制 bash



## 2. 命令之乐



### 2.2 使用 cat 进行拼接

```sh
echo "this is some text" | cat - # - 标准输出 stdin 的文件名

cat -s file # 压缩空白行
cat -b file # 去除空白行
cat -T file # 将制表符展示为 ^T
cat -n file # 添加行号
```



### 2.3 录制并回放终端会话

```sh
script -t 2> time.log -a output.session # exit 或 ctrl+d 退出
```



### 2.4 查找文件

```sh
# 单引号避免特殊字符
find . -name '*.txt' -print # -iname 忽略大小写
# -a -o 与或
find . \( -name '*e*' -and -name 's*' \) -print
# -path 路径匹配
# -regex 正则匹配
# ! 取反
# 默认不跟随符号连接，加上-L会跟随符号连接
# -maxdepth -mindepath 限制遍历的目录深度


# -type 根据文件类型搜索
find . -type f -print
# 普通文件  f
# 符号链接  l
# 目录     d
# 字符设备  c
# 块设备   b
# 套接字   s
# FIFO    p


# 根据时间搜索
find . -type f -atime -7 -print # 查找最近七天内访问过的文件
find . -type f -atime - -print # 查找七天前被访问的文件
find . -type f -atime +7 -print # 查找访问时间超过七天的文件

# 以天为单位
# 访问时间（-atime）：用户最近一次访问文件的时间。
# 修改时间（-mtime）：文件内容最后一次被修改的时间。
# 变化时间（-ctime）：文件元数据（例如权限或所有权）最后一次改变的时间
# -表示小于，+表示大于 指点天数

# 以分钟为单位
# -amin（访问时间）
# -mmin（修改时间）
# -cmin（变化时间）

# –newer 选项可以指定一个用于比较修改时间的参考文件
# 找出比file.txt修改时间更近的所有文件：
find . -type f -newer file.txt -print

# -size 根据文件大小搜索
find .t -type f -size +2k # 查找大于 2k 的文件
# b：块（512字节）
# c：字节
# w：字（2字节）
# k：千字节（1024字节）。
# M：兆字节（1024K字节）
# G：吉字节（1024M字节）

# -perm 根据文件权限搜索
find . -type f -perm 755 -print
# -user 根据指定用户搜索
find . -type f -user root -print



# 找到后进行操作
find . -type f -name '*.java' -delete # 找到后删除

# 找到 root 用户拥有的文件修改成 qgs 用户
# {} 表示文件名，;需要转义否则 shell 会将其视为 find 命令的结束而非 chown 命令的结束
find . -type f -user root -exec chown qgs {} \; 


# -prune 跳过指定目录
find . -name '.git' -purne -o -type f -print # 排除 .git 目录
```



### 2.5 xargs

```sh
# 调用只接受命令行参数的命令
gcc `find '*.c'` # 找到文件然后编译

# xargs 跟在管道之后，使用标准输入作为数据源，从 stdin 中读取的数据作为命令的参数


# 多行 -> 单行
cat sh.txt | xargs
# 单行 -> 多行
cat sh.txt | xargs -d ',' -n 3 # 用,拆分输入 每行三个

# 字符串替换
cat sh.txt | xargs -I {} ./sh.sh -p {} -l

# 删除文件 (使用空字符作为分割 '\0'，避免删错)
find . -type f -name '*.txt' -print0 | xargs -0 rm -f

# 统计文件行数
find . -type f -name '*.java' -print0 | xargs -0 wc -l
```



### 2.6 tr 转换

```sh
# 只能从 stdin 中接收输入
echo 'Hello world' | tr 'A-Z' 'a-z'
# 替换制表符
tr '\t' ' ' < file.txt


# -d 删除
ecbo 'hello 123 world' | tr -d '0-9'

# -c 补集 (将不在 0-9 的内容替换成空格)
echo 'hello 2' | tr -c '0-9' ' '
# 删除不在补集中的所有字符
echo 'hello 2' | tr -d -c '0-9 \n'

# -s 压缩字符
echo 'hello   world' | tr -s ' '
cat hello.txt | tr -s '\n'

# 文件中的数字求和 $[ 1+2+3+0 ]
cat num.txt | echo $[ $(tr '\n' '+') 0 ]
cat count.txt | tr -d [a-z] | echo "total: $[$(tr ' ' '+')]"


# 小写 -> 大写
echo 'Hello World' | tr '[:lower:]' '[:upper:]'
```



### 2.7 校验和核实

```sh
md5sum filename
sha1sum filename
```



### 2.8 加密

```sh
base64 raw.txt > base64.txt
base64 -d base64.txt > raw.txt
```



### 2.9 行排序



```sh
sort -n file.txt # 按数字顺序排序
sort -r filt.txt # 逆序

if sort -c file.txt ; then echo sorted; else echo unsorted; fi

sort -nrk 1 file.txt # 按照第一列逆序排序
sort -bd file.txt # 忽略空白，按字段顺序排序
```

```sh
sort file.txt | uniq # 去除重复行
uniq -u file.txt # 只显示不重复的行
uniq -d file.txt # 只显示重复的行
sort file.txt | uniq -c # 统计每行出现次数
```









 
