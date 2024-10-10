package org.example.Lab_1;

import org.apache.commons.math3.linear.*;

public class MatrixREF {

    public static void main(String[] args) {
        System.out.println("\n--- TASK 1.1 and 1.2 ---");

//        int[][] originMatrix = {
//                {1, 0, 1, 0, 0, 0},
//                {0, 1, 1, 0, 1, 1},
//                {0, 0, 0, 1, 0, 0},
//                {0, 0, 0, 0, 1, 1},
//                {0, 0, 0, 0, 0, 1}
//        };

//        int[][] originMatrix = {
//                {1, 2, 3},
//                {4, 5, 6},
//                {7, 8, 9}
//        };

        int[][] originMatrix = {
                {1, 0, 1, 0, 0, 0},
                {0, 1, 1, 0, 1, 1},
                {0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1, 1},
                {0, 0, 0, 0, 0, 1}
        };


        System.out.println("\nOriginal Matrix:");
        printMatrix(originMatrix);

        int[][] refMatrix = ref(originMatrix);

        System.out.println("REF Matrix:");
        printMatrix(refMatrix);

        int[][] rrefMatrix = rref(originMatrix);

        System.out.println("RREF Matrix:");
        printMatrix(rrefMatrix);
    }

    public static int[][] ref(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int lead = 0;
        for (int r = 0; r < rows; r++) {
            if (cols <= lead) return matrix;

            int i = r;
            while (matrix[i][lead] == 0) {
                i++;
                if (rows == i) {
                    i = r;
                    lead++;
                    if (cols == lead) return matrix;
                }
            }

            // Меняем строки
            int[] temp = matrix[i];
            matrix[i] = matrix[r];
            matrix[r] = temp;

            // Нормализуем ведущий элемент
            int leadValue = matrix[r][lead];
            if (leadValue != 0) {
                for (int j = 0; j < cols; j++) {
                    matrix[r][j] /= leadValue;
                }
            }

            // Обрабатываем строки ниже текущей
            for (i = r + 1; i < rows; i++) {
                if (matrix[i][lead] != 0) {
                    int factor = matrix[i][lead];
                    for (int j = 0; j < cols; j++) {
                        matrix[i][j] ^= matrix[r][j] * factor;
                    }
                }
            }

            lead++;
        }
        return matrix;
    }

    public static int[][] rref(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int lead = 0;
        for (int r = 0; r < rows; r++) {
            if (cols <= lead) return matrix;

            int i = r;
            while (matrix[i][lead] == 0) {
                i++;
                if (rows == i) {
                    i = r;
                    lead++;
                    if (cols == lead) return matrix;
                }
            }

            int[] temp = matrix[i];
            matrix[i] = matrix[r];
            matrix[r] = temp;

            if (matrix[r][lead] != 0) {
                for (int j = 0; j < cols; j++) {
                    matrix[r][j] /= matrix[r][lead];
                }
            }

            for (i = 0; i < rows; i++) {
                if (i != r && matrix[i][lead] != 0) {
                    int factor = matrix[i][lead];
                    for (int j = 0; j < cols; j++) {
                        matrix[i][j] ^= matrix[r][j] * factor;
                    }
                }
            }

            lead++;
        }

        for (int r = rows - 1; r >= 0; r--) {
            for (int leadCol = 0; leadCol < cols; leadCol++) {
                if (matrix[r][leadCol] == 1) {
                    for (int i = 0; i < r; i++) {
                        if (matrix[i][leadCol] != 0) {
                            int factor = matrix[i][leadCol];
                            for (int j = 0; j < cols; j++) {
                                matrix[i][j] ^= matrix[r][j] * factor;
                            }
                        }
                    }
                    break;
                }
            }
        }

        return matrix;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}


