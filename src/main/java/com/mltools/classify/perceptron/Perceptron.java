package com.mltools.classify.perceptron;

import com.mltools.tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nhfmaster on 2017/2/19.
 */
public class Perceptron {
    private List<Map<List<Double>, Integer>> dataList = new ArrayList<Map<List<Double>, Integer>>(); //训练数据List
    private List<Double> weight = new ArrayList<Double>(); //感知机权重
    private double bias = 0.0; //感知机偏置
    private static final double NORMAL_INIT_VALUE = 1.0; // 感知机原始形式偏置默认值
    private static final double DUAL_INIT_VALUE = 0.0; // 感知机对偶形式偏置默认值
    private double learningRate; // 学习率
    public static final int INIT_FORM = 0; //原始形式
    public static final int DUAL_FORM = 1; //对偶形式
    private int formType = 0; //学习形式

    /**
     * 感知机默认构造方法
     *
     * @param filePath     训练数据所在路径
     * @param learningRate 学习率
     */
    public Perceptron(String filePath, double learningRate) {
        this.loadTrainingData(filePath);
        this.learningRate = learningRate;
    }

    /**
     * 感知机构造方法
     *
     * @param filePath     训练数据所在路径
     * @param learningRate 学习率
     * @param formType     学习形式
     */
    public Perceptron(String filePath, double learningRate, int formType) {
        this.learningRate = learningRate;
        this.formType = formType;
        this.loadTrainingData(filePath);
    }

    /**
     * 加载训练数据，初始化模型参数
     *
     * @param filePath 训练数据所在路径
     */
    private void loadTrainingData(String filePath) {
        this.initData(filePath);
        this.initPerceptron();
    }

    /**
     * 初始化训练数据
     *
     * @param filePath 训练数据所在路径
     */
    private void initData(String filePath) {
        List<String> textList = Tools.readTXTToList(filePath);
        for (String line : textList) {
            int dataY = Integer.parseInt(line.split("\t")[0]);
            List<Double> dataXList = new ArrayList<Double>();
            for (String dataX : line.split("\t")[1].split(","))
                dataXList.add(Double.parseDouble(dataX));
            Map<List<Double>, Integer> map = new HashMap<List<Double>, Integer>();
            map.put(dataXList, dataY);
            dataList.add(map);
        }
    }

    /**
     * 初始化模型参数
     */
    private void initPerceptron() {
        if (this.formType == INIT_FORM) {
            int dimension = this.dataList.get(dataList.size() - 1).keySet().iterator().next().size();
            for (int i = 0; i < dimension; i++)
                this.weight.add(NORMAL_INIT_VALUE);
        } else {
            int dimension = this.dataList.size();
            for (int i = 0; i < dimension; i++)
                this.weight.add(DUAL_INIT_VALUE);
            System.out.println(dimension + "\t" + weight.size());
        }
    }

    /**
     * 计算初始形式损失函数数值
     *
     * @param dataXList 数据X的List
     * @param dataY     数据对应Y数值
     * @return 初始形式损失函数数值
     */
    private double calInitLossValue(List<Double> dataXList, int dataY) {
        double matrixValue = 0.0;
        for (int i = 0; i < weight.size(); i++)
            for (int j = 0; j < dataXList.size(); j++)
                matrixValue += weight.get(i) * dataXList.get(i);
        matrixValue += bias;
        matrixValue = dataY * matrixValue;
        return matrixValue;
    }

    /**
     * 判断是否需要更新初始形式模型参数
     *
     * @param dataMap 训练数据Map
     * @return 更新 true；不更新 false
     */
    private boolean ifUpdateInitPerceptron(Map<List<Double>, Integer> dataMap) {
        double matrixValue = 0.0;
        for (Map.Entry<List<Double>, Integer> entry : dataMap.entrySet()) {
            List<Double> dataXList = entry.getKey();
            int dataY = entry.getValue();
            matrixValue = calInitLossValue(dataXList, dataY);
        }
        return matrixValue <= 0;
    }

