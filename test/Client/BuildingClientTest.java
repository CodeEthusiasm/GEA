package Client;


import org.junit.jupiter.api.Test;

import java.io.IOException;

public class BuildingClientTest {
    @Test
    public void testClientConnection() throws IOException {
        BuildingClient client = new BuildingClient(2000);
        BuildingClient client2 = new BuildingClient(2001);
        client.connectToBuilding("localhost", 2001);
        client.sendData(new byte[]{0, 1, 2});
        client2.sendData(new byte[]{2, 1, 0});
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.stop();
        client2.stop();
    }
}