package com.rug.gea.Client.building;

import com.rug.gea.Model.DataModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class RemoteBuilding extends Building {
    private ArrayList<DataModel> data = new ArrayList<>();
    private ArrayList<OnDataReceivedListener> listeners = new ArrayList<>();

    public void addData(DataModel d) throws IOException, TimeoutException {
        data.add(d);
        for (OnDataReceivedListener listener : listeners)
            listener.onDataReceived(d);
    }

    public void addListener(OnDataReceivedListener listener) {
        listeners.add(listener);
    }
}
