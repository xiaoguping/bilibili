package com.imooc.bilibili.service.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * 作者：xgp
 * 时间：2024/8/16
 * 描述：
 */

@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    @Value("${elasticsearch.url}")
    private String esUrl;


    @Override
    @Bean
    //生成RestHighLevelClient，方便对es的功能调用
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(esUrl)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
