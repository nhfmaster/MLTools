package com.mltools.classify.naivebayes;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.mltools.classify.Classifier;
import com.mltools.tools.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by nhfmaster on 2017/6/17.
 */
public class NaiveBayes implements Classifier {
    private List<String> trainList = new ArrayList<String>(); // 训练数据
    private List<String> testList = new ArrayList<String>(); // 测试数据
    private String wrongPath = "";
    private BayesData bayesData;

    public NaiveBayes() {
    }

    /**
     * 构造方法
     *
     * @param trainPath 训练集路径
     * @param testPath  测试集路径
     */
    public NaiveBayes(String trainPath, String testPath, String wrongPath) {
        this.wrongPath = wrongPath;
        this.trainList = Tools.readTXTToList(trainPath);
        this.testList = Tools.readTXTToList(testPath);
    }

    /**
     * 填充数量统计map
     *
     * @param map  待填充map
     * @param word 统计的词语
     * @param step 增加的步长
     */
    private void fillIntValueMap(Map<String, Integer> map, String word, int step) {
        if (map.get(word) == null)
            map.put(word, step);
        else
            map.put(word, map.get(word) + step);
    }

    /**
     * 填充概率统计map
     *
     * @param map      待填充map
     * @param countSum 概率分母数据
     * @return 概率统计map
     */
    private Map<String, Double> fillDoubleValueMap(Map<String, Integer> map, int countSum) {
        Map<String, Double> resultMap = new HashMap<String, Double>();
        for (Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            resultMap.put(key, (double) value / countSum);
        }
        return resultMap;
    }

    /**
     * 根据另一map填充概率
     *
     * @param map      待填充map
     * @param classMap 另一统计map
     * @return 概率map
     */
    private Map<String, Double> fillDoubleKeyValueMap(Map<String, Integer> map, Map<String, Integer> classMap) {
        Map<String, Double> resultMap = new HashMap<String, Double>();
        for (Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            String className = key.split("\t")[1];
            int wordCount = classMap.get(className);
            resultMap.put(key, (double) value / wordCount);
        }
        return resultMap;
    }

    /**
     * 朴素贝叶斯训练过程
     */
    public void train() {
        Map<String, Integer> classSumMap = new HashMap<String, Integer>();
        Map<String, Integer> wordSumMap = new HashMap<String, Integer>();
        Map<String, Integer> wordClassMap = new HashMap<String, Integer>();
        Map<String, Integer> classWordMap = new HashMap<String, Integer>();
        int wordSum = 0;
        int classSum = 0;
        for (String line : trainList) {
            int classWordSum = 0;
            if (line.split("\t").length < 2)
                continue;
            String sentence = line.split("\t")[1];
            String type = line.split("\t")[0];
            classSum++;
            fillIntValueMap(classSumMap, type, 1);
            List<Term> segList = StandardTokenizer.segment(sentence.toLowerCase());
            for (Term seg : segList) {
                String word = seg.word;
                wordSum++;
                classWordSum++;
                fillIntValueMap(wordSumMap, word, 1);
                fillIntValueMap(wordClassMap, word + "\t" + type, 1);
            }
            fillIntValueMap(classWordMap, type, classWordSum);
        }
        Map<String, Double> wordProbMap = fillDoubleValueMap(wordSumMap, wordSum);
        Map<String, Double> classProbMap = fillDoubleValueMap(classSumMap, classSum);
        Map<String, Double> wordClassProbMap = fillDoubleKeyValueMap(wordClassMap, classWordMap);
        this.bayesData = new BayesData(classProbMap, wordProbMap, wordClassProbMap, wordSum, classWordMap);
    }

    /**
     * 初始化测试结果List
     *
     * @param initNumList 原始标注List
     * @param sameList    测试与原始相同List
     * @param preList     预测List
     */
    private void initTestData(List<Integer> initNumList, List<Integer> sameList, List<Integer> preList) {
        for (int i = 0; i < bayesData.classProbMap.size(); i++) {
            initNumList.add(0);
            sameList.add(0);
            preList.add(0);
        }
    }

    /**
     * 朴素贝叶斯预测过程
     */
    public void test() {
        List<String> lineList = new ArrayList<String>();
        List<Integer> initNumList = new ArrayList<Integer>();
        List<Integer> sameList = new ArrayList<Integer>();
        List<Integer> preList = new ArrayList<Integer>();
        initTestData(initNumList, sameList, preList);
        int allSum = 0;
        int preSum = 0;
        for (String line : testList) {
            if (line.split("\t").length < 2)
                continue;
            allSum++;
            String type = line.split("\t")[0];
            int index = Integer.parseInt(type) - 1;
            initNumList.set(index, initNumList.get(index) + 1);
            String text = line.split("\t")[1];
            List<Term> segList = StandardTokenizer.segment(text.toLowerCase());
            List<Double> probList = new ArrayList<Double>();
            for (int i = 0; i < bayesData.classProbMap.size(); i++) {
                probList.add(1.0);
            }
            for (Term seg : segList) {
                String word = seg.word;
                double wordProb = bayesData.wordProbMap.get(word) == null ? (double) 1 / (bayesData.wordSum + 1)
                        : bayesData.wordProbMap.get(word);
                for (int i = 0; i < probList.size(); i++) {
                    String probType = (i + 1) + "";
                    double classWordProb = bayesData.wordClassProbMap.get(word + "\t" + probType) == null
                            ? (double) 1 / (bayesData.classWordMap.get(probType) + 1)
                            : bayesData.wordClassProbMap.get(word + "\t" + probType);
                    probList.set(i, probList.get(i) * classWordProb / wordProb);
                }
            }
            for (int i = 0; i < probList.size(); i++) {
                String probType = (i + 1) + "";
                probList.set(i, probList.get(i) * bayesData.classProbMap.get(probType));
            }
            int result = Tools.getMaxIndex(probList);
            preList.set(result - 1, preList.get(result - 1) + 1);
            if ((result + "").equals(type))
                preSum++;
            else {
                lineList.add(line + "\t" + result);
            }
            if ((result + "").equals(type))
                sameList.set(result - 1, sameList.get(result - 1) + 1);
        }

        System.out.println("总体精度->" + (double) preSum / allSum * 100 + "%");
        for (int i = 0; i < preList.size(); i++) {
            System.out.println("类别" + (i + 1) + "精度->" + (double) sameList.get(i) / preList.get(i) * 100 + "%");
            System.out.println("类别" + (i + 1) + "召回->" + (double) sameList.get(i) / initNumList.get(i) * 100 + "%");
        }
        Tools.writeListToFile(lineList, wrongPath);
    }

    public static void main(String args[]) throws IOException {
        NaiveBayes nb = new NaiveBayes("train.txt", "test.txt", "wrong.txt");
        nb.train();
        nb.test();
    }
}
