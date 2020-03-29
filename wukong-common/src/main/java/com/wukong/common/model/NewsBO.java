package com.wukong.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class NewsBO implements Serializable {

    private String url;

    private String title;
}
