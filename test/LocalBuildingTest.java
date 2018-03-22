import Client.building.Data;
import Client.building.LocalBuilding;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

class LocalBuildingTest {
    @Test
    void testLocalBuildingDataGeneration() {
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
