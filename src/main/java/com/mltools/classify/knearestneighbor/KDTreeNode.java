package com.mltools.classify.knearestneighbor;

/**
 * Created by nhfmaster on 2018/1/16.
 */
public class KDTreeNode {
    KDTreeNode parentNode;
    KDTreeNode leftNode;
    KDTreeNode rightNode;
    KDTreeData kdTreeData;
    int split;

    public KDTreeNode() {
    }

    public KDTreeNode(KDTreeData kdTreeData, int split) {
        this.kdTreeData = kdTreeData;
        this.split = split;
    }

    public KDTreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(KDTreeNode parentNode) {
        this.parentNode = parentNode;
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

    public int getSplit() {
        return split;
    }

    public void setSplit(int split) {
        this.split = split;
    }

    public KDTreeData getKdTreeData() {
        return kdTreeData;
    }

    public void setKdTreeData(KDTreeData kdTreeData) {
        this.kdTreeData = kdTreeData;
    }

    public String toString() {
        return "[kdTreeData:" + kdTreeData + "; split:" + split + "]";
    }
}
