package com.mltools.classify.naivebayes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nhfmaster on 2017/6/17.
 */
public class BayesData {
    public Map<String, Double> classProbMap = new HashMap<String, Double>();
    public Map<String, Double> wordProbMap = new HashMap<String, Double>();
    public Map<String, Double> wordClassProbMap = new HashMap<String, Double>();
    public int wordSum = 0;
    public Map<String, Integer> classWordMap = new HashMap<String, Integer>();

    public BayesData(Map<String, Double> classProbMap, Map<String, Double> wordProbMap,
                     Map<String, Double> wordClassProbMap, int wordSum, Map<String, Integer> classWordMap) {
        this.classProbMap = classProbMap;
        this.wordClassProbMap = wordClassProbMap;
        this.wordProbMap = wordProbMap;
        this.wordSum = wordSum;
        this.classWordMap = classWordMap;
    }

    public BayesData() {

    }
}
