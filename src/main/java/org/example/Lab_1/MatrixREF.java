package org.example.Lab_1;

import org.apache.commons.math3.linear.*;

public class MatrixREF {

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 0, 1, 0, 0, 0},
                {0, 1, 1, 0, 1, 1},
                {0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1, 1},
                {0, 0, 0, 0, 0, 1}
        };

        int[][] refMatrix = ref(matrix);

        System.out.println("REF matrix:");
        printMatrix(refMatrix);
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

            int[] temp = matrix[i];
            matrix[i] = matrix[r];
            matrix[r] = temp;

            int leadValue = matrix[r][lead];
            if (leadValue != 0) {
                for (int j = 0; j < cols; j++) {
                    matrix[r][j] /= leadValue;
                }
            }

            for (i = 0; i < rows; i++) {
                if (i != r && matrix[i][lead] != 0) {
                    for (int j = 0; j < cols; j++) {
                        matrix[i][j] ^= matrix[r][j];
                    }
                }
            }
            lead++;
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
    }
}


