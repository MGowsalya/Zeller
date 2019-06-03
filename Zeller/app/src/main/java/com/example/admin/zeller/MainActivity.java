package com.example.admin.zeller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.zeller.Fragments.Bill_View_Fragment;
import com.example.admin.zeller.Fragments.Billing_Fragment;
import com.example.admin.zeller.Fragments.Items_fragment;
import com.example.admin.zeller.Fragments.Reports_Frag;
import com.example.admin.zeller.Fragments.Setting_Fragment;
import com.facebook.FacebookSdk;
import com.zellerr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    public static ImageButton ib_billing, ib_items, ib_reports, ib_settings;
    public static Fragment fragment;
    public static FragmentTransaction ft;
    public static SQLiteDatabase db;

    public static ArrayList<Integer> btn_cancel_arraylist = new ArrayList<>();
    int PERMISSION_ALL = 1;
    String[] permission = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public static final int MULITPLE_PERMISSIONS = 10;

    TextView tv_dialog_text;
    Button btn_dialog_ok;


    Dialog dialog_01;
    Button btn_yes_dialog_01;
    Button btn_no_dialog_01;
    private boolean doubleBackToExitPressedOnce = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.static_bar);
        FacebookSdk.sdkInitialize(getApplicationContext());

        if (!hasPermissions(this, permission)) {
            ActivityCompat.requestPermissions(this, permission, PERMISSION_ALL);
        }
