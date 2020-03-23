package com.wukong.service.algo.string;

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
        for (int i = 0; i < s.length; i++) {
            if (i >= s.length - i - 1) {
                break;
            }
            char temp = s[i];
            s[i] = s[s.length - i - 1];
            s[s.length - i - 1] = temp;
        }
    }
}
