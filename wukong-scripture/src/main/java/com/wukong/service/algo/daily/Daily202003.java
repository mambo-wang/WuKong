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
//        System.out.println(daily202003.message(new int[]{2, 1, 4, 5, 3, 1, 1, 3}));
        /*******************************************************3.25表面积*********************************************************************/
//        System.out.println(daily202003.surfaceArea(new int[][]{{2, 2, 2}, {2, 1, 2}, {2, 2, 2}}));
        /***********************************************************3.27 卡牌分组   **/
//        System.out.println(daily202003.hasGroupsSizeX(new int[]{1,2,3,4,4,4,4,3,2,1}));
        /*****************************************************3.28 Trie树 字符串压缩**********************************************************************/

        System.out.println(daily202003.minimumLengthEncoding(new String[]{"time","me","bell"}));

    }

    /**
     * 3月28日   820. 单词的压缩编码
     * 题目：
     * 给定一个单词列表，我们将这个列表编码成一个索引字符串 S 与一个索引列表 A。
     * 例如，如果这个列表是 ["time", "me", "bell"]，我们就可以将其表示为 S = "time#bell#" 和 indexes = [0, 2, 5]。
     * 对于每一个索引，我们可以通过从字符串 S 中索引的位置开始读取字符串，直到 "#" 结束，来恢复我们之前的单词列表。
     * 那么成功对给定单词列表进行编码的最小字符串长度是多少呢？
     * 链接：https://leetcode-cn.com/problems/short-encoding-of-words/solution/99-java-trie-tu-xie-gong-lue-bao-jiao-bao-hui-by-s/
     * 解题思路：
     * 为什么这题我们要用字典树做呢？因为我们需要知道单词列表里，哪些单词是其它某个单词的后缀。既然要求的是后缀，我们只要把单词的倒序插入字典树，再用字典树判断某个单词的逆序是否出现在字典树里就可以了。
     * 我们必须要先插入单词长的数组，否则会有问题。比如如果我先插入了"em"，再插入"emit",会发现两个都可以插入进去，很显然是不对的，
     * 所以在插入之前需要先根据单词的长度由长到短排序。
     * @param words
     * @return
     */
    public int minimumLengthEncoding(String[] words) {

        int len = 0;
        Trie trie = new Trie();
        // 先对单词列表根据单词长度由长到短排序
        Arrays.sort(words, (s1, s2) -> s2.length() - s1.length());
        // 单词插入trie，返回该单词增加的编码长度
        for (String word: words) {
            len += trie.insert(word);
        }
        return len;
    }

    // 定义tire
    class Trie {

        TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public int insert(String word) {
            TrieNode cur = root;
            boolean isNew = false;
            // 倒着插入单词
            for (int i = word.length() - 1; i >= 0; i--) {
                int c = word.charAt(i) - 'a';
                if (cur.children[c] == null) {
                    isNew = true; // 是新单词
                    cur.children[c] = new TrieNode();
                }
                cur = cur.children[c];
            }
            // 如果是新单词的话编码长度增加新单词的长度+1，否则不变。
            return isNew? word.length() + 1: 0;
        }
    }
    class TrieNode {
        char val;
        TrieNode[] children = new TrieNode[26];

        public TrieNode() {}
    }

    /**
     * 3月27日 每日一题  914.卡牌分组
     * 思路：求各个数字出现次数的最大公约数
     * 题解：https://leetcode-cn.com/problems/x-of-a-kind-in-a-deck-of-cards/solution/3ms-jian-dan-java-fu-zeng-reducexie-fa-miao-dong-b/
     * @param deck
     * @return
     */
    public boolean hasGroupsSizeX(int[] deck) {
        // 计数
        int[] counter = new int[10000];
        for (int num : deck) {
            counter[num]++;
        }
        // 迭代求多个数的最大公约数
        int x = 0;
        for (int cnt : counter) {
            if (cnt > 0) {
                x = gcd(x, cnt);
                if (x == 1) { // 如果某步中gcd是1，直接返回false
                    return false;
                }
            }
        }
        return x >= 2;
    }
    // 辗转相除法
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }


    /**
     * 3.26  leetcode 999 车的可用捕获量
     * https://leetcode-cn.com/problems/available-captures-for-rook/solution/jian-dan-java100-by-sweetiee/
     *
     * @param board 棋盘
     * @return
     */
    public int numRookCaptures(char[][] board) {
        // 定义上下左右四个方向
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // 找到白车所在的位置
                if (board[i][j] == 'R') {
                    // 分别判断白车的上、下、左、右四个方向
                    int res = 0;
                    for (int k = 0; k < 4; k++) {
                        int x = i, y = j;
                        while (true) {
                            x += dx[k];
                            y += dy[k];
                            if (x < 0 || x >= 8 || y < 0 || y >= 8 || board[x][y] == 'B') {
                                break;
                            }
                            if (board[x][y] == 'p') {
                                res++;
                                break;
                            }
                        }
                    }
                    return res;
                }
            }
        }
        return 0;
    }

    /**
     * 每日一题 3月25日
     * 892. 三维形体的表面积
     * 题目：在 N * N 的网格上，我们放置一些 1 * 1 * 1  的立方体。
     * 每个值 v = grid[i][j] 表示 v 个正方体叠放在对应单元格 (i, j) 上。
     * 请你返回最终形体的表面积。
     * 链接：https://leetcode-cn.com/problems/surface-area-of-3d-shapes
     * <p>
     * 题解：https://leetcode-cn.com/problems/surface-area-of-3d-shapes/solution/shi-li-you-tu-you-zhen-xiang-jiang-jie-yi-kan-jiu-/
     * 解题思路：
     * 首先，一个柱体一个柱体的看，每个柱体是由：2个底面（上表面/下表面）+ 所有的正方体都贡献了4个侧表面积。
     * 然后，把柱体贴合在一起之后，我们需要把贴合的表面积给减掉，两个柱体贴合的表面积就是 两个柱体高的最小值*2。
     *
     * @param grid
     * @return
     */
    public int surfaceArea(int[][] grid) {
        int n = grid.length, area = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // 先把grid[i][j]赋值给level，省掉了bound check，可以略微略微略微优化一下耗时。。。
                int level = grid[i][j];
                if (level > 0) {
                    // 一个柱体中：2个底面 + 所有的正方体都贡献了4个侧表面积
                    area += (level * 4) + 2;
                    // 减掉 i 与 i-1 相贴的两份表面积
                    area -= i > 0 ? Math.min(level, grid[i - 1][j]) * 2 : 0;
                    // 减掉 j 与 j-1 相贴的两份表面积
                    area -= j > 0 ? Math.min(level, grid[i][j - 1]) * 2 : 0;
                }
            }
        }
        return area;
    }

    /**
     * leetCode 每日一题 3月24日
     * 题目：面试题17.16 按摩师
     * 一个有名的按摩师会收到源源不断的预约请求，每个预约都可以选择接或不接。在每次预约服务之间要有休息时间，因此她不能接受相邻的预约。
     * 给定一个预约请求序列，替按摩师找到最优的预约集合（总预约时间最长），返回总的分钟数。
     * 题目链接：https://leetcode-cn.com/problems/the-masseuse-lcci
     * 题解：https://zhuanlan.zhihu.com/p/115731099
     *
     * @param nums
     * @return
     */
    public int message(int[] nums) {

        int n = nums.length;
        //处理边界问题
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return nums[0];
        }
        //定义dp数组，按照状态转移方程递推
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        return dp[n - 1];
    }

    /**
     * 时间 3月23日
     * 题目：876. 链表的中间结点
     * 给定一个带有头结点 head 的非空单链表，返回链表的中间结点。
     * 如果有两个中间结点，则返回第二个中间结点。
     * https://leetcode-cn.com/problems/middle-of-the-linked-list/
     * 解题思路：快慢指针
     *
     * @param head
     * @return
     */
    public ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;

        while (fast != null && fast.next != null) {
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
     *
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
