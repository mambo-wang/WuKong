package com.wukong.provider.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 6429236937319574367L;

    private String username;

    private String password;
}
