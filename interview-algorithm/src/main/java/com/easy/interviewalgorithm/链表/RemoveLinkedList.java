package com.easy.interviewalgorithm.链表;

public class RemoveLinkedList {
    public static void main(String[] args) {

    }

    public ListNode2 removeLinkedList(ListNode2 head, int n){
        ListNode2 dummy  = new ListNode2(-1);
        dummy.next = head;
        ListNode2 curr  = head;
        int len  =0;

        while (curr != null){
            len++;
            curr = curr.next;
        }

        int move  = len - n;
        curr = dummy;
        for (int i = 0; i < move; i++) {
            curr = curr.next;
        }
        curr.next = curr.next.next;
        return dummy.next;

    }

}
class ListNode2{
    int val;
    ListNode2 next;
    ListNode2(){}
    ListNode2(int val){
        this.val = val;
    }
    ListNode2(int val, ListNode2 next){
        this.val = val;
        this.next  = next;
    }
}
