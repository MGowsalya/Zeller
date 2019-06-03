package com.example.admin.zeller.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.zeller.Adapters.DeviceListAdapter;
import com.example.admin.zeller.LoggingScreen.Login_Act;
import com.example.admin.zeller.MainActivity;
import com.example.admin.zeller.Model.SalesModel;
import com.example.admin.zeller.SplashScreen_Act;
import com.example.admin.zeller.Utils;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.zellerr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static com.example.admin.zeller.SplashScreen_Act.db;

public class Setting_Fragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    public ConstraintLayout constraintLayout_billing_lay, constraintLayout_user_lay, constraintLayout_general_lay;
    public LinearLayout linear_btn_company, linear_btn_billing, linear_btn_users, linear_btn_general, linear_group_lay;
    public TextView tv_company, tv_billing, tv_users, tv_general;
    //    This attributes used for Company Layout
    public LinearLayout relative_company_lay;
    EditText et_company_name,
            et_address_one,
            et_address_two,
            et_address_three,
            et_pincode,
            et_gst_number,
            et_phone_number,
            et_bill_msg,
            et_bill_msg_02;
    Button btn_save;
    public static String str_male;
    ScrollView scrollView;
    String str_bp_id, bp_id, cur_str_bp_id;
    //This attributes used for Billing Layout
    EditText et_bill_mode_name;
    Button btn_bill_mode_save;
    ListView lv_bill_mode_details;
    ArrayAdapter<String> lv_bill_mode_adapter;
    ArrayList<String> arrayList_lv_bill_mode = new ArrayList<>();

    TextInputLayout signin_password_lay;
    //This attributes used for users layout
    EditText et_user_name;
    ShowHidePasswordEditText et_user_pin;
    Button btn_user_save;

    //This attributes for Role in user
    MaterialBetterSpinner spinner_users;
    ArrayAdapter<String> users_name_adapter;
    String[] users_name_arrayList;

    //This attributes for bill prefix in user
    MaterialBetterSpinner spinner_bill_prefix;
    ArrayAdapter<String> bill_prefix_adapter;
    String[] bill_prefix_arraylist;
    TextView tv_user_bill_prefix;
    ListView lv_user_role_list;
    View view_bill_prefix;

    //This attributes for General layout
    TextView tv_versoin_info, tv_valid_upto, tv_scan_fr_devices, tv_paired_devices, tv_available_user, tv_scanning_txt, tv_paired_user, tv_paired_devices_status;
    Spinner spin_language_selectoin;
    ArrayAdapter<String> language_selection_adapter;
    String[] languages_arraylist;
    TextView tv_languages_select, tv_logout;
    Button btn_backup, btn_restore;
    ConstraintLayout constraintLayout_general_lay_paired_listview, constraintLayout_general_lay_scan_listview;
    ListView lv_available_devices, lv_paired_devices;

    int REQUEST_CODE = 1;
    int REQUEST_FILE_CODE =42;
    String file_name ;
    Set<BluetoothDevice> paired_devices;
    String[] plist;

    private MyAdapter myAdapter;
    private ArrayList<SalesModel> salesModelArrayList = new ArrayList<>();
    SalesModel salesModel;
    ArrayList<String> name_list = new ArrayList<>();
    ArrayList<String> role_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    ArrayList<String> arrayList_lv_user_list_name = new ArrayList<>();

    String Str_BM_ID;
    String s1;
    String str_get_name;
    String str_get_roll;
    String cur_user_name;
    String cur_user_role;
    String cur_user_prefix;
    public boolean show;

    TextView tv_dialog_text;
    Button btn_dialog_ok;

    Switch aSwitch;
    Intent bluetoothIntent;
    int i = 1, language = 0, gst_value = 0;
    ArrayList<String> device_name_arrayList_01, device_address_arrayList;

    private static final String TAG = "MainActivity";
    BluetoothAdapter mBluetoothAdapter;
    Button btnEnableDisable_Discoverable;
    Button btnFindUnpairedDevices;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;
    BluetoothDevice device;
    BluetoothDevice mDevice;

    int status = 0;
    // file attachment to mail
    JSONArray jsonArray = new JSONArray();
    JSONObject object_i;
    File filelocation;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                //This is used for cursor at end of the line
                et_company_name.setSelection(et_company_name.getText().toString().length());
                et_address_one.setSelection(et_address_one.getText().toString().length());
                et_address_two.setSelection(et_address_two.getText().toString().length());
                et_address_three.setSelection(et_address_three.getText().toString().length());
                et_pincode.setSelection(et_pincode.getText().toString().length());
                et_gst_number.setSelection(et_gst_number.getText().toString().length());
                et_phone_number.setSelection(et_phone_number.getText().toString().length());
                et_bill_msg.setSelection(et_bill_msg.getText().toString().length());
                et_bill_msg_02.setSelection(et_bill_msg_02.getText().toString().length());

                SubmitCompanyDetail();
                break;
            case R.id.btn_bill_mode_save:
                String str_bill_name = et_bill_mode_name.getText().toString().trim();
                if ((s1 == null)) {
                    if (rowIdExistsBillMode(et_bill_mode_name.getText().toString().trim())) {
                        SubmitBillModeDetails();
                        GetSavedBillmodeValues();
                    } else {
                        Utils.ShortToast(Objects.requireNonNull(getActivity()).getString(R.string.bill_name_already_exists), getContext());
                        et_bill_mode_name.setText("");
                        GetSavedBillmodeValues();
                    }
                } else if (s1.equalsIgnoreCase(et_bill_mode_name.getText().toString())) {
                    Utils.ShortToast(Objects.requireNonNull(getActivity()).getString(R.string.bill_name_already_exists), getContext());
                    et_bill_mode_name.setText("");
                    GetSavedBillmodeValues();

                } else {
                    if (rowIdExists_BillMode_update(et_bill_mode_name.getText().toString().trim())) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("BM_NAME", et_bill_mode_name.getText().toString().trim());
                        db.update("BILL_MODE", contentValues, "BM_NAME='" + s1 + "'", null);
                        GetSavedBillmodeValues();
                        update_BM_NAME_Rate();
                        et_bill_mode_name.getText().clear();

                        s1 = null;
                    } else {
                        Utils.ShortToast(Objects.requireNonNull(getActivity()).getString(R.string.bill_name_already_exists), getContext());
                        et_bill_mode_name.setText("");
                        GetSavedBillmodeValues();
                    }
                }

                break;
            case R.id.btn_user_save:
                if (str_get_name == null) {
                    if (rowIdExists(et_user_name.getText().toString())) {
                        if (rowIdExists_password(et_user_pin.getText().toString())) {
                            arrayList_lv_user_list_name.clear();
                            SubmitUserDetails();
                            GetSavedUserRoleList();
                        } else {
                            Toast.makeText(getContext(), "Password Already Exists.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        GetSavedUserRoleList();
                        Utils.ShortToast(Objects.requireNonNull(getActivity()).getString(R.string.user_name_already_exists), getContext());
                        et_user_name.setText("");
                        et_user_pin.setText("");
                    }
                } else {
                    if (ValidateUserName()) {
                        return;
                    } else if (ValidateUserPin()) {
                        return;
                    } else {
                        if (rowIdExists_User_update(et_user_name.getText().toString())) {
                            if (rowIdExists_User_password_update(et_user_pin.getText().toString())) {
                                ContentValues cv_update = new ContentValues();
                                cv_update.put("USERNAME", et_user_name.getText().toString());
                                cv_update.put("PASSWORD", et_user_pin.getText().toString());
                                cv_update.put("ROLE", spinner_users.getText().toString());
                                Log.e("CCCC_updated_values", String.valueOf(cv_update));
                                db.update("USER", cv_update, "USERNAME='" + str_get_name + "'", null);
                                GetSavedUserRoleList();
                                Toast.makeText(getContext(), R.string.update_sucessfully_txt, Toast.LENGTH_SHORT).show();
                                get_BP_ID();
                                et_user_name.getText().clear();
                                et_user_pin.getText().clear();
                                str_get_name = null;
                            } else {
                                Toast.makeText(getContext(), "Password Already Exists..", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            GetSavedUserRoleList();
                            Utils.ShortToast(Objects.requireNonNull(getActivity()).getString(R.string.user_name_already_exists), getContext());
                            et_user_name.setText("");
                            et_user_pin.setText("");
                        }
                    }
                }
                break;

            case R.id.tv_company:
                SoftKeyboardVisibilty(show);
                tv_company_layout_bg();
                break;
            case R.id.tv_billing:
                SoftKeyboardVisibilty(show);
                GetBillingValues();
                break;
            case R.id.tv_users:
                SoftKeyboardVisibilty(show);
                constraintLayout_user_lay.setVisibility(View.VISIBLE);
                constraintLayout_billing_lay.setVisibility(View.GONE);
                relative_company_lay.setVisibility(View.GONE);
                constraintLayout_general_lay.setVisibility(View.GONE);
                linear_btn_users.setBackgroundResource(R.drawable.onclick_button_curve_bg);
                tv_users.setTextColor(Color.parseColor("#E0F010"));

                linear_btn_company.setBackgroundColor(Color.parseColor("#E0F010"));
                tv_company.setTextColor(Color.parseColor("#000000"));
                linear_btn_company.setBackgroundResource(R.drawable.button_curve_bg);

                linear_btn_billing.setBackgroundColor(Color.parseColor("#E0F010"));
                tv_billing.setTextColor(Color.parseColor("#000000"));
                linear_btn_billing.setBackgroundResource(R.drawable.button_curve_bg);

                linear_btn_general.setBackgroundColor(Color.parseColor("#E0F010"));
                tv_general.setTextColor(Color.parseColor("#000000"));
                linear_btn_general.setBackgroundResource(R.drawable.button_curve_bg);
                str_male = tv_users.getText().toString();
                break;
            case R.id.tv_generals:
                SoftKeyboardVisibilty(show);
                constraintLayout_general_lay.setVisibility(View.VISIBLE);
                constraintLayout_billing_lay.setVisibility(View.GONE);
                relative_company_lay.setVisibility(View.GONE);
                constraintLayout_user_lay.setVisibility(View.GONE);

                linear_btn_general.setBackgroundResource(R.drawable.onclick_button_curve_bg);
                tv_general.setTextColor(Color.parseColor("#E0F010"));

                linear_btn_company.setBackgroundColor(Color.parseColor("#E0F010"));
                tv_company.setTextColor(Color.parseColor("#000000"));
                linear_btn_company.setBackgroundResource(R.drawable.button_curve_bg);

                linear_btn_billing.setBackgroundColor(Color.parseColor("#E0F010"));
                tv_billing.setTextColor(Color.parseColor("#000000"));
                linear_btn_billing.setBackgroundResource(R.drawable.button_curve_bg);

                linear_btn_users.setBackgroundColor(Color.parseColor("#E0F010"));
                tv_users.setTextColor(Color.parseColor("#000000"));
                linear_btn_users.setBackgroundResource(R.drawable.button_curve_bg);
                str_male = tv_users.getText().toString();
                break;
            case R.id.tv_version_info:
                Utils.ShortToast("Ver_info_Inprogress", getContext());
                break;
            case R.id.tv_valid_upto:
                Utils.ShortToast("Valid_upto_Inprogress", getContext());
                break;
            case R.id.tv_paired_deivce:
                constraintLayout_general_lay_scan_listview.setVisibility(View.GONE);
                constraintLayout_general_lay_paired_listview.setVisibility(View.VISIBLE);
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(getContext(), R.string.no_device_found_txt, Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        aSwitch.setChecked(false);
                        startActivityForResult(i, REQUEST_CODE);
                    } else if (mBluetoothAdapter.isEnabled()) {
                        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        aSwitch.setChecked(true);
                        startActivityForResult(i, REQUEST_CODE);
                    }
                }
                break;
            case R.id.tv_logout:
                final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
                dialog.setContentView(R.layout.alert_dialog);
                (Objects.requireNonNull(dialog.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView msg_text = dialog.findViewById(R.id.message_text);
                Button Close_button = dialog.findViewById(R.id.button_close);
                Button Edit_button = dialog.findViewById(R.id.button_edit);
                msg_text.setText(R.string.logout_msg);
                Close_button.setText(R.string.no);
                Edit_button.setText(R.string.yes_txt);
                Close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String select = "select USERNAME from USER where STATUS='" + 1 + "'";
                        Cursor cursor = db.rawQuery(select, null);
                        String str_user_name = null;
                        if (cursor.moveToFirst()) {
                            do {
                                str_user_name = cursor.getString(0);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        ContentValues cv = new ContentValues();
                        cv.put("STATUS", 0);
                        db.update("USER", cv, "USERNAME='" + str_user_name + "'", null);
                        DBExport();
                        Log.e("ContentValues:logout", String.valueOf(cv));
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), Login_Act.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
                break;

            case R.id.btn_backup:
                String[] table_name = {"ITEM", "CATEGORY", "COMPANY", "BILL_MODE", "RATE", "BILL_PREFIX", "USER", "BILLING"};
                for (int tn = 0; tn <= table_name.length - 1; tn++) {
                    get_DB_values(table_name[tn]);
                }
                Log.e("final_jsonarray:", jsonArray.toString());

                String filename = "Zeller.txt";
                filelocation = new File(Environment.getExternalStorageDirectory(), filename);
                if (!filelocation.exists()) {
                    //  filelocation.mkdirs();
                    try {
                        filelocation.createNewFile();
                        attachMail();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (filelocation.exists()) {
                    filelocation.delete();
                    try {
                        filelocation.createNewFile();
                        attachMail();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_restore:

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("text/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_FILE_CODE);

                 /* if (delete_allDBValues()) {
                    String filename_01 = "Item.txt";
                    filelocation = new File(Environment.getExternalStorageDirectory(), filename_01);
                    if (!filelocation.exists()) {
                        Toast.makeText(getContext(), "file does not exists..", Toast.LENGTH_SHORT).show();
                    } else if (filelocation.exists()) {
                        //    Toast.makeText(getContext(), "file exists..", Toast.LENGTH_SHORT).show();
                        readFile();
                    }
                    DBExport();
                }*/
                break;
        }
    }

    private boolean delete_allDBValues() {
        String table_name[] = {"ITEM", "CATEGORY", "COMPANY", "BILL_MODE", "RATE", "BILL_PREFIX", "USER", "BILLING"};
        for (int tn = 0; tn <= table_name.length - 1; tn++) {
            db.execSQL("delete from '" + table_name[tn] + "'");
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + table_name[tn] + "'");
        }
        DBExport();
        return true;
    }

    private void get_DB_values(String T_name) {
        JSONArray jass = new JSONArray();
        String select = "SELECT * from '" + T_name + "'";
        Cursor cursor_select = db.rawQuery(select, null);
        cursor_select.moveToFirst();
        while (!cursor_select.isAfterLast()) {
            object_i = new JSONObject();
            int totalColumn = cursor_select.getColumnCount();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor_select.getColumnName(i) != null) {
                    try {
                        if (cursor_select.getString(i) != null) {
                            object_i.put(cursor_select.getColumnName(i), cursor_select.getString(i));
                        } else {
                            object_i.put(cursor_select.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.e("TAG_NAME", e.getMessage());
                    }
                }
            }
            jass.put(object_i);
            cursor_select.moveToNext();
        }
        JSONObject jobj_item = new JSONObject();
        try {
            jobj_item.put(T_name, jass);
          //  Log.e("j_item", jobj_item.toString());
            jsonArray.put(jobj_item);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        cursor_select.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readFile() {
        try {
            Log.e("file_name",file_name);
            String filename_01 = "Item.txt";
            File yourFile = new File(Environment.getExternalStorageDirectory(),filename_01);
            FileInputStream stream = new FileInputStream(yourFile);
            String jsonStr;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                jsonStr = Charset.defaultCharset().decode(bb).toString();
                Log.e("JSONArray_jsonstr", jsonStr);
                JSONArray jsonArray = new JSONArray(jsonStr);

                String arr_table_names[] = {"ITEM", "CATEGORY", "COMPANY", "BILL_MODE", "RATE", "BILL_PREFIX", "USER", "BILLING"};
             //   if(et_company_name.getText().toString().equalsIgnoreCase("xxx")){
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    JSONArray ja = jo.getJSONArray(arr_table_names[i]);
                    for (int j = 0; j <= ja.length() - 1; j++) {
                        JSONObject jo_1 = ja.getJSONObject(j);
                        Iterator<String> keys = jo_1.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String keyValue = (String) keys.next();
                            String valueString = jo_1.getString(keyValue);
                            contentValues.put(keyValue, valueString);
                        }
                        db.insert(arr_table_names[i], null, contentValues);
                    }
                }

              /*  }
                else {
                    Toast.makeText(getContext(), "not re-stored..", Toast.LENGTH_SHORT).show();
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void attachMail() {
        try {

            FileWriter writer = new FileWriter(filelocation);
            writer.write(jsonArray.toString());
            writer.append("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        //   String to[] = {"gowsalyamouleesh@gmail.com"};
            //emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
         //   emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Testing Attachment");
            startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), 12345);
                    /*Intent chooserIntent = Intent.createChooser(emailIntent, "title");
                    Toast.makeText(getApplicationContext(), chooserIntent.toString(), Toast.LENGTH_SHORT).show();
                       startActivity(emailIntent);*/
    }
    public void GetBillingValues() {
        try {
            constraintLayout_billing_lay.setVisibility(View.VISIBLE);
            relative_company_lay.setVisibility(View.GONE);
            constraintLayout_user_lay.setVisibility(View.GONE);
            constraintLayout_general_lay.setVisibility(View.GONE);
            linear_btn_billing.setBackgroundResource(R.drawable.onclick_button_curve_bg);
            tv_billing.setTextColor(Color.parseColor("#E0F010"));

            linear_btn_company.setBackgroundColor(Color.parseColor("#E0F010"));
            tv_company.setTextColor(Color.parseColor("#000000"));
            linear_btn_company.setBackgroundResource(R.drawable.button_curve_bg);

            linear_btn_users.setBackgroundColor(Color.parseColor("#E0F010"));
            tv_users.setTextColor(Color.parseColor("#000000"));
            linear_btn_users.setBackgroundResource(R.drawable.button_curve_bg);

            linear_btn_general.setBackgroundColor(Color.parseColor("#E0F010"));
            tv_general.setTextColor(Color.parseColor("#000000"));
            linear_btn_general.setBackgroundResource(R.drawable.button_curve_bg);

            str_male = tv_billing.getText().toString();

        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private void update_BM_NAME_Rate() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("BM_NAME", et_bill_mode_name.getText().toString().trim());
        db.update("RATE", contentValues, "BM_NAME='" + s1 + "'", null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void SoftKeyboardVisibilty(boolean show) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        if (show) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } else {
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
        }
    }

    private void tv_company_layout_bg() {
        relative_company_lay.setVisibility(View.VISIBLE);
        constraintLayout_user_lay.setVisibility(View.GONE);
        constraintLayout_billing_lay.setVisibility(View.GONE);
        constraintLayout_general_lay.setVisibility(View.GONE);

        linear_btn_company.setBackgroundResource(R.drawable.onclick_button_curve_bg);
        tv_company.setTextColor(Color.parseColor("#E0F010"));

        linear_btn_billing.setBackgroundColor(Color.parseColor("#E0F010"));
        tv_billing.setTextColor(Color.parseColor("#000000"));
        linear_btn_billing.setBackgroundResource(R.drawable.button_curve_bg);


        linear_btn_users.setBackgroundColor(Color.parseColor("#E0F010"));
        tv_users.setTextColor(Color.parseColor("#000000"));
        linear_btn_users.setBackgroundResource(R.drawable.button_curve_bg);

        linear_btn_general.setBackgroundColor(Color.parseColor("#E0F010"));
        tv_general.setTextColor(Color.parseColor("#000000"));
        linear_btn_general.setBackgroundResource(R.drawable.button_curve_bg);
        str_male = tv_company.getText().toString();
    }

    private void get_BP_ID() {

        String select = "Select BP_ID from USER where USERNAME='" + str_get_name + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                bp_id = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (spinner_users.getText().toString().equalsIgnoreCase("Cashier")) {
            ContentValues cv_update_billprefix = new ContentValues();
            cv_update_billprefix.put("PREFIX", spinner_bill_prefix.getText().toString());
            db.update("BILL_PREFIX", cv_update_billprefix, "BP_ID='" + bp_id + "'", null);
            spinner_users.getText().clear();
            spinner_bill_prefix.getText().clear();
        } else if (spinner_users.getText().toString().equalsIgnoreCase("SalesPerson")) {
            ContentValues cv_update_billprefix = new ContentValues();
            cv_update_billprefix.put("PREFIX", spinner_bill_prefix.getText().toString());
            db.update("BILL_PREFIX", cv_update_billprefix, "BP_ID='" + bp_id + "'", null);
            spinner_users.getText().clear();
            spinner_bill_prefix.getText().clear();
        } else {
            String prefix_null = null;
            ContentValues cv_update_billprefix = new ContentValues();
            cv_update_billprefix.put("PREFIX", prefix_null);
            db.update("BILL_PREFIX", cv_update_billprefix, "BP_ID='" + bp_id + "'", null);
            spinner_users.getText().clear();
        }
    }

    private boolean rowIdExists(String name) {
        String select = "select USERNAME from USER";
        Cursor cursor = db.rawQuery(select, null);
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
        return allMatch;
    }
    private boolean rowIdExists_password(String name) {
        String select = "select PASSWORD from USER";
        Cursor cursor = db.rawQuery(select, null);
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
        return allMatch;
    }
    private boolean rowIdExistsBillMode(String name) {
        String select = "select BM_NAME from BILL_MODE";
        Cursor cursor = db.rawQuery(select, null);
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
        return allMatch;
    }
    private boolean rowIdExists_BillMode_update(String trim) {
        String select = "select BM_NAME from BILL_MODE Where BM_NAME != '" + s1 + "'";
        Cursor cursor = db.rawQuery(select, null);
        ArrayList<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(et_bill_mode_name.getText().toString().trim())) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }
    private boolean rowIdExists_User_update(String name) {
        String select = "select USERNAME from USER Where USERNAME != '" + str_get_name + "'";
        Cursor cursor = db.rawQuery(select, null);
        ArrayList<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(et_user_name.getText().toString().trim())) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }
    private boolean rowIdExists_User_password_update(String name) {
        String select = "select PASSWORD from USER Where  USERNAME != '" + str_get_name + "'";
        Cursor cursor = db.rawQuery(select, null);
        ArrayList<String> labels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(et_user_pin.getText().toString().trim())) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }
    private void GetSavedUserRoleList() {
        name_list.clear();
        role_list.clear();
        salesModelArrayList.clear();
        String select = "SELECT USERNAME,ROLE FROM USER";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                cur_user_name = cursor.getString(0);
                cur_user_role = cursor.getString(1);
                name_list.add(cur_user_name);
                role_list.add(cur_user_role);
            } while (cursor.moveToNext());
        }
        cursor.close();
        for (int i = 0; i < name_list.size(); i++) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("name", name_list.get(i));
            hm.put("role", role_list.get(i));
            arrayList.add(hm);
            salesModel = new SalesModel();
            salesModel.setName(name_list.get(i));
            salesModel.setRole(role_list.get(i));
            salesModelArrayList.add(0, salesModel);
            myAdapter.notifyDataSetChanged();
            lv_user_role_list.setSelection(0);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void SubmitUserDetails() {
        if (ValidateUserName()) {
            return;
        }
        else if (ValidateUserPin()) {
            return;
        } else {
            ContentValues contentValues = new ContentValues();
            String user_position = spinner_users.getText().toString();

            GetSavedBillmodeValues();
            int bill_num = 100;
            if (user_position.equalsIgnoreCase("Cashier")) {
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("PREFIX", spinner_bill_prefix.getText().toString());
                contentValues1.put("BILLNO", bill_num);
                if (spinner_bill_prefix.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please Enter Prefix", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.insert("BILL_PREFIX", null, contentValues1);
                    insert_user();
                    et_user_name.getText().clear();
                    et_user_pin.getText().clear();
                    spinner_users.getText().clear();
                    spinner_bill_prefix.getText().clear();
                    et_user_name.requestFocus();
                }
            }
            else if (user_position.equalsIgnoreCase("SalesPerson")) {
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("PREFIX", spinner_bill_prefix.getText().toString());
                contentValues1.put("BILLNO", bill_num);
                db.insert("BILL_PREFIX", null, contentValues1);
                insert_user();
                et_user_name.getText().clear();
                et_user_pin.getText().clear();
                spinner_users.getText().clear();
                spinner_bill_prefix.getText().clear();
                et_user_name.requestFocus();
            }
            else if (user_position.equalsIgnoreCase("Admin")) {
                String prefix_null = null;
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("PREFIX", prefix_null);
                contentValues1.put("BILLNO", bill_num);
                db.insert("BILL_PREFIX", null, contentValues1);
                insert_user();
                et_user_name.getText().clear();
                et_user_pin.getText().clear();
                et_user_name.requestFocus();
                spinner_users.getText().clear();
            }
            et_user_pin.setText("");
            et_user_name.setText("");
        }
    }
    private void insert_user() {
        String usr_name = et_user_name.getText().toString();
        String usr_pin = et_user_pin.getText().toString();
        String user_position = spinner_users.getText().toString();
        String select = "select BP_ID from BILL_PREFIX ";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                str_bp_id = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERNAME", usr_name);
        contentValues.put("PASSWORD", usr_pin);
        contentValues.put("ROLE", user_position);
        contentValues.put("BP_ID", str_bp_id);
        contentValues.put("STATUS", 0);
        db.insert("USER", null, contentValues);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean ValidateUserPin() {
        String user_pin = et_user_pin.getText().toString();
        if (user_pin.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_user_pin), getContext());
            requstFocus(et_user_pin);
            return true;
        } else if (user_pin.length() < 4) {
            Utils.ShortToast(getString(R.string.pls_enter_valid_user_pin), getContext());
            requstFocus(et_user_pin);
            return true;
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean ValidateUserName() {
        String user_name = et_user_name.getText().toString();
        if (user_name.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_user_name), getContext());
            requstFocus(et_user_name);
            return true;
        } else if (user_name.length() < 2) {
            Utils.ShortToast(getString(R.string.pls_enter_valid_user_name), getContext());
            requstFocus(et_user_name);
            return true;
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void SubmitBillModeDetails() {
        if (!ValidateBillModeName()) {
            return;
        } else {
            ContentValues contentValues = new ContentValues();
            String bill_mode_name = et_bill_mode_name.getText().toString().trim();
            contentValues.put("BM_NAME", bill_mode_name);
            Log.e("Content_values", String.valueOf(contentValues));
            arrayList_lv_bill_mode.clear();
            String select = "SELECT BM_NAME from BILL_MODE";
            Cursor cursor = MainActivity.db.rawQuery(select, null);
            int count = cursor.getCount();
            if (count <= 2) {
                db.insert("BILL_MODE", null, contentValues);
                lv_bill_mode_adapter.notifyDataSetChanged();
                et_bill_mode_name.setText("");
            } else {
                final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
                dialog.setContentView(R.layout.main_act_dialog_window);
                (Objects.requireNonNull(dialog.getWindow())).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tv_dialog_text = dialog.findViewById(R.id.tv_error_message);
                btn_dialog_ok = dialog.findViewById(R.id.bt_dialog_ok);
                tv_dialog_text.setText(R.string.more_than_3_values_not_accepted);
                btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                lv_bill_mode_adapter.notifyDataSetChanged();
                et_bill_mode_name.setText("");
            }
            cursor.close();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean ValidateBillModeName() {
        String bill_mode_name = et_bill_mode_name.getText().toString();
        if (bill_mode_name.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_bill_mode_name), getContext());
            requstFocus(et_bill_mode_name);
            return false;
        } else if (bill_mode_name.length() < 2) {
            Utils.ShortToast(getString(R.string.pls_enter_valid_bill_mode_name), getContext());
            requstFocus(et_bill_mode_name);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void SubmitCompanyDetail() {
        //This is used for cursor at end of the line
        if (!ValidateComapnyName()) {
            return;
        } else if (!ValidateAddress_One()) {
            return;
        } else if (!ValidateAddress_Three()) {
            return;
        } else if (!ValidatePlacce()) {
            return;
        }  else if (!ValidatePhoneNum()) {
            return;
        }
        else {
            //This is used for cursor at end of the line
            ContentValues contentValues = new ContentValues();
            String company_name, address_one, address_two, address_three, place, gstnum, phonenum, bill_msg_01, bill_msg_02;
            company_name = et_company_name.getText().toString();
            address_one = et_address_one.getText().toString();
            address_two = et_address_two.getText().toString();
            address_three = et_address_three.getText().toString();
            place = et_pincode.getText().toString();
            gstnum = et_gst_number.getText().toString();
            phonenum = et_phone_number.getText().toString();
            bill_msg_01 = et_bill_msg.getText().toString();
            bill_msg_02 = et_bill_msg_02.getText().toString();
            if (rowidExists_CompanyId()) {
                contentValues.put("CC_NAME", company_name);
                contentValues.put("ADD_1", address_one);
                contentValues.put("ADD_2", address_two);
                contentValues.put("ADD_3", address_three);
                contentValues.put("PLACE", place);
                contentValues.put("GSTNO", gstnum);
                contentValues.put("PHONENO", phonenum);
                MainActivity.db.update("COMPANY", contentValues, null, null);
                Utils.LongToast(getString(R.string.company_profile_updated_msg), getContext());
                DBExport();
                et_company_name.setSelection(et_company_name.getText().toString().length());
                et_address_one.setSelection(et_address_one.getText().toString().length());
                et_address_two.setSelection(et_address_two.getText().toString().length());
                et_address_three.setSelection(et_address_three.getText().toString().length());
                et_pincode.setSelection(et_pincode.getText().toString().length());
                et_gst_number.setSelection(et_gst_number.getText().toString().length());
                et_phone_number.setSelection(et_phone_number.getText().toString().length());
                et_bill_msg.setSelection(et_bill_msg.getText().toString().length());
                et_bill_msg_02.setSelection(et_bill_msg_02.getText().toString().length());
            } else {
                contentValues.put("CC_NAME", company_name);
                contentValues.put("ADD_1", address_one);
                contentValues.put("ADD_2", address_two);
                contentValues.put("ADD_3", address_three);
                contentValues.put("PLACE", place);
                contentValues.put("GSTNO", gstnum);
                contentValues.put("PHONENO", phonenum);
                db.insert("COMPANY", null, contentValues);

                DBExport();
                Utils.LongToast(getString(R.string.company_profile_created_msg), getContext());
                et_company_name.setSelection(et_company_name.getText().toString().length());
                et_address_one.setSelection(et_address_one.getText().toString().length());
                et_address_two.setSelection(et_address_two.getText().toString().length());
                et_address_three.setSelection(et_address_three.getText().toString().length());
                et_pincode.setSelection(et_pincode.getText().toString().length());
                et_gst_number.setSelection(et_gst_number.getText().toString().length());
                et_phone_number.setSelection(et_phone_number.getText().toString().length());
                et_bill_msg.setSelection(et_bill_msg.getText().toString().length());
                et_bill_msg_02.setSelection(et_bill_msg_02.getText().toString().length());
            }
            if (rowIdExistsBillMSG()) {
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("F_MESSAGE_1", bill_msg_01);
                contentValues1.put("F_MESSAGE_2", bill_msg_02);
                contentValues1.put("BDATE", getDate());
                if (et_gst_number.getText().toString().isEmpty()) {
                    Log.e("empty_gst_value", et_gst_number.getText().toString());
                    gst_value = 0;
                } else if (!(et_gst_number.getText().toString().isEmpty())) {
                    Log.e("Not_empty_gst_value", et_gst_number.getText().toString());
                    gst_value = 1;
                }
                contentValues1.put("GST", gstnum);
                MainActivity.db.update("SETTING", contentValues1, null, null);
                DBExport();
            } else {
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("F_MESSAGE_1", bill_msg_01);
                contentValues1.put("F_MESSAGE_2", bill_msg_02);
                contentValues1.put("BDATE", getDate());
                contentValues.put("LANGUAGE", language);
                if (et_gst_number.getText().toString().isEmpty()) {
                    Log.e("empty_gst_value", et_gst_number.getText().toString());
                    gst_value = 0;
                } else if (!(et_gst_number.getText().toString().isEmpty())) {
                    Log.e("Not_empty_gst_value", et_gst_number.getText().toString());
                    gst_value = 1;
                }
                contentValues1.put("GST", gstnum);
                db.insert("SETTING", null, contentValues1);
                DBExport();
            }
        }

    }

    private boolean rowIdExistsBillMSG() {
        String select = "select *from SETTING";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(select, null);
        return cursor.getCount() > 0;
    }

    private boolean rowidExists_CompanyId() {
        String select = "select CC_ID from COMPANY";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(select, null);
        return cursor.getCount() > 0;
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        Date date1 = new Date();
        return dateFormat.format(date1);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean ValidateAddress_One() {
        String address_one = et_address_one.getText().toString().trim();
        if (address_one.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_address), getContext());
            requstFocus(et_address_one);
            return false;
        } else if (address_one.length() <= 2) {
            Utils.ShortToast(getString(R.string.pls_enter_valid_address), getContext());
            requstFocus(et_address_one);
            return false;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean ValidateAddress_Three() {

        String address_three = et_address_three.getText().toString().trim();
        if (address_three.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_city), getContext());
            requstFocus(et_address_three);
            return false;
        } else if (address_three.length() <= 2) {
            Utils.ShortToast(getString(R.string.pls_enter_valid_city_details), getContext());
            requstFocus(et_address_three);
            return false;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean ValidatePlacce() {
        String address = et_pincode.getText().toString().trim();
        if (address.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_pin_number), getContext());
            requstFocus(et_pincode);
            return false;
        } else if (address.length() <= 5) {
            Utils.ShortToast(getString(R.string.pls_enter_Valid_pin_number), getContext());
            requstFocus(et_pincode);
            return false;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean ValidatePhoneNum() {
        String str_phone_num = et_phone_number.getText().toString();
        if (str_phone_num.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_phone_num), getContext());
            requstFocus(et_phone_number);
            return false;
        } else if (str_phone_num.length() <= 9) {
            Utils.ShortToast(getString(R.string.pls_enter_valid_phone_num), getContext());
            requstFocus(et_phone_number);
            return false;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void requstFocus(View view) {
        if (view.requestFocus()) {
            Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean ValidateComapnyName() {
        String name = et_company_name.getText().toString().trim();
        if (name.isEmpty()) {
            Utils.ShortToast(getString(R.string.pls_enter_company_name), getContext());
            requstFocus(et_company_name);
            return false;
        } else if (name.length() < 3) {
            Utils.ShortToast(getString(R.string.pls_enter_valid_company_name), getContext());
            requstFocus(et_company_name);
            return false;
        }
        return true;
    }
    private void focusOnView_01() {
        scrollView.smoothScrollTo(100, et_phone_number.getTop());
        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                et_bill_msg_02.requestFocus();
                            }
                        }
        );
    }
    private void focusOnView() {
        scrollView.smoothScrollTo(100, et_address_three.getTop());
        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                et_phone_number.requestFocus();
                            }
                        }
        );
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Spinner spinner = (Spinner) adapterView;
        if (spinner.getId() == R.id.spinner_users) {
            String item = adapterView.getItemAtPosition(i).toString();
            spinner_users.setText(item);
            switch (spinner_users.getText().toString()) {
                case "Cashier":
                    view_bill_prefix.setVisibility(View.VISIBLE);
                    spinner_bill_prefix.setVisibility(View.VISIBLE);
                    tv_user_bill_prefix.setVisibility(View.VISIBLE);
                    spinner_users.setVisibility(View.VISIBLE);
                    spinner_bill_prefix.setVisibility(View.VISIBLE);
                    break;
                case "SalesPerson":
                    view_bill_prefix.setVisibility(View.VISIBLE);
                    tv_user_bill_prefix.setVisibility(View.VISIBLE);
                    spinner_bill_prefix.setVisibility(View.VISIBLE);
                    spinner_users.setVisibility(View.VISIBLE);
                    spinner_bill_prefix.setVisibility(View.VISIBLE);
                    break;
                default:
                    view_bill_prefix.setVisibility(View.GONE);
                    tv_user_bill_prefix.setVisibility(View.GONE);
                    spinner_bill_prefix.setVisibility(View.GONE);
                    spinner_users.setVisibility(View.GONE);
                    spinner_bill_prefix.setVisibility(View.GONE);
                    break;
            }
        }
        if (spinner.getId() == R.id.spinner_bill_prefix) {
            cur_user_prefix = adapterView.getItemAtPosition(i).toString();
            Log.e("Bill_Spinner_02_value", cur_user_prefix);
            spinner_bill_prefix.setText(cur_user_prefix);}
        if (spinner.getId() == R.id.spinner_lang_selecct) {
            String s1 = adapterView.getItemAtPosition(i).toString();
            tv_languages_select.setText(s1);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private MyAdapter(Context context) {
            this.context = context;
        }
        @Override
        public int getCount() {
            return salesModelArrayList.size();
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return (long) position;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = ((LayoutInflater) Objects.requireNonNull(this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.user_name_and_role_list_lay, null);
                viewHolder.User_Name = convertView.findViewById(R.id.tv_user_name);
                viewHolder.User_Role = convertView.findViewById(R.id.tv_user_role);
                viewHolder.user_role_lay = convertView.findViewById(R.id.user_role_lay);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final SalesModel salesModel = salesModelArrayList.get(position);
            viewHolder.User_Name.setText(salesModel.getName());
            viewHolder.User_Role.setText(salesModel.getRole());
            viewHolder.user_role_lay.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {
                    str_get_name = salesModel.getName();
                    str_get_name = salesModel.getName();
                    str_get_roll = salesModel.getRole();
                    switch (str_get_roll) {
                        case "Cashier":
                            view_bill_prefix.setVisibility(View.GONE);
                            spinner_bill_prefix.setVisibility(View.VISIBLE);
                            tv_user_bill_prefix.setVisibility(View.VISIBLE);

                            spinner_users.setText(R.string.cashier_txt);
                            et_user_name.setFocusable(true);

                            users_name_arrayList = getResources().getStringArray(R.array.users_array_details);
                            users_name_adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.drop_down_txt, users_name_arrayList);
                            spinner_users.setAdapter(users_name_adapter);

                            get_Prefix();
                            bill_prefix_arraylist = getResources().getStringArray(R.array.users_bill_prefix_array);
                            bill_prefix_adapter = new ArrayAdapter<>(getActivity(), R.layout.drop_down_txt, bill_prefix_arraylist);
                            spinner_bill_prefix.setAdapter(bill_prefix_adapter);
                            break;
                        case "SalesPerson":
                            view_bill_prefix.setVisibility(View.GONE);
                            spinner_bill_prefix.setVisibility(View.VISIBLE);
                            tv_user_bill_prefix.setVisibility(View.VISIBLE);
                            spinner_users.setText(R.string.salesperson_txt);
                            et_user_name.setFocusable(true);

                            users_name_arrayList = getResources().getStringArray(R.array.users_array_details);
                            users_name_adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.drop_down_txt, users_name_arrayList);
                            spinner_users.setAdapter(users_name_adapter);

                            get_Prefix();
                            bill_prefix_arraylist = getResources().getStringArray(R.array.prefix_salesperson);
                            bill_prefix_adapter = new ArrayAdapter<>(getActivity(), R.layout.drop_down_txt, bill_prefix_arraylist);
                            spinner_bill_prefix.setAdapter(bill_prefix_adapter);
                            break;
                        default:
                            view_bill_prefix.setVisibility(View.GONE);
                            spinner_bill_prefix.setVisibility(View.GONE);
                            tv_user_bill_prefix.setVisibility(View.GONE);
                            spinner_users.setText(R.string.admin_txt);
                            et_user_name.setFocusable(true);

                            users_name_arrayList = getResources().getStringArray(R.array.users_array_details);
                            users_name_adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.drop_down_txt, users_name_arrayList);
                            spinner_users.setAdapter(users_name_adapter);
                            get_Prefix();
                            break;
                    }
                    et_user_name.setText(str_get_name);
                    et_user_name.setSelection(et_user_name.getText().toString().length());
                }
            });
            return convertView;
        }

        private void get_Prefix() {
            String select = "SELECT PASSWORD,BP_ID FROM USER WHERE USERNAME='" + str_get_name + "'";
            Cursor cursor = db.rawQuery(select, null);
            if (cursor.moveToFirst()) {
                do {
                    String bill_pwd = cursor.getString(0);
                    cur_str_bp_id = cursor.getString(1);
                    et_user_pin.setText(bill_pwd);
                    et_user_pin.setSelection(et_user_pin.getText().toString().length());
                } while (cursor.moveToNext());
            }
            cursor.close();
            String select_1 = "SELECT PREFIX FROM BILL_PREFIX WHERE BP_ID='" + cur_str_bp_id + "'";
            Cursor cursor_1 = db.rawQuery(select_1, null);
            if (cursor_1.moveToFirst()) {
                do {
                    String prefix = cursor_1.getString(0);
                    spinner_bill_prefix.setText(prefix);
                } while (cursor_1.moveToNext());
            }
            cursor_1.close();
        }
    }

    static class ViewHolder {
        TextView User_Name;
        TextView User_Role;
        public LinearLayout user_role_lay;
        ViewHolder() {
        }
    }
    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };
    /**
     * +
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }
            }
        }
    };
    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            assert action != null;
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                LinkedHashSet<BluetoothDevice> device_address_set = new LinkedHashSet<>(mBTDevices);
                Log.e("Avaliable_Address_List", device_address_set.toString());
                mBTDevices.clear();
                mBTDevices.addAll(device_address_set);
                try {
                    if (!(device.getName().isEmpty())) {
                        tv_scanning_txt.setVisibility(View.GONE);
                    } else {
                        tv_scanning_txt.setVisibility(View.VISIBLE);
                        tv_scanning_txt.setText(R.string.no_device_found_txt);
                    }
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(getContext(), R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };
    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            assert action != null;
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                    Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show();
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called.");

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(mBroadcastReceiver1);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(mBroadcastReceiver2);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(mBroadcastReceiver3);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(mBroadcastReceiver4);
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"CutPasteId", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_setting__fragment, null, false);
        // Inflate the layout for this fragment
        //  Button btnONOFF = view.findViewById(R.id.btnONOFF);

       /* View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/
        //   getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        btnEnableDisable_Discoverable = view.findViewById(R.id.btnDiscoverable_on_off);
        btnFindUnpairedDevices = view.findViewById(R.id.btnFindUnpairedDevices);
        lvNewDevices = view.findViewById(R.id.lv_available_devices);
        mBTDevices = new ArrayList<>();
        tv_scan_fr_devices = view.findViewById(R.id.tv_scan_fr_devices);

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(mBroadcastReceiver4, filter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lvNewDevices.setOnItemClickListener(this);

        tv_scan_fr_devices.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                btnDiscover(v);
            }
        });
        device_name_arrayList_01 = new ArrayList<>();
        device_address_arrayList = new ArrayList<>();
        linear_btn_company = view.findViewById(R.id.linear_btn_company);
        linear_btn_billing = view.findViewById(R.id.linear_btn_billing);
        linear_btn_users = view.findViewById(R.id.linear_btn_users);
        linear_btn_general = view.findViewById(R.id.linear_btn_general);
        linear_group_lay = view.findViewById(R.id.linear_btn_general);
        relative_company_lay = view.findViewById(R.id.relative_company_lay);
        constraintLayout_billing_lay = view.findViewById(R.id.constraintLayout_billing_lay);
        constraintLayout_user_lay = view.findViewById(R.id.constraintLayout_user_lay);
        constraintLayout_general_lay = view.findViewById(R.id.constraintLayout_general_lay);

        //Company Page attributes
        et_company_name = view.findViewById(R.id.et_company_name);
        et_address_one = view.findViewById(R.id.et_address_one);
        et_address_two = view.findViewById(R.id.et_address_two);
        et_address_three = view.findViewById(R.id.et_address_three);
        et_pincode = view.findViewById(R.id.et_pincode);
        et_phone_number = view.findViewById(R.id.et_phone_number);
        et_gst_number = view.findViewById(R.id.et_gst_number);
        et_bill_msg = view.findViewById(R.id.et_bill_msg);
        et_bill_msg_02 = view.findViewById(R.id.et_bill_msg_02);

        //Layout Buttons
        tv_company = view.findViewById(R.id.tv_company);
        tv_billing = view.findViewById(R.id.tv_billing);
        tv_users = view.findViewById(R.id.tv_users);
        tv_general = view.findViewById(R.id.tv_generals);
        btn_save = view.findViewById(R.id.btn_save);
        scrollView = view.findViewById(R.id.scrollView);

        //Billing Page Attributes
        et_bill_mode_name = view.findViewById(R.id.et_bill_mode_name);
        btn_bill_mode_save = view.findViewById(R.id.btn_bill_mode_save);
        lv_bill_mode_details = view.findViewById(R.id.lv_bill_mode_details);
        lv_bill_mode_adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()).getApplicationContext(), R.layout.bill_mode_name_lay, R.id.tv_bill_mode_name, arrayList_lv_bill_mode);
        lv_bill_mode_details.setAdapter(lv_bill_mode_adapter);
        lv_bill_mode_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                s1 = adapterView.getItemAtPosition(i).toString();
                lv_bill_mode_details.setSelector(R.color.light_yellow);
                String str1 = lv_bill_mode_details.getItemAtPosition(i).toString();
                Log.e("Selected_bill_mode_name", str1);
                et_bill_mode_name.setText(s1);
                et_bill_mode_name.setSelection(et_bill_mode_name.getText().toString().length());
            }
        });

        //Users Page Attributes
        et_user_name = view.findViewById(R.id.et_user_name);
        et_user_pin = view.findViewById(R.id.et_user_pin);
        lv_user_role_list = view.findViewById(R.id.lv_user_role_list);
        myAdapter = new MyAdapter(getActivity());
        lv_user_role_list.setAdapter(myAdapter);
        signin_password_lay = view.findViewById(R.id.signin_password_lay);
        lv_user_role_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = lv_user_role_list.getItemAtPosition(i).toString();
                Log.e("Selected_items", str);
            }
        });

        et_user_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    spinner_users.showDropDown();
                    spinner_users.showContextMenu();
                    spinner_users.setFocusable(true);
                    users_name_arrayList = getResources().getStringArray(R.array.users_array_details);
                    users_name_adapter = new ArrayAdapter<>(getActivity(), R.layout.drop_down_txt, users_name_arrayList);
                    spinner_users.setAdapter(users_name_adapter);
                    spinner_users.requestFocus();
                }
                return true;
            }
        });
        //This is for users Role in user page
        spinner_users = view.findViewById(R.id.spinner_users);
        users_name_arrayList = getResources().getStringArray(R.array.users_array_details);
        users_name_adapter = new ArrayAdapter<>(getActivity(), R.layout.drop_down_txt, users_name_arrayList);
        spinner_users.setAdapter(users_name_adapter);

        tv_user_bill_prefix = view.findViewById(R.id.tv_user_bill_prefix);
        spinner_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //     Toast.makeText(getContext(), "clicsdf" + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                String user = adapterView.getItemAtPosition(i).toString();
                users_name_arrayList = getResources().getStringArray(R.array.users_array_details);
                users_name_adapter = new ArrayAdapter<>(getActivity(), R.layout.drop_down_txt, users_name_arrayList);
                spinner_users.setAdapter(users_name_adapter);

                if (user.equalsIgnoreCase("Admin"))
                {
                    spinner_bill_prefix.setVisibility(View.GONE);
                    tv_user_bill_prefix.setVisibility(View.GONE);
                }
                else if (user.equalsIgnoreCase("Cashier"))
                {
                    spinner_bill_prefix.setVisibility(View.VISIBLE);
                    tv_user_bill_prefix.setVisibility(View.VISIBLE);
                    bill_prefix_arraylist = getResources().getStringArray(R.array.users_bill_prefix_array);
                    bill_prefix_adapter = new ArrayAdapter<>(getActivity(), R.layout.drop_down_txt, bill_prefix_arraylist);
                    spinner_bill_prefix.setAdapter(bill_prefix_adapter);
                }
                else if (user.equalsIgnoreCase("SalesPerson")) {
                    spinner_bill_prefix.setVisibility(View.VISIBLE);
                    tv_user_bill_prefix.setVisibility(View.VISIBLE);
                    bill_prefix_arraylist = getResources().getStringArray(R.array.prefix_salesperson);
                    bill_prefix_adapter = new ArrayAdapter<>(getActivity(), R.layout.drop_down_txt, bill_prefix_arraylist);
                    spinner_bill_prefix.setAdapter(bill_prefix_adapter);
                }
            }
        });
        users_name_arrayList = getResources().getStringArray(R.array.users_array_details);
        users_name_adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.drop_down_txt, users_name_arrayList);
        spinner_users.setAdapter(users_name_adapter);

        ///This is for Bill prefix used in user page
        spinner_bill_prefix = view.findViewById(R.id.spinner_bill_prefix);
        view_bill_prefix = view.findViewById(R.id.view_bill_prefix);
        btn_user_save = view.findViewById(R.id.btn_user_save);
        //This is for General Layout page
        tv_logout = view.findViewById(R.id.tv_logout);
        btn_backup = view.findViewById(R.id.btn_backup);
        btn_restore = view.findViewById(R.id.btn_restore);
        tv_versoin_info = view.findViewById(R.id.tv_version_info);
        tv_valid_upto = view.findViewById(R.id.tv_valid_upto);
        tv_paired_devices = view.findViewById(R.id.tv_paired_deivce);
        spin_language_selectoin = view.findViewById(R.id.spinner_lang_selecct);
        tv_languages_select = view.findViewById(R.id.tv_languages_select);
        lv_paired_devices = view.findViewById(R.id.lv_paired_devices);

        constraintLayout_general_lay_paired_listview = view.findViewById(R.id.constraintLayout_general_lay_paired_listview);
        constraintLayout_general_lay_scan_listview = view.findViewById(R.id.constraintLayout_general_lay_scan_listview);
        tv_available_user = view.findViewById(R.id.tv_available_user);
        tv_scanning_txt = view.findViewById(R.id.tv_scanning_txt);
        tv_paired_user = view.findViewById(R.id.tv_paired_user);
        lv_available_devices = view.findViewById(R.id.lv_available_devices);
        tv_paired_devices_status = view.findViewById(R.id.tv_paired_devices_status);

        spin_language_selectoin.setOnItemSelectedListener(this);
        languages_arraylist = getResources().getStringArray(R.array.languages_selection);
        language_selection_adapter = new ArrayAdapter<>(getActivity(), R.layout.drop_down_language_selection, languages_arraylist);
        spin_language_selectoin.setAdapter(language_selection_adapter);
        aSwitch = view.findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    BluetoothEnable();
                } else {
                    BluetoothDisable();
                }
            }
        });

        if (mBluetoothAdapter.isEnabled()) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }
        //Turn on Bluetooth
        if (mBluetoothAdapter == null)
            Toast.makeText(getActivity(), R.string.device_not_support_blu_txt, Toast.LENGTH_LONG).show();
        else if (!mBluetoothAdapter.isEnabled()) {
            Intent BtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(BtIntent, 0);
            aSwitch.isChecked();
        }
//        if (mBluetoothAdapter.isDiscovering()) {
//            mBluetoothAdapter.cancelDiscovery();
//            Log.d(TAG, "btnDiscover: Canceling discovery.");
//
//            //check BT permissions in manifest
//            checkBTPermissions();
//
//            mBluetoothAdapter.startDiscovery();
//            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            Objects.requireNonNull(getActivity()).registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
//        }
//
        //  String uery = "SELECT STATUS FROM LOGINDETAILS";
        String uery = "SELECT STATUS FROM USER";
        Cursor cu = db.rawQuery(uery, null);
        if (cu.moveToFirst()) {
            do {

                status = cu.getInt(0);
            } while (cu.moveToNext());
            cu.close();
        }
        tv_company_layout_bg();
        tv_company.setOnClickListener(this);
        tv_users.setOnClickListener(this);
        tv_billing.setOnClickListener(this);
        tv_general.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_bill_mode_save.setOnClickListener(this);
        btn_user_save.setOnClickListener(this);

        tv_versoin_info.setOnClickListener(this);
        tv_valid_upto.setOnClickListener(this);
        tv_paired_devices.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        btn_backup.setOnClickListener(this);
        btn_restore.setOnClickListener(this);
        GetSavedCompanyValues();
        GetSavedBillmodeValues();
        GetSavedUserRoleList();
        et_address_three.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                focusOnView();
                return false;
            }
        });
        et_phone_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                focusOnView_01();
                return false;
            }
        });
        return view;
    }
    private void GetSavedBillmodeValues() {
        arrayList_lv_bill_mode.clear();
        String select = "SELECT * FROM BILL_MODE";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                Str_BM_ID = cursor.getString(0);
                String cur_bill_mode_name = cursor.getString(1);
                arrayList_lv_bill_mode.add(cur_bill_mode_name);
                lv_bill_mode_adapter.notifyDataSetChanged();
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
    private void GetSavedCompanyValues() {
        String select = "SELECT * FROM COMPANY";
        Cursor cursor = db.rawQuery(select, null);
        String select1 = "select * from SETTING";
        Cursor cursor1 = db.rawQuery(select1, null);
        if (cursor1.moveToFirst()) {
            do {
                String cur_bill_msg_01 = cursor1.getString(2);
                String cur_bill_msg_02 = cursor1.getString(3);
                et_bill_msg.setText(cur_bill_msg_01);
                et_bill_msg_02.setText(cur_bill_msg_02);

                et_company_name.setSelection(et_company_name.getText().toString().length());
                et_address_one.setSelection(et_address_one.getText().toString().length());
                et_address_two.setSelection(et_address_two.getText().toString().length());
                et_address_three.setSelection(et_address_three.getText().toString().length());
                et_pincode.setSelection(et_pincode.getText().toString().length());
                et_gst_number.setSelection(et_gst_number.getText().toString().length());
                et_phone_number.setSelection(et_phone_number.getText().toString().length());
                et_bill_msg.setSelection(et_bill_msg.getText().toString().length());
                et_bill_msg_02.setSelection(et_bill_msg_02.getText().toString().length());

            } while (cursor1.moveToNext());
        }
        cursor1.close();
        if (cursor.moveToFirst()) {
            do {
                String cur_company_name = cursor.getString(1);
                String cur_address_01 = cursor.getString(2);
                String cur_address_02 = cursor.getString(3);
                String cur_address_03 = cursor.getString(4);
                String cur_place = cursor.getString(5);
                String cur_gst_numm = cursor.getString(6);
                String cur_phone_num = cursor.getString(7);
                et_company_name.setText(cur_company_name);
                et_address_one.setText(cur_address_01);
                et_address_two.setText(cur_address_02);
                et_address_three.setText(cur_address_03);
                et_pincode.setText(cur_place);
                et_gst_number.setText(cur_gst_numm);
                et_phone_number.setText(cur_phone_num);

                et_company_name.setSelection(et_company_name.getText().toString().length());
                et_address_one.setSelection(et_address_one.getText().toString().length());
                et_address_two.setSelection(et_address_two.getText().toString().length());
                et_address_three.setSelection(et_address_three.getText().toString().length());
                et_pincode.setSelection(et_pincode.getText().toString().length());
                et_gst_number.setSelection(et_gst_number.getText().toString().length());
                et_phone_number.setSelection(et_phone_number.getText().toString().length());
                et_bill_msg.setSelection(et_bill_msg.getText().toString().length());
                et_bill_msg_02.setSelection(et_bill_msg_02.getText().toString().length());

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    private void BluetoothDisable() {
        mBluetoothAdapter.disable();
    }

    private void BluetoothEnable() {
        bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(bluetoothIntent, i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnDiscover(View view) {
        Log.e("Scan_clicked", "True");
        constraintLayout_general_lay_scan_listview.setVisibility(View.VISIBLE);
        constraintLayout_general_lay_paired_listview.setVisibility(View.GONE);
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
        mBluetoothAdapter.startDiscovery();tv_scanning_txt.setVisibility(View.VISIBLE);
        tv_scanning_txt.setText(R.string.scanning_txt);

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");
            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            Objects.requireNonNull(getActivity()).registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if (mBluetoothAdapter == null) {
            Toast.makeText(getContext(), R.string.no_device_found_txt, Toast.LENGTH_SHORT).show();
        }
        if (!mBluetoothAdapter.isDiscovering()) {
            //check BT permissions in manifest
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            Objects.requireNonNull(getActivity()).registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }
    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     * <p>
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        int permissionCheck = Objects.requireNonNull(getActivity()).checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += Objects.requireNonNull(getActivity()).checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.
        mBluetoothAdapter.cancelDiscovery();
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();
        Log.e("Clicked_Device_Name", "onItemClick: deviceName = " + deviceName);
        Log.e("Clicked_Device_Address", "onItemClick: deviceAddress = " + deviceAddress);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
        Log.e("Addresssss", String.valueOf(device));
        Bundle bundle = new Bundle();
        bundle.putString("DeviceAddress", deviceAddress);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        Log.e("Pairing_Device_Name", "Trying to pair with Device:" + deviceName);
        Log.e("Pairing_Device_Address", "Trying to pair with Device Address:" + deviceAddress);

        ContentValues contentValues = new ContentValues();
        contentValues.put("BLUETOOTH_NAME", deviceName);
        contentValues.put("BLUETOOTH_MAC", deviceAddress);
        contentValues.put("LANGUAGE", language);
        SplashScreen_Act.db.update("SETTING", contentValues, null, null);
        DBExport();
        mBTDevices.get(i).createBond();
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            lvNewDevices.getChildAt(i).setBackgroundColor(Color.BLUE);
            Toast.makeText(getContext(), R.string.already_paired_txt, Toast.LENGTH_SHORT).show();
        } else if (!(device.getBondState() == BluetoothDevice.BOND_BONDED)) {
            Toast.makeText(getContext(), R.string.pairing_txt, Toast.LENGTH_LONG).show();
        }
        String select = "select BLUETOOTH_NAME,BLUETOOTH_MAC from SETTING";
        Cursor cursor = db.rawQuery(select, null);
        String str_user_name;
        if (cursor.moveToFirst()) {
            do {
                str_user_name = cursor.getString(0);
                Log.e("bbbbbbbb", str_user_name);
            } while (cursor.moveToNext());
        }
        cursor.close();
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(mBroadcastReceiver1);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(mBroadcastReceiver2);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(mBroadcastReceiver3);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(mBroadcastReceiver4);
        super.onStop();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_FILE_CODE && resultCode == RESULT_OK){
            if(data != null){
                Uri uri = data.getData();
                file_name = uri.getPath();
                file_name = file_name.substring(file_name.indexOf(":")+1);
                Log.e("intent_file_name",file_name);
                if (delete_allDBValues()) {
                    filelocation = new File(Environment.getExternalStorageDirectory(), file_name);
                    if (!filelocation.exists()) {
                        Toast.makeText(getContext(), "file does not exists..", Toast.LENGTH_SHORT).show();
                    } else if (filelocation.exists()) {
                        //    Toast.makeText(getContext(), "file exists..", Toast.LENGTH_SHORT).show();
                        readFile();
                    }
                    DBExport();
                }
            }
        }
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                paired_devices = mBluetoothAdapter.getBondedDevices();
                int count = paired_devices.size();
                plist = new String[count];
                int j = 0;
                for (BluetoothDevice device : paired_devices) {
                    plist[j] = device.getName();
                    j++;
                }
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(plist));
                Bluetooth_Paired_Adapter bluetoothAdapter = new Bluetooth_Paired_Adapter(Objects.requireNonNull(getActivity()), arrayList);
                lv_paired_devices.setAdapter(bluetoothAdapter);
                Log.e("paired_devices:", Arrays.toString(plist));
            } else {
                tv_paired_devices_status.setVisibility(View.VISIBLE);
                tv_paired_devices_status.setText(R.string.no_paired_devices);
            }
        }
    }

    class Bluetooth_Paired_Adapter extends ArrayAdapter {
        private ArrayList<String> paired_device_list;
        private Context mContext;

        Bluetooth_Paired_Adapter(@NonNull Context context, ArrayList<String> paired_devices) {
            super(context, R.layout.bluetooth_connecction_status_lay);
            this.paired_device_list = paired_devices;
            this.mContext = context;
        }
        @Override
        public int getCount() {
            return paired_device_list.size();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @SuppressLint("ResourceAsColor")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder_01 viewHolder_01 = new ViewHolder_01();
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = Objects.requireNonNull(mInflater).inflate(R.layout.bluetooth_connecction_status_lay, parent, false);
                viewHolder_01.tv_paired_devices_name = convertView.findViewById(R.id.tv_paired_device_name);
                viewHolder_01.btn_delete = convertView.findViewById(R.id.btn_delete);
                convertView.setTag(viewHolder_01);
            } else {
                viewHolder_01 = (ViewHolder_01) convertView.getTag();
            }
            viewHolder_01.tv_paired_devices_name.setText(paired_device_list.get(position));
            viewHolder_01.btn_delete.setText(R.string.delete);
            viewHolder_01.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, R.string.removing_txt, Toast.LENGTH_SHORT).show();
                    for (BluetoothDevice device : mBTDevices) {
                        try {
                            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                                Toast.makeText(getContext(), R.string.removing_txt, Toast.LENGTH_SHORT).show();
                                unpairDevice(device);
                            }
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }
                }
            });
            return convertView;
        }
    }
    private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e("" + "", e.getMessage());
        }
    }
    private class ViewHolder_01 {
        TextView tv_paired_devices_name;
        Button btn_delete;
    }
}