    /**
     * 更新初始形式模型参数
     *
     * @param dataMap 训练数据Map
     */
    private void updateInitPerceptron(Map<List<Double>, Integer> dataMap) {
        for (Map.Entry<List<Double>, Integer> entry : dataMap.entrySet()) {
            List<Double> dataXList = entry.getKey();
            int dataY = entry.getValue();
            bias = bias + learningRate * dataY;
            for (int i = 0; i < weight.size(); i++) {
                double result = weight.get(i) + learningRate * dataY * dataXList.get(i);
                weight.set(i, result);
            }
        }
    }

    /**
     * 使用原始形式训练感知机
     */
    private void initFormTrain() {
        for (int i = 0; i < dataList.size(); i++) {
            Map<List<Double>, Integer> dataMap = dataList.get(i);
            boolean updateFlag = ifUpdateInitPerceptron(dataMap);
            if (updateFlag) {
                updateInitPerceptron(dataMap);
                i = 0;
            }
        }
    }

    /**
     * 使用对偶形式训练感知机
     */
    private void dualFormTrain() {
        List<List<Double>> gramMatrix = calGramMatrix();
        for (int i = 0; i < dataList.size(); i++) {
            Map<List<Double>, Integer> dataMap = dataList.get(i);
            boolean updateFlag = ifUpdateDualPerceptron(gramMatrix, i);
            if (updateFlag) {
                updateDualPerceptron(dataMap, i);
                i = 0;
            }
        }
    }

    /**
     * 更新对偶形式模型参数
     *
     * @param dataMap 训练数据Map
     */
    private void updateDualPerceptron(Map<List<Double>, Integer> dataMap, int i) {
        weight.set(i, weight.get(i) + learningRate);
        for (Map.Entry<List<Double>, Integer> entry : dataMap.entrySet())
            bias += entry.getValue() * learningRate;
    }

    /**
     * 判断是否需要更新对偶形式模型参数
     *
     * @param gramMatrix gram矩阵
     * @return 更新 true；不更新 false
     */
    private boolean ifUpdateDualPerceptron(List<List<Double>> gramMatrix, int i) {
        double matrixValue = calDualLossValue(gramMatrix, i);
        return matrixValue <= 0;
    }

    private double calDualLossValue(List<List<Double>> gramMatrix, int i) {
        double tempSum = 0.0;
        List<Integer> dataYList = getDataYList();
        for (int j = 0; j < weight.size(); j++)
            tempSum += weight.get(j) * dataYList.get(j) * gramMatrix.get(j).get(i);
        return dataYList.get(i) * (tempSum + bias);
    }

    /**
     * 获得y的数据
     *
     * @return y数据的List
     */
    private List<Integer> getDataYList() {
        List<Integer> resultList = new ArrayList<Integer>();
        for (Map<List<Double>, Integer> map : dataList)
            for (Map.Entry<List<Double>, Integer> entry : map.entrySet())
                resultList.add(entry.getValue());
        return resultList;
    }


    /**
     * 获得x的数据
     *
     * @return x数据的List
     */
    private List<List<Double>> getDataXList() {
        List<List<Double>> resultList = new ArrayList<List<Double>>();
        for (Map<List<Double>, Integer> map : dataList)
            for (Map.Entry<List<Double>, Integer> entry : map.entrySet())
                resultList.add(entry.getKey());

        return resultList;
    }

    /**
     * 计算gram矩阵
     *
     * @return gram矩阵
     */
    private List<List<Double>> calGramMatrix() {
        List<List<Double>> resultList = new ArrayList<List<Double>>();
        List<List<Double>> dataXList = getDataXList();
        for (List<Double> beforeList : dataXList) {
            List<Double> tempList = new ArrayList<Double>();
            for (List<Double> behindList : dataXList) {
                double tempMul = 0.0;
                for (int i = 0; i < beforeList.size(); i++)
                    tempMul += beforeList.get(i) * behindList.get(i);
                tempList.add(tempMul);
            }
            resultList.add(tempList);
        }
        return resultList;
    }

    /**
     * 训练感知机
     */
    private void trainPerceptron() {
        if (this.formType == INIT_FORM)
            initFormTrain();
        else
            dualFormTrain();
    }

    public static void main(String args[]) {
        Perceptron perceptron = new Perceptron("perception_test.txt", 1, Perceptron.DUAL_FORM);
        perceptron.trainPerceptron();
        System.out.println(perceptron.weight);
        System.out.println(perceptron.bias);
    }
}
