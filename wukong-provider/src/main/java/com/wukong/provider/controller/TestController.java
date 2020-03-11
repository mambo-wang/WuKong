package com.wukong.provider.controller;

import com.wukong.common.model.BaseResult;
import com.wukong.provider.config.rest.RestConnection;
import com.wukong.provider.service.MailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "测试控制器")
@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private RestConnection restConnection;

    @Autowired
    private MailService mailService;

    @GetMapping
    public BaseResult hello(){

        ResponseEntity<BaseResult> resultResponseEntity =  restConnection.get("/logs", ParameterizedTypeReference.forType(BaseResult.class));

        return resultResponseEntity.getBody();
    }

    /**
     * sendAttachmentsMail
     */
    @PostMapping
    public  BaseResult mail(){

        mailService.sendSimpleMail("mambo1991@163.com", "测试", "测试事实上司是");
        return BaseResult.success(null);
    }
}
