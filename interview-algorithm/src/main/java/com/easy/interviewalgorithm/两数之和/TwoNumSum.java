package com.easy.interviewalgorithm.两数之和;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoNumSum {
    public static void main(String[] args) {
        int[] nums = {2,7,10,11,16,18};
        int target = 9;
        int[] res  = twoNumSum(nums, target);
//        int[] res  = twoNumSum2(nums, target);
        System.out.println(Arrays.toString(res));
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
        for(int i=0; i < nums.length; i++){
            int comp  = target - nums[i];
            if(!map.containsKey(comp)){
                map.put( nums[i], i);
            }
            return new int[]{map.get(comp), i};
        }
        return new int[]{};
    }
}
