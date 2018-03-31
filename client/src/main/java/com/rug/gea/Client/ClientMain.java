package com.rug.gea.Client;

import com.rug.gea.Client.building.LocalBuilding;
import com.rug.gea.Model.Client;
import com.rug.gea.Model.DataModel;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientMain {

    private static final int INDEX_ELECTRICITY = 0;
    private static final int INDEX_GAS = 1;
    private static final String ZIP = "9714BN";
    private static final int PORT = 2000;

    public static double[] getAverage(List<DataModel> data) {
        double elec = 0, gas = 0;
        for (DataModel d : data) {
            elec += d.getElecPerSqr();
            gas += d.getGasPerSqr();
        }
        elec /= data.size();
        gas /= data.size();
        return new double[]{elec, gas};
    }

    public static void main(String argc[]) throws IOException {
        LocalBuilding building = new LocalBuilding();
        List<DataModel> myUsage = new ArrayList<>();
        building.addListener(d -> {
            myUsage.add(d);
            double[] average = getAverage(myUsage);
            System.out.println("My Usage-> electricity : " + average[INDEX_ELECTRICITY] + " kWh, gas : " + average[INDEX_GAS] + "m^3");
        });

        BuildingP2PClient p2pClient = new BuildingP2PClient(PORT);
        BuildingClient buildingClient = new BuildingClient(building, ZIP);

        List<DataModel> neighborUsage = new ArrayList<>();
        List<Client> clients = buildingClient.getClients();
        buildingClient.addListener(() -> System.out.println("clients are updated"));

        p2pClient.addOnBuildingConnectedListener(b -> {
            b.addListener(d -> {
                neighborUsage.add(d);
                double[] average = getAverage(neighborUsage);
                System.out.println("Average electricity usage : " + average[INDEX_ELECTRICITY] + " kWh, gas usage : " + average[INDEX_GAS] + " m^3");
            });
        });

        System.out.println("Neighbors are : ");
        InetAddress ip = InetAddress.getLocalHost();
        for (Client c : clients) {
            System.out.println(c);
            String[] parts = c.getConnectAddress().split(":");
            int port = Integer.valueOf(parts[1]);
            if (!ip.getHostAddress().equals(parts[0]) && port != PORT) {
                try {
                    p2pClient.connectToBuilding(parts[0], port);
                } catch (ConnectException e) {
                    System.err.println("Other systems are not started yet");
                }
            }
        }
        building.addListener(p2pClient::sendData);
        building.start();
    }
}
