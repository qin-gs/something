### Oracle

用一个表的数据更新另一个表  
```sql
update A set(a1, a2, a3) = (select b1, b2, b3 from B where A.id=B.id)  
```

#### **创建存储过程**

```sql
create or replace procedure test as
begin 
    insert into A values(sysdate);
end;
```

#### **定时任务**

```sql
创建一个job
-- what 将被执行的pl/sql语句  
-- next_date 何时运行(可以不指定)  
-- interval 何时被重新执行  
declare
    jobno number := 1;
begin
    dbms_job.submit(jobno, 'insert into A(today) values (sysdate)', 
        sysdate, 'trunc(sysdate, "mi") + 1 / (24 * 60)');
    commit;  
end;

查询定时任务job的id
select * from user_jobs;
select * from dba_jobs;

根据id, 运行定时任务
begin
    dbms_job.run(jobid);
    commit;
end;

停止已启动任务  
begin
    dbms_job.broken(25, true, sysdate);
    commit;
end;

删除定时任务
begin
    dbms_job.remove(jobid);
    commit;
end;

修改参数
dbms_job.next_date(job, next_date);
dbms_job.interval(job, interval);
```

#### **start with 层级查询**

```sql
create table DEMO (
    ID varchar2(10) primary key,
    DSC varchar2(100),
    PID varchar2(10)
)
--插入几条数据			主键id，当前数据描述，父级id
Insert Into DEMO values ('00001', '中国', '-1');
Insert Into DEMO values ('00011', '陕西', '00001');
Insert Into DEMO values ('00012', '贵州', '00001');
Insert Into DEMO values ('00013', '河南', '00001');
Insert Into DEMO values ('00111', '西安', '00011');
Insert Into DEMO values ('00112', '咸阳', '00011');
Insert Into DEMO values ('00113', '延安', '00011');

SELECT ... FROM    + 表名
WHERE              + 条件3(应用后面的条件将数据查询出来再进行筛选，必须在connect前面)
START WITH         + 条件1(起始条件；如果省略就默认把所有满足查询条件的Tree整个表中的数据从头到尾遍历一次,每一个数据做一次根,然后遍历树中其他节点信息)
CONNECT BY PRIOR   + 条件2(连接条件)

--示例
Select *, connect_by_isleaf as isLeaf, level 
From DEMO
	Start With ID = '00001'
	Connect By Prior ID = PID(上一条数据的id是本条数据的pid；prior在父节点的一侧则从下向上查，在子节点的一侧则从上向下查)

Connect By nocycle Prior ID = PID 用来消除循环
connect_by_isleaf 表示当前节点是否是叶子节点
level 表示当前节点所处层级, 这里的层级指的是 从start with查询到的节点开始往下算起, 当前属于第几层级
	如果采用的是 自底向上的 方式查询, 则 LEVEL 的 层级 同样是 从底向上, 如 00113 LEVEL 1 00011 LEVEL 2 00001 LEVEL 3.

Select ID, PID, DSC,
connect_by_isleaf,
LEVEL
From DEMO
Start With ID = '00001'
Connect By nocycle Prior ID =  PID
ORDER SIBLINGS  By DSC
SIBLINGS关键字:它会保护层次，并且在每个等级中按expre排序。

```

#### **分析函数**

分析函数是Oracle专门用于解决复杂报表统计需求的功能强大的函数，它可以在数据中进行分组然后计算基于组的某种统计值，并且每一组的每一行都可以返回一个统计值。

普通的聚合函数用group by分组，每个分组返回一个统计值，
分析函数采用partition by分组，并且每组每行都可以返回一个统计值。

分析函数带有一个开窗函数over()，包含三个分析子句:

- 分组(partition by)
- 排序(order by)
- 窗口(rows)

连续求和分析函数 sum() over()

``````sql
sum(...) over( ) 对所有行求和
sum(...) over( order by ... ) 连续求和
sum(...) over( partition by... ) 同组内行求和
sum(...) over( partition by... order by ... ) 同第1点中的排序求和原理，只是范围限制在组内

select e.deptno,
       e.ename,
       e.sal,
       sum(sal) over(order by e.ename) 连续求和,
       sum(sal) over() 总和, -- 此处sum(sal) over () 等同于sum(sal)
       100 * round(sal / sum(sal) over(), 4) "份额(%)"
  from emp e;
 
DEPTNO ENAME       SAL       连续求和         总和      份额(%)
------ ---------- --------- ---------- ---------- ----------
    20 ADAMS   1100.00       1100      29025       3.79
    30 ALLEN   1600.00       2700      29025       5.51
    30 BLAKE   2850.00       5550      29025       9.82
    10 CLARK   2450.00       8000      29025       8.44
    20 FORD   3000.00      11000      29025      10.34
    30 JAMES    950.00      11950      29025       3.27
    20 JONES   2975.00      14925      29025      10.25
    10 KING    5000.00      19925      29025      17.23
    30 MARTIN   1250.00      21175      29025       4.31
    10 MILLER   1300.00      22475      29025       4.48
    20 SCOTT   3000.00      25475      29025      10.34
    20 SMITH    800.00      26275      29025       2.76
    30 TURNER   1500.00      27775      29025       5.17
    30 WARD   1250.00      29025      29025       4.31
 
