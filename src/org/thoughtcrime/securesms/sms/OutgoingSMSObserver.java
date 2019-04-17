package org.thoughtcrime.securesms.sms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.thoughtcrime.securesms.ApplicationContext;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.RecipientDatabase;
import org.thoughtcrime.securesms.database.SmsDatabase;
import org.thoughtcrime.securesms.jobmanager.JobManager;
import org.thoughtcrime.securesms.jobs.SmsReceiveJob;
import org.thoughtcrime.securesms.jobs.SmsSentJob;
import org.thoughtcrime.securesms.recipients.Recipient;

public class OutgoingSMSObserver extends ContentObserver {

    private Context context;
    private String lastString;
    public static final String DELIVERED_SMS_ACTION = "org.thoughtcrime.securesms.SendReceiveService.DELIVERED_SMS_ACTION";


    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public OutgoingSMSObserver(Handler handler) {
        super(handler);
    }

    public OutgoingSMSObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
        this.lastString = "";
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Uri smsURI = Uri.parse("content://sms");
        Cursor cursor = context.getApplicationContext().getContentResolver().query(smsURI, null, null, null, null);
        cursor.moveToNext();
        String smsNumber = cursor.getString(cursor.getColumnIndex("address"));
        String message = cursor.getString(cursor.getColumnIndex("body"));
        String id = cursor.getString(cursor.getColumnIndex("_id"));
        if (smsNumber == null || smsNumber.length() <= 0) {
            smsNumber = "Unknown";
        }
        Log.d("OutgoingSMSObserver", "onChange() was called with type " + cursor.getInt(cursor.getColumnIndex("type")));
        if (cursor.getInt(cursor.getColumnIndex("type")) == 2 && !id.matches(lastString)) {
            Log.d("OutgoingSMSObserver", "Tried to post a sent message. with id " + id);

            // TODO Figure out how to handle this business
            Log.d("OutgoingSMSObserver", message + " #################");

            long messageId = cursor.getLong(cursor.getColumnIndex("_id"));
            //Recipient =
            //OutgoingTextMessage outgoingTextMessage = new OutgoingTextMessage();

            //ApplicationContext.getInstance(context).getJobManager().add(new SmsSentJob(context, messageId, DELIVERED_SMS_ACTION, result, 1));
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //DatabaseFactory.getSmsDatabase().insertMessageOutbox(messageId, )

            lastString = id;
        }
        cursor.close();
    }
}
