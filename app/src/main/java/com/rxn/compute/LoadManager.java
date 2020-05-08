package com.rxn.compute;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class LoadManager extends Thread{
    private MainActivity activity;
    private boolean isRunning;
    private NetworkManager networkManager;
    private ArrayList<String> addresses;
    private String currentServer;

    private int FPS = 30;
    private double avgFPS;

    ZContext context;
    ZMQ.Socket socket;

    private final static int REQUEST_TIMEOUT = 2500;
    private final static int REQUEST_RETRIES = 3;

    public LoadManager(MainActivity activity) {
        super();
        this.activity = activity;
        networkManager = new NetworkManager();
        this.addresses = new ArrayList<>();
        context = new ZContext();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean run) {
        isRunning = run;
    }

    public void run() {
        long startTime;
        long waitTime;
        long timeMillis;
        long totalTime = 0;
        long frameCount = 0;
        long targetTime = 1000/FPS;

        while (isRunning) {
            startTime = System.nanoTime();

            if (hasAvailableResources()) {
                // Send work to server
                if (socket == null)
                    connectToServer(addresses.get(0));

                sendWork();
            }
            else {
                addresses = networkManager.findDevices();
                setText("Work from local");

                try {
                    this.sleep(1000);
                } catch (InterruptedException e) {
                    continue;
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            if (waitTime < 0) {
                waitTime = 0;
            }

            try {
                this.sleep(waitTime);
            } catch(Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if (frameCount == FPS) {
                avgFPS = 100 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    private void connectToServer(String address) {
        socket = context.createSocket(SocketType.REQ);
        assert(socket != null);
        String host = "tcp://" + address + ":9001";
        socket.connect(host);
        currentServer = address;
    }

    private void sendWork() {
        int retriesLeft = REQUEST_RETRIES;

        ZMQ.Poller poller = context.createPoller(1);
        poller.register(socket, ZMQ.Poller.POLLIN);

        String request = "Work";
        socket.send(request.getBytes(ZMQ.CHARSET), 0);

        int expect_reply = 1;
        while (expect_reply > 0) {
            int rc = poller.poll(REQUEST_TIMEOUT);
            if (rc == -1)
                break;

            if (poller.pollin(0)) {
                String reply = socket.recvStr();
                if (reply == null)
                    break;
                if (reply.equals("Work remote")) {
                    retriesLeft = REQUEST_RETRIES;
                    expect_reply = 0;
                    String responseText = reply + " from " + currentServer;
                    setText(responseText);
                }
                else {
                    Log.e(Constants.LOAD_MANAGER_TAG, "Malformed message from server");
                }
            }
            else if (--retriesLeft == 0) {
                Log.e(Constants.LOAD_MANAGER_TAG, "Server is offline");
                addresses.clear();
                poller.unregister(socket);
                context.destroySocket(socket);
                socket = null;
                currentServer = "";
                break;
            }
            else {
                poller.unregister(socket);
                context.destroySocket(socket);

                socket = context.createSocket(SocketType.REQ);
                String host = "tcp://" + currentServer + ":9001";
                socket.connect(host);
                poller.register(socket, ZMQ.Poller.POLLIN);
                socket.send(request);
            }
        }
    }

    public void setText(String text) {
        activity.setResponseText(text);
    }

    private boolean hasAvailableResources() {
        return (addresses.size() > 0);
    }
}
