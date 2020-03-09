package com.wukong.service.repository.impl;

import com.wukong.common.model.OperationLogVO;
import com.wukong.service.repository.LogRepository;
import com.wukong.service.repository.entity.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by summer on 2017/5/5.
 */
@Component
@Slf4j
public class LogRepositoryImpl implements LogRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 创建对象
     * @param operationLog
     */
    @Override
    public void saveLog(OperationLog operationLog) {
        mongoTemplate.save(operationLog);
        log.info("success save a log {}", operationLog);
    }

    @Override
    public List<OperationLog> findLogs(OperationLog operationLog) {
        Query query=new Query(Criteria.where("deleted").is("n"));
        if(StringUtils.isNotEmpty(operationLog.getDesc())){
            query.addCriteria(Criteria.where("desc").is(operationLog.getDesc()));
        } else if(StringUtils.isNotEmpty(operationLog.getResult())){
            query.addCriteria(Criteria.where("result").is(operationLog.getResult()));
        } else if(StringUtils.isNotEmpty(operationLog.getModule())){
            query.addCriteria(Criteria.where("module").is(operationLog.getModule()));
        } else if(StringUtils.isNotEmpty(operationLog.getTime())){
            query.addCriteria(Criteria.where("time").gt(operationLog.getTime()));
        }
        List<OperationLog> operationLogs =  mongoTemplate.find(query , OperationLog.class);
        return operationLogs;
    }

    @Override
    public long deleteLogBeforeTime(String time, boolean persistent) {
        Query query=new Query(Criteria.where("time").lt(time));
        if(persistent){
            return mongoTemplate.remove(query, OperationLogVO.class).getDeletedCount();
        } else {
            Update update= new Update().set("deleted", "y");
            //更新查询返回结果集的第一条
//            UpdateResult result =mongoTemplate.updateFirst(query,update,OperationLogVO.class);
            //更新查询返回结果集的所有
             return mongoTemplate.updateMulti(query,update, OperationLogVO.class).getMatchedCount();
        }
    }

}
