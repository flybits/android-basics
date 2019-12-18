package com.flybits.samples.android.basics.services;

import com.flybits.android.push.models.newPush.DisplayablePush;
import com.flybits.android.push.models.newPush.Push;
import com.flybits.android.push.services.PushService;
import com.flybits.samples.android.basics.BasicsApplication;
import com.flybits.samples.android.basics.R;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class BasicsFCMReceiver extends PushService {

    @NotNull
    @Override
    public String getNotificationChannelId(@NotNull Push push) {
        return BasicsApplication.NOTIFICATION_CHANNEL_FLYBITS;
    }

    @Override
    public int getNotificationIconRes(@NotNull DisplayablePush displayablePush) {
        return R.mipmap.ic_flybits_notification;
    }

    @Override
    public void onNonFlybitsPushReceived(@NotNull RemoteMessage remoteMessage) {
        //Implement logic if the application receives non-Flybits push notification
    }

    @Override
    public void onDisplayableFlybitsPushReceived(@NotNull DisplayablePush push) {
        super.onDisplayableFlybitsPushReceived(push);
        //In case you want to use your own logic to display push notifications - remove super from above and implement logic
    }
}
