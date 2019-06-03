package com.example.admin.zeller.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zellerr.R;

import java.util.ArrayList;

public class Item_Wise_Detail_List_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> Item_Id_List;
    private ArrayList<String> Item_Name_List;
    private ArrayList<Float> Quantity_List;
    private ArrayList<Float> Amount_List;
    private ArrayList<Float> Tax_List;
    private ArrayList<Float> Total_List;

    public Item_Wise_Detail_List_Adapter(Context context, ArrayList<String> item_id_list, ArrayList<String> item_name_list, ArrayList<Float> quantity_list, ArrayList<Float> amount_list, ArrayList<Float> tax_list, ArrayList<Float> total_list) {
        super(context, R.layout.item_wise_bill_view_adapter_layout);
        this.Item_Id_List = item_id_list;
        this.Item_Name_List = item_name_list;
        this.Quantity_List = quantity_list;
        this.Amount_List = amount_list;
        this.Tax_List = tax_list;
        this.Total_List = total_list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return Item_Id_List.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert mInflater != null;
            convertView = mInflater.inflate(R.layout.item_wise_bill_view_adapter_layout, parent, false);
            viewHolder.tv_item_id_itemwise_bill_view_adapter_lay = convertView.findViewById(R.id.tv_item_id_itemwise_bill_view_adapter_lay);
            viewHolder.tv_item_name_itemwise_bill_view_adapter_lay = convertView.findViewById(R.id.tv_item_name_itemwise_bill_view_adapter_lay);
            viewHolder.tv_quantity_itemwise_bill_view_adapter_lay = convertView.findViewById(R.id.tv_quantity_itemwise_bill_view_adapter_lay);
            viewHolder.tv_amount_itemwise_bill_view_adapter_lay = convertView.findViewById(R.id.tv_amount_itemwise_bill_view_adapter_lay);
            viewHolder.tv_tax_itemwise_bill_view_adapter_lay = convertView.findViewById(R.id.tv_tax_itemwise_bill_view_adapter_lay);
            viewHolder.tv_total_itemwise_bill_view_adapter_lay = convertView.findViewById(R.id.tv_total_itemwise_bill_view_adapter_lay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
try{
        viewHolder.tv_item_id_itemwise_bill_view_adapter_lay.setText(String.valueOf(Item_Id_List.get(position)));
        viewHolder.tv_item_name_itemwise_bill_view_adapter_lay.setText(String.valueOf(Item_Name_List.get(position)));
        viewHolder.tv_quantity_itemwise_bill_view_adapter_lay.setText(String.valueOf(Quantity_List.get(position)));
        viewHolder.tv_amount_itemwise_bill_view_adapter_lay.setText(String.valueOf(Amount_List.get(position)));
        viewHolder.tv_tax_itemwise_bill_view_adapter_lay.setText(String.valueOf(Tax_List.get(position)));
        viewHolder.tv_total_itemwise_bill_view_adapter_lay.setText(String.valueOf(Total_List.get(position)));
        Log.e("Item_id", Item_Id_List.toString());

}catch (Exception e){
            e.printStackTrace();
}
        return convertView;
    }


    class ViewHolder {
        TextView tv_item_id_itemwise_bill_view_adapter_lay, tv_item_name_itemwise_bill_view_adapter_lay,
                tv_quantity_itemwise_bill_view_adapter_lay, tv_amount_itemwise_bill_view_adapter_lay,
                tv_tax_itemwise_bill_view_adapter_lay, tv_total_itemwise_bill_view_adapter_lay;
    }
}