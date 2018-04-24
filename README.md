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

3. 配置server端RPC服务, 可使用HSF或dubbo等分布式框架
```java
    <!-- 元数据读服务HSF -->
    <bean class="com.taobao.hsf.app.spring.util.HSFSpringProviderBean"
          init-method="init">
        <property name="serviceInterface"
                  value="com.taobao.lmodel.client.service.server.MetaDataReadServerService"/>
        <property name="serviceVersion" value="${lmodel.hsf.provider.version}"/>
        <property name="target" ref="metaDataReadServerService"/>
        <property name="clientTimeout" value="5000"/>
    </bean>

    <!-- 元数据写服务HSF -->
    <bean class="com.taobao.hsf.app.spring.util.HSFSpringProviderBean"
          init-method="init">
        <property name="serviceInterface"
                  value="com.taobao.lmodel.client.service.server.MetaDataWriteServerService"/>
        <property name="serviceVersion" value="${lmodel.hsf.provider.version}"/>
        <property name="target" ref="metaDataWriteServerService"/>
        <property name="clientTimeout" value="15000"/>
    </bean>

    <!-- 元数据数据源服务HSF -->
    <bean class="com.taobao.hsf.app.spring.util.HSFSpringProviderBean"
          init-method="init">
        <property name="serviceInterface"
                  value="com.taobao.lmodel.client.service.DataSourceService"/>
        <property name="serviceVersion" value="${lmodel.hsf.provider.version}"/>
        <property name="target" ref="metaDataSourceService"/>
        <property name="clientTimeout" value="5000"/>
    </bean>
```

4. 配置client端的rpc服务
```java
<bean id="serviceFactory" class="com.concur.meta.client.service.ServiceFactory">
        <property name="metaDataReadServerService" ref="metaDataReadServerService"/>
        <property name="metaDataWriteServerService" ref="metaDataWriteServerService"/>
        <property name="dataSourceService" ref="metaDataSourceService"/>
</bean>
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

2. 配置数据源(同上)




