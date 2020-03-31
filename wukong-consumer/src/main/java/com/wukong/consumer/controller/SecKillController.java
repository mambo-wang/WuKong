package com.wukong.consumer.controller;

import com.wukong.common.annotations.AccessLimit;
import com.wukong.common.model.BaseResult;
import com.wukong.common.model.GoodsVO;
import com.wukong.common.utils.ExcelTool;
import com.wukong.consumer.service.GoodsService;
import com.wukong.consumer.service.SecKillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Api(value = "秒杀接口")
@RestController
@RequestMapping("/secKill")
public class SecKillController {

    @Autowired
    private SecKillService secKillService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 秒杀接口
     * @param goodsId 商品id
     * @param username 用户名
     * @return 结果
     */
    @ApiOperation(value = "秒杀商品")
    @AccessLimit(seconds = 60, maxCount = 5, needLogin = false)
    @GetMapping()
    public BaseResult secKill(@RequestParam(name = "goodsId")Long goodsId, @RequestParam(name = "username")String username){
        secKillService.secKill(goodsId, username);
        return BaseResult.success(null);
    }

    @ApiOperation(value = "查询秒杀结果")
    @GetMapping("/result")
    public BaseResult result(@RequestParam(name = "goodsId")Long goodsId, @RequestParam(name = "username")String username){
        String result = secKillService.querySecKillResult(goodsId, username);
        return BaseResult.success(result);
    }

    @ApiOperation(value = "导出商品清单")
    @GetMapping("/export")
    public BaseResult exportGoods(HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException, IllegalAccessException, IOException {

        List<GoodsVO> goodsVOS = goodsService.queryAll();

        Workbook workbook = ExcelTool.createSelectedWorkbookWithCommonHeader(goodsVOS,
                Arrays.asList("name","title","detail", "price","stock","image"),
                Arrays.asList("商品名称","标题","介绍","价格（元）","库存","图片"),true);

        ExcelTool.downloadBrowser(workbook, response, "商品清单.xls",request);
        return BaseResult.success(null);
    }
}
