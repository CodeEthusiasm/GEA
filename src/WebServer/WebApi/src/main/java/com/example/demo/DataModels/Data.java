package com.example.demo.DataModels;

public class Data {
    String type;
    int gas;
    int electricity;
    int size;

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
