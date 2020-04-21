package com.wukong.provider.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderVO implements Serializable {
	private static final long serialVersionUID = -3065073570577687613L;
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