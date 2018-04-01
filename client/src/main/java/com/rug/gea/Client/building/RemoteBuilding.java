package com.rug.gea.Client.building;

import com.rug.gea.DataModels.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class RemoteBuilding extends Building {
    private ArrayList<Data> data = new ArrayList<>();
    private ArrayList<OnDataReceivedListener> listeners = new ArrayList<>();

    public void addData(Data d) throws IOException, TimeoutException {
        data.add(d);
        for (OnDataReceivedListener listener : listeners)
            listener.onDataReceived(d);
    }

    public void addListener(OnDataReceivedListener listener) {
        listeners.add(listener);
    }
}
