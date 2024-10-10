package org.example.Lab_1;

import org.apache.commons.math3.linear.*;

import java.util.ArrayList;
import java.util.List;

public class LinearCode {
    private RealMatrix matrix;

    public LinearCode(double[][] inputMatrix) {
        this.matrix = MatrixUtils.createRealMatrix(inputMatrix);
    }

    public RealMatrix ref() {
        int rows = matrix.getRowDimension();
        int cols = matrix.getColumnDimension();
        int lead = 0;
        double[][] m = matrix.getData();

        for (int r = 0; r < rows; r++) {
            if (lead >= cols) {
                break;
            }

            int i = r;
            while (m[i][lead] == 0) {
                i++;
                if (i == rows) {
                    i = r;
                    lead++;
                    if (lead == cols) {
                        return MatrixUtils.createRealMatrix(m);
                    }
                }
            }
            double[] temp = m[i];
            m[i] = m[r];
            m[r] = temp;

            double leadValue = m[r][lead];
            if (leadValue != 0) {
                for (int j = 0; j < cols; j++) {
                    m[r][j] /= leadValue;
                }
            }

            for (i = r + 1; i < rows; i++) {
                if (m[i][lead] != 0) {
                    double factor = m[i][lead];
                    for (int j = 0; j < cols; j++) {
                        m[i][j] -= factor * m[r][j];
                    }
                }
            }
            lead++;
        }
        return MatrixUtils.createRealMatrix(m);
    }

    // Метод приведения матрицы к приведенному ступенчатому виду
    public RealMatrix rref() {
        RealMatrix refMatrix = ref();
        double[][] m = refMatrix.getData();
        int rows = refMatrix.getRowDimension();
        int cols = refMatrix.getColumnDimension();

        for (int r = rows - 1; r >= 0; r--) {
            int leadCol = -1;
            for (int j = 0; j < cols; j++) {
                if (m[r][j] == 1) {
                    leadCol = j;
                    break;
                }
            }
            if (leadCol == -1) continue;

            for (int i = 0; i < r; i++) {
                double factor = m[i][leadCol];
                for (int j = 0; j < cols; j++) {
                    m[i][j] -= factor * m[r][j];
                }
            }
        }
        return MatrixUtils.createRealMatrix(m);
    }

    // Метод для определения ведущих столбцов
    public List<Integer> getLeadColumns(RealMatrix matrix) {
        List<Integer> leadColumns = new ArrayList<>();
        double[][] data = matrix.getData();

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != 0 && !leadColumns.contains(j)) {
                    leadColumns.add(j);
                    break;
                }
            }
        }
        return leadColumns;
    }


    // Метод для формирования сокращенной матрицы X, добавляя нулевые столбцы при необходимости
    public RealMatrix formMatrixX(RealMatrix matrix, List<Integer> leadColumns) {
        int rows = matrix.getRowDimension();
        int nonLeadColCount = matrix.getColumnDimension() - leadColumns.size();

        if (nonLeadColCount <= 1) {
            System.out.println("Добавляем дополнительные нулевые столбцы к матрице X.");
            nonLeadColCount = 2;
            double[][] newData = new double[rows][nonLeadColCount];
            return MatrixUtils.createRealMatrix(newData);
        }

        double[][] newData = new double[rows][nonLeadColCount];
        int colIndex = 0;

        for (int j = 0; j < matrix.getColumnDimension(); j++) {
            if (!leadColumns.contains(j)) {
                System.out.println("Добавление столбца " + j + " в матрицу X.");
                for (int i = 0; i < rows; i++) {
                    newData[i][colIndex] = matrix.getEntry(i, j);
                }
                colIndex++;
            }
        }
        return MatrixUtils.createRealMatrix(newData);
    }

    // Метод для формирования проверочной матрицы H
    public RealMatrix formCheckMatrixH(RealMatrix X, List<Integer> leadColumns, int totalCols) {
        int rows = X.getRowDimension();
        int cols = totalCols;
        double[][] H = new double[rows][cols];

        // Заполняем X в соответствующие позиции
        int xColIndex = 0;
        for (int j = 0; j < cols; j++) {
            if (leadColumns.contains(j)) {
                for (int i = 0; i < rows; i++) {
                    H[i][j] = 0;
                }
            } else {
                for (int i = 0; i < rows; i++) {
                    H[i][j] = X.getEntry(i, xColIndex);
                }
                xColIndex++;
            }
        }

        // Заполняем единичную матрицу в оставшиеся позиции
        int identityIndex = 0;
        for (int j : leadColumns) {
            H[identityIndex][j] = 1;
            identityIndex++;
        }

        return MatrixUtils.createRealMatrix(H);
    }

    public static void printMatrix(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                System.out.print(matrix.getEntry(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        double[][] inputMatrix = {
                {1, 0, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 0, 1, 0, 0, 0},
                {1, 1, 0, 1, 1, 0},
                {0, 0, 0, 0, 1, 1}
        };

        LinearCode linearCode = new LinearCode(inputMatrix);

        System.out.println("Original Matrix:");
        printMatrix(linearCode.matrix);

        RealMatrix refMatrix = linearCode.ref();
        System.out.println("REF Matrix:");
        printMatrix(refMatrix);

        RealMatrix rrefMatrix = linearCode.rref();
        System.out.println("RREF Matrix:");
        printMatrix(rrefMatrix);

        List<Integer> leadColumns = linearCode.getLeadColumns(rrefMatrix);
        System.out.println("Lead Columns: " + leadColumns);

        RealMatrix matrixX = linearCode.formMatrixX(rrefMatrix, leadColumns);
        System.out.println("Matrix X:");
        printMatrix(matrixX);

        RealMatrix checkMatrixH = linearCode.formCheckMatrixH(matrixX, leadColumns, rrefMatrix.getColumnDimension());
        System.out.println("Check Matrix H:");
        printMatrix(checkMatrixH);
    }
}
