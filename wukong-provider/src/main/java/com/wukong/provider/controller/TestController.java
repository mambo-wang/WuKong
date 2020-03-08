package com.wukong.provider.controller;

import com.wukong.common.model.BaseResult;
import com.wukong.provider.config.rest.RestConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private RestConnection restConnection;

    @GetMapping
    public BaseResult hello(){

        ResponseEntity<BaseResult> resultResponseEntity =  restConnection.get("/hello", ParameterizedTypeReference.forType(BaseResult.class));

        return resultResponseEntity.getBody();
    }
}
