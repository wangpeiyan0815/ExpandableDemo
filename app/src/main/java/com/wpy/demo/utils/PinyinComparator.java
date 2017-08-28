package com.wpy.demo.utils;

import com.wpy.demo.bean.CarBrand;

import java.util.Comparator;

/**
 * 用来对ListView中的数据根据A-Z进行排序，前面两个if判断主要是将不是以汉字开头的数据放在后面
 */
public class PinyinComparator implements Comparator<CarBrand> {

    public int compare(CarBrand o1, CarBrand o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o1.mSortLetters.equals("@") || o2.mSortLetters.equals("#")) {
            return -1;
        } else if (o1.mSortLetters.equals("#") || o2.mSortLetters.equals("@")) {
            return 1;
        } else {
            return o1.mSortLetters.compareTo(o2.mSortLetters);
        }
    }

}
