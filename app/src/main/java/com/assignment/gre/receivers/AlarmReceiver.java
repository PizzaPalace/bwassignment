package com.assignment.gre.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.assignment.gre.services.AlarmService;

/**
 * Created by rahul on 25-12-2015.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context, AlarmService.class);
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, service);
    }
}
