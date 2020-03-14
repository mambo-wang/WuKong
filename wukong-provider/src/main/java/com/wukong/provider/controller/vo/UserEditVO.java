package com.wukong.provider.controller.vo;

import com.wukong.common.autoconfig.validator.GroupModify;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.io.Serializable;

@Data
public class UserEditVO implements Serializable {

    private static final long serialVersionUID = 2091040200940795764L;

    @NotNull(message = "修改用户时id不能为空",groups = GroupModify.class)
    private Long id;

    @Length(min = 1, max = 10, message = "姓名长度在[1,10]之间",groups={Default.class,GroupModify.class})
    private String name;

    @Length(min = 1, max = 10, message = "登录名长度在[1,10]之间",groups={Default.class,GroupModify.class})
    private String username;

    @NotNull
    @Length(min = 5, max = 17, message = "密码长度在[5,17]之间",groups={Default.class,GroupModify.class})
    private String password;

    @NotBlank(message = "地址不能为空。",groups={Default.class,GroupModify.class})
    private String address;

    @Pattern(regexp="^[1][3,4,5,7,8][0-9]{9}$", message = "手机号格式不正确。",groups={Default.class,GroupModify.class})  //被注释的元素必须符合指定的正则表达式
    private String phoneNumber;

    @Email(message = "电子邮箱格式不正确。",groups={Default.class,GroupModify.class})
    private String email;
}
