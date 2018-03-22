package Client;

import Client.building.LocalBuilding;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class BuildingClientTest {
    @Test
    void testClientConnection() throws IOException {
        /* Create two buildings that generate data. */
        LocalBuilding building1 = new LocalBuilding();
        LocalBuilding building2 = new LocalBuilding();

        BuildingP2PClient client = new BuildingP2PClient(2000);
        BuildingP2PClient client2 = new BuildingP2PClient(2001);
        client.connectToBuilding("localhost", 2001);

        building1.addListener(client::sendData);
        building2.addListener(client2::sendData);

        building1.start();
        building2.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        building1.stop();
        building2.stop();
        client.stop();
        client2.stop();
    }
}