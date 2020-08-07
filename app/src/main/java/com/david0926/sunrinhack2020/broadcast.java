package com.david0926.sunrinhack2020;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class broadcast extends BroadcastReceiver {
    String alarm_hour,alarm_minute;
    @Override
    public void onReceive(Context context, Intent intent) {

        alarm_hour = intent.getStringExtra("alarm_time_hour");
        alarm_minute = intent.getStringExtra("alarm_time_minute");

        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.putExtra("alarm_time_hour",alarm_hour);
        intent1.putExtra("alarm_time_minute",alarm_minute);



    }
}
