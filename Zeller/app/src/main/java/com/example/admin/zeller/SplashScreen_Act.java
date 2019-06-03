package com.example.admin.zeller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.admin.zeller.LoggingScreen.Login_Act;
import com.zellerr.R;

public class SplashScreen_Act extends Activity {

    private static String TAG = SplashScreen_Act.class.getName();
    static long SLEEP_TIME = 2;
    int login_status = 0, status;
   public static SQLiteDatabase db;
   public static int visible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        db = getApplicationContext().openOrCreateDatabase("Zeller.db", Context.MODE_PRIVATE, null);
      /*  db.execSQL("create table if not exists LOGINDETAILS(USERNAME varchar,PASSWORD varchar,REPEAT_PASSWORD varchar," +
                "LOGIN_DATE date,LOGIN_TIME time,STATUS int,IMEI varchar);");*/
        db.execSQL("create table if not exists COMPANY(CC_ID Integer primary key autoincrement,CC_NAME varchar,ADD_1 varchar," +
                "ADD_2 varchar,ADD_3 varchar,PLACE varchar,GSTNO varchar,PHONENO varchar)");
        db.execSQL("create table if not exists BILL_PREFIX(BP_ID Integer primary key autoincrement,PREFIX varchar(1),BILLNO int)");
        db.execSQL("create table if not exists BILL_MODE(BM_ID Integer primary key autoincrement,BM_NAME varchar)");
        db.execSQL("create table if not exists CATEGORY(C_ID Integer primary key autoincrement,C_NAME varchar,CREATED_ON Date)");
        db.execSQL("create table if not exists ITEM(I_ID Integer primary key autoincrement,I_NAME varchar,C_ID int,C_NAME varchar,CGST double,SGST double,UNITS varchar,FAVOURITES int,CREATED_ON Date)");
        db.execSQL("create table if not exists RATE(R_ID Integer primary key autoincrement,BM_ID int,BM_NAME varchar,I_ID int,I_NAME varchar,I_RATE double)");
        db.execSQL("create table if not exists SETTING(BDATE Date,GST int,F_MESSAGE_1 varchar,F_MESSAGE_2 varchar,LANGUAGE varchar,BLUETOOTH_NAME varchar,BLUETOOTH_MAC varchar)");

        db.execSQL("create table if not exists BILLING(IDS Integer primary key autoincrement,PREFIX varchar(1),BILLNO int," +
                "BDATE Date,BP_ID int,BM_ID int,I_ID int,I_NAME varchar,RATE double,CGST double,SGST double,QTY double," +
                "AMOUNT double,GTOTAL double,LESS double,NTOTAL double,CANCELLED int,BTIME Time,U_ID int,USERNAME varchar)");
        db.execSQL("create table if not exists USER(U_ID Integer primary key autoincrement,USERNAME varchar,PASSWORD varchar,ROLE varchar,BP_ID int," +
                "LOGIN_DATE date,LOGIN_TIME time,STATUS int,IMEI varchar)");

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen_);
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();

    }

    private class IntentLauncher extends Thread {
        public void run() {
            try {
                Thread.sleep(SLEEP_TIME * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String select = "Select USERNAME,ROLE FROM USER where STATUS='"+1+"' ";
            Cursor cursor = db.rawQuery(select, null);
            int count = cursor.getCount();
            String role = null;
            if(count>0) {
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(0);
                         role = cursor.getString(1);
                        Log.e("Status", String.valueOf(status));
                        Log.e("Cursro_value", String.valueOf(cursor));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Intent intent = new Intent(SplashScreen_Act.this, MainActivity.class);
                startActivity(intent);
                assert role != null;
                if(role.equalsIgnoreCase("Admin")){
                    visible=1;
                }
                else {
                    visible = 0;
                }
            }
            else {
                Intent intent = new Intent(SplashScreen_Act.this, Login_Act.class);
                startActivity(intent);
            }
           /* if (count == 0) {
                Intent intent = new Intent(SplashScreen_Act.this, Login_Act.class);
                startActivity(intent);
                visible =0;
            } else {
                visible = 1;
                Intent intent = new Intent(SplashScreen_Act.this, MainActivity.class);
                startActivity(intent);
            }*/
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(a);
        super.onBackPressed();
    }
}
