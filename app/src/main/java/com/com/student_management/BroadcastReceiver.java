package com.com.student_management;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    private static final String TAG = "BroadCastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("users")) {
            Log.d(TAG, "onReceive: create user success");
            context.sendBroadcast(new Intent("users"));
        }
    }
}
