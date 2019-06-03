package com.example.admin.zeller.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.admin.zeller.GridItemView;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private Activity activity;
    private String[] strings;
    public List selectedPositions;

    private ArrayList<Integer> position_list;

    public GridViewAdapter(ArrayList<String> strings, Activity activity,ArrayList<Integer> position_list) {
        this.strings = strings.toArray(new String[0]);
        this.activity = activity;
        selectedPositions = new ArrayList<>();
        this.position_list = position_list;
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(activity) : (GridItemView) convertView;
        customView.display(strings[position], selectedPositions.contains(position));
           for(int i=0; i<=position_list.size()-1; i++){
            if(position == position_list.get(i)) {
                customView.display_01();
            }
        }
        return customView;
    }
}