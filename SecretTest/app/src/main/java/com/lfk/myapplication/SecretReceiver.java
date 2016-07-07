package com.lfk.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SecretReceiver extends BroadcastReceiver {
    public SecretReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SpUtils.put(context, "key", 1);
    }
}
