package com.pockeyt.cordova.plugin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class BeaconService extends Service {
    private static final String TAG = BeaconService.class.getSimpleName();
    public static final String POCKEYT_BEACON_SERVICE = "pockeyt_beacon_service";
    public static final String POCKEYT_BEACON_CHANNEL = "pockeyt_beacon_channel";
    private boolean mRunning;
    private BeaconHandler mBeaconHandler;
    private IBinder beaconBinder = new BeaconBinder();

    private class BeaconBinder extends Binder {}

    @Override
    public void onCreate() {
        super.onCreate();
        mRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mRunning && intent != null) {
            mRunning = true;
            createNotificationChannel();
            createForegroundNotification();

            mBeaconHandler = new BeaconHandler(intent.getStringExtra(BeaconPlugin.KEY_UUID));
            mBeaconHandler.start(this);
            return Service.START_STICKY;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mRunning && mBeaconHandler != null) {
            mRunning = false;
            mBeaconHandler.stop();
            deleteNotificationChannel();
        }
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(POCKEYT_BEACON_CHANNEL, "Pockeyt Beacon", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Pockeyt Beacon is running");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void deleteNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.deleteNotificationChannel(POCKEYT_BEACON_CHANNEL);
        }
    }

    private void createForegroundNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, POCKEYT_BEACON_SERVICE);
        builder.setSmallIcon(getResources().getIdentifier("ic_stat_beacon", "drawable", getPackageName()));
        builder.setContentTitle("Pockeyt Beacon");
        builder.setContentText("Pockeyt Beacon Running");
        Notification notification = builder.build();

        startForeground(-1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return beaconBinder;
    }
}
