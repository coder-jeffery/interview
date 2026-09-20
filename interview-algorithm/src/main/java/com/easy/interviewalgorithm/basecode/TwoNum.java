package com.easy.interviewalgorithm.basecode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoNum {
    public static void main(String[] args) {
        int [] arr  = {2,3,6,10};
        int target = 12;

        int [] res  = twonum(arr, target);
        System.out.println(Arrays.toString(res));
    }

    public static int [] twonum(int [] arr, int target){
        Map<Integer, Integer> map  =  new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            int num  = arr[i];
            int temp  = target -num;

            if(map.containsKey(temp)){
                return new int[]{map.get(temp), i};
            }
            map.put(num, i);
        }
        return new int[]{};
    }
}
