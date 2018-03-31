package com.rug.gea.Client;

import com.rug.gea.Client.building.LocalBuilding;
import com.rug.gea.Client.building.RemoteBuilding;
import com.rug.gea.Model.Client;
import com.rug.gea.Model.DataModel;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientMain {

    public static void main(String argc[]) throws IOException {
        LocalBuilding building = new LocalBuilding();
        BuildingP2PClient p2pClient = new BuildingP2PClient(2000);
        List<DataModel> neighborUsage = new ArrayList<>();

        p2pClient.addOnBuildingConnectedListener(b -> {
            b.addListener(d -> {
                neighborUsage.add(d);
                double elec = 0, gas = 0;
                for (DataModel data : neighborUsage) {
                    elec += data.getElecPerSqr();
                    gas += data.getGasPerSqr();
                }
                System.out.println("Average electricity usage : " + elec + " kWh, gas usage : " + gas + " m^3");
            });
        });

        List<Client> clients = API.fetchData("jeongkyun");
        System.out.println("Neighbors are : ");
        InetAddress ip = InetAddress.getLocalHost();
        for (Client c : clients) {
            System.out.println(c);
            String[] parts = c.getConnectAddress().split(":");
            int port = Integer.valueOf(parts[1]);
            if (!ip.getHostAddress().equals(parts[0]) && port != 2000) {
                try {
                    p2pClient.connectToBuilding(parts[0], port);
                } catch (ConnectException e) {
                    e.printStackTrace();
                }
            }
        }
        building.addListener(p2pClient::sendData);
        building.start();
    }
}
