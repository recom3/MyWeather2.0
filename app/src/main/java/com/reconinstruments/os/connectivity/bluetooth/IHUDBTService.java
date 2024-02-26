package com.reconinstruments.os.connectivity.bluetooth;

/**
 * Created by Recom3 on 16/01/2023.
 */

import com.reconinstruments.os.connectivity.IHUDConnectivity;

import java.io.OutputStream;

public interface IHUDBTService {
    IHUDConnectivity.ConnectionState getConnectionState();

    void a(HUDBTBaseService.OutputStreamContainer paramHUDBTBaseService$OutputStreamContainer);

    void a(HUDBTBaseService.OutputStreamContainer paramHUDBTBaseService$OutputStreamContainer, byte[] paramArrayOfbyte);

    void a(IHUDBTConsumer paramIHUDBTConsumer);

    HUDBTBaseService.OutputStreamContainer d();
}