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
     * initialize model parameters
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
     * calculate loss value in init form
     *
     * @param dataXList feature value
     * @param dataY     true value
     * @return loss value
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
     * decide whether update parameters in init form
     *
     * @param xList feature value
     * @param label true value
     * @return true if update parameters, false otherwise
     */
    private boolean ifUpdateInitPerceptron(List<Double> xList, int label) {
        double matrixValue = calInitLossValue(xList, label);
        return matrixValue <= 0;
    }

    /**
     * update parameters in init form
     *
     * @param xList feature value
     * @param label true value
     */
    private void updateInitPerceptron(List<Double> xList, int label) {
        bias = bias + learningRate * label;
        for (int i = 0; i < weight.size(); i++) {
            double result = weight.get(i) + learningRate * label * xList.get(i);
            weight.set(i, result);
        }
    }

    /**
     * train perceptron in dual form
     *
     * @param dataXList feature value
     * @param dataYList true value
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
     * train perceptron in init form
     *
     * @param dataXList feature value
     * @param dataYList true value
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
     * update parameters in dual form
     *
     * @param label true value
     * @param i     number of data
     */
    private void updateDualPerceptron(int label, int i) {
        weight.set(i, weight.get(i) + learningRate);
        bias += label * learningRate;
    }

    /**
     * decide whether update parameters in dual form
     *
     * @param dataYList  true value
     * @param gramMatrix gram matrix
     * @return true if update parameters, false otherwise
     */
    private boolean ifUpdateDualPerceptron(List<Integer> dataYList, List<List<Double>> gramMatrix, int i) {
        double matrixValue = calDualLossValue(dataYList, gramMatrix, i);
        return matrixValue <= 0;
    }

    /**
     * calculate loss value in dual form
     *
     * @param dataYList  true value
     * @param gramMatrix gram matrix
     * @param i          number of data
     * @return loss value
     */
    private double calDualLossValue(List<Integer> dataYList, List<List<Double>> gramMatrix, int i) {
        double tempSum = 0.0;
        for (int j = 0; j < weight.size(); j++)
            tempSum += weight.get(j) * dataYList.get(j) * gramMatrix.get(j).get(i);
        return dataYList.get(i) * (tempSum + bias);
    }

    /**
     * calculate gram matrix
     *
     * @param dataXList feature value
     * @return gram matrix
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
     * train perceptron
     *
     * @param dataXList feature value
     * @param dataYList true value
     */
    public void train(List<List<Double>> dataXList, List<Integer> dataYList) {
        initPerceptron(dataXList);
        if (!this.dual)
            initFormTrain(dataXList, dataYList);
        else
            dualFormTrain(dataXList, dataYList);
    }

    /**
     * predict with perceptron
     *
     * @param dataXList feature value
     * @return predict value
     */
    public List<Double> predict(List<List<Double>> dataXList) {
        List<Double> predictList = new ArrayList<Double>();
        for (List<Double> xList : dataXList) {
            double result = 0.0;
            for (int i = 0; i < xList.size(); i++)
                result += xList.get(i) * weight.get(i);
            result += bias;
            predictList.add(result);
        }
        return predictList;
    }
}
