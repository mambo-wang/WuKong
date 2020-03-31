package com.wukong.consumer.service;

public interface SecKillService {


    void secKill(Long goodsId, String username);

    String querySecKillResult(Long goodsId, String username);
}
