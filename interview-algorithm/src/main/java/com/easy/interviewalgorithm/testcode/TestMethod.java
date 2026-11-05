package com.easy.interviewalgorithm.testcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TestMethod {

    // ======================4. 两个字符串大数相加 ======================
    public static String addStrings(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int carry = 0; // 进位
        while (i >= 0 || j >= 0 || carry > 0) {
            int a = i >= 0 ? num1.charAt(i) - '0' : 0;
            int b = j >= 0 ? num2.charAt(j) - '0' : 0;
            int sum = a + b + carry;
            carry = sum / 10;
            sb.append(sum % 10);
            i--;
            j--;
        }
        // 反转
        return sb.reverse().toString();
    }

    // ======================5. 合并区间 ======================
    public static int[][] merge(int[][] intervals) {
        if (intervals.length == 0) return new int[0][];
        // 按区间左端点排序
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
        List<int[]> res = new ArrayList<>();
        res.add(intervals[0]);
        for (int i = 1; i < intervals.length; i++) {
            int[] last = res.get(res.size() - 1);
            int curL = intervals[i][0];
            int curR = intervals[i][1];
            if (curL <= last[1]) {
                // 重叠，合并，更新右端
                last[1] = Math.max(last[1], curR);
            } else {
                // 不重叠直接加入
                res.add(intervals[i]);
            }
        }
        return res.toArray(new int[res.size()][]);
    }

    // ======================6. 只出现一次的数字，其余出现两次，异或 ======================
    public static int singleNumber(int[] nums) {
        int res = 0;
        for (int x : nums) {
            res ^= x;
        }
        return res;
    }

    public static void main(String[] args) {

        //4.大数相加
        System.out.println(addStrings("21543655","4332656442")); //4354200097

        //5.合并区间
        int[][] intervals = {{1,3},{2,6},{8,10},{15,18}};
        int[][] merged = merge(intervals);
        for(int[] arr : merged){
            System.out.print(Arrays.toString(arr)+" ");
        }
        System.out.println();

        //6.只出现一次数字
        int[] arr = {2,2,1};
        System.out.println(singleNumber(arr)); //1
    }
}
