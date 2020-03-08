package com.wukong.common.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OperationLog implements Serializable {

    public static final String MODULE_CONSUMER = "WuKong-Consumer";
    public static final String MODULE_PROVIDER = "WuKong-Provider";
    public static final String RESULT_SUCCESS = "SUCCESS";
    public static final String RESULT_FAILURE = "FAILURE";

    private static final long serialVersionUID = 3744662747742736932L;

    private String module;

    private String desc;

    private String result;

    private String msg;

    private String time;

    private String operator;

    private String deleted;
}
