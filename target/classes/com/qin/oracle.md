#### oracle

用一个表的数据更新另一个表  
```sql
update A set(a1, a2, a3) = (select b1, b2, b3 from B where A.id=B.id)  
```

创建存储过程
```sql
create or replace procedure test as
begin 
    insert into A values(sysdate);
end;

```

定时任务
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






























































