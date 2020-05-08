package com.rxn.compute;

import android.util.Log;

import com.orbitz.consul.Consul;

public class NetworkSearch extends Thread {
    Consul client;
    NetworkManager mNetworkManager;

    public NetworkSearch(NetworkManager networkManager) {
        mNetworkManager = networkManager;
    }

    public void run() {
        try {
            client = Consul.builder().withUrl("http://192.168.0.11:8500").build();
            Log.e(Constants.NETWORK_MANAGER_TAG, "CONNECTED TO CLUSTER");
        } catch (Exception e) {
            Log.e(Constants.NETWORK_MANAGER_TAG, "FAILURE TO CONNECT TO CLUSTER");
            client = null;
        }

        mNetworkManager.setClient(client);
    }
}
