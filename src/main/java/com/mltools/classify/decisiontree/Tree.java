package com.mltools.classify.decisiontree;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mickeyzhou on 2017/12/16
 */
public class Tree {
    private String attribute;

    private Map<Object, Object> children = new HashMap<Object, Object>();

    public Tree(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public Object getChild(Object attrValue) {
        return children.get(attrValue);
    }

    public void setChild(Object attrValue, Object child) {
        children.put(attrValue, child);
    }

    public Set<Object> getAttributeValues() {
        return children.keySet();
    }
}
