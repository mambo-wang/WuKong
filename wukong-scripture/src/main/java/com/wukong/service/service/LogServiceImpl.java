package com.wukong.service.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.wukong.common.dubbo.LogService;
import com.wukong.common.model.OperationLog;
import com.wukong.common.utils.DateTimeTool;
import com.wukong.service.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public void addSuccessLog(String module, String desc, String msg, String operator) {
        OperationLog log = OperationLog.builder()
                .deleted("n")
                .desc(desc)
                .module(module)
                .operator(operator)
                .result(OperationLog.RESULT_SUCCESS)
                .time(DateTimeTool.formatFullDateTime(System.currentTimeMillis()))
                .msg(msg)
                .build();
        logRepository.saveLog(log);

    }

    @Override
    public void addFailureLog(String module, String desc, String msg, String operator) {
        OperationLog log = OperationLog.builder()
                .deleted("n")
                .desc(desc)
                .module(module)
                .operator(operator)
                .result(OperationLog.RESULT_FAILURE)
                .time(DateTimeTool.formatFullDateTime(System.currentTimeMillis()))
                .msg(msg)
                .build();
        logRepository.saveLog(log);
    }
}
