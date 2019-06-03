package com.example.admin.zeller.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.zeller.Adapters.BillingAdapter;
import com.example.admin.zeller.MainActivity;
import com.example.admin.zeller.Utils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.zellerr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Billing_Fragment extends Fragment implements AdapterView.OnItemSelectedListener, Runnable {
    GridView c_name_gridview;
    ListView i_name_listview, i_name_fav_listview;
    @SuppressLint("StaticFieldLeak")
    public static ListView lv_billing_product_count;


    ArrayList<String> c_name_list = new ArrayList<>();
    ArrayList<String> i_name_list = new ArrayList<>();
    ArrayList<String> i_name_fav_list = new ArrayList<>();

    ArrayAdapter<String> c_name_gridview_adapter;
    ArrayAdapter<String> i_name_listview_adapter;
    ArrayAdapter<String> i_name_listview_fav_adapter;

    //This is for Bill Mode Spinner
    public static MaterialBetterSpinner billmode_spinner;
    ArrayAdapter<String> billmode_spin_adapter;
    ArrayList<String> billmode_spin_list = new ArrayList<>();

    //This is for Sales Person Spinner
    MaterialBetterSpinner sales_person_spinner;
    ArrayAdapter<String> sales_person_spinner_adapter;
    ArrayList<String> salesperson_spin_list = new ArrayList<>();


    TextView tv_items, tv_favourites, tv_no_fav, tv_fav;
    LinearLayout linear_tv_items_lay, linear_tv_favourites_lay;

    String selected_c_name;

    ArrayList<String> item_names = new ArrayList<>();
    ArrayList<Float> item_price = new ArrayList<>();
    ArrayList<Float> item_qnty = new ArrayList<>();


    //Bill View Layout attributes
    RelativeLayout relative_billview_layout, relative_joins_layout;
    TextView tv_bill_view_txt, tv_add_new_bill, tv_add_items_black,
            tv_bill_no, tv_date, tv_time, tv_value, tv_executive;
    LinearLayout billing_view_buttons, linear_btn_bill_no,
            linear_btn_date, linear_btn_time, linear_btn_value,
            linear_btn_executive, linear_listview_layout_billing;
    ListView listview_billing_page;
    int b_number, bill_num;
    TextView tv_date_value, tv_time_value, tv_prefix;
    @SuppressLint("StaticFieldLeak")
    public static TextView tv_bill_number, tv_before_tax_value, tv_cgst_tax_value,
            tv_sgst_tax_value, tv_total_value;
    String date, date_db, time, bm_id, i_id, bp_id, U_ID, USERNAME, prefix;
    Float cgst, sgst, i_rate;
    String str_bm_id;
    Button btn_cancel, btn_save;
    @SuppressLint("StaticFieldLeak")
    public static Button btn_print;

    TextView tv_custom_alert_product_name;
    @SuppressLint("StaticFieldLeak")
    public static TextView tv_no_of_items;
    EditText et_custom_alert_price;
    Button btn_custom_alert_submit;

    ArrayList<Float> i_rate_list = new ArrayList<>();
    ArrayList<String> name_list = new ArrayList<>();
    TextView tv_categories;

    public static int pos;

    protected static final String TAG = "TAG";
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    OutputStream os;

    String BILL;
    StringBuilder print_details = new StringBuilder();
    String str_company_name, str_add_01, str_add_02, str_add_03, str_place, str_gst_no, str_phone_no;

    Float ntotal;
    String str_device_name, str_mac_address;

    ArrayList<Integer> print_sno_list;
    ArrayList<String> print_i_name_list;
    ArrayList<Float> print_qnty_list;
    ArrayList<Float> print_G_total_list;
    ArrayList<Float> print_rate_list;
    ArrayList<Float> print_cgst_list;
    ArrayList<Float> print_sgst_list;
    ArrayList<Float> print_cgst_tax_value;
    ArrayList<Float> print_sgst_tax_value;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.billing, null, false);
        c_name_gridview = view.findViewById(R.id.c_name_gridview);
        tv_categories = view.findViewById(R.id.tv_categories);
