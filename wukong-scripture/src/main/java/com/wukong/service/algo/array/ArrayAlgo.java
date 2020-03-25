package com.wukong.service.algo.array;

import java.util.ArrayList;
import java.util.HashSet;

public class ArrayAlgo {

    public static void main(String[] args) {

        ArrayAlgo arrayAlgo = new ArrayAlgo();
//        System.out.println(removeDuplicates(new int[]{1,1,1,2,3,3,4}));
//        char[] nums1 = new char[]{1,2,3,4,5,6,7};
//        char[] nums2 = new char[]{1,2,3,4,6,7};
//        rotate1(nums, 3);

//        int[] res = retainAll(nums1, nums2);
//        char c = find(nums1, nums2);
//        System.out.println(c);
        /*****************************中心索引*******************************/
//        int[] nums1 = new int[]{1, 7, 3, 6, 5, 6};
//        int index = arrayAlgo.pivotIndex(nums1);
//        System.out.println(index);
        /*****************************747. 至少是其他数字两倍的最大数*****************************************/
        int[] nums2 = new int[]{3, 6, 1, 0};
//        int i = arrayAlgo.dominantIndex(nums2);
//        System.out.println(i);
        /*********************************66 加一**************************************************/
        int[] res = arrayAlgo.plusOne(nums2);
        System.out.println();

    }


    /**66. 加一
     *
     * 给定一个由整数组成的非空数组所表示的非负整数，在该数的基础上加一。
     * 最高位数字存放在数组的首位， 数组中每个元素只存储单个数字。
     * 你可以假设除了整数 0 之外，这个整数不会以零开头。
     *
     * 题解：https://leetcode-cn.com/problems/plus-one/solution/java-shu-xue-jie-ti-by-yhhzw/
     * @param digits
     * @return
     */
    public int[] plusOne(int[] digits) {

        for (int i = digits.length - 1; i >= 0; i--) {
            digits[i]++;
            digits[i] = digits[i] % 10;
            if (digits[i] != 0) return digits;
        }
        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;
    }

    /**
     * 747. 至少是其他数字两倍的最大数
     * 在一个给定的数组nums中，总是存在一个最大元素 。
     * 查找数组中的最大元素是否至少是数组中每个其他数字的两倍。
     * 如果是，则返回最大元素的索引，否则返回-1。
     *
     * @param nums
     * @return
     */
    public int dominantIndex(int[] nums) {

        int max = -1, maxIndex = -1, maxSecond = -1;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > max) {
                maxSecond = max;
                max = nums[i];
                maxIndex = i;
            } else if (nums[i] > maxSecond) {
                maxSecond = nums[i];
            }
        }
        if (max >= 2 * maxSecond) {
            return maxIndex;
        }
        return -1;
    }

    /**
     * 724. 寻找数组的中心索引
     * https://leetcode-cn.com/problems/find-pivot-index/
     * 给定一个整数类型的数组 nums，请编写一个能够返回数组“中心索引”的方法。
     * 我们是这样定义数组中心索引的：数组中心索引的左侧所有元素相加的和等于右侧所有元素相加的和。
     * 如果数组不存在中心索引，那么我们应该返回 -1。如果数组有多个中心索引，那么我们应该返回最靠近左边的那一个。
     *
     * @param nums
     * @return
     */
    public int pivotIndex(int[] nums) {

        int sum = 0, leftSum = 0;
        for (int i : nums) {
            sum += i;
        }
        for (int i = 0; i < nums.length; i++) {
            if (leftSum == sum - leftSum - nums[i]) {
                return i;
            }
            leftSum += nums[i];
        }
        return -1;
    }

    /**
     * 1比2多一个字符，找出它
     *
     * @param chars1
     * @param chars2
     * @return
     */
    public static char find(char[] chars1, char[] chars2) {

        for (int i = 0; i < chars2.length; i++) {
            if (chars2[i] != chars1[i]) {
                return chars1[i];
            }
        }
        return chars1[chars1.length - 1];
    }

    /**
     * 349 两个数组的交集
     *
     * @param array1
     * @param array2
     * @return
     */
    public static int[] retainAll(int[] array1, int[] array2) {

        HashSet<Integer> set = new HashSet<>();

        for (int i : array1) {
            set.add(i);
        }

        ArrayList<Integer> insect = new ArrayList<>();

        for (int j : array2) {
            if (set.contains(j)) {
                insect.add(j);
                set.remove(j);
            }
        }

        int[] res = new int[insect.size()];
        int idx = 0;
        for (int k : insect) {
            res[idx++] = k;
        }
        return res;

    }

    /**
     * 时间 11.25
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * <p>
     * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/two-sum
     */
    public static int[] twoSum(int[] nums, int target) throws Exception {

        for (int i = 0; i < nums.length; i++) {

            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        throw new Exception("没有符合条件的两个元素");
    }

    /**
     * 时间 11.23
     * 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
     * 方法1：旋转法
     * 这个方法基于这个事实：当我们旋转数组 k 次， k\%nk%n 个尾部元素会被移动到头部，剩下的元素会被向后移动。
     * <p>
     * 在这个方法中，我们首先将所有元素反转。然后反转前 k 个元素，再反转后面 n-kn−k 个元素，就能得到想要的结果。
     * <p>
     * 假设 n=7n=7 且 k=3k=3 。
     * <p>
     * 原始数组                  : 1 2 3 4 5 6 7
     * 反转所有数字后             : 7 6 5 4 3 2 1
     * 反转前 k 个数字后          : 5 6 7 4 3 2 1
     * 反转后 n-k 个数字后        : 5 6 7 1 2 3 4 --> 结果
     *
     * @param nums
     * @param k
     */
    public static void rotate1(int[] nums, int k) {
        k %= nums.length; //太牛了
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    public static void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    /**
     * 方法2 暴力
     * 最简单的方法是旋转 k 次，每次将数组旋转 1 个元素。
     *
     * @param nums
     * @param k
     */
    public static void rotate2(int[] nums, int k) {
        int temp, previous;
        for (int i = 0; i < k; i++) {
            previous = nums[nums.length - 1];
            for (int j = 0; j < nums.length; j++) {
                temp = nums[j];
                nums[j] = previous;
                previous = temp;
            }
        }
    }


    /**
     * 时间 11.22
     * 给定一个排序数组，你需要在原地删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
     * 不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     * @param nums
     * @return
     */
    public static int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }


}
