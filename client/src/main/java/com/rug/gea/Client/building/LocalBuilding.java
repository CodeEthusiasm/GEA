package com.rug.gea.Client.building;

import com.rug.gea.DataModels.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 *  A building that generates data from time to time.
 **/
public class LocalBuilding extends Building {

    private ArrayList<Data> data = new ArrayList<>();
    private ArrayList<OnDataReceivedListener> listeners = new ArrayList<>();
    private boolean shouldRun = false;

    public void addListener(OnDataReceivedListener listener) {
        listeners.add(listener);
    }

    private void addData(Data d) throws IOException, TimeoutException {
        data.add(d);
        for (OnDataReceivedListener listener : listeners)
            listener.onDataReceived(d);
    }

    public void start() {
        if (shouldRun)
            throw new RuntimeException("main.java.com.rug.gea.Client.building.Building is already running");
        shouldRun = true;
        new Thread(() -> {
            while (shouldRun) {
                try {
                    addData(getRandomData());
                    Thread.sleep(1000);
                } catch (InterruptedException | TimeoutException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static Data getRandomData() {
        Random r = new Random();
        return new Data("house",
                (r.nextInt() & Integer.MAX_VALUE) % 1000 + 50,
                (r.nextInt() & Integer.MAX_VALUE) % 100 + 50,
                (r.nextInt() & Integer.MAX_VALUE) % 100 + 20);
    }

    public void stop() {
        if (!shouldRun)
            throw new RuntimeException("main.java.com.rug.gea.Client.building.Building is not running");
        shouldRun = false;
    }

    @Override
    public Data[] getData() {
        return (Data[]) data.toArray();
    }
}
