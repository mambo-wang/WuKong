package com.wukong.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = -3397342065917320688L;
    private Long id;

    private String name;

    private String username;

    private String address;

    private String phoneNumber;

    private String email;

    private Integer score;
}
