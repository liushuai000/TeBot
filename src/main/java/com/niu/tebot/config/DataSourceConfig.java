package com.niu.tebot.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
//@MapperScan(basePackages = "com.chuangyao.niu.mapper")
public class DataSourceConfig {



    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //注册乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }


//    @Autowired
//    private  DataSource dataSource ;
//    @Bean(name = "entityManagerFactoryPrimary")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
//        return builder.dataSource(dataSource)
//                .packages("com.chuangyao.niu.entity")
//                .persistenceUnit("primaryPersistenceUnit")
//                .build();
//    }


//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, MybatisPlusInterceptor interceptor) throws Exception {
//        MybatisSqlSessionFactoryBean ssfb = new MybatisSqlSessionFactoryBean();
//        ssfb.setDataSource(dataSource);
//        ssfb.setPlugins(interceptor);
//        //到哪里找xml文件
//        ssfb.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath:/mapper/*Mapper.xml"));
//        return ssfb.getObject();
//    }
}
