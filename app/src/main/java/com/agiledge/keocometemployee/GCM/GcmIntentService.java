package com.agiledge.keocometemployee.GCM;///*
// * Copyright (C) 2013 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.agiledge.field_serv.GCM;
//
//import android.app.IntentService;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.agiledge.field_serv.Activities.JobList;
//import com.agiledge.field_serv.R;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//
//import org.json.JSONObject;
//
///**
// * This {@code IntentService} does the actual handling of the GCM message.
// * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
// * partial wake lock for this service while the service does its work. When the
// * service is finished, it calls {@code completeWakefulIntent()} to release the
// * wake lock.
// */
//public class GcmIntentService extends IntentService {
//
//    public static final int NOTIFICATION_ID = 1;
//    private NotificationManager mNotificationManager;
//    NotificationCompat.Builder builder;
//
//    public GcmIntentService() {
//        super("GcmIntentService");
//    }
//    public static final String TAG = "GCM Demo";
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        Bundle extras = intent.getExtras();
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
//        // The getMessageType() intent parameter must be the intent you received
//        // in your BroadcastReceiver.
//        String messageType = gcm.getMessageType(intent);
//
//        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
//            /*
//             * Filter messages based on message type. Since it is likely that GCM will be
//             * extended in the future with new message types, just ignore any message types you're
//             * not interested in, or that you don't recognize.
//             */
//            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                sendNotification("Send error: " + extras.toString());
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendNotification("Deleted messages on server: " + extras.toString());
//            // If it's a regular GCM message, do some work.
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
////
////                // This loop represents the service doing some work.
////                for (int i = 0; i < 5; i++) {
////                    Log.i(TAG, "Working... " + (i + 1)
////                            + "/5 @ " + SystemClock.elapsedRealtime());
////                    try {
////
////                        Thread.sleep(5000);
////
////                    } catch (InterruptedException e) {
////
////                    }
////                }
////                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
//                String msg=extras.getString("data");
//
//                    // Post notification of received message.
//                    sendNotification(msg);
//
//                Log.i(TAG, "Received: " + msg);
//            }
//        }
//        // Release the wake lock provided by the WakefulBroadcastReceiver.
//        GcmBroadcastReceiver.completeWakefulIntent(intent);
//    }
//
//    // Put the message into a notification and post it.
//    // This is just one simple example of what you might choose to do with
//    // a GCM message.
//    private void sendNotification(String msg) {
//        try {
//          //  Toast.makeText(getApplicationContext(),"msgggg"+msg,Toast.LENGTH_LONG).show();
//
//            JSONObject json = new JSONObject(msg);
//            String heading=json.getString("TYPE");
//            String content=json.getString("MESSAGE");
//                    mNotificationManager = (NotificationManager)
//                    this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                    new Intent(this, JobList.class), 0);
//
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.agile)
//                    .setContentTitle(heading)
//                    .setStyle(new NotificationCompat.BigTextStyle()
//                            .bigText(heading))
//                    .setContentText(content);
//
//            mBuilder.setContentIntent(contentIntent);
//            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//        }catch(Exception e){e.printStackTrace();}
//
//    }
//    /**
//     * Issues a notification to inform the user that server has sent a message.
//     */
//    private static void generateNotification(Context context, String message) {
//        int icon = R.drawable.agile;
//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(icon, "Dear Engineer, New Job is Scheduled", when);
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        notification.sound=soundUri;
//        String title = context.getString(R.string.app_name);
//        Intent notificationIntent = new Intent(context, JobList.class);
//        notificationIntent.putExtra("message", message);
//        // set intent so it does not start a new activity
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent =
//                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        //notification.setLatestEventInfo(context, title, message, intent);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notificationManager.notify(0, notification);
//    }
//}
//
