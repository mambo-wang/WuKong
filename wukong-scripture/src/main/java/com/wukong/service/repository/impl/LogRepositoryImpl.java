package com.wukong.service.repository.impl;

import com.wukong.common.model.OperationLog;
import com.wukong.service.repository.LogRepository;
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
public class LogRepositoryImpl implements LogRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 创建对象
     * @param log
     */
    @Override
    public void saveLog(OperationLog log) {
        mongoTemplate.save(log);
    }

    @Override
    public List<OperationLog> findLogs(OperationLog log) {
        Query query=new Query(Criteria.where("deleted").is("n"));
        if(StringUtils.isNotEmpty(log.getDesc())){
            query.addCriteria(Criteria.where("desc").is(log.getDesc()));
        } else if(StringUtils.isNotEmpty(log.getResult())){
            query.addCriteria(Criteria.where("result").is(log.getResult()));
        } else if(StringUtils.isNotEmpty(log.getModule())){
            query.addCriteria(Criteria.where("module").is(log.getModule()));
        } else if(StringUtils.isNotEmpty(log.getTime())){
            query.addCriteria(Criteria.where("time").gt(log.getTime()));
        }
        List<OperationLog> operationLogs =  mongoTemplate.find(query , OperationLog.class);
        return operationLogs;
    }

    @Override
    public long deleteLogBeforeTime(String time, boolean persistent) {
        Query query=new Query(Criteria.where("time").lt(time));
        if(persistent){
            return mongoTemplate.remove(query, OperationLog.class).getDeletedCount();
        } else {
            Update update= new Update().set("deleted", "y");
            //更新查询返回结果集的第一条
//            UpdateResult result =mongoTemplate.updateFirst(query,update,OperationLog.class);
            //更新查询返回结果集的所有
             return mongoTemplate.updateMulti(query,update,OperationLog.class).getMatchedCount();
        }
    }

}
