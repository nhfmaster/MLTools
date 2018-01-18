package com.mltools.classify.knearestneighbor;

import com.mltools.metrics.Metric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nhfmaster on 2018/1/16.
 */
public class KDTree {
    private KDTreeNode node;
    private double[][] data;

    /**
     * constructor
     *
     * @param data two-dimensional array data
     */
    public KDTree(double[][] data) {
        this.data = data;
        if (data.length == 0)
            node = new KDTreeNode();
        else
            node = createKDTree(data);
    }

    /**
     * create KDTree
     *
     * @param remainData two-dimensional array data
     * @return KDTreeNode
     */
    private KDTreeNode createKDTree(double[][] remainData) {
        return getDataNode(node, remainData);
    }

    /**
     * find KDTree split field
     *
     * @param remainData data array
     * @return {@code n}<sup>{@code th}</sup> dimension
     */
    private int getSplitField(double[][] remainData) {
        double maxVariance = 0.0;
        int index = -1;
        int columnSize = remainData[0].length;
        for (int i = 0; i < columnSize; i++) {
            List<Double> tempList = new ArrayList<Double>();
            for (double[] aData : remainData) tempList.add(aData[i]);
            double variance = Metric.calVariance(tempList);
            if (maxVariance <= variance) {
                maxVariance = variance;
                index = i;
            }
        }
        return index;
    }

    /**
     * sort data by {@code n}<sup>{@code th}</sup> dimension
     *
     * @param list  data list
     * @param index {@code n}<sup>{@code th}</sup> dimension
     */
    private void sortBySingleDimension(List<List<Double>> list, final int index) {
        Collections.sort(list, new Comparator<List<Double>>() {
            public int compare(List<Double> list1, List<Double> list2) {
                double minus = list1.get(index) - list2.get(index);
                if (minus > 0)
                    return 1;
                else if (minus == 0)
                    return 0;
                else
                    return -1;
            }
        });
    }

    /**
     * iterate add KDTree node
     *
     * @param node       KDTreeNode
     * @param remainData two-dimensional array data
     * @return KDTreeNode
     */
    private KDTreeNode getDataNode(KDTreeNode node, double[][] remainData) {
        if (remainData.length == 0)
            return null;
        int index = getSplitField(remainData);
        List<List<Double>> dataList = new ArrayList<List<Double>>();
        for (double[] aRemainData : remainData) {
            List<Double> tempList = new ArrayList<Double>();
            for (double anARemainData : aRemainData) tempList.add(anARemainData);
            dataList.add(tempList);
        }
        sortBySingleDimension(dataList, index);
        for (int i = 0; i < dataList.size(); i++)
            for (int j = 0; j < dataList.get(i).size(); j++)
                remainData[i][j] = dataList.get(i).get(j);

        int medianIndex = dataList.size() / 2;
        List<Double> medianData = dataList.get(medianIndex);
        node = new KDTreeNode(medianData, index);
        double[][] leftRemainData = new double[medianIndex][data[0].length];
        System.arraycopy(remainData, 0, leftRemainData, 0, medianIndex);
        node.leftNode = getDataNode(node, leftRemainData);
        double[][] rightRemainData = new double[remainData.length - medianIndex - 1][data[0].length];
        System.arraycopy(remainData, medianIndex + 1, rightRemainData, 0, remainData.length - medianIndex - 1);
        node.rightNode = getDataNode(node, rightRemainData);
        return node;
    }

    public static void main(String args[]) {
        double[][] doubleArr = {{2, 3}, {5, 4}, {9, 6}, {4, 7}, {8, 1}, {7, 2}};
        KDTree kdTree = new KDTree(doubleArr);
        KDTreeNode kdTreeNode = kdTree.node;
        System.out.println(kdTreeNode);
    }
}
