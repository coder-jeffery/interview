package com.easy.interviewweb.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public PoolingHttpClientConnectionManager connectionManager(){
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(200);
        manager.setDefaultMaxPerRoute(50);
        return manager;
    }

    @Bean
    public RequestConfig requestConfig(){
        return RequestConfig.custom()
                .setConnectionKeepAlive(TimeValue.ofMinutes(50000))
                .setConnectionRequestTimeout(Timeout.ofMinutes(5000))
                .build();
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager connectionManager, RequestConfig requestConfig){
        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictIdleConnections( TimeValue.ofMinutes(30))
                .build();
    }

    @Bean
    public RestTemplate restTemplate(CloseableHttpClient client){
        HttpComponentsClientHttpRequestFactory factory  = new HttpComponentsClientHttpRequestFactory(client);
        return new RestTemplate(factory);
    }
}
