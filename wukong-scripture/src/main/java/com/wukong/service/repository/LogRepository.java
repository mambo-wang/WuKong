package com.wukong.service.repository;

import com.wukong.common.model.OperationLogVO;
import com.wukong.service.repository.entity.OperationLog;

import java.util.List;

/**
 * Created by wangbao
 */
public interface LogRepository {

    void saveLog(OperationLog log);

    List<OperationLog> findLogs(OperationLog log);

    long deleteLogBeforeTime(String time, boolean persistent);

}
