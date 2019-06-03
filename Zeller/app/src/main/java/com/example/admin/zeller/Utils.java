package com.example.admin.zeller;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void ShortToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void LongToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
