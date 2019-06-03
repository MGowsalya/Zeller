package com.example.admin.zeller;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zellerr.R;

public class GridItemView extends FrameLayout {

    private TextView textView;

    public GridItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_gridview_text, this);
        textView = getRootView().findViewById(R.id.tv_item_gridview_textview);

    }

    public void display(String text, boolean isSelected) {
        textView.setText(text);
        display(isSelected);
//        display_01();
    }

    public void display(boolean isSelected) {
        textView.setBackgroundResource(isSelected ? R.drawable.gridview_selected : R.drawable.gridview_defalut);
    }
    public  void display_01(){

        textView.setBackgroundResource(R.drawable.gridview_selected);
    }

}
