package com.wukong.service.algo.dp;

/**
 * 动态规划相关算法题
 */
public class DPAlgo {

    public static void main(String[] args) {

        DPAlgo algo = new DPAlgo();
        System.out.println(algo.fib(5));
    }

    /**
     * 面试题10- I. 509斐波那契数列
     * 题目： https://leetcode-cn.com/problems/fibonacci-number/
     * 题解：https://labuladong.gitbook.io/algo/di-ling-zhang-bi-du-xi-lie/dong-tai-gui-hua-xiang-jie-jin-jie
     * 思路：找出状态转移方程：dp[i] = dp[i - 1] + dp[i - 2]
     * 优化：当前状态只和之前的两个状态有关，其实并不需要那么长的一个 DP table 来存储所有的状态
     * @param n
     * @return
     */
    int fib(int n) {
        if (n == 2 || n == 1)
            return 1;
        int prev = 1, curr = 1;
        for (int i = 3; i <= n; i++) {
            int sum = prev + curr;
            prev = curr;
            curr = sum;
        }
        return curr;
    }
}