//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        int currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getActivity()).getWindow().getDecorView().setSystemUiVisibility(flags);
            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getActivity().getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }


        i_name_fav_listview = view.findViewById(R.id.i_name_fav_listview);
        billmode_spinner = view.findViewById(R.id.spinner_bill_mode);
        sales_person_spinner = view.findViewById(R.id.spinner_sales_person);
        tv_items = view.findViewById(R.id.tv_items);
        tv_favourites = view.findViewById(R.id.tv_favourites);
        tv_no_fav = view.findViewById(R.id.no_fav_textview);
        tv_fav = view.findViewById(R.id.tv_fav);
        tv_favourites = view.findViewById(R.id.tv_favourites);
        tv_items = view.findViewById(R.id.tv_items);
        linear_tv_items_lay = view.findViewById(R.id.linear_tv_items_lay);
        linear_tv_favourites_lay = view.findViewById(R.id.linear_tv_favourites_lay);
        lv_billing_product_count = view.findViewById(R.id.lv_billing_product_count);
        i_name_listview = view.findViewById(R.id.i_name_listview);
        tv_prefix = view.findViewById(R.id.tv_prefix);
        tv_cgst_tax_value = view.findViewById(R.id.tv_cgst_tax_value);
        tv_sgst_tax_value = view.findViewById(R.id.tv_sgst_tax_value);
        tv_total_value = view.findViewById(R.id.tv_total_value);
        tv_no_of_items = view.findViewById(R.id.tv_no_of_items);

        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_print = view.findViewById(R.id.btn_print);
        btn_save = view.findViewById(R.id.btn_save_bill);

        //Bill View Layout attributes
        //Relative Layout
        relative_billview_layout = view.findViewById(R.id.relative_billview_layout);
        relative_joins_layout = view.findViewById(R.id.relative_joins_layout);

        //Bill View Layout Textview attributes
        tv_bill_view_txt = view.findViewById(R.id.tv_bill_view_txt);
        tv_add_new_bill = view.findViewById(R.id.tv_add_new_bill);
        tv_add_items_black = view.findViewById(R.id.tv_add_items_black);
        tv_bill_no = view.findViewById(R.id.tv_bill_no);
        tv_date = view.findViewById(R.id.tv_date);
        tv_time = view.findViewById(R.id.tv_time);
        tv_value = view.findViewById(R.id.tv_value);
        tv_executive = view.findViewById(R.id.tv_executive);

        tv_before_tax_value = view.findViewById(R.id.tv_before_tax_value);

        tv_bill_number = view.findViewById(R.id.tv_bill_number);
        get_Billing_details();

        date = new SimpleDateFormat("dd/MM/yy").format(new Date());
        date_db = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        time = mdformat.format(calendar.getTime());
        tv_date_value = view.findViewById(R.id.tv_date_value);
        tv_time_value = view.findViewById(R.id.tv_time_value);
        tv_date_value.setText(date);
        tv_time_value.setText(time);

        //Bill View Layout LinearLayout attributes
        billing_view_buttons = view.findViewById(R.id.billing_view_buttons);
        linear_btn_bill_no = view.findViewById(R.id.linear_btn_bill_no);
        linear_btn_date = view.findViewById(R.id.linear_btn_date);
        linear_btn_time = view.findViewById(R.id.linear_btn_time);
        linear_btn_value = view.findViewById(R.id.linear_btn_value);
        linear_btn_executive = view.findViewById(R.id.linear_btn_executive);
        linear_listview_layout_billing = view.findViewById(R.id.linear_listview_layout_billing);

        //Bill View Layout Listviewattributes
        listview_billing_page = view.findViewById(R.id.listview_billing_page);

        sales_person_spinner.setBackgroundResource(R.drawable.bg_spinner_in_billing);
        sales_person_spinner.setTextColor(Color.YELLOW);
        billmode_spinner.setBackgroundResource(R.drawable.bg_spinner_in_billing);
        billmode_spinner.setTextColor(Color.YELLOW);
        tv_before_tax_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                set_total_value();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> arrayList_i_names = new ArrayList<>();
                String number = tv_bill_number.getText().toString();
                String select_name = "SELECT I_NAME from BILLING where BILLNO ='" + number + "'";
                Cursor cursor_name = MainActivity.db.rawQuery(select_name, null);
                int c = cursor_name.getCount();
                if (c > 0) {
                    if (cursor_name.moveToFirst()) {
                        do {
                            String name = cursor_name.getString(0);
                            arrayList_i_names.add(name);
                        } while (cursor_name.moveToNext());
                    }
                    cursor_name.close();
                    for (int j = 0; j <= cursor_name.getCount(); j++) {
                        if (j <= arrayList_i_names.size() - 1) {
                            Float total = Float.valueOf(tv_total_value.getText().toString());
                            ContentValues cv_cancel_update = new ContentValues();
                            cv_cancel_update.put("CANCELLED", 1);
                            cv_cancel_update.put("NTOTAL", total);
                            MainActivity.db.update("BILLING", cv_cancel_update,
                                    "I_NAME='" + arrayList_i_names.get(j) + "' and BILLNO = '" + tv_bill_number.getText().toString() + "'", null);
                            DBExport();
                        }
                    }
                    clear_Adapter_method();
                }
            }
        });
        btn_print.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = 28)
            @Override
            public void onClick(View view) {
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
                    Float total = Float.valueOf(tv_total_value.getText().toString());
                    ContentValues cv_bill_update = new ContentValues();
                    cv_bill_update.put("NTOTAL", total);
                    MainActivity.db.update("BILLING", cv_bill_update, "BILLNO='" + tv_bill_number.getText().toString() + "'", null);
                    DBExport();
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
                                        "BILLNO ='" + Billing_Fragment.tv_bill_number.getText().toString() + "'";
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
                                String select_billing = "SELECT I_NAME,QTY,GTOTAL,NTOTAL,CGST,SGST from BILLING where BILLNO='" + tv_bill_number.getText().toString() + "'";
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
                                    String select_rate = "SELECT RATE from BILLING where BILLNO='" + tv_bill_number.getText().toString() + "'" +
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
                    /*Log.e("thread_state", String.valueOf(t.getState()));
                    if(t.getState()==RUNNABLE){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                clear_Adapter_method();
                            }
                        }, 15000);
                    }*/
                    /*
                    Handler handler = new Handler();
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
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_total_value();
                Float total = Float.valueOf(tv_total_value.getText().toString());
                ContentValues cv_bill_update = new ContentValues();
                cv_bill_update.put("NTOTAL", total);
                MainActivity.db.update("BILLING", cv_bill_update, "BILLNO='" + tv_bill_number.getText().toString() + "'", null);
                DBExport();
                String select = "SELECT * from BILLING where BILLNO = '" + tv_bill_number.getText().toString() + "'";
                Cursor cursor = MainActivity.db.rawQuery(select, null);
                if (cursor.getCount() > 0) {
                    clear_Adapter_method();
                } else {
                    Toast.makeText(getContext(), R.string.no_bill_added_txt, Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });

        i_name_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String name = adapterView.getItemAtPosition(i).toString();
                if (rowIdExists_billing(name)) {
                    Float rate = 0.00f, cgst = 0.00f, sgst = 0.00f, qty = 0.00f;
                    Float calc_amount, calc_gtotal;
                    String select_bill_details = "SELECT RATE,CGST,SGST,QTY from BILLING where " +
                            "I_NAME='" + name + "' and BILLNO = '" + tv_bill_number.getText().toString().trim() + "'";
                    Cursor cursor_bill_details = MainActivity.db.rawQuery(select_bill_details, null);
                    if (cursor_bill_details.moveToFirst()) {
                        do {
                            rate = cursor_bill_details.getFloat(0);
                            cgst = cursor_bill_details.getFloat(1);
                            sgst = cursor_bill_details.getFloat(2);
                            qty = cursor_bill_details.getFloat(3);
                        } while (cursor_bill_details.moveToNext());
                    }
                    cursor_bill_details.close();
                    calc_amount = rate * (qty + 1);
                    Float calc = calc_amount * ((cgst + sgst) / 100);
                    calc_gtotal = calc_amount + calc;
                    ContentValues cv_bill_update = new ContentValues();
                    cv_bill_update.put("QTY", qty + 1);
                    cv_bill_update.put("AMOUNT", calc_amount);
                    cv_bill_update.put("GTOTAL", calc_gtotal);
                    MainActivity.db.update("BILLING", cv_bill_update,
                            "I_NAME='" + name + "' and BILLNO = '" + tv_bill_number.getText().toString().trim() + "'", null);
                    DBExport();
                    //   scrollMyListViewToBottom_01();
                    set_tax_method(name);
                    set_total_value();
                } else {
                    item_names.clear();
                    item_qnty.clear();
                    item_price.clear();
                    String select = "SELECT I_RATE,BM_ID,I_ID from RATE where I_NAME='" + name + "' and BM_NAME = '" + billmode_spinner.getText().toString().trim() + "'";
                    Cursor cursor = MainActivity.db.rawQuery(select, null);
                    if (cursor.getCount() > 0) {
                        if (cursor.moveToFirst()) {
                            do {
                                i_rate = cursor.getFloat(0);
                                bm_id = cursor.getString(1);
                                i_id = cursor.getString(2);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        if (i_rate == 0.0) {
                            final Dialog dialog_01 = new Dialog(Objects.requireNonNull(getContext()));
                            dialog_01.setContentView(R.layout.custom_alert_in_billing);
                            dialog_01.setCancelable(false);
                            dialog_01.show();
                            Objects.requireNonNull(dialog_01.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            tv_custom_alert_product_name = dialog_01.findViewById(R.id.tv_custom_alert_product_name);
                            et_custom_alert_price = dialog_01.findViewById(R.id.et_custom_alert_price);
                            btn_custom_alert_submit = dialog_01.findViewById(R.id.btn_custom_alert_submit);
                            tv_custom_alert_product_name.setText(name);
                            btn_custom_alert_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String s1 = et_custom_alert_price.getText().toString();
                                    if (s1.isEmpty()) {
                                        i_rate = 0.00f;
                                        Utils.ShortToast(getString(R.string.please_enter_price_txt), getContext());
                                    } else {
                                        i_rate = Float.valueOf(et_custom_alert_price.getText().toString());
                                        Log.e("I_Rate_value", String.valueOf(i_rate));
                                        method_01(name);
                                        dialog_01.dismiss();
                                    }
                                }
                            });
                            dialog_01.show();
                        } else {
                            method_01(name);
                        }
                    } else if (cursor.getCount() == 0) {
                        final Dialog dialog_01 = new Dialog(Objects.requireNonNull(getContext()));
                        dialog_01.setContentView(R.layout.custom_alert_in_billing);
                        dialog_01.setCancelable(false);
                        dialog_01.show();
                        Objects.requireNonNull(dialog_01.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        tv_custom_alert_product_name = dialog_01.findViewById(R.id.tv_custom_alert_product_name);
                        et_custom_alert_price = dialog_01.findViewById(R.id.et_custom_alert_price);
                        btn_custom_alert_submit = dialog_01.findViewById(R.id.btn_custom_alert_submit);
                        tv_custom_alert_product_name.setText(name);
                        btn_custom_alert_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String s1 = et_custom_alert_price.getText().toString();
                                if (s1.isEmpty()) {
                                    i_rate = 0.00f;
                                    Utils.ShortToast(getString(R.string.please_enter_price_txt), getContext());
                                } else {
                                    i_rate = Float.valueOf(et_custom_alert_price.getText().toString());
                                    Log.e("I_Rate_value", String.valueOf(i_rate));
                                    method_01(name);
                                    dialog_01.dismiss();
                                }
                            }
                        });
                        dialog_01.show();
                        Log.e("i_rateeee", String.valueOf(i_rate));
                        String select_id = "SELECT I_ID from ITEM where I_NAME='" + name + "'";
                        Cursor cursor_id = MainActivity.db.rawQuery(select_id, null);
                        if (cursor_id.moveToFirst()) {
                            do {
                                i_id = cursor_id.getString(0);
                            } while (cursor_id.moveToNext());
                        }
                        cursor_id.close();
                    }
                }
            }
        });
        i_name_fav_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String name = adapterView.getItemAtPosition(i).toString();
                if (rowIdExists_billing(name)) {
                    Float rate = 0.00f, cgst = 0.00f, sgst = 0.00f, qty = 0.00f;
                    Float calc_amount, calc_gtotal;
                    String select_bill_details = "SELECT RATE,CGST,SGST,QTY from BILLING where " +
                            "I_NAME='" + name + "' and BILLNO = '" + tv_bill_number.getText().toString().trim() + "'";
                    Cursor cursor_bill_details = MainActivity.db.rawQuery(select_bill_details, null);
                    if (cursor_bill_details.moveToFirst()) {
                        do {
                            rate = cursor_bill_details.getFloat(0);
                            cgst = cursor_bill_details.getFloat(1);
                            sgst = cursor_bill_details.getFloat(2);
                            qty = cursor_bill_details.getFloat(3);
                        } while (cursor_bill_details.moveToNext());
                    }
                    cursor_bill_details.close();
                    calc_amount = rate * (qty + 1);
                    Float calc = calc_amount * ((cgst + sgst) / 100);
                    calc_gtotal = calc_amount + calc;
                    ContentValues cv_bill_update = new ContentValues();
                    cv_bill_update.put("QTY", qty + 1);
                    cv_bill_update.put("AMOUNT", calc_amount);
                    cv_bill_update.put("GTOTAL", calc_gtotal);
                    MainActivity.db.update("BILLING", cv_bill_update,
                            "I_NAME='" + name + "' and BILLNO = '" + tv_bill_number.getText().toString().trim() + "'", null);
                    DBExport();
                    set_tax_method(name);
                    set_total_value();
                } else {
                    item_names.clear();
                    item_qnty.clear();
                    item_price.clear();

                    String select = "SELECT I_RATE,BM_ID,I_ID from RATE where I_NAME='" + name + "' and BM_NAME = '" + billmode_spinner.getText().toString().trim() + "'";
                    Cursor cursor = MainActivity.db.rawQuery(select, null);
                    if (cursor.getCount() > 0) {
                        if (cursor.moveToFirst()) {
                            do {
                                i_rate = cursor.getFloat(0);
                                bm_id = cursor.getString(1);
                                i_id = cursor.getString(2);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        if (i_rate == 0.0) {
                            final Dialog dialog_01 = new Dialog(Objects.requireNonNull(getContext()));
                            dialog_01.setContentView(R.layout.custom_alert_in_billing);
                            dialog_01.setCancelable(false);
                            dialog_01.show();
                            Objects.requireNonNull(dialog_01.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            tv_custom_alert_product_name = dialog_01.findViewById(R.id.tv_custom_alert_product_name);
                            et_custom_alert_price = dialog_01.findViewById(R.id.et_custom_alert_price);
                            btn_custom_alert_submit = dialog_01.findViewById(R.id.btn_custom_alert_submit);
                            tv_custom_alert_product_name.setText(name);
                            btn_custom_alert_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String s1 = et_custom_alert_price.getText().toString();
                                    if (s1.isEmpty()) {
                                        i_rate = 0.00f;
                                        Utils.ShortToast(getString(R.string.please_enter_price_txt), getContext());
                                    } else {
                                        i_rate = Float.valueOf(et_custom_alert_price.getText().toString());
                                        Log.e("I_Rate_value", String.valueOf(i_rate));
                                        method_01(name);
                                        dialog_01.dismiss();
                                    }
                                }
                            });
                            dialog_01.show();
                        } else {
                            method_01(name);
                        }
                    } else if (cursor.getCount() == 0) {
                        final Dialog dialog_01 = new Dialog(Objects.requireNonNull(getContext()));
                        dialog_01.setContentView(R.layout.custom_alert_in_billing);
                        dialog_01.setCancelable(false);
                        dialog_01.show();
                        Objects.requireNonNull(dialog_01.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        tv_custom_alert_product_name = dialog_01.findViewById(R.id.tv_custom_alert_product_name);
                        et_custom_alert_price = dialog_01.findViewById(R.id.et_custom_alert_price);
                        btn_custom_alert_submit = dialog_01.findViewById(R.id.btn_custom_alert_submit);
                        tv_custom_alert_product_name.setText(name);
                        btn_custom_alert_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String s1 = et_custom_alert_price.getText().toString();
                                if (s1.isEmpty()) {
                                    i_rate = 0.00f;
                                    Utils.ShortToast(getString(R.string.please_enter_price_txt), getContext());
                                } else {
                                    i_rate = Float.valueOf(et_custom_alert_price.getText().toString());
                                    Log.e("I_Rate_value", String.valueOf(i_rate));
                                    method_01(name);
                                    dialog_01.dismiss();
                                }
                            }
                        });
                        dialog_01.show();
                        String select_id = "SELECT I_ID from ITEM where I_NAME='" + name + "'";
                        Cursor cursor_id = MainActivity.db.rawQuery(select_id, null);
                        if (cursor_id.moveToFirst()) {
                            do {
                                i_id = cursor_id.getString(0);
                            } while (cursor_id.moveToNext());
                        }
                        cursor_id.close();
                    }
                }
            }
        });
        GetBillMode_details();
        GetSalesPerson_details();
        get_SalesPerson_details();

        String select = "SELECT C_NAME from CATEGORY";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                c_name_list.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        c_name_gridview_adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.gridview_text, R.id.gridview_textview, c_name_list);
        c_name_gridview.setAdapter(c_name_gridview_adapter);
        c_name_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_c_name = adapterView.getItemAtPosition(i).toString();
                i_name_list.clear();
                i_name_fav_list.clear();

                linear_tv_items_lay.setBackgroundResource(R.drawable.onclick_button_curve_bg);
                tv_items.setTextColor(Color.parseColor("#E0F010"));
                linear_tv_favourites_lay.setBackgroundResource(R.drawable.billing_button_curve_bg);
                tv_favourites.setTextColor(Color.parseColor("#000000"));
                i_name_listview.setVisibility(View.VISIBLE);
                i_name_fav_listview.setVisibility(View.GONE);
                tv_no_fav.setVisibility(View.GONE);

                String select = "SELECT I_NAME from ITEM where C_NAME='" + selected_c_name + "' ";// and FAVOURITES =0";
                Cursor cursor = MainActivity.db.rawQuery(select, null);
                int count = cursor.getCount();
                if (count > 0) {
                    tv_fav.setVisibility(View.GONE);
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(0);
                            i_name_list.add(name);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    i_name_listview_adapter = new ArrayAdapter<>(getContext(), R.layout.billing_normal_listview_adapter_layout,
                            R.id.tv_item_name_listview, i_name_list);
                    i_name_listview.setAdapter(i_name_listview_adapter);
                } else {
                    tv_fav.setVisibility(View.VISIBLE);
                    tv_fav.setGravity(Gravity.CENTER);
                    tv_fav.setText("No Items Exists");
                }
            }
        });

        linear_tv_items_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_c_name == null) {
                    Utils.LongToast("Please Select Any One Category", getContext());
                }
                i_name_list.clear();
                i_name_fav_list.clear();
                linear_tv_items_lay.setBackgroundResource(R.drawable.onclick_button_curve_bg);
                tv_items.setTextColor(Color.parseColor("#E0F010"));
                i_name_listview.setVisibility(View.VISIBLE);
                i_name_fav_listview.setVisibility(View.GONE);
                tv_no_fav.setVisibility(View.GONE);
                linear_tv_favourites_lay.setBackgroundColor(Color.parseColor("#E0F010"));
                tv_favourites.setTextColor(Color.parseColor("#000000"));
                linear_tv_favourites_lay.setBackgroundResource(R.drawable.button_curve_bg);

                i_name_fav_listview.setVisibility(View.GONE);
                i_name_listview.setVisibility(View.VISIBLE);
                String select = "SELECT I_NAME from ITEM where C_NAME='" + selected_c_name + "'";//" and FAVOURITES =0";
                Cursor cursor = MainActivity.db.rawQuery(select, null);
                int count = cursor.getCount();
                if (count == 0) {
                    tv_fav.setVisibility(View.VISIBLE);
                    tv_fav.setText(String.valueOf("No Items Exists"));
                } else {
                    tv_fav.setVisibility(View.GONE);
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(0);
                            i_name_list.add(name);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    i_name_listview_adapter = new ArrayAdapter<>(getContext(), R.layout.billing_normal_listview_adapter_layout, R.id.tv_item_name_listview, i_name_list);
                    i_name_listview.setAdapter(i_name_listview_adapter);
                }
            }
        });
        linear_tv_favourites_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_c_name == null) {
                    Utils.LongToast("Please select any one category", getContext());
                } else {
                    i_name_list.clear();
                    i_name_fav_list.clear();

                    linear_tv_favourites_lay.setBackgroundResource(R.drawable.onclick_button_curve_bg);
                    tv_favourites.setTextColor(Color.parseColor("#E0F010"));
                    i_name_fav_listview.setVisibility(View.VISIBLE);
                    i_name_listview.setVisibility(View.GONE);

                    linear_tv_items_lay.setBackgroundColor(Color.parseColor("#E0F010"));
                    tv_items.setTextColor(Color.parseColor("#000000"));
                    linear_tv_items_lay.setBackgroundResource(R.drawable.button_curve_bg);

                    i_name_fav_listview.setVisibility(View.VISIBLE);
                    i_name_listview.setVisibility(View.GONE);
                    String select_fav = "SELECT I_NAME from ITEM where C_NAME='" + selected_c_name + "' and FAVOURITES =1";
                    Cursor cursor1 = MainActivity.db.rawQuery(select_fav, null);
                    if (cursor1.moveToFirst()) {
                        do {
                            String name = cursor1.getString(0);
                            i_name_fav_list.add(name);
                        } while (cursor1.moveToNext());
                    }
                    cursor1.close();
                    i_name_listview_fav_adapter = new ArrayAdapter<>(getContext(), R.layout.billing_normal_listview_adapter_layout, R.id.tv_item_name_listview, i_name_fav_list);
                    i_name_fav_listview.setAdapter(i_name_listview_fav_adapter);
                    if (i_name_fav_list.isEmpty()) {
                        i_name_fav_listview.setVisibility(View.GONE);
                        i_name_listview.setVisibility(View.GONE);
                        tv_fav.setVisibility(View.GONE);
                        tv_no_fav.setVisibility(View.VISIBLE);
                        tv_no_fav.setGravity(Gravity.CENTER);
                        tv_no_fav.setText(String.valueOf("No Favourites Exists"));
                    }
                }
            }
        });
        sales_person_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                String select_user = "Select BP_ID,U_ID,USERNAME from USER where USERNAME ='" + name + "'";
                Cursor cursor_user = MainActivity.db.rawQuery(select_user, null);
                if (cursor_user.moveToFirst()) {
                    do {
                        bp_id = cursor_user.getString(0);
                        U_ID = cursor_user.getString(1);
                        USERNAME = cursor_user.getString(2);
                    } while (cursor_user.moveToNext());
                }
                cursor_user.close();

                String select_prefix = "Select PREFIX from BILL_PREFIX where  BP_ID='" + bp_id + "'";
                Cursor cursor_prefix = MainActivity.db.rawQuery(select_prefix, null);
                if (cursor_prefix.moveToFirst()) {
                    do {
                        prefix = cursor_prefix.getString(0);
                        tv_prefix.setText(prefix);
                    } while (cursor_prefix.moveToNext());
                }
                cursor_prefix.close();

                String select_billing = "SELECT I_NAME from BILLING where BILLNO='" + tv_bill_number.getText().toString() + "'";
                Cursor cursor_bill = MainActivity.db.rawQuery(select_billing, null);
                if (cursor_bill.getCount() > 0) {
                    ArrayList<String> arrayList_i_name = new ArrayList<>();
                    if (cursor_bill.moveToFirst()) {
                        do {
                            String names = cursor_bill.getString(0);
                            arrayList_i_name.add(names);
                        } while (cursor_bill.moveToNext());
                    }
                    cursor_bill.close();
                    for (int j = 0; j <= cursor_bill.getCount(); j++) {
                        if (j <= arrayList_i_name.size() - 1) {
                            ContentValues cv_prefix_update = new ContentValues();
                            cv_prefix_update.put("PREFIX", prefix);
                            cv_prefix_update.put("BP_ID", bp_id);
                            cv_prefix_update.put("U_ID", U_ID);
                            cv_prefix_update.put("USERNAME", USERNAME);
                            MainActivity.db.update("BILLING", cv_prefix_update,
                                    "I_NAME='" + arrayList_i_name.get(j) + "' and BILLNO = '" + tv_bill_number.getText().toString() + "'", null);
                            DBExport();
                        }
                    }
                }
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void bluetooth_dialog() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.setContentView(R.layout.bluetooth_dialog);
        dialog.setCancelable(false);
        final Button btn_ok = dialog.findViewById(R.id.btn_ok_bluetooth_dialog);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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

       /* Log.e("cgst_calc", String.valueOf(print_cgst_tax_value));
        Log.e("sgst_calc", String.valueOf(print_sgst_tax_value));*/
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

    private boolean check_values_before_print() {
        String select = "SELECT * from BILLING where BILLNO='" + tv_bill_number.getText().toString() + "'";
        @SuppressLint("Recycle") Cursor cursor = MainActivity.db.rawQuery(select, null);
        return cursor.getCount() > 0;
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
                /*
                Intent connectIntent = new Intent(getActivity(),
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

    /* public void onActivityResult(int mRequestCode, int mResultCode,
                                  Intent mDataIntent) {
         super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

         switch (mRequestCode) {
             case REQUEST_CONNECT_DEVICE:
                 if (mResultCode == Activity.RESULT_OK) {
                     Bundle mExtra = mDataIntent.getExtras();
                     String mDeviceAddress = mExtra.getString("DeviceAddress");
                     Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                     mBluetoothDevice = mBluetoothAdapter
                             .getRemoteDevice(mDeviceAddress);
                     mBluetoothConnectProgressDialog = ProgressDialog.show(getActivity(),
                             "Connecting...", mBluetoothDevice.getName() + " : "
                                     + mBluetoothDevice.getAddress(), true, false);
                     Thread mBlutoothConnectThread = new Thread(this);
                     mBlutoothConnectThread.start();
                     // pairToDevice(mBluetoothDevice); This method is replaced by
                     // progress dialog with thread
                 }
                 break;
         }
     }
 */
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void clear_Adapter_method() {
        item_names.clear();
        item_price.clear();
        item_qnty.clear();
        BillingAdapter myAdapter = new BillingAdapter(Objects.requireNonNull
                (getContext()), item_names, item_price, item_qnty);
        lv_billing_product_count.setAdapter(myAdapter);

        tv_no_of_items.setVisibility(View.GONE);
        tv_before_tax_value.setText(String.valueOf(0.00));
        tv_cgst_tax_value.setText(String.valueOf(0.00));
        tv_sgst_tax_value.setText(String.valueOf(0.00));
        tv_total_value.setText(String.valueOf(0.00));

        int billno = Integer.parseInt(tv_bill_number.getText().toString());
        int b_num = billno + 1;
        tv_bill_number.setText(String.valueOf(b_num));
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void method_01(String name) {
        item_names.clear();
        item_price.clear();
        item_qnty.clear();
        get_SalesPerson_details();
        String select_01 = "SELECT CGST,SGST from ITEM where I_NAME='" + name + "'";
        Cursor cursor_01 = MainActivity.db.rawQuery(select_01, null);
        if (cursor_01.moveToFirst()) {
            do {
                cgst = cursor_01.getFloat(0);
                sgst = cursor_01.getFloat(1);
            } while (cursor_01.moveToNext());
        }
        cursor_01.close();
        Float calc = cgst + sgst;
        Float c = i_rate * calc / 100;
        Float GTotal = i_rate + c;
        ContentValues cv_billing_insert = new ContentValues();
        cv_billing_insert.put("BILLNO", tv_bill_number.getText().toString());
        cv_billing_insert.put("BDATE", date_db);
        cv_billing_insert.put("BM_ID", bm_id);
        cv_billing_insert.put("BP_ID", bp_id);
        cv_billing_insert.put("I_ID", i_id);
        cv_billing_insert.put("I_NAME", name);
        cv_billing_insert.put("RATE", i_rate);
        cv_billing_insert.put("CGST", cgst);
        cv_billing_insert.put("SGST", sgst);
        cv_billing_insert.put("QTY", 1);
        cv_billing_insert.put("AMOUNT", i_rate);
        cv_billing_insert.put("GTOTAL", GTotal);
        cv_billing_insert.put("U_ID", U_ID);
        cv_billing_insert.put("USERNAME", USERNAME);
        cv_billing_insert.put("PREFIX", prefix);
        cv_billing_insert.put("CANCELLED", 0);
        cv_billing_insert.put("LESS", 0);
        cv_billing_insert.put("BTIME", tv_time_value.getText().toString());
        MainActivity.db.insert("BILLING", null, cv_billing_insert);
        DBExport();
        scrollMyListViewToBottom();
        set_tax_method(name);
        set_total_value();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void set_tax_method(String name) {
        item_names.clear();
        item_price.clear();
        item_qnty.clear();
        String selection = "Select BILLNO from BILLING";
        Cursor cursor1 = MainActivity.db.rawQuery(selection, null);
        if (cursor1.moveToFirst()) {
            do {
                bill_num = cursor1.getInt(0);
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        String select_billing = "SELECT I_NAME,AMOUNT,QTY from BILLING where BILLNO = '" + bill_num + "'";
        Cursor cursor_02 = MainActivity.db.rawQuery(select_billing, null);
        if (cursor_02.moveToFirst()) {
            do {
                String item_name = cursor_02.getString(0);
                Float item_rate = cursor_02.getFloat(1);
                Float item_qty = cursor_02.getFloat(2);
                item_names.add(item_name);
                item_price.add(item_rate);
                item_qnty.add(item_qty);
            } while (cursor_02.moveToNext());
        }
        cursor_02.close();
        for (int n = 0; n <= item_names.size() - 1; n++) {
            if (name.equalsIgnoreCase(item_names.get(n))) {
                pos = n;
            }
        }

        BillingAdapter myAdapter = new BillingAdapter(Objects.requireNonNull(getContext()), item_names, item_price, item_qnty);
        lv_billing_product_count.setAdapter(myAdapter);

        int num_of_items = item_names.size();
        if (num_of_items == 0) {
            tv_no_of_items.setVisibility(View.INVISIBLE);
        } else {
            tv_no_of_items.setVisibility(View.VISIBLE);
            tv_no_of_items.setText(String.valueOf(num_of_items + " items"));
        }
        Float cal = 0.0f;
        for (int cc = 0; cc < item_price.size(); cc++) {
            cal = cal + item_price.get(cc);
        }
        @SuppressLint("DefaultLocale") String result = String.format("%.2f", cal);
        tv_before_tax_value.setText(result);

        Float cgst_tax;
        Float sgst_tax;
        String select_tax = "SELECT CGST,SGST,RATE from BILLING " +
                "where I_NAME = '" + name + "' and BILLNO='" + tv_bill_number.getText().toString() + "'";
        Cursor cursor_tax = MainActivity.db.rawQuery(select_tax, null);
        if (cursor_tax.moveToFirst()) {
            do {
                Float cgst = cursor_tax.getFloat(0);
                Float sgst = cursor_tax.getFloat(1);
                Float rate = cursor_tax.getFloat(2);
                Float calc_01 = rate * cgst / 100;
                Float calc_02 = rate * sgst / 100;
                if (tv_cgst_tax_value.getText().toString().isEmpty()) {
                    cgst_tax = 0.00f;
                } else {
                    cgst_tax = Float.valueOf(tv_cgst_tax_value.getText().toString());
                }
                if (tv_sgst_tax_value.getText().toString().isEmpty()) {
                    sgst_tax = 0.00f;
                } else {
                    sgst_tax = Float.valueOf(tv_sgst_tax_value.getText().toString());
                }
                Float c_tax = calc_01 + cgst_tax;
                Float s_tax = calc_02 + sgst_tax;
                @SuppressLint("DefaultLocale") String result_1 = String.format("%.2f", c_tax);
                @SuppressLint("DefaultLocale") String result_2 = String.format("%.2f", s_tax);
                tv_cgst_tax_value.setText(result_1);
                tv_sgst_tax_value.setText(result_2);

            } while (cursor_tax.moveToNext());
        }
        cursor_tax.close();
    }

    private void scrollMyListViewToBottom() {
        lv_billing_product_count.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv_billing_product_count.setSelection(item_names.size() - 1);
            }
        });
    }

    private void set_tax_inc() {
        ArrayList<String> arrayList_names = new ArrayList<>();
        ArrayList<Float> arrayList_cgst = new ArrayList<>();
        ArrayList<Float> arrayList_sgst = new ArrayList<>();
        String select = "SELECT I_NAME from BILLING where BILLNO = '" + tv_bill_number.getText().toString() + "'";
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
                String select_tax = "SELECT CGST,SGST,RATE,QTY from BILLING where I_NAME ='" + arrayList_names.get(i) + "' and BILLNO='" + tv_bill_number.getText().toString() + "'";
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
                tv_cgst_tax_value.setText(result_01);
                for (int sgst = 0; sgst < arrayList_sgst.size(); sgst++) {
                    cal_02 = cal_02 + arrayList_sgst.get(sgst);
                }
                @SuppressLint("DefaultLocale") String result_02 = String.format("%.2f", cal_02);
                tv_sgst_tax_value.setText(result_02);
            }
        }

    }


    private void set_total_value() {
        Float before_tax = Float.valueOf(tv_before_tax_value.getText().toString());
        Float cgst = Float.valueOf(tv_cgst_tax_value.getText().toString());
        Float sgst = Float.valueOf(tv_sgst_tax_value.getText().toString());
        Float calc = before_tax + cgst + sgst;
        @SuppressLint("DefaultLocale") String result = String.format("%.2f", calc);
        tv_total_value.setText(result);
    }

    private void get_SalesPerson_details() {
        String select_user = "Select BP_ID,U_ID,USERNAME from USER where USERNAME ='" + sales_person_spinner.getText().toString() + "'";
        Cursor cursor_user = MainActivity.db.rawQuery(select_user, null);
        if (cursor_user.moveToFirst()) {
            do {
                bp_id = cursor_user.getString(0);
                U_ID = cursor_user.getString(1);
                USERNAME = cursor_user.getString(2);
            } while (cursor_user.moveToNext());
        }
        cursor_user.close();

        String select_prefix = "Select PREFIX from BILL_PREFIX where  BP_ID='" + bp_id + "'";
        Cursor cursor_prefix = MainActivity.db.rawQuery(select_prefix, null);
        if (cursor_prefix.moveToFirst()) {
            do {
                prefix = cursor_prefix.getString(0);
            } while (cursor_prefix.moveToNext());
        }
        cursor_prefix.close();
        tv_prefix.setText(prefix);
    }

    public boolean rowIdExists_billing(String name) {
        String select = "select I_NAME from BILLING where BILLNO='" + tv_bill_number.getText().toString() + "'";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        List<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(name)) {
                allMatch = false;
                break;
            }
        }
        return !allMatch;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void get_Billing_details() {
        item_names.clear();
        item_qnty.clear();
        item_price.clear();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Float> amount = new ArrayList<>();
        ArrayList<Float> qnty = new ArrayList<>();
        names.clear();
        amount.clear();
        qnty.clear();
        String select = "select BILLNO from BILLING";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                b_number = cursor.getInt(0);
                Log.e("b_number_01", String.valueOf(b_number));
            } while (cursor.moveToNext());
        }
        cursor.close();
        int count = cursor.getCount();
        if (count > 0) {
            if (get_details()) {
                String bm_id = null;
                tv_bill_number.setText(String.valueOf(b_number));
                String select_01 = "select I_NAME,AMOUNT,QTY,BM_ID from BILLING where BILLNO='" + tv_bill_number.getText().toString() + "' ";//And NTOTAL='"+0.0+"'";
                Cursor cursor_01 = MainActivity.db.rawQuery(select_01, null);
                if (cursor_01.moveToFirst()) {
                    do {
                        String name = cursor_01.getString(0);
                        Float amnt = cursor_01.getFloat(1);
                        Float qty = cursor_01.getFloat(2);
                        bm_id = cursor_01.getString(3);
                        names.add(name);
                        amount.add(amnt);
                        qnty.add(qty);
                    } while (cursor_01.moveToNext());
                }
                cursor_01.close();
                String select_bm_name = "SELECT BM_NAME from BILL_MODE where BM_ID='" + bm_id + "'";
                Cursor cursor_bm_name = MainActivity.db.rawQuery(select_bm_name, null);
                if (cursor_bm_name.moveToFirst()) {
                    do {
                        String bm_name = cursor_bm_name.getString(0);
                        billmode_spinner.setText(bm_name);
                        Log.e("bm_name", bm_name);
                    } while (cursor_bm_name.moveToNext());
                }
                cursor_bm_name.close();
                BillingAdapter myAdapter = new BillingAdapter(Objects.requireNonNull(getContext()), names, amount, qnty);
                lv_billing_product_count.setAdapter(myAdapter);
                Log.e("names", String.valueOf(names));

                int num_of_items = names.size();
                if (num_of_items == 0) {
                    tv_no_of_items.setVisibility(View.INVISIBLE);
                } else {
                    tv_no_of_items.setVisibility(View.VISIBLE);
                    tv_no_of_items.setText(String.valueOf(num_of_items + " items"));
                }
                Float cal = 0.0f;
                for (int cc = 0; cc < amount.size(); cc++) {
                    cal = cal + amount.get(cc);
                }
                @SuppressLint("DefaultLocale") String result = String.format("%.2f", cal);
                tv_before_tax_value.setText(result);
                set_tax_inc();
                set_total_value();
            } else {
                int Bill_num = b_number + 1;
                tv_bill_number.setText(String.valueOf(Bill_num));
                set_bm_name();
            }
        } else {
            int Bill_num = 1;
            tv_bill_number.setText(String.valueOf(Bill_num));
            set_bm_name();
        }
    }

    private void set_bm_name() {
        ArrayList<String> arrayList_bm_name = new ArrayList<>();
        String select1 = "SELECT BM_NAME from BILL_MODE";
        Cursor cursor1 = MainActivity.db.rawQuery(select1, null);
        if (cursor1.getCount() > 0) {
            if (cursor1.moveToFirst()) {
                do {
                    String name = cursor1.getString(0);
                    arrayList_bm_name.add(name);
                    billmode_spinner.setText(String.valueOf(arrayList_bm_name.get(0)));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
    }

    private boolean get_details() {
        String select = "SELECT NTOTAL from BILLING where BILLNO='" + b_number + "'";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                ntotal = cursor.getFloat(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ntotal == 0.0 || ntotal == null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void GetSalesPerson_details() {
        String role = "Admin";
        String select = "Select USERNAME from USER where ROLE !='" + role + "' ";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                String person_name = cursor.getString(0);
                salesperson_spin_list.add(person_name);
                sales_person_spinner.setText(person_name);
                Log.e("tv_salesperson_txt_val", sales_person_spinner.getText().toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
        sales_person_spinner_adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.drop_down_txt, salesperson_spin_list);
        sales_person_spinner.setAdapter(sales_person_spinner_adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void GetBillMode_details() {
        String select1 = "SELECT BM_NAME from BILL_MODE";
        Cursor cursor1 = MainActivity.db.rawQuery(select1, null);
        if (cursor1.moveToFirst()) {
            do {
                String name = cursor1.getString(0);
                billmode_spin_list.add(name);
                //   billmode_spinner.setText(billmode_spin_list.get(0));
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        billmode_spin_adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.drop_down_txt, billmode_spin_list);
        billmode_spinner.setAdapter(billmode_spin_adapter);
        billmode_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item_names.clear();
                if (toastMsg()) {
                    Toast.makeText(getContext(), "Bill Mode is changed to " + adapterView.getItemAtPosition(i).toString() + "." + "\n" + "So the prices would be changed.", Toast.LENGTH_LONG).show();
                }
                final ArrayList<String> arrayList_i_names = new ArrayList<>();
                ArrayList<Float> arrayList_i_rates = new ArrayList<>();
                ArrayList<Float> arrayList_i_quantity = new ArrayList<>();
                ArrayList<Float> arrayList_i_calc = new ArrayList<>();
                ArrayList<Float> arrayList_GTotal = new ArrayList<>();
                String spin = adapterView.getItemAtPosition(i).toString();

                String select_bm_id = "SELECT BM_ID from BILL_MODE where BM_NAME ='" + spin + "'";
                Cursor cursor_bm_id = MainActivity.db.rawQuery(select_bm_id, null);
                if (cursor_bm_id.moveToFirst()) {
                    do {
                        str_bm_id = cursor_bm_id.getString(0);
                    } while (cursor_bm_id.moveToNext());
                }
                cursor_bm_id.close();

                String number = tv_bill_number.getText().toString();
                String selection = "SELECT * from BILLING where BILLNO ='" + number + "'";
                Cursor cursor = MainActivity.db.rawQuery(selection, null);
                int c = cursor.getCount();
                if (c > 0) {
                    String select = "SELECT I_NAME from BILLING where BILLNO='" + number + "'";
                    Cursor cursor1 = MainActivity.db.rawQuery(select, null);
                    if (cursor1.moveToFirst()) {
                        do {
                            String name = cursor1.getString(0);
                            arrayList_i_names.add(name);
                            item_names.add(name);
                        } while (cursor1.moveToNext());
                    }
                    cursor1.close();
                    for (int j = 0; j <= arrayList_i_names.size() - 1; j++) {
                        //   if (j <= arrayList_i_names.size() - 1) {
                        //    String select_01 = "SELECT RATE from BILLING where BM_ID='"+str_bm_id+"'and I_NAME='" + arrayList_i_names.get(j) + "'and BILLNO='"+tv_bill_number.getText().toString()+"'";
                        String select_01 = "SELECT I_RATE from RATE where BM_NAME='" + spin + "' and I_NAME='" + arrayList_i_names.get(j) + "'";
                        Cursor cursor_01 = MainActivity.db.rawQuery(select_01, null);
                        if (cursor_01.getCount() > 0) {
                            if (cursor_01.moveToFirst()) {
                                do {
                                    Float rate = cursor_01.getFloat(0);
                                    arrayList_i_rates.add(rate);
                                } while (cursor_01.moveToNext());
                            }
                            cursor_01.close();
                        } else {
                            arrayList_i_rates.add(0.00f);
                        }
                        String select_02 = "SELECT QTY from BILLING where I_NAME='" + item_names.get(j) + "'and BILLNO='" + number + "'";
                        Cursor cursor_02 = MainActivity.db.rawQuery(select_02, null);
                        if (cursor_02.moveToFirst()) {
                            do {
                                Float qty = cursor_02.getFloat(0);
                                arrayList_i_quantity.add(qty);
                            } while (cursor_02.moveToNext());
                        }
                        cursor_02.close();

                        String select_tax = "SELECT CGST,SGST from ITEM where I_NAME='" + arrayList_i_names.get(j) + "'";
                        Cursor cursor_tax = MainActivity.db.rawQuery(select_tax, null);
                        if (cursor_tax.moveToFirst()) {
                            do {
                                cgst = cursor_tax.getFloat(0);
                                sgst = cursor_tax.getFloat(1);
                            } while (cursor_tax.moveToNext());
                        }
                        cursor_tax.close();
                        // calculation
                        Float flt_rate = arrayList_i_rates.get(j);
                        Float flt_qty = arrayList_i_quantity.get(j);
                        Float calc_value = flt_rate * flt_qty;
                        arrayList_i_calc.add(calc_value);

                        Float calc = cgst + sgst;
                        Float c1 = flt_rate * calc / 100;
                        Float GTotal = flt_rate + c1;
                        Float final_total = GTotal * flt_qty;
                        arrayList_GTotal.add(final_total);

                        ContentValues cv_billing_update = new ContentValues();
                        cv_billing_update.put("RATE", arrayList_i_rates.get(j));
                        cv_billing_update.put("AMOUNT", arrayList_i_calc.get(j));
                        cv_billing_update.put("BM_ID", str_bm_id);
                        cv_billing_update.put("QTY", arrayList_i_quantity.get(j));
                        cv_billing_update.put("GTOTAL", arrayList_GTotal.get(j));
                        MainActivity.db.update("BILLING", cv_billing_update,
                                "I_NAME='" + item_names.get(j) + "' and BILLNO='" + number + "'", null);
                        DBExport();
                        BillingAdapter myAdapter = new BillingAdapter(Objects.requireNonNull(getContext()), arrayList_i_names, arrayList_i_calc, arrayList_i_quantity);
                        lv_billing_product_count.setAdapter(myAdapter);
                        // }
                    }
                    Float before_tax = 0.00f;
                    for (int ii = 0; ii < arrayList_i_calc.size(); ii++) {
                        before_tax = before_tax + arrayList_i_calc.get(ii);
                    }
                    tv_before_tax_value.setText(String.valueOf(before_tax));

                }
                cursor.close();

                name_list.clear();
                i_rate_list.clear();
                String select_01 = "SELECT I_NAME from BILLING where RATE='" + 0.0 + "' and BILLNO='" + tv_bill_number.getText().toString() + "'";
                Cursor cursor_01 = MainActivity.db.rawQuery(select_01, null);
                if (cursor_01.getCount() > 0) {
                    if (cursor_01.moveToFirst()) {
                        do {
                            String names = cursor_01.getString(0);
                            name_list.add(names);
                        } while (cursor_01.moveToNext());
                    }
                    cursor_01.close();
                  /*  Log.e("dialoggg:", String.valueOf(name_list));*/
                    for (int k = 0; k <= name_list.size() - 1; k++) {
                        dialogbox_value_update(k);
                        //   method_02();
                    }
                }
            }
        });
    }

    private boolean toastMsg() {
        String select = "SELECT C_ID from CATEGORY";
        @SuppressLint("Recycle") Cursor cursor = MainActivity.db.rawQuery(select, null);
        int c_count = cursor.getCount();
        String select_1 = "SELECT C_ID from CATEGORY";
        @SuppressLint("Recycle") Cursor cursor_1 = MainActivity.db.rawQuery(select_1, null);
        int i_count = cursor_1.getCount();
        return c_count > 0 && i_count > 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void dialogbox_value_update(int value) {
        final Dialog dialog_01 = new Dialog(Objects.requireNonNull(getContext()));
        dialog_01.setContentView(R.layout.custom_alert_in_billing);
        dialog_01.setCancelable(false);
        dialog_01.show();
        Objects.requireNonNull(dialog_01.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_custom_alert_product_name = dialog_01.findViewById(R.id.tv_custom_alert_product_name);
        final EditText et_custom_alert_price_multi = dialog_01.findViewById(R.id.et_custom_alert_price);
        btn_custom_alert_submit = dialog_01.findViewById(R.id.btn_custom_alert_submit);
        tv_custom_alert_product_name.setText(name_list.get(value));
        et_custom_alert_price_multi.setText("");
        btn_custom_alert_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = et_custom_alert_price_multi.getText().toString();
                if (s1.isEmpty()) {
                    i_rate = 0.00f;
                    Utils.ShortToast(getString(R.string.please_enter_price_txt), getContext());
                } else {
                    i_rate = Float.valueOf(et_custom_alert_price_multi.getText().toString());
                    i_rate_list.add(i_rate);
                    Log.e("i_rate_list", i_rate_list.toString());

                    dialog_01.dismiss();
                    method_02();
                    item_names.clear();
                    item_price.clear();
                    item_qnty.clear();
                    //    Log.e("bill number:..", String.valueOf(bill_num));
                    String select_billing = "SELECT I_NAME,AMOUNT,QTY from BILLING where BILLNO = '" + tv_bill_number.getText().toString() + "'";
                    Cursor cursor_02 = MainActivity.db.rawQuery(select_billing, null);
                    if (cursor_02.moveToFirst()) {
                        do {
                            String item_name = cursor_02.getString(0);
                            Float item_rate = cursor_02.getFloat(1);
                            Float item_qty = cursor_02.getFloat(2);
                            item_names.add(item_name);
                            item_price.add(item_rate);
                            item_qnty.add(item_qty);
                        } while (cursor_02.moveToNext());
                    }
                    cursor_02.close();

                    BillingAdapter myAdapter = new BillingAdapter(Objects.requireNonNull(getContext()), item_names, item_price, item_qnty);
                    lv_billing_product_count.setAdapter(myAdapter);

                    Float before_tax = 0.00f;
                    for (int ii = 0; ii < item_price.size(); ii++) {
                        before_tax = before_tax + item_price.get(ii);
                    }
                    tv_before_tax_value.setText(String.valueOf(before_tax));
                    set_tax_inc();
                    set_total_value();
                }
            }
        });
        dialog_01.show();
    }

    private void method_02() {

        ArrayList<String> reverse_name_list = new ArrayList<>();
        ArrayList<Float> qty_list = new ArrayList<>();
        ArrayList<Float> calc_amt_list = new ArrayList<>();
        ArrayList<Float> arrayList_GTotal = new ArrayList<>();
        reverse_name_list.clear();
        qty_list.clear();
        for (int z = name_list.size() - 1; z >= 0; z--) {
            reverse_name_list.add(name_list.get(z));
         }
        for (int j = 0; j <= i_rate_list.size() - 1; j++) {
            if (j <= reverse_name_list.size() - 1) {
                String select_qty = "SELECT QTY from BILLING " +
                        "where I_NAME='" + reverse_name_list.get(j) + "'and BILLNO='" + tv_bill_number.getText().toString() + "'";
                Cursor cursor_qty = MainActivity.db.rawQuery(select_qty, null);
                if (cursor_qty.moveToFirst()) {
                    do {
                        Float qty = cursor_qty.getFloat(0);
                        qty_list.add(qty);
                    } while (cursor_qty.moveToNext());
                }
                cursor_qty.close();

                Float calc = i_rate_list.get(j) * qty_list.get(j);
                calc_amt_list.add(calc);

                String select_tax = "SELECT CGST,SGST from ITEM where I_NAME='" + reverse_name_list.get(j) + "'";
                Cursor cursor_tax = MainActivity.db.rawQuery(select_tax, null);
                if (cursor_tax.moveToFirst()) {
                    do {
                        cgst = cursor_tax.getFloat(0);
                        sgst = cursor_tax.getFloat(1);
                    } while (cursor_tax.moveToNext());
                }
                cursor_tax.close();
                // calculation
                Float flt_rate = i_rate_list.get(j);
                Float flt_qty = qty_list.get(j);

                Float calc_01 = cgst + sgst;
                Float c1 = flt_rate * calc_01 / 100;
                Float GTotal = flt_rate + c1;
                Float final_total = GTotal * flt_qty;
                arrayList_GTotal.add(final_total);

                ContentValues cv = new ContentValues();
                cv.put("RATE", i_rate_list.get(j));
                cv.put("AMOUNT", calc_amt_list.get(j));
                cv.put("GTOTAL", arrayList_GTotal.get(j));
                MainActivity.db.update("BILLING", cv, "I_NAME='" + reverse_name_list.get(j) + "' and BILLNO='" + tv_bill_number.getText().toString() + "'", null);
                Log.e("ContentValues", String.valueOf(cv));
                DBExport();
            }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spinner_sales_person) {
            String sales_person_name = adapterView.getItemAtPosition(i).toString();
            sales_person_spinner.setText(sales_person_name);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
