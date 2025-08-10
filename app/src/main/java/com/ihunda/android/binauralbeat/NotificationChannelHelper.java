/*  NotificationChannelHelper.java  */
package com.ihunda.android.binauralbeat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public final class NotificationChannelHelper {

    /** Single, low‑importance channel for continuous playback */
    private static final String CHANNEL_ID   = "bbeat_playback";

    /**
     *  Ensures that the default channel exists and returns its ID.
     *  Safe to call on every service start.
     */
    public static String getDefaultChannel(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm =
                    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel ch = new NotificationChannel(
                        CHANNEL_ID,
                        ctx.getString(R.string.notif_started),   // e.g. “Binaural Beats”
                        NotificationManager.IMPORTANCE_LOW);
                ch.setDescription(ctx.getString(R.string.notif_descr, "JENLA")); // “Keeps the session running”
                ch.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);
                nm.createNotificationChannel(ch);
            }
        }
        return CHANNEL_ID;
    }

    private NotificationChannelHelper() { /* no‑instance */ }
}