package com.wukong.consumer.repository.entity;

import com.wukong.common.utils.DateTimeTool;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_goods")
public class Goods {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "title")
    private String title;
    @Column(name = "image")
    private String image;
    @Column(name = "detail")
    private String detail;
    @Column(name = "price")
    private Double price;
    @Column(name = "stock")
    private Integer stock;

    @CreationTimestamp
    @javax.persistence.Column(name = "create_time", updatable = false)
    @DateTimeFormat(pattern = DateTimeTool.DATE_FORMAT_DEFAULT_VIEW)
    private Date createTime;

    @UpdateTimestamp
    @javax.persistence.Column(name = "update_time", insertable = false)
    @DateTimeFormat(pattern = DateTimeTool.DATE_FORMAT_DEFAULT_VIEW)
    private Date updateTime;

    @javax.persistence.Column(name = "deleted")
    private String deleted;
}
