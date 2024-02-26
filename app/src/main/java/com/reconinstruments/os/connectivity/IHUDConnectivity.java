package com.reconinstruments.os.connectivity;

public interface IHUDConnectivity {

    enum ConnectionState {
        STOPPED,
        LISTENING,
        CONNECTED,
        CONNECTING,
        DISCONNECTED
    }

    enum NetworkEvent {
        LOCAL_WEB_GAINED,
        LOCAL_WEB_LOST,
        REMOTE_WEB_GAINED,
        REMOTE_WEB_LOST
    }

    void onConnectionStateChanged(ConnectionState paramConnectionState);

    void onNetworkEvent(NetworkEvent paramNetworkEvent, boolean paramBoolean);

    void onDeviceName(String paramString);
}