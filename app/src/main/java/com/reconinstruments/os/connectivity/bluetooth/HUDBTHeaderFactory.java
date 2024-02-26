package com.reconinstruments.os.connectivity.bluetooth;

public class HUDBTHeaderFactory {
    /**
     * /* Header Data
     * Byte [0]: Version
     * byte [1]: Data Length
     * Byte [2]: Transfer Method
     * Byte [3]: Application
     */
    public static final int HEADER_LENGTH = 32;

    private static final int VERSION_IDX = 0;
    private static final int CODE_IDX = 1;
    private static final int REQUEST_HDR_IDX = 2;
    private static final int APPLICATION_IDX = 3;
    private static final int CMD_IDX = 4;
    private static final int ARG1_IDX = 5;
    private static final int HAS_PAYLOAD_IDX = 14;
    private static final int PAYLOAD_LEN_BASE_IDX = 23;//15;
    private static final int HAS_BODY_IDX = 27;//23;
    private static final int BODY_LEN_BASE_IDX = 28;//24;

    public static final byte HEADER_BYTE__NULL = 0x0;
    public static final byte VERSION__1 = 0x1;
    public static final byte CODE__NOCODE = 0x1;
    public static final byte CODE__ERROR = 0x2;
    public static final byte CODE__SUCCESS = 0x3;
    public static final byte REQUEST_HDR__RESPONSE = 0x1;
    public static final byte REQUEST_HDR__ONEWAY = 0x2;

    public static final byte APPLICATION__PHONE = 0x1;
    public static final byte APPLICATION__WEB = 0x2;
    public static final byte APPLICATION__CMD = 0x3;

    public static final byte CMD__CHECK_REMOTE_NETWORK = 0x1;
    public static final byte CMD__UPDATE_REMOTE_NETWORK = 0x2;
    public static final byte ARG1__HAS_NETWORK = 0x1;
    public static final byte ARG1__NO_NETWORK = 0x2;

    private static int a(byte[] paramArrayOfbyte, int paramInt) {
        return (paramArrayOfbyte[paramInt] & 0xFF) + ((paramArrayOfbyte[paramInt + 1] & 0xFF) << 8) + ((paramArrayOfbyte[paramInt + 2] & 0xFF) << 16) + ((paramArrayOfbyte[paramInt + 3] & 0xFF) << 24);
    }

    public static void a(byte[] paramArrayOfbyte, byte paramByte) {
        paramArrayOfbyte[1] = (byte)paramByte;
    }

    private static void a(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
        paramArrayOfbyte[paramInt1] = (byte)(byte)(paramInt2 & 0xFF);
        paramArrayOfbyte[paramInt1 + 1] = (byte)(byte)(paramInt2 >> 8 & 0xFF);
        paramArrayOfbyte[paramInt1 + 2] = (byte)(byte)(paramInt2 >> 16 & 0xFF);
        paramArrayOfbyte[paramInt1 + 3] = (byte)(byte)(paramInt2 >> 24 & 0xFF);
    }

    /**
     * @return a request header to transmit Internet request and an optional response
     */
    public static byte[] getInternetRequestHeader(boolean hasResponse, int requestLength, int bodyLength) {
        byte[] header = getBaseHeader(true, hasResponse ? REQUEST_HDR__RESPONSE : REQUEST_HDR__ONEWAY);

        header[APPLICATION_IDX] = APPLICATION__WEB;
        setPayloadLength(header, requestLength);

        if (bodyLength > 0) {
            header[HAS_BODY_IDX] = 1;
            setBodyLength(header, bodyLength);
        } else {
            header[HAS_BODY_IDX] = 0;
        }

        return header;
    }

    public static boolean a(byte[] paramArrayOfbyte) {
        boolean bool = true;
        if (paramArrayOfbyte[3] != 1)
            bool = false;
        return bool;
    }

    public static byte[] a() {
        byte[] arrayOfByte = b(false, (byte)3);
        arrayOfByte[2] = (byte)2;
        return arrayOfByte;
    }

