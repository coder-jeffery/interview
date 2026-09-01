package com.easy.interviewalgorithm.移动零;

import java.util.Arrays;

public class SlowZeron {
    public static void main(String[] args) {
        int [] nums  = {0,100,2,0,4,0,3,5,200};
        int[] resInt  = moveZero(nums);
        String res  = Arrays.toString(resInt);
        System.out.println(res);
    }

    public static int[] moveZero(int [] nums){
        //
        int slow =0;
        for (int i = 0; i < nums.length; i++) {
            if(nums[i] != 0){
                nums[slow] = nums[i];
                slow++;
            }
        }

        for (int i = slow; i < nums.length ; i++) {
            nums[i] = 0;
        }

        return nums;
    }
}
