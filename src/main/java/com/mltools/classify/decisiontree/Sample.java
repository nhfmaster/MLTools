package com.mltools.classify.decisiontree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by mickeyzhou on 2017/12/16
 */
public class Sample {
    private Map<String, Object> attributes = new HashMap<String, Object>();

    private Object category;

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public String toString() {
        return attributes.toString();
    }

    public static Map<Object, List<Sample>> readSamples(String[] attrNames) {

        // 样本属性及其所属分类（数组中的最后一个元素为样本所属分类）
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

        // 读取样本属性及其所属分类，构造表示样本的Sample对象，并按分类划分样本集
        Map<Object, List<Sample>> ret = new HashMap<Object, List<Sample>>();
        for (Object[] row : rawData) {
            Sample sample = new Sample();
            int i = 0;
            for (int n = row.length - 1; i < n; i++)
                sample.setAttribute(attrNames[i], row[i]);
            sample.setCategory(row[i]);
            List<Sample> samples = ret.get(row[i]);
            if (samples == null) {
                samples = new LinkedList<Sample>();
                ret.put(row[i], samples);
            }
            samples.add(sample);
        }

        return ret;
    }
}
