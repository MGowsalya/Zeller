package com.example.admin.zeller.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.zeller.Fragments.Item_Wise_Detail_List_Fragment;
import com.zellerr.R;

import java.util.ArrayList;

public class Bill_View_Adapter_class extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList<Integer> billno_list;
    private ArrayList<String> date_list;
    private ArrayList<String> time_list;
    private ArrayList<Float> value_list;
    private ArrayList<String> executive_list;

    public static String bn;

    public Bill_View_Adapter_class(FragmentActivity context, ArrayList<Integer> billno_list, ArrayList<String> date_list,
                                   ArrayList<String> time_list, ArrayList<Float> value_list, ArrayList<String> executive_list) {
        super(context, R.layout.bill_view_adapter_layout);
        this.mContext = context;
        this.billno_list = billno_list;
        this.date_list = date_list;
        this.time_list = time_list;
        this.value_list = value_list;
        this.executive_list = executive_list;
    }

    @Override
    public int getCount() {
        return billno_list.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder_billview viewHolder = new ViewHolder_billview();
        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert mInflater != null;
            convertView = mInflater.inflate(R.layout.bill_view_adapter_layout, parent, false);
            viewHolder.tv_bill_no_bill_view_adapt_lay = convertView.findViewById(R.id.tv_bill_no_bill_view_adapt_lay);
            viewHolder.tv_date_bill_view_adapt_lay = convertView.findViewById(R.id.tv_date_bill_view_adapt_lay);
            viewHolder.tv_time_bill_view_adapt_lay = convertView.findViewById(R.id.tv_time_bill_view_adapt_lay);
            viewHolder.tv_value_bill_view_adapt_lay = convertView.findViewById(R.id.tv_value_bill_view_adapt_lay);
            viewHolder.tv_executive_bill_view_adapt_lay = convertView.findViewById(R.id.tv_executive_bill_view_adapt_lay);
            viewHolder.tv_view_bill_view_adapt_lay = convertView.findViewById(R.id.tv_view_bill_view_adapt_lay);
            viewHolder.btn_cancel_bill_view__adapt_lay = convertView.findViewById(R.id.btn_cancel_bill_view__adapt_lay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder_billview) convertView.getTag();
        }
        SpannableString spannableString = new SpannableString("View");
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        viewHolder.tv_view_bill_view_adapt_lay.setText(spannableString);
        final ViewHolder_billview finalViewHolder = viewHolder;
        viewHolder.btn_cancel_bill_view__adapt_lay.setTag(viewHolder);
        viewHolder.tv_view_bill_view_adapt_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bn = finalViewHolder.tv_bill_no_bill_view_adapt_lay.getText().toString();
                //    Toast.makeText(mContext, "cancel clicked.." + number, Toast.LENGTH_SHORT).show();
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                Fragment fragment = new Item_Wise_Detail_List_Fragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.static_page_linearlayout, fragment)
                        .commit();
            }
        });
        viewHolder.btn_cancel_bill_view__adapt_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("bill_list", String.valueOf(billno_list));
                Log.e("pos", String.valueOf(billno_list.get(position)));
                billno_list.remove(position);
                date_list.remove(position);
                time_list.remove(position);
                value_list.remove(position);
                executive_list.remove(position);
                notifyDataSetChanged();
            }
        });


        viewHolder.tv_bill_no_bill_view_adapt_lay.setText(String.valueOf(billno_list.get(position)));
        viewHolder.tv_date_bill_view_adapt_lay.setText(String.valueOf(date_list.get(position)));
        viewHolder.tv_time_bill_view_adapt_lay.setText(String.valueOf(time_list.get(position)));
        viewHolder.tv_value_bill_view_adapt_lay.setText(String.valueOf(value_list.get(position)));
        viewHolder.tv_executive_bill_view_adapt_lay.setText(executive_list.get(position));

        if (viewHolder.tv_executive_bill_view_adapt_lay.getText().length() <= 8) {
            viewHolder.tv_executive_bill_view_adapt_lay.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.executvie_text_size_max));
        } else {
            viewHolder.tv_executive_bill_view_adapt_lay.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.executvie_text_size_min));
        }
        return convertView;
    }


    private class ViewHolder_billview {
        private TextView tv_bill_no_bill_view_adapt_lay, tv_date_bill_view_adapt_lay, tv_time_bill_view_adapt_lay,
                tv_value_bill_view_adapt_lay, tv_executive_bill_view_adapt_lay, tv_view_bill_view_adapt_lay;
        Button btn_cancel_bill_view__adapt_lay;
    }
}