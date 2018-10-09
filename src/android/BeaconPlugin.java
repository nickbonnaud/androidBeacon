package com.pockeyt.cordova.plugin;

import android.content.Intent;
import android.util.Log;

import org.altbeacon.beacon.BeaconTransmitter;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BeaconPlugin extends CordovaPlugin {
    public static final String KEY_UUID = "key_uuid";
    private static final String TAG = BeaconPlugin.class.getSimpleName();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        if (BeaconTransmitter.checkTransmissionSupported(cordova.getContext()) != BeaconTransmitter.SUPPORTED) {
            callbackContext.error("Error: Transmitting not supported by this device");
            return false;
        }

        if (action.equals("start")) {
            return doStart(args, callbackContext);
        }

        if (action.equals("stop")) {
            return doStop(callbackContext);
        }

        callbackContext.error(action + " " + "is not a recognized action");
        return false;

    }

    private boolean doStart(JSONArray args, CallbackContext callbackContext) {
        String uuid;
        try {
            JSONObject options = args.getJSONObject(0);
            uuid = options.getString("uuid");
        } catch (JSONException e) {
            callbackContext.error("Error: " + e.getMessage());
            return false;
        }

        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        callbackContext.sendPluginResult(pluginResult);
        return startBeaconService(uuid);
    }

    private boolean doStop(CallbackContext callbackContext) {
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        callbackContext.sendPluginResult(pluginResult);
        return stopBeaconService();
    }

    private boolean startBeaconService(String uuid) {
        Intent intent = new Intent(cordova.getContext(), BeaconService.class);
        intent.putExtra(KEY_UUID, uuid);
        cordova.getActivity().startService(intent);
        return true;
    }

    private boolean stopBeaconService() {
        Intent intent = new Intent(cordova.getContext(), BeaconService.class);
        cordova.getActivity().stopService(intent);
        return true;
    }
}