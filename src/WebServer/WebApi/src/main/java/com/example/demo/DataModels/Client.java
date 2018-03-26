package com.example.demo.DataModels;

import org.springframework.data.annotation.Id;

public class Client {

    public String address;
    public String zip;
    public int sqm;
    public String connectAddress;
    public String buildingType;

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }


    public Client() {
    }

    public Client(String address, String zip, int sqm, String connectAddress,String buildingType) {
        this.address = address;
        this.zip = zip;
        this.sqm = sqm;
        this.connectAddress = connectAddress;
        this.buildingType = buildingType;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getSqm() {
        return sqm;
    }

    public void setSqm(int sqm) {
        this.sqm = sqm;
    }

    public String getConnectAddress() {
        return connectAddress;
    }

    public void setConnectAddress(String connectAddress) {
        this.connectAddress = connectAddress;
    }

    @Override
    public String toString() {
        return "Client{" +
                "address='" + address + '\'' +
                ", zip='" + zip + '\'' +
                ", sqm=" + sqm +
                ", connectAddress='" + connectAddress + '\'' +
                ", buildingType='" + buildingType + '\'' +
                '}';
    }
}
