package com.example.admin.zeller.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.zeller.Adapters.Item_Wise_Detail_List_Adapter;
import com.example.admin.zeller.Adapters.ListViewAdapter;
import com.example.admin.zeller.ListItemView;
import com.example.admin.zeller.MainActivity;
import com.example.admin.zeller.SplashScreen_Act;
import com.zellerr.R;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.example.admin.zeller.Fragments.Bill_View_Fragment.billno_list;
import static com.example.admin.zeller.Fragments.Bill_View_Fragment.executive_list;
import static com.example.admin.zeller.Fragments.Bill_View_Fragment.highlight_cancel_list;
import static com.example.admin.zeller.Fragments.Bill_View_Fragment.time_list;
import static com.example.admin.zeller.Fragments.Bill_View_Fragment.value_list;

public class Item_Wise_Detail_List_Fragment extends Fragment implements Runnable {
    TextView tv_bill_number_item_wise_lay, tv_date_item_wise_lay, tv_time_item_wise_lay,
            tv_executive_item_wise_lay, tv_tax_item_wise_lay,
            tv_amount__item_wise_lay, tv_total_amount_item_wise_lay;
    @SuppressLint("StaticFieldLeak")
    public static ListView lv_item_wise_listview;
    Button btn_cancel_item_wise_lay;
    @SuppressLint("StaticFieldLeak")
    public static Button btn_print_item_wise_lay;
    Button btn_bill_cancel_item_wise_lay;
    ArrayList<String> Item_Id_List;
    ArrayList<String> Item_Name_List;
    ArrayList<Float> Quantity_List;
    ArrayList<Float> Amount_List;
    ArrayList<Float> Tax_List;
    ArrayList<Float> Total_List;

    Float amount = 0.00f, tax = 0.00f, total = 0.00f;
    String cursor_date, cursor_time, cursor_username;
    int cancel_value;
    Fragment fragment;
    FragmentTransaction ft;

    ArrayList<Integer> print_sno_list;
    ArrayList<String> print_i_name_list;
    ArrayList<Float> print_qnty_list;
    ArrayList<Float> print_G_total_list;
    ArrayList<Float> print_rate_list;
    ArrayList<Float> print_cgst_list;
    ArrayList<Float> print_sgst_list;

    ArrayList<Float> print_cgst_tax_value;
    ArrayList<Float> print_sgst_tax_value;
    String BILL;
    StringBuilder print_details = new StringBuilder();
    String str_company_name, str_add_01, str_add_02, str_add_03, str_place, str_gst_no, str_phone_no;
    String str_device_name, str_mac_address;

