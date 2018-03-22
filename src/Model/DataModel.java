package Model;

import java.io.*;
import java.util.Date;

/**
 * Created by jk on 13/03/18.
 */
public class DataModel implements Serializable {

    private Date mDate;
    private BuildingType mBuildingType;
    private int mElectricity;
    private int mGas;
    private int mSquareMeter;

    public DataModel(Date date, BuildingType type, int electricity, int gas, int size) {
        this.mDate = date;
        this.mBuildingType = type;
        this.mElectricity = electricity;
        this.mGas = gas;
        this.mSquareMeter = size;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public BuildingType getBuildingType() {
        return mBuildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        mBuildingType = buildingType;
    }

    public int getElectricity() {
        return mElectricity;
    }

    public void setElectricity(int electricity) {
        this.mElectricity = electricity;
    }

    public int getGas() {
        return mGas;
    }

    public void setGas(int gas) {
        this.mGas = gas;
    }

    public int getSquareMeter() {
        return mSquareMeter;
    }

    public void setSquareMeter(int squareMeter) {
        mSquareMeter = squareMeter;
    }

    public static byte[] serialize(DataModel data) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(data);
        return byteOut.toByteArray();
    }

    public static DataModel deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return (DataModel)objIn.readObject();
    }

    @Override
    public String toString() {
        return "\n" +
                mDate.toString() + "\n" +
                "type :" + mBuildingType + "\n" +
                "consumption\n" +
                "electricity: " + mElectricity + " kWh\n" +
                "gas: " + mGas + " m^3";
    }
}
