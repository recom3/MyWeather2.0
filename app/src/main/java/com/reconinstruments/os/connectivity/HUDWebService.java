package com.reconinstruments.os.connectivity;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.myweather.MainActivity;
import com.reconinstruments.os.connectivity.bluetooth.HUDSPPService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by Recom on 14/01/2023.
 * This library is missing the method push
 * (check the HUDConnectivityService service in Snow2 project to see how it works)
 * The push method should be used to push messages to the phone, using the:
 *  Bluetooth Serial Port HudSPPService? Maybe the Bluetooth Chat?
 *  or
 *  Using the Object/Command channels?
 *
 *  Contains:
 *  HUDConnectivityManager that contains IHUDConnectivityConnection that is implemented by
 *      HUDConnectivityPhoneConnection that contains the
 *          HUDSPPService
 *  HUDConnectivityManager has inside the queues mCommandQueue, File, etc
 *
 *  Contains and call (its only called on destroy!):
 *  HUDConnectivityManager$ConnectivityHandler what does this do has be analyzed
 *      Has a methos that return public final WeakReference<IHUDConnectivity>
 *  but also in HUDConnectivityManager constructor:
 *  public HUDConnectivityManager() {
 *       this.a = new HUDConnectivityManager$ConnectivityHandler(this, (byte)0);
 *  }
 */

public class HUDWebService extends Service {
    private static final String a = HUDWebService.class.getName();
    private static final String f2143a = HUDWebService.class.getName();

    //private HUDStateUpdateListener b;
    private HUDStateUpdateListener hudStateUpdateListener;

    //private HUDConnectivityManager c;
    public HUDConnectivityManager hudConnectivityManager;

    private final IHUDConnectivity d = new IHUDConnectivity() {
        @Override
        public void onConnectionStateChanged(ConnectionState paramConnectionState) {

        }

        @Override
        public void onNetworkEvent(NetworkEvent paramNetworkEvent, boolean paramBoolean) {

        }

        @Override
        public void onDeviceName(String paramString) {

        }

        public final void a(IHUDConnectivity.ConnectionState param1ConnectionState) {
            Log.i(HUDWebService.this.a, "onConnectionStateChanged(): " + param1ConnectionState);
        }

        public final void a(IHUDConnectivity.NetworkEvent param1NetworkEvent, boolean param1Boolean) {
            Log.i(HUDWebService.this.a, "onNetworkEvent(): " + param1NetworkEvent + ", hasNetworkAccess: " + param1Boolean);
        }

        public final void a(String param1String) {
            Log.i(HUDWebService.this.a, "onDeviceName(): " + param1String);
        }
    };

    public static class AnonymousClass3 {

        static final int[] f2147a = new int[HUDStateUpdateListener.HUD_STATE.values().length];

