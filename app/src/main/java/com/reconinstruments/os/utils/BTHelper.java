package com.reconinstruments.os.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by recom3 on 09/05/2024.
 */

public class BTHelper {
    public enum DeviceType {
        ANDROID,
        IOS
    }

    private static final String TAG = BTHelper.class.getSimpleName();
    private static BTHelper instance = null;
    private Context mContext;
    private Handler mapHandler = new Handler();
    private Runnable connectMapSS1_runnable = new Runnable() { // from class: com.reconinstruments.utils.BTHelper.1
        @Override // java.lang.Runnable
        public void run() {
            Log.i(BTHelper.TAG, "connectMapSS1");
            Intent i = new Intent("RECON_SS1_MAP_COMMAND");
            i.putExtra("command", 500);
            i.putExtra("address", BTHelper.this.getLastPairedDeviceAddress());
            BTHelper.this.mContext.sendBroadcast(i);
        }
    };

    protected BTHelper(Context context) {
        this.mContext = context;
    }

    public static BTHelper getInstance(Context context) {
        if (instance == null) {
            instance = new BTHelper(context);
        }
        return instance;
    }

    public int getBTConnectionState() {
        return getBTConnectionState(this.mContext);
    }

    public static int getBTConnectionState(Context context) {
        try {
            int res = Settings.System.getInt(context.getContentResolver(), "BTConnectionState");
            return res;
        } catch (Settings.SettingNotFoundException e) {
            return 0;
        }
    }

    public static boolean isConnected(Context context) {
        return getBTConnectionState(context) == 2;
    }

    public int getLastPairedDeviceType() {
        try {
            int res = Settings.System.getInt(this.mContext.getContentResolver(), "LastPairedDeviceType");
            return res;
        } catch (Settings.SettingNotFoundException e) {
            return 0;
        }
    }

    public String getLastPairedDeviceAddress() {
        String deviceAddress = Settings.System.getString(this.mContext.getContentResolver(), "LastPairedDeviceAddress");
        if (deviceAddress == null) {
            return "";
        }
        return deviceAddress;
    }

    public String getLastPairedDeviceName() {
        String deviceName = Settings.System.getString(this.mContext.getContentResolver(), "LastPairedDeviceName");
        if (deviceName == null) {
            return "";
        }
        return deviceName;
    }

    public void setLastPairedDeviceType(int deviceType) {
        Settings.System.putInt(this.mContext.getContentResolver(), "LastPairedDeviceType", deviceType);
    }

    public void setLastPairedDeviceAddress(String deviceAddress) {
        Settings.System.putString(this.mContext.getContentResolver(), "LastPairedDeviceAddress", deviceAddress);
    }

    public void setLastPairedDeviceName(String deviceName) {
        Settings.System.putString(this.mContext.getContentResolver(), "LastPairedDeviceName", deviceName);
    }

    public void disconnect() {
        Log.i(TAG, "Send disconnect request to HUDService");
        Intent i = new Intent("com.reconinstruments.mobilesdk.hudconnectivity.request.disconnect");
        int deviceType = getLastPairedDeviceType();
        if (deviceType == 1) {
            i.putExtra("deviceType", DeviceType.IOS.name());
        } else {
            i.putExtra("deviceType", DeviceType.ANDROID.name());
        }
        this.mContext.sendBroadcast(i);
    }

    public void reconnect() {
        Log.i(TAG, "Send connect request to HUDService");
        Intent i = new Intent("com.reconinstruments.mobilesdk.hudconnectivity.connect");
        i.putExtra("address", getLastPairedDeviceAddress());
        int deviceType = getLastPairedDeviceType();
        if (deviceType == 1) {
            i.putExtra("deviceType", DeviceType.IOS.name());
        } else {
            i.putExtra("deviceType", DeviceType.ANDROID.name());
        }
        i.putExtra("attempts", 0);
        this.mContext.sendBroadcast(i);
    }

    public void restartHUDService() {
        Log.i(TAG, "Send kill request to HUDService");
        Intent i = new Intent("com.reconinstruments.mobilesdk.hudconnectivity.kill");
        this.mContext.sendBroadcast(i);
    }

    public void reEnableMapSS1() {
        disconnectMapSS1();
        connectMapSS1_delayed();
    }

    private void disconnectMapSS1() {
        Log.i(TAG, "disconnectMapSS1");
        Intent i = new Intent("RECON_SS1_MAP_COMMAND");
        i.putExtra("command", 600);
        this.mContext.sendBroadcast(i);
    }

    private void connectMapSS1_delayed() {
        Log.i(TAG, "connecting with Map with a delay of 5 seconds");
        /*
        this.mapHandler.removeCallbacks(this.connectMapSS1_runnable);
        this.mapHandler.postDelayed(this.connectMapSS1_runnable, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
        */
    }
}
