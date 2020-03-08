package com.wukong.service.controller;

import com.wukong.common.model.BaseResult;
import com.wukong.common.model.OperationLog;
import com.wukong.service.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @PostMapping
    public BaseResult queryLog(@RequestBody OperationLog operationLog){
        return BaseResult.success(logRepository.findLogs(operationLog));
    }

    @DeleteMapping
    public BaseResult removeLog(@RequestParam(name = "time")String time, @RequestParam(name = "per")boolean persistent){
        long result = logRepository.deleteLogBeforeTime(time, persistent);
        return BaseResult.success(result);
    }

}
