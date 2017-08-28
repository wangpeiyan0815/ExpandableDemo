package com.wpy.demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.wpy.demo.R;
import com.wpy.demo.bean.CarBrand;

import java.util.List;

/**
 * Created by Victor Liu on 2017/8/28.
 */

public class CarBrandAdapter extends BaseExpandableListAdapter implements SectionIndexer {
    private List<CarBrand> mBrandList = null;
    private Context mContext;

    public CarBrandAdapter(Context mContext, List<CarBrand> list) {
        this.mContext = mContext;
        this.mBrandList = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     */
    public void updateListView(List<CarBrand> list) {
        this.mBrandList = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mBrandList.size();
    }

    public Object getItem(int position) {
        return mBrandList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mBrandList.get(position).mSortLetters.charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mBrandList.get(i).mSortLetters;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getGroupCount() {
        return mBrandList == null ? 0 : mBrandList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mBrandList == null ? 0
                : (mBrandList.get(groupPosition) == null ? 0 : (mBrandList
                .get(groupPosition).mCarStyleList == null ? 0
                : mBrandList.get(groupPosition).mCarStyleList.size()));
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mBrandList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mBrandList.get(groupPosition).mCarStyleList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final CarBrand mContent = mBrandList.get(groupPosition);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.car_brand_item, null);
            viewHolder.mLinerl = (LinearLayout) convertView.findViewById(R.id.mLiner);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.titleBar = (TextView) convertView.findViewById(R.id.title_bar);
            viewHolder.tvLocationCity = (TextView) convertView.findViewById(R.id.tv_location_city);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (0 == groupPosition) {
            viewHolder.mLinerl.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mLinerl.setVisibility(View.GONE);
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.tvLocationCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("TAG", "onClick: " + finalViewHolder.tvLocationCity.getText().toString());
                }
            });
        }
        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(groupPosition);
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (groupPosition == getPositionForSection(section)) {
            viewHolder.titleBar.setVisibility(View.VISIBLE);
            viewHolder.titleBar.setText(mContent.mSortLetters);
        } else {
            viewHolder.titleBar.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(this.mBrandList.get(groupPosition).mBrandName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CarStyleViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new CarStyleViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_child, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.title_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CarStyleViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(this.mBrandList.get(groupPosition).
                mCarStyleList.get(childPosition).mStyleName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    final static class ViewHolder {
        LinearLayout mLinerl;
        TextView tvTitle;
        TextView titleBar;
        TextView tvLocationCity;
    }

    final static class CarStyleViewHolder {
        TextView name;
    }
}
