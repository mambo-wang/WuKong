package com.wukong.service.leetcode.string;

public class StringAlgo {

    public static void main(String[] args) {
        StringAlgo stringAlgo = new StringAlgo();

        char[] str = "abcde".toCharArray();
        stringAlgo.reverseString(str);
        System.out.println(String.valueOf(str));
    }

    /**
     * 344. 反转字符串
     * @param s
     */
    public void reverseString(char[] s) {
        int i = 0; int j=s.length -1;

        while(i < j){
            char temp = s[i];
            s[i] = s[j];
            s[j] = temp;
            i ++;
            j --;
        }
    }
}
