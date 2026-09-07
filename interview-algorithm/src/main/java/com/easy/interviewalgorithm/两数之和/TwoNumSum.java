package com.easy.interviewalgorithm.两数之和;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoNumSum {
    public static void main(String[] args) {
        int[] nums = {2,7,10,11,16,18};
        int target = 9;
        int[] res1  = twoNumSum(nums, target);
        int[] res2  = twoNumSum2(nums, target);
        System.out.println(Arrays.toString(res1) + "\n" + Arrays.toString(res2));
    }

    /**
     * 方式一
     * */
    public static int[] twoNumSum(int[] nums, int target){
        for (int i = 0; i < nums.length; i++) {
            for(int j = i+1; j < nums.length; j++){

                if(target == nums[i] + nums[j]){

                    return new int[]{i,j};
                }
            }
        }
        return new int[]{};
    }

    /**
     * 方式二
     * */
    public static int[] twoNumSum2(int[] nums, int target){
        Map<Integer, Integer> map  = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int num  = nums[i];
            int result  = target - num;
            if(map.containsKey(result)){
                return new int[]{map.get(result), i};
            }
            map.put(num, i);
        }
        return new int[]{};
    }
}
