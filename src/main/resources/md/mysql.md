### Mysql

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



#### mysql 日期格式化

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

