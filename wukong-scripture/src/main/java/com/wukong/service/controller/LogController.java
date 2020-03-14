package com.wukong.service.controller;

import com.wukong.common.annotations.DateValidator;
import com.wukong.common.autoconfig.ftp.FtpService;
import com.wukong.common.model.BaseResult;
import com.wukong.service.repository.LogRepository;
import com.wukong.service.repository.entity.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
@Validated
public class LogController {

    @Autowired
    private FtpService ftpService;

    @Autowired
    private LogRepository logRepository;

    @PostMapping
    public BaseResult queryLog(@RequestBody OperationLog operationLog){
        return BaseResult.success(logRepository.findLogs(operationLog));
    }

    @DeleteMapping
    public BaseResult removeLog(@DateValidator(dateFormat = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "time")String time, @RequestParam(name = "per")boolean persistent){
        long result = logRepository.deleteLogBeforeTime(time, persistent);
        return BaseResult.success(result);
    }

}
