package com.mltools.tools;

import java.util.*;

/**
 * Created by nhfmaster on 2017/8/22.
 */
public class Viterbi {
    /**
     * 获取可见状态出现序列数量
     *
     * @param emissionMap 发射概率 （隐状态表现为显状态的概率）
     * @return 可见状态出现序列数量
     */
    private int getOccurTimes(Map<String, Double> emissionMap) {
        Set<String> occurSet = new HashSet<String>();
        Set<String> keySet = emissionMap.keySet();
        for (String key : keySet)
            occurSet.add(key.split("->")[1]);
        return occurSet.size();
    }

    /**
     * 填充路径概率表
     *
     * @param stateArr        路径概率表
     * @param observationList 观测序列
     * @param statesList      隐状态
     * @param startMap        初始概率（隐状态）
     * @param transitionMap   转移概率（隐状态）
     * @param emissionMap     发射概率 （隐状态表现为显状态的概率）
     */
    private void fillStatusArr(double[][] stateArr, List<String> observationList, List<String> statesList,
                               Map<String, Double> startMap, Map<String, Double> transitionMap, Map<String, Double> emissionMap) {
        for (int i = 0; i < stateArr.length; i++) {
            for (int j = 0; j < stateArr[i].length; j++) {
                String state = statesList.get(j);
                String observation = observationList.get(i);
                if (i == 0) {
                    stateArr[i][j] = startMap.get(state) * emissionMap.get(state + "->" + observation);
                } else {
                    double max = -1.0;
                    for (int k = 0; k < stateArr[i - 1].length; k++) {
                        double preStateProb = stateArr[i - 1][k];
                        String preState = statesList.get(k);
                        double tempProb = preStateProb * transitionMap.get(preState + "->" + state) *
                                startMap.get(state) * emissionMap.get(state + "->" + observation);
                        if (max <= tempProb)
                            max = tempProb;
                    }
                    stateArr[i][j] = max;
                }
            }
        }
    }

    /**
     * 预测隐状态序列
     *
     * @param observationList 观测序列
     * @param statesList      隐状态
     * @param startMap        初始概率（隐状态）
     * @param transitionMap   转移概率（隐状态）
     * @param emissionMap     发射概率 （隐状态表现为显状态的概率）
     * @return 隐状态序列
     */
    public String predictStatus(List<String> observationList, List<String> statesList, Map<String, Double> startMap,
                                Map<String, Double> transitionMap, Map<String, Double> emissionMap) {
        StringBuffer sb = new StringBuffer();
        int occurTime = getOccurTimes(emissionMap);
        double[][] stateArr = new double[occurTime][startMap.size()];
        fillStatusArr(stateArr, observationList, statesList, startMap, transitionMap, emissionMap);
        for (int i = 0; i < stateArr.length; i++) {
            int maxIndex = -1;
            double max = -1.0;
            for (int j = 0; j < stateArr[i].length; j++) {
                double stateProb = stateArr[i][j];
                if (max <= stateProb) {
                    maxIndex = j;
                    max = stateProb;
                }
            }
            if (sb.length() == 0)
                sb.append(statesList.get(maxIndex));
            else
                sb.append("->").append(statesList.get(maxIndex));
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        Viterbi v = new Viterbi();
        Map<String, Double> startMap = new HashMap<String, Double>();
        startMap.put("Rainy", 0.6);
        startMap.put("Sunny", 0.4);
        Map<String, Double> transitionMap = new HashMap<String, Double>();
        transitionMap.put("Rainy->Rainy", 0.7);
        transitionMap.put("Rainy->Sunny", 0.3);
        transitionMap.put("Sunny->Rainy", 0.4);
        transitionMap.put("Sunny->Sunny", 0.6);
        Map<String, Double> emissionMap = new HashMap<String, Double>();
        emissionMap.put("Rainy->walk", 0.1);
        emissionMap.put("Rainy->shop", 0.4);
        emissionMap.put("Rainy->clean", 0.5);
        emissionMap.put("Sunny->walk", 0.6);
        emissionMap.put("Sunny->shop", 0.3);
        emissionMap.put("Sunny->clean", 0.1);
        List<String> statesList = new ArrayList<String>();
        statesList.add("Sunny");
        statesList.add("Rainy");
        List<String> observationList = new ArrayList<String>();
        observationList.add("walk");
        observationList.add("shop");
        observationList.add("clean");
        System.out.println(v.predictStatus(observationList, statesList, startMap, transitionMap, emissionMap));
    }
}
