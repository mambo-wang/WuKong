package com.wukong.service.leetcode.dp;

/**
 * 动态规划相关算法题
 */
public class DPAlgo {

    public static void main(String[] args) {

        DPAlgo algo = new DPAlgo();

        /**************************************斐波那契数列************************************************/
//        System.out.println(algo.fib(5));
        /****************************************零钱兑换********************************************************/
        System.out.println(algo.coinChange(new int[]{1,2,5}, 11));
    }

    /**
     * 面试题10- I. 509斐波那契数列
     * 题目： https://leetcode-cn.com/problems/fibonacci-number/
     * 题解：https://labuladong.gitbook.io/algo/di-ling-zhang-bi-du-xi-lie/dong-tai-gui-hua-xiang-jie-jin-jie
     * 思路：找出状态转移方程：dp[i] = dp[i - 1] + dp[i - 2]
     * 优化：当前状态只和之前的两个状态有关，其实并不需要那么长的一个 DP table 来存储所有的状态
     *
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

    /**
     * leetCode，322 零钱兑换
     * 给定不同面额的硬币 coins 和一个总金额 amount。编写一个函数来计算可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1。
     * 链接：https://leetcode-cn.com/problems/coin-change
     *
     * 解题思路：动态规划
     * 两个小技巧：
     * 预设一个0位方便后续计算，组成0的最少硬币数是0，所以dp[0] = 0
     * 给每一个数预设一个最小值amount+1，因为硬币面额最小为整数1，所以只要有解，最小硬币数必然小于amount+1
     * 作者：pendygg
     * 链接：https://leetcode-cn.com/problems/coin-change/solution/zi-di-xiang-shang-dong-tai-gui-hua-by-pendygg/
     */
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        for (int i = 0; i < amount + 1; i++) {
            dp[i] = i == 0 ? 0 : amount + 1;
            for (int coin : coins) {
                if (i - coin >= 0) {
                    dp[i] = Math.min(dp[i - coin] + 1, dp[i]);
                }
            }
        }
        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

}
