package com.wukong.service.repository.entity;

import com.alibaba.fastjson.JSONObject;
import com.wukong.common.utils.DateTimeTool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationLog {

    private String module;

    private String desc;

    private String result;

    private String msg;

    private String time;

    private String operator;

    private String deleted;
}
