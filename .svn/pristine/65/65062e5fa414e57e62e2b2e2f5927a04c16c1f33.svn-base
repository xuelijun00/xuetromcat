/*
package com.yks.bigdata.config;

import com.google.common.net.InetAddresses;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.annotation.Resource;

*/
/**
 * Created by Administrator on 2017/6/29.
 *//*

@Configuration
@PropertySource(value = "classpath:config/elasticsearch.properties")
public class ElasticsearchConfiguration {

    @Resource
    private Environment environment;

    @Bean
    public Client getClient() {
        //Settings settings = Settings.builder().put("cluster.name","elasticsearch").build();
        TransportClient transportClient = TransportClient.builder().build();
        TransportAddress address = new InetSocketTransportAddress(
                InetAddresses.forString(environment.getProperty("spring.data.elasticsearch.properties.host"))
                ,Integer.parseInt(environment.getProperty("spring.data.elasticsearch.properties.port"))
        );
        transportClient.addTransportAddress(address);
        return transportClient;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(getClient());
    }
}*/
