package com.mltools.classify.knearestneighbor;

import com.mltools.classify.Classifier;
import com.mltools.metrics.Metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<KDTreeNode> backtrackSearch(List<KDTreeNode> searchPathList, List<Double> xList) {
        List<KDTreeNode> nearestList = new ArrayList<KDTreeNode>();
        KDTreeNode nearFurthestNode = null;
        if (searchPathList.size() == 0)
            return nearestList;
        KDTreeNode lastNode = searchPathList.get(searchPathList.size() - 1);
        nearFurthestNode = lastNode;
        double distance = Metric.calMinkowskiDistance(xList, lastNode.kdTreeData.dataX, 2);
        while (searchPathList.size() > 0) {
            KDTreeNode backTrackNode = searchPathList.get(searchPathList.size() - 1);
            searchPathList.remove(searchPathList.size() - 1);

            double nearDistance = Metric.calMinkowskiDistance(xList, nearFurthestNode.kdTreeData.dataX, 2);
            double currDistance = Metric.calMinkowskiDistance(xList, backTrackNode.kdTreeData.dataX, 2);

            if (nearDistance > currDistance) {
                if (nearestList.size() < neighbourNum) {
                    nearFurthestNode = backTrackNode;
                    distance = currDistance;
                    nearFurthestNode.distance = currDistance;
                    nearestList.add(nearFurthestNode);
                } else {
                    nearestList.remove(nearFurthestNode);
                    nearFurthestNode = backTrackNode;
                    distance = currDistance;
                    nearFurthestNode.distance = currDistance;
                    nearestList.add(nearFurthestNode);
                }
            }

            if (nearestList.size() < neighbourNum)
                if (!nearestList.contains(backTrackNode))
                    nearestList.add(backTrackNode);

            if (!(backTrackNode.leftNode == null && backTrackNode.rightNode == null)) {
                int split = backTrackNode.split;
                if (nearestList.size() < neighbourNum) {
                    KDTreeNode searchNode;
                    if (xList.get(split) <= backTrackNode.kdTreeData.dataX.get(split))
                        searchNode = backTrackNode.rightNode;
                    else
                        searchNode = backTrackNode.leftNode;
                    if (searchNode != null)
                        searchPathList.add(searchNode);
                } else {
                    if (distance > Math.abs(backTrackNode.kdTreeData.dataX.get(split) - xList.get(split))) {
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
        }
        return nearestList;
    }

    public List<Double> predict(List<List<Double>> dataXList) {
        List<Double> predictList = new ArrayList<Double>();
        for (List<Double> xList : dataXList) {
            List<KDTreeNode> searchPathList = getSearchPath(xList);
            List<KDTreeNode> nearList = backtrackSearch(searchPathList, xList);
            Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
            for (KDTreeNode node : nearList) {
                int yValue = node.kdTreeData.dataY;
                if (countMap.get(yValue) == null)
                    countMap.put(yValue, 1);
                else
                    countMap.put(yValue, countMap.get(yValue) + 1);
            }
            int maxCount = 0;
            double result = 0;
            for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
                int label = entry.getKey();
                int count = entry.getValue();
                if (count >= maxCount) {
                    result = label;
                    maxCount = count;
                }
            }
            predictList.add(result);
        }
        return predictList;
    }

    public static void main(String args[]) {
        double[][] doubleArr = {{2, 3}, {5, 4}, {9, 6}, {4, 7}, {8, 1}, {7, 2}, {4, 3}, {1, 1}, {6, 2}, {4, 4}, {3, 3}, {5, 6}};
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
        KNeighborsClassifier kNeighborsClassifier = new KNeighborsClassifier.Builder().setNeighbourNum(4).build();
        kNeighborsClassifier.train(dataXList, dataYList);
        List<List<Double>> predictList = new ArrayList<List<Double>>();
        List<Double> tempList = new ArrayList<Double>();
        tempList.add(3.5);
        tempList.add(4.5);
        predictList.add(tempList);
        List<Double> resultList = kNeighborsClassifier.predict(predictList);
        System.out.println(resultList);
    }
}
