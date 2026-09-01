package com.easy.interviewalgorithm.盛水最多容器;

public class WaterLarge {
    public static void main(String[] args) {


    }

    public static  int largeArea(int [] height){
        //

        int left  =0;
        int right = height.length-1;

        int maxArea = 0;

        while (left < right){

            int w  = right - left;
            int h  = Math.min(height[left], height[right]);
            int area = w * h;
            maxArea = Math.max(maxArea, area);

            if(height[left] < height[right]){
                left++;
            }else {
                right--;
            }
        }
        return maxArea;
    }
}