select deptno,
       ename,
       sal,
       sum(sal) over(partition by deptno order by ename) 部门连续求和, --各部门的薪水"连续"求和
       sum(sal) over(partition by deptno) 部门总和, -- 部门统计的总和，同一部门总和不变
       100 * round(sal / sum(sal) over(partition by deptno), 4) "部门份额(%)",
       sum(sal) over(order by deptno, ename) 连续求和, --所有部门的薪水"连续"求和
       sum(sal) over() 总和, -- 此处sum(sal) over () 等同于sum(sal)，所有员工的薪水总和
       100 * round(sal / sum(sal) over(), 4) "总份额(%)"
  from emp

``````

![](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTYxMTAzMjIzNTMzOTAz)



#### **wm_count**

拼接一些内容

```sql
u_id  goods  num 
------------------
1     苹果    2 
1     西瓜    4 
1     橘子    3
2     梨子    5 
3     葡萄    1 
3     香蕉    1 

select u_id, 
       wmsys.wm_concat(goods) goods_sum   
from shopping
group by u_id
--------------------
u_id  goods_sum 
____________________ 
1     苹果,西瓜,橘子 
2     梨子 
3     葡萄,香蕉 
-------------------- 
 
select u_id, 
       wmsys.wm_concat(goods || '(' || num || '斤)' ) goods_sum   
from shopping
group by u_id
-------------------------------- 
u_id  goods_sum 
_____________________
1     苹果(2斤),西瓜(4斤),橘子(3斤) 
2     梨子(5斤) 
3     葡萄(1斤),香蕉(1斤) 
---------------------------------

```



```sql
function REGEXP_SUBSTR(String, pattern, position, occurrence, modifier)

string:需要进行正则处理的字符串
pattern：进行匹配的正则表达式
position：起始位置，从字符串的第几个字符开始正则表达式匹配（默认为1） 注意：字符串最初的位置是1而不是0
occurrence：获取第几个分割出来的组（分割后最初的字符串会按分割的顺序排列成组）
modifier：模式（‘i’不区分大小写进行检索；‘c’区分大小写进行检索。默认为’c’）针对的是正则表达式里字符大小写的匹配

select regexp_substr('1|23|456|7890|12', '[^|]+', 1, 1) as s0,
       regexp_substr('1|23|456|7890|12', '[^|]+', 1, 2) as s1,
       regexp_substr('1|23|456|7890|12', '[^|]+', 1, 3) as s2,
       regexp_substr('1|23|456|7890|12', '[^|]+', 1, 4) as s3,
       regexp_substr('1|23|456|7890|12', '[^|]+', 1, 5) as s4
from dual;

s0	s1	 s2	  s3    S4
1	23	 456  7890  12
```

```sql
function REGEXP_COUNT ( source_char, pattern [, position [, match_param]])

REGEXP_COUNT 返回pattern 在source_char 串中出现的次数。如果未找到匹配，则函数返回0。
position 变量告诉Oracle 在源串的什么位置开始搜索。在开始位置之后每出现一次模式，都会使计数结果增加1。
match_param 变量支持下面几个值：
'i' 用于不区分大小写的匹配
'c' 用于区分大小写的匹配
'n' 允许句点(.)作为通配符去匹配换行符。如果省略该参数，则句点将不匹配换行符
'm' 将源串视为多行。即Oracle 将^和$分别看作源串中任意位置任何行的开始和结束，而不是仅仅看作整个源串的开始或结束。如果省略该参数，则Oracle将源串看作一行。
'x' 忽略空格字符。默认情况下，空格字符与自身相匹配。

select REGEXP_COUNT('abc_bcd_abc','bc',1,'i')
from DUAL;

3
```



```sql
MERGE INTO [target-table] A 
	USING [source-table sql] B 
	ON([conditional expression] and [...]...) 
WHEN MATCHED THEN 
	[UPDATE sql] 
WHEN NOT MATCHED THEN 
[INSERT sql]

判断Ｂ表和Ａ表是否满足ON中条件，如果满足则用B表去更新A表，如果不满足，则将B表数据插入A表但是有很多可选项，如下:
1.正常模式
2.只update或者只insert
3.带条件的update或带条件的insert
4.全插入insert实现
5.带delete的update(觉得可以用3来实现)

MERGE INTO JYJF_j2_1_2021 a
USING (SELECT ? as dwdm, ? as ztid, ? as xxlbdm, ? as dqdm
       FROM dual) b
ON (a.dwdm = b.dwdm and a.ztid = b.ztid)
WHEN MATCHED THEN
    UPDATE
    SET a.dqdm = b.dqdm
WHEN NOT MATCHED THEN
    INSERT (dwdm, dqdm, xxlbdm)
    values (b.dwdm, b.dqdm, b.xxlbdm);
```



#### listagg

行转列函数

```SQL
-- 比如一个人的多个手机号
select a, listagg(b, ', ') within group ( order by b) as b
from TEST
group by a;


A	B
-------
1	aaa
1	bbb
2	ccc
3	ddd
4	eee
5	fff
5	ggg

A	B
--------------
1	"aaa, bbb"
2	ccc
3	ddd
4	eee
5	"fff, ggg"

```













































