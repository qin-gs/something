# Mysql是怎样运行的



## 2. 启动选项和系统变量

### 启动选项

短形式的选项名只有一个字母，与使用长形式选项时需要在选项名前加两个短划线`--`不同的是，使用短形式选项时在选项名前只加一个短划线`-`前缀。使用短形式指定启动选项时，选项名和选项值之间可以没有间隙，或者用空白字符隔开（`-p`选项有些特殊，`-p`和密码值之间不能有空白字符）

对于启动选项来说，如果启动选项名由多个单词组成，各个单词之间用短划线`-`或者下划线`_`连接起来都可以，但是对应的系统变量之间必须使用下划线`_`连接起来。



| 长形式       | 短形式 | 含义     |
| ------------ | ------ | -------- |
| `--host`     | `-h`   | 主机名   |
| `--user`     | `-u`   | 用户名   |
| `--password` | `-p`   | 密码     |
| `--port`     | `-P`   | 端口     |
| `--version`  | `-V`   | 版本信息 |



### 配置文件位置

Windows

| 路径名                                | 备注                                   |
| ------------------------------------- | -------------------------------------- |
| `%WINDIR%\my.ini`， `%WINDIR%\my.cnf` | `echo %WINDIR%`                        |
| `C:\my.ini`， `C:\my.cnf`             |                                        |
| `BASEDIR\my.ini`， `BASEDIR\my.cnf`   | `BASEDIR`指的是`MySQL`安装目录的路径   |
| `defaults-extra-file`                 | 命令行指定的额外配置文件路径           |
| `%APPDATA%\MySQL\.mylogin.cnf`        | 登录路径选项，不是纯文本（仅限客户端） |

Unix

| 路径名                | 备注                                             |
| --------------------- | ------------------------------------------------ |
| `/etc/my.cnf`         |                                                  |
| `/etc/mysql/my.cnf`   |                                                  |
| `SYSCONFDIR/my.cnf`   |                                                  |
| `$MYSQL_HOME/my.cnf`  | 特定于服务器的选项（仅限服务器）                 |
| `defaults-extra-file` | 命令行指定的额外配置文件路径                     |
| `~/.my.cnf`           | 用户特定选项                                     |
| `~/.mylogin.cnf`      | 用户特定的登录路径选项，不是纯文本（仅限客户端） |



### 配置文件内容



定义多个分组

```
[server]
option1            #这是option1，该选项不需要选项值
option2 = value2      #这是option2，该选项需要选项值
...

[mysqld]
(具体的启动选项...)

[client]
(具体的启动选项...)
```



命令启动时会按顺序读取其他分组的配置

| 启动命令       | 类别       | 能读取的组                               |
| -------------- | ---------- | ---------------------------------------- |
| `mysqld`       | 启动服务器 | `[mysqld]`、`[server]`                   |
| `mysqld_safe`  | 启动服务器 | `[mysqld]`、`[server]`、`[mysqld_safe]`  |
| `mysql.server` | 启动服务器 | `[mysqld]`、`[server]`、`[mysql.server]` |
| `mysql`        | 启动客户端 | `[mysql]`、`[client]`                    |
| `mysqladmin`   | 启动客户端 | `[mysqladmin]`、`[client]`               |
| `mysqldump`    | 启动客户端 | `[mysqldump]`、`[client]`                |



### MySQL 系统变量

```sql
SHOW VARIABLES LIKE 'default_storage_engine';
```

可以动态修改而无需停止服务

系统变量的作用范围

- global：全局
- session：会话

不是所有系统变量都具有`GLOBAL`和`SESSION`的作用范围。有些系统变量是只读的(`version`)

```sql
SET GLOBAL default_storage_engine = MyISAM;
SET SESSION default_storage_engine = MyISAM;
```



### 状态变量

关于程序运行状态的变量，只能由服务器程序自己来设置；作用范围也是两种

(`Threads_connected`表示当前有多少客户端与服务器建立了连接，`Handler_update`表示已经更新了多少行记录)

```sql
SHOW STATUS LIKE 'thread%';
```



## 3. 字符集

```sql
SHOW VARIABLES LIKE 'character_set_client';
SHOW VARIABLES LIKE 'character_set_connection';
SHOW VARIABLES LIKE 'character_set_results';
```



| 系统变量                   | 描述                                                         |
| -------------------------- | ------------------------------------------------------------ |
| `character_set_client`     | 服务器解码请求时使用的字符集                                 |
| `character_set_connection` | 服务器处理请求时会把请求字符串从`character_set_client`转为`character_set_connection` |
| `character_set_results`    | 服务器向客户端返回数据时使用的字符集                         |



![字符转换](./assets/MySQL字符转换.png)



## 4. InnoDB 记录结构















































```sql
oracle 与 mysql 的一些对应函数

nvl -> ifnull
to_date(?,'yyyy-MM-dd hh24:mi:ss') -> str_to_date(?, '%Y-%m-%d %H:%i:%S')
to_char(?,'yyyy-MM-dd') -> date_format(?,'%Y-%m-%d')
to_number(?) -> cast(? as unsigned int)
wm_concat -> group_concat
sys_guid -> replace(uuid(), '-', '')
sysdate -> sysdate()
add_months(?) -> date_add(sysdate(), Interval ? month)
'a'||'b'||'c' -> concat('a', 'b', 'c')(任意一个为null结果就是null)  
				 (concat_ws('-', 'a', 'b')有分隔符的拼接)
```

