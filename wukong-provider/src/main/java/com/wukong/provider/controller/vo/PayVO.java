package com.wukong.provider.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayVO implements Serializable {

    private Long orderId;

    private String password;
}
