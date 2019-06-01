package com.abdelrahman.soleeklabtask.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.abdelrahman.soleeklabtask.R;

public class SnackBarUtil {

    private static Snackbar snackbar;

    private SnackBarUtil() {
    }

    public static void makeSnackBar(Context context, View view, String message, int duration) {
        if (snackbar != null) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
            snackbar = null;
        }
        ;
        snackbar.make(view, message, duration)
                .setActionTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .show();
    }
}
