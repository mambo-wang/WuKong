package com.wukong.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private Long id;

    private String name;

    private String username;
}
