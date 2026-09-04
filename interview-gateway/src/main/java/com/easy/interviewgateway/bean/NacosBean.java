package com.easy.interviewgateway.bean;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Getter
public class NacosBean {

    /**
     * 不加`@RefreshScope`，`@Value`不会热更新。
     * */
    @Value("${app.title:标题内容}")
    private String title;

    @Value("${app.timeout:300}")
    private Long timeout;
}
