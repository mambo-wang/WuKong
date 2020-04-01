package com.wukong.service.leetcode.stack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class StackAlgo {


    public static void main(String[] args) {
        StackAlgo stackAlgo = new StackAlgo();
//     *******************************************20 有效的字符串**********************************************
        System.out.println(stackAlgo.isValid("([{}]["));
    }

    /**
     * leetcode 20 有效的字符串
     * https://leetcode-cn.com/problems/valid-parentheses/submissions/
     * 思路：使用栈进行字符串验证
     */
    public boolean isValid(String s){
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');

        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()){
            if(c == '(' || c == '[' || c == '{'){
                stack.push(c);
            } else {
                if(stack.isEmpty() || map.get(c) != stack.pop()){
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }
}
