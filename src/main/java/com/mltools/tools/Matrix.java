package com.mltools.tools;

import java.util.List;

/**
 * Created by nhfmaster on 2017/12/13.
 */
public class Matrix {
    private double[][] array;

    public Matrix(double[][] array) {
        this.array = array;
    }

    public Matrix(List<List<Double>> dataList) {
        int rowSize = dataList.size();
        int column = dataList.get(0).size();
        array = new double[rowSize][column];
        for (int i = 0; i < dataList.size(); i++) {
            List<Double> list = dataList.get(i);
            for (int j = 0; j < list.size(); i++) {
                double value = list.get(j);
                array[i][j] = value;
            }
        }
    }

    public void set(int row, int column, double value) {
        array[row][column] = value;
    }

    public double get(int row, int column) {
        return array[row][column];
    }

    public Matrix plus(Matrix matrix) {
        double[][] resultArr = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[i].length; j++)
                resultArr[i][j] = array[i][j] + matrix.array[i][j];

        return new Matrix(resultArr);
    }

    public Matrix minus(Matrix matrix) {
        double[][] resultArr = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[i].length; j++)
                resultArr[i][j] = array[i][j] - matrix.array[i][j];

        return new Matrix(resultArr);
    }

    public Matrix multiply(Matrix matrix) {
        double[][] resultArr = new double[array.length][matrix.array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < matrix.array[i].length; j++) {
                double sum = 0.0;
                for (int k = 0; k < array[i].length; k++)
                    sum += array[i][k] * matrix.array[k][j];
                resultArr[i][j] = sum;
            }
        }
        return new Matrix(resultArr);
    }

    public Matrix multiply(double times) {
        double[][] resultArr = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[i].length; j++)
                resultArr[i][j] = array[i][j] * times;

        return new Matrix(resultArr);
    }

    public Matrix transpose() {
        double[][] resultArr = new double[array[0].length][array.length];
        for (int i = 0; i < resultArr.length; i++)
            for (int j = 0; j < resultArr[i].length; j++)
                resultArr[i][j] = array[j][i];

        return new Matrix(resultArr);
    }

    public Matrix reverse() {
        double[][] resultArr = new double[array.length][array[0].length];
        return new Matrix(resultArr);
    }
}
