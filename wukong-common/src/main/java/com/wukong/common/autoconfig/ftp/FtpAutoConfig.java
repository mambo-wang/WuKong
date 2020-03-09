package com.wukong.common.autoconfig.ftp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({FtpProperties.class})
@ConditionalOnProperty(prefix = "ftp", name = "enabled", havingValue = "true", matchIfMissing = false)
public class FtpAutoConfig {

    @Autowired
    private FtpProperties ftpProperties;


    @Bean
    public FtpService ftpService(){
        return new FtpService(
                ftpProperties.getHost(),
                ftpProperties.getPort(),
                ftpProperties.getUsername(),
                ftpProperties.getPassword()
        );
    }



}
