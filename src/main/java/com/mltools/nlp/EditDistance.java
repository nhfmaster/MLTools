package com.mltools.nlp;

/**
 * Created by nhfmaster on 2017/8/15.
 */
public class EditDistance {
    /**
     * 初始化转换表
     *
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 转换表
     */
    private static int[][] initState(String s1, String s2) {
        int[][] stateArr = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i < s1.length() + 1; i++)
            stateArr[0][i] = i;
        for (int i = 0; i < s2.length() + 1; i++)
            stateArr[i][0] = i;
        return stateArr;
    }

    /**
     * 得到三个数最小值
     *
     * @param v1 值1
     * @param v2 值2
     * @param v3 值3
     * @return 最小值
     */
    private static int getMinValue(int v1, int v2, int v3) {
        int min = v1;
        if (v2 <= min)
            min = v2;
        if (v3 <= min)
            min = v3;
        return min;
    }

    /**
     * 填充转换表中的内容
     *
     * @param stateArr 转换表
     * @param s1       字符串1
     * @param s2       字符串2
     */
    private static void fillState(int[][] stateArr, String s1, String s2) {
        for (int i = 1; i < stateArr.length; i++) {
            for (int j = 1; j < stateArr[i].length; j++) {
                int temp = (s1.charAt(i - 1) + "").equals((s2.charAt(j - 1) + "")) ? 0 : 1;
                int v1 = stateArr[i - 1][j] + 1;
                int v2 = stateArr[i][j - 1] + 1;
                int v3 = stateArr[i - 1][j - 1] + temp;
                int min = getMinValue(v1, v2, v3);
                stateArr[i][j] = min;
            }
        }
    }

    /**
     * 计算编辑距离
     *
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 编辑距离
     */
    public static int calEditDistance(String s1, String s2) {
        if (s1.length() == 0)
            return s2.length();
        if (s2.length() == 0)
            return s1.length();
        int[][] stateArr = initState(s1, s2);
        fillState(stateArr, s1, s2);
        return stateArr[s1.length()][s2.length()];
    }

    public static void main(String args[]) {
        System.out.println(EditDistance.calEditDistance("江阳环境", "江苏阳光"));
    }
}
