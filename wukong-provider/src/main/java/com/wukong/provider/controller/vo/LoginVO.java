package com.wukong.provider.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "登录参数")
@Data
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 6429236937319574367L;

    @ApiModelProperty(name = "用户名")
    private String username;

    @ApiModelProperty(name = "密码")
    private String password;
}
