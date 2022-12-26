package com.test.test.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(value = "com.test.test.test1.repository", sqlSessionFactoryRef = "factory")
public class OneDataSourceConfig {

    @Primary
    @Bean(name = "datasource")
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "factory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage("com.test.test.test1");
        sqlSessionFactory
                .setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper1/*.xml"));
        return sqlSessionFactory.getObject();
    }

    @Primary
    @Bean(name = "sqlSession")
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
