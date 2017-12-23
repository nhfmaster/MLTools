package com.mltools.classify.logisticregression;

import com.mltools.classify.Classifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhfmaster on 2017/11/4.
 */
public class LogisticRegression extends Classifier {
    private boolean dual; // dual form
    private String penalty; // penalization function
    private double threshold; // threshold for stopping algorithm
    private double penaltyStrength; // strength of regularization
    private String optimizer; // optimization algorithm
    private int maxIteration; // max number of iterations
    private double learningRate;
    private List<Double> parameterList = new ArrayList<Double>();

    public static class Builder extends Classifier.Builder<LogisticRegression.Builder> {
        private double learningRate = 0.01;
        private String penalty = "l2";
        private boolean dual = false;
        private double threshold = 0.0001;
        private double penaltyStrength = 1.0;
        private String optimizer = "liblinear";
        private int maxIteration = 100;

        public Builder() {
        }

        public Builder setDual(boolean dual) {
            this.dual = dual;
            return this;
        }

        public Builder setPenalty(String penalty) {
            this.penalty = penalty;
            return this;
        }

        public Builder setThreshold(double threshold) {
            this.threshold = threshold;
            return this;
        }

        public Builder setPenaltyStrength(double penaltyStrength) {
            this.penaltyStrength = penaltyStrength;
            return this;
        }

        public Builder setOptimizer(String optimizer) {
            this.optimizer = optimizer;
            return this;
        }

        public Builder setMaxIteration(int maxIteration) {
            this.maxIteration = maxIteration;
            return this;
        }

        public LogisticRegression build() {
            return new LogisticRegression(this);
        }
    }

    LogisticRegression(Builder builder) {
        super(builder);
        this.dual = builder.dual;
        this.penalty = builder.penalty;
        this.threshold = builder.threshold;
        this.penaltyStrength = builder.penaltyStrength;
        this.optimizer = builder.optimizer;
        this.maxIteration = builder.maxIteration;
    }

    private double sigmoid(List<Double> parameterList, List<Double> xList) {
        double linearResult = 0.0;
        for (int i = 0; i < xList.size(); i++)
            linearResult += xList.get(i) * parameterList.get(i);
        return 1 / (1 + Math.pow(Math.E, -linearResult));
    }

    private void initParameter(List<List<Double>> dataXList) {
        for (int i = 0; i < dataXList.get(0).size(); i++)
            parameterList.add(1.0);
    }

    private double updateParameter(List<List<Double>> dataXList, List<Integer> dataYList) {
        int dataSum = dataXList.size();
        for (int i = 0; i < parameterList.size(); i++) {
            double parameter = parameterList.get(i);
            double dataCalUpdate = 0.0;
            for (int j = 0; j < dataXList.size(); j++) {
                List<Double> xList = dataXList.get(j);
                int label = dataYList.get(j);
                dataCalUpdate += xList.get(i) * (sigmoid(parameterList, xList) - label);
            }
            parameter = parameter - learningRate * (double) 1 / dataSum * dataCalUpdate
                    - penaltyStrength / dataSum * parameter;
            parameterList.set(i, parameter);
        }
        return calLossValue(dataXList, dataYList);
    }

    private double calLossValue(List<List<Double>> dataXList, List<Integer> dataYList) {
        double loss = 0.0;
        for (int i = 0; i < dataXList.size(); i++) {
            int label = dataYList.get(i);
            List<Double> xList = dataXList.get(i);
            double sigmoidResult = sigmoid(parameterList, xList);
            loss += -label * Math.log(sigmoidResult) -
                    (1 - label) * Math.log(1 - sigmoidResult);
        }
        return loss;
    }

    public void train(List<List<Double>> dataXList, List<Integer> dataYList) {
        int count = 0;
        initParameter(dataXList);
        while (count <= maxIteration) {
            double loss = updateParameter(dataXList, dataYList);
            if (loss <= threshold)
                break;
        }
    }

    public List<Double> predict(List<List<Double>> dataXList) {
        List<Double> predictList = new ArrayList<Double>();
        for (int i = 0; i < dataXList.size(); i++) {
            List<Double> xList = dataXList.get(i);
            double sigmoidResult = sigmoid(parameterList, xList) >= 0.5 ? 1.0 : 0.0;
            predictList.add(sigmoidResult);
        }
        return predictList;
    }


    public static void main(String args[]) {
        LogisticRegression lr = new LogisticRegression.Builder().build();
    }
}
