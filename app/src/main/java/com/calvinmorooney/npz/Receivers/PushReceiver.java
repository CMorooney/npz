package com.calvinmorooney.npz.Receivers;

import android.content.Context;
import android.content.Intent;

import com.calvinmorooney.npz.Services.FloatingNippleService;
import com.parse.ParsePushBroadcastReceiver;

public class PushReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushReceive(Context context, Intent intent)
    {
        context.startService(new Intent(context, FloatingNippleService.class));
    }
}
