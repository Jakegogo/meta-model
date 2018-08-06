# 元数据驱动引擎之 数据服务;
## 接入说明
### 1.服务化模式
如果需要独立的服务化server

1. 建立WebServer工程, 引入包
```java
<dependency>
    <groupId>com.concur.meta</groupId>
    <artifactId>model-core</artifactId>
</dependency>
```

2. 配置数据源, bean ID为dataSource
```java
<bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
	<property name="driverClassName" value="${spring.datasource.driverClassName}" />
	<property name="url" value="${spring.datasource.url}" />
	<property name="username" value="${spring.datasource.username}" />
	<property name="password" value="${spring.datasource.password}" />
	<property name="validationQuery" value="${spring.datasource.validation-query}" />
	<property name="maxTotal" value="${spring.datasource.max-active}" />
	<property name="testOnBorrow" value="${spring.datasource.test-on-borrow}" />
</bean>
```

3. 配置server端RPC服务, 可使用dubbo等分布式框架
```java
    <!-- 元数据读服务-->
    ......
    <bean id="metaDataReadServerService" class="com.concur.meta.core.service.impl.MetaDataReadServerServiceImpl"/>
    <dubbo:service timeout="3000" interface="com.concur.meta.client.service.server.MetaDataReadServerService" ref="metaDataReadServerService"/>
    ......

    <!-- 元数据写服务-->
    ......
    <bean id="metaDataWriteServerService" class="com.concur.meta.core.service.impl.MetaDataWriteServerServiceImpl"/>
    <dubbo:service timeout="3000" interface="com.concur.meta.client.service.server.MetaDataWriteServerService" ref="metaDataWriteServerService"/>
    ......

    <!-- 元数据数据源服务-->
    ......
    <bean id="metaDataSourceService" class="com.concur.meta.metadata.service.impl.DataSourceServiceImpl"/>
    <dubbo:service timeout="3000" interface="com.concur.meta.client.service.DataSourceService" ref="dataSourceService"/>
    ......
```

4. 配置client端的rpc服务
```java
<bean id="serviceFactory" class="com.concur.meta.client.service.ServiceFactory">
	<!-- 引用 rpc client bean metaDataReadServerService -->
        <property name="metaDataReadServerService" ref="metaDataReadServerService"/>
	<!-- 引用 rpc client bean metaDataWriteServerService -->
        <property name="metaDataWriteServerService" ref="metaDataWriteServerService"/>
	<!-- 引用 rpc client bean metaDataSourceService -->
        <property name="dataSourceService" ref="metaDataSourceService"/>
</bean>
```    
    
附录server端完整配置:
```
    <context:annotation-config/>
    <aop:aspectj-autoproxy />
	
    <context:component-scan base-package="com.concur.meta"/>

    <import resource="classpath:lmodel.datasource.xml" />

    <bean class="com.concur.meta.metadata.util.ApplicationContextUtils"/>

    <bean id="metaDataConfigService" class="com.concur.meta.metadata.service.impl.MetaDataConfigServiceImpl"/>

    <bean id="metaDataFactory" class="com.concur.meta.core.dbengine.factory.impl.MetaDataFactoryImpl"/>
    <bean id="tableMetaManager" class="com.concur.meta.core.manager.impl.TableMetaManagerImpl"/>
    <bean id="dataSourceManager" class="com.concur.meta.core.manager.impl.DataSourceManagerImpl"/>


    <bean id="lMetaService" class="com.concur.meta.metadata.service.impl.LMetaServiceImpl"/>

    <bean id="serviceFactory" class="com.concur.meta.client.service.ServiceFactory">
        <property name="metaDataReadServerService" ref="metaDataReadServerService"/>
        <property name="metaDataWriteServerService" ref="metaDataWriteServerService"/>
        <property name="dataSourceService" ref="metaDataSourceService"/>
    </bean>
     <!-- 元数据读服务-->
    <bean id="metaDataReadServerService" class="com.concur.meta.core.service.impl.MetaDataReadServerServiceImpl"/>
    <dubbo:service timeout="3000" interface="com.concur.meta.client.service.server.MetaDataReadServerService" ref="metaDataReadServerService"/>

    <!-- 元数据写服务-->
    <bean id="metaDataWriteServerService" class="com.concur.meta.core.service.impl.MetaDataWriteServerServiceImpl"/>
    <dubbo:service timeout="3000" interface="com.concur.meta.client.service.server.MetaDataWriteServerService" ref="metaDataWriteServerService"/>

    <!-- 元数据数据源服务-->
    <bean id="metaDataSourceService" class="com.concur.meta.metadata.service.impl.DataSourceServiceImpl"/>
    <dubbo:service timeout="3000" interface="com.concur.meta.client.service.DataSourceService" ref="dataSourceService"/>
```


### 2.内嵌模式
内嵌模式是在应用里面执行数据操作, 需要注意spring包冲突, beanID冲突等问题
1. 引入maven依赖:
```java
<dependency>
    <groupId>com.concur.meta</groupId>
    <artifactId>model-core</artifactId>
    <version>${meta-model.version}</version>
</dependency>
```

2. 配置数据源datasource bean(同上第2步)

3. 引入meta-model的spring配置
```
<import resource="classpath:lmodel-application.xml"/>
```

## 使用接口

