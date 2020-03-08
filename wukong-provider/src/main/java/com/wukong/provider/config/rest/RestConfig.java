package com.wukong.provider.config.rest;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author w14014
 * @date 2018/9/18
 */
@Data
@Component
@PropertySource(value = "classpath:conf/rest.properties", ignoreResourceNotFound = true)
public class RestConfig {

    @Value("${rest.protocol}")
    private String protocol;

    @Value("${rest.host}")
    private String host;

    @Value("${rest.port}")
    private Integer port;

    public String accessUrl(String uri) {
        String baseUrl = this.protocol + "://" + this.host + ":" + port;
        if(StringUtils.isNotEmpty(uri) && uri.startsWith("/")){
            uri = uri.substring(1);
        }
        return baseUrl + "/" + uri;
    }
}
