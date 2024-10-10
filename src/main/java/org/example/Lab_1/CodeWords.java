package org.example.Lab_1;

import org.apache.commons.math3.linear.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CodeWords {

    // Метод для генерации всех кодовых слов из порождающей матрицы G
    public static Set<String> generateCodeWordsByAddingRows(RealMatrix G) {
        int rows = G.getRowDimension();
        int cols = G.getColumnDimension();
        Set<String> codeWords = new HashSet<>();

        // Перебираем все комбинации строк
        int totalCombinations = (1 << rows);
        for (int i = 0; i < totalCombinations; i++) {
            double[] sum = new double[cols];
            for (int j = 0; j < rows; j++) {
                if ((i & (1 << j)) != 0) {
                    for (int k = 0; k < cols; k++) {
                        sum[k] = (sum[k] + G.getEntry(j, k)) % 2;
                    }
                }
            }
            codeWords.add(arrayToString(sum));
        }
        return codeWords;
    }

    // Метод для умножения всех двоичных слов длины k на матрицу G
    public static Set<String> generateCodeWordsByMultiplying(RealMatrix G, int k) {
        int cols = G.getColumnDimension();
        Set<String> codeWords = new HashSet<>();

        int totalVectors = (1 << k);
        for (int i = 0; i < totalVectors; i++) {
            double[] binaryVector = new double[k];
            for (int j = 0; j < k; j++) {
                binaryVector[j] = (i >> j) & 1;
            }
            double[] result = new double[cols];
            for (int j = 0; j < k; j++) {
                if (binaryVector[j] == 1) {
                    for (int m = 0; m < cols; m++) {
                        result[m] = (result[m] + G.getEntry(j, m)) % 2;
                    }
                }
            }
            codeWords.add(arrayToString(result));
        }
        return codeWords;
    }

    // Метод для вычисления минимального расстояния Хэмминга между кодовыми словами
    public static int calculateMinimumHammingDistance(Set<String> codeWords) {
        List<String> wordList = new ArrayList<>(codeWords);
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < wordList.size(); i++) {
            for (int j = i + 1; j < wordList.size(); j++) {
                int distance = hammingDistance(wordList.get(i), wordList.get(j));
                minDistance = Math.min(minDistance, distance);
            }
        }
        return minDistance;
    }

    // Метод для расчета расстояния Хэмминга
    private static int hammingDistance(String a, String b) {
        int distance = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    // Вспомогательный метод для преобразования массива в строку
    private static String arrayToString(double[] array) {
        StringBuilder sb = new StringBuilder();
        for (double value : array) {
            sb.append((int) value);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        double[][] GArray = {
                {1, 1, 0, 0, 1, 0},
                {0, 1, 1, 0, 0, 1},
                {1, 0, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 1}
        };


        RealMatrix G = MatrixUtils.createRealMatrix(GArray);

        Set<String> codeWordsByAdding = generateCodeWordsByAddingRows(G);
        Set<String> codeWordsByMultiplying = generateCodeWordsByMultiplying(G, G.getRowDimension());

        System.out.println("Кодовые слова, полученные сложением строк G:");
        codeWordsByAdding.forEach(System.out::println);

        System.out.println("\nКодовые слова, полученные умножением на G:");
        codeWordsByMultiplying.forEach(System.out::println);

        int d = calculateMinimumHammingDistance(codeWordsByAdding);
        System.out.println("\nМинимальное кодовое расстояние d = " + d);
        System.out.println("Кратность обнаруживаемой ошибки t = " + (d - 1) / 2);
    }
}
