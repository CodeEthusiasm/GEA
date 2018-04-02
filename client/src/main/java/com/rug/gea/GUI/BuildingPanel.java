package com.rug.gea.GUI;

import com.rug.gea.DataModels.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Basic panel to demonstrate exchanging data
 */
public class BuildingPanel extends JPanel {

    private JTextField myGas;
    private JTextField myElec;
    private JTextField neighborGas;
    private JTextField neighborElec;
    private JTextField neighborList;

    private double neighborgas = 0;
    private double neighborelec = 0;

    public BuildingPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JTextField myusage = new JTextField("My Usage");
        myusage.setEditable(false);
        myusage.setAlignmentX(Component.CENTER_ALIGNMENT);
        myGas = new JTextField();
        myGas.setEditable(false);
        myGas.setAlignmentX(Component.CENTER_ALIGNMENT);
        myGas.setBackground(Color.WHITE);
        myElec = new JTextField();
        myElec.setEditable(false);
        myElec.setAlignmentX(Component.CENTER_ALIGNMENT);
        myElec.setBackground(Color.WHITE);

        JTextField neighborUsage = new JTextField("Neighbor Usage");
        neighborUsage.setEditable(false);
        neighborUsage.setAlignmentX(Component.CENTER_ALIGNMENT);
        neighborGas = new JTextField();
        neighborGas.setEditable(false);
        neighborGas.setAlignmentX(Component.CENTER_ALIGNMENT);
        neighborGas.setBackground(Color.WHITE);
        neighborElec = new JTextField();
        neighborElec.setEditable(false);
        neighborElec.setAlignmentX(Component.CENTER_ALIGNMENT);
        neighborElec.setBackground(Color.WHITE);

        JTextField list = new JTextField("Who are your neighbors?");
        list.setEditable(false);
        list.setAlignmentX(Component.CENTER_ALIGNMENT);
        neighborList = new JTextField();
        neighborList.setEditable(false);
        neighborList.setAlignmentX(Component.CENTER_ALIGNMENT);
        neighborList.setBackground(Color.WHITE);

        this.add(myusage);
        this.add(myGas);
        this.add(myElec);
        this.add(neighborUsage);
        this.add(neighborGas);
        this.add(neighborElec);
        this.add(list);
        this.add(neighborList);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setVisible(true);
    }

    public void setMyGas(double myGas) {
        this.myGas.setText("[    Gas    ] " + myGas + " m^3");
        if (myGas > neighborgas && neighborgas != 0) {
            this.myGas.setBackground(Color.RED);
        } else {
            this.myGas.setBackground(Color.GREEN);
        }
    }

    public void setMyElec(double myElec) {
        this.myElec.setText("[Electricity] " + myElec + " kWh");
        if (myElec > neighborelec && neighborelec != 0) {
            this.myElec.setBackground(Color.RED);
        } else {
            this.myElec.setBackground(Color.GREEN);
        }
    }

    public void setNeighborGas(double neighborGas) {
        this.neighborgas = neighborGas;
        this.neighborGas.setText("[    Gas    ] " + neighborGas + " m^3");
    }

    public void setNeighborElec(double neighborElec) {
        this.neighborelec = neighborElec;
        this.neighborElec.setText("[Electricity] " + neighborElec + " kWh");
    }

    public void setNeighborList(ArrayList<Client> clients) {
        StringBuilder builder = new StringBuilder();
        for (Client c : clients) {
            builder.append(c.getAddress()).append('\n');
        }
        this.neighborList.setText(builder.toString());
    }
}