package com.mltools.metrics;

import java.util.List;

/**
 * Created by nhfmaster on 2017/12/5.
 */
public class RegressionMetric {
    /**
     * calculate explained variance
     *
     * @param predictList predicted value
     * @param trueList    true value
     */
    public static void calExplainedVariance(List<Double> predictList, List<Double> trueList) {
        double trueVariance = Metric.calVariance(trueList);
        List<Double> minusList = Metric.getMinusList(predictList, trueList);
        double minusVariance = Metric.calVariance(minusList);
        double explainedVariance = 1 - minusVariance / trueVariance;
        System.out.println("Explained variance: " + explainedVariance);
    }

    /**
     * calculate Mean absolute error
     *
     * @param predictList predicted value
     * @param trueList    true value
     */
    public static void calMeanAbsoluteError(List<Double> predictList, List<Double> trueList) {
        double error = 0.0;
        for (int i = 0; i < trueList.size(); i++)
            error += Math.abs(trueList.get(i) - predictList.get(i));
        error = error / trueList.size();
        System.out.println("Mean absolute error: " + error);
    }

    /**
     * calculate Mean squared error
     *
     * @param predictList predicted value
     * @param trueList    true value
     */
    public static void calMeanSquaredError(List<Double> predictList, List<Double> trueList) {
        double error = 0.0;
        for (int i = 0; i < trueList.size(); i++)
            error += Math.pow(trueList.get(i) - predictList.get(i), 2);
        error = error / trueList.size();
        System.out.println("Mean squared error: " + error);
    }

    /**
     * calculate Mean squared logarithmic error
     *
     * @param predictList predicted value
     * @param trueList    true value
     */
    public static void calMeanSquaredLogarithmicError(List<Double> predictList, List<Double> trueList) {
        List<Double> predictLogEList = Metric.getLogEList(predictList);
        List<Double> trueLogEList = Metric.getLogEList(trueList);
        double error = 0.0;
        for (int i = 0; i < trueLogEList.size(); i++)
            error += Math.pow(trueLogEList.get(i) - predictLogEList.get(i), 2);
        error = error / trueLogEList.size();
        System.out.println("Mean squared logarithmic error: " + error);
    }

    /**
     * calculate Median absolute error
     *
     * @param predictList predicted value
     * @param trueList    true value
     */
    public static void calMedianAbsoluteError(List<Double> predictList, List<Double> trueList) {
        List<Double> minusAbsList = Metric.getMinusAbsList(predictList, trueList);
        double median = Metric.getMedian(minusAbsList);
        System.out.println("Median absolute error: " + median);
    }

    /**
     * calculate R2 score
     *
     * @param predictList predicted value
     * @param trueList    true value
     */
    public static void calR2Score(List<Double> predictList, List<Double> trueList) {
        double trueAverage = Metric.calAverage(trueList);
        double numerator = 0.0;
        double denominator = 0.0;
        for (int i = 0; i < trueList.size(); i++) {
            denominator += Math.pow(trueList.get(i) - trueAverage, 2);
            numerator += Math.pow(trueList.get(i) - predictList.get(i), 2);
        }
        double r2Score = 1 - numerator / denominator;
        System.out.println("R2 Score: " + r2Score);
    }
}