    private static byte[] a(byte paramByte) {
        byte[] arrayOfByte = b(false, paramByte);
        arrayOfByte[4] = (byte)3;
        return arrayOfByte;
    }

    public static byte[] a(int paramInt1, int paramInt2) {
        byte[] arrayOfByte = b(true, (byte)1);
        arrayOfByte[4] = (byte)2;
        a(arrayOfByte, 23, paramInt1);
        if (paramInt2 > 0) {
            arrayOfByte[27] = (byte)1;
            a(arrayOfByte, 28, paramInt2);
            return arrayOfByte;
        }
        arrayOfByte[27] = (byte)0;
        return arrayOfByte;
    }

    public static byte[] adquireHeader2(int paramInt1, int paramInt2) {
        byte[] arrayOfByte = b(true, (byte)2);
        arrayOfByte[4] = (byte)2;
        a(arrayOfByte, 23, paramInt1);
        if (paramInt2 > 0) {
            arrayOfByte[27] = (byte)1;
            a(arrayOfByte, 28, paramInt2);
            return arrayOfByte;
        }
        arrayOfByte[27] = (byte)0;
        return arrayOfByte;
    }

    public static byte[] getUpdateNetworkHeader(boolean paramBoolean) {
        byte b = 2;
        byte[] arrayOfByte = a((byte)3);
        arrayOfByte[5] = (byte)2;
        if (paramBoolean)
            b = 1;
        arrayOfByte[6] = (byte)b;
        return arrayOfByte;
    }

    public static byte[] a(boolean paramBoolean, byte paramByte) {
        byte b = 2;
        byte[] arrayOfByte = a((byte)1);
        arrayOfByte[5] = (byte)2;
        if (paramBoolean)
            b = 1;
        arrayOfByte[6] = (byte)b;
        arrayOfByte[1] = (byte)paramByte;
        return arrayOfByte;
    }

    public static byte getMsgId(byte[] paramArrayOfbyte) {
        return paramArrayOfbyte[1];
    }

    private static byte[] b(boolean paramBoolean, byte paramByte) {
        boolean bool = true;
        byte[] arrayOfByte = new byte[32];
        arrayOfByte[0] = (byte)1;
        arrayOfByte[2] = (byte)1;
        if (!paramBoolean)
            bool = false;
        arrayOfByte[22] = (byte)(bool ? 1 : 0);
        arrayOfByte[27] = (byte)0;
        arrayOfByte[3] = (byte)paramByte;
        arrayOfByte[16] = (byte)-113;
        arrayOfByte[17] = (byte)12;
        arrayOfByte[18] = (byte)65;
        arrayOfByte[19] = (byte)90;
        arrayOfByte[20] = (byte)107;
        arrayOfByte[21] = (byte)-101;
        return arrayOfByte;
    }

    /*
    public static byte c(byte[] paramArrayOfbyte) {
        return paramArrayOfbyte[APPLICATION_IDX];
    }
    */

    public static byte getCmd(byte[] header) {
        return header[CMD_IDX];
    }

    public static byte e(byte[] paramArrayOfbyte) {
        return paramArrayOfbyte[5];
    }

    public static byte f(byte[] paramArrayOfbyte) {
        return paramArrayOfbyte[6];
    }

    public static boolean g(byte[] paramArrayOfbyte) {
        boolean bool = true;
        if (paramArrayOfbyte[22] != 1)
            bool = false;
        return bool;
    }

    public static int h(byte[] paramArrayOfbyte) {
        return a(paramArrayOfbyte, 23);
    }

    public static boolean i(byte[] paramArrayOfbyte) {
        boolean bool = true;
        if (paramArrayOfbyte[27] != 1)
            bool = false;
        return bool;
    }

    public static int j(byte[] paramArrayOfbyte) {
        return a(paramArrayOfbyte, 28);
    }

