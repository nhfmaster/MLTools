package com.mltools.classify.decisiontree;

import java.util.*;

/**
 * Created by mickeyzhou on 2017/12/16
 */
public class DecisionTree {

    public Object generateDecisionTree(
            Map<Object, List<Sample>> categoryToSamples, String[] attrNames) {

        // 如果只有一个样本，将该样本所属分类作为新样本的分类
        if (categoryToSamples.size() == 1)
            return categoryToSamples.keySet().iterator().next();

        // 如果没有供决策的属性，则将样本集中具有最多样本的分类作为新样本的分类，即投票选举出分类
        if (attrNames.length == 0) {
            int max = 0;
            Object maxCategory = null;
            for (Map.Entry<Object, List<Sample>> entry : categoryToSamples
                    .entrySet()) {
                int cur = entry.getValue().size();
                if (cur > max) {
                    max = cur;
                    maxCategory = entry.getKey();
                }
            }
            return maxCategory;
        }

        // 选取测试属性
        Object[] rst = chooseBestTestAttribute(categoryToSamples, attrNames);

        // 决策树根结点，分支属性为选取的测试属性
        Tree tree = new Tree(attrNames[(Integer) rst[0]]);

        // 已用过的测试属性不应再次被选为测试属性
        String[] subA = new String[attrNames.length - 1];
        for (int i = 0, j = 0; i < attrNames.length; i++)
            if (i != (Integer) rst[0])
                subA[j++] = attrNames[i];

        // 根据分支属性生成分支
        @SuppressWarnings("unchecked")
        Map<Object, Map<Object, List<Sample>>> splits =
        /* NEW LINE */(Map<Object, Map<Object, List<Sample>>>) rst[2];
        for (Map.Entry<Object, Map<Object, List<Sample>>> entry : splits.entrySet()) {
            Object attrValue = entry.getKey();
            Map<Object, List<Sample>> split = entry.getValue();
            Object child = generateDecisionTree(split, subA);
            tree.setChild(attrValue, child);
        }

        return tree;
    }

    public Object[] chooseBestTestAttribute(
            Map<Object, List<Sample>> categoryToSamples, String[] attrNames) {

        int minIndex = -1; // 最优属性下标
        double minValue = Double.MAX_VALUE; // 最小信息量
        Map<Object, Map<Object, List<Sample>>> minSplits = null; // 最优分支方案

        // 对每一个属性，计算将其作为测试属性的情况下在各分支确定新样本的分类需要的信息量之和，选取最小为最优
        for (int attrIndex = 0; attrIndex < attrNames.length; attrIndex++) {
            int allCount = 0; // 统计样本总数的计数器

            // 按当前属性构建Map：属性值->(分类->样本列表)
            Map<Object, Map<Object, List<Sample>>> curSplits =
            /* NEW LINE */new HashMap<Object, Map<Object, List<Sample>>>();
            for (Map.Entry<Object, List<Sample>> entry : categoryToSamples
                    .entrySet()) {
                Object category = entry.getKey();
                List<Sample> samples = entry.getValue();
                for (Sample sample : samples) {
                    Object attrValue = sample
                            .getAttribute(attrNames[attrIndex]);
                    Map<Object, List<Sample>> split = curSplits.get(attrValue);
                    if (split == null) {
                        split = new HashMap<Object, List<Sample>>();
                        curSplits.put(attrValue, split);
                    }
                    List<Sample> splitSamples = split.get(category);
                    if (splitSamples == null) {
                        splitSamples = new LinkedList<Sample>();
                        split.put(category, splitSamples);
                    }
                    splitSamples.add(sample);
                }
                allCount += samples.size();
            }

            // 计算将当前属性作为测试属性的情况下在各分支确定新样本的分类需要的信息量之和
            double curValue = 0.0; // 计数器：累加各分支
            for (Map<Object, List<Sample>> splits : curSplits.values()) {
                double perSplitCount = 0;
                for (List<Sample> list : splits.values())
                    perSplitCount += list.size(); // 累计当前分支样本数
                double perSplitValue = 0.0; // 计数器：当前分支
                for (List<Sample> list : splits.values()) {
                    double p = list.size() / perSplitCount;
                    perSplitValue -= p * (Math.log(p) / Math.log(2));
                }
                curValue += (perSplitCount / allCount) * perSplitValue;
            }

            // 选取最小为最优
            if (minValue > curValue) {
                minIndex = attrIndex;
                minValue = curValue;
                minSplits = curSplits;
            }
        }

        return new Object[] { minIndex, minValue, minSplits };
    }

    public void outputDecisionTree(Object obj, int level, Object from) {
        for (int i = 0; i < level; i++)
            System.out.print("|-----");
        if (from != null)
            System.out.printf("(%s):", from);
        if (obj instanceof Tree) {
            Tree tree = (Tree) obj;
            String attrName = tree.getAttribute();
            System.out.printf("[%s = ?]\n", attrName);
            for (Object attrValue : tree.getAttributeValues()) {
                Object child = tree.getChild(attrValue);
                outputDecisionTree(child, level + 1, attrName + " = "
                        + attrValue);
            }
        } else {
            System.out.printf("[CATEGORY = %s]\n", obj);
        }
    }
}
