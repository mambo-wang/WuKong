package com.wukong.provider.service;

import com.wukong.common.model.UserVO;
import com.wukong.provider.controller.vo.LoginVO;
import com.wukong.provider.controller.vo.UserImportVO;
import com.wukong.provider.controller.vo.UserEditVO;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    List<UserVO> queryAll();

    UserVO findById(Long id);

    UserVO findByUsername(String username);

    UserVO addUser(UserEditVO userEditVO);

    void modifyUser(UserEditVO userEditVO);

    void removeUser(List<Long> ids);

    String login(HttpServletResponse response, LoginVO loginVo);

    UserVO getByToken(HttpServletResponse response, String token);

    void addScore(Long userId, Integer score);

    int reduceBalance(Long userId, BigDecimal price);

    Workbook createExcelTemplate();

    UserImportVO uploadExcel(MultipartFile file) throws IOException, InstantiationException, IllegalAccessException;

    boolean checkPwd(Long userId, String password);

    void testSeata(String username, String phoneNumber, Long goods);
}
