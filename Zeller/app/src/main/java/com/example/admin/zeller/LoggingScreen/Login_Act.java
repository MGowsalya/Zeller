package com.example.admin.zeller.LoggingScreen;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.zeller.Fragments.Item_Wise_Detail_List_Fragment;
import com.example.admin.zeller.MainActivity;
import com.example.admin.zeller.SplashScreen_Act;
import com.example.admin.zeller.Utils;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.zellerr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Login_Act extends Activity implements View.OnClickListener {
    ShowHidePasswordEditText et_login_pin;
    TextView tv_forgot_pin, tv_sign_up_now, tv_not_a_member_txt;
    Button btn_login;
    public static int visible ;
  //  SQLiteDatabase db;
    String str_pin;
    int status = 1;
    int PERMISSION_ALL = 1;
    @SuppressLint("InlinedApi")
    String[] permission = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,};


    public static final int MULITPLE_PERMISSIONS = 10;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        if (!hasPermissions(this, permission)) {
            ActivityCompat.requestPermissions(this, permission, PERMISSION_ALL);
        }
      /*  db = Objects.requireNonNull(getApplicationContext().openOrCreateDatabase("Zeller.db", Context.MODE_PRIVATE, null));
        db.execSQL("create table if not exists LOGINDETAILS(USERNAME varchar,PASSWORD varchar,REPEAT_PASSWORD varchar," +
                "LOGIN_DATE date,LOGIN_TIME time,STATUS int,IMEI varchar);");*/

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        et_login_pin = findViewById(R.id.et_login_pin);
        tv_forgot_pin = findViewById(R.id.tv_forgot_pin);
        tv_not_a_member_txt = findViewById(R.id.textView49);
        tv_sign_up_now = findViewById(R.id.tv_sign_up_now);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_sign_up_now.setOnClickListener(this);
        tv_forgot_pin.setOnClickListener(this);
        if(User_count()){
            tv_not_a_member_txt.setVisibility(View.GONE);
            tv_sign_up_now.setVisibility(View.GONE);
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                str_pin = et_login_pin.getText().toString();
                if (str_pin.isEmpty()) {
                    Utils.ShortToast("Please Enter Pin", Login_Act.this);
                    requestFocus(et_login_pin);
                } else if (!rowIDExistPinNumber(et_login_pin.getText().toString())) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("STATUS", status);
                    //   db.update("LOGINDETAILS", contentValues, null, null);
                 //   SplashScreen_Act.db.update("USER", contentValues, null, null);
                    SplashScreen_Act.db.update("USER", contentValues, "PASSWORD='"+et_login_pin.getText().toString()+"'", null);
                    DBExport();
                    Intent intent = new Intent(Login_Act.this, MainActivity.class);
                    startActivity(intent);

                    String select = "SELECT USERNAME,PASSWORD,ROLE,STATUS from USER where PASSWORD='"+et_login_pin.getText().toString()+"'";
                    Cursor cursor = SplashScreen_Act.db.rawQuery(select,null);
                    String role = null;
                    if(cursor.moveToFirst()){
                        do{
                            String name = cursor.getString(0);
                            String pswd = cursor.getString(1);
                            role = cursor.getString(2);
                            String status = cursor.getString(3);
                            Log.e("name",name);
                            Log.e("pswd",pswd);
                            Log.e("role",role);
                            Log.e("status",status);

                        }while (cursor.moveToNext());
                    }
                    cursor.close();
                    assert role != null;
                    if(role.equalsIgnoreCase("Admin")){
                        visible =1;
                    }
                    else {
                        visible =0;
                    }
                } else {
                    Utils.ShortToast("Please Enter Valid Password", Login_Act.this);
                }
                break;
            case R.id.tv_sign_up_now:
                Intent intent = new Intent(Login_Act.this, Registration_Act.class);
                startActivity(intent);
                break;
            case R.id.tv_forgot_pin:
                Intent intent1 = new Intent(Login_Act.this, ForgotPin_Act.class);
                startActivity(intent1);
                break;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean rowIDExistPinNumber(String name) {
        // String select = "select * from LOGINDETAILS ";//where PASSWORD='"+ et_login_pin.getText().toString() +"'";
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

    private boolean User_count(){
        String select = "SELECT * from USER";
        Cursor cursor = SplashScreen_Act.db.rawQuery(select,null);
        return cursor.getCount()>0;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
