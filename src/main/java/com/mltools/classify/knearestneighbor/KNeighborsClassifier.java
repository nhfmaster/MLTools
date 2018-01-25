package com.mltools.classify.knearestneighbor;

import com.mltools.classify.Classifier;
import com.mltools.metrics.Metric;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhfmaster on 2018/1/23.
 */
public class KNeighborsClassifier extends Classifier {
    private int neighbourNum; // Number of neighbours
    private String metric; // distance metric
    private KDTree kdTree;

    public static class Builder extends Classifier.Builder<KNeighborsClassifier.Builder> {
        private int neighbourNum = 5;
        private String metric = "minkowski";

        public Builder() {
        }

        public Builder setNeighbourNum(int neighbourNum) {
            this.neighbourNum = neighbourNum;
            return this;
        }

        public Builder setMetric(String metric) {
            this.metric = metric;
            return this;
        }

        public KNeighborsClassifier build() {
            return new KNeighborsClassifier(this);
        }
    }

    KNeighborsClassifier(Builder builder) {
        super(builder);
        this.neighbourNum = builder.neighbourNum;
        this.metric = builder.metric;
    }

    public void train(List<List<Double>> dataXList, List<Integer> dataYList) {
        List<KDTreeData> kdTreeDataList = new ArrayList<KDTreeData>();
        for (int i = 0; i < dataXList.size(); i++) {
            KDTreeData kdTreeData = new KDTreeData(dataXList.get(i), dataYList.get(i));
            kdTreeDataList.add(kdTreeData);
        }
        kdTree = new KDTree(kdTreeDataList);
    }

    public List<KDTreeNode> getSearchPath(List<Double> xList) {
        List<KDTreeNode> kdTreeNodeList = new ArrayList<KDTreeNode>();
        traverse(kdTree.node, kdTreeNodeList, xList);
        return kdTreeNodeList;
    }

    private void traverse(KDTreeNode traverseNode, List<KDTreeNode> kdTreeNodeList, List<Double> xList) {
        if (traverseNode != null) {
            kdTreeNodeList.add(traverseNode);
            KDTreeData kdTreeData = traverseNode.kdTreeData;
            int split = traverseNode.split;
            double kdTreeNum = kdTreeData.dataX.get(split);
            double xNum = xList.get(split);
            if (kdTreeNum > xNum) {
                traverse(traverseNode.leftNode, kdTreeNodeList, xList);
            } else {
                traverse(traverseNode.rightNode, kdTreeNodeList, xList);
            }
        }
    }

    private KDTreeNode backtrackSearch(List<KDTreeNode> searchPathList, List<Double> xList) {
//        List<KDTreeNode> nearestList = new ArrayList<KDTreeNode>();
        KDTreeNode nearestNode = null;
        if (searchPathList.size() == 0)
            return nearestNode;
        KDTreeNode lastNode = searchPathList.get(searchPathList.size() - 1);
        nearestNode = lastNode;
        double distance = Metric.calMinkowskiDistance(xList, lastNode.kdTreeData.dataX, 2);
        while (searchPathList.size() > 0) {
            KDTreeNode backTrackNode = searchPathList.get(searchPathList.size() - 1);
            searchPathList.remove(searchPathList.size() - 1);
            if (backTrackNode.leftNode == null && backTrackNode.rightNode == null) {
                double nearDistance = Metric.calMinkowskiDistance(xList, nearestNode.kdTreeData.dataX, 2);
                double currDistance = Metric.calMinkowskiDistance(xList, backTrackNode.kdTreeData.dataX, 2);
                if (nearDistance > currDistance) {
                    nearestNode = backTrackNode;
                    distance = currDistance;
                }
            } else {
                int split = backTrackNode.split;
                if (distance > Math.abs(backTrackNode.kdTreeData.dataX.get(split) - xList.get(split))) {
                    double nearDistance = Metric.calMinkowskiDistance(xList, nearestNode.kdTreeData.dataX, 2);
                    double currDistance = Metric.calMinkowskiDistance(xList, backTrackNode.kdTreeData.dataX, 2);
                    if (nearDistance > currDistance) {
                        nearestNode = backTrackNode;
                        distance = currDistance;
                    }
                    KDTreeNode searchNode;
                    if (xList.get(split) <= backTrackNode.kdTreeData.dataX.get(split))
                        searchNode = backTrackNode.rightNode;
                    else
                        searchNode = backTrackNode.leftNode;
                    if (searchNode != null)
                        searchPathList.add(searchNode);
                }
            }
        }
        return nearestNode;
    }

    public List<Double> predict(List<List<Double>> dataXList) {
        List<Double> predictList = new ArrayList<Double>();
        for (List<Double> xList : dataXList) {
            List<KDTreeNode> searchPathList = getSearchPath(xList);
            KDTreeNode nearestNode = backtrackSearch(searchPathList, xList);
            System.out.println(nearestNode);
        }
        return predictList;
    }

    public static void main(String args[]) {
        double[][] doubleArr = {{2, 3}, {5, 4}, {9, 6}, {4, 7}, {8, 1}, {7, 2}};
        List<List<Double>> dataXList = new ArrayList<List<Double>>();
        List<Integer> dataYList = new ArrayList<Integer>();
        for (int i = 0; i < doubleArr.length; i++) {
            List<Double> list = new ArrayList<Double>();
            for (int j = 0; j < doubleArr[i].length; j++) {
                list.add(doubleArr[i][j]);
            }
            dataXList.add(list);
            dataYList.add(1);
        }
        KNeighborsClassifier kNeighborsClassifier = new KNeighborsClassifier.Builder().setNeighbourNum(1).build();
        kNeighborsClassifier.train(dataXList, dataYList);
        List<List<Double>> predictList = new ArrayList<List<Double>>();
        List<Double> tempList = new ArrayList<Double>();
        tempList.add(2.1);
        tempList.add(3.1);
        predictList.add(tempList);
        kNeighborsClassifier.predict(predictList);
    }
}
