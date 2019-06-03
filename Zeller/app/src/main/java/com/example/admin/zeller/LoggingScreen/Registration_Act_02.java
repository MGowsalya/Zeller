package com.example.admin.zeller.LoggingScreen;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.admin.zeller.MainActivity;
import com.example.admin.zeller.SplashScreen_Act;
import com.example.admin.zeller.Utils;
import com.zellerr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Registration_Act_02 extends Activity {
    EditText et_shop_name, et_line_01, et_line_02,
            et_city, et_pincode, et_gst, et_phone_number_reg_02;

    Button btn_save_reg_act;

    String str_shop_name, str_line_01, str_line_02,

    str_city, str_pincode, str_gst, str_phone_number;
 //   SQLiteDatabase db;
    int gst_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__act_02);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       /* db = getApplicationContext().openOrCreateDatabase("Zeller.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists COMPANY(CC_ID Integer primary key autoincrement,CC_NAME varchar,ADD_1 varchar," +
                "ADD_2 varchar,ADD_3 varchar,PLACE varchar,GSTNO varchar,PHONENO varchar)");*/
        et_shop_name = findViewById(R.id.et_shop_name);
        et_line_01 = findViewById(R.id.et_line_01);
        et_line_02 = findViewById(R.id.et_line_02);
        et_city = findViewById(R.id.et_city);
        et_pincode = findViewById(R.id.et_pincode);
        et_gst = findViewById(R.id.et_gst);
        et_phone_number_reg_02 = findViewById(R.id.et_phone_number_reg_02);
        btn_save_reg_act = findViewById(R.id.btn_save_reg_act);
        btn_save_reg_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidateShopName()) {
                    return;

                } else if (!Validateline_01()) {
                    return;
                }
                /*else if (!Validateline_02()) {
                    return;
                }*/
                else if (!Validatecity()) {
                    return;
                } else if (!ValidatePincode()) {
                    return;
                } else if (!ValidatePhonenum()) {
                    return;
                } else {
                    ContentValues contentValues = new ContentValues();
                    str_shop_name = et_shop_name.getText().toString();
                    str_line_01 = et_line_01.getText().toString();
                    str_line_02 = et_line_02.getText().toString();
                    str_city = et_city.getText().toString();
                    str_pincode = et_pincode.getText().toString();
                    str_gst = et_gst.getText().toString();
                    str_phone_number = et_phone_number_reg_02.getText().toString();
                    if (rowidExists_CompanyId()) {
                       /* contentValues.put("CC_NAME", str_shop_name);
                        contentValues.put("ADD_1", str_line_01);
                        contentValues.put("ADD_2", str_line_02);
                        contentValues.put("ADD_3", str_city);
                        contentValues.put("PLACE", str_pincode);
                        contentValues.put("GSTNO", str_gst);
                        contentValues.put("PHONENO", str_phone_number);
                        Log.e("Content_values", String.valueOf(contentValues));
                        SplashScreen_Act.db.update("COMPANY", contentValues, null, null);


                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("BDATE", getDate());
                        contentValues1.put("GST", str_gst);
                        SplashScreen_Act.db.update("SETTING", contentValues1, null, null);*/
                        Utils.LongToast(getString(R.string.company_profile_updated_msg), Registration_Act_02.this);
                    } else {
                        contentValues.put("CC_NAME", str_shop_name);
                        contentValues.put("ADD_1", str_line_01);
                        contentValues.put("ADD_2", str_line_02);
                        contentValues.put("ADD_3", str_city);
                        contentValues.put("PLACE", str_pincode);
                        contentValues.put("GSTNO", str_gst);
                        contentValues.put("PHONENO", str_phone_number);
                        Log.e("Content_values", String.valueOf(contentValues));
                        SplashScreen_Act.db.insert("COMPANY", null, contentValues);
                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("BDATE", getDate());
                        contentValues1.put("GST", str_gst);
                        SplashScreen_Act.db.insert("SETTING", null, contentValues1);
                        DBExport();
                        Utils.LongToast(getString(R.string.company_profile_created_msg), Registration_Act_02.this);
                    }
                    Intent intent = new Intent(Registration_Act_02.this, MainActivity.class);
                    startActivity(intent);
                    et_shop_name.getText().clear();
                    et_line_01.getText().clear();
                    et_line_02.getText().clear();
                    et_city.getText().clear();
                    et_pincode.getText().clear();
                    et_gst.getText().clear();
                    et_phone_number_reg_02.getText().clear();
                }
            }
        });
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

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    private boolean rowidExists_CompanyId() {
        String select = "select CC_ID from COMPANY";
        Cursor cursor = SplashScreen_Act.db.rawQuery(select, null);
        return cursor.getCount() > 0;
    }

    private boolean ValidatePhonenum() {
        str_phone_number = et_phone_number_reg_02.getText().toString();
        if (str_phone_number.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_phone_num), Registration_Act_02.this);
            requestFocus(et_phone_number_reg_02);
            return false;
        } else if (str_phone_number.length() <= 9) {
            Utils.ShortToast(getString(R.string.pls_enter_valid_phone_num), Registration_Act_02.this);
            requestFocus(et_phone_number_reg_02);
            return false;
        }
        return true;
    }

    private boolean Validategst() {
        str_gst = et_gst.getText().toString();
        if (str_gst.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_gst_number), Registration_Act_02.this);
            requestFocus(et_gst);
            return false;
        }
        return true;
    }

    private boolean ValidatePincode() {
        str_pincode = et_pincode.getText().toString();
        if (str_pincode.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_pincode), Registration_Act_02.this);
            requestFocus(et_pincode);
            return false;
        } else if (str_pincode.length() <= 5) {
            Utils.ShortToast(getString(R.string.pls_enter_valid_pincode), Registration_Act_02.this);
            requestFocus(et_pincode);
            return false;
        }
        return true;
    }

    private boolean Validatecity() {
        str_city = et_city.getText().toString();
        if (str_city.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_city), Registration_Act_02.this);
            requestFocus(et_city);
            return false;
        }
        return true;
    }

    private boolean Validateline_02() {
        str_line_02 = et_line_02.getText().toString();
        if (str_line_02.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_address_02), Registration_Act_02.this);
            requestFocus(et_line_02);
            return false;
        }
        return true;
    }

    private boolean Validateline_01() {
        str_line_01 = et_line_01.getText().toString();
        if (str_line_01.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_address), Registration_Act_02.this);
            requestFocus(et_line_01);
            return false;
        }
        return true;
    }

    private boolean ValidateShopName() {
        str_shop_name = et_shop_name.getText().toString();
        if (str_shop_name.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_shop_name), Registration_Act_02.this);
            requestFocus(et_shop_name);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }
}
