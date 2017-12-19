package com.mltools.classify.logisticregression;

import com.mltools.classify.Classifier;

/**
 * Created by nhfmaster on 2017/11/4.
 */
public class LogisticRegression extends Classifier {
    private boolean dual;
    private String penalty;
    private double tolerance;
    private double penaltyStrength;
    private String optimizer;
    private int maxIteration;

    public static class Builder extends Classifier.Builder<LogisticRegression.Builder> {
        private String penalty = "l2";
        private boolean dual = false;
        private double tolerance = 0.0001;
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

        public Builder setTolerance(double tolerance) {
            this.tolerance = tolerance;
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
        this.tolerance = builder.tolerance;
        this.penaltyStrength = builder.penaltyStrength;
        this.optimizer = builder.optimizer;
        this.maxIteration = builder.maxIteration;
    }

    public void train() {

    }

    public void predict() {

    }
}