1.添加模型注解
```
@LModel(table = "ddy_scene_push_task")
public class sampleDO extends BaseDO {
```
如果字段名和属性名不一致,通过注解映射字段名(驼峰形式):
```
@LColumn(name = "beginTime")
    private Date pushBeginTime;
```

2.查询列表

```
Query.create(LModelDO.class)
                .dataSource(1l)
                .execute().getList();
```

3.按条件查询

```
DB.Query.create(LModelDO.class)
            .dataSource(1l)
            .condition(CLASS_NAME, className)
            .execute().getOne();


```
4.通过example、分页查询
```
SampleDO sampleDO = new SampleDO();
sampleDO.setTaskName(taskName);

PageResult<T> result = Query.create(SampleDO.class).dataSource(1l).page(1).size(20).example(sampleDO).execute();

List<SampleDO> list = result.getList();// 当前页列表
int count = result.getTotalNum();// 总数
```

5.PostgresSql使用sql语句查询demo
```
String sql = " select * from  \"public\".\"lbs_analysis_user_location\" limit 5";

Map<String, Serializable> params = new HashMap<String, Serializable>();
params.put("userId", 3529L);
Object list1 = Query.create(SampleDO.class).dataSource(3L).listBySql(sql).params(params).execute().getList();
```

6.写入操作    
不使用事务:
```
MetaModelDO lmodel0 = new MetaModelDO();
lmodel0.setGmtCreate(new Date());
lmodel0.setGmtModified(new Date());
lmodel0.setModelCode("321313");
lmodel0.setModelName("test");
lmodel0.setModelType("1");
lmodel0.setStatus(0);

boolean isSucess = DB.Persist.create().update(lmodel0).isSuccess();//boolean isSUccess() 判断updateCount是否>=1
if (isSucess) {
    // do something
}

int count = DB.Persist.create().delete(lmodel0).getCount();//int getCount() 获取更新生效数量
if (count == 1) {
    // do something
}
```


使用事务:
```
// 开启事务
Transaction transaction = Transaction.start();
transaction.setDataConsistency(true);// 需要支持数据幂等性,设置后可捕获DataConsistencyException
try {
    .........
    
    lmodel0 = DB.Persist.create(MetaModelDO.class).insert(lmodel0).getResult();// insert会立即入库

    lmodel0.setStatus(1);
    // update不会立即执行
    // 开启事务之后,update和delete操作不会立即入库， 并且返回的updateCount==-1, 提交时如果updateCount==0则会抛出异常而执行catch块的rollback操作 来实现幂等性支持
    DB.Persist.create().update(lmodel0);

    DB.Persist.create().delete(lmodel0);

    // 提交到数据库执行更新和删除操作
    Transaction.commit(transaction);// 提交事务
} catch (ConsistencyException e) {// 幂等性异常,比如updateCount==0时抛出; 如果要求幂等性则调用回滚
    // Transaction.rollback(transaction);
} catch (TransactionFailException e1) {// 事务异常 (一般都会有脏数据)
    Transaction.rollback(transaction);// 回滚insert的数据(通过deletebyId)
} catch (RuntimeException e1) {// 运行时异常
    Transaction.rollback(transaction);// 调用回滚
}

// 或者单独捕获幂等性异常
try {
    ....
} catch (ConsistencyException e) {
    // 因为ConsistencyException继承了TransactionFailException, 单独捕获时,
    // 也可以捕获的抛出的TransactionFailException异常
    Transaction.rollback(transaction);
}

```

7.SQLID模式查询

```java
 Map<String, Serializable> params = new HashMap<String,Serializable>();
 List list = DB.Query.create(HashMap.class).listBySqlId(sqlId).params(params).execute().getList();

```

8.SQL KEY模式查询

```java
 Map<String, Serializable> params = new HashMap<String,Serializable>();
 List list = DB.Query.create(HashMap.class).listBySqlKey(sqlKey).params(params).execute().getList();

```

    
### 完整示例代码

```

	@Override
	public Long insert(SampleDO example) {
		Persist.create()
		.dataSource(lmodelDatasource)
		.insert(example);
		
		return example.getId();
	}

	@Override
	public Integer update(SampleDO example) {
		return Persist.create()
				.dataSource(lmodelDatasource)
				.update(example)
				.getCount();
	}

	@Override
	public SampleDO findByPrimaryKey(Long id) {
		return Query.create(SampleDO.class)
				.dataSource(lmodelDatasource)
				.get(id)
				.execute()
				.getOne();
	}

	@Override
	public List<SampleDO> listByExample(SampleDO example) {
		return Query.create(SampleDO.class)
				.dataSource(lmodelDatasource)
				.example(example)
				.execute()
				.getList();
	}

	@Override
	public List<SampleDO> pageByExample(SampleDO example, PageInfo page) {
		
		return Query.create(SampleDO.class)
				.dataSource(lmodelDatasource)
				.example(example)
				.rowStart(page.getPageStart())
				.size(page.getPageSize())
				.execute()
				.getList();
	}

	@Override
	public Integer deleteByPrimaryKey(Long id) {
		
		return Persist.create()
				.dataSource(lmodelDatasource)
				.deleteById(id)
				.getCount();
	}

	@Override
	public Long countByExample(SampleDO example) {
		Integer num = Query.create(SampleDO.class)
				.dataSource(lmodelDatasource)
				.count("*")
				.example(example)
				.execute()
				.getResult();
		
		return new Long(num);
	}


```


