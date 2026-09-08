package com.easy.interviewalgorithm.二叉树;

public class TreeNodeDemo {

    public static void main(String[] args) {

        TreeNode treeNode  = new TreeNode();
        
    }
}

class TreeNode{
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(){

    }
    TreeNode(int val){
        this.val = val;
    }
    TreeNode(int val, TreeNode left, TreeNode right){
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
