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

    public static Map<Object, List<Sample>> loadSamples(String[] attrNames, Object[][] rawData) {

        Map<Object, List<Sample>> result = new HashMap<Object, List<Sample>>();
        for (Object[] row : rawData) {
            Sample sample = new Sample();
            int i = 0;
            for (int n = row.length - 1; i < n; i++) {
                sample.setAttribute(attrNames[i], row[i]);
            }
            sample.setCategory(row[i]);

            List<Sample> samples = result.get(row[i]);
            if (samples == null) {
                samples = new LinkedList<Sample>();
            }
            samples.add(sample);
            result.put(row[i], samples);
        }

        return result;
    }
}
