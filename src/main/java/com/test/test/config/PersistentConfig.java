package com.test.test.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

public class PersistentConfig {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @Qualifier("oracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource-oracle")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Qualifier("mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource-mysql")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    public SqlSessionFactory oracleSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(oracleDataSource());
        factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis/mybatis-config.xml"));
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resource = resolver.getResources("mybatis/oracle/**/*.xml");
        factoryBean.setMapperLocations(resource);

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionFactory mysqlSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(mysqlDataSource());
        factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis/mybatis-config.xml"));
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resource = resolver.getResources("mybatis/mysql/**/*.xml");
        factoryBean.setMapperLocations(resource);

        return factoryBean.getObject();
    }

    // @Bean
    // @Primary
    // public CommonDao commonDao() throws Exception {
    // CommonDao commonDao = new CommonDao();
    // commonDao.setSqlSessionFactory(oracleSqlSessionFactory());
    // return commonDao;
    // }

    // @Bean
    // @Qualifier("mysqlCommonDao")
    // public CommonDao mysqlCommonDao() throws Exception {
    // CommonDao commonDao = new CommonDao();
    // commonDao.setSqlSessionFactory(mysqlSqlSessionFactory());
    // return commonDao;
    // }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(oracleDataSource());
    }

    @Bean
    public PlatformTransactionManager mysqlTransactionManager() {
        return new DataSourceTransactionManager(mysqlDataSource());
    }
}
