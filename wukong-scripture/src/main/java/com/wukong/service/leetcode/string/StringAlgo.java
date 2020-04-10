package com.wukong.service.leetcode.string;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class StringAlgo {

    public static void main(String[] args) {
        StringAlgo stringAlgo = new StringAlgo();

        /************************************344 反转字符串**********************************************/
//        char[] str = "abcde".toCharArray();
//        stringAlgo.reverseString(str);
//        System.out.println(String.valueOf(str));
        /*****************************************22 括号生成******************************************************/
//        System.out.println(stringAlgo.generateParenthesis(3));
        /*****************************************151. 翻转字符串里的单词**********************************************************/
        System.out.println(stringAlgo.reverseWords(" Hello   World!   "));
    }

    /**
     * 344. 反转字符串
     *
     * @param s
     */
    public void reverseString(char[] s) {
        int i = 0;
        int j = s.length - 1;

        while (i < j) {
            char temp = s[i];
            s[i] = s[j];
            s[j] = temp;
            i++;
            j--;
        }
    }

    /**
     * leetCode每日一题 4月9日 22 括号生成
     * 链接：https://leetcode-cn.com/problems/generate-parentheses/solution/jian-dan-dfsmiao-dong-by-sweetiee/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    List<String> res = new ArrayList<>();
    public List<String> generateParenthesis(int n) {
        dfs(n, n, "");
        return res;
    }
    private void dfs(int left, int right, String curStr) {
        if (left == 0 && right == 0) { // 左右括号都不剩余了，递归终止
            res.add(curStr);
            return;
        }
        if (left > 0) { // 如果左括号还剩余的话，可以拼接左括号
            dfs(left - 1, right, curStr + "(");
        }
        if (right > left) { // 如果右括号剩余多于左括号剩余的话，可以拼接右括号
            dfs(left, right - 1, curStr + ")");
        }
    }

    /**
     * leetCode每日一题 151. 翻转字符串里的单词
     * 题目：给定一个字符串，逐个翻转字符串中的每个单词。去除多余空串
     * 解题思路：由于双端队列支持从队列头部插入的方法，因此我们可以沿着字符串一个一个单词处理，然后将单词压入队列的头部，再将队列转成字符串即可。
     * https://leetcode-cn.com/problems/reverse-words-in-a-string/
     */
    public String reverseWords(String s){
        if(s == null || s.length() == 0){
            return s;
        }
        s = s.trim();

        Deque<String> deque = new ArrayDeque<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i =0; i < s.length(); i++){
            char c = s.charAt(i);
            if(c != ' '){
                stringBuilder.append(c);
            } else if(stringBuilder.length() > 0 ){
                deque.addFirst(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
        }
        deque.addFirst(stringBuilder.toString());
        return String.join(" ", deque);
    }
}
