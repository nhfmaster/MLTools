package com.mltools.metrics;

import com.mltools.tools.Tools;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nhfmaster on 2017/12/5.
 */
public class ClassificationMetric {
    /**
     * calculate accuracy
     *
     * @param predictList predicted value
     * @param trueList    true value
     */
    public static void calAccuracy(List<Integer> predictList, List<Integer> trueList) {
        int sameCount = 0;
        for (int i = 0; i < trueList.size(); i++)
            if (trueList.get(i).equals(predictList.get(i)))
                sameCount++;
        double accuracy = (double) sameCount / trueList.size() * 100;
        System.out.println("Accuracy: " + accuracy + "%");
    }

    /**
     * calculate precision recall f1-score
     *
     * @param predictList predicted value
     * @param trueList    true value
     */
    public static void getClassificationDetail(List<Integer> predictList, List<Integer> trueList) {
        DecimalFormat df = new DecimalFormat("######0.00");
        Map<Integer, Integer> sameValueMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> trueValueMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> predictValueMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < trueList.size(); i++) {
            int trueValue = trueList.get(i);
            int predictValue = predictList.get(i);
            Tools.fillCountMap(trueValueMap, trueValue);
            Tools.fillCountMap(predictValueMap, predictValue);
            if (trueValue == predictValue)
                Tools.fillCountMap(sameValueMap, trueValue);
        }
        System.out.println("\t" + "\t" + "precision" + "\t" + "recall" + "\t" + "f1-score");

        for (Map.Entry<Integer, Integer> entry : trueValueMap.entrySet()) {
            int classIndex = entry.getKey();
            int trueSum = entry.getValue();
            int predictSum = predictValueMap.get(classIndex) == null ? 0 : predictValueMap.get(classIndex);
            int sameSum = sameValueMap.get(classIndex) == null ? 0 : sameValueMap.get(classIndex);
            double precision = (double) sameSum / predictSum * 100;
            double recall = (double) sameSum / trueSum * 100;
            double f1Score = 0.0;
            if (precision != 0 || recall != 0)
                f1Score = 2 * (precision / 100 * recall / 100) / (precision / 100 + recall / 100);
            System.out.println("class " + classIndex + "\t" + df.format(precision) + "%"
                    + "\t" + df.format(recall) + "%" + "\t" + df.format(f1Score));
        }
    }

    /**
     * calculate Hamming loss
     *
     * @param predictList predicted value
     * @param trueList    true value
     */
    public static void calHammingLoss(List<Integer> predictList, List<Integer> trueList) {
        int differ = 0;
        for (int i = 0; i < trueList.size(); i++)
            if (!trueList.get(i).equals(predictList.get(i)))
                differ++;
        double loss = (double) differ / trueList.size();
        System.out.println("Hamming loss: " + loss);
    }
}
