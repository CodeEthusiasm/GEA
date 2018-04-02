package com.rug.gea.Client;

import com.rug.gea.Client.building.LocalBuilding;
import com.rug.gea.Client.building.RemoteBuilding;
import com.rug.gea.DataModels.Data;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class BuildingP2PTest {
    @Test
    public void testClientConnection() throws IOException {
        /* Create two buildings that generate data. */
        LocalBuilding building1 = new LocalBuilding();
        LocalBuilding building2 = new LocalBuilding();

        // Load the first data from the buildings
        AtomicReference<Data> building1SentModel = new AtomicReference<>();
        AtomicReference<Data> building2SentModel = new AtomicReference<>();

        building1.addListener((Data data) -> {
            if (building1SentModel.get() == null) building1SentModel.set(data);
        });
        building2.addListener((Data data) -> {
            if (building2SentModel.get() == null) building2SentModel.set(data);
        });

        // Create the clients
        BuildingP2PClient client = new BuildingP2PClient(2000);
        BuildingP2PClient client2 = new BuildingP2PClient(2001);

        // Load the remote buildings
        AtomicReference<RemoteBuilding> remoteBuilding1 = new AtomicReference<>();
        AtomicReference<RemoteBuilding> remoteBuilding2 = new AtomicReference<>();
        AtomicReference<Data> building1ReceivedModel = new AtomicReference<>();
        AtomicReference<Data> building2ReceivedModel = new AtomicReference<>();
        setupBuildingConnected(client, remoteBuilding1, building1ReceivedModel);
        setupBuildingConnected(client2, remoteBuilding2, building2ReceivedModel);

        client.connectToBuilding("localhost", 2001);

        // Sent data when it is generated
        building1.addListener(client::sendData);
        building2.addListener(client2::sendData);

        building1.start();
        building2.start();

        long timeout = 100;

        while (timeout > 0) {
            if (building1ReceivedModel.get() != null
                    && building2ReceivedModel.get() != null
                    && building1SentModel.get() != null
                    && building2SentModel.get() != null) {
                // Data has been received

                if (!building1ReceivedModel.get().equals(building2SentModel.get()))
                    throw new RuntimeException("Data not equal");
                if (!building2ReceivedModel.get().equals(building1SentModel.get()))
                    throw new RuntimeException("Data not equal");
                break;
            }
            // Wait
            try {
                Thread.sleep(100);
                timeout--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        building1.stop();
        building2.stop();
        client.stop();
        client2.stop();
    }

    private void setupBuildingConnected(BuildingP2PClient client2, AtomicReference<RemoteBuilding> remoteBuilding2, AtomicReference<Data> building2ReceivedModel) {
        client2.addOnBuildingConnectedListener(newValue -> {
            remoteBuilding2.set(newValue);
            newValue.addListener((Data model) -> {
                if (building2ReceivedModel.get() == null)
                    building2ReceivedModel.set(model);
            });
        });
    }
}
