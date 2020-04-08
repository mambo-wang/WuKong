package com.wukong.service.leetcode.graph;

import java.util.LinkedList;
import java.util.Queue;

public class GraphAlgo {

    public static void main(String[] args) {
        GraphAlgo graphAlgo = new GraphAlgo();
        System.out.println(graphAlgo.movingCountBFS(2,3,1));
    }

    /**
     * leetCode每日一题  4月8日  面试题13 机器人的运动范围
     * bfs解法
     * 链接：https://leetcode-cn.com/problems/ji-qi-ren-de-yun-dong-fan-wei-lcof/solution/mian-shi-ti-13-ji-qi-ren-de-yun-dong-fan-wei-dfs-b/
     */
    public int movingCountBFS(int m, int n, int k) {
        boolean[][] visited = new boolean[m][n];
        int res = 0;
        Queue<int[]> queue = new LinkedList<int[]>();
        //原点在左上角
        queue.add(new int[]{0, 0, 0, 0});
        while (queue.size() > 0) {
            //单元格出队： 将队首单元格的 索引、数位和 弹出，作为当前搜索单元格。
            int[] x = queue.poll();
            int i = x[0], j = x[1], si = x[2], sj = x[3];
            //若 ① 行列索引越界 或 ② 数位和超出目标值 k 或 ③ 当前元素已访问过 时，执行 continue
            if (i < 0 || i >= m || j < 0 || j >= n || k < si + sj || visited[i][j]) continue;
            //标记当前单元格 ：将单元格索引 (i, j) 存入 Set visited 中，代表此单元格 已被访问过 。
            visited[i][j] = true;
            res++;
            //单元格入队： 将当前元素的 下方、右方 单元格的 索引、数位和 加入 queue
            queue.add(new int[]{i + 1, j, (i + 1) % 10 != 0 ? si + 1 : si - 8, sj});
            queue.add(new int[]{i, j + 1, si, (j + 1) % 10 != 0 ? sj + 1 : sj - 8});
        }
        //Set visited 的长度 len(visited) ，即可达解的数量
        return res;
    }

    /**
     * leetCode每日一题  4月8日  面试题13 机器人的运动范围
     * dfs解法
     * 链接：https://leetcode-cn.com/problems/ji-qi-ren-de-yun-dong-fan-wei-lcof/solution/mian-shi-ti-13-ji-qi-ren-de-yun-dong-fan-wei-dfs-b/
     */
    int m, n, k;
    boolean[][] visited;

    public int movingCountDFS(int m, int n, int k) {
        this.m = m;
        this.n = n;
        this.k = k;
        this.visited = new boolean[m][n];
        return dfs(0, 0, 0, 0);
    }

    public int dfs(int i, int j, int si, int sj) {
        //当 ① 行列索引越界 或 ② 数位和超出目标值 k 或 ③ 当前元素已访问过 时，返回 00 ，代表不计入可达解。
        if (i < 0 || i >= m || j < 0 || j >= n || k < si + sj || visited[i][j]) return 0;
        //标记当前单元格 ：将索引 (i, j) 存入 Set visited 中，代表此单元格已被访问过。
        visited[i][j] = true;
        //搜索下一单元格： 计算当前元素的 下、右 两个方向元素的数位和，并开启下层递归 。
        return 1 + dfs(i + 1, j, (i + 1) % 10 != 0 ? si + 1 : si - 8, sj) + dfs(i, j + 1, si, (j + 1) % 10 != 0 ? sj + 1 : sj - 8);
    }

}
