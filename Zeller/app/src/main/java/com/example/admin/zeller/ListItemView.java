package com.example.admin.zeller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.admin.zeller.Adapters.ListViewAdapter;
import com.example.admin.zeller.Fragments.Bill_View_Fragment;
import com.example.admin.zeller.Fragments.Item_Wise_Detail_List_Fragment;
import com.zellerr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.admin.zeller.Fragments.Bill_View_Fragment.billno_list;
import static com.example.admin.zeller.Fragments.Bill_View_Fragment.executive_list;
import static com.example.admin.zeller.Fragments.Bill_View_Fragment.highlight_cancel_list;
import static com.example.admin.zeller.Fragments.Bill_View_Fragment.time_list;
import static com.example.admin.zeller.Fragments.Bill_View_Fragment.value_list;

@SuppressLint("ViewConstructor")
public class ListItemView extends FrameLayout {
    TextView tv_bill_no_bill_view_adapt_lay;
    TextView tv_date_bill_view_adapt_lay,tv_time_bill_view_adapt_lay,
    tv_value_bill_view_adapt_lay,tv_executive_bill_view_adapt_lay,tv_view_bill_view_adapt_lay;
    Button btn_cancel_bill_view__adapt_lay;
    public static String bn;

    public ListItemView(final Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.bill_view_adapter_layout, this);
        tv_bill_no_bill_view_adapt_lay = getRootView().findViewById(R.id.tv_bill_no_bill_view_adapt_lay);
        tv_date_bill_view_adapt_lay = getRootView().findViewById(R.id.tv_date_bill_view_adapt_lay);
        tv_time_bill_view_adapt_lay = getRootView().findViewById(R.id.tv_time_bill_view_adapt_lay);
        tv_value_bill_view_adapt_lay = getRootView().findViewById(R.id.tv_value_bill_view_adapt_lay);
        tv_executive_bill_view_adapt_lay = getRootView().findViewById(R.id.tv_executive_bill_view_adapt_lay);
        tv_view_bill_view_adapt_lay = getRootView().findViewById(R.id.tv_view_bill_view_adapt_lay);
        btn_cancel_bill_view__adapt_lay = getRootView().findViewById(R.id.btn_cancel_bill_view__adapt_lay);
        tv_view_bill_view_adapt_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bn = tv_bill_no_bill_view_adapt_lay.getText().toString();
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                Fragment fragment = new Item_Wise_Detail_List_Fragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.static_page_linearlayout, fragment)
                        .commit();
            }
        });
        String role = null;
        String select_user = "SELECT USERNAME,STATUS,ROLE from USER where STATUS=1";
        Cursor cursor = SplashScreen_Act.db.rawQuery(select_user,null);
        if(cursor.moveToFirst()){
            do{
                role = cursor.getString(2);
            }while (cursor.moveToNext());
        }
        cursor.close();
        assert role != null;
        if(role.equalsIgnoreCase("Admin")){
            btn_cancel_bill_view__adapt_lay.setVisibility(View.VISIBLE);
        }
        else {
            btn_cancel_bill_view__adapt_lay.setVisibility(View.GONE);
        }

        btn_cancel_bill_view__adapt_lay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                final String b_no = tv_bill_no_bill_view_adapt_lay.getText().toString();
                String cancel_text = btn_cancel_bill_view__adapt_lay.getText().toString();
                if(cancel_text.equalsIgnoreCase("Cancel")) {
                    display_01();
                    ContentValues contentValues_cancel = new ContentValues();
                    contentValues_cancel.put("CANCELLED", 1);
                    SplashScreen_Act.db.update("BILLING", contentValues_cancel, "BILLNO='" + b_no + "'", null);
                    btn_cancel_bill_view__adapt_lay.setText("Uncancel");
                    DBExport();
                 }
                 if(cancel_text.equalsIgnoreCase("Uncancel")) {
                    display_02();
                    ContentValues contentValues_cancel = new ContentValues();
                    contentValues_cancel.put("CANCELLED", 0);
                    SplashScreen_Act.db.update("BILLING", contentValues_cancel, "BILLNO='" + b_no + "'", null);
                    btn_cancel_bill_view__adapt_lay.setText("Cancel");
                    DBExport();
                }

                billno_list.clear();
                Bill_View_Fragment.date_list.clear();
                time_list.clear();
                value_list.clear();
                executive_list.clear();
                Bill_View_Fragment.cancel_list.clear();
                highlight_cancel_list.clear();

                String select_bills = "SELECT BILLNO,BDATE,NTOTAL,USERNAME,BTIME,CANCELLED from BILLING  Group By BILLNO";// where BILLNO='"+145+"'";
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
                ListViewAdapter listViewAdapter = new ListViewAdapter((View.OnClickListener) activity,billno_list,Bill_View_Fragment.arrayList_datelist,time_list,
                        value_list,executive_list,highlight_cancel_list);
                Bill_View_Fragment.bill_view_listview.setAdapter(listViewAdapter);
                listViewAdapter.selectedPositions.clear();
                listViewAdapter.selectedPositions.addAll(highlight_cancel_list);
                listViewAdapter.notifyDataSetChanged();
            }
        });
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
         //       Toast.makeText(getContext(), "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display(String bno,String date,String time,String value,String executive, boolean isSelected) {
        tv_bill_no_bill_view_adapt_lay.setText(bno);
        tv_date_bill_view_adapt_lay.setText(date);
        tv_time_bill_view_adapt_lay.setText(time);
                            Float total = Float.valueOf(value);
                            @SuppressLint("DefaultLocale") String f = String.format("%.2f",total);

        tv_value_bill_view_adapt_lay.setText(f);
        tv_executive_bill_view_adapt_lay.setText(executive);
        display(isSelected);
    }

    @SuppressLint("ResourceAsColor")
    public void display(boolean isSelected) {
        tv_bill_no_bill_view_adapt_lay.setTextColor(isSelected ? getResources().getColor(R.color.colorRed) : getResources().getColor(R.color.colorBlack));
        tv_date_bill_view_adapt_lay.setTextColor(isSelected ? getResources().getColor(R.color.colorRed) : getResources().getColor(R.color.colorBlack));
        tv_time_bill_view_adapt_lay.setTextColor(isSelected ? getResources().getColor(R.color.colorRed) : getResources().getColor(R.color.colorBlack));
        tv_value_bill_view_adapt_lay.setTextColor(isSelected ? getResources().getColor(R.color.colorRed) : getResources().getColor(R.color.colorBlack));
        tv_executive_bill_view_adapt_lay.setTextColor(isSelected ? getResources().getColor(R.color.colorRed) : getResources().getColor(R.color.colorBlack));
        btn_cancel_bill_view__adapt_lay.setText(isSelected ? "Uncancel" : "Cancel");
    }
    public void display_01(){
        tv_bill_no_bill_view_adapt_lay.setTextColor(getResources().getColor(R.color.colorRed));
        tv_date_bill_view_adapt_lay.setTextColor(getResources().getColor(R.color.colorRed));
        tv_time_bill_view_adapt_lay.setTextColor(getResources().getColor(R.color.colorRed));
        tv_value_bill_view_adapt_lay.setTextColor(getResources().getColor(R.color.colorRed));
        tv_executive_bill_view_adapt_lay.setTextColor(getResources().getColor(R.color.colorRed));
    }

    public void display_02(){
        tv_bill_no_bill_view_adapt_lay.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_date_bill_view_adapt_lay.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_time_bill_view_adapt_lay.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_value_bill_view_adapt_lay.setTextColor(getResources().getColor(R.color.colorBlack));
        tv_executive_bill_view_adapt_lay.setTextColor(getResources().getColor(R.color.colorBlack));

    }

}
