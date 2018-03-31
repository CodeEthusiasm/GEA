package com.rug.gea.Model;

import java.io.*;

/**
 * Created by jk on 13/03/18.
 */
public class DataModel implements Serializable {

    private String mBuildingType;
    private double mElecPerSqr;
    private double mGasPerSqr;

    public DataModel(String type, int electricity, int gas, int size) {
        this.mBuildingType = type;
        this.mElecPerSqr = electricity / (double)size;
        this.mGasPerSqr = gas / (double)size;
    }

    public String getBuildingType() {
        return mBuildingType;
    }

    public void setBuildingType(String buildingType) {
        mBuildingType = buildingType;
    }

    public double getElecPerSqr() {
        return mElecPerSqr;
    }

    public void setElecPerSqr(int elecPerSqr) {
        this.mElecPerSqr = elecPerSqr;
    }

    public double getGasPerSqr() {
        return mGasPerSqr;
    }

    public void setGasPerSqr(int gasPerSqr) {
        this.mGasPerSqr = gasPerSqr;
    }

    @Override
    public boolean equals(Object obj) {
        DataModel dataModel2;
        try {
            dataModel2 = (DataModel) obj;
        } catch (ClassCastException e) {
            return false;
        }
        return dataModel2.mBuildingType.equals(mBuildingType)
                && dataModel2.mElecPerSqr == mElecPerSqr
                && dataModel2.mGasPerSqr == mGasPerSqr;
    }

    @Override
    public String toString() {
        return "electricity: " + mElecPerSqr + " kWh" + ", gas: " + mGasPerSqr + " m^3";
    }
}
