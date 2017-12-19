package com.mltools.classify;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */
public class Classifier implements Serializable {
    private boolean dual = true; // dual form
    private double learningRate; // learning rate
    private String penalty;
    private double tolerance;
    private double penaltyStrength;
    private String optimizer;
    private int maxIteration;

    public static class Builder<T extends Builder<T>> {
        private boolean dual;
        private double learningRate;
        private String penalty;
        private double tolerance;
        private double penaltyStrength;
        private String optimizer;
        private int maxIteration;

        public Builder() {
        }

        public Builder<T> setDual(boolean dual) {
            this.dual = dual;
            return this;
        }

        public Builder<T> setLearningRate(double learningRate) {
            this.learningRate = learningRate;
            return this;
        }

        public Builder<T> setPenalty(String penalty) {
            this.penalty = penalty;
            return this;
        }

        public Builder<T> setTolerance(double tolerance) {
            this.tolerance = tolerance;
            return this;
        }

        public Builder<T> setPenaltyStrength(double penaltyStrength) {
            this.penaltyStrength = penaltyStrength;
            return this;
        }

        public Builder<T> setOptimizer(String optimizer) {
            this.optimizer = optimizer;
            return this;
        }

        public Builder<T> setMaxIteration(int maxIteration) {
            this.maxIteration = maxIteration;
            return this;
        }

        public Classifier build() {
            return new Classifier(this);
        }
    }

    protected Classifier(Builder<?> builder) {
        this.dual = builder.dual;
        this.learningRate = builder.learningRate;
    }

    void train(List<List<Double>> dataXList, List<Integer> dataYList) {
    }


    void predict(List<List<Double>> dataXList) {
    }

}
