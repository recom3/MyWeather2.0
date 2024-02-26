package com.reconinstruments.os.connectivity.http;

import org.apache.http.HttpConnection;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class URLConnectionHUDAdaptor {
    public static HUDHttpResponse sendWebRequest(HUDHttpRequest hUDHttpRequest) {
        HUDHttpResponse hUDHttpResponse = null;
        String str;
        Iterator<String> it;
        BufferedInputStream bufferedInputStream = null;
        Iterator<String> it2;
        new StringBuilder("sendRequest: Method=").append(hUDHttpRequest.requestMethod).append(" URL(host)=").append(hUDHttpRequest.uRL.getHost());
        boolean b2 = hUDHttpRequest.b();
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) hUDHttpRequest.uRL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            httpURLConnection.setConnectTimeout(hUDHttpRequest.timeOut);
            //switch (HUDHttpRequest.AnonymousClass2.f2748a[hUDHttpRequest.c.ordinal()]) {
            switch (hUDHttpRequest.requestMethod.getValue()) {
                case 1:
                    str = "DELETE";
                    break;
                case 2:
                    str = "GET";
                    break;
                case 3:
                    str = "HEAD";
                    break;
                case 4:
                    str = "OPTIONS";
                    break;
                case 5:
                    str = "POST";
                    break;
                case 6:
                    str = "PUT";
                    break;
                case 7:
                    str = "TRACE";
                    break;
                default:
                    str = null;
                    break;
            }
            httpURLConnection.setRequestMethod(str);
            if (b2) {
                httpURLConnection.setReadTimeout(hUDHttpRequest.timeOut);
            }
            Map<String, List<String>> map = hUDHttpRequest.a;
            if (map != null) {
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    it2 = entry.getValue().iterator();
                    while (it2.hasNext()) {
                        httpURLConnection.addRequestProperty(entry.getKey(), it2.next());
                    }
                }
            }
            httpURLConnection.setRequestProperty("Connection", "close");
            if (hUDHttpRequest.getDoInput()) {
                httpURLConnection.setDoOutput(true);
                byte[] bArr = hUDHttpRequest.b;
                httpURLConnection.setFixedLengthStreamingMode(bArr.length);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(bArr);
                outputStream.close();
                //it = outputStream;
            } else {
                httpURLConnection.setDoOutput(false);
                //it = it2;
            }
            try {
                try {
                    httpURLConnection.connect();
                    if (b2) {
                        hUDHttpResponse = new HUDHttpResponse(httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage());
                        try {
                            try {
                                bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                            } catch (SocketTimeoutException e) {
                                try {
                                    HUDHttpResponse hUDHttpResponse2 = new HUDHttpResponse(408, "Request Timeout");
                                    try {
                                        httpURLConnection.disconnect();
                                        hUDHttpResponse = hUDHttpResponse2;
                                    } catch (Exception e2) {
                                        hUDHttpResponse = hUDHttpResponse2;
                                    }
                                    new StringBuilder("sendRequest(Response): Method=").append(hUDHttpRequest.requestMethod).append(" URL(host)=").append(hUDHttpRequest.uRL.getHost());
                                    return hUDHttpResponse;
                                } catch (Throwable th) {
                                    th = th;
                                    try {
                                        httpURLConnection.disconnect();
                                        throw th;
                                    } catch (Exception e3) {
                                    }
                                }
                            } catch (Exception e4) {
                                //it = hUDHttpResponse;
                                httpURLConnection.disconnect();
                                new StringBuilder("sendRequest(Response): Method=").append(hUDHttpRequest.requestMethod).append(" URL(host)=").append(hUDHttpRequest.uRL.getHost());
                                return hUDHttpResponse;
                            } catch (Throwable th2) {
                                //th = th2;
                                httpURLConnection.disconnect();
                                throw th2;
                            }
                        } catch (FileNotFoundException e5) {
                            bufferedInputStream = new BufferedInputStream(httpURLConnection.getErrorStream());
                        }
                        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(1024);
                        byte[] bArr2 = new byte[1024];
                        while (true) {
                            int read = bufferedInputStream.read(bArr2);
                            if (read != -1) {
                                byteArrayBuffer.append(bArr2, 0, read);
                            } else {
                                bufferedInputStream.close();
                                byte[] byteArray = byteArrayBuffer.toByteArray();
                                new StringBuilder("Received body size of ").append(byteArray.length);
                                hUDHttpResponse.a = httpURLConnection.getHeaderFields();
                                hUDHttpResponse.a(byteArray);
                            }
                        }
                    } else {
                        hUDHttpResponse = null;
                    }
                    //it = hUDHttpResponse;
                    httpURLConnection.disconnect();
                } catch (SocketTimeoutException e6) {
                    hUDHttpResponse = null;
                } catch (Exception e7) {
                    hUDHttpResponse = null;
                } catch (Throwable th3) {
                    //th = th3;
                    hUDHttpResponse = null;
                }
            } catch (Exception e8) {
                //hUDHttpResponse = it;
            }
        } catch (Exception e9) {
            hUDHttpResponse = null;
        }
        new StringBuilder("sendRequest(Response): Method=").append(hUDHttpRequest.requestMethod).append(" URL(host)=").append(hUDHttpRequest.uRL.getHost());
        return hUDHttpResponse;
    }

}
