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
//        System.out.println(algo.coinChange(new int[]{1, 2, 5}, 11));
        /*******************************************硬币************************************************************/
        System.out.println(algo.waysToChange(10));
    }

    /**
     * leetCode 0406 72. 编辑距离
     * https://leetcode-cn.com/problems/edit-distance/solution/jian-dan-dpmiao-dong-by-sweetiee/
     * 编辑距离
     * 状态定义：
     * dp[i][j]表示word1的前i个字母转换成word2的前j个字母所使用的最少操作。
     *
     * 状态转移：
     * i指向word1，j指向word2
     * 若当前字母相同，则dp[i][j] = dp[i - 1][j - 1];
     * 否则取增删替三个操作的最小值 + 1， 即:
     * dp[i][j] = min(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1]) + 1
     */
    public int minDistance(String word1, String word2) {
        int len1 = word1.length(), len2 = word2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }
        return dp[len1][len2];
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
     * <p>
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

    /**
     * leetCode每日一题  面试题 08.11. 硬币
     * 硬币。给定数量不限的硬币，币值为25分、10分、5分和1分，编写代码计算n分有几种表示法。(结果可能会很大，你需要将结果模上1000000007)
     * https://leetcode-cn.com/problems/coin-lcci/
     * @param n
     * @return
     */
    public int waysToChange(int n) {
        int[] coins = new int[]{25,10,5,1};
        int[][] dp = new int[5][n+1];
        for (int i = 1; i <=4; i ++){
            dp[i][0] = 1;
        }
        //动态规划方程dp[i][j] = dp[i-1][j] + dp[i][j-coins[i - 1]]
        for(int i = 1; i <=4;i++){

            for (int j = 1; j <= n; j ++ ){
                if(j < coins[i-1]){
                    dp[i][j] = dp[i-1][j]% 1000000007;
                }else {
                    dp[i][j] = (dp[i-1][j] + dp[i][j-coins[i-1]])% 1000000007;
                }
            }
        }
        return dp[4][n];

    }

}
