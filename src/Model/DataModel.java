package Model;

import java.io.*;
import java.util.Date;

/**
 * Created by jk on 13/03/18.
 */
public class DataModel implements Serializable {

    private Date mDate;
    private String mBuildingType;
    private int mElectricity;
    private int mGas;

    public DataModel(Date date, String type, int electricity, int gas) {
        this.mDate = date;
        this.mBuildingType = type;
        this.mElectricity = electricity;
        this.mGas = gas;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getBuildingType() {
        return mBuildingType;
    }

    public void setBuildingType(String buildingType) {
        mBuildingType = buildingType;
    }

    public int getmElectricity() {
        return mElectricity;
    }

    public void setmElectricity(int electricity) {
        this.mElectricity = electricity;
    }

    public int getmGas() {
        return mGas;
    }

    public void setmGas(int gas) {
        this.mGas = gas;
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
