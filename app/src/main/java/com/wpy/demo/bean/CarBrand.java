package com.wpy.demo.bean;

import java.util.ArrayList;

/**
 * Created by Victor Liu on 2017/8/28.
 */

public class CarBrand {
    /**
     * 汽车的品牌名字
     */
    public String mBrandName;
    public String mSortLetters;
    /** 该品牌下包含的汽车类型 */
    public ArrayList<CarStyle> mCarStyleList;
}
