package com.example.admin.zeller.Adapters;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.zeller.Fragments.Billing_Fragment;
import com.example.admin.zeller.MainActivity;
import com.zellerr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Objects;

public class BillingAdapter extends ArrayAdapter<String> {

    private ArrayList<String> name;
    private ArrayList<Float> qnty;
    private ArrayList<Float> price;
    private Context mContext;
    private Float gtotal_calc = 0.00f;
    private Float quantity;

    public BillingAdapter(@NonNull Context context, ArrayList<String> i_names, ArrayList<Float> i_price, ArrayList<Float> i_qnty) {
        super(context, R.layout.billing_product_count_layout);
        this.name = i_names;
        this.price = i_price;
        this.qnty = i_qnty;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert mInflater != null;
            convertView = mInflater.inflate(R.layout.billing_product_count_layout, parent, false);
            mViewHolder.tv_name = convertView.findViewById(R.id.tv_item_name);
            mViewHolder.tv_count = convertView.findViewById(R.id.tv_item_price);
            mViewHolder.et_custom_edittext = convertView.findViewById(R.id.et_custom_edittext);
            mViewHolder.ib_plus_icon = convertView.findViewById(R.id.ib_plus_icon);
            mViewHolder.ib_minus_icon = convertView.findViewById(R.id.ib_minus_icon);
            mViewHolder.ib_cross_icon = convertView.findViewById(R.id.ib_cross_icon);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            mViewHolder.tv_name.setText(name.get(position));
            mViewHolder.et_custom_edittext.setText(String.valueOf(qnty.get(position)));
            mViewHolder.tv_count.setText(String.valueOf(price.get(position)));
            if (position == Billing_Fragment.pos) {
                final ViewHolder finalMViewHolder4 = mViewHolder;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        finalMViewHolder4.tv_name.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                        finalMViewHolder4.tv_count.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                        finalMViewHolder4.et_custom_edittext.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                    }
                }, 2000);

                mViewHolder.tv_name.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                mViewHolder.tv_count.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                mViewHolder.et_custom_edittext.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));
            }

            if (mViewHolder.tv_name.getText().length() <= 9) {
                mViewHolder.tv_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.result_font_max));
            } else {
                mViewHolder.tv_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.result_font_min));
            }
        } catch (IndexOutOfBoundsException aib) {
            aib.printStackTrace();
        }
        mViewHolder.et_custom_edittext.setSelection(mViewHolder.et_custom_edittext.getText().toString().length());

        final ViewHolder finalMViewHolder = mViewHolder;
        finalMViewHolder.ib_plus_icon.setTag(position);
        mViewHolder.et_custom_edittext.setTag(position);
        finalMViewHolder.ib_plus_icon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String n = finalMViewHolder.tv_name.getText().toString();
                Float value = Float.valueOf(finalMViewHolder.et_custom_edittext.getText().toString());
                Float value_01 = value +1;
             //   @SuppressLint("DefaultLocale") String format_value = String.format("%.3f",value_01);
              //  finalMViewHolder.et_custom_edittext.setText(format_value);
                finalMViewHolder.et_custom_edittext.setText(String.valueOf(value_01));
                finalMViewHolder.et_custom_edittext.setSelection(finalMViewHolder.et_custom_edittext.getText().toString().length());

                set_before_tax();

                ContentValues contentValues = new ContentValues();
                contentValues.put("QTY", finalMViewHolder.et_custom_edittext.getText().toString());
                contentValues.put("AMOUNT", finalMViewHolder.tv_count.getText().toString());
                MainActivity.db.update("BILLING", contentValues, "I_NAME='" + n + "' and BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'", null);
                DBExport();

                if (finalMViewHolder.et_custom_edittext.getText().toString().isEmpty()) {
                    quantity = 0.0f;
                } else {
                    quantity = Float.valueOf(finalMViewHolder.et_custom_edittext.getText().toString());
                }
                update_GTotal();
                set_tax_inc();

            }
        });
        final ViewHolder finalMViewHolder3 = mViewHolder;
        finalMViewHolder3.ib_minus_icon.setTag(position);
        mViewHolder.et_custom_edittext.setTag(position);
        finalMViewHolder3.ib_minus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String val = finalMViewHolder3.et_custom_edittext.getText().toString();
                Float v = Float.valueOf(val);
                Float c = v-1;

                if (finalMViewHolder3.et_custom_edittext.getText().toString().equalsIgnoreCase("1.0")) {
                    Toast.makeText(mContext, R.string.one_mini_value_txt, Toast.LENGTH_SHORT).show();
                    Log.e("val", String.valueOf(v));
                }
                else if(c<=0.00){
                  //  Toast.makeText(mContext, "0 value", Toast.LENGTH_SHORT).show();
                }
                else {
                    String n = finalMViewHolder.tv_name.getText().toString();
                    Float value = Float.valueOf(finalMViewHolder3.et_custom_edittext.getText().toString());
                    Log.e("value: ", String.valueOf(value));
                    Log.e("value-1: ", String.valueOf(value - 1));

               //     @SuppressLint("DefaultLocale") String format_value = String.format("%.3f",value-1);
                    //   finalMViewHolder.et_custom_edittext.setText(format_value);

                    finalMViewHolder.et_custom_edittext.setText(String.valueOf(value - 1));
                    finalMViewHolder.et_custom_edittext.setSelection(finalMViewHolder.et_custom_edittext.getText().toString().length());

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("QTY", finalMViewHolder.et_custom_edittext.getText().toString());
                    contentValues.put("AMOUNT", finalMViewHolder.tv_count.getText().toString());
                    MainActivity.db.update("BILLING", contentValues, "I_NAME='" + n + "' and BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'", null);
                    DBExport();
                    set_tax_inc();
                    set_before_tax();

                    if (finalMViewHolder3.et_custom_edittext.getText().toString().isEmpty()) {
                        quantity = 0.0f;
                    } else {
                        quantity = Float.valueOf(finalMViewHolder3.et_custom_edittext.getText().toString());
                    }
                    update_GTotal();
                }
            }
        });
        mViewHolder.ib_cross_icon.setTag(position);
        mViewHolder.ib_cross_icon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                ArrayList<Float> amount_list = new ArrayList<>();
                ArrayList<String> name_list = new ArrayList<>();
                ArrayList<Float> qnty_list = new ArrayList<>();
                Float before_tax = 0.00f;
                String n = finalMViewHolder.tv_name.getText().toString();
                set_tax(n);
                MainActivity.db.delete("BILLING", "I_NAME='" + n + "' and BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'", null);
                DBExport();
                name.remove(position);
                price.remove(position);
                qnty.remove(position);
                notifyDataSetChanged();
                String select_details = "SELECT AMOUNT from BILLING where BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'";
                Cursor cursor_details = MainActivity.db.rawQuery(select_details, null);
                if (cursor_details.moveToFirst()) {
                    do {
                        Float amount = cursor_details.getFloat(0);
                        amount_list.add(amount);
                    } while (cursor_details.moveToNext());
                }
                cursor_details.close();
                for (int i = 0; i < amount_list.size(); i++) {
                    before_tax = before_tax + amount_list.get(i);
                }
                Billing_Fragment.tv_before_tax_value.setText(String.valueOf(before_tax));

                String select = "SELECT I_NAME,QTY,AMOUNT from BILLING where BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'";
                Cursor cursor = MainActivity.db.rawQuery(select, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(0);
                        Float qty = cursor.getFloat(1);
                        name_list.add(name);
                        qnty_list.add(qty);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                BillingAdapter myAdapter = new BillingAdapter(Objects.requireNonNull(getContext()), name_list, amount_list, qnty_list);
                Billing_Fragment.lv_billing_product_count.setAdapter(myAdapter);

                int num_of_items = name_list.size();
                if (num_of_items == 0) {
                    Billing_Fragment.tv_no_of_items.setVisibility(View.INVISIBLE);
                } else {
                    Billing_Fragment.tv_no_of_items.setVisibility(View.VISIBLE);
                    Billing_Fragment.tv_no_of_items.setText(String.valueOf(num_of_items + " items"));
                }
            }
        });
        final ViewHolder finalMViewHolder1 = mViewHolder;
        final ViewHolder finalMViewHolder2 = mViewHolder;
        mViewHolder.et_custom_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("LongLogTag")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String q = finalMViewHolder1.et_custom_edittext.getText().toString();
                if(q.equals(".")){
                    finalMViewHolder.et_custom_edittext.setText("0.");
                    finalMViewHolder.et_custom_edittext.setSelection(finalMViewHolder.et_custom_edittext.getText().toString().length());
                }
                String str_rate = null;
                String name = finalMViewHolder1.tv_name.getText().toString();
                String select_rate = "SELECT RATE from BILLING where " +
                        "BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "' and I_NAME ='" + name + "'";
                Cursor cursor1 = MainActivity.db.rawQuery(select_rate, null);
                if (cursor1.getCount() > 0) {
                    if (cursor1.moveToFirst()) {
                        do {
                            str_rate = cursor1.getString(0);
                        } while (cursor1.moveToNext());
                    }
                    cursor1.close();
                } else {
                    str_rate = String.valueOf(0.00f);
                }
                if (finalMViewHolder3.et_custom_edittext.getText().toString().isEmpty()) {
                    quantity = 0.0f;
                } else {
                    quantity = Float.valueOf(finalMViewHolder3.et_custom_edittext.getText().toString());
                }
                Float rate = Float.valueOf(str_rate);
                Float calc = quantity * rate;
                @SuppressLint("DefaultLocale") String result = String.format("%.2f", calc);
                finalMViewHolder2.tv_count.setText(String.valueOf(result));

                String select_GTotal = "SELECT CGST,SGST,RATE from BILLING where I_NAME='" + name + "' and BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'";
                Cursor cursor_GTotal = MainActivity.db.rawQuery(select_GTotal, null);
                if (cursor_GTotal.moveToFirst()) {
                    do {
                        Float cgst = cursor_GTotal.getFloat(0);
                        Float sgst = cursor_GTotal.getFloat(1);
                        Float rates = cursor_GTotal.getFloat(2);
                        Float tax = (cgst + sgst) / 100;
                        Float gtotal_01 = rates * tax;
                        Float gtotal = rates + gtotal_01;
                        gtotal_calc = gtotal * quantity;
                    } while (cursor_GTotal.moveToNext());
                }
                cursor_GTotal.close();
                //updation:
                ContentValues contentValues = new ContentValues();
                contentValues.put("AMOUNT", calc);
                contentValues.put("QTY", finalMViewHolder3.et_custom_edittext.getText().toString());
                contentValues.put("GTOTAL", gtotal_calc);
                Log.e("content_values_textwatcher:", String.valueOf(contentValues));
                MainActivity.db.update("BILLING", contentValues,
                        "I_NAME='" + name + "' and BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'", null);
                DBExport();
                set_before_tax();
                set_tax_inc();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return convertView;
    }

    private void set_before_tax() {
        Float rate;
        Float initial_value = 0.00f;
        ArrayList<String> arrayList_item_names = new ArrayList<>();
        ArrayList<Float> arrayList_item_rates = new ArrayList<>();
        String select_name = "SELECT I_NAME from BILLING where BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'";
        Cursor cursor_name = MainActivity.db.rawQuery(select_name, null);
        if (cursor_name.moveToFirst()) {
            do {
                String name = cursor_name.getString(0);
                arrayList_item_names.add(name);
            } while (cursor_name.moveToNext());
        }
        cursor_name.close();
        for (int j = 0; j < arrayList_item_names.size(); j++) {
            if (j <= arrayList_item_names.size() - 1) {
                String select_rate = "SELECT AMOUNT from BILLING " +
                        "where I_NAME='" + arrayList_item_names.get(j) + "' and BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'";
                Cursor cursor_rate = MainActivity.db.rawQuery(select_rate, null);
                if (cursor_rate.moveToFirst()) {
                    do {
                        rate = cursor_rate.getFloat(0);
                        arrayList_item_rates.add(rate);
                    } while (cursor_rate.moveToNext());
                }
                cursor_rate.close();
            }
            initial_value = initial_value + arrayList_item_rates.get(j);
        }
        Billing_Fragment.tv_before_tax_value.setText(String.valueOf(initial_value));
    }

    private void set_tax_inc() {
        //     Float cgst_tax,sgst_tax;
        ArrayList<String> arrayList_names = new ArrayList<>();
        ArrayList<Float> arrayList_cgst = new ArrayList<>();
        ArrayList<Float> arrayList_sgst = new ArrayList<>();
        String select = "SELECT I_NAME from BILLING where BILLNO = '" + Billing_Fragment.tv_bill_number.getText().toString() + "'";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                String names = cursor.getString(0);
                arrayList_names.add(names);
            } while (cursor.moveToNext());
        }
        cursor.close();
        for (int i = 0; i <= arrayList_names.size(); i++) {
            if (i <= arrayList_names.size() - 1) {
                String select_tax = "SELECT CGST,SGST,RATE,QTY from BILLING where I_NAME ='" + arrayList_names.get(i) + "' and BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'";
                Cursor cursor_tax = MainActivity.db.rawQuery(select_tax, null);
                if (cursor_tax.moveToFirst()) {
                    do {
                        Float cgst = cursor_tax.getFloat(0);
                        Float sgst = cursor_tax.getFloat(1);
                        Float rate = cursor_tax.getFloat(2);
                        Float qnty = cursor_tax.getFloat(3);

                        Float calc_01 = rate * cgst / 100;
                        Float calc_02 = rate * sgst / 100;

                        Float tax_c = calc_01 * qnty;
                        Float tax_s = calc_02 * qnty;
                        arrayList_cgst.add(tax_c);
                        arrayList_sgst.add(tax_s);
                        Log.e("tax_c", String.valueOf(tax_c));
                        Log.e("tax_c", String.valueOf(tax_s));

                    } while (cursor_tax.moveToNext());
                }
                cursor_tax.close();
                Float cal_01 = 0.0f;
                Float cal_02 = 0.0f;
                for (int cgst = 0; cgst < arrayList_cgst.size(); cgst++) {
                    cal_01 = cal_01 + arrayList_cgst.get(cgst);
                }
                @SuppressLint("DefaultLocale") String result_01 = String.format("%.2f", cal_01);
                Billing_Fragment.tv_cgst_tax_value.setText(result_01);
                for (int sgst = 0; sgst < arrayList_sgst.size(); sgst++) {
                    cal_02 = cal_02 + arrayList_sgst.get(sgst);
                }
                @SuppressLint("DefaultLocale") String result_02 = String.format("%.2f", cal_02);
                Billing_Fragment.tv_sgst_tax_value.setText(result_02);
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private void set_tax(String n) {

        Float cgst_tax, sgst_tax;
        String select_tax = "SELECT CGST,SGST,RATE,QTY from BILLING where I_NAME ='" + n + "' and BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'";
        Cursor cursor_tax = MainActivity.db.rawQuery(select_tax, null);
        if (cursor_tax.moveToFirst()) {
            do {
                Float cgst = cursor_tax.getFloat(0);
                Float sgst = cursor_tax.getFloat(1);
                Float rate = cursor_tax.getFloat(2);
                Float qnty = cursor_tax.getFloat(3);

                Float calc_01 = rate * cgst / 100;
                Float calc_02 = rate * sgst / 100;

                Float tax_c = calc_01 * qnty;
                Float tax_s = calc_02 * qnty;

                if (Billing_Fragment.tv_cgst_tax_value.getText().toString().isEmpty()) {
                    cgst_tax = 0.00f;
                } else {
                    cgst_tax = Float.valueOf(Billing_Fragment.tv_cgst_tax_value.getText().toString());
                }
                if (Billing_Fragment.tv_sgst_tax_value.getText().toString().isEmpty()) {
                    sgst_tax = 0.00f;
                } else {
                    sgst_tax = Float.valueOf(Billing_Fragment.tv_sgst_tax_value.getText().toString());
                }
                Float c_tax = cgst_tax - tax_c;
                Float s_tax = sgst_tax - tax_s;
                @SuppressLint("DefaultLocale") String result_1 = String.format("%.2f", c_tax);
                @SuppressLint("DefaultLocale") String result_2 = String.format("%.2f", s_tax);
                Billing_Fragment.tv_cgst_tax_value.setText(result_1);
                Billing_Fragment.tv_sgst_tax_value.setText(result_2);

            } while (cursor_tax.moveToNext());
        }
        cursor_tax.close();
    }

    private void update_GTotal() {

        String select_GTotal = "SELECT CGST,SGST,RATE from BILLING where I_NAME='" + name + "' and BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'";
        Cursor cursor_GTotal = MainActivity.db.rawQuery(select_GTotal, null);
        if (cursor_GTotal.moveToFirst()) {
            do {
                Float cgst = cursor_GTotal.getFloat(0);
                Float sgst = cursor_GTotal.getFloat(1);
                Float rate = cursor_GTotal.getFloat(3);
                Float tax = (cgst + sgst) / 100;
                Float gtotal_01 = rate * tax;
                Float gtotal = rate + gtotal_01;
                gtotal_calc = gtotal * quantity;
            } while (cursor_GTotal.moveToNext());
        }
        cursor_GTotal.close();
        ContentValues cv_GTotal_update = new ContentValues();
        cv_GTotal_update.put("GTOTAL", gtotal_calc);
        MainActivity.db.update("BILLING", cv_GTotal_update,
                "I_NAME='" + name + "' and BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'", null);
        DBExport();
    }

    private void DBExport() {

        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + "com.zellerr" + "/databases/" + "Zeller.db";
        String backupDBPath = "Zeller_Demo.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ViewHolder {
        TextView tv_name;
        TextView tv_count;
        ImageButton ib_plus_icon, ib_minus_icon, ib_cross_icon;
        EditText et_custom_edittext;
    }
}
