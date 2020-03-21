package com.wukong.service.kafka;
 
 
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * @ClassName ConsoleApi
 * @Author wangbao
 * @Describe 主题操作控制类
 * @Date 2019/5/31 0031 9:32
 */
@Service
public class KafkaConsole {
    @Autowired
    private AdminClient adminClient;
 
    /**
     * 返回主题的信息
     * @param topicName 主题名称
     * @return
     */
    public Map<String, TopicDescription> SelectTopicInfo(String topicName) throws ExecutionException, InterruptedException {
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList(topicName));
        Map<String, TopicDescription> all = result.all().get();
        return all;
    }
 
 
    /**
     * 增加某个主题的分区（注意分区只能增加不能减少）
     * @param topicName  主题名称
     * @param number  修改数量
     */
    public void increaseTopicPartitions(String topicName,Integer number){
        Map<String, NewPartitions> newPartitions=new HashMap<String, NewPartitions>();
        //创建新的分区的结果
        newPartitions.put(topicName,NewPartitions.increaseTo(number));
        adminClient.createPartitions(newPartitions);
    }
 
 
 
 
 
 
}