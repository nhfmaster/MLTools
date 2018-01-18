package com.mltools.classify.knearestneighbor;

import java.util.List;

/**
 * Created by nhfmaster on 2018/1/16.
 */
public class KDTreeNode {
    KDTreeNode leftNode;
    KDTreeNode rightNode;
    List<Double> data;
    int split;

    public KDTreeNode() {
    }

    public KDTreeNode(List<Double> data, int split) {
        this.data = data;
        this.split = split;
    }

    public KDTreeNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(KDTreeNode leftNode) {
        this.leftNode = leftNode;
    }

    public KDTreeNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(KDTreeNode rightNode) {
        this.rightNode = rightNode;
    }

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public int getSplit() {
        return split;
    }

    public void setSplit(int split) {
        this.split = split;
    }

    public String toString() {
        return "data:" + data + ";split:" + split;
    }
}
