package com.assignment.gre.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.assignment.gre.common.AlarmUtil;

/**
 * Created by rahul on 25-12-2015.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            AlarmUtil util = new AlarmUtil();
            util.setAlarm(context);
        }
    }
}
