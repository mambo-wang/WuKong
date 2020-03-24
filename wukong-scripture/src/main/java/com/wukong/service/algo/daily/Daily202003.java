package com.wukong.service.algo.daily;

import java.util.*;

public class Daily202003 {

    public static void main(String[] args) {

        Daily202003 daily202003 = new Daily202003();
        /**************************************************3.20****************************************************************/
//        int[] result = getLeastNumbers(new int[]{4,5,1,6,2,7,3,8}, 4);
        /**************************************************3.21水壶****************************************************************/
//        System.out.println(daily202003.canMeasureWater_BFS(3,5,4));
        /**************************************************3.22****************************************************************/
//        System.out.println(daily202003.minIncrementForUniq(new int[]{3, 2, 1, 2, 1, 7}));
        /*****************************************3.23求链表中间节点**************************************************/
//        ListNode listNode1 = new ListNode(1);
//        ListNode listNode2 = new ListNode(2);
//        ListNode listNode3 = new ListNode(3);
//        ListNode listNode4 = new ListNode(4);
//        ListNode listNode5 = new ListNode(5);
//        ListNode listNode6 = new ListNode(6);
//        listNode1.next = listNode2;
//        listNode2.next = listNode3;
//        listNode3.next = listNode4;
//        listNode4.next = listNode5;
//        listNode5.next = listNode6;
//        ListNode listNode = daily202003.middleNode(listNode1);
//        System.out.println(listNode.val);
       /******************************************************3.24按摩师************************************************************/
        System.out.println(daily202003.message(new int[]{2,1,4,5,3,1,1,3}));
        /****************************************************************************************************************************/
    }

    /**
     * leetCode 每日一题 3月24日
     * 题目：面试题17.16 按摩师
     * 一个有名的按摩师会收到源源不断的预约请求，每个预约都可以选择接或不接。在每次预约服务之间要有休息时间，因此她不能接受相邻的预约。
     * 给定一个预约请求序列，替按摩师找到最优的预约集合（总预约时间最长），返回总的分钟数。
     * 题目链接：https://leetcode-cn.com/problems/the-masseuse-lcci
     * 题解：https://zhuanlan.zhihu.com/p/115731099
     * @param nums
     * @return
     */
    public int message(int[] nums){

        int n = nums.length;
        //处理边界问题
        if(n == 0){
            return 0;
        }
        if(n == 1){
            return nums[0];
        }
        //定义dp数组，按照状态转移方程递推
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0],nums[1]);
        for (int i = 2; i < n; i++){
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        return dp[n-1];
    }

    /**
     * 时间 3月23日
     * 题目：876. 链表的中间结点
     * 给定一个带有头结点 head 的非空单链表，返回链表的中间结点。
     * 如果有两个中间结点，则返回第二个中间结点。
     * https://leetcode-cn.com/problems/middle-of-the-linked-list/
     * 解题思路：快慢指针
     * @param head
     * @return
     */
    public ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;

        while (fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    //Definition for singly-linked list.
    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


    /**
     * 日期：3月22日
     * 945. 题目： 使数组唯一的最小增量
     * 给定整数数组 A，每次 move 操作将会选择任意 A[i]，并将其递增 1。
     * 返回使 A 中的每个值都是唯一的最少操作次数。
     * https://leetcode-cn.com/problems/minimum-increment-to-make-array-unique/solution/ji-shu-onxian-xing-tan-ce-fa-onpai-xu-onlogn-yi-ya/
     *
     * @return
     */
    public int minIncrementForUniq(int[] a) {

        Arrays.sort(a);
        int count = 0;
        for (int i = 1; i < a.length; i++) {
            if (a[i] <= a[i - 1]) {
                int pre = a[i];
                a[i] = a[i - 1] + 1;
                count += a[i] - pre;
            }
        }
        return count;
    }

    /**
     * 日期：3月21日
     * 题目：365 水壶问题
     * 有两个容量分别为 x升 和 y升 的水壶以及无限多的水。请判断能否通过使用这两个水壶，从而可以得到恰好 z升 的水？
     * https://leetcode-cn.com/problems/water-and-jug-problem/solution/hu-dan-long-wei-liang-zhang-you-yi-si-de-tu-by-ant/
     * 解法：极简bfs，我个人的理解是，这种bfs简化的思路是以总水量在讨论，而其他题解都着重于X，Y两种状态来讨论，所以相对多了互相倒水的细节讨论。
     * 每次改变总水量都能看作是X或Y升的增加，或者是X或Y升的减少，就算是相互倒水其实通过改变倒水方式也是可以化作X或Y的增减。
     * 这样思路也可以很容易转到数学思路了（然而我并不知道贝祖定理）
     * 贝祖定理：对于给定的正整数a，b，方程a*x+b*y=c有解的充要条件为c是gcd（a，b）的整数倍（gcd为最大公约数）
     * @param x 桶1容量
     * @param y 桶2容量
     * @param z 目标体积
     * @return
     */
    public boolean canMeasureWater_BFS(int x, int y, int z) {
        if (z < 0 || z > x + y) {
            return false;
        }
        Set<Integer> set = new HashSet<>();
        Queue<Integer> q = new LinkedList<>();
        q.offer(0);
        while (!q.isEmpty()) {
            int n = q.poll();
            if (n + x <= x + y && set.add(n + x)) {
                q.offer(n + x);
            }
            if (n + y <= x + y && set.add(n + y)) {
                q.offer(n + y);
            }
            if (n - x >= 0 && set.add(n - x)) {
                q.offer(n - x);
            }
            if (n - y >= 0 && set.add(n - y)) {
                q.offer(n - y);
            }
            if (set.contains(z)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 3.20 leetcode每日一题
     * 题目：面试题40. 最小的k个数
     * 题目描述 输入整数数组 arr ，找出其中最小的 k 个数。例如，输入4、5、1、6、2、7、3、8这8个数字，则最小的4个数字是1、2、3、4。
     * 优先级队列知识 https://www.jianshu.com/p/f1fd9b82cb72
     * 题解 https://leetcode-cn.com/problems/zui-xiao-de-kge-shu-lcof/solution/3chong-jie-fa-miao-sha-topkkuai-pai-dui-er-cha-sou/
     * <p>
     * 解题思路：// 保持堆的大小为K，然后遍历数组中的数字，遍历的时候做如下判断：
     * // 1. 若目前堆的大小小于K，将当前数字放入堆中。
     * // 2. 否则判断当前数字与大根堆堆顶元素的大小关系，如果当前数字比大根堆堆顶还大，这个数就直接跳过；
     * //    反之如果当前数字比大根堆堆顶小，先poll掉堆顶，再将该数字放入堆中。
     *
     * @return
     */
    public static int[] getLeastNumbers(int[] arr, int k) {

        if (k == 0 || arr.length == 0) {
            return new int[0];
        }

        //创建大根堆（默认是小根堆，需要重写比较器）
        Queue<Integer> pq = new PriorityQueue<>((v1, v2) -> v2 - v1);
        for (int num : arr) {
            if (pq.size() < k) {
                pq.offer(num);
            } else if (num < pq.peek()) {
                pq.poll();
                pq.offer(num);
            }
        }

        int[] res = new int[pq.size()];
        int idx = 0;
        for (int num : pq) {
            res[idx++] = num;
        }
        return res;
    }


}
