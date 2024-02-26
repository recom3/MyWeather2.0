package com.reconinstruments.os.connectivity;

public class HUDObjectConnector extends HUDConnector {
    public void run() {
        while (true) {
            if (!HUDConnectivityManager.mObjectQueue.isEmpty()) {
                QueueMessage queueMessage = HUDConnectivityManager.mObjectQueue.poll();
                if (queueMessage != null)
                    queueMessage.getHUDConnectivityCallBack().onCompleted(false);
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }
}