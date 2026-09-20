package com.easy.interviewalgorithm.二叉树;

public class TreeNodeTestCode {
    public static void main(String[] args) {

    }

    // 前序：根 → 左 → 右
    public void preOrder(NorTreeNode root){
        if(root == null) return ;
        preOrder(root.left);
        preOrder(root.right);
    }

    // 中序：左 → 根 → 右（BST中序升序）
    public void inOrder(NorTreeNode root){
        if(root == null) return;
        inOrder(root.left);
        System.out.println(root.val);
        inOrder(root.right);
    }

    // 后序：左 → 右 → 根
    public void postOrder(NorTreeNode root){
        if(root == null) return;
        postOrder(root.right);
        postOrder((root.left));
        System.out.println(root.val);
    }
}


class NorTreeNode{
    int val;
    NorTreeNode left;
    NorTreeNode right;

    public NorTreeNode() {
    }

    public NorTreeNode(int val) {
        this.val = val;
    }

    public NorTreeNode(int val, NorTreeNode left, NorTreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}