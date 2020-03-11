package com.wukong.provider.controller.vo;

import com.wukong.provider.dto.UserImportDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 王宝
 * @description 用户导入返回类
 * @date 20200312
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserImportVO {

    private boolean result;

    private List<UserImportDTO> users;
}
