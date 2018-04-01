package com.rug.gea.DataModels;

import java.io.*;

/**
 * Created by jk on 13/03/18.
 */
public class Data implements Serializable {

    private String type;
    private double gas;
    private double electricity;

    public Data(String type, int electricity, int gas, int size) {
        this.type = type;
        this.electricity = electricity / (double)size;
        this.gas = gas / (double)size;
    }

    public String getType() {
        return type;
    }

    public void setType(String buildingType) {
        type = buildingType;
    }


    public double getGas() {
        return gas;
    }

    public void setGas(int gasPerSqr) {
        this.gas = gasPerSqr;
    }

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(int elecPerSqr) {
        this.electricity = elecPerSqr;
    }

    @Override
    public boolean equals(Object obj) {
        Data data2;
        try {
            data2 = (Data) obj;
        } catch (ClassCastException e) {
            return false;
        }
        return data2.type.equals(type)
                && data2.electricity == electricity
                && data2.gas == gas;
    }

    @Override
    public String toString() {
        return "electricity: " + electricity + " kWh" + ", gas: " + gas + " m^3";
    }
}
