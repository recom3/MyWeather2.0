package com.reconinstruments.os.connectivity;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import java.util.LinkedList;
import java.util.Queue;

import android.os.Bundle;
import android.os.Message;

import android.os.Handler;

import com.reconinstruments.os.connectivity.bluetooth.HUDBTService;
import com.reconinstruments.os.connectivity.http.HUDHttpBTConnection;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest;
import com.reconinstruments.os.connectivity.http.HUDHttpResponse;
import com.reconinstruments.os.connectivity.http.URLConnectionHUDAdaptor;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Class to:
 * 1. Push the messages from HUD
 * 2. Send web request
 * This class is implemented in Snow3/Ice phone app with BTConnectivityManager
 * In the code from Snow2 version 3.0: the HUDConnectivityManager contains the queues in these projects:
 * ConnectDevice, DashNotification, ReconAppLauncher
 */
public class HUDConnectivityManager implements IHUDConnectivity {
    private static final boolean DEBUG = true;
    private static final String TAG = "HUDConnectivityManager";

    private HUDHttpBTConnection mHUDHttpBTConnection = null;
    public HUDBTService mHUDBTService = null;

    private boolean mIsHUD = true;

    public HUDConnectivityManager.ConnectivityHandler connectivityHandler = null;

    public String mDeviceName = "";

    public com.reconinstruments.os.connectivity.IHUDConnectivityConnection hudConnectivityConnection = null;

    public IHUDConnectivity.ConnectionState connectionState = IHUDConnectivity.ConnectionState.DISCONNECTED;

    public boolean mHasLocalWeb = false;

    public boolean mHasRemoteWeb = false;

    //Force to connected on startup, because the common library cannot maybe or not is installed,
    //so it is hard to know if the hud has connectivity
    private boolean mHUDConnected = true;//false;

    static Queue<QueueMessage> mCommandQueue = new LinkedList<QueueMessage>();

    static Queue<QueueMessage> mFileQueue;

    static Queue<QueueMessage> mObjectQueue = new LinkedList<QueueMessage>();

    static {
        mFileQueue = new LinkedList<QueueMessage>();
    }

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_BT_STATE_CHANGE = 1;
    public static final int MESSAGE_NETWORK_EVENT = 2;
    public static final int MESSAGE_READ = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;

    public HUDConnectivityManager() {
        Log.i("HUDConnectivityManager", "command connector is starting...");
        (new HUDCommandConnector()).start();
        Log.i("HUDConnectivityManager", "object connector is starting...");
        (new HUDObjectConnector()).start();
        Log.i("HUDConnectivityManager", "file connector is starting...");
        (new HUDFileConnector()).start();
    }

    /**
     * @param context
     * @param hudConnectivity an interface to receiver IHUDConnectivity call backs
     * @param isHUD           This class runs on a HUD or a smart phone: HUDConnectivityManager.RUNNING_ON_XXX
     * @param forceBTEnable   Flag to force BT enable
     * @param appUniqueName   A Unique Name for your application, for example: com.mycompany.myapp
     * @param hudRequestUUID  Unique UUID for the application that uses this service
     * @param phoneRequestUUID       Unique UUID for the application that uses this service
     * @throws Exception
     */
    public HUDConnectivityManager(Context context, IHUDConnectivity hudConnectivity, boolean isHUD, boolean forceBTEnable, String appUniqueName, UUID hudRequestUUID, UUID phoneRequestUUID) throws Exception {
        //This is not implemnted yet: is to receive events
        //mHUDConnectivity = hudConnectivity;
        mIsHUD = isHUD;

        //This is not implemnted yet: is to receive events
        //mHandler = new ConnectivityHandler(mHUDConnectivity);
        mHUDBTService = new HUDBTService(this, isHUD, forceBTEnable, appUniqueName, hudRequestUUID, phoneRequestUUID);
        mHUDHttpBTConnection = new HUDHttpBTConnection(context, this, isHUD, mHUDBTService);
    }

    /**
     * To use with the reconsdk lib
     * @param context
     * @param hudConnectivity
     * @param isHUD
     * @throws Exception
     */
    public HUDConnectivityManager(Context context, IHUDConnectivity hudConnectivity, boolean isHUD) throws Exception {

        //This is not implemented yet: is to receive events
        //mHUDConnectivity = hudConnectivity;
        mIsHUD = isHUD;

        //This is not implemented yet: is to receive events
        //mHandler = new ConnectivityHandler(mHUDConnectivity);

        if (mIsHUD) {
            //This is connecting with the recon sdk lib
            //mHUDConnectivityConnection = new HUDConnectivityServiceConnection(context, this);
        } else {
            hudConnectivityConnection = new HUDConnectivityPhoneConnection(context, this);
        }
        //mHUDConnectivityConnection.start();
        hudConnectivityConnection.startListening();
    }

