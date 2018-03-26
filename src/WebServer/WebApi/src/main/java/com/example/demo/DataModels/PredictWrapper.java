package com.example.demo.DataModels;

public class PredictWrapper {
    int sqm;
    String type;

    public PredictWrapper() {
        sqm = 0;
        type = "";
    }

    public PredictWrapper(int sqm, String type) {
        this.sqm = sqm;
        this.type = type;
    }

    public int getSqm() {
        return sqm;
    }

    public void setSqm(int sqm) {
        this.sqm = sqm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PredictWrapper{" +
                "sqm=" + sqm +
                ", type='" + type + '\'' +
                '}';
    }
}
