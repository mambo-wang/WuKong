package com.wukong.service.algo.daily;

import java.util.PriorityQueue;
import java.util.Queue;

public class Daily202003 {

    public static void main(String[] args) {

        int[] result = getLeastNumbers(new int[]{4,5,1,6,2,7,3,8}, 4);
        System.out.println(result);
    }

    /**
     * 3.20 leetcode每日一题
     * 题目：面试题40. 最小的k个数
     * 题目描述 输入整数数组 arr ，找出其中最小的 k 个数。例如，输入4、5、1、6、2、7、3、8这8个数字，则最小的4个数字是1、2、3、4。
     * 优先级队列知识 https://www.jianshu.com/p/f1fd9b82cb72
     * 题解 https://leetcode-cn.com/problems/zui-xiao-de-kge-shu-lcof/solution/3chong-jie-fa-miao-sha-topkkuai-pai-dui-er-cha-sou/
     *
     * 解题思路：// 保持堆的大小为K，然后遍历数组中的数字，遍历的时候做如下判断：
     * // 1. 若目前堆的大小小于K，将当前数字放入堆中。
     * // 2. 否则判断当前数字与大根堆堆顶元素的大小关系，如果当前数字比大根堆堆顶还大，这个数就直接跳过；
     * //    反之如果当前数字比大根堆堆顶小，先poll掉堆顶，再将该数字放入堆中。
     * @return
     */
    public static int[] getLeastNumbers(int[] arr, int k){

        if(k == 0 || arr.length == 0){
            return new int[0];
        }

        //创建大根堆（默认是小根堆，需要重写比较器）
        Queue<Integer> pq = new PriorityQueue<>((v1,v2) -> v2 - v1);
        for(int num : arr){
            if(pq.size() < k){
                pq.offer(num);
            } else if(num < pq.peek()){
                pq.poll();
                pq.offer(num);
            }
        }

        int[] res = new int[pq.size()];
        int idx = 0;
        for(int num : pq){
            res[idx ++] = num;
        }
        return res;
    }


}
