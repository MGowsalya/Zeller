package com.example.admin.zeller.LoggingScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Registration_Act extends Activity {
    EditText et_user_name_reg_act, et_pin_reg, et_repeat_pin_reg;
    Button btn_next;
    String str_user_name, str_pin, str_repeat_pin,str_bp_id;
   // SQLiteDatabase db;
    String str_date, str_time, str_imei;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    int status = 1;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      /*  db = getApplicationContext().openOrCreateDatabase("Zeller.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists LOGINDETAILS(USERNAME varchar,PASSWORD varchar,REPEAT_PASSWORD varchar," +
                "LOGIN_DATE date,LOGIN_TIME time,STATUS int,IMEI varchar);");*/
        et_user_name_reg_act = findViewById(R.id.et_user_name_reg_act);
        et_pin_reg = findViewById(R.id.et_pin_reg);
        et_repeat_pin_reg = findViewById(R.id.et_repeat_pin_reg);
        btn_next = findViewById(R.id.btn_next);


        if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            getDeviceImei();
        }



        /*Getting System date and time*/
        GettingDate_Time();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_user_name = et_user_name_reg_act.getText().toString();
                str_pin = et_pin_reg.getText().toString();
                str_repeat_pin = et_repeat_pin_reg.getText().toString();
                if (!ValidateUserName()) {
                    return;
                } else if (!ValidateUserPin()) {
                    return;
                } else if (!ValidateRepeatPin()) {
                    return;
                } else {
                    if (rowIDExistPinNumber(et_pin_reg.getText().toString())) {

                        ContentValues cv_prefix = new ContentValues();
                        String prefix = null;
                        cv_prefix.put("PREFIX",prefix);
                        cv_prefix.put("BILLNO",100);
                        SplashScreen_Act.db.insert("BILL_PREFIX",null,cv_prefix);

                        String select = "select BP_ID from BILL_PREFIX ";
                        Cursor cursor = SplashScreen_Act.db.rawQuery(select, null);
                        if (cursor.moveToFirst()) {
                            do {
                                str_bp_id = cursor.getString(0);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("USERNAME", et_user_name_reg_act.getText().toString());
                        contentValues.put("PASSWORD", et_pin_reg.getText().toString());
                      //  contentValues.put("REPEAT_PASSWORD ", et_repeat_pin_reg.getText().toString());
                        contentValues.put("LOGIN_DATE", str_date);
                        contentValues.put("LOGIN_TIME", str_time);
                        contentValues.put("STATUS", status);
                        contentValues.put("IMEI", str_imei);
                        contentValues.put("ROLE", "Admin");
                        contentValues.put("BP_ID", str_bp_id);
                  //      db.insert("LOGINDETAILS", null, contentValues);
                        SplashScreen_Act.db.insert("USER", null, contentValues);

                        DBEXPORT();
                           if (rowIdExistofShopDetails()) {
                            Intent intent = new Intent(Registration_Act.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Registration_Act.this, Registration_Act_02.class);
                            startActivity(intent);
                        }
                        //  Intent intent = new Intent(Registration_Act.this, Registration_Act_02.class);
//                        startActivity(intent);
                        et_user_name_reg_act.getText().clear();
                        et_pin_reg.getText().clear();
                        et_repeat_pin_reg.getText().clear();

                       } else {
                        Utils.ShortToast("This pin is already used ,please use another pin number", Registration_Act.this);
                    }
                }
                  /*  try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("username", et_user_name_reg_act.getText().toString());
                        jsonObject.put("password", et_repeat_pin_reg.getText().toString());
                        Log.e("JSON_OBJECT", jsonObject.toString());
                        APIInterface apiInterface = (APIInterface) Factory.getClient();
                        Call<Register_Response> call = apiInterface.REGISTER_RESPONSE_CALL("application/json", jsonObject.toString());
                        call.enqueue(new Callback<Register_Response>() {
                            @Override
                            public void onResponse(Call<Register_Response> call, Response<Register_Response> response) {
                                if (response.code() == 200) {
                                    Log.e("Completed", response.body().toString());
                                    Intent intent = new Intent(Registration_Act.this, Registration_Act_02.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Registration_Act.this, "Invalid Username and Password!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Register_Response> call, Throwable t) {
                                Toast.makeText(Registration_Act.this, "Server Problem", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });
    }

    public boolean rowIDExistPinNumber(String name) {
      //  String select = "select * from LOGINDETAILS ";//where PASSWORD='"+ et_login_pin.getText().toString() +"'";
        String select = "select * from USER ";
        Cursor cursor = SplashScreen_Act.db.rawQuery(select, null);

        List<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(2);
                Log.e("Login_Details", var);
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
        return allMatch;
    }

    public boolean rowIdExistofShopDetails() {
        String select = "select * from  COMPANY";
        Cursor cursor = SplashScreen_Act.db.rawQuery(select, null);
        return cursor.getCount() > 0;
    }

    private void DBEXPORT() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/" + "com.zeller" + "/databases/" + "Zeller.db";
        String backupDBPath = "Zeller_Demo.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            //   Toast.makeText(getContext(), "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceImei();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDeviceImei() {
        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        assert mTelephonyManager != null;
        @SuppressLint("HardwareIds") String deviceid = mTelephonyManager.getDeviceId();
        str_imei = mTelephonyManager.getImei();
       // Log.d("msg", "DeviceImei " + deviceid);
     //   Log.e("Imei ", str_imei);
    }

    @SuppressLint("SimpleDateFormat")
    private void GettingDate_Time() {
        str_date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        str_time = mdformat.format(calendar.getTime());

    }

    private boolean ValidateRepeatPin() {
        str_pin = et_pin_reg.getText().toString();
        str_repeat_pin = et_repeat_pin_reg.getText().toString();
        if (str_repeat_pin.isEmpty()) {
            Utils.ShortToast(getString(R.string.enter_user_pin_txt), Registration_Act.this);
            requestFocus(et_repeat_pin_reg);
            return false;
        } else if (!str_pin.equals(str_repeat_pin)) {
            Utils.ShortToast(getString(R.string.enter_user_pin_not_match), Registration_Act.this);
            requestFocus(et_repeat_pin_reg);
            return false;
        }
        return true;
    }

    private boolean ValidateUserPin() {
        str_pin = et_pin_reg.getText().toString();
        if (str_pin.isEmpty()) {
            Utils.ShortToast(getString(R.string.enter_user_pin_txt), Registration_Act.this);
            requestFocus(et_pin_reg);
            return false;
        } else if (str_pin.length() < 4) {
            Utils.ShortToast(getString(R.string.enter_valid_user_pin), Registration_Act.this);
            requestFocus(et_pin_reg);
            return false;
        }
        return true;
    }

    private boolean ValidateUserName() {
        str_user_name = et_user_name_reg_act.getText().toString();
        if (str_user_name.isEmpty()) {
            Utils.ShortToast(getString(R.string.enter_user_name_txt), Registration_Act.this);
            requestFocus(et_user_name_reg_act);
            return false;
        } else if (str_user_name.length() <= 1) {
            Utils.ShortToast(getString(R.string.enter_valid_user_name), Registration_Act.this);
            requestFocus(et_user_name_reg_act);
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
