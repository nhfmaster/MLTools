package com.mltools.tools;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tools {
    /**
     * 读取文件为List
     *
     * @param filename 文件路径
     * @return 文件内容List
     */
    static public List<String> readTXTToList(String filename) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> result = new ArrayList<>();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                line = line.replace("\n", "");
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取文件为Set
     *
     * @param filename 文件路径
     * @return 文件内容Set
     */
    static public HashSet<String> readTXTToSet(String filename) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashSet<String> result = new HashSet<>();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                line = line.replace("\n", "");
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Set写入本地文件
     *
     * @param sets Set
     * @param out  文件路径
     */
    static public void writeSetToFile(Set<String> sets, String out) {
        BufferedWriter wr2 = null;
        try {
            wr2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out)));
            for (String set : sets) {
                wr2.write(set + "\n");
            }
            wr2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * List写入本地文件
     *
     * @param list List
     * @param out  文件路径
     */
    static public void writeListToFile(List<String> list, String out) {
        BufferedWriter wr2 = null;
        try {
            wr2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out)));
            for (String s : list) {
                wr2.write(s + "\n");
            }
            wr2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数组中最大的数
     *
     * @param arr 数组
     * @return 数组中最大的数
     */
    public static int getMaxNumber(double[] arr) {
        double temp = 0.0;
        int result = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= temp) {
                temp = arr[i];
                result = i;
            }
        }
        return result;
    }


    /**
     * 得到list最大数的下标
     *
     * @param list 数据list
     * @return 最大数的下标
     */
    public static int getMaxIndex(List<Double> list) {
        int index = -1;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < list.size(); i++) {
            if (max <= list.get(i)) {
                max = list.get(i);
                index = i + 1;
            }
        }
        return index;
    }
}
