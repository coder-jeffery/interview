package com.easy.interviewalgorithm.连续最长子数组;

public class LongSubArr {
    public static void main(String[] args) {
        /**
         * 连续最长子数组
         * */
        int[] nums = {1,3,5,4,7,10,9,2};
        int res  = longsumarr(nums);
        System.out.println(res);
    }

    public static  int longsumarr(int[] nums){
        if(nums == null || nums.length == 0) return 0;
        int maxLen  = 1;
        int currlen = 1;
        for (int i = 1; i < nums.length; i++) {
            //
            if(nums[i] > nums[i-1]){
                currlen ++;
            }else {
                currlen = 1;
            }
            maxLen = Math.max(maxLen, currlen);
        }
        return maxLen;
    }
}
