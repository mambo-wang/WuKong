package com.wukong.service.leetcode.daily;

public class
Daily202004 {

    public static void main(String[] args) {

        Daily202004 daily202004 = new Daily202004();
        System.out.println(daily202004.myAtoI("   -22342iutreit"));
    }

    /**
     * leetcode每日一题 4月3日
     * 8 字符串转换整数
     *
     * @return
     */
    public int myAtoI(String str){
        char[] ca = str.toCharArray();
        int n  = ca.length;
        int idx = 0;

        while (idx < n && ca[idx] == ' '){
            //去掉前导空格
            idx ++;
        }
        if(idx == n){
            return 0;
        }
        boolean negative = false;
        if(ca[idx] == '-'){
            negative = true;//负数
            idx ++;
        } else if(ca[idx] == '+'){
            idx ++;
        } else if(!Character.isDigit(ca[idx])){
            return 0;
        }

        int ans = 0;
        while (idx < n && Character.isDigit(ca[idx])){

            int digit = ca[idx] - '0';
            if(ans > (Integer.MAX_VALUE - digit)/10){//防止越界
                return negative ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }

            ans = ans * 10 + digit;
            idx ++;
        }
        return negative ? -ans : ans;
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
