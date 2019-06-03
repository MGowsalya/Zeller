package com.example.admin.zeller.Fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.zeller.Adapters.ListViewAdapter;
import com.example.admin.zeller.MainActivity;
import com.example.admin.zeller.SplashScreen_Act;
import com.zellerr.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Bill_View_Fragment extends Fragment {
    TextView tv_add_icon_bill_view_lay, tv_add_new_bill_view_lay;
    Fragment fragment;
    FragmentTransaction ft;

   @SuppressLint("StaticFieldLeak")
   public static ListView bill_view_listview;
   public static ArrayList<String> billno_list = new ArrayList<>();
    public static ArrayList<String> date_list = new ArrayList<>();
    public static ArrayList<String> time_list = new ArrayList<>();
    public static ArrayList<Float> value_list = new ArrayList<>();
    public static ArrayList<String> executive_list = new ArrayList<>();
    public static ArrayList<Integer> cancel_list = new ArrayList<>();
   public static ArrayList<Integer> highlight_cancel_list = new ArrayList<>();
    public static ArrayList<String> arrayList_datelist;

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.bill_view_layout, null, false);

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
        tv_add_icon_bill_view_lay = view.findViewById(R.id.tv_add_icon_bill_view_lay);
        tv_add_new_bill_view_lay = view.findViewById(R.id.tv_add_new_bill_view_lay);
        bill_view_listview = view.findViewById(R.id.bill_view_listview);

        String role = null;
        String select_user = "SELECT ROLE from USER where STATUS=1";
        Cursor cursor = SplashScreen_Act.db.rawQuery(select_user,null);
        if(cursor.moveToFirst()){
            do{
                role = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("role:",role);
        assert role != null;
        if(role.equalsIgnoreCase("Admin")){
            tv_add_new_bill_view_lay.setVisibility(View.GONE);
            tv_add_icon_bill_view_lay.setVisibility(View.GONE);
        }
        else {
            tv_add_new_bill_view_lay.setVisibility(View.VISIBLE);
            tv_add_icon_bill_view_lay.setVisibility(View.VISIBLE);

        }
        tv_add_new_bill_view_lay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                    fragment_transaction();
            }
        });
        tv_add_icon_bill_view_lay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                fragment_transaction();
            }
        });
        listView_Details();
        return view;
    }

    public void listView_Details() {
        billno_list.clear();
        date_list.clear();
        time_list.clear();
        value_list.clear();
        executive_list.clear();
        cancel_list.clear();
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
                date_list.add(date);
                time_list.add(time);
             /*   @SuppressLint("DefaultLocale") String format_NTotal = String.format("%.2f",NTotal);
                value_list.add(Float.valueOf(format_NTotal));
                Log.e("value_list:",value_list.toString());*/
                value_list.add(NTotal);
                executive_list.add(username);
                cancel_list.add(cancel);
            } while (cursor_bills.moveToNext());
        }
        cursor_bills.close();
        datelist_format();
        for(int i=0; i<=cancel_list.size()-1; i++){
            if(cancel_list.get(i)==1) {
                highlight_cancel_list.add(i);
            }
        }

          ListViewAdapter listViewAdapter = new ListViewAdapter((View.OnClickListener) getActivity(),billno_list,arrayList_datelist,time_list,
                                             value_list,executive_list,highlight_cancel_list);
        bill_view_listview.setAdapter(listViewAdapter);
        listViewAdapter.selectedPositions.clear();
        listViewAdapter.selectedPositions.addAll(highlight_cancel_list);
        listViewAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void fragment_transaction() {
        fragment = new Billing_Fragment();
        ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.static_page_linearlayout, fragment);
        ft.commit();
    }

    private void datelist_format() {
        arrayList_datelist = new ArrayList<>();
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
    }
}
