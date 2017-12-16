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
        Object[][] rawData = new Object[][] {
                { "<30  ", "High  ", "No ", "Fair     ", "0" },
                { "<30  ", "High  ", "No ", "Excellent", "0" },
                { "30-40", "High  ", "No ", "Fair     ", "1" },
                { ">40  ", "Medium", "No ", "Fair     ", "1" },
                { ">40  ", "Low   ", "Yes", "Fair     ", "1" },
                { ">40  ", "Low   ", "Yes", "Excellent", "0" },
                { "30-40", "Low   ", "Yes", "Excellent", "1" },
                { "<30  ", "Medium", "No ", "Fair     ", "0" },
                { "<30  ", "Low   ", "Yes", "Fair     ", "1" },
                { ">40  ", "Medium", "Yes", "Fair     ", "1" },
                { "<30  ", "Medium", "Yes", "Excellent", "1" },
                { "30-40", "Medium", "No ", "Excellent", "1" },
                { "30-40", "High  ", "Yes", "Fair     ", "1" },
                { ">40  ", "Medium", "No ", "Excellent", "0" } };

        // LoadSample
        Map<Object, List<Sample>> samples = Sample.loadSamples(attrNames,rawData);

        // GenerateDecisionTree
        Object decisionTree = dt.generateDecisionTree(samples, attrNames);

        // OutputDecisionTree
        dt.outputDecisionTree(decisionTree, 0, null);
    }
}