    public static boolean k(byte[] paramArrayOfbyte) {
        boolean bool1 = false;
        if (paramArrayOfbyte.length < 32)
            return bool1;
        boolean bool2 = bool1;
        if (paramArrayOfbyte[16] == -113) {
            bool2 = bool1;
            if (paramArrayOfbyte[17] == 12) {
                bool2 = bool1;
                if (paramArrayOfbyte[18] == 65) {
                    bool2 = bool1;
                    if (paramArrayOfbyte[19] == 90) {
                        bool2 = bool1;
                        if (paramArrayOfbyte[20] == 107) {
                            bool2 = bool1;
                            if (paramArrayOfbyte[21] == -101)
                                bool2 = true;
                        }
                    }
                }
            }
        }
        return bool2;
    }

    //-----------------------------------------------------------------------------------------------------------------

    public static byte getVersion(byte[] header) {
        return header[VERSION_IDX];
    }

    public static byte getCode(byte[] header) {
        return header[CODE_IDX];
    }

    public static byte getRequestHeaderType(byte[] header) {
        return header[REQUEST_HDR_IDX];
    }

    public static byte getApplication(byte[] header) {
        return header[APPLICATION_IDX];
    }

    private static int getInt(byte[] header, int baseAddr) {
        int value = header[baseAddr] +
                (header[baseAddr + 1] << 4) +
                (header[baseAddr + 2] << 8) +
                (header[baseAddr + 3] << 12) +
                (header[baseAddr + 4] << 16) +
                (header[baseAddr + 5] << 20) +
                (header[baseAddr + 6] << 24) +
                (header[baseAddr + 7] << 28);
        return value;
    }

    private static int getInt4(byte[] header, int baseAddr) {
        int byte1 = header[baseAddr] & 0xff;
        int value = (header[baseAddr] & 0xff) +
                (header[baseAddr + 1] << 8) +
                (header[baseAddr + 2] << 16) +
                (header[baseAddr + 3] << 24);
        return value;
    }

    private static void setInt(byte[] header, int baseAddr, int value) {
        header[baseAddr] = (byte) (value & 0xf);
        header[baseAddr + 1] = (byte) (value >> 4 & 0xf);
        header[baseAddr + 2] = (byte) (value >> 8 & 0xf);
        header[baseAddr + 3] = (byte) (value >> 12 & 0xf);
        header[baseAddr + 4] = (byte) (value >> 16 & 0xf);
        header[baseAddr + 5] = (byte) (value >> 20 & 0xf);
        header[baseAddr + 6] = (byte) (value >> 24 & 0xf);
        header[baseAddr + 7] = (byte) (value >> 28 & 0xf);
    }

    public static boolean hasPayload(byte[] header) {
        return header[HAS_PAYLOAD_IDX] == 1;
    }

    public static int getPayloadLength(byte[] header) {
        return getInt4(header, PAYLOAD_LEN_BASE_IDX);
    }

    public static void setPayloadLength(byte[] header, int length) {
        setInt(header, PAYLOAD_LEN_BASE_IDX, length);
    }

    public static boolean hasBody(byte[] header) {
        return header[HAS_BODY_IDX] == 1;
    }

    public static int getBodyLength(byte[] header) {
        return getInt4(header, BODY_LEN_BASE_IDX);
    }

    public static void setBodyLength(byte[] header, int length) {
        setInt(header, BODY_LEN_BASE_IDX, length);
    }

    private static byte[] getBaseHeader(boolean hasPayload, byte requestType) {
        byte[] header = new byte[HEADER_LENGTH];

        header[VERSION_IDX] = VERSION__1;
        header[CODE_IDX] = CODE__NOCODE;
        header[HAS_PAYLOAD_IDX] = hasPayload ? (byte) 1 : (byte) 0;
        header[HAS_BODY_IDX] = 0;
        header[REQUEST_HDR_IDX] = requestType;

        return header;
    }

    public static byte[] getErrorHeader() {
        byte[] header = getBaseHeader(false, REQUEST_HDR__ONEWAY);

        header[CODE_IDX] = CODE__ERROR;

        return header;
    }
}
