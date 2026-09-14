//package com.easy.interviewweb.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//
//@Configuration
//// 仅当配置 mysql.enabled = true 才实例化这个配置类
//@ConditionalOnProperty(prefix = "mysql", name = "enabled", havingValue = "false", matchIfMissing = false)
//public class MysqlDataSourceConfig {
//
//    // 读取 mysql.datasource 下面所有配置
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "mysql.datasource")
//    public DataSource mysqlDataSource() {
//        return new HikariDataSource();
//    }
//}