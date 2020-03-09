package com.wukong.common.autoconfig.ftp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties(prefix = "ftp")
@PropertySource(value = {"classpath:application.properties"},
        ignoreResourceNotFound = true, encoding = "UTF-8", name = "application.properties")
public class FtpProperties {

    private String host;

    private int port;

    private String username;

    private String password;
}
