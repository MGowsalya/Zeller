package com.example.admin.zeller.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zellerr.R;

import java.util.ArrayList;

public class ReportsAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> prefix_list;
    private ArrayList<String> date_list;
    private ArrayList<String> billl_num_list;
    private ArrayList<Float> before_tax_list;
    private ArrayList<Float> tax_list;
    private ArrayList<Float> net_total_list;


    public ReportsAdapter(FragmentActivity activity, ArrayList<String> date_list, ArrayList<String> prefix_list,
                          ArrayList<String> billl_num_list, ArrayList<Float> net_total_list, ArrayList<Float> before_tax_list, ArrayList<Float> tax_list) {
        super(activity, R.layout.reports_adapter_layout);
        this.mContext = activity;
        this.date_list = date_list;
        this.prefix_list = prefix_list;
        this.billl_num_list = billl_num_list;
        this.before_tax_list = before_tax_list;
        this.tax_list = tax_list;
        this.net_total_list = net_total_list;
    }


    @Override
    public int getCount() {
        return date_list.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert mInflater != null;
            convertView = mInflater.inflate(R.layout.reports_adapter_layout, parent, false);
            viewHolder.tv_date = convertView.findViewById(R.id.tv_reports_date);
            viewHolder.tv_prefix = convertView.findViewById(R.id.tv_reports_prefix);
            viewHolder.tv_bill_no = convertView.findViewById(R.id.tv_reports_bill_num);
            viewHolder.tv_before_tax = convertView.findViewById(R.id.tv_reports_before_tax_value);
            viewHolder.tv_tax = convertView.findViewById(R.id.tv_reports_tax);
            viewHolder.tv_net_total = convertView.findViewById(R.id.tv_reports_net_amount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            viewHolder.tv_date.setText(date_list.get(position));
            viewHolder.tv_prefix.setText(prefix_list.get(position));
            viewHolder.tv_bill_no.setText(billl_num_list.get(position));
            viewHolder.tv_before_tax.setText(String.valueOf(before_tax_list.get(position)));
            viewHolder.tv_tax.setText(String.valueOf(tax_list.get(position)));
            viewHolder.tv_net_total.setText(String.valueOf(net_total_list.get(position)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_date, tv_prefix, tv_bill_no, tv_before_tax, tv_tax, tv_net_total;
    }
}
