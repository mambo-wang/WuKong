package com.wukong.common.contants;

public interface Constant {

    interface RedisKey{
        //商品缓存
        String KEY_GOODS = "wukong_goods";
        //库存缓存
        String KEY_STOCK = "wukong_stock";
        //销量缓存
        String KEY_SALES = "wukong_sales";

        String KEY_USER = "wukong_user";
        String KEY_USER_USERNAME = "wukong_user_username";

        String KEY_TOKEN = "wukong_token";
        /**
         * 一定时间内用户访问次数
         */
        String KEY_ACCESS = "wukong_access";
        //秒杀结果
        String KEY_KILL_RESULT = "wukong_result";

        String KEY_RESULT_KEY = "orderId:%s";

        String KEY_IDEMPOTENT = "IDEMPOTENT";
    }

    interface Order{
        /**
         * 0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成
         */
        int STAT_NOT_PAY = 0;
        int STAT_PAY = 1;
        int STAT_OUT = 2;
        int STAT_RECEIVED = 3;
        int STAT_BACK = 4;
        int STAT_DONE = 5;
        int STAT_PAY_FAIL = 6;
        int STAT_TIMEOUT = 7;
    }

    interface SecKill{
        String  processing = "processing";
        String  success = "success";
        String  fail = "fail";
    }

    interface RabbitMQ{
        String queueAddScore = "addScore";
    }
}
