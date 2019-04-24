package org.thoughtcrime.securesms.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import org.thoughtcrime.securesms.logging.Log;
import org.thoughtcrime.securesms.sms.OutgoingSMSObserver;

public class SmsSentListener extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        OutgoingSMSObserver outgoingSMSObserver = new OutgoingSMSObserver(new Handler(), getApplicationContext());
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms/"), true, outgoingSMSObserver);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Log.d("SmsSentListener", "Service SmsSentListener was successfully started.");
    }
}
