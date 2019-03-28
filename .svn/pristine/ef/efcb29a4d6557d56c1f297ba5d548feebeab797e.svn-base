package com.yks.bigdata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2017/6/29.
 */
@Configuration
@PropertySource(value = "classpath:config/db.properties")
public class DataSourceConfiguration {

    private final static Logger LOG = LoggerFactory.getLogger(DataSourceConfiguration.class);

    /**
     * 数据源类型
     */
    private Class<? extends DataSource> type;

    /**
     * 创建主数据源
     * @return DataSource
     */
    @Bean(name = "masterDataSource")
    @Primary
    @ConfigurationProperties(prefix = "db.master")
    public DataSource masterDataSource() {
        LOG.info("-------------------- masterDataSource init ---------------------");
        return DataSourceBuilder.create().type(type).build();
    }

    /**
     * 创建老erp据源
     * @return DataSource
     */
    @Bean(name = "erpDataSource")
    @ConfigurationProperties(prefix = "db.erp")
    public DataSource erpDataSource() {
        LOG.info("-------------------- erpDataSource init ---------------------");
        return DataSourceBuilder.create().type(type).build();
    }

    /**
     * 创建老bi据源
     * 主要是为了获取账号对应的平台
     * @return DataSource
     */
    @Bean(name = "biDataSource")
    @ConfigurationProperties(prefix = "db.bi")
    public DataSource biDataSource() {
        LOG.info("-------------------- biDataSource init ---------------------");
        return DataSourceBuilder.create().type(type).build();
    }

    public Class<? extends DataSource> getType() {
        return type;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer createPropertySourcesPlaceholderConfigurer() {
        ClassPathResource resource = new ClassPathResource("config/db.properties");
        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(resource);
        return propertyPlaceholderConfigurer;
    }

}
