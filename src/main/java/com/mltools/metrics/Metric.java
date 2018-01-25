package com.mltools.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nhfmaster on 2017/12/8.
 */
public class Metric {
    /**
     * calculate data average(mean)
     *
     * @param dataList data
     * @return average(mean) value
     */
    public static double calAverage(List<Double> dataList) {
        double sum = 0.0;
        for (double data : dataList)
            sum += data;
        return sum / dataList.size();
    }

    /**
     * calculate data variance
     *
     * @param dataList data
     * @return variance value
     */
    public static double calVariance(List<Double> dataList) {
        double average = calAverage(dataList);
        double variance = 0.0;
        for (double data : dataList)
            variance += Math.pow(data - average, 2);
        return variance / dataList.size();
    }

    /**
     * calculate loge(1+data)
     *
     * @param dataList data
     * @return loge calculate list
     */
    public static List<Double> getLogEList(List<Double> dataList) {
        List<Double> logEList = new ArrayList<Double>();
        for (double data : dataList)
            logEList.add(Math.log(1.0 + data));
        return logEList;
    }

    /**
     * calculate true value minus predicted value
     *
     * @param predictList predicted value
     * @param trueList    true value
     * @return list of true value minus predicted value
     */
    public static List<Double> getMinusList(List<Double> predictList, List<Double> trueList) {
        List<Double> minusList = new ArrayList<Double>();
        for (int i = 0; i < trueList.size(); i++)
            minusList.add(trueList.get(i) - predictList.get(i));
        return minusList;
    }

    /**
     * calculate abs of true value minus predicted value
     *
     * @param predictList predicted value
     * @param trueList    true value
     * @return list of abs of true value minus predicted value
     */
    public static List<Double> getMinusAbsList(List<Double> predictList, List<Double> trueList) {
        List<Double> minusList = new ArrayList<Double>();
        for (int i = 0; i < trueList.size(); i++)
            minusList.add(Math.abs(trueList.get(i) - predictList.get(i)));
        return minusList;
    }

    /**
     * calculate median
     *
     * @param list data
     * @return median
     */
    public static double getMedian(List<Double> list) {
        if (list.size() == 0)
            return 0;
        Collections.sort(list);
        int size = list.size();
        if (size % 2 == 0)
            return (list.get(size / 2 - 1) + list.get(size / 2)) / 2;
        else
            return list.get((size + 1) / 2 - 1);
    }

    /**
     * calculate Minkowski Distance
     *
     * @param list1 data 1
     * @param list2 data 2
     * @param p     param
     * @return Minkowski Distance
     */
    public static double calMinkowskiDistance(List<Double> list1, List<Double> list2, int p) {
        double sum = 0.0;
        for (int i = 0; i < list1.size(); i++) {
            double num1 = list1.get(i);
            double num2 = list2.get(i);
            sum += Math.pow(Math.abs(num1 - num2), p);
        }
        sum = Math.pow(sum, (double) 1 / p);
        return sum;
    }
}
