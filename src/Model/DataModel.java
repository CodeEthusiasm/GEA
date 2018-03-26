package Model;

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
                "type :" + mBuildingType + "\n" +
                "consumption\n" +
                "electricity: " + mElecPerSqr + " kWh\n" +
                "gas: " + mGasPerSqr + " m^3";
    }
}
