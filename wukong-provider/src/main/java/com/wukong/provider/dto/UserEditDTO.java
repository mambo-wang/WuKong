package com.wukong.provider.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserEditDTO implements Serializable {

    private static final long serialVersionUID = 2091040200940795764L;
    private Long id;

    private String name;

    private String username;

    private String password;
}
