package com.mltools.classify.knearestneighbor;

import java.util.List;

/**
 * Created by nhfmaster on 2018/1/20.
 */
public class KDTreeData {
    public List<Double> dataX; // feature value
    public int dataY; // true value

    public KDTreeData(List<Double> dataX, int dataY) {
        this.dataX = dataX;
        this.dataY = dataY;
    }

    public String toString() {
        return "[dataX:" + dataX + "; dataY:" + dataY + "]";
    }
}
