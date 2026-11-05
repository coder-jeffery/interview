package com.easy.interviewalgorithm.药明生物;

import java.util.*;

public class ScoreCode2 {

    private Map<Integer, Integer> playerScoreMap;

    public ScoreCode2() {
        playerScoreMap = new HashMap<>();
    }

    public void addScore(int playedId, int score) {
        playerScoreMap.put(playedId, playerScoreMap.getOrDefault(playedId, 0) + score);
    }

    // 【优化版：小顶堆】求前K名分数总和 O(n log K)
    public Integer top(int K) {
        // 小顶堆，堆顶是当前K个里面最小的分数
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (Integer score : playerScoreMap.values()) {
            minHeap.offer(score);
            if (minHeap.size() > K) {
                minHeap.poll();
            }
        }
        int sum = 0;
        for (Integer s : minHeap) {
            sum += s;
        }
        return sum;
    }

    // reset：删除玩家，返回当前排行榜 [[id,score],...] 降序
    public List<int[]> reset(int playedId) {
        playerScoreMap.remove(playedId);
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(playerScoreMap.entrySet());
        list.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        List<int[]> res = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : list) {
            res.add(new int[]{entry.getKey(), entry.getValue()});
        }
        return res;
    }

    // 测试入口，模拟题目输入
    public static void main(String[] args) {
        String[] ops = {"leaderboard", "addScore", "addScore", "top", "reset"};
        int[][] params = {{}, {1, 100}, {2, 90}, {1}, {1}};

        List<Object> output = new ArrayList<>();
        ScoreTestCode scoreTestCode = null;

        for (int i = 0; i < ops.length; i++) {
            String op = ops[i];
            int[] p = params[i];
            switch (op) {
                case "leaderboard":
                    scoreTestCode = new ScoreTestCode();
                    output.add(null);
                    break;
                case "addScore":
                    scoreTestCode.addScore(p[0], p[1]);
                    output.add(null);
                    break;
                case "top":
                    Integer topSum = scoreTestCode.top(p[0]);
                    output.add(topSum);
                    break;
                case "reset":
                    List<int[]> rankList = scoreTestCode.reset(p[0]);
                    output.add(rankList);
                    break;
            }
        }

        // 格式化打印，输出 [null, null, null, 100, [2,90]]
        System.out.print("[");
        for (int i = 0; i < output.size(); i++) {
            Object o = output.get(i);
            if (o == null) {
                System.out.print("null");
            } else if (o instanceof Integer) {
                System.out.print(o);
            } else if (o instanceof List<?>) {
                List<int[]> arrList = (List<int[]>) o;
                if (!arrList.isEmpty()) {
                    int[] item = arrList.get(0);
                    System.out.print("[" + item[0] + "," + item[1] + "]");
                }
            }
            if (i != output.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}
