package com.rug.gea.Client.building;

import java.util.ArrayList;

public class RemoteBuilding extends Building {
    private ArrayList<Data> data = new ArrayList<>();
    private ArrayList<OnDataReceivedListener> listeners = new ArrayList<>();
    private boolean shouldRun = false;

    public RemoteBuilding(int port) {

    }

    public void addData(Data d) {
        data.add(d);
        for (OnDataReceivedListener listener : listeners)
            listener.onDataReceived(d);
    }

    public void addListener(OnDataReceivedListener listener) {
        listeners.add(listener);
    }
}
