package com.wukong.service.leetcode.daily;

public class Daily202004 {

    public static void main(String[] args) {

    }


    /**
     * leetCode每日一题 1111. 有效括号的嵌套深度
     * 题解：https://leetcode-cn.com/problems/maximum-nesting-depth-of-two-valid-parentheses-strings/solution/ti-mian-shuo-ming-ti-mu-jiang-jie-shuo-hao-fa-wan-/
     * 思路：两个子串均分，比如说奇数个括号给a串，偶数个括号给b串
     * @param seq
     * @return
     */
    public int[] maxDepthAfterSplit(String seq){

        int[] res = new int[seq.length()];

        int idx = 0;
        for (char c : seq.toCharArray()){
            if(c == '('){
                res[idx] = idx % 2==0 ? 0 : 1;
            } else {
                res[idx] = idx % 2 == 0 ? 1 : 0;
            }
            idx ++;
        }
        return res;
    }



}
