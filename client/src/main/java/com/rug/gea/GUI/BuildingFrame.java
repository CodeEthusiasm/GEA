package com.rug.gea.GUI;

import com.rug.gea.Client.BuildingClient;
import com.rug.gea.Client.BuildingP2PClient;
import com.rug.gea.Client.building.LocalBuilding;
import com.rug.gea.DataModels.Client;
import com.rug.gea.DataModels.Data;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class BuildingFrame extends JFrame {

    private static final int INDEX_GAS = 0;
    private static final int INDEX_ELECTRICITY = 1;
    private static final String ZIP = "9714BN";
    private static final int PORT = 2000;

    private double[] localUsage = new double[]{0, 0};
    private double[] remoteUsage = new double[]{0, 0};

    private LocalBuilding building;
    private BuildingP2PClient p2pClient;

    private static double[] getAverage(List<Data> data) {
        double elec = 0, gas = 0;
        for (Data d : data) {
            elec += d.getElectricity();
            gas += d.getGas();
        }
        elec /= data.size();
        gas /= data.size();
        return new double[]{elec, gas};
    }

    /**
     * Setting up the buildings and clients
     * @param panel JPanel contents of JFrame
     * @throws IOException if it encounters a problem
     */
    private void setup(final BuildingPanel panel) throws IOException {
        building = new LocalBuilding();
        java.util.List<Data> myUsage = new ArrayList<>();
        building.addListener(d -> {
            myUsage.add(d);
            double[] average = getAverage(myUsage);
            localUsage[INDEX_ELECTRICITY] = average[INDEX_ELECTRICITY];
            localUsage[INDEX_GAS] = average[INDEX_GAS];
            panel.setMyGas(localUsage[INDEX_GAS]);
            panel.setMyElec(localUsage[INDEX_ELECTRICITY]);
            System.out.println("My Usage-> electricity : " + average[INDEX_ELECTRICITY] + " kWh, gas : " + average[INDEX_GAS] + "m^3");
        });

        p2pClient = new BuildingP2PClient(PORT);
        BuildingClient buildingClient = new BuildingClient(building, ZIP);

        java.util.List<Data> neighborUsage = new ArrayList<>();
        List<Client> clients = buildingClient.getClients();
        panel.setNeighborList((ArrayList<Client>) clients);
        buildingClient.addListener(() -> panel.setNeighborList((ArrayList<Client>) buildingClient.getClients()));

        p2pClient.addOnBuildingConnectedListener(b -> {
            b.addListener(d -> {
                neighborUsage.add(d);
                double[] average = getAverage(neighborUsage);
                remoteUsage[INDEX_ELECTRICITY] = average[INDEX_ELECTRICITY];
                remoteUsage[INDEX_GAS] = average[INDEX_GAS];
                panel.setNeighborGas(remoteUsage[INDEX_GAS]);
                panel.setNeighborElec(remoteUsage[INDEX_ELECTRICITY]);
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

    public void close() {
        building.stop();
        p2pClient.stop();
    }

    public BuildingFrame() throws HeadlessException, IOException {
        super();
        BuildingPanel panel = new BuildingPanel();
        panel.setMyElec(0);
        panel.setMyGas(0);
        panel.setNeighborElec(0);
        panel.setNeighborGas(0);
        setup(panel);
        this.setTitle("Compare with neighbors");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.add(panel);
        this.setVisible(true);
    }
}