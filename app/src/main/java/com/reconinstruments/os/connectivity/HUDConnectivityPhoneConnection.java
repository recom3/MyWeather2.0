package com.reconinstruments.os.connectivity;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.reconinstruments.os.connectivity.bluetooth.HUDBTHeaderFactory;
import com.reconinstruments.os.connectivity.bluetooth.HUDBTMessage;
import com.reconinstruments.os.connectivity.bluetooth.HUDBTMessageCollectionManager;
import com.reconinstruments.os.connectivity.bluetooth.HUDSPPService;
import com.reconinstruments.os.connectivity.bluetooth.IHUDBTConsumer;
import com.reconinstruments.os.connectivity.bluetooth.IHUDBTService;
import com.reconinstruments.os.connectivity.http.HUDHttpBTConnection;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest;
import com.reconinstruments.os.connectivity.http.HUDHttpResponse;

import java.io.UnsupportedEncodingException;

/**
 * Created by Recom3 on 30/01/2023.
 * Contains:
 * HUDHttpBTConnection
 * HUDSPPService
 */

public class HUDConnectivityPhoneConnection implements IHUDConnectivityConnection {

    private HUDHttpBTConnection hudHttpBTConnection = null;

    private HUDSPPService hudSPPService = null;

    public HUDConnectivityPhoneConnection(Context paramContext, IHUDConnectivity paramIHUDConnectivity) {
        try {
            this.hudSPPService = new HUDSPPService(paramIHUDConnectivity);
        }
        catch (Exception ex)
        {
            Log.i("HUDConnectivityPhone", ex.getMessage());
        }
        this.hudHttpBTConnection = new HUDHttpBTConnection(paramContext, paramIHUDConnectivity);
        HUDHttpBTConnection hUDHttpBTConnection = this.hudHttpBTConnection;
        HUDSPPService hUDSPPService = this.hudSPPService;
        HUDHttpBTConnection.ihudbtService = (IHUDBTService)hUDSPPService;
        hUDSPPService.a((IHUDBTConsumer)hUDHttpBTConnection);
    }

    @Override
    public boolean hasNetworkAccess() {
        return HUDHttpBTConnection.hasWebConnection();
    }

    @Override
    public void startListening() {
        HUDHttpBTConnection hUDHttpBTConnection = this.hudHttpBTConnection;
        if (hUDHttpBTConnection.context != null) {
            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            hUDHttpBTConnection.context.registerReceiver(hUDHttpBTConnection.c, intentFilter);
        }
        this.hudSPPService.startListening();
        hUDHttpBTConnection = this.hudHttpBTConnection;
        hUDHttpBTConnection.updateLocalWebConnection(hUDHttpBTConnection.context);
    }

    @Override
    public void stopListening() {
        HUDHttpBTConnection hUDHttpBTConnection = this.hudHttpBTConnection;
        if (hUDHttpBTConnection.context != null)
            hUDHttpBTConnection.context.unregisterReceiver(hUDHttpBTConnection.c);
        this.hudSPPService.stopListening();
    }

    /**
     * This is a new function called by HUDConnectivityManager
     * @param hudHttpRequest
     */
    @Override
    public HUDHttpResponse sendWebRequest(HUDHttpRequest hudHttpRequest) {
        byte[] msgId = new byte[1];

        //We need a wating loop for the response
        HUDHttpResponse hudHttpResponse = null;
        hudHttpBTConnection.callHttp(hudHttpRequest, msgId);

        //Get message id
        byte b2 = msgId[0];

        //We implement the waiting loop here
        int counter = 0;
        while(counter < 16)//16=8s
        {
            if(HUDBTMessageCollectionManager.getMatrixTo1(b2))
            {
                //If true msg is ready
                HUDBTMessage hudbtMessage = HUDBTMessageCollectionManager.getMsg(b2);

                byte[] bytes = hudbtMessage.d;

                try {
                    String str = new String(bytes, "UTF-8");
                    hudHttpResponse = new HUDHttpResponse(200, str);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;

            Log.i(this.getClass().getName(), "Wait loop #" + counter);
        }

        return hudHttpResponse;
    }
}
