package com.wukong.provider.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderVO {
	private Long id;

	private Long userId;

	private Long goodsId;

	private String address;

	private String goodsName;

	private Integer goodsCount;

	private BigDecimal goodsPrice;

	private Integer status;

	private Date createDate;

	private Date payDate;
}