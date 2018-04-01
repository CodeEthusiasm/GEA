package com.rug.gea.DataModels;

public class Data {
    String type;
    int gas;
    int electricity;
    int size;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getGas() {
        return gas;
    }

    public void setGas(int gas) {
        this.gas = gas;
    }

    public int getElectricity() {
        return electricity;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Data{" +
                "type='" + type + '\'' +
                ", gas=" + gas +
                ", electricity=" + electricity +
                ", size=" + size +
                '}'+"\n";
    }

}