    protected static final String TAG = "TAG";
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    OutputStream os;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.item_vise_bill_view_layout, null, false);

        View decorView = Objects.requireNonNull(getActivity()).getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        tv_bill_number_item_wise_lay = view.findViewById(R.id.tv_bill_number_item_wise_lay);
        tv_date_item_wise_lay = view.findViewById(R.id.tv_date_item_wise_lay);
        tv_time_item_wise_lay = view.findViewById(R.id.tv_time_item_wise_lay);
        tv_executive_item_wise_lay = view.findViewById(R.id.tv_executive_item_wise_lay);
        tv_tax_item_wise_lay = view.findViewById(R.id.tv_tax_item_wise_lay);
        tv_amount__item_wise_lay = view.findViewById(R.id.tv_amount__item_wise_lay);
        tv_total_amount_item_wise_lay = view.findViewById(R.id.tv_total_amount_item_wise_lay);
        lv_item_wise_listview = view.findViewById(R.id.lv_item_wise_listview);
        btn_cancel_item_wise_lay = view.findViewById(R.id.btn_cancel_item_wise_lay);
        btn_print_item_wise_lay = view.findViewById(R.id.btn_print_item_wise_lay);
        btn_bill_cancel_item_wise_lay = view.findViewById(R.id.btn_bill_cancel_item_wise_lay);

        String role = null;
        String select_user = "SELECT ROLE from USER where STATUS=1";
        Cursor cursor_user = SplashScreen_Act.db.rawQuery(select_user,null);
        if(cursor_user.moveToFirst()){
            do{
                role = cursor_user.getString(0);
            }while (cursor_user.moveToNext());
        }
        cursor_user.close();
        assert role != null;
        if(role.equalsIgnoreCase("Admin")){
            btn_print_item_wise_lay.setVisibility(View.VISIBLE);
        }
        else {
            btn_print_item_wise_lay.setVisibility(View.GONE);
        }
        /* if ((SplashScreen_Act.visible == 1) || (Login_Act.visible == 1)) {
            btn_print_item_wise_lay.setVisibility(View.VISIBLE);
        } else {
            btn_print_item_wise_lay.setVisibility(View.GONE);
        }*/
        Item_Id_List = new ArrayList<>();
        Item_Name_List = new ArrayList<>();
        Quantity_List = new ArrayList<>();
        Amount_List = new ArrayList<>();
        Tax_List = new ArrayList<>();
        Total_List = new ArrayList<>();
        tv_bill_number_item_wise_lay.setText(ListItemView.bn);
        String select = "SELECT I_ID,I_NAME,QTY,AMOUNT,GTOTAL,BDATE,USERNAME,BTIME,CANCELLED from BILLING where BILLNO = '" + ListItemView.bn + "' order by I_ID";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                Float qty = cursor.getFloat(2);
                Float amount = cursor.getFloat(3);
                Float grand_total = cursor.getFloat(4);
                cursor_date = cursor.getString(5);
                cursor_username = cursor.getString(6);
                cursor_time = cursor.getString(7);
                cancel_value  = cursor.getInt(8);
                Log.e("cursor_values:", id + " " + name + " " + qty + " " + amount);
                Item_Id_List.add(id);
                Item_Name_List.add(name);
                Quantity_List.add(qty);
                Amount_List.add(amount);
                //   Tax_List.add(amount);
                Total_List.add(grand_total);
                Log.e("Item_id_bill_view", Item_Id_List.toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
        try {
            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy/MM/dd");
            Date date = sdfSource.parse(cursor_date);
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
            cursor_date = sdfDestination.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(cancel_value==0){
            btn_bill_cancel_item_wise_lay.setText(String.valueOf("Cancel"));
            btn_bill_cancel_item_wise_lay.setBackground(getResources().getDrawable(R.drawable.red_button));
        }
        else if(cancel_value==1){
            btn_bill_cancel_item_wise_lay.setText(String.valueOf("Uncancel"));
            btn_bill_cancel_item_wise_lay.setBackground(getResources().getDrawable(R.drawable.orange_button));
        }
        tv_date_item_wise_lay.setText(cursor_date);
        tv_time_item_wise_lay.setText(cursor_time);
        tv_executive_item_wise_lay.setText(cursor_username);
        for (int t = 0; t <= Amount_List.size() - 1; t++) {
            Float tt = Total_List.get(t) - Amount_List.get(t);
            @SuppressLint("DefaultLocale") String result = String.format("%.2f", tt);
            Tax_List.add(Float.valueOf(result));
        }
        Item_Wise_Detail_List_Adapter item_wise_detail_list_adapter = new Item_Wise_Detail_List_Adapter(getContext(), Item_Id_List, Item_Name_List, Quantity_List, Amount_List, Tax_List, Total_List);
        lv_item_wise_listview.setAdapter(item_wise_detail_list_adapter);
        for (int i = 0; i <= Amount_List.size() - 1; i++) {
            amount = amount + Amount_List.get(i);
        }
        @SuppressLint("DefaultLocale") String result_01 = String.format("%.2f", amount);
        tv_amount__item_wise_lay.setText(result_01);

        for (int j = 0; j <= Tax_List.size() - 1; j++) {
            tax = tax + Tax_List.get(j);
        }
        @SuppressLint("DefaultLocale") String result_02 = String.format("%.2f", tax);
        tv_tax_item_wise_lay.setText(result_02);

        for (int k = 0; k <= Total_List.size() - 1; k++) {
            total = total + Total_List.get(k);
        }
        @SuppressLint("DefaultLocale") String result_03 = String.format("%.2f", total);
        tv_total_amount_item_wise_lay.setText(String.valueOf(result_03));

        btn_cancel_item_wise_lay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                fragment = new Bill_View_Fragment();
                ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.static_page_linearlayout, fragment);
                ft.commit();
            }
        });
        btn_print_item_wise_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Billing_Fragment.btn_print.callOnClick();
            }*/
                print_sno_list = new ArrayList<>();
                print_i_name_list = new ArrayList<>();
                print_qnty_list = new ArrayList<>();
                print_rate_list = new ArrayList<>();
                print_G_total_list = new ArrayList<>();
                print_cgst_list = new ArrayList<>();
                print_sgst_list = new ArrayList<>();
                print_cgst_tax_value = new ArrayList<>();
                print_sgst_tax_value = new ArrayList<>();
                if (check_values_before_print()) {
                    Float total = Float.valueOf(tv_total_amount_item_wise_lay.getText().toString());
                    ContentValues cv_bill_update = new ContentValues();
                    cv_bill_update.put("NTOTAL", total);
                    MainActivity.db.update("BILLING", cv_bill_update, "BILLNO='" + tv_bill_number_item_wise_lay.getText().toString() + "'", null);
                    ScanBT();
                    print_details.setLength(0);
                    Thread t = new Thread() {
                        public void run() {
                            try {
                                if (os == null) {
                                    os = mBluetoothSocket
                                            .getOutputStream();
                                } else
                                    os = mBluetoothSocket
                                            .getOutputStream();
                                String select = "Select CC_NAME,ADD_1,ADD_2,ADD_3,PLACE," +
                                        "GSTNO,PHONENO from COMPANY where CC_ID='" + 1 + "'";
                                Cursor cursor = MainActivity.db.rawQuery(select, null);
                                if (cursor.moveToFirst()) {
                                    do {
                                        str_company_name = String.format("%27s", cursor.getString(0).toUpperCase());
                                        str_add_01 = cursor.getString(1);
                                        if (str_add_01.length() <= 5) {
                                            str_add_01 = String.format("%18s", cursor.getString(1));
                                        } else if (str_add_01.length() >= 10) {
                                            str_add_01 = String.format("%22s", cursor.getString(1));
                                        }
                                        str_add_02 = String.format("%27s", cursor.getString(2));
                                        str_add_03 = String.format("%18s", cursor.getString(3));
                                        str_place = String.format("%1s", cursor.getString(4));
                                        str_gst_no = String.format("%10s", cursor.getString(5));
                                        str_phone_no = String.format("%10s", cursor.getString(6));
                                        if (str_gst_no == null || str_gst_no.isEmpty()) {
                                            print_details.append(str_company_name).append("\n").append(str_add_01).append(",").append(str_add_02)
                                                    .append("\n").append(str_add_03)
                                                    .append("-").append(str_place).append(",").append("\n")
                                                    .append("       Ph No:").append(str_phone_no).append(".").append("\n");
                                        } else {
                                            print_details.append(str_company_name).append("\n").append(str_add_01).append(",").append("\n")
                                                    .append(str_add_02).append(",").append("\n").append(str_add_03)
                                                    .append("-").append(str_place).append(",").append("\n").append("       GST NO:").append(cursor.getString(5))
                                                    .append(",").append("\n").append("       Ph No:").append(str_phone_no).append(".").append("\n");

                                        }
                                    } while (cursor.moveToNext());
                                }
                                cursor.close();
                                String b_date = null, b_time = null;
                                String select_01 = "SELECT BDATE,BTIME from BILLING where " +
                                        "BILLNO ='" + tv_bill_number_item_wise_lay.getText().toString() + "'";
                                Cursor cursor_01 = MainActivity.db.rawQuery(select_01, null);
                                if (cursor_01.moveToFirst()) {
                                    do {
                                        b_date = cursor_01.getString(0);
                                        try {
                                            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy/MM/dd");
                                            Date date = sdfSource.parse(b_date);
                                            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
                                            b_date = sdfDestination.format(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        b_time = cursor_01.getString(1);
                                    } while (cursor_01.moveToNext());
                                }
                                cursor_01.close();
                                print_details.append("-------------------------------" + "\n");
                                print_details.append("Bill No:").append(Billing_Fragment.tv_bill_number.getText().toString()).append("\n");
                                print_details.append("DATE   :").append(b_date).append(",").append("TIME:").append(b_time).append("\n");
                                print_details.append("-------------------------------" + "\n");

                                String str_item = "ITEM";
                                String str_rte = "RATE";
                                String str_qty = "QTY";
                                String str_amount = "AMOUNT";
                                String str_title = String.format("%-8s %8s %5s %8s", str_item, str_rte, str_qty, str_amount);
                                print_details.append(str_title).append("\n");
                                print_details.append("-------------------------------" + "\n");

                                Float n_total = null;
                                int temp_qty = 0;
                                String select_billing = "SELECT I_NAME,QTY,GTOTAL,NTOTAL,CGST,SGST from BILLING where BILLNO='" + tv_bill_number_item_wise_lay.getText().toString() + "'";
                                Cursor cursor_billing = MainActivity.db.rawQuery(select_billing, null);
                                if (cursor_billing.moveToFirst()) {
                                    do {
                                        String i_name = cursor_billing.getString(0);
                                        Float qty = cursor_billing.getFloat(1);
                                        Float gtotal = cursor_billing.getFloat(2);
                                        n_total = cursor_billing.getFloat(3);
                                        Float cgst = cursor_billing.getFloat(4);
                                        Float sgst = cursor_billing.getFloat(5);
                                        print_i_name_list.add(i_name);
                                        print_qnty_list.add(qty);
                                        @SuppressLint("DefaultLocale") String format_gtotal = String.format("%.2f", gtotal);
                                        print_G_total_list.add(Float.valueOf(format_gtotal));
                                        print_cgst_list.add(cgst);
                                        print_sgst_list.add(sgst);
                                    } while (cursor_billing.moveToNext());
                                }
                                cursor_billing.close();
                                for (int j = 0; j <= print_i_name_list.size() - 1; j++) {
                                    String select_rate = "SELECT RATE from BILLING where BILLNO='" + tv_bill_number_item_wise_lay.getText().toString() + "'" +
                                            " and I_NAME='" + print_i_name_list.get(j) + "'";
                                    Cursor cursor_rate = MainActivity.db.rawQuery(select_rate, null);
                                    if (cursor_rate.moveToFirst()) {
                                        do {
                                            Float rate = cursor_rate.getFloat(0);
                                            @SuppressLint("DefaultLocale") String str_rate = String.format("%.2f", rate);

                                            print_rate_list.add(Float.valueOf(str_rate));
                                        } while (cursor_rate.moveToNext());
                                    }
                                    cursor_rate.close();
                                }
                                for (int t = 0; t <= print_cgst_list.size() - 1; t++) {
                                    Float calc = (print_cgst_list.get(t) * print_rate_list.get(t)) / 100;
                                    Float c = calc * print_qnty_list.get(t);
                                    Toast.makeText(getContext(), "calc:" + calc + c, Toast.LENGTH_SHORT).show();
                                    Log.e("calc_cgst:", String.valueOf(calc));
                                    Log.e("calc_cgst_qnty:", String.valueOf(c));
                                    @SuppressLint("DefaultLocale") String cgst = String.format("%.2f", c);
                                    print_cgst_tax_value.add(Float.valueOf(cgst));
                                }
                                for (int t1 = 0; t1 <= print_sgst_list.size() - 1; t1++) {
                                    Float calc = (print_sgst_list.get(t1) * print_rate_list.get(t1)) / 100;
                                    Float c = calc * print_qnty_list.get(t1);
                                    Toast.makeText(getContext(), "calc:" + calc + c, Toast.LENGTH_SHORT).show();
                                    Log.e("calc_sgst:", String.valueOf(calc));
                                    Log.e("calc_sgst_qnty:", String.valueOf(c));
                                    @SuppressLint("DefaultLocale") String sgst = String.format("%.2f", c);
                                    print_sgst_tax_value.add(Float.valueOf(sgst));
                                }

                                append_print_details();
                                for (int i = 0; i <= print_qnty_list.size() - 1; i++) {
                                    temp_qty = (int) (temp_qty + print_qnty_list.get(i));
                                }

                                print_details.append("--------------------------------" + "\n");
                                String str_total_qty_values = String.format("%-8s", "Total Qty:");
                                String str_total_amt_values = String.format("%-10s", "Total Amt:");
                                @SuppressLint("DefaultLocale") String net_total = String.format("%.2f", n_total);
                                String nt_total = String.format("%9s",net_total);
                                print_details.append(str_total_qty_values).append(temp_qty).append(",").append(str_total_amt_values).append(nt_total).append("\n");
                                print_details.append("------------------------------" + "\n");

                                String select_03 = "SELECT F_MESSAGE_1,F_MESSAGE_2 FROM SETTING";
                                Cursor cursor1 = MainActivity.db.rawQuery(select_03, null);
                                String str_footer_msg_01 = null, str_footer_msg_02 = null;
                                if (cursor1.moveToFirst()) {
                                    do {
                                        str_footer_msg_01 = String.format("%-10s", cursor1.getString(0));
                                        str_footer_msg_02 = String.format("%-10s", cursor1.getString(1));
                                    } while (cursor1.moveToNext());
                                }
                                cursor1.close();
                                if ((str_footer_msg_01 == null) || (str_footer_msg_01.isEmpty())) {
                                    print_details.append("\n");
                                } else {
                                    print_details.append(str_footer_msg_01).append("-").append(str_footer_msg_02);
                                }
                                print_details.append("\n");
                                print_details.append("\n");
                                print_details.append("\n");

                                BILL = String.valueOf(print_details);

                                BILL = BILL + "\n\n ";
                                os.write(BILL.getBytes());
                                //This is printer specific code you can comment ==== > Start

                                // Setting height
                                int gs = 29;
                                os.write(intToByteArray(gs));
                                int h = 104;
                                os.write(intToByteArray(h));
                                int n = 162;
                                os.write(intToByteArray(n));

                                // Setting Width
                                int gs_width = 29;
                                os.write(intToByteArray(gs_width));
                                int w = 119;
                                os.write(intToByteArray(w));
                                int n_width = 2;
                                os.write(intToByteArray(n_width));

                            } catch (Exception e) {
                                Log.e("MainActivity", "Exe ", e);
                            }
                        }
                    };
                    t.start();
                  /*  Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clear_Adapter_method();
                        }
                    }, 15000);*/
                } else {
                    Toast.makeText(getContext(), R.string.pls_enter_bill_value_txt, Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_bill_cancel_item_wise_lay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                String b_no = tv_bill_number_item_wise_lay.getText().toString();
                String cancel_text = btn_bill_cancel_item_wise_lay.getText().toString();

                if(cancel_text.equalsIgnoreCase("Cancel")) {
                   // display_01();
                    ContentValues contentValues_cancel = new ContentValues();
                    contentValues_cancel.put("CANCELLED", 1);
                    SplashScreen_Act.db.update("BILLING", contentValues_cancel, "BILLNO='" + b_no + "'", null);
                    btn_bill_cancel_item_wise_lay.setText(String.valueOf("Uncancel"));
                    btn_bill_cancel_item_wise_lay.setBackground(getResources().getDrawable(R.drawable.orange_button));
                  //  DBExport();
                    Toast.makeText(getContext(), "Bill number:"+b_no+" cancelled", Toast.LENGTH_SHORT).show();

                }
                if(cancel_text.equalsIgnoreCase("Uncancel")) {
                  //  display_02();
                    ContentValues contentValues_cancel = new ContentValues();
                    contentValues_cancel.put("CANCELLED", 0);
                    SplashScreen_Act.db.update("BILLING", contentValues_cancel, "BILLNO='" + b_no + "'", null);
                    btn_bill_cancel_item_wise_lay.setText(String.valueOf("Cancel"));
                  //  DBExport();
                    btn_bill_cancel_item_wise_lay.setBackground(getResources().getDrawable(R.drawable.red_button));
                    Toast.makeText(getContext(), "Bill number:"+b_no+" Uncancelled", Toast.LENGTH_SHORT).show();
                }

                billno_list.clear();
                Bill_View_Fragment.date_list.clear();
                time_list.clear();
                value_list.clear();
                executive_list.clear();
                Bill_View_Fragment.cancel_list.clear();
                highlight_cancel_list.clear();

                String select_bills = "SELECT BILLNO,BDATE,NTOTAL,USERNAME,BTIME,CANCELLED from BILLING  Group By BILLNO";
                Cursor cursor_bills = MainActivity.db.rawQuery(select_bills, null);
                if (cursor_bills.moveToFirst()) {
                    do {
                        int billno = cursor_bills.getInt(0);
                        String date = cursor_bills.getString(1);
                        Float NTotal = cursor_bills.getFloat(2);
                        String username = cursor_bills.getString(3);
                        String time = cursor_bills.getString(4);
                        int cancel = cursor_bills.getInt(5);
                        billno_list.add(String.valueOf(billno));
                        Bill_View_Fragment.date_list.add(date);
                        time_list.add(time);
                        value_list.add(NTotal);
                        executive_list.add(username);
                        Bill_View_Fragment.cancel_list.add(cancel);
                    } while (cursor_bills.moveToNext());
                }
                cursor_bills.close();
                datelist_format();
                for(int i=0; i<=Bill_View_Fragment.cancel_list.size()-1; i++){
                    if(Bill_View_Fragment.cancel_list.get(i)==1) {
                        highlight_cancel_list.add(i);
                      }
                }
                ListViewAdapter listViewAdapter = new ListViewAdapter((View.OnClickListener) getActivity(),billno_list,Bill_View_Fragment.arrayList_datelist,time_list,
                        value_list,executive_list,highlight_cancel_list);
                Bill_View_Fragment.bill_view_listview.setAdapter(listViewAdapter);
                listViewAdapter.selectedPositions.clear();
                listViewAdapter.selectedPositions.addAll(highlight_cancel_list);
                listViewAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private void datelist_format() {
        Bill_View_Fragment.arrayList_datelist = new ArrayList<>();
        for (int i = 0; i <= Bill_View_Fragment.date_list.size() - 1; i++) {
            try {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy/MM/dd");
                Date date = sdfSource.parse(Bill_View_Fragment.date_list.get(i));
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
                Bill_View_Fragment.arrayList_datelist.add(sdfDestination.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void ScanBT() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Message1", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                ListPairedDevices();
                /*Intent connectIntent = new Intent(getActivity(),
                        DeviceListActivity.class);
                startActivityForResult(connectIntent,
                        REQUEST_CONNECT_DEVICE);*/
                String select = "Select BLUETOOTH_NAME,BLUETOOTH_MAC from SETTING";
                Cursor cursor = MainActivity.db.rawQuery(select, null);
                if (cursor.moveToFirst()) {
                    do {
                        str_device_name = cursor.getString(0);
                        str_mac_address = cursor.getString(1);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                if (str_mac_address == null) {
                    Toast.makeText(getContext(), R.string.need_to_pair_bluth_printer_txt, Toast.LENGTH_LONG).show();
                } else {
                    PrinterConnection();
                }
            }
        }
    }

    private void PrinterConnection() {
        String select = "Select BLUETOOTH_NAME,BLUETOOTH_MAC from SETTING";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_device_name = cursor.getString(0);
                str_mac_address = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (str_mac_address.isEmpty()) {
            Toast.makeText(getContext(), R.string.need_to_pair_bluth_printer_txt, Toast.LENGTH_LONG).show();
        } else {
            mBluetoothDevice = mBluetoothAdapter
                    .getRemoteDevice(str_mac_address);
            Log.e("str_mac_address", str_mac_address);
            ProgressDialog mBluetoothConnectProgressDialog = ProgressDialog.show(getActivity(),
                    "Connecting...", mBluetoothDevice.getName() + " : "
                            + mBluetoothDevice.getAddress(), true, false);
            mBluetoothConnectProgressDialog.dismiss();
            Thread mBlutoothConnectThread = new Thread(this);
            mBlutoothConnectThread.start();
            // pairToDevice(mBluetoothDevice); This method is replaced by
            // progress dialog with thread
        }
    }
    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }

    }
    private boolean check_values_before_print() {
        String select = "SELECT * from BILLING where BILLNO='" + tv_bill_number_item_wise_lay.getText().toString() + "'";
        @SuppressLint("Recycle") Cursor cursor = MainActivity.db.rawQuery(select, null);
        return cursor.getCount() > 0;
    }

    private void append_print_details() {
        String i_name;
        Float cgst_calc =0.00f;
        Float sgst_calc =0.00f;

        for (int z = 0; z <= print_i_name_list.size() - 1; z++) {
            Float calc_01 = print_rate_list.get(z) * ((print_cgst_list.get(z) + print_sgst_list.get(z)) / 100);
            Float calc = print_rate_list.get(z) + calc_01;
            @SuppressLint("DefaultLocale") String format_rate = String.format("%.2f", calc);
            @SuppressLint("DefaultLocale") String gtotal = String.format("%.2f", print_G_total_list.get(z));

            if (print_i_name_list.get(z).length() >= 8) {
                i_name = print_i_name_list.get(z).substring(0, 7);
                String values = (String.format("%-8s %8s %5s %8s", i_name, format_rate, print_qnty_list.get(z), gtotal));
                print_details.append(values).append("\n"); } else {
                i_name = String.format("%-9s", print_i_name_list.get(z));
                String values = (String.format("%-8s %8s %5s %8s", print_i_name_list.get(z), format_rate, print_qnty_list.get(z), gtotal));
                print_details.append(values).append("\n");
            }
        }
        print_details.append("--------------------------------" + "\n");
        for(int c=0; c<= print_sgst_tax_value.size()-1; c++){
            cgst_calc = cgst_calc + print_cgst_tax_value.get(c);
            sgst_calc = sgst_calc + print_sgst_tax_value.get(c);
        }
        String c = String.format("%22s","CGST:");
        String s = String.format("%22s","SGST:");
        @SuppressLint("DefaultLocale") String cgst_format_01 = String.format("%.2f",cgst_calc);
        @SuppressLint("DefaultLocale") String sgst_format_01 = String.format("%.2f",sgst_calc);

        String print_cgst_format = String.format("%9s",cgst_format_01);
        String print_sgst_format = String.format("%9s",sgst_format_01);
        print_details.append(c).append(print_cgst_format).append("\n").append(s).
                append(print_sgst_format).append("\n");

        Log.e("print_details", String.valueOf(print_details));
    }

    @Override
    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createInsecureRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
        }

    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(getActivity(), R.string.connecting_to_printer_txt, Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), R.string.printer_press_again_txt, Toast.LENGTH_LONG).show();
                }
            }, 5000);
        }
    };

    private int intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }
        return b[3];
    }
}