```sql
-- group_concat使用

select name, group_concat(id order by id desc separator '_') from user group by name;

name group_result
qqq	 6_4_2
www  5_3_1
```



## mysql 日期格式化

- date_format(date, format) 函数，MySQL日期格式化函数date_format()

- unix_timestamp() 函数

- str_to_date(str, format) 函数

- from_unixtime(unix_timestamp, format) 函数，MySQL时间戳格式化函数from_unixtime

  ```sql
  -- 时间转字符串
  select date_format(now(), '%Y-%m-%d') as day
  -- 2021-11-11
  
  -- 时间转时间戳
  select unix_timestamp(now())
  -- 1636592439
  
  -- 字符串转时间
  select str_to_date('2021-11-11', '%Y-%m-%d %H');
  -- 2021-11-11 00:00:00
  
  -- 字符串转时间戳
  select unix_timestamp('2021-11-11');
  -- 1636560000
  
  -- 时间戳转时间
  select from_unixtime(1636565678, '%Y-%m-%d %H:%i:%s');
  select from_unixtime(1636565678, '%Y-%m-%d %T');
  -- 2021-11-11 01:34:38
  
  -- 时间戳转字符串
  select from_unixtime(1636560000,'%Y-%d');
  -- 2021-11
  ```

  日期格式化取值范围

  <table>
      <tbody>
      <tr>
          <th>&nbsp;</th>
          <th>值</th>
          <th>含义</th>
      </tr>
      <tr>
          <td>秒</td>
          <td>%S、%s</td>
          <td>两位数字形式的秒（ 00,01, ..., 59）</td>
      </tr>
      <tr>
          <td>分</td>
          <td>%I、%i</td>
          <td>两位数字形式的分（ 00,01, ..., 59）</td>
      </tr>
      <tr>
          <td rowspan="7">小时&nbsp;</td>
          <td>%H</td>
          <td>24小时制，两位数形式小时（00,01, ...,23）</td>
      </tr>
      <tr>
          <td>%h</td>
          <td>12小时制，两位数形式小时（00,01, ...,12）</td>
      </tr>
      <tr>
          <td>%k</td>
          <td>24小时制，数形式小时（0,1, ...,23）</td>
      </tr>
      <tr>
          <td>%l</td>
          <td>12小时制，数形式小时（0,1, ...,12）</td>
      </tr>
      <tr>
          <td>%T</td>
          <td>24小时制，时间形式（HH:mm:ss）</td>
      </tr>
      <tr>
          <td>%r</td>
          <td>&nbsp;12小时制，时间形式（hh:mm:ss AM 或 PM）</td>
      </tr>
      <tr>
          <td>%p&nbsp;</td>
          <td>AM上午或PM下午&nbsp;</td>
      </tr>
      <tr>
          <td rowspan="5">&nbsp;&nbsp;周&nbsp;</td>
          <td>&nbsp;%W</td>
          <td>一周中每一天的名称（Sunday,Monday, ...,Saturday）</td>
      </tr>
      <tr>
          <td>&nbsp;%a</td>
          <td>一周中每一天名称的缩写（Sun,Mon, ...,Sat）&nbsp;</td>
      </tr>
      <tr>
          <td>%w&nbsp;</td>
          <td>以数字形式标识周（0=Sunday,1=Monday, ...,6=Saturday）&nbsp;</td>
      </tr>
      <tr>
          <td>%U</td>
          <td>数字表示周数，星期天为周中第一天</td>
      </tr>
      <tr>
          <td>%u</td>
          <td>数字表示周数，星期一为周中第一天</td>
      </tr>
      <tr>
          <td rowspan="4">天</td>
          <td>%d&nbsp;</td>
          <td>两位数字表示月中天数（01,02, ...,31）</td>
      </tr>
      <tr>
          <td>%e&nbsp;</td>
          <td>&nbsp;数字表示月中天数（1,2, ...,31）</td>
      </tr>
      <tr>
          <td>&nbsp;%D</td>
          <td>英文后缀表示月中天数（1st,2nd,3rd ...）&nbsp;</td>
      </tr>
      <tr>
          <td>&nbsp;%j</td>
          <td>以三位数字表示年中天数（001,002, ...,366）&nbsp;</td>
      </tr>
      <tr>
          <td rowspan="4">月</td>
          <td>%M&nbsp;</td>
          <td>英文月名（January,February, ...,December）&nbsp;</td>
      </tr>
      <tr>
          <td>%b&nbsp;</td>
          <td>英文缩写月名（Jan,Feb, ...,Dec）&nbsp;</td>
      </tr>
      <tr>
          <td>%m&nbsp;</td>
          <td>两位数字表示月份（01,02, ...,12）</td>
      </tr>
      <tr>
          <td>%c&nbsp;</td>
          <td>数字表示月份（1,2, ...,12）&nbsp;</td>
      </tr>
      <tr>
          <td rowspan="2">年</td>
          <td>%Y&nbsp;</td>
          <td>四位数字表示的年份（2015,2016...）</td>
      </tr>
      <tr>
          <td>%y&nbsp;</td>
          <td>&nbsp;两位数字表示的年份（15,16...）</td>
      </tr>
      <tr>
          <td>文字输出&nbsp;</td>
          <td>%文字&nbsp;</td>
          <td>直接输出文字内容</td>
      </tr>
      </tbody>
  </table>