//        CheckRunTimePermission();
        ib_billing = findViewById(R.id.billing_imagebutton);
        ib_items = findViewById(R.id.item_imagebutton);
        ib_reports = findViewById(R.id.reports_imagebutton);
        ib_settings = findViewById(R.id.settings_imagebutton);
        ib_billing.setOnClickListener(this);
        ib_items.setOnClickListener(this);
        ib_reports.setOnClickListener(this);
        ib_settings.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).hide();
        /*Database Queries*/
        db = getApplicationContext().openOrCreateDatabase("Zeller.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists COMPANY(CC_ID Integer primary key autoincrement,CC_NAME varchar,ADD_1 varchar," +
                "ADD_2 varchar,ADD_3 varchar,PLACE varchar,GSTNO varchar,PHONENO varchar)");
        db.execSQL("create table if not exists BILL_PREFIX(BP_ID Integer primary key autoincrement,PREFIX varchar(1),BILLNO int)");
        db.execSQL("create table if not exists BILL_MODE(BM_ID Integer primary key autoincrement,BM_NAME varchar)");
        db.execSQL("create table if not exists CATEGORY(C_ID Integer primary key autoincrement,C_NAME varchar,CREATED_ON Date)");
        db.execSQL("create table if not exists ITEM(I_ID Integer primary key autoincrement,I_NAME varchar,C_ID int" +
                ",C_NAME varchar,CGST double,SGST double,UNITS varchar,FAVOURITES int, CREATED_ON Date)");
        db.execSQL("create table if not exists RATE(R_ID Integer primary key autoincrement,BM_ID int,BM_NAME varchar,I_ID int,I_NAME varchar,I_RATE double)");
        db.execSQL("create table if not exists SETTING(BDATE Date,GST int,F_MESSAGE_1 varchar,F_MESSAGE_2 varchar,LANGUAGE varchar,BLUETOOTH_NAME varchar,BLUETOOTH_MAC varchar)");

        db.execSQL("create table if not exists BILLING(IDS Integer primary key autoincrement,PREFIX varchar(1),BILLNO int," +
                "BDATE Date,BP_ID int,BM_ID int,I_ID int,I_NAME varchar,RATE double,CGST double,SGST double,QTY double," +
                "AMOUNT double,GTOTAL double,LESS double,NTOTAL double,CANCELLED int,BTIME Time,U_ID int,USERNAME varchar)");
     //   db.execSQL("create table if not exists USER(U_ID Integer primary key autoincrement,USERNAME varchar,PASSWORD varchar,ROLE varchar,BP_ID int)");
        db.execSQL("create table if not exists USER(U_ID Integer primary key autoincrement,USERNAME varchar,PASSWORD varchar,ROLE varchar,BP_ID int," +
                "LOGIN_DATE date,LOGIN_TIME time,STATUS int,IMEI varchar)");

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULITPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    for (String per : permissions) {
                    }
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_imagebutton:
                String select_category = "SELECT * from CATEGORY";
                Cursor cursor_categoty = SplashScreen_Act.db.rawQuery(select_category,null);
             //   Toast.makeText(this, "cursor_category"+cursor_categoty.getCount(), Toast.LENGTH_SHORT).show();
                cursor_categoty.close();
                hideSoftKeyboard();
                ib_items.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorYellow));
                ib_items.setImageResource(R.drawable.items_b);
                fragment = new Items_fragment();
                if (!GetCompanyDetails()) {
                    /*ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.static_page_linearlayout, fragment);
                    ft.commit();*/
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.static_page_linearlayout, fragment)
                            .commit();
                } else if (!GetBillModeDetails()) {
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.static_page_linearlayout, fragment)
                            .commit();
                } else if (!GetUserNameDetails()) {
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.static_page_linearlayout, fragment)
                            .commit();
                } else {
                    fragment = new Items_fragment();
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.static_page_linearlayout, fragment)
                            .commit();
                }
                ib_billing.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_billing.setImageResource(R.drawable.billing__y);

                ib_reports.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_reports.setImageResource(R.drawable.reports_y);

                ib_settings.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_settings.setImageResource(R.drawable.settings__y);
                break;
            case R.id.billing_imagebutton:
                hideSoftKeyboard();
                Log.e("btn_cancel_arraylist",btn_cancel_arraylist.toString());

                // ib_billing.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                ib_billing.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorYellow));
                ib_billing.setImageResource(R.drawable.billing__b);
                fragment = new Bill_View_Fragment();
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.static_page_linearlayout, fragment)
                        .commit();
                ib_items.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_items.setImageResource(R.drawable.items_y);
                ib_reports.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_reports.setImageResource(R.drawable.reports_y);
                ib_settings.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_settings.setImageResource(R.drawable.settings__y);
                break;
            case R.id.reports_imagebutton:
                hideSoftKeyboard();
                ib_reports.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorYellow));
                ib_reports.setImageResource(R.drawable.reports_b);
                fragment = new Reports_Frag();
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.static_page_linearlayout, fragment)
                        .commit();
                ib_billing.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_billing.setImageResource(R.drawable.billing__y);
                ib_items.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_items.setImageResource(R.drawable.items_y);
                ib_settings.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_settings.setImageResource(R.drawable.settings__y);
                break;
            case R.id.settings_imagebutton:

                hideSoftKeyboard();
                ib_settings.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorYellow));
                ib_settings.setImageResource(R.drawable.settings__b);
                fragment = new Setting_Fragment();
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null) //add itself (FirstFragment) into the stack
                        .replace(R.id.static_page_linearlayout, fragment)
                        .commit();

                ib_billing.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_billing.setImageResource(R.drawable.billing__y);
                ib_items.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_items.setImageResource(R.drawable.items_y);
                ib_reports.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                ib_reports.setImageResource(R.drawable.reports_y);
                break;

            default:
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean GetUserNameDetails() {
        String select = "SELECT USERNAME from USER";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        int n1 = cursor.getCount();
        if (n1 == 0) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.main_act_dialog_window);
            (Objects.requireNonNull(dialog.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            tv_dialog_text = dialog.findViewById(R.id.tv_error_message);
            btn_dialog_ok = dialog.findViewById(R.id.bt_dialog_ok);
            tv_dialog_text.setText(R.string.please_fill_user_details);
            btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ib_settings.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorYellow));
                    ib_settings.setImageResource(R.drawable.settings__b);
                    fragment = new Setting_Fragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.static_page_linearlayout, fragment);
                    ft.commit();
                    ib_billing.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    ib_billing.setImageResource(R.drawable.billing__y);
                    ib_items.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    ib_items.setImageResource(R.drawable.items_y);
                    ib_reports.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    ib_reports.setImageResource(R.drawable.reports_y);
                    dialog.dismiss();
                }
            });
            dialog.show();

            return false;
        }
        cursor.close();
        return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean GetBillModeDetails() {
        String select = "SELECT BM_NAME from BILL_MODE";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        int n1 = cursor.getCount();
        if (n1 <= 0) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.main_act_dialog_window);
            (Objects.requireNonNull(dialog.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            tv_dialog_text = dialog.findViewById(R.id.tv_error_message);
            btn_dialog_ok = dialog.findViewById(R.id.bt_dialog_ok);
            tv_dialog_text.setText(R.string.please_fill_billing_details);
            btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ib_settings.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorYellow));
                    ib_settings.setImageResource(R.drawable.settings__b);
                    fragment = new Setting_Fragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.static_page_linearlayout, fragment);
                    ft.commit();

                    Setting_Fragment setting_fragment = new Setting_Fragment();
                    setting_fragment.GetBillingValues();

                    ib_billing.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    ib_billing.setImageResource(R.drawable.billing__y);
                    ib_items.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    ib_items.setImageResource(R.drawable.items_y);
                    ib_reports.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    ib_reports.setImageResource(R.drawable.reports_y);
                    dialog.dismiss();

                }
            });
            dialog.show();
            return false;
        }
        cursor.close();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean GetCompanyDetails() {
        String select = "SELECT CC_NAME from COMPANY";
        Cursor cursor_01 = MainActivity.db.rawQuery(select, null);
        int n1 = cursor_01.getCount();
        if (n1 == 0) {
//            Toast.makeText(this, "Cursor_count" + n1, Toast.LENGTH_SHORT).show();
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.main_act_dialog_window);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            tv_dialog_text = dialog.findViewById(R.id.tv_error_message);
            btn_dialog_ok = dialog.findViewById(R.id.bt_dialog_ok);
            tv_dialog_text.setText(R.string.please_fill_company_details);
            btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ib_settings.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorYellow));
                    ib_settings.setImageResource(R.drawable.settings__b);
                    fragment = new Setting_Fragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.static_page_linearlayout, fragment);
                    ft.commit();

                    ib_billing.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    ib_billing.setImageResource(R.drawable.billing__y);
                    ib_items.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    ib_items.setImageResource(R.drawable.items_y);
                    ib_reports.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
                    ib_reports.setImageResource(R.drawable.reports_y);
                    dialog.dismiss();
                }
            });
            dialog.show();
            return false;
        }
        cursor_01.close();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        //   Toast.makeText(getApplicationContext(), "clicked..", Toast.LENGTH_SHORT).show();
        if (fragment instanceof Billing_Fragment) {
            dialog_01 = new Dialog(MainActivity.this);
            dialog_01.setContentView(R.layout.exit_alert_dialog);
            btn_no_dialog_01 = dialog_01.findViewById(R.id.btn_no_dialog_01);
            btn_yes_dialog_01 = dialog_01.findViewById(R.id.btn_yes_dialog_01);

            dialog_01.setCancelable(false);
            dialog_01.show();
            Objects.requireNonNull(dialog_01.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            btn_no_dialog_01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_01.dismiss();
                }
            });
            btn_yes_dialog_01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Float total = Float.valueOf(Billing_Fragment.tv_total_value.getText().toString());
                    ContentValues cv_bill_update = new ContentValues();
                    cv_bill_update.put("NTOTAL", total);
                    MainActivity.db.update("BILLING", cv_bill_update, "BILLNO='" + Billing_Fragment.tv_bill_number.getText().toString() + "'", null);
                    DBExport();
                    dialog_01.dismiss();
                    finish();
                }
            });
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            ib_settings.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            ib_settings.setImageResource(R.drawable.settings__y);

            ib_billing.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            ib_billing.setImageResource(R.drawable.billing__y);

            ib_items.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            ib_items.setImageResource(R.drawable.items_y);

            ib_reports.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            ib_reports.setImageResource(R.drawable.reports_y);

            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(a);

            Utils.LongToast("Press again to exit!", MainActivity.this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
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
            //    Toast.makeText(getContext(), "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
