package com.reconinstruments.os.connectivity.http;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class HUDHttpRequest extends HUDHttpMessage {

    public enum RequestMethod {
        //a, b, c, d, e, f, g;
        DELETE(1), GET(2), HEAD(3), OPTIONS(4), POST(5), PUT(6), TRACE(7), PATCH(8);

        private final int value;
        private RequestMethod(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static final Parcelable.Creator<HUDHttpRequest> CREATOR = new Parcelable.Creator<HUDHttpRequest>() {
        @Override
        public HUDHttpRequest createFromParcel(Parcel parcel) {
            return new HUDHttpRequest(parcel);
        }

        @Override
        public HUDHttpRequest[] newArray(int i) {
            return new HUDHttpRequest[i];
        }
    };

    private static final int DEFAULT_TIMEOUT_MS = 15000;
    private static final boolean DEFAULT_DO_INPUT = true;

    HUDHttpRequest.RequestMethod requestMethod;
    private boolean mDoInput = DEFAULT_DO_INPUT;

    //URL d;
    URL uRL;

    int timeOut = 15000;

    private final String f = getClass().getSimpleName();

    private boolean doInput = true;

    public HUDHttpRequest(Parcel paramParcel) {
        a(paramParcel);
    }

    public HUDHttpRequest(byte[] paramArrayOfbyte) {
        try
        {
            b(new JSONObject(new String(paramArrayOfbyte)));
        }
        catch (Exception ex)
        {
            Log.e("HUDHttpRequest", ex.getMessage());
        }
    }

    public HUDHttpRequest(HUDHttpRequest.RequestMethod requestMethod, String url)
    {
        this.requestMethod = requestMethod;
        try{
            this.uRL = new URL(url);
        }
        catch (Exception ex)
        {
            Log.e("HUDHttpRequest", ex.getMessage());
        }
    }

    public final void a(Parcel paramParcel) {
        boolean bool = true;
        this.requestMethod = HUDHttpRequest.RequestMethod.values()[paramParcel.readInt()];
        try {
            URL uRL = new URL(paramParcel.readString());
            this.uRL = uRL;
        } catch (MalformedURLException malformedURLException) {}
        if (paramParcel.readInt() != 1)
            bool = false;
        this.doInput = bool;
        this.timeOut = paramParcel.readInt();
        super.a(paramParcel);
    }

    public final void a(JSONObject paramJSONObject) {
        try
        {
            paramJSONObject.put("requestMethod", this.requestMethod.ordinal());
            paramJSONObject.put("url", this.uRL);
            paramJSONObject.put("doInput", POST());
            paramJSONObject.put("timeout", this.timeOut);
        }
        catch (Exception ex)
        {
            Log.e("HUDHttpRequest", ex.getMessage());
        }
        super.a(paramJSONObject);
    }

    public final void b(JSONObject paramJSONObject) {
        try {
            this.requestMethod = RequestMethod.values()[paramJSONObject.getInt("requestMethod")];
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.uRL = new URL(paramJSONObject.getString("url"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (paramJSONObject.has("doInput")) {
            try {
                this.doInput = paramJSONObject.getBoolean("doInput");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            this.doInput = true;
        }
        if (paramJSONObject.has("timeout")) {
            try {
                this.timeOut = paramJSONObject.getInt("timeout");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            this.timeOut = 15000;
        }
        super.b(paramJSONObject);
    }

    //Is really POST?
    //public final boolean b() {
    public final boolean POST() {
        return (this.requestMethod == RequestMethod.POST) ? true : this.doInput;
    }
    public final boolean b() {
        return (this.requestMethod == RequestMethod.POST) ? true : this.doInput;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        boolean bool;
        paramParcel.writeInt(this.requestMethod.ordinal());
        paramParcel.writeString(this.uRL.toString());
        if (this.doInput) {
            bool = true;
        } else {
            bool = false;
        }
        paramParcel.writeInt(bool ? 1 : 0);
        paramParcel.writeInt(this.timeOut);
        super.writeToParcel(paramParcel, paramInt);
    }

    /**
     * @return the value of the doInput flag which will be given to the URLConnection.
     * </br>This is always true of it is a GET request
     */
    public boolean getDoInput() {
        if (requestMethod == RequestMethod.GET) {
            return true;
        }
        return mDoInput;
    }
}
