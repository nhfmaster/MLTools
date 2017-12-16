package com.mltools.classify.demo;

import com.mltools.classify.decisiontree.Sample;
import com.mltools.classify.decisiontree.DecisionTree;

import java.util.List;
import java.util.Map;

/**
 * Created by mickeyzhou on 2017/12/16
 */
public class DecisionTreeDemo {
    public static void main(String[] args) throws Exception {

        DecisionTree dt = new DecisionTree();
        String[] attrNames = new String[] { "AGE", "INCOME", "STUDENT",
                "CREDIT_RATING" };

        // LoadSample
        Map<Object, List<Sample>> samples = Sample.readSamples(attrNames);

        // GenerateDecisionTree
        Object decisionTree = dt.generateDecisionTree(samples, attrNames);

        // OutputDecisionTree
        dt.outputDecisionTree(decisionTree, 0, null);
    }
}
