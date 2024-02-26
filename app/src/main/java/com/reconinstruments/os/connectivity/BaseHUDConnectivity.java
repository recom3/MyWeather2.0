package com.reconinstruments.os.connectivity;

import android.os.Parcelable;
import org.json.JSONObject;

public abstract class BaseHUDConnectivity implements Parcelable {

    public BaseHUDConnectivity()
    {

    }

    public BaseHUDConnectivity(byte[] bytes) throws Exception {
        this(new JSONObject(new String(bytes)));
    }

    public BaseHUDConnectivity(JSONObject json) throws Exception {
        //readFromJSON(json);
        b(json);
    }

    public abstract void a(JSONObject paramJSONObject);

    public abstract void b(JSONObject paramJSONObject);

    public String toString() {
        String str = null;
        try {
            JSONObject jSONObject = new JSONObject();
            a(jSONObject);
            str = jSONObject.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
            exception = null;
        }
        return str;
    }

    public byte[] getByteArray() throws Exception {
        return toString().getBytes();
    }
}
