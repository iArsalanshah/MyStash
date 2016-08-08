package com.citemenu.mystash.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.citemenu.mystash.R;
import com.citemenu.mystash.helper.Constant_util;
import com.citemenu.mystash.login_pages.Login_activity;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGCMListenerService extends GcmListenerService {

    private static final String TAG = "MyGCMListenerService";
    private SharedPreferences sharedPreferences;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        sharedPreferences = getSharedPreferences(Constant_util.PREFS_NAME, 0);
        String message = data.getString("message");
        String msgType = data.getString("type");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "Booking ID: " + msgType);

//        if (from.startsWith("/topics/")) {
//            // message received from some topic.
//        } else {
//            // normal downstream message.
//        }
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.common_plus_signin_btn_icon_light)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!");
//
//        Intent resultIntent = new Intent(this, MainActivity.class);
//// Because clicking the notification opens a new ("special") activity, there's
//// no need to create an artificial back stack.
//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        this,
//                        0,
//                        resultIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);
//        int mNotificationId = 001;
//        NotificationManager mNotifyMgr =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message, msgType);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     * @param msgType
     */
    private void sendNotification(String message, String msgType) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent intent = null;
        try {

            if (!sharedPreferences.getString(Constant_util.IS_LOGIN, "").equals(Constant_util.IS_LOGIN)) {
                intent = new Intent(this, Login_activity.class);
                Log.d(TAG, "Login");
            } else {
                if (msgType.equals("message"))
                    intent = new Intent(this, com.citemenu.mystash.home.Messages.class);
                else {
                    if (com.citemenu.mystash.home.mystash_box.Program_Details.activity) {
                        intent = new Intent("your_action");
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    } else {
                        intent = new Intent(this, com.citemenu.mystash.home.MainActivity.class);
                    }
                }
                Log.d(TAG, "Home");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                0); //PendingIntent.FLAG_ONE_SHOT
//
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                //.setSmallIcon(R.drawable.ic_stat_ic_notification)
//                .setContentTitle("GCM Message")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(getResources().getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle("MyStash")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notification);
    }
}