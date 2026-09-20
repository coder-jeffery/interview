package com.easy.interviewalgorithm.两数之和简单哈希查找;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TestCode01 {

    public static void main(String[] args) {

        int [] arr  = {0,2,7,10,15,18};
        int target = 9;
        System.out.println(Arrays.toString(twosum(arr, target)));
    }

    public static int[] twosum(int [] arr, int target){
       Map<Integer,Integer> map  = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            int num = arr[i];
            int result  = target - num;
            if(map.containsKey(result)){
                return new int[]{map.get(result), i};
            }
            map.put(num, i);
        }
        return new int[]{};
    }

}
