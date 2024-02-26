package com.reconinstruments.os.connectivity;

public class HUDConnectivityMessage {
    public byte[] data;

    public String intentFilter;

    public String sender;

    public HUDConnectivityMessage() {}

    public HUDConnectivityMessage(String paramString1, byte[] paramArrayOfbyte, String paramString2) {
        this.intentFilter = paramString1;
        this.data = paramArrayOfbyte;
        this.sender = paramString2;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getIntentFilter() {
        return this.intentFilter;
    }

    public String getSender() {
        return this.sender;
    }

    public void setData(byte[] paramArrayOfbyte) {
        this.data = paramArrayOfbyte;
    }

    public void setIntentFilter(String paramString) {
        this.intentFilter = paramString;
    }

    public void setSender(String paramString) {
        this.sender = paramString;
    }

    public QueueMessage toQueueMessage() {
        return new QueueMessage(this.intentFilter, this.data);
    }
}
