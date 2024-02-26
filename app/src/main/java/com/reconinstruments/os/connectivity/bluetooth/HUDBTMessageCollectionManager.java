package com.reconinstruments.os.connectivity.bluetooth;

import android.support.v4.media.TransportMediator;
import android.util.SparseArray;

/* loaded from: recon_engage_jar.jar:com/reconinstruments/os/connectivity/bluetooth/HUDBTMessageCollectionManager.class */
public class HUDBTMessageCollectionManager {

    /* renamed from: a  reason: collision with root package name */
    private static boolean[] f2734a = new boolean[TransportMediator.KEYCODE_MEDIA_PLAY];

    /* renamed from: b  reason: collision with root package name */
    private static SparseArray<HUDBTMessage> arrRet;
    private static SparseArray<HUDBTMessage> c;

    static {
        for (int i = 0; i < 126; i++) {
            //!!!
            //f2734a[i] = true;
            f2734a[i] = false;
        }
        arrRet = new SparseArray<>();
        c = new SparseArray<>();
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0271  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x029f  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x02ad  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x007b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static HUDBTMessage receive(int i, byte[] bArr, int nReceived, ExcessDataAgent excessDataAgent) {
        HUDBTMessage hUDBTMessage;
        HUDBTMessage hUDBTMessage2 = null;
        int i3 = 0;
        int length = 0;
        int i4;
        boolean k = HUDBTHeaderFactory.k(bArr);
        if (k) {
            byte b2 = HUDBTHeaderFactory.getMsgId(bArr);
            //This code has to refactored
            //---------------------------------------------------------------------------------------------------------
            //!!!
            /*
            if (HUDBTHeaderFactory.c(bArr) == 1) {
                HUDBTMessage hUDBTMessage3 = c.get(i);
                if (hUDBTMessage3 != null && k) {
                    c.remove(i);
                    setMatrixTo1(hUDBTMessage3.f2732a);
                }
                hUDBTMessage = arrRet.get(b2);
                if (hUDBTMessage == null) {
                    hUDBTMessage2 = null;
                } else {
                    arrRet.remove(b2);
                }
            } else {
                hUDBTMessage = new HUDBTMessage(b2);
            }
            */
            hUDBTMessage = new HUDBTMessage(b2);
            //---------------------------------------------------------------------------------------------------------
            c.put(i, hUDBTMessage);
            if (bArr != null && (hUDBTMessage.f2733b != null || k)) {
                if (k) {
                    i3 = 0;
                } else {
                    try {
                        hUDBTMessage.f2733b = new byte[32];
                        System.arraycopy(bArr, 0, hUDBTMessage.f2733b, 0, hUDBTMessage.f2733b.length);
                        length = hUDBTMessage.f2733b.length + 0;
                    } catch (Exception e) {
                        e = e;
                        i3 = 0;
                    }
                    try {
                        if (HUDBTHeaderFactory.g(hUDBTMessage.f2733b)) {
                            hUDBTMessage.c = new byte[HUDBTHeaderFactory.h(hUDBTMessage.f2733b)];
                            hUDBTMessage.f = 0;
                        }
                        if (HUDBTHeaderFactory.i(hUDBTMessage.f2733b)) {
                            hUDBTMessage.d = new byte[HUDBTHeaderFactory.j(hUDBTMessage.f2733b)];
                            hUDBTMessage.e = 0;
                        }
                        i3 = length;
                        if (nReceived <= length) {
                            excessDataAgent.f2725a = null;
                        }
                    } catch (Exception e2) {
                        Exception e = e2;
                        i3 = length;
                        e.printStackTrace();
                        int i5 = i3;
                        if (!hUDBTMessage.b()) {
                        }
                        i4 = i5;
                        if (!hUDBTMessage.a()) {
                        }
                        if (nReceived > i4) {
                        }
                        if (!hUDBTMessage.c()) {
                        }
                        return hUDBTMessage2;
                    }
                }
                //i3 = length header
                int i52 = i3;
                if (!hUDBTMessage.b()) {
                    int a2 = HUDBTMessage.a(nReceived, i3, hUDBTMessage.c.length, hUDBTMessage.f);
                    System.arraycopy(bArr, i3, hUDBTMessage.c, hUDBTMessage.f, a2);
                    int i6 = i3 + a2;
                    hUDBTMessage.f = a2 + hUDBTMessage.f;
                    i52 = i6;
                    if (nReceived <= i6) {
                        excessDataAgent.f2725a = null;
                    }
                }
                i4 = i52;
                if (!hUDBTMessage.a()) {
                    int a3 = HUDBTMessage.a(nReceived, i52, hUDBTMessage.d.length, hUDBTMessage.e);
                    System.arraycopy(bArr, i52, hUDBTMessage.d, hUDBTMessage.e, a3);
                    int i7 = i52 + a3;
                    hUDBTMessage.e = a3 + hUDBTMessage.e;
                    i4 = i7;
                    if (nReceived <= i7) {
                        excessDataAgent.f2725a = null;
                    }
                }
                if (nReceived > i4) {
                    int i8 = nReceived - i4;
                    excessDataAgent.f2725a = new byte[i8];
                    System.arraycopy(bArr, i4, excessDataAgent.f2725a, 0, i8);
                }
            }
            //---------------------------------------------------------------------------------------------------------
            if (!hUDBTMessage.c()) {
                hUDBTMessage2 = null;
            }
            //!!!
            /*
            else if (HUDBTHeaderFactory.a(hUDBTMessage.f2733b)) {
                synchronized (hUDBTMessage) {
                    try {
                        hUDBTMessage.notify();
                    } catch (Throwable th) {
                        HUDBTMessage hUDBTMessage4 = hUDBTMessage;
                        throw th;
                    }
                }
                setMatrixTo1(HUDBTHeaderFactory.getMsgId(hUDBTMessage.f2733b));
                c.remove(i);
                hUDBTMessage2 = null;
            }*/
            else {
                c.remove(i);
                hUDBTMessage2 = hUDBTMessage;
            }
            //---------------------------------------------------------------------------------------------------------
        } else {
            hUDBTMessage = c.get(i);
            if (hUDBTMessage == null) {
                if (excessDataAgent.a() > 0) {
                    byte[] bArr2 = new byte[32];
                    int a4 = 32 - excessDataAgent.a();
                    if (a4 <= nReceived) {
                        System.arraycopy(excessDataAgent.f2725a, 0, bArr2, 0, excessDataAgent.a());
                        System.arraycopy(bArr, 0, bArr2, excessDataAgent.a(), a4);
                        if (HUDBTHeaderFactory.k(bArr2)) {
                            byte[] bArr3 = new byte[excessDataAgent.a() + nReceived];
                            System.arraycopy(excessDataAgent.f2725a, 0, bArr3, 0, excessDataAgent.a());
                            System.arraycopy(bArr, 0, bArr3, excessDataAgent.a(), nReceived);
                            excessDataAgent.f2725a = bArr3;
                        }
                    }
                }
                hUDBTMessage2 = null;
            }
            else
            {
                //Can be data present in excess data agent?
                /*
                int i8 = nBytesInBuffer - i4;
                excessDataAgent.buffer = new byte[i8];
                System.arraycopy(bArr, i4, excessDataAgent.buffer, 0, i8);
                */
                int i8 = 0;
                if(excessDataAgent!=null && excessDataAgent.f2725a!=null)
                {
                    i8 = excessDataAgent.f2725a.length;
                }
            }
            if (bArr != null) {
                if (k) {
                    //If header is not valid this is an error?
                }
                int i52 = i3;
                if (!hUDBTMessage.b()) {
                    int a2 = HUDBTMessage.a(nReceived, i3, hUDBTMessage.c.length, hUDBTMessage.f);
                    System.arraycopy(bArr, i3, hUDBTMessage.c, hUDBTMessage.f, a2);
                    int i6 = i3 + a2;
                    hUDBTMessage.f = a2 + hUDBTMessage.f;
                    i52 = i6;
                    if (nReceived <= i6) {
                        excessDataAgent.f2725a = null;
                    }
                }
                i4 = i52;
                if (!hUDBTMessage.a()) {
                    int a3 = HUDBTMessage.a(nReceived, i52, hUDBTMessage.d.length, hUDBTMessage.e);
                    System.arraycopy(bArr, i52, hUDBTMessage.d, hUDBTMessage.e, a3);
                    int i7 = i52 + a3;
                    hUDBTMessage.e = a3 + hUDBTMessage.e;
                    i4 = i7;
                    if (nReceived <= i7) {
                        excessDataAgent.f2725a = null;
                    }
                }
                if (nReceived > i4) {
                    /*
                    int i8 = i2 - i4;
                    excessDataAgent.f2725a = new byte[i8];
                    System.arraycopy(bArr, i4, excessDataAgent.f2725a, 0, i8);
                    */
                }
            }
            if (!hUDBTMessage.c()) {
                hUDBTMessage2 = null;
            }
            else
            {
                c.remove(i);
                hUDBTMessage2 = hUDBTMessage;
            }
        }

        //Rec3:2023-05-21
        //New code to try to return the response
        //-------------------------------------------------------------------------------------------------------------
        if(hUDBTMessage2!=null) {

            bArr = hUDBTMessage2.f2733b;
            byte b2 = HUDBTHeaderFactory.getMsgId(bArr);
            if (HUDBTHeaderFactory.getApplication(bArr) == 1) {
                setMatrixTo1(hUDBTMessage2.f2732a);
                arrRet.put(b2, hUDBTMessage);
                hUDBTMessage2 = null;
            }
        }
        //-------------------------------------------------------------------------------------------------------------
        return hUDBTMessage2;
    }

    private static void setMatrixTo1(byte b2) {
        synchronized (f2734a) {
            if (b2 < 126) {
                f2734a[b2] = true;
            }
        }
    }

    public static boolean getMatrixTo1(byte b2) {
        synchronized (f2734a) {
            if (b2 < 126) {
                return f2734a[b2];
            }
            else
            {
                return false;
            }
        }
    }

    public static HUDBTMessage getMsg(byte b2)
    {
        synchronized (f2734a) {
            HUDBTMessage hudbtMessage = arrRet.get(b2);
            if (b2 < 126) {
                f2734a[b2] = false;
            }
            return hudbtMessage;
        }
    }
}