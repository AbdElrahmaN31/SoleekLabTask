package com.abdelrahman.soleeklabtask.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class IntentUtil {

    private IntentUtil() {
    }

    public static void makeIntent(Context currentActivity, Class targetActivity) {
        Intent intent = new Intent(currentActivity, targetActivity);
        currentActivity.startActivity(intent);
        ((Activity) currentActivity).finish();
    }
}
