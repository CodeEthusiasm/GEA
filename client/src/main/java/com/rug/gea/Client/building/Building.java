package com.rug.gea.Client.building;

import com.rug.gea.Model.DataModel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class Building {
    public interface OnDataReceivedListener {
        void onDataReceived(DataModel d) throws IOException, TimeoutException;
    }

    private DataModel[] data;

    public DataModel[] getData() {
        return data;
    }

    public abstract void addListener(OnDataReceivedListener listener);
}
