package com.reconinstruments.os.connectivity.http;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public final class HUDHttpResponse extends HUDHttpMessage {
    public static final Parcelable.Creator<HUDHttpResponse> CREATOR = new Parcelable.Creator<HUDHttpResponse>() {
        @Override
        public HUDHttpResponse createFromParcel(Parcel parcel) {
            return new HUDHttpResponse(parcel);
        }

        @Override
        public HUDHttpResponse[] newArray(int i) {
            return new HUDHttpResponse[i];
        }
    };

    private final String c = getClass().getSimpleName();

    private int responseCode;

    private String responseMessage;

    public HUDHttpResponse(byte[] bytes) throws Exception {
        super(bytes);
    }

    public HUDHttpResponse(int paramInt, String paramString) {
        this(paramInt, paramString, (byte)0);
    }

    private HUDHttpResponse(int paramInt, String paramString, byte paramByte) {
        super((byte)0);
        this.responseCode = paramInt;
        this.responseMessage = paramString;
    }

    public HUDHttpResponse(Parcel paramParcel) {
        a(paramParcel);
    }

    public final void a(Parcel paramParcel) {
        this.responseCode = paramParcel.readInt();
        this.responseMessage = paramParcel.readString();
        super.a(paramParcel);
    }

    public final void a(JSONObject paramJSONObject) {
        try {
            paramJSONObject.put("responseCode", this.responseCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            paramJSONObject.put("responseMessage", this.responseMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.a(paramJSONObject);
    }

    public final void b(JSONObject paramJSONObject) {
        try {
            this.responseCode = paramJSONObject.getInt("responseCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.responseMessage = paramJSONObject.getString("responseMessage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.b(paramJSONObject);
    }

    public final void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeInt(this.responseCode);
        paramParcel.writeString(this.responseMessage);
        super.writeToParcel(paramParcel, paramInt);
    }

    /*
    public final String getBody()
    {
        return this.responseMessage;
    }
    */

    /*
    public final boolean hasBody()
    {
        return !TextUtils.isEmpty(getBody());
    }
    */

    public final int getResponseCode()
    {
        return this.responseCode;
    }

    public final String getResponseMessage()
    {
        return this.responseMessage;
    }
}
