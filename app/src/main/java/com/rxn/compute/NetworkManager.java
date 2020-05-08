package com.rxn.compute;

import android.util.Log;
import android.util.Pair;

import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.health.ServiceHealth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NetworkManager {
    private ArrayList<String> addresses;
    private Consul client;

    private List<ProcessorData> processors;

    private class ProcessData {
        private String name;
        private List<Pair<String, Integer>> objectPairs;

        public String getName() {
            return name;
        }

        public List<Pair<String, Integer>> getObjectPairs() {
            return objectPairs;
        }

        public ProcessData(String name, List<Pair<String, Integer>> objectPairs) {
            this.name = name;
            this.objectPairs = objectPairs;
        }
    }

    private class ProcessorData {
        private String name;
        private ArrayList<ProcessData> processes;
        private int expectedUtilization;
        private int currentUtilization;

        public int getCurrentUtilization() {
            return currentUtilization;
        }

        public void setCurrentUtilization(int currentUtilization) {
            this.currentUtilization = currentUtilization;
        }

        public int getExpectedUtilization() {
            return expectedUtilization;
        }

        public void setExpectedUtilization(int expectedUtilization) {
            this.expectedUtilization = expectedUtilization;
        }

        public String getName() {
            return name;
        }

        public List<ProcessData> getProcesses() {
            return processes;
        }

        public ProcessorData(String name, ArrayList<ProcessData> processes, int currentUtilization) {
            this.name = name;
            this.processes = processes;
            this.expectedUtilization = 0;
            this.currentUtilization = currentUtilization;
        }
    }

    public NetworkManager() {
        addresses = new ArrayList<>();

        this.client = null;
        new NetworkSearch(this).start();

        processors = new ArrayList<>();
    }

    public void setClient(Consul client) {
        this.client = client;
    }

    public ArrayList<String> findDevices() {
        addresses.clear();

        if (client == null) {
            return addresses;
        }

        HealthClient hc = client.healthClient();
        List<ServiceHealth> nodes = hc.getHealthyServiceInstances("compute").getResponse();

        // Parse JSON Data
        for (ServiceHealth node: nodes) {
            String address = node.getNode().getAddress();
            JSONObject nodeJson = new JSONObject(node.getNode().getNodeMeta().get());
            addresses.add(address);

            int utilization = 0;
            ArrayList<ProcessData> processes = new ArrayList<>();

            try {
                JSONArray processesArr = new JSONArray(nodeJson.get("processes").toString());
                utilization = nodeJson.getInt("utilization");

                for (int i = 0; i < processesArr.length(); i++) {
                    JSONObject process = processesArr.getJSONObject(i);
                    String processName = process.getString("name");
                    JSONArray objsJson = process.getJSONArray("objs");
                    List<Pair<String, Integer>> objectData = new ArrayList<>();

                    for (int j = 0; j < objsJson.length(); j++) {
                        JSONObject obj = objsJson.getJSONObject(j);
                        String objName = obj.getString("name");
                        int objUtil = obj.getInt("utilization");

                        Pair<String, Integer> objPair = new Pair<>(objName, objUtil);
                        objectData.add(objPair);
                    }

                    ProcessData processData = new ProcessData(processName, objectData);
                    processes.add(processData);
                }
            } catch (JSONException e) {
                // Error
                e.printStackTrace();
                Log.e(Constants.NETWORK_MANAGER_TAG, "JSON Read error");
            }

            ProcessorData processorData = new ProcessorData(address, processes, utilization);
            processors.add(processorData);
        }

        List<ProcessorData> rankedProcessors = processors;

        // LAAF Algorithm
        for (int i = 0; i < processors.size(); i++) {
            int currUtil = processors.get(i).getCurrentUtilization();
            processors.get(i).setExpectedUtilization(currUtil);

            int index = rankedProcessors.indexOf(processors.get(i));

            rankedProcessors.get(index).setExpectedUtilization(currUtil);
            List<ProcessData> processList = processors.get(i).getProcesses();

            for (int j = 0; j < processList.size(); j++) {
                List<Pair<String, Integer>> objList = processList.get(j).getObjectPairs();

                for (int  k = 0; k < objList.size(); k++) {
                    // Sort rankedProcessors
                    rankedProcessors.sort(new Comparator<ProcessorData>() {
                        @Override
                        public int compare(ProcessorData o1, ProcessorData o2) {
                            return (o1.getExpectedUtilization() -
                                    o2.getExpectedUtilization());
                        }
                    });

                    int load = objList.get(k).second
                            + rankedProcessors.get(0).getExpectedUtilization();
                    rankedProcessors.get(0).setExpectedUtilization(load);
                }
            }
        }

        ArrayList<String> sortedAddresses = new ArrayList<>();
        for (ProcessorData processorData: rankedProcessors) {
            sortedAddresses.add(processorData.getName());
        }

        addresses = sortedAddresses;

        return addresses;
    }
}
