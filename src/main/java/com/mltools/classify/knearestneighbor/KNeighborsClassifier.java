package com.mltools.classify.knearestneighbor;

import com.mltools.classify.Classifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhfmaster on 2018/1/23.
 */
public class KNeighborsClassifier extends Classifier {
    private int neighbourNum; // Number of neighbours
    private String metric; // distance metric

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

        KDTree kdTree = new KDTree(kdTreeDataList);
    }
}
