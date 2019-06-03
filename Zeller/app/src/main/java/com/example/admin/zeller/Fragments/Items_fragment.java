package com.example.admin.zeller.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.zeller.Adapters.GridViewAdapter;
import com.example.admin.zeller.GridItemView;
import com.example.admin.zeller.MainActivity;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.zellerr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Items_fragment extends Fragment {
    String selectedItem;
    ListView category_listview, product_listview;
    EditText et_category_name;
    TextView tv_created_date;
    ArrayAdapter<String> product_adapter;
    ArrayList<String> category_name_list = new ArrayList<>();
    ArrayList<String> product_name_list = new ArrayList<>();
    ImageButton ib_category_add, ib_product_add;
    Dialog dialog;
    Dialog dialog_window;
    String c_name;
    Button b_edit_items;
    TextView tv_unit_spinner_txt, category_name;
    ImageView iv_favourites, iv_fav_dialog_window;

    //For Product full details layout attributes
    TextView tv_wholesale_price_table, tv_retail_price_table,
            tv_resell_price_table, tv_wholesale_price_dialog,
            tv_resell_price_dialog, tv_retail_price_dialog;
    TextView tv_date, tv_sold_item_count, tv_cgst_percentage, tv_sgst_percentage,
            tv_wholsale_billing_price, tv_retail_billing_price,
            tv_resell_billing_price, tv_gst_price;
    LinearLayout linear_category_edit_layout, product_full_details_layout;
    ImageButton imgbtn_product_edit;
    TextView tv_product_name;
    // SimpleRatingBar ratingBar_fav, ratingBar_fav_dialog_window;

    //For Dialog windows attributes
    EditText et_wholesale_dialog_lay, et_retail_dialog_lay, et_resell_dialog_lay,
            et_cgst_percentage_dialog_lay, et_sgst_percentage_dialog_lay;
    LinearLayout dialog_control_button_01_lay, dialog_control_button_02_lay,
            linear_basic_details_lay;
    ConstraintLayout constraint_price_and_tax_lay;
    TextView tv_basic_details, tv_price_and_tax, tv_number_of_products;
    TextView tv_cgst_dialog_lay, tv_sgst_dialog_lay;
    ImageButton imgbtn_cross_dialog_lay, imgbtn_yes_dialog_lay, ib_dialog_1_yes,
            ib_dialog_1_no, ib_c_name_edit;

    //Basic Details Layout
    EditText et_product_name_edittext_dialog_lay;
    MaterialBetterSpinner spinner_units_dialog_lay;
    MaterialBetterSpinner spinner_category_dialog_lay;

    ArrayAdapter<String> spin_cate_adapter;
    ArrayList<String> c_name_list = new ArrayList<>();
    String str_item_name;
    String str_c_id;
    ArrayAdapter<String> unit_spin_adapter;
    String[] unit_spin_arraylist;

    int number_of_products, favourites_value = 0;
    EditText et_update_cate_name;
    String cursor1_i_id, cursor1_i_name;
    TextView tv_wholesale, tv_retail, tv_resell;
    TextView tv_final_wholesale, tv_final_retail, tv_final_resell;

    //Dialog Textview
    TextView tv_wholesale_table, tv_retail_table, tv_resell_table,
            tv_wholesale_price, tv_retail_price, tv_resell_price;
    String[] item_rate;
    ArrayList<String> bm_id_list = new ArrayList<>();

    int Selected_Position;
    private String I_ID;
    GridView gridView;

    TextView tv_selected_product;
    private ArrayList<String> selectedStrings;

    TextView tv_no_products_for;
    TextView tv_c_name_selected;
    TextView tv_custom_toast;
    String toast_message;

    ConstraintLayout constraintLayout_table_layout, constraintLayout_wholesale,
            constraintLayout_retail, constraintLayout_resell;
    View view4_bottom_wholesale_lay, view4_bottom_retail_lay;
    //    This attributes for dialog_window layout
    ConstraintLayout constraintLayout_table_layout_in_dialog, constraintLayout_wholesale_dialog_lay,
            constraintLayout_retail_dialog_lay, constraintLayout_resell_dialog_lay;
    View view4_bottom_wholesale_lay_diallog, view4_bottom_retail_lay_dialog_lay;

    class ItemsAdapter extends ArrayAdapter {
        private ArrayList<String> category_name;
        private ArrayList<String> category_count;
        private Context mContext;
        ItemsAdapter(@NonNull Context context, ArrayList<String> c_name, ArrayList<String> c_count) {
            super(context, R.layout.listview_design);
            this.category_name = c_name;
            this.category_count = c_count;
            this.mContext = context;
        }
        @Override
        public int getCount() {
            return category_name.size();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @SuppressLint("ResourceAsColor")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = Objects.requireNonNull(mInflater).inflate(R.layout.listview_design, parent, false);
                viewHolder.tv_c_name = convertView.findViewById(R.id.c_name_textview);
                viewHolder.tv_c_count = convertView.findViewById(R.id.number_of_products_textView);
                viewHolder.list_item_layout = convertView.findViewById(R.id.list_item_lay);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_c_name.setText(category_name.get(position));
            viewHolder.tv_c_count.setText(String.valueOf(category_count.get(position)));
            final ViewHolder finalViewHolder = viewHolder;
            final ViewHolder finalViewHolder1 = viewHolder;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_c_name_selected.setText(category_name.get(position));
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            tv_c_name_selected.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                        }
                    }, 1000);

                    tv_c_name_selected.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                    c_name = finalViewHolder.tv_c_name.getText().toString();
                    linear_category_edit_layout.setVisibility(View.VISIBLE);
                    product_full_details_layout.setVisibility(View.GONE);
                    get_category_details();
                    create_product_list();
                    if (finalViewHolder1.tv_c_count.getText().toString().equals("0 Products")) {
                        product_listview.setVisibility(View.GONE);
                        tv_no_products_for.setVisibility(View.VISIBLE);
                    } else {
                        product_listview.setVisibility(View.VISIBLE);
                        tv_no_products_for.setVisibility(View.GONE);
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_c_name, tv_c_count;
            LinearLayout list_item_layout;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.items, null, false);

        int currentApiVersion = Build.VERSION.SDK_INT;

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
        category_listview = view.findViewById(R.id.category_listview);
        product_listview = view.findViewById(R.id.product_listview);
        ib_category_add = view.findViewById(R.id.category_add_image_button);
        ib_product_add = view.findViewById(R.id.product_add_image_button);
        ib_c_name_edit = view.findViewById(R.id.ib_c_name_edit);
        category_name = view.findViewById(R.id.tv_category_name_editpage);
        tv_created_date = view.findViewById(R.id.date_editText);
        b_edit_items = view.findViewById(R.id.edit_items_button);
        iv_favourites = view.findViewById(R.id.ib_items_fav);
        tv_no_products_for = view.findViewById(R.id.tv_no_products_for);

        selectedStrings = new ArrayList<>();
        category_name.setPaintFlags(category_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //For Product full details layout attributes
        tv_wholesale_price_table = view.findViewById(R.id.tv_wholesale_price_table);
        tv_retail_price_table = view.findViewById(R.id.tv_retail_price_table);
        tv_resell_price_table = view.findViewById(R.id.tv_resell_price_table);
        tv_cgst_percentage = view.findViewById(R.id.tv_cgst_percentage);
        tv_sgst_percentage = view.findViewById(R.id.tv_sgst_percentage);

        tv_wholesale_price_table.setPaintFlags(tv_wholesale_price_table.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_retail_price_table.setPaintFlags(tv_retail_price_table.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_resell_price_table.setPaintFlags(tv_resell_price_table.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_cgst_percentage.setPaintFlags(tv_cgst_percentage.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_sgst_percentage.setPaintFlags(tv_sgst_percentage.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv_product_name = view.findViewById(R.id.tv_product_name);
        tv_product_name.setPaintFlags(tv_product_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv_date = view.findViewById(R.id.tv_date);
        tv_sold_item_count = view.findViewById(R.id.tv_sold_item_count);
        tv_cgst_percentage = view.findViewById(R.id.tv_cgst_percentage);
        tv_sgst_percentage = view.findViewById(R.id.tv_sgst_percentage);
        tv_wholsale_billing_price = view.findViewById(R.id.tv_wholse_billing_price);
        tv_retail_billing_price = view.findViewById(R.id.tv_retail_billing_price);
        tv_resell_billing_price = view.findViewById(R.id.tv_resell_billing_price);
        tv_gst_price = view.findViewById(R.id.tv_gst_price);

        imgbtn_product_edit = view.findViewById(R.id.imgbtn_product_edit);
        tv_c_name_selected = view.findViewById(R.id.tv_c_name);

        linear_category_edit_layout = view.findViewById(R.id.category_edit_layout);
        product_full_details_layout = view.findViewById(R.id.product_full_details_layout);
        tv_unit_spinner_txt = view.findViewById(R.id.tv_unit_spinner_txt_items_layout);

        tv_number_of_products = view.findViewById(R.id.number_of_products_textView);

        tv_wholesale = view.findViewById(R.id.tv_wholesale);
        tv_retail = view.findViewById(R.id.tv_retail);
        tv_resell = view.findViewById(R.id.tv_resell);

        tv_final_wholesale = view.findViewById(R.id.textView25);
        tv_final_retail = view.findViewById(R.id.textView26);
        tv_final_resell = view.findViewById(R.id.textView27);

        constraintLayout_table_layout = view.findViewById(R.id.constraintLayout_table_layout);
        constraintLayout_wholesale = view.findViewById(R.id.constraintLayout_wholesale);
        constraintLayout_retail = view.findViewById(R.id.constraintLayout_retail);
        constraintLayout_resell = view.findViewById(R.id.constraintLayout_resell);

        view4_bottom_wholesale_lay = view.findViewById(R.id.view4_bottom_wholesale_lay);
        view4_bottom_retail_lay = view.findViewById(R.id.view4_bottom_retail_lay);
        tv_wholesale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                Float cgst;
                Float sgst, wholesale;
                if (tv_wholesale_price_table.getText().toString().isEmpty()) {
                    wholesale = 0.0f;
                } else {
                    String whole = tv_wholesale_price_table.getText().toString();
                    String[] split = whole.split(" ");
                    String wholesale_price = split[1].trim();
                    wholesale = Float.valueOf(wholesale_price);
                }
                if (tv_cgst_percentage.getText().toString().isEmpty()) {
                    cgst = 0.0f;
                } else {
                    String cgst_1 = tv_cgst_percentage.getText().toString();
                    String[] split = cgst_1.split("%");
                    String cgst_price = split[0].trim();
                    cgst = Float.valueOf(cgst_price);
                }
                if (tv_sgst_percentage.getText().toString().isEmpty()) {
                    sgst = 0.0f;
                } else {
                    String sgst_1 = tv_sgst_percentage.getText().toString();
                    String[] split = sgst_1.split("%");
                    String sgst_price = split[0].trim();

                    sgst = Float.valueOf(sgst_price);
                }

                Float tax = cgst + sgst;
                Float wholesale_rate = wholesale * tax / 100;
                Float calc1 = wholesale + wholesale_rate;
                @SuppressLint("DefaultLocale") String i_rate_1 = String.format("%.2f", calc1);
                tv_wholsale_billing_price.setText("Rs " + i_rate_1);
            }
        });
        tv_retail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                Float cgst;
                Float sgst, retail;
                if (tv_retail_price_table.getText().toString().isEmpty()) {
                    retail = 0.0f;
                } else {
                    String retail_01 = tv_retail_price_table.getText().toString();
                    String[] split = retail_01.split(" ");
                    String retail_01_price = split[1].trim();
                    retail = Float.valueOf(retail_01_price);
                }
                if (tv_cgst_percentage.getText().toString().isEmpty()) {
                    cgst = 0.0f;
                } else {
                    String cgst_1 = tv_cgst_percentage.getText().toString();
                    String[] split = cgst_1.split("%");
                    String cgst_price = split[0].trim();
                    cgst = Float.valueOf(cgst_price);
                }
                if (tv_sgst_percentage.getText().toString().isEmpty()) {
                    sgst = 0.0f;
                } else {
                    String sgst_1 = tv_sgst_percentage.getText().toString();
                    String[] split = sgst_1.split("%");
                    String sgst_price = split[0].trim();
                    sgst = Float.valueOf(sgst_price);
                }
                Float tax = cgst + sgst;
                Float retail_01_rate = retail * tax / 100;
                Float calc1 = retail + retail_01_rate;
                @SuppressLint("DefaultLocale") String i_rate_1 = String.format("%.2f", calc1);
                tv_retail_billing_price.setText("Rs " + i_rate_1);
            }
        });
        tv_resell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                Float cgst;
                Float sgst, resell;
                if (tv_resell_price_table.getText().toString().isEmpty()) {
                    resell = 0.0f;
                } else {
                    String resell_01 = tv_resell_price_table.getText().toString();
                    String[] split = resell_01.split(" ");
                    String resell_01_price = split[1].trim();
                    resell = Float.valueOf(resell_01_price);
                }
                if (tv_cgst_percentage.getText().toString().isEmpty()) {
                    cgst = 0.0f;
                } else {
                    String cgst_1 = tv_cgst_percentage.getText().toString();
                    String[] split = cgst_1.split("%");
                    String cgst_price = split[0].trim();
                    cgst = Float.valueOf(cgst_price);
                }
                if (tv_sgst_percentage.getText().toString().isEmpty()) {
                    sgst = 0.0f;
                } else {
                    String sgst_1 = tv_sgst_percentage.getText().toString();
                    String[] split = sgst_1.split("%");
                    String sgst_price = split[0].trim();

                    sgst = Float.valueOf(sgst_price);
                }
                Float tax = cgst + sgst;
                Float wholesale_rate = resell * tax / 100;
                Float calc1 = resell + wholesale_rate;
                @SuppressLint("DefaultLocale") String i_rate_1 = String.format("%.2f", calc1);
                tv_resell_billing_price.setText("Rs " + i_rate_1);
            }
        });
        create_listview();
        ib_category_add.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                dialog = new Dialog(Objects.requireNonNull(getContext()));
                dialog.setContentView(R.layout.category_add_alert_dialog);
                dialog.setCancelable(false);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                et_category_name = dialog.findViewById(R.id.category_name_edittext);

                ImageButton ib_yes = dialog.findViewById(R.id.yes_imagebutton);
                ImageButton ib_no = dialog.findViewById(R.id.no_imagebutton);
                ImageButton ib_cateogry_product_add = dialog.findViewById(R.id.product_add_image_button);

                ib_cateogry_product_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (insert_category_details()) {
                            category_product_add_alertdialog();
                            imgbtn_yes_dialog_lay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (insert_product_details()) {
                                        dialog_window.dismiss();
                                    }
                                }
                            });
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
                ib_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (insert_category_details()) {
                            final Dialog d = new Dialog(Objects.requireNonNull(getContext()));
                            d.setContentView(R.layout.save_edit_alertdialog);
                            d.setCancelable(false);
                            Objects.requireNonNull(d.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            Button b_yes, b_no;
                            b_yes = d.findViewById(R.id.yes_button);
                            b_no = d.findViewById(R.id.no_button);
                            b_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    d.dismiss();
                                    category_product_add_alertdialog();
                                    imgbtn_yes_dialog_lay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (insert_product_details()) {
                                                dialog_window.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
                            b_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    d.dismiss();
                                }
                            });
                            d.show();
                        }
                    }
                });
                ib_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        ib_product_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c_name == null) {
                    toast_message = getString(R.string.select_any_cate_txt);
                    custom_toast(toast_message);
                } else {
                    category_product_add_alertdialog();
                    imgbtn_yes_dialog_lay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (insert_product_details()) {
                                dialog_window.dismiss();
                            }
                        }
                    });
                    spinner_category_dialog_lay.setText(c_name);
                }
            }
        });

        b_edit_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c_name == null) {
                    toast_message = getString(R.string.select_any_cate_txt);
                    custom_toast(toast_message);
                } else {
                    selectedStrings.clear();
                    product_existing_alertdialog();
                }

            }
        });
        product_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                str_item_name = adapterView.getItemAtPosition(i).toString();
                linear_category_edit_layout.setVisibility(View.GONE);
                product_full_details_layout.setVisibility(View.VISIBLE);
                tv_product_name.setText(str_item_name);
                get_Product_details();
                get_Rate_details();
                set_BM_NAME();
            }
        });
        ib_c_name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c_name == null) {
                    toast_message = getString(R.string.select_any_cate_txt);
                    custom_toast(toast_message);
                } else {
                    final Dialog d = new Dialog(Objects.requireNonNull(getContext()));
                    d.setContentView(R.layout.category_name_edit_dialog);
                    Objects.requireNonNull(d.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    et_update_cate_name = d.findViewById(R.id.et_update_cate_name);
                    et_update_cate_name.setSelection(et_update_cate_name.getText().toString().length());
                    Button b_update = d.findViewById(R.id.btn_update);
                    et_update_cate_name.setText(category_name.getText().toString());
                    et_update_cate_name.setSelectAllOnFocus(true);
                    Log.e("et_update_cate_name: ", et_update_cate_name.getText().toString());
                    b_update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(c_name.equalsIgnoreCase(et_update_cate_name.getText().toString().trim())){
                                toast_message = "No Changes Made";
                                custom_toast(toast_message);
                                Toast.makeText(getContext(), "No Changes Made", Toast.LENGTH_SHORT).show();
                            }
                          else if (rowIdExists_c_name_update(et_update_cate_name.getText().toString().trim())) {
                                Pattern ps = Pattern.compile("^[ a-zA-Z0-9]+$");
                                Matcher ms = ps.matcher(et_update_cate_name.getText().toString().trim());
                                boolean bs = ms.matches();
                                if (!bs) {
                                    Pattern pattern_2 = Pattern.compile("[ a-zA-Z!@#$%^&*()<>,./:;'+-=-_]+$");
                                    Matcher matcher_2 = pattern_2.matcher(et_update_cate_name.getText().toString());
                                    boolean b2 = matcher_2.matches();
                                    if (b2) {
                                        Toast.makeText(getActivity(), R.string.spl_char_not_allowed, Toast.LENGTH_SHORT).show();
                                        et_update_cate_name.getText().clear();
                                    } else {
                                        Toast.makeText(getActivity(), R.string.pls_enter_valid_name, Toast.LENGTH_SHORT).show();
                                        et_update_cate_name.getText().clear();
                                    }
                                    //    Toast.makeText(getContext(), "Please Enter Item Name", Toast.LENGTH_SHORT).show();
                                } else {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("C_NAME", et_update_cate_name.getText().toString().trim());
                                    MainActivity.db.update("CATEGORY", contentValues, "C_NAME = '" + category_name.getText().toString() + "'", null);

                                    ContentValues cv_item_update = new ContentValues();
                                    cv_item_update.put("C_NAME", et_update_cate_name.getText().toString().trim());
                                    MainActivity.db.update("ITEM", cv_item_update, "C_NAME = '" + category_name.getText().toString() + "'", null);
                                    DBExport();
                                    create_listview();
                                    tv_c_name_selected.setText(et_update_cate_name.getText().toString().trim());
                                    category_name.setText(et_update_cate_name.getText().toString().trim());
                                    Toast.makeText(getContext(), "Category Name Updated Successfully", Toast.LENGTH_SHORT).show();
                                    d.dismiss();
                                }
                            } else {
                                toast_message = "Category Name Already Exists";
                                custom_toast(toast_message);
                                create_listview();
                            }
                        }
                    });
                    d.show();
                }
            }
        });

        imgbtn_product_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c_name == null) {
                    toast_message = getString(R.string.select_any_cate_txt);
                    custom_toast(toast_message);
                } else {
                    category_product_add_alertdialog_1();
                    get_Product_details_edit();
                    get_Rate_details_edit();
                }
            }
        });
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void custom_toast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View toastLayout = inflater.inflate(R.layout.content_custom_toast, null, false);
        tv_custom_toast = toastLayout.findViewById(R.id.tv_custom_toast);
        tv_custom_toast.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }

    @SuppressLint("SetTextI18n")
    private void get_Product_details() {
        String selectQuery = "SELECT * from ITEM where I_NAME = '" + tv_product_name.getText().toString() + "'";
        Cursor cursor = MainActivity.db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String cursor_i_name = cursor.getString(1);
                String cursor_cgst = cursor.getString(4);
                String cursor_sgst = cursor.getString(5);
                String cursor_units = cursor.getString(6);
                String cursor_created_on = cursor.getString(8);
                int cursor_favourites = cursor.getInt(7);

                String[] date_time = cursor_created_on.split(" ");
                String date = date_time[0].trim();
                tv_product_name.setText(cursor_i_name);
                tv_unit_spinner_txt.setText(cursor_units);
                if (cursor_cgst.isEmpty()) {
                    tv_cgst_percentage.setText("0" + "%");
                } else {
                    tv_cgst_percentage.setText(cursor_cgst + "%");
                }
                if (cursor_sgst.isEmpty()) {
                    tv_sgst_percentage.setText("0" + "%");
                } else {
                    tv_sgst_percentage.setText(cursor_sgst + "%");
                }
                tv_date.setText(date);

                if (cursor_favourites == 0) {
                    iv_favourites.setImageResource(R.drawable.black_star);
                } else if (cursor_favourites == 1) {
                    iv_favourites.setImageResource(R.drawable.yellow_star);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        ArrayList<Float> qty_list = new ArrayList<>();
        String select_qty = "SELECT QTY from BILLING where I_NAME='" + tv_product_name.getText().toString() + "' and CANCELLED='" + 0 + "'";
        Cursor cursor_qty = MainActivity.db.rawQuery(select_qty, null);
        Float qnty = 0.00f;
        if (cursor_qty.getCount() > 0) {
            if (cursor_qty.moveToFirst()) {
                do {
                    Float quantity = cursor_qty.getFloat(0);
                    qty_list.add(quantity);
                } while (cursor_qty.moveToNext());
            }
            cursor_qty.close();
            for (int i = 0; i <= qty_list.size() - 1; i++) {
                qnty = qnty + qty_list.get(i);
                tv_sold_item_count.setText(String.valueOf(qnty));
            }
        } else {
            tv_sold_item_count.setText(String.valueOf(qnty));
        }

    }

    @SuppressLint("SetTextI18n")
    private void get_Rate_details() {
        ArrayList<Float> i_rate_list = new ArrayList<>();
        String selectQuery = "SELECT I_RATE from RATE where I_NAME = '" + tv_product_name.getText().toString().trim() + "'";
        Cursor cursor = MainActivity.db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Float cursor_i_rate = cursor.getFloat(0);
                i_rate_list.add(cursor_i_rate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (i_rate_list.size() == 0) {
            tv_wholesale_price_table.setVisibility(View.GONE);
            tv_retail_price_table.setVisibility(View.GONE);
            tv_resell_price_table.setVisibility(View.GONE);

            tv_final_wholesale.setVisibility(View.GONE);
            tv_final_retail.setVisibility(View.GONE);
            tv_final_resell.setVisibility(View.GONE);

            tv_wholsale_billing_price.setVisibility(View.GONE);
            tv_retail_billing_price.setVisibility(View.GONE);
            tv_resell_billing_price.setVisibility(View.GONE);

            tv_wholesale.setVisibility(View.GONE);
            tv_retail.setVisibility(View.GONE);
            tv_resell.setVisibility(View.GONE);
            constraintLayout_wholesale.setVisibility(View.GONE);
            constraintLayout_retail.setVisibility(View.GONE);
            constraintLayout_resell.setVisibility(View.GONE);

            view4_bottom_wholesale_lay.setVisibility(View.GONE);
            view4_bottom_retail_lay.setVisibility(View.GONE);

        } else if (i_rate_list.size() == 1) {
            tv_wholesale_price_table.setText("Rs " + i_rate_list.get(0));
            tv_wholesale_price_table.setVisibility(View.VISIBLE);
            tv_retail_price_table.setVisibility(View.GONE);
            tv_resell_price_table.setVisibility(View.GONE);

            tv_final_wholesale.setVisibility(View.VISIBLE);
            tv_final_retail.setVisibility(View.GONE);
            tv_final_resell.setVisibility(View.GONE);

            tv_wholsale_billing_price.setText("Rs " + i_rate_list.get(0));
            tv_wholsale_billing_price.setVisibility(View.VISIBLE);
            tv_retail_billing_price.setVisibility(View.GONE);
            tv_resell_billing_price.setVisibility(View.GONE);

            tv_wholesale.setVisibility(View.VISIBLE);
            tv_retail.setVisibility(View.GONE);
            tv_resell.setVisibility(View.GONE);

            constraintLayout_wholesale.setVisibility(View.VISIBLE);
            constraintLayout_retail.setVisibility(View.GONE);
            constraintLayout_resell.setVisibility(View.GONE);
            view4_bottom_wholesale_lay.setVisibility(View.GONE);
            view4_bottom_retail_lay.setVisibility(View.GONE);
        }
        else if (i_rate_list.size() == 2) {
            tv_wholesale_price_table.setText("Rs " + i_rate_list.get(0));
            tv_retail_price_table.setText("Rs " + i_rate_list.get(1));
            tv_wholesale_price_table.setVisibility(View.VISIBLE);
            tv_retail_price_table.setVisibility(View.VISIBLE);
            tv_resell_price_table.setVisibility(View.GONE);

            tv_final_wholesale.setVisibility(View.VISIBLE);
            tv_final_retail.setVisibility(View.VISIBLE);
            tv_final_resell.setVisibility(View.GONE);

            tv_wholsale_billing_price.setText("Rs " + i_rate_list.get(0));
            tv_retail_billing_price.setText("Rs " + i_rate_list.get(1));
            tv_wholsale_billing_price.setVisibility(View.VISIBLE);
            tv_retail_billing_price.setVisibility(View.VISIBLE);
            tv_resell_billing_price.setVisibility(View.GONE);

            tv_wholesale.setVisibility(View.VISIBLE);
            tv_retail.setVisibility(View.VISIBLE);
            tv_resell.setVisibility(View.GONE);

            constraintLayout_wholesale.setVisibility(View.VISIBLE);
            constraintLayout_retail.setVisibility(View.VISIBLE);
            constraintLayout_resell.setVisibility(View.GONE);

            view4_bottom_wholesale_lay.setVisibility(View.VISIBLE);
            view4_bottom_retail_lay.setVisibility(View.GONE);
        }
        else if (i_rate_list.size() == 3) {
            tv_wholesale_price_table.setText("Rs " + i_rate_list.get(0));
            tv_retail_price_table.setText("Rs " + i_rate_list.get(1));
            tv_resell_price_table.setText("Rs " + i_rate_list.get(2));
            tv_wholesale_price_table.setVisibility(View.VISIBLE);
            tv_retail_price_table.setVisibility(View.VISIBLE);
            tv_resell_price_table.setVisibility(View.VISIBLE);

            tv_final_wholesale.setVisibility(View.VISIBLE);
            tv_final_retail.setVisibility(View.VISIBLE);
            tv_final_resell.setVisibility(View.VISIBLE);

            tv_wholsale_billing_price.setText("Rs " + i_rate_list.get(0));
            tv_retail_billing_price.setText("Rs " + i_rate_list.get(1));
            tv_resell_billing_price.setText("Rs " + i_rate_list.get(2));
            tv_wholsale_billing_price.setVisibility(View.VISIBLE);
            tv_retail_billing_price.setVisibility(View.VISIBLE);
            tv_resell_billing_price.setVisibility(View.VISIBLE);

            tv_wholesale.setVisibility(View.VISIBLE);
            tv_retail.setVisibility(View.VISIBLE);
            tv_resell.setVisibility(View.VISIBLE);

            constraintLayout_wholesale.setVisibility(View.VISIBLE);
            constraintLayout_retail.setVisibility(View.VISIBLE);
            constraintLayout_resell.setVisibility(View.VISIBLE);

            view4_bottom_wholesale_lay.setVisibility(View.VISIBLE);
            view4_bottom_retail_lay.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void get_Rate_details_edit() {
        ArrayList<Float> i_rate_list = new ArrayList<>();
        String selectQuery = "SELECT I_RATE from RATE where I_NAME = '" + tv_product_name.getText().toString().trim() + "'";
        Cursor cursor = MainActivity.db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Float cursor_i_rate = cursor.getFloat(0);
                i_rate_list.add(cursor_i_rate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (i_rate_list.size() == 0) {
            tv_wholesale_table.setVisibility(View.GONE);
            tv_retail_table.setVisibility(View.GONE);
            tv_resell_table.setVisibility(View.GONE);
            et_wholesale_dialog_lay.setVisibility(View.GONE);
            et_retail_dialog_lay.setVisibility(View.GONE);
            et_resell_dialog_lay.setVisibility(View.GONE);

            constraintLayout_wholesale_dialog_lay.setVisibility(View.GONE);
            constraintLayout_retail_dialog_lay.setVisibility(View.GONE);
            constraintLayout_resell_dialog_lay.setVisibility(View.GONE);
            view4_bottom_wholesale_lay_diallog.setVisibility(View.GONE);
            view4_bottom_retail_lay_dialog_lay.setVisibility(View.GONE);

            tv_wholesale_price.setVisibility(View.GONE);
            tv_retail_price.setVisibility(View.GONE);
            tv_resell_price.setVisibility(View.GONE);

            tv_wholesale_price_dialog.setVisibility(View.GONE);
            tv_retail_price_dialog.setVisibility(View.GONE);
            tv_resell_price_dialog.setVisibility(View.GONE);
        }
        else if (i_rate_list.size() == 1) {
            constraintLayout_wholesale_dialog_lay.setVisibility(View.VISIBLE);
            constraintLayout_retail_dialog_lay.setVisibility(View.GONE);
            constraintLayout_resell_dialog_lay.setVisibility(View.GONE);
            view4_bottom_wholesale_lay_diallog.setVisibility(View.GONE);
            view4_bottom_retail_lay_dialog_lay.setVisibility(View.GONE);

            et_wholesale_dialog_lay.setVisibility(View.VISIBLE);
            et_retail_dialog_lay.setVisibility(View.GONE);
            et_resell_dialog_lay.setVisibility(View.GONE);
            tv_wholesale_table.setVisibility(View.VISIBLE);
            tv_retail_table.setVisibility(View.GONE);
            tv_resell_table.setVisibility(View.GONE);
            et_wholesale_dialog_lay.setText("Rs " + i_rate_list.get(0));

            tv_wholesale_price.setVisibility(View.VISIBLE);
            tv_retail_price.setVisibility(View.GONE);
            tv_resell_price.setVisibility(View.GONE);

            tv_wholesale_price_dialog.setVisibility(View.VISIBLE);
            tv_retail_price_dialog.setVisibility(View.GONE);
            tv_resell_price_dialog.setVisibility(View.GONE);
        }
        else if (i_rate_list.size() == 2) {
            et_wholesale_dialog_lay.setVisibility(View.VISIBLE);
            et_retail_dialog_lay.setVisibility(View.VISIBLE);
            et_resell_dialog_lay.setVisibility(View.GONE);
            tv_wholesale_table.setVisibility(View.VISIBLE);
            tv_retail_table.setVisibility(View.VISIBLE);
            tv_resell_table.setVisibility(View.GONE);
            et_wholesale_dialog_lay.setText("Rs " + i_rate_list.get(0));
            et_retail_dialog_lay.setText("Rs " + i_rate_list.get(1));

            constraintLayout_wholesale_dialog_lay.setVisibility(View.VISIBLE);
            constraintLayout_retail_dialog_lay.setVisibility(View.VISIBLE);
            constraintLayout_resell_dialog_lay.setVisibility(View.GONE);
            view4_bottom_wholesale_lay_diallog.setVisibility(View.VISIBLE);
            view4_bottom_retail_lay_dialog_lay.setVisibility(View.GONE);

            tv_wholesale_price.setVisibility(View.VISIBLE);
            tv_retail_price.setVisibility(View.VISIBLE);
            tv_resell_price.setVisibility(View.GONE);

            tv_wholesale_price_dialog.setVisibility(View.VISIBLE);
            tv_retail_price_dialog.setVisibility(View.VISIBLE);
            tv_resell_price_dialog.setVisibility(View.GONE);
        }
        else if (i_rate_list.size() == 3) {
            et_wholesale_dialog_lay.setVisibility(View.VISIBLE);
            et_retail_dialog_lay.setVisibility(View.VISIBLE);
            et_resell_dialog_lay.setVisibility(View.VISIBLE);
            tv_wholesale_table.setVisibility(View.VISIBLE);
            tv_retail_table.setVisibility(View.VISIBLE);
            tv_resell_table.setVisibility(View.VISIBLE);

            constraintLayout_table_layout_in_dialog.setVisibility(View.VISIBLE);
            constraintLayout_wholesale_dialog_lay.setVisibility(View.VISIBLE);
            constraintLayout_retail_dialog_lay.setVisibility(View.VISIBLE);
            constraintLayout_resell_dialog_lay.setVisibility(View.VISIBLE);
            view4_bottom_wholesale_lay_diallog.setVisibility(View.VISIBLE);
            view4_bottom_retail_lay_dialog_lay.setVisibility(View.VISIBLE);
            et_wholesale_dialog_lay.setText("Rs " + i_rate_list.get(0));
            et_retail_dialog_lay.setText("Rs " + i_rate_list.get(1));
            et_resell_dialog_lay.setText("Rs " + i_rate_list.get(2));

            tv_wholesale_price.setVisibility(View.VISIBLE);
            tv_retail_price.setVisibility(View.VISIBLE);
            tv_resell_price.setVisibility(View.VISIBLE);

            tv_wholesale_price_dialog.setVisibility(View.VISIBLE);
            tv_retail_price_dialog.setVisibility(View.VISIBLE);
            tv_resell_price_dialog.setVisibility(View.VISIBLE);
        }
        et_wholesale_dialog_lay.setSelection(et_wholesale_dialog_lay.getText().toString().length());
        et_retail_dialog_lay.setSelection(et_retail_dialog_lay.getText().toString().length());
        et_resell_dialog_lay.setSelection(et_resell_dialog_lay.getText().toString().length());
    }

    @SuppressLint({"SetTextI18n", "LongLogTag"})
    private void get_Product_details_edit() {

        String selectQuery = "SELECT * from ITEM where I_NAME = '" + tv_product_name.getText().toString() + "'";
        Cursor cursor = MainActivity.db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String cursor_i_name = cursor.getString(1);
                String cursor_c_name = cursor.getString(3);
                String cursor_cgst = cursor.getString(4);
                String cursor_sgst = cursor.getString(5);
                String cursor_units = cursor.getString(6);
                int cursor_favourites = cursor.getInt(7);
                spinner_category_dialog_lay.setText(cursor_c_name);
                et_product_name_edittext_dialog_lay.setText(cursor_i_name);

                spinner_units_dialog_lay.setText(cursor_units);
                et_cgst_percentage_dialog_lay.setText(cursor_cgst);
                et_sgst_percentage_dialog_lay.setText(cursor_sgst);
                if (cursor_favourites == 0) {
                    iv_fav_dialog_window.setImageResource(R.drawable.black_star);
                } else if (cursor_favourites == 1) {
                    iv_fav_dialog_window.setImageResource(R.drawable.yellow_star);
                }
                et_product_name_edittext_dialog_lay.setSelection(et_product_name_edittext_dialog_lay.getText().toString().length());
                et_cgst_percentage_dialog_lay.setSelection(et_cgst_percentage_dialog_lay.getText().toString().length());
                et_sgst_percentage_dialog_lay.setSelection(et_sgst_percentage_dialog_lay.getText().toString().length());
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    private void get_category_names() {
        c_name_list.clear();
        String select = "SELECT C_NAME from CATEGORY";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                String c_name = cursor.getString(0);
                c_name_list.add(c_name);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean insert_category_details() {
        boolean allMatch = false;
        if (rowIdExists(et_category_name.getText().toString())) {
            Log.e("et_category_name:", et_category_name.getText().toString());
            Pattern ps = Pattern.compile("^[ a-zA-Z0-9]+$");
            Matcher ms = ps.matcher(et_category_name.getText().toString().trim());
            boolean bs = ms.matches();
            if (!bs) {
                Pattern pattern_2 = Pattern.compile("[ a-zA-Z!@#$%^&*()<>,./:;'+-=-_]+$");
                Matcher matcher_2 = pattern_2.matcher(et_category_name.getText().toString());
                boolean b2 = matcher_2.matches();
                if (b2) {
                    Toast.makeText(getActivity(), R.string.spl_char_not_allowed, Toast.LENGTH_SHORT).show();
                    et_category_name.getText().clear();
                } else {
                    Toast.makeText(getActivity(), R.string.pls_enter_valid_name, Toast.LENGTH_SHORT).show();
                    et_category_name.getText().clear();
                }
            } else {
                dialog.dismiss();
                ContentValues contentValues = new ContentValues();
                contentValues.put("C_NAME", et_category_name.getText().toString().trim());
                contentValues.put("CREATED_ON", getDateTime());
                MainActivity.db.insert("CATEGORY", null, contentValues);
                DBExport();
                category_name_list.clear();
                create_listview();
                allMatch = true;
            }
        } else {
            Toast.makeText(getContext(), R.string.cate_name_already_exists, Toast.LENGTH_SHORT).show();
            allMatch = false;
        }
        return allMatch;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean insert_product_details() {
        boolean allMatch = false;
        if (rowIdExists_product(et_product_name_edittext_dialog_lay.getText().toString().trim())) {
            Pattern ps = Pattern.compile("^[ a-zA-Z0-9]+$");
            Matcher ms = ps.matcher(et_product_name_edittext_dialog_lay.getText().toString().trim());
            boolean bs = ms.matches();
            if (!bs) {
                Pattern pattern_2 = Pattern.compile("[ a-zA-Z!@#$%^&*()<>,./:;'+-=-_]+$");
                Matcher matcher_2 = pattern_2.matcher(et_product_name_edittext_dialog_lay.getText().toString());
                boolean b2 = matcher_2.matches();
                if (b2) {
                    Toast.makeText(getActivity(), R.string.spl_char_not_allowed, Toast.LENGTH_SHORT).show();
                    et_product_name_edittext_dialog_lay.getText().clear();
                } else {
                    Toast.makeText(getActivity(), R.string.pls_enter_valid_name, Toast.LENGTH_SHORT).show();
                    et_product_name_edittext_dialog_lay.getText().clear();
                }
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("I_NAME", et_product_name_edittext_dialog_lay.getText().toString().trim());
                get_C_ID();
                contentValues.put("C_ID", str_c_id);
                contentValues.put("C_NAME", spinner_category_dialog_lay.getText().toString());
                contentValues.put("CGST", et_cgst_percentage_dialog_lay.getText().toString());
                contentValues.put("SGST", et_sgst_percentage_dialog_lay.getText().toString());
                contentValues.put("UNITS", spinner_units_dialog_lay.getText().toString());
                contentValues.put("CREATED_ON", getDateTime());
                contentValues.put("FAVOURITES", favourites_value);
                MainActivity.db.insert("ITEM", null, contentValues);
                DBExport();
                if (favourites_value == 1) {
                    iv_fav_dialog_window.setImageResource(R.drawable.black_star);
                    favourites_value = 0;
                }
                product_name_list.clear();
                product_listview.setVisibility(View.VISIBLE);
                tv_no_products_for.setVisibility(View.GONE);
                create_product_list();
                insert_rate_details();
                create_listview();
                allMatch = true;
            }
        } else {
            Toast.makeText(getContext(), R.string.item_name_already_exists, Toast.LENGTH_SHORT).show();
            allMatch = false;
        }
        return allMatch;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean update_product_details() {
        boolean allMatch = false;
        if (rowIdExists_item_update(I_ID)) {
            Drawable.ConstantState res = iv_fav_dialog_window.getDrawable().getConstantState();
            assert res != null;
            if (res.equals(Objects.requireNonNull(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()),
                    R.drawable.black_star)).getConstantState())) {
                favourites_value = 0;
            } else if (res.equals(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(),
                    R.drawable.yellow_star)).getConstantState())) {
                favourites_value = 1;
            }
            Pattern ps = Pattern.compile("^[ a-zA-Z0-9]+$");
            Matcher ms = ps.matcher(et_product_name_edittext_dialog_lay.getText().toString().trim());
            boolean bs = ms.matches();
            if (!bs) {
                Pattern pattern_2 = Pattern.compile("[ a-zA-Z!@#$%^&*()<>,./:;'+-=-_]+$");
                Matcher matcher_2 = pattern_2.matcher(et_product_name_edittext_dialog_lay.getText().toString());
                boolean b2 = matcher_2.matches();
                if (b2) {
                    Toast.makeText(getActivity(), R.string.spl_char_not_allowed, Toast.LENGTH_SHORT).show();
                    et_product_name_edittext_dialog_lay.getText().clear();
                } else {
                    Toast.makeText(getActivity(), R.string.pls_enter_valid_name, Toast.LENGTH_SHORT).show();
                    et_product_name_edittext_dialog_lay.getText().clear();
                }
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("I_NAME", et_product_name_edittext_dialog_lay.getText().toString().trim());
                get_C_ID();
                contentValues.put("C_ID", str_c_id);
                contentValues.put("C_NAME", spinner_category_dialog_lay.getText().toString());
                contentValues.put("CGST", et_cgst_percentage_dialog_lay.getText().toString());
                contentValues.put("SGST", et_sgst_percentage_dialog_lay.getText().toString());
                contentValues.put("UNITS", spinner_units_dialog_lay.getText().toString());
                contentValues.put("CREATED_ON", getDateTime());
                contentValues.put("FAVOURITES", favourites_value);
                MainActivity.db.update("ITEM", contentValues, "I_ID = '" + I_ID + "'", null);
                tv_product_name.setText(et_product_name_edittext_dialog_lay.getText().toString().trim());
                DBExport();
                if (favourites_value == 1) {
                    iv_fav_dialog_window.setImageResource(R.drawable.black_star);
                    favourites_value = 0;
                }
                product_name_list.clear();
                create_product_list();
                create_listview();
                update_rate_details();
                Float retail, resell, wholesale;
                //  Float cgst, sgst;
                if (et_wholesale_dialog_lay.getText().toString().trim().isEmpty()) {
                    wholesale = 0.00f;
                } else {
                    wholesale = Float.valueOf(et_wholesale_dialog_lay.getText().toString().trim());
                }
                if (et_retail_dialog_lay.getText().toString().trim().isEmpty()) {
                    retail = 0.00f;
                } else {
                    retail = Float.valueOf(et_retail_dialog_lay.getText().toString().trim());
                }
                if (et_resell_dialog_lay.getText().toString().trim().isEmpty()) {
                    resell = 0.00f;
                } else {
                    resell = Float.valueOf(et_resell_dialog_lay.getText().toString().trim());
                }
                String it_rate = String.valueOf(wholesale + "," + retail + "," + resell);
                item_rate = it_rate.split(",");

                String select_bm = "SELECT * from BILL_MODE";
                Cursor cursor_bm = MainActivity.db.rawQuery(select_bm, null);
                if (cursor_bm.getCount() > 0) {
                    if (cursor_bm.getCount() == 1) {
                        ContentValues cv_rate = new ContentValues();
                        cv_rate.put("I_NAME", et_product_name_edittext_dialog_lay.getText().toString().trim());
                        cv_rate.put("I_RATE", item_rate[0].trim());
                        MainActivity.db.update("RATE", cv_rate, "I_ID='" + I_ID + "' and BM_ID = '" + bm_id_list.get(0) + "'", null);
                    } else if (cursor_bm.getCount() == 2) {
                        for (int i = 0; i < 2; i++) {
                            ContentValues cv_rate = new ContentValues();
                            cv_rate.put("I_NAME", et_product_name_edittext_dialog_lay.getText().toString().trim());
                            cv_rate.put("I_RATE", item_rate[i].trim());
                            MainActivity.db.update("RATE", cv_rate, "I_ID='" + I_ID + "' and BM_ID = '" + bm_id_list.get(i) + "'", null);
                        }
                    } else if (cursor_bm.getCount() == 3) {
                        for (int i = 0; i < 3; i++) {
                            ContentValues cv_rate = new ContentValues();
                            cv_rate.put("I_NAME", et_product_name_edittext_dialog_lay.getText().toString().trim());
                            cv_rate.put("I_RATE", item_rate[i].trim());
                            MainActivity.db.update("RATE", cv_rate, "I_ID='" + I_ID + "' and BM_ID = '" + bm_id_list.get(i) + "'", null);
                        }
                    }
                }
                cursor_bm.close();
                get_Rate_details();
                set_i_rate_values();
                DBExport();
                get_Product_details();
                allMatch = true;
            }
        } else {
            Toast.makeText(getContext(), R.string.item_name_already_exists, Toast.LENGTH_SHORT).show();
            allMatch = false;
        }
        return allMatch;
    }

    private void insert_rate_details() {
        ArrayList<String> bm_name_list = new ArrayList<>();
        ArrayList<String> bm_id_list = new ArrayList<>();
        String select_BM_values = "SELECT BM_ID,BM_NAME from BILL_MODE";
        Cursor cursor = MainActivity.db.rawQuery(select_BM_values, null);
        if (cursor.moveToFirst()) {
            do {
                String bm_id = cursor.getString(0);
                String bm_name = cursor.getString(1);
                bm_id_list.add(bm_id);
                bm_name_list.add(bm_name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        String select_ITEM_values = "SELECT I_ID,I_NAME from ITEM";
        Cursor cursor1 = MainActivity.db.rawQuery(select_ITEM_values, null);
        if (cursor1.moveToFirst()) {
            do {
                cursor1_i_id = cursor1.getString(0);
                cursor1_i_name = cursor1.getString(1);
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        Float wholesale, retail, resell;
        if (et_wholesale_dialog_lay.getText().toString().isEmpty()) {
            wholesale = 0.00f;
        } else {
            wholesale = Float.valueOf(et_wholesale_dialog_lay.getText().toString());
        }
        if (et_retail_dialog_lay.getText().toString().isEmpty()) {
            retail = 0.00f;
        } else {
            retail = Float.valueOf(et_retail_dialog_lay.getText().toString());
        }
        if (et_resell_dialog_lay.getText().toString().isEmpty()) {
            resell = 0.00f;
        } else {
            resell = Float.valueOf(et_resell_dialog_lay.getText().toString());
        }
        String it_rate = String.valueOf(wholesale + "," + retail + "," + resell);
        String[] item_rate = it_rate.split(",");
        for (int i = 0; i < bm_id_list.size(); i++) {
            ContentValues cv_rate_insert = new ContentValues();
            cv_rate_insert.put("BM_ID", bm_id_list.get(i));
            cv_rate_insert.put("BM_NAME", bm_name_list.get(i));
            cv_rate_insert.put("I_ID", cursor1_i_id);
            cv_rate_insert.put("I_NAME", cursor1_i_name);
            cv_rate_insert.put("I_RATE", item_rate[i].trim());
            MainActivity.db.insert("RATE", null, cv_rate_insert);
        }
        et_wholesale_dialog_lay.setSelection(et_wholesale_dialog_lay.getText().toString().length());
        et_retail_dialog_lay.setSelection(et_retail_dialog_lay.getText().toString().length());
        et_resell_dialog_lay.setSelection(et_resell_dialog_lay.getText().toString().length());
        et_cgst_percentage_dialog_lay.setSelection(et_cgst_percentage_dialog_lay.getText().toString().length());
        et_sgst_percentage_dialog_lay.setSelection(et_sgst_percentage_dialog_lay.getText().toString().length());
        DBExport();
    }

    private void update_rate_details() {
        String select_BM_values = "SELECT BM_ID,BM_NAME from BILL_MODE";
        Cursor cursor = MainActivity.db.rawQuery(select_BM_values, null);
        if (cursor.moveToFirst()) {
            do {
                String bm_id = cursor.getString(0);
                bm_id_list.add(bm_id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        String select_ITEM_values = "SELECT I_ID,I_NAME from ITEM";
        Cursor cursor1 = MainActivity.db.rawQuery(select_ITEM_values, null);
        if (cursor1.moveToFirst()) {
            do {
                cursor1_i_id = cursor1.getString(0);
                cursor1_i_name = cursor1.getString(1);
            } while (cursor1.moveToNext());
        }
        cursor1.close();
    }

    @SuppressLint("SetTextI18n")
    private void set_i_rate_values() {
        Float cgst;
        Float sgst, wholesale, retail, resell;
        if (tv_wholesale_price_table.getText().toString().isEmpty()) {
            wholesale = 0.00f;
        } else {
            String whole = tv_wholesale_price_table.getText().toString();
            String[] split = whole.split(" ");
            String wholesale_price = split[1].trim();
            wholesale = Float.valueOf(wholesale_price);
        }
        if (tv_retail_price_table.getText().toString().isEmpty()) {
            retail = 0.00f;
        } else {
            String retail_01 = tv_retail_price_table.getText().toString();
            String[] split = retail_01.split(" ");
            String retail_01_price = split[1].trim();
            retail = Float.valueOf(retail_01_price);
        }
        if (tv_resell_price_table.getText().toString().isEmpty()) {
            resell = 0.00f;
        } else {
            String resell_01 = tv_resell_price_table.getText().toString();
            String[] split = resell_01.split(" ");
            String resell_01_price = split[1].trim();
            resell = Float.valueOf(resell_01_price);
        }
        if (tv_cgst_percentage.getText().toString().isEmpty()) {
            cgst = 0.00f;
        } else {
            String cgst_1 = tv_cgst_percentage.getText().toString();
            String[] split = cgst_1.split("%");
            String cgst_price = split[0].trim();
            cgst = Float.valueOf(cgst_price);
        }
        if (tv_sgst_percentage.getText().toString().isEmpty()) {
            sgst = 0.00f;
        } else {
            String sgst_1 = tv_sgst_percentage.getText().toString();
            String[] split = sgst_1.split("%");
            String sgst_price = split[0].trim();
            sgst = Float.valueOf(sgst_price);
        }
        Float tax = cgst + sgst;
        Float wholesale_rate = wholesale * tax / 100;
        Float calc1 = wholesale + wholesale_rate;
        @SuppressLint("DefaultLocale") String i_rate_1 = String.format("%.2f", calc1);
        tv_wholsale_billing_price.setText("Rs " + i_rate_1);

        Float retail_rate = retail * tax / 100;
        Float calc2 = retail + retail_rate;
        @SuppressLint("DefaultLocale") String i_rate_2 = String.format("%.2f", calc2);
        tv_retail_billing_price.setText("Rs " + i_rate_2);

        Float resell_rate = resell * tax / 100;
        Float calc3 = resell + resell_rate;
        @SuppressLint("DefaultLocale") String i_rate_3 = String.format("%.2f", calc3);
        tv_resell_billing_price.setText("Rs " + i_rate_3);
    }

    private void get_C_ID() {
        String select_C_ID = "SELECT C_ID from CATEGORY where C_NAME = '" + spinner_category_dialog_lay.getText().toString().trim() + "'";
        Cursor cursor = MainActivity.db.rawQuery(select_C_ID, null);
        if (cursor.moveToFirst()) {
            do {
                str_c_id = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public boolean rowIdExists(String name) {
        String select = "select C_NAME from CATEGORY ";
        String replace_name = name.replace(" ", "");
        Log.e("replace_name:", replace_name);
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
            String replace_string = string.replace(" ", "");
            if ((replace_string.equalsIgnoreCase(replace_name)) || (replace_string.equalsIgnoreCase(name))) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    public boolean rowIdExists_item_update(String name) {
        String select = "SELECT I_NAME from ITEM where I_ID!='" + name + "'";
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
            if (string.equalsIgnoreCase(et_product_name_edittext_dialog_lay.getText().toString().trim())) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }


    public boolean rowIdExists_product(String name) {
        String select = "select I_NAME from ITEM ";
        String replace_name = name.replace(" ", "");
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
            String replace_string = string.replace(" ", "");
            if ((replace_string.equalsIgnoreCase(name)) || (replace_string.equalsIgnoreCase(replace_name))) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    public boolean rowIdExists_c_name_update(String name) {
        String replace_name = name.replace(" ", "");
        String select = "select C_NAME from CATEGORY ";//Where C_NAME != '" + et_update_cate_name.getText().toString() + "'";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
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
            String replace_string = string.replace(" ", "");
            if ((replace_string.equalsIgnoreCase(et_update_cate_name.getText().toString().trim())) ||
                    (replace_string.equalsIgnoreCase(replace_name))) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void create_listview() {
        category_name_list.clear();
        String selectQuery = "Select C_NAME from CATEGORY";
        Cursor cursor = MainActivity.db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String c_name = cursor.getString(0);
                category_name_list.add(c_name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        ArrayList<String> count_list = new ArrayList<>();
        int count;
        for (int i = 0; i < category_name_list.size(); i++) {
            String select = "Select * from ITEM where C_NAME = '" + category_name_list.get(i).trim() + "'";
            Cursor cursor_1 = MainActivity.db.rawQuery(select, null);
            count = cursor_1.getCount();
            count_list.add(count + " Products");
            cursor_1.close();
        }
        ItemsAdapter itemsAdapter = new ItemsAdapter(Objects.requireNonNull(getContext()).getApplicationContext(), category_name_list, count_list);
        category_listview.setAdapter(itemsAdapter);
        category_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Selected_Position = position;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void create_product_list() {
        product_name_list.clear();
        String selectQuery_1 = "Select I_NAME from ITEM where C_NAME = '" + c_name + "'";
        Cursor cursor_1 = MainActivity.db.rawQuery(selectQuery_1, null);
        number_of_products = cursor_1.getCount();
        tv_number_of_products.setText(String.valueOf(number_of_products));
        if (cursor_1.moveToFirst()) {
            do {
                String i_name = cursor_1.getString(0);
                product_name_list.add(i_name);
            } while (cursor_1.moveToNext());
        }
        cursor_1.close();
        product_adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.product_listview_text, R.id.product_listvew_text, product_name_list);
        product_listview.setAdapter(product_adapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void category_product_add_alertdialog() {
        dialog_window = new Dialog(Objects.requireNonNull(getContext()));
        dialog_window.setContentView(R.layout.dialog_window);
        dialog_window.setCancelable(false);
        Objects.requireNonNull(dialog_window.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //For Dialog windows attributes
        dialog_control_button_01_lay = dialog_window.findViewById(R.id.dialog_control_button_01_lay);
        dialog_control_button_02_lay = dialog_window.findViewById(R.id.dialog_control_button_02_lay);

        linear_basic_details_lay = dialog_window.findViewById(R.id.linear_basic_details_lay);
        constraint_price_and_tax_lay = dialog_window.findViewById(R.id.constraint_price_and_tax_lay);

        et_wholesale_dialog_lay = dialog_window.findViewById(R.id.et_wholesale_dialog_lay);
        et_retail_dialog_lay = dialog_window.findViewById(R.id.et_retail_dialog_lay);
        et_resell_dialog_lay = dialog_window.findViewById(R.id.et_resell_dialog_lay);
        tv_wholesale_price_dialog = dialog_window.findViewById(R.id.tv_wholesale_price_dialog);
        tv_resell_price_dialog = dialog_window.findViewById(R.id.tv_resell_price_dialog);
        tv_retail_price_dialog = dialog_window.findViewById(R.id.tv_retail_price_dialog);

        et_wholesale_dialog_lay.setSelection(et_wholesale_dialog_lay.getText().toString().length());
        et_retail_dialog_lay.setSelection(et_retail_dialog_lay.getText().toString().length());
        et_resell_dialog_lay.setSelection(et_resell_dialog_lay.getText().toString().length());

        tv_wholesale_table = dialog_window.findViewById(R.id.wholesale);
        tv_retail_table = dialog_window.findViewById(R.id.retail);
        tv_resell_table = dialog_window.findViewById(R.id.resell);

        tv_wholesale_price = dialog_window.findViewById(R.id.tv_wholesale_price);
        tv_retail_price = dialog_window.findViewById(R.id.tv_retail_price);
        tv_resell_price = dialog_window.findViewById(R.id.tv_resell_price);

        //This attributes  for dialogwindow layout
        constraintLayout_table_layout_in_dialog = dialog_window.findViewById(R.id.constraintLayout_table_layout_in_dialog);
        constraintLayout_wholesale_dialog_lay = dialog_window.findViewById(R.id.constraintLayout_wholesale_dialog_lay);
        constraintLayout_retail_dialog_lay = dialog_window.findViewById(R.id.constraintLayout_retail_dialog_lay);
        constraintLayout_resell_dialog_lay = dialog_window.findViewById(R.id.constraintLayout_resell_dialog_lay);
        view4_bottom_wholesale_lay_diallog = dialog_window.findViewById(R.id.view4_bottom_wholesale_lay_diallog);
        view4_bottom_retail_lay_dialog_lay = dialog_window.findViewById(R.id.view4_bottom_retail_lay_dialog_lay);

        set_BM_NAME_dialog();
        et_wholesale_dialog_lay.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 7, afterDecimal = 2;
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = et_wholesale_dialog_lay.getText() + source.toString();
                        if (temp.equals(".")) {
                            return "0.";
                        } else if (!temp.contains(".")) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }
                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        et_retail_dialog_lay.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 7, afterDecimal = 2;
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = et_retail_dialog_lay.getText() + source.toString();
                        if (temp.equals(".")) {
                            return "0.";
                        } else if (!temp.contains(".")) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }
                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        et_resell_dialog_lay.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 7, afterDecimal = 2;
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = et_resell_dialog_lay.getText() + source.toString();
                        if (temp.equals(".")) {
                            return "0.";
                        } else if (!temp.contains(".")) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }
                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        tv_cgst_dialog_lay = dialog_window.findViewById(R.id.tv_cgst_dialog_lay);
        tv_sgst_dialog_lay = dialog_window.findViewById(R.id.tv_sgst_dialog_lay);

        et_cgst_percentage_dialog_lay = dialog_window.findViewById(R.id.et_cgst_percentage_dialog_lay);
        et_sgst_percentage_dialog_lay = dialog_window.findViewById(R.id.et_sgst_percentage_dialog_lay);
        et_cgst_percentage_dialog_lay.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 3, afterDecimal = 2;
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = et_cgst_percentage_dialog_lay.getText() + source.toString();
                        if (temp.equals(".")) {
                            return "0.";
                        } else if (!temp.contains(".")) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }
                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        et_sgst_percentage_dialog_lay.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 3, afterDecimal = 2;
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = et_sgst_percentage_dialog_lay.getText() + source.toString();
                        if (temp.equals(".")) {
                            return "0.";
                        } else if (!temp.contains(".")) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }
                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        imgbtn_cross_dialog_lay = dialog_window.findViewById(R.id.imgbtn_cross_dialog_lay);
        imgbtn_yes_dialog_lay = dialog_window.findViewById(R.id.imgbtn_yes_dialog_lay);
        ib_dialog_1_yes = dialog_window.findViewById(R.id.yes_imagebutton_dialog_1);
        ib_dialog_1_no = dialog_window.findViewById(R.id.no_imagebutton_dialog_1);

        tv_basic_details = dialog_window.findViewById(R.id.tv_basic_details);
        tv_price_and_tax = dialog_window.findViewById(R.id.tv_price_and_tax);
        iv_fav_dialog_window = dialog_window.findViewById(R.id.ratingBar_fav_dialog_window);
        et_product_name_edittext_dialog_lay = dialog_window.findViewById(R.id.product_name_edittext);
        spinner_category_dialog_lay = dialog_window.findViewById(R.id.spinner_category);
        spinner_units_dialog_lay = dialog_window.findViewById(R.id.spinner_units);
        spinner_category_dialog_lay = dialog_window.findViewById(R.id.spinner_category);
        get_category_names();
        int size = c_name_list.size();
        int calc = size - 1;
        spinner_category_dialog_lay.setText(c_name_list.get(calc));
        Collections.sort(c_name_list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        spin_cate_adapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_txt, c_name_list);
        spinner_category_dialog_lay.setAdapter(spin_cate_adapter);
        unit_spin_arraylist = getResources().getStringArray(R.array.units_values);
        unit_spin_adapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_txt, unit_spin_arraylist);
        spinner_units_dialog_lay.setAdapter(unit_spin_adapter);

        et_sgst_percentage_dialog_lay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                Float cgst;
                Float sgst, wholesale, retail, resell;
                if (et_wholesale_dialog_lay.getText().toString().isEmpty()) {
                    wholesale = 0.0f;
                } else {
                    wholesale = Float.valueOf(et_wholesale_dialog_lay.getText().toString());
                }
                if (et_retail_dialog_lay.getText().toString().isEmpty()) {
                    retail = 0.0f;
                } else {
                    retail = Float.valueOf(et_retail_dialog_lay.getText().toString());
                }
                if (et_resell_dialog_lay.getText().toString().isEmpty()) {
                    resell = 0.0f;
                } else {
                    resell = Float.valueOf(et_resell_dialog_lay.getText().toString());
                }
                if (et_cgst_percentage_dialog_lay.getText().toString().isEmpty()) {
                    cgst = 0.0f;
                } else {
                    cgst = Float.valueOf(et_cgst_percentage_dialog_lay.getText().toString());
                }if (et_sgst_percentage_dialog_lay.getText().toString().isEmpty()) {
                    sgst = 0.0f;
                } else {
                    sgst = Float.valueOf(et_sgst_percentage_dialog_lay.getText().toString());
                }
                Float tax = cgst + sgst;
                Float wholesale_rate = wholesale * tax / 100;
                Float retail_rate = retail * tax / 100;
                Float resell_rate = resell * tax / 100;

                Float calc1 = wholesale + wholesale_rate;
                Float calc2 = retail + retail_rate;
                Float calc3 = resell + resell_rate;

                @SuppressLint("DefaultLocale") String i_rate_1 = String.format("%.2f", calc1);
                @SuppressLint("DefaultLocale") String i_rate_2 = String.format("%.2f", calc2);
                @SuppressLint("DefaultLocale") String i_rate_3 = String.format("%.2f", calc3);

                tv_wholesale_price_dialog.setText("Rs " + i_rate_1);
                tv_retail_price_dialog.setText("Rs " + i_rate_2);
                tv_resell_price_dialog.setText("Rs " + i_rate_3);
            }
        });

        et_cgst_percentage_dialog_lay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                Float cgst;
                Float sgst, wholesale, retail, resell;
                if (et_wholesale_dialog_lay.getText().toString().isEmpty()) {
                    wholesale = 0.0f;
                } else {
                    wholesale = Float.valueOf(et_wholesale_dialog_lay.getText().toString());
                }
                if (et_retail_dialog_lay.getText().toString().isEmpty()) {
                    retail = 0.0f;
                } else {
                    retail = Float.valueOf(et_retail_dialog_lay.getText().toString());
                }
                if (et_resell_dialog_lay.getText().toString().isEmpty()) {
                    resell = 0.0f;
                } else {
                    resell = Float.valueOf(et_resell_dialog_lay.getText().toString());
                }
                if (et_cgst_percentage_dialog_lay.getText().toString().isEmpty()) {
                    cgst = 0.0f;
                } else {
                    cgst = Float.valueOf(et_cgst_percentage_dialog_lay.getText().toString());
                }
                if (et_sgst_percentage_dialog_lay.getText().toString().isEmpty()) {
                    sgst = 0.0f;
                } else {
                    sgst = Float.valueOf(et_sgst_percentage_dialog_lay.getText().toString());
                }
                Float tax = cgst + sgst;
                Float wholesale_rate = wholesale * tax / 100;
                Float retail_rate = retail * tax / 100;
                Float resell_rate = resell * tax / 100;

                Float calc1 = wholesale + wholesale_rate;
                Float calc2 = retail + retail_rate;
                Float calc3 = resell + resell_rate;

                @SuppressLint("DefaultLocale") String i_rate_1 = String.format("%.2f", calc1);
                @SuppressLint("DefaultLocale") String i_rate_2 = String.format("%.2f", calc2);
                @SuppressLint("DefaultLocale") String i_rate_3 = String.format("%.2f", calc3);

                tv_wholesale_price_dialog.setText("Rs " + i_rate_1);
                tv_retail_price_dialog.setText("Rs " + i_rate_2);
                tv_resell_price_dialog.setText("Rs " + i_rate_3);
            }
        });
        et_wholesale_dialog_lay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                Float cgst;
                Float sgst, wholesale;
                if (et_wholesale_dialog_lay.getText().toString().isEmpty()) {
                    wholesale = 0.0f;
                } else {
                    wholesale = Float.valueOf(et_wholesale_dialog_lay.getText().toString());
                }
                if (et_cgst_percentage_dialog_lay.getText().toString().isEmpty()) {
                    cgst = 0.0f;
                } else {
                    cgst = Float.valueOf(et_cgst_percentage_dialog_lay.getText().toString());
                }
                if (et_sgst_percentage_dialog_lay.getText().toString().isEmpty()) {
                    sgst = 0.0f;
                } else {
                    sgst = Float.valueOf(et_sgst_percentage_dialog_lay.getText().toString());
                }
                Float tax = cgst + sgst;
                Float wholesale_rate = wholesale * tax / 100;
                Float calc1 = wholesale + wholesale_rate;
                @SuppressLint("DefaultLocale") String i_rate_1 = String.format("%.2f", calc1);
                tv_wholesale_price_dialog.setText("Rs " + i_rate_1);
            }
        });
        et_retail_dialog_lay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                Float cgst;
                Float sgst, retail;
                if (et_retail_dialog_lay.getText().toString().isEmpty()) {
                    retail = 0.0f;
                } else {
                    retail = Float.valueOf(et_retail_dialog_lay.getText().toString());
                }
                //   Toast.makeText(getContext(), "retail", Toast.LENGTH_SHORT).show();
                if (et_cgst_percentage_dialog_lay.getText().toString().isEmpty()) {
                    cgst = 0.0f;
                } else {
                    cgst = Float.valueOf(et_cgst_percentage_dialog_lay.getText().toString());
                }
                if (et_sgst_percentage_dialog_lay.getText().toString().isEmpty()) {
                    sgst = 0.0f;
                } else {
                    sgst = Float.valueOf(et_sgst_percentage_dialog_lay.getText().toString());
                }
                Float tax = cgst + sgst;
                Float retail_rate = retail * tax / 100;
                Float calc1 = retail + retail_rate;
                @SuppressLint("DefaultLocale") String i_rate_1 = String.format("%.2f", calc1);
                tv_retail_price_dialog.setText("Rs " + i_rate_1);
            }
        });
        et_resell_dialog_lay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                Float cgst;
                Float sgst, resell;
                if (et_resell_dialog_lay.getText().toString().isEmpty()) {
                    resell = 0.0f;
                } else {
                    resell = Float.valueOf(et_resell_dialog_lay.getText().toString());
                }
                if (et_cgst_percentage_dialog_lay.getText().toString().isEmpty()) {
                    cgst = 0.0f;
                } else {
                    cgst = Float.valueOf(et_cgst_percentage_dialog_lay.getText().toString());
                }
                if (et_sgst_percentage_dialog_lay.getText().toString().isEmpty()) {
                    sgst = 0.0f;
                } else {
                    sgst = Float.valueOf(et_sgst_percentage_dialog_lay.getText().toString());
                }
                Float tax = cgst + sgst;
                Float resell_rate = resell * tax / 100;
                Float calc1 = resell + resell_rate;
                @SuppressLint("DefaultLocale") String i_rate_1 = String.format("%.2f", calc1);
                tv_resell_price_dialog.setText("Rs " + i_rate_1);
            }
        });
        iv_fav_dialog_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Drawable.ConstantState res = iv_fav_dialog_window.getDrawable().getConstantState();
                assert res != null;
                if (res.equals(Objects.requireNonNull(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()),
                        R.drawable.black_star)).getConstantState())) {
                    iv_fav_dialog_window.setImageResource(R.drawable.yellow_star);
                    favourites_value = 1;
                } else if (res.equals(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.yellow_star)).getConstantState())) {
                    iv_fav_dialog_window.setImageResource(R.drawable.black_star);
                    favourites_value = 0;
                }
            }
        });
        imgbtn_cross_dialog_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_window.dismiss();
            }
        });
        ib_dialog_1_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogButton_layout02();
            }
        });
        ib_dialog_1_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_window.dismiss();
            }
        });
        DialogButton_layout01();
        dialog_control_button_01_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogButton_layout01();
            }
        });
        dialog_control_button_02_lay.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                DialogButton_layout02();
            }
        });
        et_product_name_edittext_dialog_lay.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("ResourceType")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    spinner_category_dialog_lay.showDropDown();
                    spinner_category_dialog_lay.showContextMenu();
                    spinner_category_dialog_lay.setFocusable(true);

                    spin_cate_adapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_txt, c_name_list);
                    spinner_category_dialog_lay.setAdapter(spin_cate_adapter);
                    spinner_units_dialog_lay.requestFocus();
                }
                return true;
            }
        });
        dialog_window.show();
    }

    private void set_BM_NAME_dialog() {
        ArrayList<String> bm_list = new ArrayList<>();
        String select = "SELECT BM_NAME from BILL_MODE";// where I_NAME = '" + tv_product_name.getText().toString().trim() + "'";
        Cursor cursor_1 = MainActivity.db.rawQuery(select, null);
        if (cursor_1.moveToFirst()) {
            do {
                String name = cursor_1.getString(0);
                bm_list.add(name);
            } while (cursor_1.moveToNext());
        }
        cursor_1.close();
        if (bm_list.size() == 0) {
            constraintLayout_wholesale_dialog_lay.setVisibility(View.GONE);
            constraintLayout_retail_dialog_lay.setVisibility(View.GONE);
            constraintLayout_resell_dialog_lay.setVisibility(View.GONE);
            view4_bottom_wholesale_lay_diallog.setVisibility(View.GONE);
            view4_bottom_retail_lay_dialog_lay.setVisibility(View.GONE);

            tv_wholesale_price.setVisibility(View.GONE);
            tv_retail_price.setVisibility(View.GONE);
            tv_resell_price.setVisibility(View.GONE);

            tv_wholesale_price_dialog.setVisibility(View.GONE);
            tv_retail_price_dialog.setVisibility(View.GONE);
            tv_resell_price_dialog.setVisibility(View.GONE);
        }
        else if (bm_list.size() == 1) {
            tv_wholesale_table.setText(bm_list.get(0));
            tv_wholesale_price.setText(bm_list.get(0));

            constraintLayout_wholesale_dialog_lay.setVisibility(View.VISIBLE);
            constraintLayout_retail_dialog_lay.setVisibility(View.GONE);
            constraintLayout_resell_dialog_lay.setVisibility(View.GONE);
            view4_bottom_wholesale_lay_diallog.setVisibility(View.GONE);
            view4_bottom_retail_lay_dialog_lay.setVisibility(View.GONE);

            tv_wholesale_price.setVisibility(View.VISIBLE);
            tv_retail_price.setVisibility(View.GONE);
            tv_resell_price.setVisibility(View.GONE);

            tv_wholesale_price_dialog.setVisibility(View.VISIBLE);
            tv_retail_price_dialog.setVisibility(View.GONE);
            tv_resell_price_dialog.setVisibility(View.GONE);
        }
        else if (bm_list.size() == 2) {
            tv_wholesale_table.setText(bm_list.get(0));
            tv_retail_table.setText(bm_list.get(1));
            tv_wholesale_price.setText(bm_list.get(0));
            tv_retail_price.setText(bm_list.get(1));

            constraintLayout_wholesale_dialog_lay.setVisibility(View.VISIBLE);
            constraintLayout_retail_dialog_lay.setVisibility(View.VISIBLE);
            constraintLayout_resell_dialog_lay.setVisibility(View.GONE);
            view4_bottom_wholesale_lay_diallog.setVisibility(View.VISIBLE);
            view4_bottom_retail_lay_dialog_lay.setVisibility(View.VISIBLE);

            tv_wholesale_price.setVisibility(View.VISIBLE);
            tv_retail_price.setVisibility(View.VISIBLE);
            tv_resell_price.setVisibility(View.GONE);

            tv_wholesale_price_dialog.setVisibility(View.VISIBLE);
            tv_retail_price_dialog.setVisibility(View.VISIBLE);
            tv_resell_price_dialog.setVisibility(View.GONE);
        }
        else if (bm_list.size() == 3) {
            tv_wholesale_table.setText(bm_list.get(0));
            tv_retail_table.setText(bm_list.get(1));
            tv_resell_table.setText(bm_list.get(2));
            tv_wholesale_price.setText(bm_list.get(0));
            tv_retail_price.setText(bm_list.get(1));
            tv_resell_price.setText(bm_list.get(2));

            constraintLayout_wholesale_dialog_lay.setVisibility(View.VISIBLE);
            constraintLayout_retail_dialog_lay.setVisibility(View.VISIBLE);
            constraintLayout_resell_dialog_lay.setVisibility(View.VISIBLE);
            view4_bottom_wholesale_lay_diallog.setVisibility(View.VISIBLE);
            view4_bottom_retail_lay_dialog_lay.setVisibility(View.VISIBLE);

            tv_wholesale_price.setVisibility(View.VISIBLE);
            tv_retail_price.setVisibility(View.VISIBLE);
            tv_resell_price.setVisibility(View.VISIBLE);

            tv_wholesale_price_dialog.setVisibility(View.VISIBLE);
            tv_retail_price_dialog.setVisibility(View.VISIBLE);
            tv_resell_price_dialog.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void category_product_add_alertdialog_1() {
        category_product_add_alertdialog();
        imgbtn_yes_dialog_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tv_product_name.getText().toString().trim();
                String select_I_ID = "SELECT I_ID from ITEM where I_NAME='" + name + "'";
                Cursor cursor = MainActivity.db.rawQuery(select_I_ID, null);
                if (cursor.moveToFirst()) {
                    do {
                        I_ID = cursor.getString(0);
                    } while (cursor.moveToNext());
                }
                if (update_product_details()) {
                    set_i_rate_values();
                    dialog_window.dismiss();
                }
                cursor.close();
            }
        });
        et_wholesale_dialog_lay.setSelection(et_wholesale_dialog_lay.getText().toString().length());
        et_retail_dialog_lay.setSelection(et_retail_dialog_lay.getText().toString().length());
        et_resell_dialog_lay.setSelection(et_resell_dialog_lay.getText().toString().length());
        et_cgst_percentage_dialog_lay.setSelection(et_cgst_percentage_dialog_lay.getText().toString().length());
        et_sgst_percentage_dialog_lay.setSelection(et_sgst_percentage_dialog_lay.getText().toString().length());
    }

    private void set_BM_NAME() {
        ArrayList<String> bm_name_list = new ArrayList<>();
        bm_name_list.clear();
        String select = "SELECT BM_NAME from RATE where I_NAME = '" + tv_product_name.getText().toString().trim() + "'";
        Cursor cursor_1 = MainActivity.db.rawQuery(select, null);
        if (cursor_1.moveToFirst()) {
            do {
                String name = cursor_1.getString(0);
                bm_name_list.add(name);
            } while (cursor_1.moveToNext());
        }
        cursor_1.close();
        if (bm_name_list.size() == 0) {
            tv_wholesale.setText(" ");
            tv_retail.setText(" ");
            tv_resell.setText(" ");
            tv_final_wholesale.setText(" ");
            tv_final_retail.setText(" ");
            tv_final_resell.setText(" ");
        } else if (bm_name_list.size() == 1) {
            tv_wholesale.setText(bm_name_list.get(0));
            tv_final_wholesale.setText(bm_name_list.get(0));
            tv_retail.setText(" ");
            tv_resell.setText(" ");
            tv_final_retail.setText(" ");
            tv_final_resell.setText(" ");
        } else if (bm_name_list.size() == 2) {
            tv_wholesale.setText(bm_name_list.get(0));
            tv_retail.setText(bm_name_list.get(1));
            tv_final_wholesale.setText(bm_name_list.get(0));
            tv_final_retail.setText(bm_name_list.get(1));
        } else if (bm_name_list.size() == 3) {
            tv_wholesale.setText(bm_name_list.get(0));
            tv_retail.setText(bm_name_list.get(1));
            tv_resell.setText(bm_name_list.get(2));

            tv_final_wholesale.setText(bm_name_list.get(0));
            tv_final_retail.setText(bm_name_list.get(1));
            tv_final_resell.setText(bm_name_list.get(2));
        }
    }

    private void DialogButton_layout02() {
        constraint_price_and_tax_lay.setVisibility(View.VISIBLE);
        linear_basic_details_lay.setVisibility(View.GONE);
        dialog_control_button_02_lay.setBackgroundResource(R.drawable.onclick_button_curve_bg);
        tv_price_and_tax.setTextColor(Color.parseColor("#E0F010"));

        dialog_control_button_01_lay.setBackgroundColor(Color.parseColor("#E0F010"));
        tv_basic_details.setTextColor(Color.parseColor("#000000"));
        dialog_control_button_01_lay.setBackgroundResource(R.drawable.button_curve_bg);
    }

    private void DialogButton_layout01() {
        linear_basic_details_lay.setVisibility(View.VISIBLE);
        constraint_price_and_tax_lay.setVisibility(View.GONE);
        dialog_control_button_01_lay.setBackgroundResource(R.drawable.onclick_button_curve_bg);
        tv_basic_details.setTextColor(Color.parseColor("#E0F010"));

        dialog_control_button_02_lay.setBackgroundColor(Color.parseColor("#E0F010"));
        tv_price_and_tax.setTextColor(Color.parseColor("#000000"));
        dialog_control_button_02_lay.setBackgroundResource(R.drawable.button_curve_bg);
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
            //   Toast.makeText(getContext(), "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void get_category_details() {
        String selectQuery = "Select * from CATEGORY where C_NAME = '" + c_name + "'";
        Cursor cursor = MainActivity.db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String c_name = cursor.getString(1);
                String created_date = cursor.getString(2);
                String[] date_time = created_date.split(" ");
                String date = date_time[0].trim();
                category_name.setText(c_name);
                tv_created_date.setText(date);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void product_existing_alertdialog() {
        final Dialog d = new Dialog(Objects.requireNonNull(getContext()));
        d.setContentView(R.layout.product_add_existing_alert_dialog);
        d.setCancelable(false);
        Objects.requireNonNull(d.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextView tv_category_name = d.findViewById(R.id.existing_page_category_name_textview);
        tv_selected_product = d.findViewById(R.id.tv_selected_product);

        tv_category_name.setText(category_name.getText().toString());
        ImageButton ib_yes = d.findViewById(R.id.yes_imagebutton);
        ImageButton ib_no = d.findViewById(R.id.no_imagebutton);
        gridView = d.findViewById(R.id.gridview);

        ib_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        final ArrayList<String> arrayList = new ArrayList<>();
        String select = "Select I_NAME from ITEM";
        Cursor cursor = MainActivity.db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                String product_name = cursor.getString(0);
                arrayList.add(product_name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        final ArrayList<String> arrayList1 = new ArrayList<>();
        String select1 = "Select I_NAME from ITEM where C_NAME = '" + c_name + "'";
        Cursor cursor1 = MainActivity.db.rawQuery(select1, null);
        if (cursor1.moveToFirst()) {
            do {
                String product_name = cursor1.getString(0);
                arrayList1.add(product_name);

            } while (cursor1.moveToNext());
        }
        cursor1.close();
        Collections.sort(arrayList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        int position;
        final ArrayList<Integer> position_list = new ArrayList<>();
        for (int i = 0; i <= arrayList.size() - 1; i++) {
            for (int j = 0; j <= arrayList1.size() - 1; j++) {
                if (arrayList1.get(j).equals(arrayList.get(i))) {
                    position = i;
                    position_list.add(position);
                }
            }
        }
        final GridViewAdapter adapter = new GridViewAdapter(arrayList, getActivity(), position_list);
        gridView.setAdapter(adapter);
        adapter.selectedPositions.addAll(position_list);
        selectedStrings.addAll(arrayList1);
        tv_selected_product.setText(selectedStrings.size() + " Products Selected");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                selectedItem = String.valueOf(parent.getCount());
                position_list.clear();
                arrayList.clear();
                int selectedIndex = adapter.selectedPositions.indexOf(position);
                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) v).display(false);
                    selectedStrings.remove((String) parent.getItemAtPosition(position));
                } else {
                    adapter.selectedPositions.add(position);
                    ((GridItemView) v).display(true);
                    selectedStrings.add((String) parent.getItemAtPosition(position));
                }
                tv_selected_product.setText(selectedStrings.size() + " Products Selected");
            }
        });

        ib_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String select_C_ID = "SELECT C_ID from CATEGORY where C_NAME = '" + tv_category_name.getText().toString().trim() + "'";
                Cursor cursor = MainActivity.db.rawQuery(select_C_ID, null);
                if (cursor.moveToFirst()) {
                    do {
                        str_c_id = cursor.getString(0);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                ContentValues cv = new ContentValues();
                cv.put("C_NAME", "");
                cv.put("C_ID", "");
                for (int j_1 = 0; j_1 <= arrayList1.size() - 1; j_1++) {
                    MainActivity.db.update("ITEM", cv, "I_NAME='" + arrayList1.get(j_1) + "'", null);
                    DBExport();
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("C_NAME", tv_category_name.getText().toString());
                contentValues.put("C_ID", str_c_id);
                for (int j = 0; j <= selectedStrings.size() - 1; j++) {
                    MainActivity.db.update("ITEM", contentValues, "I_NAME='" + selectedStrings.get(j) + "'", null);
                    DBExport();
                }
                d.dismiss();
                selectedStrings.clear();
                create_listview();
                tv_no_products_for.setVisibility(View.GONE);
                product_listview.setVisibility(View.VISIBLE);
                create_product_list();
            }
        });
        d.show();
    }
}
