package com.reconinstruments.os.connectivity.bluetooth;

/* loaded from: recon_engage_jar.jar:com/reconinstruments/os/connectivity/bluetooth/HUDBTMessage.class */
public class HUDBTMessage {

    /* renamed from: a  reason: collision with root package name */
    final byte f2732a;

    /* renamed from: b  reason: collision with root package name */
    //Header
    byte[] f2733b = null;
    //Payload
    byte[] c = null;
    //Body
    public byte[] d = null;
    int e = 0;
    int f = 0;

    public HUDBTMessage(byte b2) {
        this.f2732a = b2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int i, int i2, int i3, int i4) {
        int i5 = i3 - i4;
        int i6 = i - i2;
        if (i5 < i6) {
            i6 = i5;
        }
        return i6;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean a() {
        boolean z = true;
        if (this.d != null) {
            new StringBuilder("isBodyComplete() mBodyCurrentLength:").append(this.e).append(" mBody: ").append(this.d.length);
            if (this.e < this.d.length) {
                z = false;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean b() {
        boolean z = true;
        if (this.c != null && this.f < this.c.length) {
            z = false;
        }
        return z;
    }

    public final boolean c() {
        boolean z;
        try {
            if (this.f2733b == null) {
                z = false;
            } else {
                z = false;
                if (a()) {
                    z = false;
                    if (b()) {
                        z = true;
                    }
                }
            }
        } catch (Exception e) {
            z = false;
        }
        return z;
    }
}