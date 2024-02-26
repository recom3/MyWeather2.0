package com.reconinstruments.os.connectivity;

import com.reconinstruments.os.connectivity.http.HUDHttpResponse;

public interface IHUDConnectivityConnection {
    boolean hasNetworkAccess();

    void startListening();

    void stopListening();
    //New
    HUDHttpResponse sendWebRequest(com.reconinstruments.os.connectivity.http.HUDHttpRequest hudHttpRequest);
}
