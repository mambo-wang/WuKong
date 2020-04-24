package com.wukong.service.leetcode.daily;

public class
Daily202004 {

    public static void main(String[] args) {

        Daily202004 daily202004 = new Daily202004();
//        System.out.println(daily202004.myAtoI("   -22342iutreit"));
        /***********************************数组中的逆序对*************************************/
        System.out.println(daily202004.reversePairs(new int[]{7,5,6,4}));;
    }

    /**
     * leetCode每日一题 4.24 面试题51. 数组中的逆序对
     * https://leetcode-cn.com/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
     * 归并排序
     * @param nums
     * @return
     */
    public int reversePairs(int[] nums) {
        return mergeSort(nums, 0, nums.length - 1);
    }
    private int mergeSort(int[] nums, int left, int right) {
        if (left >= right) return 0;
        int mid = (left + right) >> 1;
        //将归并的所有结果汇总返回
        return mergeSort(nums, left, mid) + mergeSort(nums, mid + 1, right) + merge(nums, left, mid, right);
    }
    private int merge(int[] nums, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int k = 0;
        int count = 0;
        int res[] = new int[right - left + 1];
        while (i <= mid && j <= right) {
            if (nums[i] > nums[j]) count += mid - i + 1;//如果j位置小于i位置，那么j位置小于i位置后所有的左半边的数
            res[k++] = nums[i] <= nums[j] ? nums[i++] : nums[j++];
        }
        while (i <= mid) res[k++] = nums[i++];
        while (j <= right) res[k++] = nums[j++];
        for (int m = 0; m < res.length; m++) {
            nums[left + m] = res[m];
        }
        return count;
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
