package com.wukong.consumer.controller;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsVO implements Serializable {

    private Long id;
    private String name;
    private String title;
    private String image;
    private String detail;
    private Double price;
    private Integer stock;
}
