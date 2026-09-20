package com.easy.interviewalgorithm.LRU;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUTestCode  extends LinkedHashMap<Integer, Integer> {
    private final int capacity;

    public LRUTestCode(int capacity){
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }


    public void put(){

    }

}
