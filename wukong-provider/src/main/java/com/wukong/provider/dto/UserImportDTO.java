package com.wukong.provider.dto;

import lombok.Data;

/**
 * @author 王宝
 * @description 用户导入解析类
 * @date 20200312
 */
@Data
public class UserImportDTO {

    private String name;

    private String username;

    private String address;

    private String phoneNumber;

    private String email;

}
