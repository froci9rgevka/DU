package com.pea.du.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.pea.du.db.data.Contract;
import com.pea.du.db.methods.ReadMethods;

import java.util.ArrayList;
import java.util.List;

import static com.pea.du.db.data.Contract.GuestEntry.*;
import static com.pea.du.db.data.Contract.GuestEntry.DEFECT_MEASURE;
import static com.pea.du.db.data.Contract.GuestEntry.DEFECT_MEASURE_TABLE_NAME;
import static java.lang.Integer.parseInt;

public class Defect implements Parcelable{

    private Act act;
    private Integer id;
    private Integer serverId;
    private StaticValue type;
    private StaticValue constructiveElement;
    private String porch;
    private String floor;
    private String flat;
    private String description;
    private StaticValue measure;
    private String currency;

    public Defect()  {
    }

    public Defect(Defect defect) {
        this.act = new Act(defect.getAct());
        this.id = defect.getId();
        this.serverId = defect.getServerId();
        this.type = new StaticValue(defect.getType());
        this.constructiveElement = new StaticValue(defect.getConstructiveElement());
        this.porch = defect.getPorch();
        this.floor = defect.getFloor();
        this.flat = defect.getFlat();
        this.description = defect.getDescription();
        this.measure = new StaticValue(defect.getMeasure());
        this.currency = defect.getCurrency();
    }

    public Defect(Act act, StaticValue type, StaticValue constructiveElement, String porch, String floor, String flat, String description, StaticValue measure, String currency) {
        this.act = new Act(act);
        this.type = type;
        this.constructiveElement = constructiveElement;
        this.porch = porch;
        this.floor = floor;
        this.flat = flat;
        this.description = description;
        this.measure = measure;
        this.currency = currency;
    }

    public Defect(Parcel source) {
        final ClassLoader cl = getClass().getClassLoader();

        Object[] data = source.readArray(cl);

        act = (Act) data[0];
        id = (Integer) data[1];
        serverId = (Integer) data[2];
        type = (StaticValue) data[3];
        constructiveElement = (StaticValue) data[4];
        porch = (String) data[5];
        floor = (String) data[6];
        flat = (String) data[7];
        description = (String) data[8];
        measure = (StaticValue) data[9];
        currency = (String) data[10];
    }

    public static ArrayList<Defect> getDefectsByAct(Context context, Act act) {
        ArrayList<Defect> defectList = ReadMethods.getDefects(context,
                Contract.GuestEntry.ACT_ID + " = ?",
                new String[]{act.getServerId().toString()});
        return defectList;
    }

    public boolean isFilled(){
        if (type.equals("") ||  porch.equals("") || floor.equals("") || flat.equals(""))
            return false;
        else
            return true;
    }

    public static Defect fromStringList(List<String> list, Context context){
        Defect defect = new Defect();

        defect.setServerId(parseInt(list.get(0)));

        Act act = new Act();
        act.setServerId(parseInt(list.get(1)));
        defect.setAct(act);

        StaticValue name = new StaticValue(DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME, DEFECT_CONSTRUCTIVE_ELEMENT);
        name.setServerId(parseInt(list.get(2)));
        name.getStaticById(context);
        defect.setConstructiveElement(name);

        StaticValue type = new StaticValue(DEFECT_TYPE_TABLE_NAME, DEFECT_TYPE);
        type.setServerId(parseInt(list.get(3)));
        type.getStaticById(context);
        defect.setType(type);

        defect.setPorch(list.get(4));
        defect.setFloor(list.get(5));
        defect.setFlat(list.get(6));
        defect.setDescription(list.get(7));

        StaticValue measure = new StaticValue(DEFECT_MEASURE_TABLE_NAME, DEFECT_MEASURE);
        measure.setServerId(parseInt(list.get(8)));
        measure.getStaticById(context);
        defect.setMeasure(measure);

        defect.setCurrency(list.get(9));

        return  defect;
    }

///////////////////////////////////////Parcelable/////////////////////////////////////////////////////////
    @Override
    public int describeContents() {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Object[] array = new Object[11];
        array[0] = act;
        array[1] = id;
        array[2] = serverId;
        array[3] = type;
        array[4] = constructiveElement;
        array[5] = porch;
        array[6] = floor;
        array[7] = flat;
        array[8] = description;
        array[9] = measure;
        array[10] = currency;

        dest.writeArray(array);
    }

    public static final Parcelable.Creator<Defect> CREATOR = new Parcelable.Creator<Defect>() {

        @Override
        public Defect createFromParcel(Parcel source) {
            return new Defect(source);
        }

        @Override
        public Defect[] newArray(int size) {
            return new Defect[size];
        }
    };

/////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public String toString() {
        if (id == null)
            return "";
        else
            return constructiveElement.toString() +
                    ". Тип дефекта:" + type.toString() +
                    ". Парадная №" + porch +
                    ". Этаж " + floor +
                    ". Квартира " + flat + "."
                    ;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StaticValue getType() {
        return type;
    }

    public void setType(StaticValue type) {
        this.type = new StaticValue(type);
    }

    public String getPorch() {
        return porch;
    }

    public void setPorch(String porch) {
        this.porch = porch;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public StaticValue getConstructiveElement() {
        return constructiveElement;
    }

    public void setConstructiveElement(StaticValue constructiveElement) {
        this.constructiveElement = new StaticValue(constructiveElement);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StaticValue getMeasure() {
        return measure;
    }

    public void setMeasure(StaticValue measure) {
        this.measure = new StaticValue(measure);
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
