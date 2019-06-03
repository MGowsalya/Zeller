package com.example.admin.zeller.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.admin.zeller.ListItemView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Integer> position_list;

    private String[] billno;
    private String[] date;
    private String[] time;
    private Float[] value;
    private String[] executive;
    public List selectedPositions;

    public ListViewAdapter(View.OnClickListener context, ArrayList<String> billno_list, ArrayList<String> date_list,
                           ArrayList<String> time_list, ArrayList<Float> value_list, ArrayList<String> executive_list, ArrayList<Integer> cancel_list) {
        this.activity = (Activity) context;
        this.billno = billno_list.toArray(new String[0]);
        this.date = date_list.toArray(new String[0]);
        this.time = time_list.toArray(new String[0]);
        this.value = value_list.toArray(new Float[0]);
        this.executive = executive_list.toArray(new String[0]);
        this.position_list = cancel_list;

        selectedPositions = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return billno.length;
    }

    @Override
    public Object getItem(int position) {
        return billno[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItemView listItemView = (convertView ==null) ? new ListItemView(activity):(ListItemView) convertView;
        listItemView.display(billno[position],date[position],time[position], String.valueOf(value[position]),executive[position],selectedPositions.contains(position));
        /*for(int i=0; i<=position_list.size()-1; i++){
            if(position == position_list.get(i)){
                listItemView.display_01();
            }
        }*/
        return listItemView;
    }
}