package com.wpy.demo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.wpy.demo.adapter.CarBrandAdapter;
import com.wpy.demo.bean.CarBrand;
import com.wpy.demo.bean.CarStyle;
import com.wpy.demo.utils.PinyinComparator;
import com.wpy.demo.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Model：定义的数据
    private String[] groups = {"北京市", "河北省", "河南省", "广东省"
    };
    private List<CarBrand> SourceDateList;
    private List<CarBrand> SourceDateList2 = new ArrayList<>();
    private String[] indexItems = {"#"};//头部的索引值
    private EasySideBar sideBar;
    private boolean isLazyRespond = false;//是否为懒加载
    private int maxOffset = 80;//滑动特效 最大偏移量
    private ExpandableListView expandList;
    private CarBrandAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandList = (ExpandableListView) findViewById(R.id.expand_list);
        setCarBrand();
        intiView();
    }

    //添加数据源
    private void setCarBrand() {
        ArrayList<CarStyle> carStyles = new ArrayList<>();
        CarStyle carStyle = new CarStyle();
        carStyle.mStyleName = "海淀区";
        CarStyle carStyle2 = new CarStyle();
        carStyle2.mStyleName = "朝阳区";
        carStyles.add(carStyle);
        carStyles.add(carStyle2);

        CarBrand carBrand = new CarBrand();
        carBrand.mBrandName = "北京市";
        carBrand.mCarStyleList = carStyles;


        ArrayList<CarStyle> carStyles1 = new ArrayList<>();
        CarStyle carStyle1_two = new CarStyle();
        carStyle1_two.mStyleName = "张家口市";
        CarStyle carStyle2_two = new CarStyle();
        carStyle2_two.mStyleName = "石家庄";
        carStyles1.add(carStyle1_two);
        carStyles1.add(carStyle2_two);
        CarBrand carBrand1 = new CarBrand();
        carBrand1.mBrandName = "河北省";
        carBrand1.mCarStyleList = carStyles1;

        ArrayList<CarStyle> carStyles2 = new ArrayList<>();
        CarStyle carStyle1_three = new CarStyle();
        carStyle1_three.mStyleName = "禹州市";
        CarStyle carStyle2_three = new CarStyle();
        carStyle2_three.mStyleName = "许昌市";
        carStyles2.add(carStyle1_three);
        carStyles2.add(carStyle2_three);
        CarBrand carBrand2 = new CarBrand();
        carBrand2.mBrandName = "河南省";
        carBrand2.mCarStyleList = carStyles2;


        ArrayList<CarStyle> carStyles3 = new ArrayList<>();
        CarStyle carStyle1_three3 = new CarStyle();
        carStyle1_three3.mStyleName = "深圳市";
        CarStyle carStyle2_three3 = new CarStyle();
        carStyle2_three3.mStyleName = "佛山";
        carStyles3.add(carStyle1_three3);
        carStyles3.add(carStyle2_three3);
        CarBrand carBrand3 = new CarBrand();
        carBrand3.mBrandName = "广东省";
        carBrand3.mCarStyleList = carStyles3;


        SourceDateList2.add(carBrand);
        SourceDateList2.add(carBrand1);
        SourceDateList2.add(carBrand2);
        SourceDateList2.add(carBrand3);
    }

    private void intiView() {
        sideBar = (EasySideBar) findViewById(R.id.sidebar);
        expandList.setGroupIndicator(null);
        initSideBar();
        initEvents();
        setDate();
    }

    private void setDate() {
        // 设置右侧触摸监听
        sideBar.setOnSelectIndexItemListener(new EasySideBar.OnSelectIndexItemListener() {

            @Override
            public void onSelectIndexItem(int index, String value) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(value.charAt(0));
                if (position != -1) {
                    expandList.setSelection(position);
                }
            }
        });

        SourceDateList = filledData(groups);
        // 根据a-z进行排序
        Collections.sort(SourceDateList, new PinyinComparator());
        //如果设置打开关闭的图片可以设置 背景选择器
        mAdapter = new CarBrandAdapter(this, SourceDateList);
        expandList.setAdapter(mAdapter);
        expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Toast.makeText(MainActivity.this, SourceDateList.get(i).mCarStyleList
                        .get(i1).mStyleName, Toast.LENGTH_LONG);
                Log.i("TAG", "onChildClick: " + SourceDateList.get(i).mCarStyleList
                        .get(i1).mStyleName);
                return true;
            }
        });
    }

    private List<CarBrand> filledData(String[] date) {//获取数据，并根据拼音分类,添加index
        List<CarBrand> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();//索引字母数组
        boolean isGarbled = false;

        for (int i = 0; i < date.length; i++) {
            CarBrand sortModel = new CarBrand();
            sortModel.mBrandName = date[i];
            for (int j = 0; j < SourceDateList2.size(); j++) {
                if (sortModel.mBrandName.equals(SourceDateList2.get(j).mBrandName)) {
                    sortModel.mCarStyleList = SourceDateList2.get(j).mCarStyleList;
                }
            }
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.mSortLetters = sortString.toUpperCase();
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            } else {
                sortModel.mSortLetters = "#";
                isGarbled = true;
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        if (isGarbled) {//出现乱码，将其添加到索引
            indexString.add("#");
        }

        String[] IndexList = Concat(indexItems, indexString.toArray(new String[indexString.size()]));
        sideBar.setIndexItems(IndexList); //只显示有内容部分的字母index

        return mSortList;
    }

    private String[] Concat(String[] a, String[] b) {//合并两个数组
        String[] mIndexItems = new String[a.length + b.length];
        System.arraycopy(a, 0, mIndexItems, 0, a.length);
        System.arraycopy(b, 0, mIndexItems, a.length, b.length);
        return mIndexItems;
    }

    private void initEvents() {
        //设置右侧触摸监听, （此处还需要优化）
        sideBar.setOnSelectIndexItemListener(new EasySideBar.OnSelectIndexItemListener() {

            private int position;

            @Override
            public void onSelectIndexItem(int index, String value) {
                //该字母首次出现的位置

            }
        });
    }

    private void initSideBar() {//初始化sidebar

        sideBar.setLazyRespond(isLazyRespond);
        sideBar.setTextColor(Color.parseColor("#0068B7"));
        sideBar.setMaxOffset(maxOffset);
    }
}
