package com.pea.du.data;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.pea.du.db.local.data.Contract;
import com.pea.du.db.local.methods.ReadMethods;

import java.util.ArrayList;

public class StaticValue implements Parcelable {
    private Integer id;
    private String name;
    private Integer serverId;
    private String tableName;
    private String columnName;

    public StaticValue() {
    }

    public StaticValue(Integer serverId) {
        this.serverId = serverId;
    }

    public StaticValue(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public StaticValue(StaticValue staticValue){
        this.name = staticValue.getName();
        this.serverId = staticValue.getServerId();
        this.tableName = staticValue.getTableName();
        this.columnName = staticValue.getColumnName();
    }

    public StaticValue(String name, Integer serverId, String tableName, String columnName) {
        this.name = name;
        this.serverId = serverId;
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public StaticValue(Integer serverId, String tableName, String columnName) {
        this.name = name;
        this.serverId = serverId;
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public StaticValue(Parcel source) {
        final ClassLoader cl = getClass().getClassLoader();

        java.lang.Object[] data = source.readArray(cl);

        serverId = (Integer) data[0];
        id = (Integer) data[1];
        name = (String) data[2];
        tableName = (String) data[3];
        columnName = (String) data[4];

    }

    public static String getNameById(Context context, String tableName, Integer id){
        ArrayList<StaticValue> staticValueArrayList = ReadMethods.getStaticValues(context, tableName,
                Contract.GuestEntry.SERVER_ID + " = ?",
                new String[]{id.toString()});
        if (staticValueArrayList.size() > 0)
            return staticValueArrayList.get(0).getName();
        else
            return null;
    }

    public void assignStaticValue(StaticValue staticValue){
        this.setServerId(staticValue.getServerId());
        this.setId(staticValue.getId());
        this.setName(staticValue.getName());
    }

    public void getStaticById(Context context){
        ArrayList<StaticValue> staticValueArrayList = ReadMethods.getStaticValues(context, tableName,
                Contract.GuestEntry.SERVER_ID + " = ?",
                new String[]{serverId.toString()});
        if (staticValueArrayList.size() > 0)
            assignStaticValue(staticValueArrayList.get(0));
    }

    public void getStaticByName(Context context){
        ArrayList<StaticValue> staticValueArrayList = ReadMethods.getStaticValues(context, tableName,
                columnName + " = ?",
                new String[]{name});
        if (staticValueArrayList.size() > 0)
            assignStaticValue(staticValueArrayList.get(0));
    }



    ///////////////////////////////////////Parcelable/////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        java.lang.Object[] array = new java.lang.Object[5];;

        array[0] = serverId;
        array[1] = id;
        array[2] = name;
        array[3] = tableName;
        array[4] = columnName;

        dest.writeArray(array);
    }

    public static final Parcelable.Creator<StaticValue> CREATOR = new Parcelable.Creator<StaticValue>() {

        @Override
        public StaticValue createFromParcel(Parcel source) {
            return new StaticValue(source);
        }

        @Override
        public StaticValue[] newArray(int size) {
            return new StaticValue[size];
        }
    };

/////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StaticValue staticValue = (StaticValue) o;

        return name != null ? name.equals(staticValue.name) : staticValue.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
