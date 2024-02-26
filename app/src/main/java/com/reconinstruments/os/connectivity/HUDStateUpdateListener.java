package com.reconinstruments.os.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by Recom3 on 14/01/2023.
 */

public abstract
class HUDStateUpdateListener extends BroadcastReceiver {

    /* loaded from: recon_engage_jar.jar:com/reconinstruments/mobilesdk/hudconnectivity/HUDStateUpdateListener$HUD_STATE.class */
    public enum HUD_STATE {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }


    public static final String a = HUDStateUpdateListener.class.getSimpleName();

    private final String b = "HUD_STATE_CHANGED";

    private final String c = "state";

    public final void a(Context paramContext) {

        paramContext.registerReceiver(this, new IntentFilter("HUD_STATE_CHANGED"));

        //!!!
        //a(HUDConnectivityService.c());
    }

    public abstract void a(HUDStateUpdateListener.HUD_STATE paramHUDStateUpdateListener$HUD_STATE);
    /*
    public void a(HUDStateUpdateListener.HUD_STATE paramHUDStateUpdateListener$HUD_STATE)
    {
        int debug_here = 1;
    }
    */

    public void onReceive(Context paramContext, Intent paramIntent) {
        int i = paramIntent.getExtras().getInt("state");
        if (i > (HUDStateUpdateListener.HUD_STATE.values()).length - 1) {
            Log.i(a, String.format("Received unknown %s intent with state: %d", new Object[] { "HUD_STATE_CHANGED", Integer.valueOf(i) }));
            return;
        }
        a(HUDStateUpdateListener.HUD_STATE.values()[i]);
    }
}
