package com.easy.interviewalgorithm.最长连续序列;

import java.util.*;

public class LongConJer {
    public static void main(String[] args) {

    }

    public static int receiveLongestArr(int [] nums){
        // 1.存储数据的集合  多个int数组
        // 2.比较长度
        // 3.输出最长的连续整数数组
        Set<Integer> set = new HashSet<>();
        for(int num: nums){
            set.add(num);
        }
        int maxLen = 0;

        for(int x : set){
            if(!set.contains( x -  1)){
                //
                int currentNum = x;
                int currentLen = 1;

                while (set.contains(currentNum +1)){
                    currentNum++;
                    currentLen++;
                }
                maxLen  = Math.max(maxLen, currentLen);
            }
        }
        return maxLen;
    }
}
