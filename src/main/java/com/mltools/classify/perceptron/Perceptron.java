package com.mltools.classify.perceptron;

import com.mltools.classify.Classifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhfmaster on 2017/2/19.
 */
public class Perceptron extends Classifier {
    private static final double NORMAL_INIT_VALUE = 1.0; // init form default value
    private static final double DUAL_INIT_VALUE = 0.0; // dual form default value
    private double bias = 0.0; // bias
    private List<Double> weight = new ArrayList<Double>(); // weight
    private boolean dual; // dual form
    private double learningRate; // learning rate

    public static class Builder extends Classifier.Builder<Builder> {
        private boolean dual = true;
        private double learningRate = 0.001;

        public Builder() {
        }

        public Builder setDual(boolean dual) {
            this.dual = dual;
            return this;
        }

        public Builder setLearningRate(double learningRate) {
            this.learningRate = learningRate;
            return this;
        }

        public Perceptron build() {
            return new Perceptron(this);
        }
    }

    Perceptron(Builder builder) {
        super(builder);
        this.dual = builder.dual;
        this.learningRate = builder.learningRate;
    }

    /**
     * 初始化模型参数
     */
    private void initPerceptron(List<List<Double>> dataXList) {
        if (!this.dual) {
            int dimension = dataXList.iterator().next().size();
            for (int i = 0; i < dimension; i++)
                this.weight.add(NORMAL_INIT_VALUE);
        } else {
            int dimension = dataXList.size();
            for (int i = 0; i < dimension; i++)
                this.weight.add(DUAL_INIT_VALUE);
        }
    }

    /**
     * 计算初始形式损失函数数值
     *
     * @param dataXList 数据X的List
     * @param dataY     数据对应Y数值
     * @return 初始形式损失函数数值
     */
    private double calInitLossValue(List<Double> dataXList, int dataY) {
        double matrixValue = 0.0;
        for (int i = 0; i < weight.size(); i++)
            for (int j = 0; j < dataXList.size(); j++)
                matrixValue += weight.get(i) * dataXList.get(i);
        matrixValue += bias;
        matrixValue = dataY * matrixValue;
        return matrixValue;
    }

    /**
     * 判断是否需要更新初始形式模型参数
     *
     * @param xList
     * @param label
     * @return
     */
    private boolean ifUpdateInitPerceptron(List<Double> xList, int label) {
        double matrixValue = calInitLossValue(xList, label);
        return matrixValue <= 0;
    }

    /**
     * 更新初始形式模型参数
     *
     * @param xList
     * @param label
     */
    private void updateInitPerceptron(List<Double> xList, int label) {
        bias = bias + learningRate * label;
        for (int i = 0; i < weight.size(); i++) {
            double result = weight.get(i) + learningRate * label * xList.get(i);
            weight.set(i, result);
        }
    }

    /**
     * 使用对偶形式训练感知机
     */
    private void dualFormTrain(List<List<Double>> dataXList, List<Integer> dataYList) {
        List<List<Double>> gramMatrix = calGramMatrix(dataXList);
        for (int i = 0; i < dataXList.size(); i++) {
            int label = dataYList.get(i);
            boolean updateFlag = ifUpdateDualPerceptron(dataYList, gramMatrix, i);
            if (updateFlag) {
                updateDualPerceptron(label, i);
                i = 0;
            }
        }
    }

    /**
     * 使用原始形式训练感知机
     */
    private void initFormTrain(List<List<Double>> dataXList, List<Integer> dataYList) {
        for (int i = 0; i < dataXList.size(); i++) {
            List<Double> xList = dataXList.get(i);
            int label = dataYList.get(i);
            boolean updateFlag = ifUpdateInitPerceptron(xList, label);
            if (updateFlag) {
                updateInitPerceptron(xList, label);
                i = 0;
            }
        }
    }

    /**
     * 更新对偶形式模型参数
     *
     * @param label
     * @param i
     */
    private void updateDualPerceptron(int label, int i) {
        weight.set(i, weight.get(i) + learningRate);
        bias += label * learningRate;
    }

    /**
     * 判断是否需要更新对偶形式模型参数
     *
     * @param gramMatrix gram矩阵
     * @return 更新 true；不更新 false
     */
    private boolean ifUpdateDualPerceptron(List<Integer> dataYList, List<List<Double>> gramMatrix, int i) {
        double matrixValue = calDualLossValue(dataYList, gramMatrix, i);
        return matrixValue <= 0;
    }

    private double calDualLossValue(List<Integer> dataYList, List<List<Double>> gramMatrix, int i) {
        double tempSum = 0.0;
        for (int j = 0; j < weight.size(); j++)
            tempSum += weight.get(j) * dataYList.get(j) * gramMatrix.get(j).get(i);
        return dataYList.get(i) * (tempSum + bias);
    }

//    /**
//     * 获得y的数据
//     *
//     * @return y数据的List
//     */
//    private List<Integer> getDataYList() {
//        List<Integer> resultList = new ArrayList<Integer>();
//        for (Map<List<Double>, Integer> map : dataList)
//            for (Map.Entry<List<Double>, Integer> entry : map.entrySet())
//                resultList.add(entry.getValue());
//        return resultList;
//    }
//
//
//    /**
//     * 获得x的数据
//     *
//     * @return x数据的List
//     */
//    private List<List<Double>> getDataXList() {
//        List<List<Double>> resultList = new ArrayList<List<Double>>();
//        for (Map<List<Double>, Integer> map : dataList)
//            for (Map.Entry<List<Double>, Integer> entry : map.entrySet())
//                resultList.add(entry.getKey());
//
//        return resultList;
//    }

    /**
     * 计算gram矩阵
     *
     * @return gram矩阵
     */
    private List<List<Double>> calGramMatrix(List<List<Double>> dataXList) {
        List<List<Double>> resultList = new ArrayList<List<Double>>();
        for (List<Double> beforeList : dataXList) {
            List<Double> tempList = new ArrayList<Double>();
            for (List<Double> behindList : dataXList) {
                double tempMul = 0.0;
                for (int i = 0; i < beforeList.size(); i++)
                    tempMul += beforeList.get(i) * behindList.get(i);
                tempList.add(tempMul);
            }
            resultList.add(tempList);
        }
        return resultList;
    }

    /**
     * 训练感知机
     */
    private void train(List<List<Double>> dataXList, List<Integer> dataYList) {
        initPerceptron(dataXList);
        if (!this.dual)
            initFormTrain(dataXList, dataYList);
        else
            dualFormTrain(dataXList, dataYList);
    }

    public static void main(String args[]) {
        Perceptron perceptron = new Perceptron.Builder().setLearningRate(100).build();
    }
}
