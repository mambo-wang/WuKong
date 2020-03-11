package com.wukong.provider.service;

import com.wukong.common.model.AddScoreDTO;
import com.wukong.common.model.UserVO;
import com.wukong.provider.controller.vo.LoginVO;
import com.wukong.provider.controller.vo.UserImportVO;
import com.wukong.provider.dto.UserEditDTO;
import com.wukong.provider.dto.UserImportDTO;
import com.wukong.provider.entity.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserService {

    List<UserVO> queryAll();

    UserVO findById(Long id);

    UserVO findByUsername(String username);

    UserVO addUser(UserEditDTO userEditDTO);

    void modifyUser(UserEditDTO userEditDTO);

    void removeUser(List<Long> ids);

    String login(HttpServletResponse response, LoginVO loginVo);

    UserVO getByToken(HttpServletResponse response, String token);

    void addScore(AddScoreDTO addScoreDTO);

    Workbook createExcelTemplate();

    UserImportVO uploadExcel(MultipartFile file) throws IOException, InstantiationException, IllegalAccessException;
}
