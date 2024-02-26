package com.reconinstruments.os.connectivity.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import com.reconinstruments.os.connectivity.IHUDConnectivity;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class HUDBTBaseService implements IHUDBTService {
    protected final String a = getClass().getSuperclass().getSimpleName();

    protected BluetoothAdapter b = null;

    protected final IHUDConnectivity c;

    protected IHUDConnectivity.ConnectionState d;

    protected final String TAG = getClass().getSuperclass().getSimpleName();

    protected String e = "NULL";

    protected final ArrayList<IHUDBTConsumer> f = new ArrayList<IHUDBTConsumer>();

    public abstract class OutStreamWriter {

        /* renamed from: a  reason: collision with root package name */
        protected final BlockingQueue<OutputStreamContainer> f2728a;

        public OutStreamWriter(ArrayBlockingQueue<OutputStreamContainer> arrayBlockingQueue) {
            this.f2728a = arrayBlockingQueue;
        }

        public final OutputStreamContainer a() throws InterruptedException {
            new StringBuilder("OutputStreamPool obtain: ").append(this.f2728a.size());
            return this.f2728a.take();
        }

        public final void a(OutputStreamContainer outputStreamContainer) throws InterruptedException {
            this.f2728a.put(outputStreamContainer);
            new StringBuilder("OutputStreamPool release: ").append(this.f2728a.size());
        }
    }

    public class OutputStreamContainer {

        /* renamed from: a  reason: collision with root package name */
        protected OutputStream f2730a;
        private int c = -1;
        private int d = -1;

        public OutputStreamContainer(OutputStream outputStream) {
            this.f2730a = outputStream;
        }

        public final OutputStream a() {
            return this.f2730a;
        }
    }

    public HUDBTBaseService(IHUDConnectivity paramIHUDConnectivity) throws Exception {
        if (paramIHUDConnectivity == null)
            throw new NullPointerException("HUDSPPService Constructor can't have null values");
        this.c = paramIHUDConnectivity;
        this.b = BluetoothAdapter.getDefaultAdapter();
        if (this.b == null) {
            throw new Exception("HUDSPPService: BluetoothAdapter.getDefaultAdapter() is null, is your Bluetooth Off?");
        }
        this.b.isEnabled();
        this.d = IHUDConnectivity.ConnectionState.DISCONNECTED;
    }

    public final IHUDConnectivity.ConnectionState getConnectionState() {
        IHUDConnectivity.ConnectionState connectionState;
        synchronized (this) {
            connectionState = this.d;
        }
        return connectionState;
    }

    public void a(IHUDBTConsumer paramIHUDBTConsumer) {
        synchronized (this.f) {
            if (this.f.contains(paramIHUDBTConsumer))
                return;
            this.f.add(paramIHUDBTConsumer);
            return;
        }
    }

    public final void setConnState(String str, IHUDConnectivity.ConnectionState connectionState) {
        synchronized (this) {
            new StringBuilder("setState(").append(str).append(") ").append(this.d).append(" -> ").append(connectionState);
            this.d = connectionState;
            this.c.onConnectionStateChanged(connectionState);
        }
    }
}
