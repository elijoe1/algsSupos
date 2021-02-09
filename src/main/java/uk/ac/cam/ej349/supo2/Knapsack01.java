package uk.ac.cam.ej349.supo2;

import java.util.*;

public class Knapsack01 {

    private static int[][] createMatrix(int n, int v) {
        int[][] matrix = new int[n+1][v+1];
        for (int i = 0; i < n+1; i++) {
            for (int j = 0; j < v+1; j++) {
                matrix[i][j] = 0;
            }
        }
        return matrix;
    }

    public static int maxVal(List<Item> items, int v) {
        int n = items.size();
        int[][] matrix = createMatrix(n, v);
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= v; w++) {
                if (items.get(i-1).weight > w) {
                    matrix[i][w] = matrix[i-1][w];
                } else {
                    matrix[i][w] = Math.max(matrix[i-1][w], items.get(i-1).value + matrix[i-1][w-items.get(i-1).weight]);
                }
            }
        }
        return matrix[n][v];
    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>(List.of(
                new Item(18, 6),
                new Item(10, 5),
                new Item(10, 5)));

        System.out.println(maxVal(items, 10));
    }
}
