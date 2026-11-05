package com.easy.interviewalgorithm.testcode;

import java.util.*;

public class TestCode {

    // 1. 删除链表倒数第 N 个节点
    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 快慢指针，一次遍历
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        // 虚拟头节点，处理删除头结点边界
        ListNode dummy = new ListNode(0, head);
        ListNode fast = dummy;
        ListNode slow = dummy;
        // fast先走n步
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }
        // fast走到末尾，slow走到待删节点前一个
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        // 删除节点
        slow.next = slow.next.next;
        return dummy.next;
    }

    // 2. 识别有效括号
    public static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');

        for (char c : s.toCharArray()) {
            if (map.containsKey(c)) {
                // 右括号
                if (stack.isEmpty() || stack.pop() != map.get(c)) {
                    return false;
                }
            } else {
                // 左括号入栈
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }

    // 3. 最小路径和：只能向下、向右
    public static int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = grid[0][0];
        // 第一行，只能从左边来
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }
        // 第一列，只能从上面来
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        // dp[i][j] = min(上边,左边) + 当前值
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }
        return dp[m - 1][n - 1];
    }



    // ------------------- 测试main函数 -------------------
    public static void main(String[] args) {
        // 1. 删除倒数第N节点测试 [1,2,3,4,5] n=2
        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        ListNode newHead = removeNthFromEnd(head,2);
        printList(newHead); //1 2 3 5

        //2.有效括号
        System.out.println(isValid("()[]{}")); //true
        System.out.println(isValid("(]")); //false

        //3.最小路径和
        int[][] grid = {{1,3,1},{1,5,1},{4,2,1}};
        System.out.println(minPathSum(grid)); //7


    }

    //打印链表辅助
    public static void printList(ListNode node){
        while(node != null){
            System.out.print(node.val+" ");
            node = node.next;
        }
        System.out.println();
    }
}