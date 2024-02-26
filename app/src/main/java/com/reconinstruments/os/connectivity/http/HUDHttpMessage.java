package com.reconinstruments.os.connectivity.http;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.reconinstruments.os.connectivity.BaseHUDConnectivity;
import com.reconinstruments.os.hardware.ashmem.HUDAshmem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HUDHttpMessage extends BaseHUDConnectivity {
    public static final Parcelable.Creator<HUDHttpMessage> CREATOR = new Parcelable.Creator<HUDHttpMessage>() {
        @Override
        public HUDHttpMessage createFromParcel(Parcel parcel) {
            return new HUDHttpMessage(parcel);
        }

        @Override
        public HUDHttpMessage[] newArray(int i) {
            return new HUDHttpMessage[i];
        }
    };

    Map<String, List<String>> a = null;

    public byte[] b = null;

    private final String c = getClass().getSimpleName();

    public HUDHttpMessage() {}

    public HUDHttpMessage(byte[] bytes) throws Exception {
        //!!!
        super(bytes);
    }

    protected HUDHttpMessage(byte paramByte) {
        this.a = null;
    }

    public HUDHttpMessage(Parcel paramParcel) {
        a(paramParcel);
    }

    public void a(Parcel paramParcel) {
        int i = paramParcel.readInt();
        if (i > 0) {
            this.a = new HashMap<String, List<String>>();
            for (byte b = 0; b < i; b++) {
                String str = paramParcel.readString();
                ArrayList<String> arrayList = new ArrayList();
                paramParcel.readStringList(arrayList);
                this.a.put(str, arrayList);
            }
        } else {
            this.a = null;
        }
        int j = paramParcel.readInt();
        if (j > 0) {
            i = ((ParcelFileDescriptor)paramParcel.readParcelable(ParcelFileDescriptor.class.getClassLoader())).getFd();
            if (i > 0) {
                this.b = HUDAshmem.a(i, j);
                HUDAshmem.b(i);
                System.gc();
                return;
            }
            this.b = null;
            return;
        }
        this.b = null;
    }

    public void a(JSONObject paramJSONObject) {
        if (this.a != null) {
            JSONObject jSONObject = new JSONObject();
            Iterator<Map.Entry<String,List<String>>> iterator = this.a.entrySet().iterator();
            for (byte b = 0; iterator.hasNext(); b++) {
                Map.Entry entry = iterator.next();
                String str1 = (String)entry.getKey();
                String str2 = str1;
                if (str1 == null) {
                    str2 = str1;
                    if (b==0)
                        str2 = "Status";
                }
                try {
                    jSONObject.put(str2, new JSONArray((Collection)entry.getValue()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                paramJSONObject.put("headers", jSONObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public final void a(byte[] paramArrayOfbyte) {
        if (paramArrayOfbyte != null) {
            this.b = paramArrayOfbyte;
            return;
        }
        this.b = null;
    }

    /**
     * @return the message body in index
     */
    public byte[] getBody() {
        return this.b;
    }

    /**
     * Set a new body
     *
     * @param body the message body
     */
    public void setBody(byte[] body) {
        if (body != null) {
            this.b = body;
        }
    }

    public final boolean hasBody() {
        boolean bool = false;
        if (this.b != null && this.b.length > 0)
            bool = true;
        return bool;
    }

    public void b(JSONObject paramJSONObject) {
        if (paramJSONObject.has("headers")) {
            this.a = new HashMap<String, List<String>>();
            JSONObject jSONObject = null;
            try {
                jSONObject = paramJSONObject.getJSONObject("headers");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Iterator<String> iterator = jSONObject.keys();
            while (iterator.hasNext()) {
                String str2 = iterator.next();
                JSONArray jSONArray = null;
                try {
                    jSONArray = jSONObject.getJSONArray(str2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<String> arrayList = new ArrayList();
                for (byte b = 0; b < jSONArray.length(); b++)
                    try {
                        arrayList.add(jSONArray.getString(b));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                String str1 = str2;
                if (str2.equals("Status"))
                    str1 = null;
                this.a.put(str1, arrayList);
            }
        } else {
            this.a = null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        if (this.a != null) {
            paramParcel.writeInt(this.a.size());
            Iterator<Map.Entry<String,List<String>>> iterator = this.a.entrySet().iterator();
            byte b = 0;
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                if (entry.getKey() != null || b==0) {
                    paramParcel.writeString((String)entry.getKey());
                    paramParcel.writeStringList((List)entry.getValue());
                    b++;
                }
            }
        } else {
            paramParcel.writeInt(0);
        }
        if (this.b != null) {
            if (this.b.length > 0) {
                int i = HUDAshmem.a(this.b.length);
                if (i > 0) {
                    HUDAshmem.a(i, this.b, this.b.length);
                    try {
                        ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.fromFd(i);
                        paramParcel.writeInt(this.b.length);
                        paramParcel.writeParcelable((Parcelable)parcelFileDescriptor, paramInt);
                    } catch (IOException iOException) {
                        (new StringBuilder("Failed to get bodyFd from bodyHandle: ")).append(iOException);
                        paramParcel.writeInt(0);
                        HUDAshmem.b(i);
                    }
                }
            }
            return;
        }
        paramParcel.writeInt(0);
    }
}
