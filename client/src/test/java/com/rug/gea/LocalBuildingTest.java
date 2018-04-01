package com.rug.gea;

import com.rug.gea.Client.building.LocalBuilding;
import com.rug.gea.DataModels.Data;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class LocalBuildingTest {
    @Test
    /**
     * Tests whether the local building will generate data.
     */
    public void testLocalBuildingDataGeneration() {
        LocalBuilding building = new LocalBuilding();
        AtomicBoolean receivedData = new AtomicBoolean(false);
        building.addListener((Data d) -> receivedData.set(true));
        building.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!receivedData.get())
            throw new RuntimeException("Receiver is taking too long to receive data");
        building.stop();
    }
}
