package com.pockeyt.cordova.plugin;

import android.annotation.SuppressLint;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

public class BeaconHandler {
    private static final String TAG = BeaconHandler.class.getSimpleName();
    private String uuid;
    private BeaconTransmitter mBeaconTransmitter;

    BeaconHandler(String uuid) {
        this.uuid = uuid;
    }

    public void start(Context context) {
        Runnable runnable = () -> startBeacon(context);
        Thread thread = new Thread(runnable);
        thread.setName("Pockeyt Beacon Thread");
        thread.start();
    }

    public void stop() {
        if (mBeaconTransmitter != null) {
            mBeaconTransmitter.stopAdvertising();
        }
    }

    @SuppressLint("NewApi")
    private void startBeacon(Context context) {
        Beacon beacon = setBeacon();
        BeaconParser beaconParser = setBeaconParser();
        mBeaconTransmitter = setBeaconTransmitter(context, beaconParser);

        mBeaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.d(TAG, "Started advertising success");
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e(TAG, "Error: " + errorCode);
                super.onStartFailure(errorCode);
            }
        });
    }






    private BeaconTransmitter setBeaconTransmitter(Context context, BeaconParser beaconParser) {
        return new BeaconTransmitter(context, beaconParser);
    }

    private BeaconParser setBeaconParser() {
        return new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
    }

    private Beacon setBeacon() {
        return new Beacon.Builder()
                .setId1(this.uuid)
                .setId2("2")
                .setId3("3")
                .setManufacturer(0x004C)
                .build();
    }
}