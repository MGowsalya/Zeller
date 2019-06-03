package com.example.admin.zeller.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.zeller.Adapters.ReportsAdapter;
import com.example.admin.zeller.MainActivity;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.zellerr.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class Reports_Frag extends Fragment {
    TextView tv_today_sales_value, tv_reports_by;
    MaterialBetterSpinner spinner_bill_mode;
    TextView spinner_from;
    MaterialBetterSpinner spinner_bill_prefix_reports_page;
    TextView spinner_to;
    RelativeLayout relative_listview_lay;
    ListView listview_reports_frag;

    String standard_date = "";
    DatePickerDialog.OnDateSetListener from_dateSetListener;
    DatePickerDialog.OnDateSetListener to_dateSetListener;

    String[] prefix_arraylist;
    ArrayAdapter<String> bill_prefix_adapter;
    ArrayAdapter<String> bm_name_adapter;
    ArrayList<String> bm_name_list = new ArrayList<>();

    ArrayList<String> prefix_list;
    ArrayList<String> date_list;
    ArrayList<String> billl_num_list;
    ArrayList<Float> before_tax_list;
    ArrayList<Float> tax_list;
    ArrayList<Float> net_total_list;
    String date, str_prefix, str_bm_name, str_bm_id;
    ConstraintLayout reports_page_title_layout;
    TextView tv_no_data_found;
    Button btn_submit;
    String from, to;
    ArrayList<String> arrayList_datelist = new ArrayList<>();

    private int currentApiVersion;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_reports__frag, container, false);
        /*View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

        //  getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);

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


        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        tv_today_sales_value = view.findViewById(R.id.tv_today_sales_value);
        spinner_bill_mode = view.findViewById(R.id.spinner_bills);
        spinner_from = view.findViewById(R.id.spinner_from);
        spinner_bill_prefix_reports_page = view.findViewById(R.id.spinner_bill_prefix_reports_page);
        spinner_to = view.findViewById(R.id.spinner_to);
        relative_listview_lay = view.findViewById(R.id.relative_listview_lay);
        listview_reports_frag = view.findViewById(R.id.listview_reports_frag);
        tv_reports_by = view.findViewById(R.id.tv_reports_by);
        reports_page_title_layout = view.findViewById(R.id.reports_page_title_layout);
        tv_no_data_found = view.findViewById(R.id.tv_no_data_found);
        btn_submit = view.findViewById(R.id.btn_submit);

        prefix_arraylist = getResources().getStringArray(R.array.prefix_salesperson);
        Log.e("prefix: ", Arrays.toString(prefix_arraylist));
        bill_prefix_adapter = new ArrayAdapter<>(getActivity(), R.layout.prefix_lay, prefix_arraylist);
        bill_prefix_adapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        spinner_bill_prefix_reports_page.setAdapter(bill_prefix_adapter);
        spinner_bill_prefix_reports_page.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                str_prefix = adapterView.getItemAtPosition(i).toString();
            }
        });
        date_list = new ArrayList<>();
        prefix_list = new ArrayList<>();
        billl_num_list = new ArrayList<>();
        before_tax_list = new ArrayList<>();
        tax_list = new ArrayList<>();
        net_total_list = new ArrayList<>();

        date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());


        String select_today_sale = "SELECT sum(GTOTAL) from BILLING where BDATE='" + date + "' and CANCELLED ='" + 0 + "'";
        Cursor cursor_today_sale = MainActivity.db.rawQuery(select_today_sale, null);
        if (cursor_today_sale.moveToFirst()) {
            do {
                Float today_sale = cursor_today_sale.getFloat(0);
                @SuppressLint("DefaultLocale") String result = String.format("%.2f", today_sale);
                tv_today_sales_value.setText(String.valueOf(result));
                //       Log.e("result:", String.valueOf(today_sale));
            } while (cursor_today_sale.moveToNext());
        }
        cursor_today_sale.close();

        String select_billmode = "SELECT BM_NAME from BILL_MODE";
        final Cursor cursor_billmode = MainActivity.db.rawQuery(select_billmode, null);
        if (cursor_billmode.moveToFirst()) {
            do {
                String names = cursor_billmode.getString(0);
                bm_name_list.add(names);
            } while (cursor_billmode.moveToNext());
        }
        cursor_billmode.close();
        Collections.sort(bm_name_list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        bm_name_adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.prefix_lay, bm_name_list);
        spinner_bill_mode.setAdapter(bm_name_adapter);
        //   spinner_bill_mode.setText(bm_name_adapter.getItem(0));
        spinner_bill_mode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                str_bm_name = adapterView.getItemAtPosition(i).toString();
            }
        });


        spinner_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showFromDatePicker();
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                @SuppressLint("ResourceType") DatePickerDialog datePickerDialog =
                        new DatePickerDialog(Objects.requireNonNull(getContext()),
                                android.R.style.Theme_Holo_Dialog, from_dateSetListener, year, month, day);
                Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
                // datePicker.show
            }
        });
        from_dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                int m = monthOfYear + 1;
                if (dayOfMonth < 10) {
                    standard_date = standard_date + (String.valueOf("0" + dayOfMonth) + "/");
                } else {
                    standard_date = standard_date + (String.valueOf(dayOfMonth) + "/");
                }
                if (m < 10) {
                    standard_date = standard_date + (String.valueOf("0" + m) + "/");
                } else {
                    standard_date = standard_date + (String.valueOf(m) + "/");

                }
                Log.e("date:", standard_date);
                standard_date = standard_date + String.valueOf(year);
                spinner_from.setText(standard_date);
                standard_date = "";
            }
        };
        spinner_to.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {


                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                @SuppressLint("ResourceType") DatePickerDialog datePickerDialog =
                        new DatePickerDialog(Objects.requireNonNull(getContext()),
                                android.R.style.Theme_Holo_Dialog, to_dateSetListener, year, month, day);
                Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();


            }
        });
        to_dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                int m = monthOfYear + 1;
                if (dayOfMonth < 10) {
                    standard_date = standard_date + (String.valueOf("0" + dayOfMonth) + "/");
                } else {
                    standard_date = standard_date + (String.valueOf(dayOfMonth) + "/");
                }
                if (m < 10) {
                    standard_date = standard_date + (String.valueOf("0" + m) + "/");
                } else {
                    standard_date = standard_date + (String.valueOf(m) + "/");
                }
                standard_date = standard_date + String.valueOf(year);
                spinner_to.setText(standard_date);
                standard_date = "";
            }
        };

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_list.clear();
                prefix_list.clear();
                billl_num_list.clear();
                net_total_list.clear();
                before_tax_list.clear();
                tax_list.clear();
                arrayList_datelist.clear();
                if ((spinner_from.getText().toString().isEmpty()) &&
                        (spinner_to.getText().toString().isEmpty()) && (spinner_bill_mode.getText().toString().isEmpty())) {
                    Toast.makeText(getActivity(), R.string.pls_enter_detaisl_txt, Toast.LENGTH_LONG).show();
                } else if (spinner_from.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), R.string.pls_enter_from_date_txt, Toast.LENGTH_LONG).show();
                } else if (spinner_to.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), R.string.pls_enter_to_date_txt, Toast.LENGTH_LONG).show();
                } else if (spinner_bill_mode.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), R.string.pls_enter_bill_mode_txt, Toast.LENGTH_LONG).show();
                } else {
                    if (get_Details()) {
                        datelist_array();
                        reports_page_title_layout.setVisibility(View.VISIBLE);
                        listview_reports_frag.setVisibility(View.VISIBLE);
                        tv_no_data_found.setVisibility(View.GONE);
                        ReportsAdapter reportsAdapter = new ReportsAdapter(getActivity(), arrayList_datelist, prefix_list, billl_num_list, net_total_list, before_tax_list, tax_list);
                        listview_reports_frag.setAdapter(reportsAdapter);
                    } else {
                        listview_reports_frag.setVisibility(View.GONE);
                        reports_page_title_layout.setVisibility(View.GONE);
                        tv_no_data_found.setVisibility(View.VISIBLE);

                    }
                }

            }
        });

        return view;
    }

    private boolean get_Details() {
        String select_query;
        date_list.clear();
        prefix_list.clear();
        billl_num_list.clear();
        net_total_list.clear();
        before_tax_list.clear();
        tax_list.clear();

        //  Toast.makeText(getContext(), "bm_id:"+str_bm_name, Toast.LENGTH_SHORT).show();
        String select_bm_id = "SELECT BM_ID from BILL_MODE where BM_NAME='" + str_bm_name + "'";
        Cursor cursor_bm_id = MainActivity.db.rawQuery(select_bm_id, null);
        if (cursor_bm_id.moveToFirst()) {
            do {
                str_bm_id = cursor_bm_id.getString(0);
            } while (cursor_bm_id.moveToNext());
        }
        cursor_bm_id.close();
        dateFormat();

        if(str_prefix==null || str_prefix.isEmpty()){
             select_query = "SELECT BDATE,PREFIX,BILLNO,NTOTAL,sum(AMOUNT),sum(GTOTAL) from BILLING " +
                    "where BDATE BETWEEN '" + from + "' AND '" + to + "' " +
                    "and PREFIX='" + spinner_bill_prefix_reports_page.getText().toString() + "' and BM_ID='" + str_bm_id + "' and CANCELLED ='" + 0 + "' GROUP BY BILLNO ";

        }
        else{
            select_query = "SELECT BDATE,PREFIX,BILLNO,NTOTAL,sum(AMOUNT),sum(GTOTAL) from BILLING " +
                    "where BDATE BETWEEN '" + from + "' AND '" + to + "' " +
                    "and PREFIX='" + str_prefix + "' and BM_ID='" + str_bm_id + "' and CANCELLED ='" + 0 + "' GROUP BY BILLNO ";
        }
        Cursor cursor = MainActivity.db.rawQuery(select_query, null);
        Log.e("count_value", String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                String prefix = cursor.getString(1);
                String bill_num = cursor.getString(2);
                Float gtotal = cursor.getFloat(4);
                Float net_total = cursor.getFloat(5);
                Log.e("gtotal", String.valueOf(gtotal));
                date_list.add(date);
                prefix_list.add(prefix);
                billl_num_list.add(bill_num);
                @SuppressLint("DefaultLocale") String result_1 = String.format("%.2f", net_total);
                @SuppressLint("DefaultLocale") String result_2 = String.format("%.2f", gtotal);
                net_total_list.add(Float.valueOf(result_1));
                before_tax_list.add(Float.valueOf(result_2));

            } while (cursor.moveToNext());
        }
        Float cc;
        for (int i = 0; i <= before_tax_list.size() - 1; i++) {
            cc = net_total_list.get(i) - before_tax_list.get(i);
            @SuppressLint("DefaultLocale") String result_3 = String.format("%.2f", cc);
            tax_list.add(Float.valueOf(result_3));
            Log.e("calculated_list", before_tax_list.get(i) + " " + tax_list.get(i) + " " + net_total_list.get(i));
        }
        cursor.close();
        return cursor.getCount() > 0;
    }

    private void dateFormat() {

        Log.e("TAG", "original format: " + spinner_from.getText().toString());
        Log.e("TAG", "original format2: " + spinner_to.getText().toString());
        from = spinner_from.getText().toString();
        to = spinner_to.getText().toString();

        try {
            //create SimpleDateFormat object with source string date format
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfSource = new SimpleDateFormat("dd/MM/yyyy");

            //parse the string into Date object
            Date date = sdfSource.parse(from);
            Date date2 = sdfSource.parse(to);

            //create SimpleDateFormat object with desired date format
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy/MM/dd");

            //parse the date into another format
            from = sdfDestination.format(date);
            to = sdfDestination.format(date2);
            //  date_list.add(from);
            Log.e("TAG", "converted format: " + from);
            Log.e("TAG", "converted format2: " + to);

        } catch (ParseException pe) {
            System.out.println("Parse Exception : " + pe);
        }
    }

    private void datelist_array() {
        for (int i = 0; i <= date_list.size() - 1; i++) {
            try {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy/MM/dd");
                Date date = sdfSource.parse(date_list.get(i));
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
                arrayList_datelist.add(sdfDestination.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Log.e("arrayList_datelist", String.valueOf(arrayList_datelist));
    }

}
