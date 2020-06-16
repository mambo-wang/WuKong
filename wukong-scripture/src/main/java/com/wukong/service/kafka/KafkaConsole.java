package com.wukong.service.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.requests.DescribeLogDirsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        newPartitions.put(topicName, NewPartitions.increaseTo(number));
        adminClient.createPartitions(newPartitions);
    }

    /**
     * 查询broker占用磁盘空间
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public long getSpace() throws ExecutionException, InterruptedException {

            DescribeLogDirsResult ret = adminClient.describeLogDirs(Collections.singletonList(1)); // 指定Broker id，在zookeeper中/brokers/ids路径下
            long size = 0L;
            for (Map<String, DescribeLogDirsResponse.LogDirInfo> logDirInfoMap : ret.all().get().values()) {
                size += logDirInfoMap.values().stream().map(logDirInfo -> logDirInfo.replicaInfos).flatMap(
                        topicPartitionReplicaInfoMap ->
                                topicPartitionReplicaInfoMap.values().stream().map(replicaInfo -> replicaInfo.size))
                        .mapToLong(Long::longValue).sum();
            }
            System.out.println(size);
            return size;
    }

    /**
     * 查询某个消费者组的位移
     *
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public Map<TopicPartition, OffsetAndMetadata> queryOffsets() throws InterruptedException, ExecutionException, TimeoutException {
        String groupID = "testGroup";
            ListConsumerGroupOffsetsResult result = adminClient.listConsumerGroupOffsets(groupID);
            Map<TopicPartition, OffsetAndMetadata> offsets =
                    result.partitionsToOffsetAndMetadata().get(10, TimeUnit.SECONDS);
            System.out.println(offsets);
            return offsets;
    }
}