package com.easy.interviewalgorithm.字母异位词分组哈希设计;

import java.util.*;

/**
 * 49. 字母异位词分组｜中等｜哈希 key 设计
 *
 * */
public class TestCode02 {
    public static void main(String[] args) {
        String[] str  = {"t", "e", "a"};
        System.out.println(groupAnagrams(str));
    }

    public static List<List<String>> groupAnagrams(String [] strArr){
        Map<String, List<String>> map = new HashMap<>();
        for(String str : strArr){
            char[] arr  = str.toCharArray();
            Arrays.sort(arr);
            String key  = new String(arr);
            map.computeIfAbsent(key,  k -> new ArrayList<>()).add(str);//computeIfAbsent
        }
        return new ArrayList<>(map.values());
    }
}
