package com.easy.interviewalgorithm.链表;

public class SolutionLinkedList {
    public static void main(String[] args) {
        /**
         * Definition for singly-linked list.
         * public class ListNode {
         *     int val;
         *     ListNode next;
         *     ListNode() {}
         *     ListNode(int val) { this.val = val; }
         *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
         * }
         */

    }

    public ListNode receiveNode(ListNode l1, ListNode l2){

        ListNode dummp  = new ListNode(-1);
        ListNode curr = dummp;
        int carry =0;

        while(l1 != null || l2 != null){
            int v1  = (l1==null) ? 0 : l1.val;
            int v2 = (l2 == null) ? 0: l2.val;

            int sum  = v1 + v2 + carry;
            carry = sum /10;
            curr.next  = new ListNode(carry);

            curr = curr.next;
            if(l1!= null) l1 = l1.next;
            if(l2!= null) l2 = l2.next;

        }

        if(carry != 0){
            curr.next = new ListNode(carry);

        }
        return  dummp.next;
    }

}

 class ListNode{
    int val;
    ListNode next;
    ListNode(){}
     ListNode(int val){
        this.val = val;
     }
     ListNode(int val, ListNode next){
        this.val = val;
        this.next = next;
     }
}
