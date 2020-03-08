package com.wukong.service.service;

public interface LogService {

    void addSuccessLog(String module, String desc, String msg, String operator);

    void addFailureLog(String module, String desc, String msg, String operator);
}
