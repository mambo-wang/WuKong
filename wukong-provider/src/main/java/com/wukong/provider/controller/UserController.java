package com.wukong.provider.controller;

import com.wukong.common.model.BaseResult;
import com.wukong.common.model.UserVO;
import com.wukong.common.annotations.AccessLimit;
import com.wukong.common.utils.ExcelTool;
import com.wukong.common.autoconfig.validator.GroupModify;
import com.wukong.provider.controller.vo.LoginVO;
import com.wukong.provider.controller.vo.UserImportVO;
import com.wukong.provider.controller.vo.UserEditVO;
import com.wukong.provider.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Api(value = "用戶控制器")
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public BaseResult<List<UserVO>> queryAll(){
        return BaseResult.success(userService.queryAll());
    }

    @AccessLimit(seconds = 60, maxCount = 5, needLogin = false)
    @GetMapping("/{id}")
    public  BaseResult<UserVO> findById(@PathVariable(name = "id") Long id){
        return BaseResult.success(userService.findById(id));
    }

    @ApiOperation(value = "根据用户名查找")
    @GetMapping("/byUsername")
    public BaseResult<UserVO> findByUsername(@ApiParam(value = "用户名") @Length(min = 1, max = 10, message = "username长度1-10")@RequestParam(name = "username") String username){
        return BaseResult.success(userService.findByUsername(username));
    }

    @PostMapping
    public BaseResult addUser(@RequestBody @Valid UserEditVO userEditVO){
        userService.addUser(userEditVO);
        return BaseResult.success(null);
    }

    @PutMapping
    public BaseResult modifyUser(@RequestBody @Validated({GroupModify.class}) UserEditVO userEditVO){
        userService.modifyUser(userEditVO);
        return BaseResult.success(null);
    }

    @GetMapping("/delete")
    public BaseResult removeUser(@RequestParam(name = "ids") List<Long> ids){
        userService.removeUser(ids);
        return BaseResult.success(null);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public BaseResult login(@RequestBody LoginVO loginVO, HttpServletResponse response){
        String token = userService.login(response, loginVO);
        return BaseResult.success(token);
    }

    @ApiOperation(value = "导出Excel")
    @GetMapping("/export")
    public BaseResult exportGoods(HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException, IllegalAccessException, IOException {

        List<UserVO> userVOS = userService.queryAll();

        Workbook workbook = ExcelTool.createSelectedWorkbookWithCommonHeader(userVOS,
                Arrays.asList("name","username","address", "phoneNumber","email","score"),
                Arrays.asList("姓名","用户名","收货地址","手机号","邮箱地址","积分"),false);

        ExcelTool.downloadBrowser(workbook, response, "用户.xls",request);
        return BaseResult.success(null);
    }

    @ApiOperation(value = "下载excel导入模板")
    @ApiImplicitParam(name = "X-CSRF-TOKEN", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/downloadTemplate")
    public BaseResult downloadTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Workbook workbook = userService.createExcelTemplate();
        ExcelTool.downloadBrowser(workbook, response, "user_template.xls", request);
        return BaseResult.success(null);
    }

    @ApiOperation(value = "导入单个excel文件")
    @ApiImplicitParam(name = "X-CSRF-TOKEN", value = "token", dataType = "String", paramType = "header")
    @PostMapping(value = "/uploadFile",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResult uploadFile(@RequestPart("file") MultipartFile file) throws IOException, IllegalAccessException, InstantiationException, InterruptedException {
        UserImportVO userImportVO = userService.uploadExcel(file);
        return BaseResult.success(userImportVO);
    }
}