    public void push(HUDConnectivityMessage paramHUDConnectivityMessage, CHANNEL paramCHANNEL, IHUDConnectivityCallBack paramIHUDConnectivityCallBack) {
        QueueMessage queueMessage = paramHUDConnectivityMessage.toQueueMessage();
        queueMessage.setHUDConnectivityCallBack(paramIHUDConnectivityCallBack);
        switch (paramCHANNEL) {
            default:
                Log.i("HUDConnectivityManager", "wrong channel filled in");
                return;
            case COMMAND_CHANNEL:
                mCommandQueue.add(queueMessage);
                Log.i("HUDConnectivityManager", "push a message to command queue");
                return;
            case OBJECT_CHANNEL:
                mObjectQueue.add(queueMessage);
                Log.i("HUDConnectivityManager", "push a message to object queue");
                return;
            case FILE_CHANNEL:
                break;
        }
        mFileQueue.add(queueMessage);
        Log.i("HUDConnectivityManager", "push a message to file queue");
    }

    @Override
    public void onConnectionStateChanged(ConnectionState state) {
        //This is not implemented yet: is to receive events
        //mHandler.obtainMessage(MESSAGE_BT_STATE_CHANGE, state.ordinal(), -1).sendToTarget();
        setHUDConnected(state == ConnectionState.CONNECTED);
    }

    @Override
    public void onNetworkEvent(NetworkEvent networkEvent, boolean hasNetworkAccess) {
        int i = 1;
        new StringBuilder("onNetworkEvent:").append(networkEvent).append(" hasNetworkAccess:").append(hasNetworkAccess);
        switch (networkEvent) {
            case LOCAL_WEB_GAINED:
                this.mHasLocalWeb = true;
                break;
            case LOCAL_WEB_LOST:
                this.mHasLocalWeb = false;
                break;
            case REMOTE_WEB_GAINED:
                this.mHasRemoteWeb = true;
                break;
            case REMOTE_WEB_LOST:
                this.mHasRemoteWeb = false;
                break;
        }
        ConnectivityHandler connectivityHandler = this.connectivityHandler;
        int ordinal = networkEvent.ordinal();
        if (!hasNetworkAccess()) {
            i = 0;
        }
        connectivityHandler.obtainMessage(2, ordinal, i).sendToTarget();
    }

    @Override
    public void onDeviceName(String paramString) {
        this.mDeviceName = paramString;
        Message obtainMessage = this.connectivityHandler.obtainMessage(4);
        Bundle bundle = new Bundle();
        bundle.putString("device_name", paramString);
        obtainMessage.setData(bundle);
        this.connectivityHandler.sendMessage(obtainMessage);
    }

    public enum CHANNEL {
        COMMAND_CHANNEL, FILE_CHANNEL, OBJECT_CHANNEL;
    }

    public final boolean hasNetworkAccess() {
        boolean z;
        try {
            z = this.hudConnectivityConnection.hasNetworkAccess();
        } catch (Exception e) {
            z = false;
        }
        return z;
    }

    public final void a(IHUDConnectivity.ConnectionState connectionState) {
        (new StringBuilder("onConnectionStateChanged:")).append(connectionState);
        this.connectionState = connectionState;
        setHUDConnected(connectionState == ConnectionState.CONNECTED);
        this.connectivityHandler.obtainMessage(1, connectionState.ordinal(), -1).sendToTarget();
    }

    private void setHUDConnected(boolean hudConnected) {
        if (mHUDConnected != hudConnected) {
            mHUDConnected = hudConnected;
            if (mIsHUD) {
                //Not implemented yet
                //mHUDHttpBTConnection.updateRemoteWebConnection();
            }
        }
    }

    public boolean isHUDConnected() {
        return mHUDConnected;
    }

