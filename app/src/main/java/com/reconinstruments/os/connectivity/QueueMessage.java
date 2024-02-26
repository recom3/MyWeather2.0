package com.reconinstruments.os.connectivity;

public class QueueMessage {
    private byte[] data;

    private IHUDConnectivityCallBack hudConnectivityCallBack;

    private String intentFilter;

    public QueueMessage(String paramString, byte[] paramArrayOfbyte) {
        this.intentFilter = paramString;
        this.data = paramArrayOfbyte;
    }

    public byte[] getData() {
        return this.data;
    }

    public IHUDConnectivityCallBack getHUDConnectivityCallBack() {
        return this.hudConnectivityCallBack;
    }

    public String getIntentFilter() {
        return this.intentFilter;
    }

    public void setData(byte[] paramArrayOfbyte) {
        this.data = paramArrayOfbyte;
    }

    public void setHUDConnectivityCallBack(IHUDConnectivityCallBack paramIHUDConnectivityCallBack) {
        this.hudConnectivityCallBack = paramIHUDConnectivityCallBack;
    }

    public void setIntentFilter(String paramString) {
        this.intentFilter = paramString;
    }
}
