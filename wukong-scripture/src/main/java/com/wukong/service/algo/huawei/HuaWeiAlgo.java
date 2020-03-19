package com.wukong.service.algo.huawei;

/**
 * 华为面试真题
 */
public class HuaWeiAlgo {


    public static void main(String[] args) {
        int[] ints = slideWindow(new int[]{2,3,4,2,6,2,5,1}, 3);
        System.out.println(slideWindow(new int[]{2,3,4,2,6,2,5,1}, 3));
    }

    /**
     * 滑动窗口
     * @param nums
     * @param k
     * @return
     */
    public static int[] slideWindow(int[] nums, int k) {
        if (nums.length == 0 || k > nums.length){
            return new int[0];
        }

        int[] ans = new int[nums.length - k + 1];
        int max = 0;
        for (int i = 0; i < k; i++){
            max = Math.max(max, nums[i]);
        }

        int index = 0;
        ans[index++] = max;
        for (int i = k; i < nums.length; ++i) {
            //如果进来的右边界的数大于之前的最大值的话，那么更新最大值为新进来的右边界的值
            if (max < nums[i]) {
                max = nums[i];
                //如果最大值是即将划走的左边界的值的话，那么需要重新更新需找最大值
            } else if (max == nums[i - k]) {
                max = nums[i - k + 1];
                for (int j = i - k + 1; j <= i; ++j) {
                    max = Math.max(max, nums[j]);
                }
            }
            ans[index++] = max;
        }

        return ans;
    }

    /**
     * 剪绳子
     * @param n
     * @return
     */
    public static int cuttingRope(int n) {
        int ans = 0;
        for(int i = 2; i <= n/2 + 1; i ++){
            ans = Math.max(ans, cal(n, i));
        }
        return ans;
    }

    private static int cal(int n, int m){
        int a = n / m, b = n % m,ans = 1;
        for(int i = 0; i < b; i ++){
            ans *= a + 1;
        }
        for(int i = 0; i < m - b; i ++){
            ans *= a;
        }
        return ans;
    }
}
