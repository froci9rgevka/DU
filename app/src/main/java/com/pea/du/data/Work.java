package com.pea.du.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.pea.du.db.local.data.Contract;
import com.pea.du.db.local.methods.ReadMethods;

import java.util.ArrayList;
import java.util.Date;

import static com.pea.du.db.local.data.Contract.GuestEntry.STAGE;
import static com.pea.du.db.local.data.Contract.GuestEntry.STAGE_TABLE_NAME;
import static com.pea.du.flags.Flags.currentContext;
import static com.pea.du.flags.Flags.workStageType;
import static com.pea.du.flags.Flags.workType;

public class Work implements Parcelable {
    private Integer id;
    private Integer serverId;
    private User user;
    private StaticValue address;
    private StaticValue name;
    private StaticValue stage;
    private String cnt;
    private StaticValue measure;
    private String descr;
    private Boolean subcontract;
    private StaticValue contractor;
    private Date docdate;

    public Work() {
    }

    public Work(Integer serverId) {
        this.serverId = serverId;
    }

    public Work(User user, StaticValue address, StaticValue name, StaticValue stage, String cnt, StaticValue measure, String descr, Boolean subcontract, StaticValue contractor) {
        this.user = user;
        this.address = address;
        this.name = name;
        this.stage = stage;
        this.cnt = cnt;
        this.measure = measure;
        this.descr = descr;
        this.subcontract = subcontract;
        this.contractor = contractor;
    }

    public Work(Work work) {
        this.id = work.getId();
        this.serverId = work.getServerId();
        this.user = work.getUser();
        this.address = work.getAddress();
        this.name = work.getName();
        this.stage = work.getStage();
        this.cnt = work.getCnt();
        this.measure = work.getMeasure();
        this.descr = work.getDescr();
        this.subcontract = work.getSubcontract();
        this.contractor = work.getContractor();
        this.docdate = work.getDocdate();
    }


    public Work(Parcel source) {
        final ClassLoader cl = getClass().getClassLoader();

        Object[] data = source.readArray(cl);

        id = (Integer) data[0];
        serverId = (Integer) data[1];
        user = (User) data[2];
        address = (StaticValue) data[3];
        name = (StaticValue) data[4];
        stage = (StaticValue) data[5];
        cnt = (String) data[6];
        measure = (StaticValue) data[7];
        descr = (String) data[8];
        subcontract = (Boolean) data[7];
        contractor = (StaticValue) data[10];
        docdate = (Date) data[11];
    }



    public static Work getWorkById(Integer id){
        ArrayList<Work> workArrayList = ReadMethods.getWorks(currentContext,
                Contract.GuestEntry.SERVER_ID + " = ?",
                new String[]{id.toString()});
        if (workArrayList.size() > 0)
            return (workArrayList.get(0));
        return null;
    }

    public static ArrayList<Work> getWorksByAddressAndUser(Integer addressId, Integer userId) {
        ArrayList<Work> workArrayList;
        if (workStageType==null)
            workArrayList = ReadMethods.getWorks(currentContext,
                    Contract.GuestEntry.ADDRESS_ID + " = ? AND " +
                            Contract.GuestEntry.USER_ID + " = ?",
                    new String[]{addressId.toString(), userId.toString()});
        else {
            StaticValue stage = new StaticValue(STAGE_TABLE_NAME,STAGE);
            stage.setName(workStageType);
            stage.getStaticByName(currentContext);
            workArrayList = ReadMethods.getWorks(currentContext,
                    Contract.GuestEntry.ADDRESS_ID + " = ? AND " +
                            Contract.GuestEntry.USER_ID + " = ? AND " +
                            Contract.GuestEntry.STAGE_ID + " = ?",
                    new String[]{addressId.toString(), userId.toString(), stage.getServerId().toString()});
        }
        return workArrayList;
    }

    ///////////////////////////////////////Parcelable/////////////////////////////////////////////////////////
    @Override
    public int describeContents() {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Object[] array = new Object[16];
        array[0] = id;
        array[1] = serverId;
        array[2] = user;
        array[3] = address;
        array[4] = name;
        array[5] = stage;
        array[6] = cnt;
        array[7] = measure;
        array[8] = descr;
        array[9] = subcontract;
        array[10] = contractor;
        array[11] = docdate;

        dest.writeArray(array);
    }

    public static final Parcelable.Creator<Work> CREATOR = new Parcelable.Creator<Work>() {

        @Override
        public Work createFromParcel(Parcel source) {
            return new Work(source);
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };

/////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public String toString() {
        if (id == null)
            return "";
        else
            return  stage.toString() + " работы: '" + name.toString() +
                    "'. Количество: " + cnt +
                    " " + measure +
                    ". Описание: " + descr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StaticValue getAddress() {
        return address;
    }

    public void setAddress(StaticValue address) {
        this.address = address;
    }

    public StaticValue getName() {
        return name;
    }

    public void setName(StaticValue name) {
        this.name = name;
    }

    public StaticValue getStage() {
        return stage;
    }

    public void setStage(StaticValue stage) {
        this.stage = stage;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public StaticValue getMeasure() {
        return measure;
    }

    public void setMeasure(StaticValue measure) {
        this.measure = measure;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Boolean getSubcontract() {
        return subcontract;
    }

    public void setSubcontract(Boolean subcontract) {
        this.subcontract = subcontract;
    }

    public StaticValue getContractor() {
        return contractor;
    }

    public void setContractor(StaticValue contractor) {
        this.contractor = contractor;
    }

    public Date getDocdate() {
        return docdate;
    }

    public void setDocdate(Date docdate) {
        this.docdate = docdate;
    }
}
