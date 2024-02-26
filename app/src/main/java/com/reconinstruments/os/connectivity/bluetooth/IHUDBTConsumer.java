package com.reconinstruments.os.connectivity.bluetooth;

/**
 * Created by Recom3 on 16/01/2023.
 */

public interface IHUDBTConsumer {
    boolean sendHttpRequest(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);

    boolean consumeBTData(byte[] header, byte[] payload, byte[] body);
}

