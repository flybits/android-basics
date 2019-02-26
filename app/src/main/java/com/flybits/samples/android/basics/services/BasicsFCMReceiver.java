package com.flybits.samples.android.basics.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.flybits.android.push.FlybitsNotificationManager;
import com.flybits.android.push.PushManager;
import com.flybits.android.push.exceptions.FlybitsPushException;
import com.flybits.android.push.models.Push;
import com.flybits.commons.library.api.results.callbacks.ObjectResultCallback;
import com.flybits.commons.library.exceptions.FlybitsException;
import com.flybits.samples.android.basics.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class BasicsFCMReceiver extends FirebaseMessagingService {

    private static final String NOTIFICATION_CHANNEL_FLYBITS = "com.flybits.push.channel.generic";
    private static final String _TAG = "FCMReceiver";

    @Override
    public void onMessageReceived(final RemoteMessage message) {

        Log.d(_TAG, "FCM Message Received: " + message.getData().toString());

        final Map data = message.getData();
        /************************************************************************
         * SETUP: Step 1
         *
         * Parse the Push Notification and verify it is a Push Notification coming
         * from Flybits
         ***********************************************************************/
        PushManager.parsePushNotification(getBaseContext(), data, new ObjectResultCallback<Push>() {
            @Override
            public void onSuccess(Push push) {
                /************************************************************************
                 * Valid Flybits Push Notification
                 ***********************************************************************/
                displayPushSimple(getApplicationContext(), push);
            }

            @Override
            public void onException(FlybitsException e) {
                if (e instanceof FlybitsPushException) {
                    /************************************************************************
                     * SETUP: Optional Step
                     *
                     * This is not a Flybits Push Notification. It was probably caused by another
                     * Push service. This should be handled in whichever manner the other Push service
                     * in your application indicates within their documentation.
                     ***********************************************************************/
                }
            }
        });
    }

    /************************************************************************
     * Simple Push Display Through Flybits
     ***********************************************************************/
    private void displayPushSimple(Context context, Push push){

        FlybitsNotificationManager manager = new FlybitsNotificationManager.Simplifier(context, push, R.mipmap.ic_flybits_notification)
                .build();

        manager.show();
    }

    /************************************************************************
     * Custom Push Display Through Flybits
     ***********************************************************************/
    private void displayPush(Push push) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        long[] vibration = new long[1];
        vibration[0] = 100L;

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(push.getMessage());

        String txtTitle = (push.getTitle() != null) ? push.getTitle() : getString(R.string.pushEmptyTitle);
        bigText.setBigContentTitle(txtTitle);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_FLYBITS)
                        .setContentText(push.getMessage())
                        .setSmallIcon(R.mipmap.ic_flybits_notification)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setVibrate(vibration)
                        .setStyle(bigText)
                        .setWhen(System.currentTimeMillis())
                        .setShowWhen(true)
                        .setAutoCancel(true);

        if (push.getTitle() != null) {
            mBuilder.setContentTitle(txtTitle);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_FLYBITS, getString(R.string.pushChannel),
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            mChannel.enableLights(true);
            mChannel.enableVibration(true);

            // Configure the notification channel.
            mNotificationManager.createNotificationChannel(mChannel);
        }

        int num = (int) System.currentTimeMillis();
        mNotificationManager.notify(num, mBuilder.build());
    }
}
