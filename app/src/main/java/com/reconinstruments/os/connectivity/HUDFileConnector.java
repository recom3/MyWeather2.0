package com.reconinstruments.os.connectivity;

public class HUDFileConnector extends HUDConnector {
    public void run() {
        while (true) {
            if (!HUDConnectivityManager.mFileQueue.isEmpty()) {
                QueueMessage queueMessage = HUDConnectivityManager.mFileQueue.poll();
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