package com.assignment.gre.common;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;

import com.assignment.gre.receivers.AlarmReceiver;
import com.assignment.gre.receivers.BootReceiver;

/**
 * Created by rahul on 25-12-2015.
 */
public class AlarmUtil {

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager mAlarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent mAlarmIntent;

    // BEGIN_INCLUDE(set_alarm)
    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     */
    public void setAlarm(Context context) {
        mAlarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        mAlarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        mAlarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                      SystemClock.elapsedRealtime() +
                7*1000, mAlarmIntent);

        mAlarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                  AlarmManager.INTERVAL_HOUR,
        AlarmManager.INTERVAL_DAY, mAlarmIntent);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (mAlarmMgr!= null) {
            mAlarmMgr.cancel(mAlarmIntent);
        }

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context,BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
