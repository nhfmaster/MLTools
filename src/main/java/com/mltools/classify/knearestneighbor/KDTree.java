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
    public KDTreeNode node;
    private List<KDTreeData> kdTreeDataList;

    /**
     * constructor
     *
     * @param kdTreeDataList training data list
     */
    public KDTree(List<KDTreeData> kdTreeDataList) {
        this.kdTreeDataList = kdTreeDataList;
        if (kdTreeDataList.size() == 0)
            node = new KDTreeNode();
        else
            node = createKDTree();
    }

    /**
     * create KDTree
     *
     * @return KDTreeNode
     */
    private KDTreeNode createKDTree() {
        return getDataNode(node, kdTreeDataList);
    }

    /**
     * find KDTree split field
     *
     * @param kdTreeDataList training data list
     * @return {@code n}<sup>{@code th}</sup> dimension
     */
    private int getSplitField(List<KDTreeData> kdTreeDataList) {
        double maxVariance = 0.0;
        int index = -1;
        int columnSize = kdTreeDataList.get(0).dataX.size();
        for (int i = 0; i < columnSize; i++) {
            List<Double> tempList = new ArrayList<Double>();
            for (KDTreeData kdTreeData : kdTreeDataList) tempList.add(kdTreeData.dataX.get(i));
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
     * @param kdTreeDataList training data list
     * @param index          {@code n}<sup>{@code th}</sup> dimension
     */
    private void sortBySingleDimension(List<KDTreeData> kdTreeDataList, final int index) {
        Collections.sort(kdTreeDataList, new Comparator<KDTreeData>() {
            public int compare(KDTreeData o1, KDTreeData o2) {
                double minus = o1.dataX.get(index) - o2.dataX.get(index);
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
     * @param node           KDTreeNode
     * @param kdTreeDataList training data list
     * @return KDTreeNode
     */
    private KDTreeNode getDataNode(KDTreeNode node, List<KDTreeData> kdTreeDataList) {
        if (kdTreeDataList.size() == 0)
            return null;
        int index = getSplitField(kdTreeDataList);
        sortBySingleDimension(kdTreeDataList, index);
        int medianIndex = kdTreeDataList.size() / 2;
        node = new KDTreeNode(kdTreeDataList.get(medianIndex), index);
        List<KDTreeData> leftRemainList = new ArrayList<KDTreeData>();
        leftRemainList.addAll(kdTreeDataList.subList(0, medianIndex));
        node.leftNode = getDataNode(node, leftRemainList);
        if (node.leftNode != null)
            node.leftNode.parentNode = node;
        List<KDTreeData> rightRemainList = new ArrayList<KDTreeData>();
        rightRemainList.addAll(kdTreeDataList.subList(medianIndex + 1, kdTreeDataList.size()));
        node.rightNode = getDataNode(node, rightRemainList);
        if (node.rightNode != null)
            node.rightNode.parentNode = node;
        return node;
    }

    public static void main(String args[]) {
        List<KDTreeData> allList = new ArrayList<KDTreeData>();
        double[][] doubleArr = {{2, 3}, {5, 4}, {9, 6}, {4, 7}, {8, 1}, {7, 2}};
        for (int i = 0; i < doubleArr.length; i++) {
            List<Double> list = new ArrayList<Double>();
            for (int j = 0; j < doubleArr[i].length; j++) {
                list.add(doubleArr[i][j]);
            }
            KDTreeData kdTreeData = new KDTreeData(list, 1);
            allList.add(kdTreeData);
        }
        KDTree kdTree = new KDTree(allList);
        KDTreeNode kdTreeNode = kdTree.node;
        System.out.println(kdTreeNode);
    }
}
