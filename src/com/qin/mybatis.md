参考: <https://mybatis.org/mybatis-3/zh/sqlmap-xml.html#cache>  
#### 缓存
默认情况下，只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。 要启用全局的二级缓存，只需要在你的 SQL 映射文件中添加一行  
```
</cache>
```
1.映射语句文件中的所有 select 语句的结果将会被缓存。  
2.映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存。  
3.缓存会使用最近最少使用算法（LRU, Least Recently Used）算法来清除不需要的缓存。  
4.缓存不会定时进行刷新（也就是说，没有刷新间隔）。  
5.缓存会保存列表或对象（无论查询方法返回哪种）的 1024 个引用。  
6.缓存会被视为读/写缓存，这意味着获取到的对象并不是共享的，可以安全地被调用者修改，而不干扰其他调用者或线程所做的潜在修改。  

    <cache
      eviction="FIFO"
      flushInterval="60000"
      size="512"
      readOnly="true"/>
1.eviction 回收算法  
2.flushInterval 刷新间隔  
3.size 保存引用数量  
4.readOnly  
- 4.1 只读的缓存会给所有调用者返回缓存对象的相同实例。 因此这些对象不能被修改。这就提供了可观的性能提升  
- 4.2 而可读写的缓存会（通过序列化）返回缓存对象的拷贝。 速度上会慢一些，但是更安全，因此默认值是 false。    

二级缓存是事务性的。这意味着，当 SqlSession 完成并提交时，或是完成并回滚，但没有执行 flushCache=true 的 insert/delete/update 语句时，缓存会获得更新。  