        static {
            try {
                f2147a[HUDStateUpdateListener.HUD_STATE.CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f2147a[HUDStateUpdateListener.HUD_STATE.CONNECTING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f2147a[HUDStateUpdateListener.HUD_STATE.DISCONNECTED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public HUDWebService() {

        /*
        hudStateUpdateListener = new HUDStateUpdateListener() {
            @Override
            public void a(HUD_STATE paramHUDStateUpdateListener$HUD_STATE) {

            }
        };
        */

        /*
        super.onCreate();
        Log.i(f2143a, "onCreate()");
        this.hudConnectivityManager = new HUDConnectivityManager();
        this.hudStateUpdateListener = new HUDStateUpdateListener() { // from class: com.reconinstruments.jetandroid.services.HUDWebService.1
            @Override // com.reconinstruments.mobilesdk.hudconnectivity.HUDStateUpdateListener
            public final void a(HUDStateUpdateListener.HUD_STATE hud_state) {
                HUDWebService.a(HUDWebService.this, hud_state);
            }
        };
        this.hudStateUpdateListener.a(this);
        */
    }

    /**
     * This is the responding to the event in update listener
     * @param hud_state
     */
    static void a(HUDWebService hUDWebService, HUDStateUpdateListener.HUD_STATE hud_state) {
        String tag = a;
        Log.i(tag, "updateState" + hud_state);
        switch (AnonymousClass3.f2147a[hud_state.ordinal()]) {
            case 1:
                try {
                    HUDConnectivityManager hUDConnectivityManager = hUDWebService.hudConnectivityManager;
                    hUDConnectivityManager.connectivityHandler = new HUDConnectivityManager.ConnectivityHandler(hUDConnectivityManager, (byte) 0);
                    hUDConnectivityManager.hudConnectivityConnection = new HUDConnectivityPhoneConnection(hUDWebService, hUDConnectivityManager);

                    boolean useNewBTConn = true;

                    if(useNewBTConn) {
                        hUDConnectivityManager.hudConnectivityConnection.startListening();
                        HUDConnectivityManager hUDConnectivityManager2 = hUDWebService.hudConnectivityManager;
                        IHUDConnectivity iHUDConnectivity = hUDWebService.d;
                        if (hUDConnectivityManager2.connectivityHandler != null) {
                            if (iHUDConnectivity == null) {
                                throw new IllegalArgumentException("Interface IHUDConnectivity you registered is null");
                            }
                            if (hUDConnectivityManager2.connectivityHandler.addIHUDConnectivity(iHUDConnectivity)) {
                                iHUDConnectivity.onDeviceName(hUDConnectivityManager2.mDeviceName);
                                iHUDConnectivity.onConnectionStateChanged(hUDConnectivityManager2.connectionState);
                                iHUDConnectivity.onNetworkEvent(hUDConnectivityManager2.mHasLocalWeb ? IHUDConnectivity.NetworkEvent.LOCAL_WEB_GAINED : IHUDConnectivity.NetworkEvent.LOCAL_WEB_LOST, hUDConnectivityManager2.hasNetworkAccess());
                                iHUDConnectivity.onNetworkEvent(hUDConnectivityManager2.mHasRemoteWeb ? IHUDConnectivity.NetworkEvent.REMOTE_WEB_GAINED : IHUDConnectivity.NetworkEvent.REMOTE_WEB_LOST, hUDConnectivityManager2.hasNetworkAccess());
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    else
                    {

                    }
                } catch (Exception e) {
                    Log.i(f2143a, "Failed to init HUDConnectivityManager", e);
                    return;
                }
            case 2:
            default:
                return;
            case 3:
                hUDWebService.b();
                return;
        }
    }

    /**
     * Destroy called code?
     */
    private void b() {
        WeakReference<IHUDConnectivity> a2;
        try {
            this.hudConnectivityManager.hudConnectivityConnection.stopListening();
        } catch (Exception e) {
            Log.i(f2143a, "Failed to stop HUDConnectivityManager", e);
        }
        HUDConnectivityManager hUDConnectivityManager = this.hudConnectivityManager;
        IHUDConnectivity iHUDConnectivity = this.d;
        if (iHUDConnectivity == null) {
            throw new IllegalArgumentException("Interface IHUDConnectivity you unregistered is null");
        }
        HUDConnectivityManager.ConnectivityHandler connectivityHandler = hUDConnectivityManager.connectivityHandler;
        if (iHUDConnectivity == null || (a2 = connectivityHandler.getIHUDConnectivity(iHUDConnectivity)) == null) {
            return;
        }
        synchronized (connectivityHandler.mWeakHUDConnectivityArr) {
            connectivityHandler.mWeakHUDConnectivityArr.remove(a2);
        }
    }

    public IBinder onBind(Intent paramIntent) {

        return this.mBinder;
    }

    public void onCreate() {
        super.onCreate();
        Log.i(f2143a, "onCreate()");

        boolean useNewBTConn = true;

        boolean isNewImpl = useNewBTConn;

        if(!isNewImpl) {
            this.hudConnectivityManager = new HUDConnectivityManager();
        }
        else {
            try {
                UUID phoneRequestUUID = UUID.fromString(HUDSPPService.g[0]);

                UUID hudRequestUUID = UUID.fromString(HUDSPPService.g[1]);

                this.hudConnectivityManager = new HUDConnectivityManager(this, null, true, true,
                        "com.recom3.snow2liftie",
                        //hudRequestUUID, phoneRequestUUID
                        phoneRequestUUID, hudRequestUUID
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.hudStateUpdateListener = new HUDStateUpdateListener() { // from class: com.reconinstruments.jetandroid.services.HUDWebService.1
            @Override // com.reconinstruments.mobilesdk.hudconnectivity.HUDStateUpdateListener
            public final void a(HUDStateUpdateListener.HUD_STATE hud_state) {
                HUDWebService.a(HUDWebService.this, hud_state);
            }
        };
        this.hudStateUpdateListener.a(this);

        //Rec3: 15.05.2023
        boolean bForce = true;
        if(bForce)
        {
            connect();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(a, "onDestroy()");
        unregisterReceiver((BroadcastReceiver)this.hudStateUpdateListener);
        b();
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        Log.i(a, "onStartCommand()");
        //return 1;
        return START_STICKY;
    }

    private final IBinder mBinder = (IBinder)new LocalBinder();

    public class LocalBinder extends Binder {
        public HUDWebService getService() {
            return HUDWebService.this;
        }
    }

    public void connect()
    {
        try {
            HUDConnectivityManager hUDConnectivityManager = this.hudConnectivityManager;
            hUDConnectivityManager.connectivityHandler = new HUDConnectivityManager.ConnectivityHandler(hUDConnectivityManager, (byte) 0);
            hUDConnectivityManager.hudConnectivityConnection = new HUDConnectivityPhoneConnection(this, hUDConnectivityManager);

            boolean useNewBTConn = true;

            if(!useNewBTConn) {
                hUDConnectivityManager.hudConnectivityConnection.startListening();
                HUDConnectivityManager hUDConnectivityManager2 = this.hudConnectivityManager;
                IHUDConnectivity iHUDConnectivity = this.d;
                if (hUDConnectivityManager2.connectivityHandler != null) {
                    if (iHUDConnectivity == null) {
                        throw new IllegalArgumentException("Interface IHUDConnectivity you registered is null");
                    }

                    if (hUDConnectivityManager2.connectivityHandler.addIHUDConnectivity(iHUDConnectivity)) {
                        iHUDConnectivity.onDeviceName(hUDConnectivityManager2.mDeviceName);
                        iHUDConnectivity.onConnectionStateChanged(hUDConnectivityManager2.connectionState);
                        iHUDConnectivity.onNetworkEvent(hUDConnectivityManager2.mHasLocalWeb ? IHUDConnectivity.NetworkEvent.LOCAL_WEB_GAINED : IHUDConnectivity.NetworkEvent.LOCAL_WEB_LOST, hUDConnectivityManager2.hasNetworkAccess());
                        iHUDConnectivity.onNetworkEvent(hUDConnectivityManager2.mHasRemoteWeb ? IHUDConnectivity.NetworkEvent.REMOTE_WEB_GAINED : IHUDConnectivity.NetworkEvent.REMOTE_WEB_LOST, hUDConnectivityManager2.hasNetworkAccess());
                        return;
                    }
                    return;
                }
            }
            else
            {
                String address = "";
                //if(MainActivity.phoneConnected) {

                //Force P10
                address = "D8:C7:71:E1:95:FA";
                //!!!recom3
                address = MainActivity.phoneAddress;

                /*
                Iterator<BluetoothDevice> iterator = BluetoothAdapter.getDefaultAdapter().getBondedDevices().iterator();
                while (true) {
                    if (!iterator.hasNext()) {
                        break;
                    }
                    BluetoothDevice bluetoothDevice = iterator.next();
                    BluetoothClass btClass = bluetoothDevice.getBluetoothClass();

                    String a = bluetoothDevice.getAddress();

                    if(btClass.getDeviceClass()== BluetoothClass.Device.PHONE_SMART
                            || btClass.getDeviceClass()== BluetoothClass.Device.PHONE_CELLULAR)
                    {
                        int bh = BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(BluetoothHeadset.HEALTH);
                        if(bh != BluetoothHeadset.STATE_DISCONNECTED)
                        {
                            address = bluetoothDevice.getAddress();
                            break;
                        }
                    }
                }//end while
                */

                if(!address.isEmpty()) {
                    hUDConnectivityManager.mHUDBTService.connect(address);
                }
                //}
            }

            return;
        } catch (Exception e) {
            Log.i(f2143a, "Failed to init HUDConnectivityManager", e);
            return;
        }
    }
}