    /**
     * This is a new function: we need to return an object
     * @param hudHttpRequest
     */
    public final HUDHttpResponse sendWebRequest2(com.reconinstruments.os.connectivity.http.HUDHttpRequest hudHttpRequest) {
        //It is implemented by HUDConnectivityPhoneConnection
        HUDHttpResponse hudHttpResponse = null;

        boolean useNewBTConn = true;

        boolean isNewImpl = useNewBTConn;

        if(!isNewImpl) {
            hudHttpResponse = hudConnectivityConnection.sendWebRequest(hudHttpRequest);
        }
        else
        {
            try {
                hudHttpResponse = sendWebRequest(hudHttpRequest);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return hudHttpResponse;
    }

    /**
     * sendWebRequest
     * @param request
     * @return
     * @throws Exception
     */
    public HUDHttpResponse sendWebRequest(HUDHttpRequest request) throws Exception {
        if (DEBUG) Log.i(TAG, "sendWebRequest");

        //This class is not planned to be used in phone, but possibly this will be uncommented
        /*
        if (mHUDHttpBTConnection.hasLocalWebConnection()) {
            return URLConnectionHUDAdaptor.sendWebRequest(request);
        }
        */

        if (!mIsHUD) {
            throw new NetworkErrorException("Phone is not connected to the internet");
        }

        if (isHUDConnected()) {
            return mHUDHttpBTConnection.sendWebRequest(request);
        } else {
            throw new NetworkErrorException("Not connected to a smartphone");
        }
    }

    public void register(Activity activity)
    {

    }

    public void unregister(Activity activity)
    {

    }

    public static class ConnectivityHandler extends Handler {

        public ArrayList<WeakReference<IHUDConnectivity>> mWeakHUDConnectivityArr;

        public ConnectivityHandler(HUDConnectivityManager hUDConnectivityManager) {}

        private ConnectivityHandler() {
            this.mWeakHUDConnectivityArr = new ArrayList<>();
        }

        public ConnectivityHandler(HUDConnectivityManager hUDConnectivityManager, byte b) {
            this();
        }

        public final WeakReference<IHUDConnectivity> getIHUDConnectivity(IHUDConnectivity iHUDConnectivity) {
            WeakReference<IHUDConnectivity> weakReference;
            synchronized (this.mWeakHUDConnectivityArr) {
                int i = 0;
                while (true) {
                    if (i >= this.mWeakHUDConnectivityArr.size()) {
                        weakReference = null;
                        break;
                    } else if (this.mWeakHUDConnectivityArr.get(i).get() == iHUDConnectivity) {
                        weakReference = this.mWeakHUDConnectivityArr.get(i);
                        break;
                    } else {
                        i++;
                    }
                }
            }
            return weakReference;

        }

        public final boolean addIHUDConnectivity(IHUDConnectivity iHUDConnectivity) {
            boolean z = false;
            if (iHUDConnectivity != null && getIHUDConnectivity(iHUDConnectivity) == null) {
                synchronized (this.mWeakHUDConnectivityArr) {
                    this.mWeakHUDConnectivityArr.add(new WeakReference<>(iHUDConnectivity));
                }
                z = true;
            }
            return z;
        }

        public void handleMessage(Message message) {
            if (this.mWeakHUDConnectivityArr.size() == 0) {
                return;
            }
            switch (message.what) {
                case MESSAGE_BT_STATE_CHANGE:
                    new StringBuilder("MESSAGE_BT_STATE_CHANGE: ").append(message.arg1);
                    synchronized (this.mWeakHUDConnectivityArr) {
                        int i = 0;
                        while (i < this.mWeakHUDConnectivityArr.size()) {
                            IHUDConnectivity iHUDConnectivity = this.mWeakHUDConnectivityArr.get(i).get();
                            if (iHUDConnectivity != null) {
                                iHUDConnectivity.onConnectionStateChanged(IHUDConnectivity.ConnectionState.values()[message.arg1]);
                            } else {
                                this.mWeakHUDConnectivityArr.remove(i);
                                i--;
                            }
                            i++;
                        }
                    }
                    return;
                case MESSAGE_NETWORK_EVENT:
                    new StringBuilder("MESSAGE_NETWORK_EVENT: ").append(message.arg1);
                    synchronized (this.mWeakHUDConnectivityArr) {
                        int i2 = 0;
                        while (i2 < this.mWeakHUDConnectivityArr.size()) {
                            IHUDConnectivity iHUDConnectivity2 = this.mWeakHUDConnectivityArr.get(i2).get();
                            if (iHUDConnectivity2 != null) {
                                iHUDConnectivity2.onNetworkEvent(IHUDConnectivity.NetworkEvent.values()[message.arg1], message.arg2 == 1);
                            } else {
                                this.mWeakHUDConnectivityArr.remove(i2);
                                i2--;
                            }
                            i2++;
                        }
                    }
                    return;
                case MESSAGE_READ:
                default:
                    return;
                case MESSAGE_DEVICE_NAME:
                    new StringBuilder("MESSAGE_DEVICE_NAME: ").append(message.getData().getString("device_name"));
                    synchronized (this.mWeakHUDConnectivityArr) {
                        int i3 = 0;
                        while (i3 < this.mWeakHUDConnectivityArr.size()) {
                            IHUDConnectivity iHUDConnectivity3 = this.mWeakHUDConnectivityArr.get(i3).get();
                            if (iHUDConnectivity3 != null) {
                                iHUDConnectivity3.onDeviceName(message.getData().getString("device_name"));
                            } else {
                                this.mWeakHUDConnectivityArr.remove(i3);
                                i3--;
                            }
                            i3++;
                        }
                    }
                    return;
            }
        }
    }

}
